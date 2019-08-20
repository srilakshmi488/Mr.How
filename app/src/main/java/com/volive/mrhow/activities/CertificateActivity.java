package com.volive.mrhow.activities;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonElement;
import com.volive.mrhow.R;
import com.volive.mrhow.util.ApiClass;
import com.volive.mrhow.util.MainUrl;
import com.volive.mrhow.util.NetworkConnection;
import com.volive.mrhow.util.PreferenceUtils;
import com.volive.mrhow.util.RetrofitClass;

import org.json.JSONObject;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CertificateActivity extends AppCompatActivity {

    Button button_share, button_download;
    ImageView image_back, image_certificate;
    PreferenceUtils preferenceUtils;
    NetworkConnection networkConnection;
    String userid = "", courseid = "", certificate = "",language="";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificate);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//only working in api level 26
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.lightgreen));
        }

        preferenceUtils = new PreferenceUtils(CertificateActivity.this);
        networkConnection = new NetworkConnection(CertificateActivity.this);

        language = preferenceUtils.getStringFromPreference(PreferenceUtils.Language,"");

        userid = preferenceUtils.getStringFromPreference(PreferenceUtils.userid, "");
        courseid = getIntent().getStringExtra("courseid");


        initializeUI();
        initializeValues();

        if (networkConnection.isConnectingToInternet()) {

            courseCertificate();
        } else {
            Toast.makeText(CertificateActivity.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
        }


    }

    private void courseCertificate() {
        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement> call = apiClass.coursecertificate(userid, courseid, language, MainUrl.apikey);
        View view = getWindow().getDecorView();
        view.setBackgroundColor(getResources().getColor(R.color.whitecolour));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Log.e("jdfgdg ", response.body().toString());
                if (response.isSuccessful()) {
                    try {

                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        if (status.equalsIgnoreCase("1")) {
                            JSONObject dataobject = jsonObject.getJSONObject("data");
                            certificate = dataobject.getString("certificate");

                            String bg_color = jsonObject.getString("bg_color");
                            View view = getWindow().getDecorView();
                            view.setBackgroundColor(Color.parseColor(bg_color));

                            Glide.with(CertificateActivity.this).load(MainUrl.imageurl + certificate).into(image_certificate);

                        } else {
                            Toast.makeText(CertificateActivity.this, message, Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("sdsg ", e.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e("jdfg ", t.toString());

            }
        });
    }

    private void initializeUI() {
        button_share = findViewById(R.id.button_share);
        button_download = findViewById(R.id.button_download);
        image_back = findViewById(R.id.image_back);
        image_certificate = findViewById(R.id.image_certificate);
    }

    private void initializeValues() {

        button_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                Uri screenshotUri = Uri.parse(MainUrl.imageurl + certificate);

//                try {
//                    InputStream stream = getContentResolver().openInputStream(screenshotUri);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }

                sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                sharingIntent.setType("image/*");
                startActivity(Intent.createChooser(sharingIntent, "Share image using"));

            }

        });


        button_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = MainUrl.imageurl + certificate;
                downloadFile(url);
            }
        });

        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void downloadFile(String url) {
        File direct = new File(Environment.getExternalStorageDirectory() + "/MrHow");
        if (!direct.exists()) {
            direct.mkdirs();
        }

        DownloadManager mgr = (DownloadManager) CertificateActivity.this.getSystemService(Context.DOWNLOAD_SERVICE);

        Uri downloadUri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(
                downloadUri);

        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI
                        | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false).setTitle("Demo")
                .setDescription("Something useful. No, really.")
                .setDestinationInExternalPublicDir("/MrHow", "fileName.jpg");

        mgr.enqueue(request);

        // Open Download Manager to view File progress
        Toast.makeText(CertificateActivity.this, "Downloading...", Toast.LENGTH_LONG).show();
        startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));

    }

}
