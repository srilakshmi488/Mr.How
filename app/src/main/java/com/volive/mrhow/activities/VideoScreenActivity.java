package com.volive.mrhow.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.gson.JsonElement;
import com.volive.mrhow.R;
import com.volive.mrhow.util.ApiClass;
import com.volive.mrhow.util.MainUrl;
import com.volive.mrhow.util.NetworkConnection;
import com.volive.mrhow.util.PreferenceUtils;
import com.volive.mrhow.util.RetrofitClass;

import org.json.JSONObject;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoScreenActivity extends AppCompatActivity {

    Button button_signin, button_signup;
    NetworkConnection networkConnection;
    PreferenceUtils preferenceUtils;
    SimpleExoPlayerView exoPlayerView;
    SimpleExoPlayer exoPlayer;
    String newVideoUrl,languageToLoad,language;
    ImageView image_home,image_language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_screen);

        preferenceUtils = new PreferenceUtils(VideoScreenActivity.this);
        networkConnection = new NetworkConnection(VideoScreenActivity.this);
        language = preferenceUtils.getStringFromPreference(PreferenceUtils.Language,"");

        initializeUI();
        initializeValues();


    }



    private void initializeUI() {
        button_signin = findViewById(R.id.button_signin);
        button_signup = findViewById(R.id.button_signup);
        image_home = findViewById(R.id.image_home);
        image_language = findViewById(R.id.image_language);
    }

    private void initializeValues() {
        button_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VideoScreenActivity.this, LogInActivity.class));
//                finish();
            }
        });

        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VideoScreenActivity.this, SignUp.class));
//                finish();
            }
        });

        image_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeDialog();
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        if(exoPlayerView==null){
            exoPlayerView = (SimpleExoPlayerView) findViewById(R.id.exo_player_view);
            exoPlayerView.setUseController(false);
            if(networkConnection.isConnectingToInternet()) {
                welcomeVideo();
            }else {
                Toast.makeText(VideoScreenActivity.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void welcomeVideo() {
        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement> call=apiClass.welcomevideo(MainUrl.apikey,language);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Log.e("sdfdg f",response.body().toString());

                if(response.isSuccessful()){
                    try {

                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String status = jsonObject.getString("status");
                        String base_url= jsonObject.getString("base_url");
                        if(status.equalsIgnoreCase("1")) {
                            JSONObject dataobject = jsonObject.getJSONObject("data");
                            String video = dataobject.getString("video");
                            String type = dataobject.getString("type");

                            if(type.equalsIgnoreCase("video")){
                                image_home.setVisibility(View.GONE);
                                exoPlayerView.setVisibility(View.VISIBLE);
                            }else {
                                image_home.setVisibility(View.VISIBLE);
                                exoPlayerView.setVisibility(View.GONE);
                                Glide.with(VideoScreenActivity.this).load(base_url+video).into(image_home);
                            }

                           newVideoUrl=base_url+video;

                            playVideo(newVideoUrl);

                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e("dsfjdshf ",t.toString());

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        releasePlayer();
        super.onDestroy();
    }


    private void releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    private void playVideo(String newVideoUrl) {
        try {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
            Uri videoURI = Uri.parse(newVideoUrl);
            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null);
            exoPlayerView.setPlayer(exoPlayer);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);
        } catch (Exception e) {
            Log.e("jhjghjhj", e.toString());
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void ChangeDialog() {
        final Dialog dialog = new Dialog(VideoScreenActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.change_dialog);
        dialog.setCanceledOnTouchOutside(false);
        Button button_english = (Button) dialog.findViewById(R.id.button_english);
        Button button_arabic = (Button) dialog.findViewById(R.id.button_arabic);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        button_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                languageToLoad = "en"; // your language
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getResources().updateConfiguration(config, getResources().getDisplayMetrics());
                preferenceUtils.setLanguage("en");
                Intent intent = new Intent(VideoScreenActivity.this, VideoScreenActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();


            }
        });

        button_arabic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                languageToLoad = "ar"; // your language
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getResources().updateConfiguration(config, getResources().getDisplayMetrics());
                preferenceUtils.setLanguage("ar");
                Intent intent = new Intent(VideoScreenActivity.this, VideoScreenActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                finish();
            }
        });

    }

}
