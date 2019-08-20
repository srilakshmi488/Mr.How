package com.volive.mrhow.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.volive.mrhow.R;
import com.volive.mrhow.util.NetworkConnection;
import com.volive.mrhow.util.PreferenceUtils;

import java.util.Locale;

public class SplashActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST = 1;
    private static int SPLASH_SCREEN_TIME_OUT = 2500;
    PreferenceUtils preferenceUtils;
    NetworkConnection networkConnection;
    String strSelectLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        preferenceUtils = new PreferenceUtils(SplashActivity.this);
        networkConnection = new NetworkConnection(SplashActivity.this);
        strSelectLanguage = preferenceUtils.getStringFromPreference(PreferenceUtils.Language, "");

        if (strSelectLanguage == null || strSelectLanguage.isEmpty()) {
            String languageToLoad = "ar"; // your language
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
            preferenceUtils.setLanguage("ar");

        } else if (strSelectLanguage.equalsIgnoreCase("en")) {
            String languageToLoad = "en"; // your language
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
            preferenceUtils.setLanguage("en");
        } else if (strSelectLanguage.equalsIgnoreCase("ar")) {
            String languageToLoad = "ar"; // your language
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
            preferenceUtils.setLanguage("ar");
        } else {
            String languageToLoad = "ar"; // your language
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
            preferenceUtils.setLanguage("ar");
        }

        if (!preferenceUtils.getbooleanFromPreference(PreferenceUtils.LOGIN, false)) {

            if (checkPermissions()) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();

                    }
                }, SPLASH_SCREEN_TIME_OUT);
            } else {
                checkPermissions();
            }

        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(SplashActivity.this, HomeNavigation.class);
                    startActivity(i);
                    finish();

                }
            }, SPLASH_SCREEN_TIME_OUT);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();

                        }
                    }, SPLASH_SCREEN_TIME_OUT);
                } else {
                    checkPermissions();
                }
                return;
        }
    }


    private boolean checkPermissions() {
        final Context context = SplashActivity.this;
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_NETWORK_STATE}, MY_PERMISSIONS_REQUEST);
            return false;
        } else {
            return true;
        }
    }


}
