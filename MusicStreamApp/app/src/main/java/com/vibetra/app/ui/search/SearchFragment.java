package com.vibetra.app.ui.search;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vibetra.app.BuildConfig;
import com.vibetra.app.R;
import com.vibetra.app.adapters.TrackAdapter;
import com.vibetra.app.data.model.Track;
import com.vibetra.app.utils.AdManager;
import com.vibetra.app.utils.PlayerLauncher;
import com.vibetra.app.utils.PlaylistDialogHelper;
import com.vibetra.app.utils.Result;
import com.vibetra.app.utils.SearchHistory;
import com.vibetra.app.viewmodel.FavoritesViewModel;
import com.vibetra.app.viewmodel.SearchViewModel;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;

import android.util.Log;

import com.vibetra.app.utils.Constants;

import java.util.List;

public class SearchFragment extends Fragment {

    private SearchViewModel searchViewModel;
    private FavoritesViewModel favoritesViewModel;
    private AdManager adManager;
    private SearchHistory searchHistory;
    private TrackAdapter adapter;
    private RecentSearchAdapter recentAdapter;

    private android.widget.EditText etSearch;
    private ImageButton btnClear;
    private TextWatcher searchTextWatcher;
    private RecyclerView rvRecentSearches;
    private RecyclerView rvSearch;
    private ProgressBar progressSearch;
    private TextView tvNoResults;
    private TextView tvEmptyHistory;
    private TextView tvClearHistory;
    private View emptyStateScroll;
    private View searchResultsContainer;
    private TextView tvYouTubeAttribution;
    private Handler searchHandler;
    private Runnable searchRunnable;
    private static final long SEARCH_DELAY = 500;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        favoritesViewModel = new ViewModelProvider(requireActivity()).get(FavoritesViewModel.class);
        adManager = new AdManager(requireContext());
        searchHistory = new SearchHistory(requireContext());
        searchHandler = new Handler(Looper.getMainLooper());

        etSearch = view.findViewById(R.id.etSearch);
        btnClear = view.findViewById(R.id.btnClear);
        rvRecentSearches = view.findViewById(R.id.rvRecentSearches);
        rvSearch = view.findViewById(R.id.rvSearch);
        progressSearch = view.findViewById(R.id.progressSearch);
        tvNoResults = view.findViewById(R.id.tvNoResults);
        tvEmptyHistory = view.findViewById(R.id.tvEmptyHistory);
        tvClearHistory = view.findViewById(R.id.tvClearHistory);
        emptyStateScroll = view.findViewById(R.id.emptyStateScroll);
        searchResultsContainer = view.findViewById(R.id.searchResultsContainer);
        tvYouTubeAttribution = view.findViewById(R.id.tvYouTubeAttribution);

