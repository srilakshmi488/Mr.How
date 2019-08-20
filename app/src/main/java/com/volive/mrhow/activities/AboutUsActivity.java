package com.volive.mrhow.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

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


public class AboutUsActivity extends AppCompatActivity {

    ImageView image_back, image_facebook, image_google, image_instagram, image_twitter;
    WebView web_aboutus;
    PreferenceUtils preferenceUtils;
    NetworkConnection networkConnection;
    String facebook = "", google = "", instagram = "", twitter = "",language="";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//only working in api level 26
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.lightgreen));
        }

        initializeUI();
        initializeValues();

        preferenceUtils = new PreferenceUtils(AboutUsActivity.this);
        networkConnection = new NetworkConnection(AboutUsActivity.this);

        language = preferenceUtils.getStringFromPreference(PreferenceUtils.Language,"");

        if (networkConnection.isConnectingToInternet()) {
            aboutUS();
        } else {
            Toast.makeText(AboutUsActivity.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
        }


    }

    private void initializeUI() {
        image_back = findViewById(R.id.image_back);
        web_aboutus = findViewById(R.id.web_aboutus);
        image_facebook = findViewById(R.id.image_facebook);
        image_google = findViewById(R.id.image_google);
        image_instagram = findViewById(R.id.image_instagram);
        image_twitter = findViewById(R.id.image_twitter);
    }

    private void initializeValues() {

        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        image_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebook));
                startActivity(browserIntent);
            }
        });

        image_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(google));
                startActivity(browserIntent);
            }
        });

        image_instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(instagram));
                startActivity(browserIntent);
            }
        });

        image_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitter));
                startActivity(browserIntent);
            }
        });

    }

    private void aboutUS() {
        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement> call = apiClass.aboutus(MainUrl.apikey, language);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    try {

                        JSONObject jsonObject = new JSONObject(response.body().toString());

                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        if (status.equalsIgnoreCase("1")) {

                            JSONObject dataobject = jsonObject.getJSONObject("data");
                            String text = dataobject.getString("text");
                            facebook = dataobject.getString("facebook");
                            google = dataobject.getString("google");
                            instagram = dataobject.getString("instagram");
                            twitter = dataobject.getString("twitter");

                            web_aboutus.getSettings().setJavaScriptEnabled(true);
                            web_aboutus.loadDataWithBaseURL("", text, "text/html", "UTF-8", "");

                        } else {
                            Toast.makeText(AboutUsActivity.this, message, Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("afsd ", e.toString());
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e("asfdsa ", t.toString());

            }
        });


    }

}
