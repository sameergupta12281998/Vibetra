# Project specific ProGuard rules.

# ---- Retrofit + OkHttp ----
-dontwarn okhttp3.**
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# ---- Gson / JSON model DTOs ----
-keepattributes *Annotation*
-keep class com.google.gson.** { *; }
-keep class com.example.musicstreamapp.data.network.dto.** { *; }
-keep class com.example.musicstreamapp.data.model.** { *; }

# ---- Room ----
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao interface *
-dontwarn androidx.room.**

# ---- Google Mobile Ads (AdMob) ----
-keep class com.google.android.gms.ads.** { *; }
-dontwarn com.google.android.gms.**

# ---- ExoPlayer ----
-keep class com.google.android.exoplayer2.** { *; }
-dontwarn com.google.android.exoplayer2.**

# ---- Glide ----
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.AppGlideModule { *; }
-dontwarn com.bumptech.glide.**
