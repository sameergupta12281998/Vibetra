package com.vibetra.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class SearchHistory {
    private static final String PREF_NAME = "search_history";
    private static final String KEY_SEARCHES = "recent_searches";
    private static final int MAX_HISTORY = 10;

    private final SharedPreferences prefs;

    public SearchHistory(Context context) {
        this.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void addSearch(String query) {
        if (query == null || query.trim().isEmpty()) {
            return;
        }
        query = query.trim();
        List<String> searches = getSearches();
        searches.remove(query);
        searches.add(0, query);
        if (searches.size() > MAX_HISTORY) {
            searches = searches.subList(0, MAX_HISTORY);
        }
        prefs.edit()
                .putString(KEY_SEARCHES, String.join(",", searches))
                .apply();
    }

    public List<String> getSearches() {
        String data = prefs.getString(KEY_SEARCHES, "");
        List<String> result = new ArrayList<>();
        if (data.isEmpty()) {
            return result;
        }
        for (String s : data.split(",")) {
            if (!s.isEmpty()) {
                result.add(s);
            }
        }
        return result;
    }

    public void removeSearch(String query) {
        if (query == null || query.trim().isEmpty()) {
            return;
        }
        List<String> searches = getSearches();
        searches.remove(query.trim());
        prefs.edit()
                .putString(KEY_SEARCHES, String.join(",", searches))
                .apply();
    }

    public void clear() {
        prefs.edit().remove(KEY_SEARCHES).apply();
    }
}
