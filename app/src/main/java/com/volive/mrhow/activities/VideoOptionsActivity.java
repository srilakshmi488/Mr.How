package com.volive.mrhow.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.JsonElement;
import com.volive.mrhow.R;
import com.volive.mrhow.util.ApiClass;
import com.volive.mrhow.util.MainUrl;
import com.volive.mrhow.util.NetworkConnection;
import com.volive.mrhow.util.PreferenceUtils;
import com.volive.mrhow.util.RetrofitClass;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoOptionsActivity extends AppCompatActivity {
    LinearLayout video_quality;
    ImageView image_continuevideo,image_back;
    String  userid = "",background_video="",strbackgroundvideo="",language="";
    PreferenceUtils preferenceUtils;
    NetworkConnection networkConnection;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_options);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//only working in api level 26
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.lightgreen));
        }

        preferenceUtils = new PreferenceUtils(VideoOptionsActivity.this);
        networkConnection = new NetworkConnection(VideoOptionsActivity.this);
        language = preferenceUtils.getStringFromPreference(PreferenceUtils.Language,"");

        userid = preferenceUtils.getStringFromPreference(PreferenceUtils.userid, "");
        background_video = getIntent().getStringExtra("background_video");

        initializeUI();
        initializeValues();


    }

    private void initializeValues() {
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        video_quality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), VideoQualityActivity.class));
            }
        });

        image_continuevideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (background_video.equalsIgnoreCase("1")) {
                    image_continuevideo.setImageResource(R.drawable.off1);
                    strbackgroundvideo = "0";
                } else if (background_video.equalsIgnoreCase("0")) {
                    image_continuevideo.setImageResource(R.drawable.on);
                    strbackgroundvideo = "1";

                }
                userSettings();
            }
        });

    }


    private void initializeUI() {
        image_back = findViewById(R.id.image_back);
        video_quality = findViewById(R.id.video_quality);
        image_continuevideo = findViewById(R.id.image_continuevideo);

        if (background_video.equalsIgnoreCase("1")) {
            image_continuevideo.setImageResource(R.drawable.on);
        } else {
            image_continuevideo.setImageResource(R.drawable.off1);
        }
    }

    private void userSettings() {
        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement> call = apiClass.usersettings(userid, strbackgroundvideo, "1","1",
                MainUrl.apikey,language);

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Log.e("usersettings ", response.body().toString());
                if (response.isSuccessful()) {
                    try {

                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        if (status.equalsIgnoreCase("1")) {

                            JSONObject dataobject = jsonObject.getJSONObject("data");
                            String user_id = dataobject.getString("user_id");
                            String name = dataobject.getString("name");
                            String email = dataobject.getString("email");
                            String phone = dataobject.getString("phone");
                            String gender = dataobject.getString("gender");
                            String profile_pic = dataobject.getString("profile_pic");
                            String video_quality = dataobject.getString("video_quality");
                            String wifi_only = dataobject.getString("wifi_only");
                             background_video = dataobject.getString("background_video");
                             String trainer_updates = dataobject.getString("trainer_updates");
                            String recommendations = dataobject.getString("recommendations");

                            preferenceUtils.saveString(PreferenceUtils.backgroundvideo, background_video);

                            if (background_video.equalsIgnoreCase("1")) {
                                image_continuevideo.setImageResource(R.drawable.on);
                            } else {
                                image_continuevideo.setImageResource(R.drawable.off1);
                            }

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("dhfdb", e.toString());
                    }


                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e("khgf ", t.toString());

            }
        });
    }

}
