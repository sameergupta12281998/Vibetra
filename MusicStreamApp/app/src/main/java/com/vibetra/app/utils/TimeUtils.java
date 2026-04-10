package com.vibetra.app.utils;

import java.util.Locale;

public final class TimeUtils {

    private TimeUtils() {
    }

    public static String formatDuration(long seconds) {
        long min = seconds / 60;
        long sec = seconds % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", min, sec);
    }

    public static String formatMillis(long millis) {
        long totalSeconds = Math.max(0, millis / 1000);
        return formatDuration(totalSeconds);
    }
}
