package com.vibetra.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.vibetra.app.BuildConfig;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class AdManager {

    private final SharedPreferences prefs;
    private InterstitialAd interstitialAd;
    private RewardedAd rewardedAd;

    public interface RewardCallback {
        void onRewarded();

        void onClosed();
    }

    public AdManager(Context context) {
        this.prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        loadInterstitial(context);
        loadRewarded(context);
    }

    public void incrementPlayCount() {
        int count = prefs.getInt(Constants.PREF_PLAY_COUNT, 0) + 1;
        prefs.edit().putInt(Constants.PREF_PLAY_COUNT, count).apply();
    }

    public boolean shouldShowInterstitial() {
        int count = prefs.getInt(Constants.PREF_PLAY_COUNT, 0);
        return count > 0 && count % 3 == 0;
    }

    public void showInterstitialIfReady(Activity activity) {
        if (interstitialAd != null) {
            interstitialAd.show(activity);
            interstitialAd = null;
            loadInterstitial(activity);
        }
    }

    public void showRewarded(Activity activity, RewardCallback callback) {
        if (rewardedAd == null) {
            callback.onClosed();
            loadRewarded(activity);
            return;
        }
        rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdDismissedFullScreenContent() {
                callback.onClosed();
                loadRewarded(activity);
            }
        });
        rewardedAd.show(activity, rewardItem -> {
            unlockHdForOneHour();
            callback.onRewarded();
        });
        rewardedAd = null;
    }

    public boolean isHdUnlocked() {
        long until = prefs.getLong(Constants.PREF_HD_UNLOCKED_UNTIL, 0L);
        return System.currentTimeMillis() < until;
    }

    private void unlockHdForOneHour() {
        long unlockUntil = System.currentTimeMillis() + (60L * 60L * 1000L);
        prefs.edit().putLong(Constants.PREF_HD_UNLOCKED_UNTIL, unlockUntil).apply();
    }

    private void loadInterstitial(Context context) {
        if (BuildConfig.DEBUG) Log.d("ADS_MANAGER", "Loading interstitial with unit ID: " + BuildConfig.ADMOB_INTERSTITIAL_UNIT_ID);
        AdRequest request = new AdRequest.Builder().build();
        InterstitialAd.load(context, BuildConfig.ADMOB_INTERSTITIAL_UNIT_ID, request, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(InterstitialAd ad) {
                if (BuildConfig.DEBUG) Log.d("ADS_MANAGER", "Interstitial loaded successfully");
                interstitialAd = ad;
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                if (BuildConfig.DEBUG) Log.e("ADS_MANAGER", "Interstitial failed: " + loadAdError.getCode() + " - " + loadAdError.getMessage());
                interstitialAd = null;
            }
        });
    }

    private void loadRewarded(Context context) {
        if (BuildConfig.DEBUG) Log.d("ADS_MANAGER", "Loading rewarded with unit ID: " + BuildConfig.ADMOB_REWARDED_UNIT_ID);
        AdRequest request = new AdRequest.Builder().build();
        RewardedAd.load(context, BuildConfig.ADMOB_REWARDED_UNIT_ID, request, new RewardedAdLoadCallback() {
            @Override
            public void onAdLoaded(RewardedAd ad) {
                if (BuildConfig.DEBUG) Log.d("ADS_MANAGER", "Rewarded loaded successfully");
                rewardedAd = ad;
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                if (BuildConfig.DEBUG) Log.e("ADS_MANAGER", "Rewarded failed: " + loadAdError.getCode() + " - " + loadAdError.getMessage());
                rewardedAd = null;
            }
        });
    }
}
