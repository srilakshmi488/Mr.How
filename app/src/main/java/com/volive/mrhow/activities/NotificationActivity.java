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

public class NotificationActivity extends AppCompatActivity {
    ImageView image_back, image_personalized, image_followtrainer;
    String trainer_updates = "", recommendations = "", userid = "", strtrainerupdates = "", strrecommendations = "";
    PreferenceUtils preferenceUtils;
    NetworkConnection networkConnection;
    String language="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//only working in api level 26
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.lightgreen));
        }

        preferenceUtils = new PreferenceUtils(NotificationActivity.this);
        networkConnection = new NetworkConnection(NotificationActivity.this);
        language = preferenceUtils.getStringFromPreference(PreferenceUtils.Language,"");

        userid = preferenceUtils.getStringFromPreference(PreferenceUtils.userid, "");

        trainer_updates = getIntent().getStringExtra("trainer_updates");
        recommendations = getIntent().getStringExtra("recommendations");


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

        image_followtrainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (trainer_updates.equalsIgnoreCase("1")) {
                    image_followtrainer.setImageResource(R.drawable.off1);
                    strtrainerupdates = "0";
                    strrecommendations = recommendations;
                } else if (trainer_updates.equalsIgnoreCase("0")) {
                    image_followtrainer.setImageResource(R.drawable.on);
                    strtrainerupdates = "1";
                    strrecommendations = recommendations;
                }
                updateNotificationSettings();
            }
        });

        image_personalized.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recommendations.equalsIgnoreCase("1")) {
                    image_personalized.setImageResource(R.drawable.off1);
                    strrecommendations = "0";
                    strtrainerupdates = trainer_updates;
                } else if (recommendations.equalsIgnoreCase("0")) {
                    image_personalized.setImageResource(R.drawable.on);
                    strrecommendations = "1";
                    strtrainerupdates = trainer_updates;
                }
                updateNotificationSettings();
            }
        });

    }

    private void initializeUI() {
        image_back = findViewById(R.id.image_back);
        image_followtrainer = findViewById(R.id.image_followtrainer);
        image_personalized = findViewById(R.id.image_personalized);

        if (trainer_updates.equalsIgnoreCase("1")) {
            image_followtrainer.setImageResource(R.drawable.on);
        } else {
            image_followtrainer.setImageResource(R.drawable.off1);
        }

        if (recommendations.equalsIgnoreCase("1")) {
            image_personalized.setImageResource(R.drawable.on);
        } else {
            image_personalized.setImageResource(R.drawable.off1);
        }
    }

    private void updateNotificationSettings() {
        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement> call = apiClass.updatenotificationsettings(userid, strtrainerupdates, strrecommendations,
                language, MainUrl.apikey);

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Log.e("dgdfgfd ", response.body().toString());
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
                            String background_video = dataobject.getString("background_video");
                             trainer_updates = dataobject.getString("trainer_updates");
                             recommendations = dataobject.getString("recommendations");

                            if (trainer_updates.equalsIgnoreCase("1")) {
                                image_followtrainer.setImageResource(R.drawable.on);
                            } else {
                                image_followtrainer.setImageResource(R.drawable.off1);
                            }

                            if (recommendations.equalsIgnoreCase("1")) {
                                image_personalized.setImageResource(R.drawable.on);
                            } else {
                                image_personalized.setImageResource(R.drawable.off1);
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