        setupSearchBar();
        setupRecentSearches();
        setupResults();
        setupBanner(view);
        observeData();
    }

    private void setupBanner(View view) {
        AdView adView = view.findViewById(R.id.adViewSearch);
        if (BuildConfig.ADMOB_BANNER_UNIT_ID == null || BuildConfig.ADMOB_BANNER_UNIT_ID.trim().isEmpty()) {
            adView.setVisibility(View.GONE);
            return;
        }
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (BuildConfig.DEBUG) Log.d("ADS_SEARCH", "Banner loaded successfully");
            }
            @Override
            public void onAdFailedToLoad(LoadAdError error) {
                if (BuildConfig.DEBUG) Log.e("ADS_SEARCH", "Banner failed: " + error.getCode() + " - " + error.getMessage());
            }
        });
        adView.loadAd(new AdRequest.Builder().build());
    }

    private void setupSearchBar() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnClear.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
                if (s.length() > 0) {
                    scheduleSearch(s.toString());
                } else {
                    showEmptyState();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnClear.setOnClickListener(v -> etSearch.setText(""));

        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            String query = etSearch.getText().toString().trim();
            if (!query.isEmpty()) {
                executeSearch(query);
            }
            return true;
        });
    }

    private void scheduleSearch(String query) {
        searchHandler.removeCallbacks(searchRunnable);
        searchRunnable = () -> executeSearch(query);
        searchHandler.postDelayed(searchRunnable, SEARCH_DELAY);
    }

    private void executeSearch(String query) {
        searchHistory.addSearch(query);
        showSearchResults();
        searchViewModel.search(query);
    }

    private void setupRecentSearches() {
        rvRecentSearches.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        recentAdapter = new RecentSearchAdapter(new RecentSearchAdapter.InteractionListener() {
            @Override
            public void onItemClick(String query) {
                etSearch.setText(query);
                executeSearch(query);
            }

            @Override
            public void onRemoveClick(String search) {
                searchHistory.removeSearch(search);
                loadRecentSearches();
            }
        });
        rvRecentSearches.setAdapter(recentAdapter);
        tvClearHistory.setOnClickListener(v -> {
            searchHistory.clear();
            loadRecentSearches();
        });
        loadRecentSearches();
    }

    private void loadRecentSearches() {
        List<String> searches = searchHistory.getSearches();
        tvClearHistory.setVisibility(searches.isEmpty() ? View.GONE : View.VISIBLE);
        if (searches.isEmpty()) {
            tvEmptyHistory.setVisibility(View.VISIBLE);
            recentAdapter.submitList(searches);
        } else {
            tvEmptyHistory.setVisibility(View.GONE);
            recentAdapter.submitList(searches);
        }
    }

    private void setupResults() {
        adapter = new TrackAdapter(new TrackAdapter.Listener() {
            @Override
            public void onPlay(Track track, int position) {
                List<Track> tracks = adapter.getItems();
                if (track.getStreamUrl() == null || track.getStreamUrl().isEmpty()) {
                    Toast.makeText(requireContext(), R.string.youtube_fallback_note, Toast.LENGTH_SHORT).show();
                    return;
                }
                adManager.incrementPlayCount();
                if (adManager.shouldShowInterstitial()) {
                    adManager.showInterstitialIfReady(requireActivity());
                }
                PlayerLauncher.playTrackList(requireContext(), tracks, position, adManager.isHdUnlocked());
            }

            @Override
            public void onFavorite(Track track) {
                favoritesViewModel.toggleFavorite(track);
            }

            @Override
            public void onAddToPlaylist(Track track) {
                PlaylistDialogHelper.showAddToPlaylistDialog(requireContext(), track);
            }
        }, true, true, true);

        rvSearch.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvSearch.setAdapter(adapter);
    }

    private void showEmptyState() {
        emptyStateScroll.setVisibility(View.VISIBLE);
        searchResultsContainer.setVisibility(View.GONE);
        loadRecentSearches();
    }

    private void showSearchResults() {
        emptyStateScroll.setVisibility(View.GONE);
        searchResultsContainer.setVisibility(View.VISIBLE);
    }

    private void observeData() {
        searchViewModel.getSearchResult().observe(getViewLifecycleOwner(), result -> {
            if (result == null) {
                return;
            }
            if (result.getStatus() == Result.Status.LOADING) {
                progressSearch.setVisibility(View.VISIBLE);
                rvSearch.setVisibility(View.GONE);
                tvNoResults.setVisibility(View.GONE);
            } else if (result.getStatus() == Result.Status.SUCCESS) {
                progressSearch.setVisibility(View.GONE);
                boolean isEmpty = result.getData() == null || result.getData().isEmpty();
                rvSearch.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
                tvNoResults.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
                if (!isEmpty) {
                    adapter.submitList(result.getData());
                    boolean hasYouTube = result.getData().stream()
                            .anyMatch(t -> Constants.TRACK_SOURCE_YOUTUBE.equals(t.getSource()));
                    tvYouTubeAttribution.setVisibility(hasYouTube ? View.VISIBLE : View.GONE);
                }
            } else {
                progressSearch.setVisibility(View.GONE);
                rvSearch.setVisibility(View.GONE);
                tvNoResults.setVisibility(View.VISIBLE);
                tvNoResults.setText(result.getError() != null ? result.getError() : getString(R.string.no_results));
            }
        });

        favoritesViewModel.getToastMessage().observe(getViewLifecycleOwner(), message -> {
            if (message != null) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        searchHandler.removeCallbacks(searchRunnable);
    }
}
