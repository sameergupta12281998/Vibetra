# MusicStreamApp (Java, Android)

An Android music streaming app built with Java and public, legal music APIs.

## Legal Notice

- Vibetra is an independent application and is not affiliated with, endorsed by, or sponsored by Spotify, YouTube, Google, Audius, or Jamendo.
- All music, artwork, and metadata belong to their respective owners and/or licensors.
- The app does not provide tools to download, rip, or extract copyrighted audio from third-party platforms.
- YouTube API data is used for metadata/search fallback only; playback is limited to tracks with permitted direct stream URLs.

## Features

- Home screen with:
  - Trending songs (Audius)
  - Popular tracks
  - Albums section
- Search screen:
  - Audius search first
  - YouTube Data API metadata fallback
- Music player:
  - ExoPlayer streaming
  - Play/Pause, Next/Previous, Seek
  - Foreground playback service
  - Notification controls
- Favorites:
  - Local persistence with Room
- Playlist:
  - Create local playlists
  - Add/remove tracks
- Monetization:
  - AdMob banner on Home
  - Interstitial every 3 plays
  - Rewarded ad to unlock HD mode flag

## Stack

- Java
- MVVM (ViewModel + LiveData)
- Retrofit + Gson
- Room DB
- ExoPlayer
- XML UI
- AdMob

## API Notes

### Audius API (Primary)

Base URL used: `https://api.audius.co/`

Endpoints:
- `/v1/tracks/trending`
- `/v1/tracks/search`

Streams are played from legal Audius track stream endpoints.

### YouTube Data API (Fallback)

Used for search fallback metadata only (title/artist/thumbnail).

Important: YouTube Data API does not provide direct legal audio stream URLs for arbitrary videos. In this app, fallback results are shown for discovery, but playback is enabled only when a direct stream URL exists (Audius tracks).

## Setup Guide

1. Open the project in Android Studio.
2. Ensure Android SDK for `compileSdk 34` is installed.
3. Add these entries to `~/.gradle/gradle.properties` (or project `gradle.properties`):

```properties
YOUTUBE_API_KEY=YOUR_YOUTUBE_DATA_API_KEY
ADMOB_BANNER_UNIT_ID=ca-app-pub-3940256099942544/6300978111
ADMOB_INTERSTITIAL_UNIT_ID=ca-app-pub-3940256099942544/1033173712
ADMOB_REWARDED_UNIT_ID=ca-app-pub-3940256099942544/5224354917
```

4. Sync Gradle.
5. Run app on a device/emulator with internet enabled.
6. Grant notification permission on Android 13+ for playback notifications.

## AdMob

The default AdMob IDs are Google test IDs. Replace with your own production ad unit IDs before release.

## Build

If your project does not contain Gradle Wrapper files, generate them once from Android Studio:

- `File > Sync Project with Gradle Files`
- or run Gradle tasks from the IDE

Then build:

```bash
./gradlew :app:assembleDebug
```

## Package Highlights

- `data/network`: Retrofit APIs and DTOs
- `data/repository`: Network + DB repositories
- `data/db`: Room entities, DAOs, database
- `viewmodel`: Home/Search/Favorites/Playlist state
- `player`: ExoPlayer manager and foreground service
- `ui/*`: Fragments and player screen
- `adapters`: RecyclerView adapters

## Edge Case Handling

- No internet: home screen shows retry error message
- Broken streams: ExoPlayer retry behavior in player manager
- Background playback: foreground service + notification controls

## Future Enhancements

- Better stream quality switching with multi-bitrate endpoints
- Download for offline playback (where license allows)
- Equalizer and audio effects
- Improved playlist sorting/filtering
