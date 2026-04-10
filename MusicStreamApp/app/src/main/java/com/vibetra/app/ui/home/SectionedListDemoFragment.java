package com.vibetra.app.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vibetra.app.R;
import com.vibetra.app.adapters.Section;
import com.vibetra.app.adapters.SectionedRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SectionedListDemoFragment extends Fragment {

    private SectionedRecyclerViewAdapter<String> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sectioned_demo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rv = view.findViewById(R.id.rvSectionedList);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new SectionedRecyclerViewAdapter<>(this::onItemClick);
        rv.setAdapter(adapter);

        setupData();
    }

    private void setupData() {
        List<Section<?>> sections = new ArrayList<>();

        // Horizontal carousel section
        List<String> trendingTracks = Arrays.asList(
                "Track 1 - Artist A",
                "Track 2 - Artist B",
                "Track 3 - Artist C",
                "Track 4 - Artist D",
                "Track 5 - Artist E"
        );
        sections.add(new Section<>("Trending Now", trendingTracks, Section.TYPE_HORIZONTAL_LIST));

        // Vertical list section
        List<String> newReleases = Arrays.asList(
                "New Release 1",
                "New Release 2",
                "New Release 3",
                "New Release 4"
        );
        sections.add(new Section<>("New Releases", newReleases, Section.TYPE_VERTICAL_LIST));

        // Grid section
        List<String> playlists = Arrays.asList(
                "Workout Mix",
                "Chill Vibes",
                "Party Time",
                "Romantic Hits"
        );
        sections.add(new Section<>("Popular Playlists", playlists, Section.TYPE_GRID));

        // Another horizontal section
        List<String> recommendations = Arrays.asList(
                "Recommended 1",
                "Recommended 2",
                "Recommended 3",
                "Recommended 4"
        );
        sections.add(new Section<>("Recommended For You", recommendations, Section.TYPE_HORIZONTAL_LIST));

        adapter.setData(sections);
    }

    private void onItemClick(String item, int position) {
        Toast.makeText(requireContext(), "Clicked: " + item, Toast.LENGTH_SHORT).show();
    }
}
