package com.volive.mrhow.activities;

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

public class TermsConditionsActivity extends AppCompatActivity {

        WebView webview;
        ImageView image_back;
        PreferenceUtils preferenceUtils;
        NetworkConnection networkConnection;
        String language ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);

        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//only working in api level 26
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.lightgreen));
        }

        preferenceUtils = new PreferenceUtils(TermsConditionsActivity.this);
        networkConnection = new NetworkConnection(TermsConditionsActivity.this);
        language = preferenceUtils.getStringFromPreference(PreferenceUtils.Language,"");

        initializeUI();
        initializeValues();



        if(networkConnection.isConnectingToInternet()){
            termsAndConditions();
        }else {
            Toast.makeText(TermsConditionsActivity.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
        }



    }



    private void initializeUI(){
        webview = findViewById(R.id.webview);
        image_back = findViewById(R.id.image_back);

    }

    private void initializeValues() {

        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void termsAndConditions() {
        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement> call = apiClass.termsandconditions(MainUrl.apikey,language);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.isSuccessful()){
                    try {

                        JSONObject jsonObject = new JSONObject(response.body().toString());

                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        if(status.equalsIgnoreCase("1")){

                            JSONObject dataobject = jsonObject.getJSONObject("data");
                            String text = dataobject.getString("text");

                            webview.getSettings().setJavaScriptEnabled(true);
                            webview.loadDataWithBaseURL("", text, "text/html", "UTF-8", "");



                        }else {
                            Toast.makeText(TermsConditionsActivity.this, message, Toast.LENGTH_SHORT).show();
                        }


                    }catch (Exception e){
                        e.printStackTrace();
                        Log.e("sdkff ",e.toString());
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e("sdsdfg ",t.toString());

            }
        });

    }



}
