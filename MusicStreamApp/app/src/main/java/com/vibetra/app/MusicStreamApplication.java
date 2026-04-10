package com.vibetra.app;

import android.app.Application;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;

import java.util.Arrays;

public class MusicStreamApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // TEST DEVICE — shows real-looking test ads on your physical device.
        // Remove this block before releasing to Play Store.
        if (BuildConfig.DEBUG) {
            MobileAds.setRequestConfiguration(
                new RequestConfiguration.Builder()
                    .setTestDeviceIds(Arrays.asList("CHECKSUM"))
                    .build()
            );
        }
        MobileAds.initialize(this, initializationStatus -> {
            // No-op.
        });
    }
}
