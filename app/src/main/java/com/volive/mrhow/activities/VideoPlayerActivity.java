package com.volive.mrhow.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.volive.mrhow.R;
import com.volive.mrhow.fragments.ProfileFragment;
import com.volive.mrhow.util.BackgroundSoundService;
import com.volive.mrhow.util.MainUrl;
import com.volive.mrhow.util.PreferenceUtils;


public class VideoPlayerActivity extends AppCompatActivity {
    PlayerView playerView;
    SimpleExoPlayer exoPlayer;
    String videourl = "", course_title = "";
    String backgroundvideo = "";
    long playerPosition;
    private BackgroundSoundService mService;
    private boolean mBound = false;
    private Intent intent;
    PreferenceUtils preferenceUtils;


    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            BackgroundSoundService.LocalBinder binder = (BackgroundSoundService.LocalBinder) iBinder;
            mService = binder.getService();
            mBound = true;
            initializePlayer();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        preferenceUtils = new PreferenceUtils(VideoPlayerActivity.this);

        String cover = getIntent().getStringExtra("cover");
        course_title = getIntent().getStringExtra("course_title");
//        backgroundvideo = ProfileFragment.background_video;
        backgroundvideo=preferenceUtils.getStringFromPreference(PreferenceUtils.backgroundvideo,"");
        System.out.println("sdhgfdjg " + backgroundvideo);

        videourl = MainUrl.imageurl + cover;
        playerView = findViewById(R.id.playerView);

        if (backgroundvideo.equalsIgnoreCase("1")) {
            intent = new Intent(VideoPlayerActivity.this, BackgroundSoundService.class);
            intent.putExtra("videourl", videourl);
            intent.putExtra("course_title", course_title);
            startService(intent);

            playerView.setUseController(true);
            playerView.showController();
            playerView.setControllerAutoShow(true);
            playerView.setControllerHideOnTouch(false);

        } else {
//            playVideo();
            setUp();
        }

    }

    private void initializePlayer() {
        if (backgroundvideo.equalsIgnoreCase("1")) {
            if (mBound) {
                exoPlayer = mService.getplayerInstance();
                playerView.setPlayer(exoPlayer);
            }
        } else {
            if (exoPlayer == null) {
                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
                TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
                // 2. Create the player
                exoPlayer = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(this), trackSelector);
                playerView.setPlayer(exoPlayer);


            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (backgroundvideo.equalsIgnoreCase("1")) {
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
            initializePlayer();
        } else {
            if (exoPlayer == null) {
                setUp();
            }

            exoPlayer.seekTo(playerPosition);
            exoPlayer.setPlayWhenReady(true);


        }
    }


    @Override
    protected void onResume() {
        if (backgroundvideo.equalsIgnoreCase("1")) {

        } else {
//            if (exoPlayer != null) {
//                exoPlayer.setPlayWhenReady(true);
//                exoPlayer.getPlaybackState();
//            }

//            playVideo();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (backgroundvideo.equalsIgnoreCase("1")) {

        } else {
//            if (exoPlayer != null) {
////                position = exoPlayer.getCurrentPosition();
//                exoPlayer.setPlayWhenReady(false);
//                exoPlayer.getPlaybackState();
//            }

        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onStop() {
        if (backgroundvideo.equalsIgnoreCase("1")) {
            unbindService(mConnection);
            mBound = false;
        } else {
//            if (exoPlayer != null) {
//                exoPlayer.stop();
//                exoPlayer.release();
//                exoPlayer = null;
//            }

            playerPosition = exoPlayer.getCurrentPosition();
            exoPlayer.setPlayWhenReady(false);


        }
        super.onStop();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (backgroundvideo.equalsIgnoreCase("1")) {
            Intent myService = new Intent(VideoPlayerActivity.this, BackgroundSoundService.class);
            stopService(myService);
        } else {
            if (exoPlayer != null) {
                exoPlayer.release();
                exoPlayer = null;
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (backgroundvideo.equalsIgnoreCase("1")) {
            Intent myService = new Intent(VideoPlayerActivity.this, BackgroundSoundService.class);
            stopService(myService);
            finish();

//            Intent intent = new Intent(VideoPlayerActivity.this, ViewDetailsActivity.class);
//            intent.putExtra("courseid", ViewDetailsActivity.courseid);
//            startActivity(intent);

        }
    }

    private void setUp() {
        initializePlayer();
        if (videourl == null) {
            return;
        }
        buildMediaSource(Uri.parse(videourl));
    }

    private void buildMediaSource(Uri mUri) {
        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, getString(R.string.app_name)), bandwidthMeter);
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(mUri);
        // Prepare the player with the source.
        exoPlayer.prepare(videoSource);
        exoPlayer.setPlayWhenReady(true);

    }

    private void playVideo() {
        try {

            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
//            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
            Uri videoURI = Uri.parse(videourl);
            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null);
            playerView.setPlayer(exoPlayer);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);
        } catch (Exception e) {
            Log.e("jhjghjhj", e.toString());
        }

    }

}



