package com.volive.mrhow.util;


import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.volive.mrhow.R;
import com.volive.mrhow.activities.VideoPlayerActivity;

import java.util.List;

public class BackgroundSoundService extends Service {
    private final IBinder mBinder = new LocalBinder();
    String videourl = "", course_title = "";
    private SimpleExoPlayer player;
    private PlayerNotificationManager playerNotificationManager;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onDestroy() {
        releasePlayer();
        super.onDestroy();
    }

    private void releasePlayer() {
        if (player != null) {
            playerNotificationManager.setPlayer(null);
            player.release();
            player = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public SimpleExoPlayer getplayerInstance() {
        if (player == null) {
            startPlayer();
        }
        return player;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        videourl = intent.getStringExtra("videourl");
        course_title = intent.getStringExtra("course_title");
        if (player == null) {
            startPlayer();
        }
        return START_STICKY;
    }

    private void startPlayer() {
        final Context context = this;
        Uri uri = Uri.parse(videourl);
        player = ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector());
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, getString(R.string.app_name)));


        ConcatenatingMediaSource concatenatingMediaSource = new ConcatenatingMediaSource();

        MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).
                createMediaSource(uri);

        concatenatingMediaSource.addMediaSource(mediaSource);
        player.prepare(mediaSource);
        player.setPlayWhenReady(true);

        showNotificationMethod(context);

    }

    private void showNotificationMethod(final Context context) {
        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(context, AppConstants.PLAYBACK_CHANNEL_ID,
                R.string.playback_channel_name,
                AppConstants.PLAYBACK_NOTIFICATION_ID,
                new PlayerNotificationManager.MediaDescriptionAdapter() {
                    @Override
                    public String getCurrentContentTitle(Player player) {
                        String title = "MrHow";
//                        return item.getTitle();
                        return title;
                    }

                    @Nullable
                    @Override
                    public PendingIntent createCurrentContentIntent(Player player) {
                        Intent intent = new Intent(context, VideoPlayerActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(videourl, videourl);

//                       intent.putExtra(course_title, course_title);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

//                        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        return  pendingIntent;

                    }

                    @Nullable
                    @Override
                    public String getCurrentContentText(Player player) {
//                        String summary = "MrHowSummary";
//                        return item.getSummary();
                        return course_title;
                    }

                    @Nullable
                    @Override
                    public Bitmap getCurrentLargeIcon(Player player, PlayerNotificationManager.BitmapCallback callback) {
                        return null;
                    }
                }
        );
        playerNotificationManager.setNotificationListener(new PlayerNotificationManager.NotificationListener() {
            @Override
            public void onNotificationStarted(int notificationId, Notification notification) {

                startForeground(notificationId, notification);
            }

            @Override
            public void onNotificationCancelled(int notificationId) {
                stopSelf();
            }
        });

        playerNotificationManager.setPlayer(player);
        playerNotificationManager.setStopAction(null);

    }

    public boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    public class LocalBinder extends Binder {
        public BackgroundSoundService getService() {
            return BackgroundSoundService.this;
        }
    }

}