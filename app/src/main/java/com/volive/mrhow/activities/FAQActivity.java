package com.volive.mrhow.activities;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.volive.mrhow.R;
import com.volive.mrhow.adapters.FaqAdapter;
import com.volive.mrhow.models.FaqModels;
import com.volive.mrhow.util.ApiClass;
import com.volive.mrhow.util.DialogsUtils;
import com.volive.mrhow.util.MainUrl;
import com.volive.mrhow.util.NetworkConnection;
import com.volive.mrhow.util.PreferenceUtils;
import com.volive.mrhow.util.RetrofitClass;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FAQActivity extends AppCompatActivity {

    RecyclerView faq_recyclerview;
    ImageView image_back;
    PreferenceUtils preferenceUtils;
    NetworkConnection networkConnection;
    ProgressDialog progressDialog;
    ArrayList<FaqModels>faqModels=new ArrayList<>();
    String language ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//only working in api level 26
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.lightgreen));
        }


        preferenceUtils = new PreferenceUtils(FAQActivity.this);
        networkConnection = new NetworkConnection(FAQActivity.this);

        language = preferenceUtils.getStringFromPreference(PreferenceUtils.Language,"");


        initializeUI();
        initializeValues();

        if (networkConnection.isConnectingToInternet()) {
            getFaqs();
        } else {
            Toast.makeText(FAQActivity.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
        }


    }

    private void initializeUI() {
        image_back = findViewById(R.id.image_back);
        faq_recyclerview = (RecyclerView) findViewById(R.id.faq_recyclerview);
    }

    private void initializeValues() {
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(FAQActivity.this);
        faq_recyclerview.setLayoutManager(mLayoutManager);
        faq_recyclerview.setItemAnimator(new DefaultItemAnimator());


    }

    private void getFaqs() {
        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement> call = apiClass.faqs(MainUrl.apikey, language);

        progressDialog = DialogsUtils.showProgressDialog(FAQActivity.this, String.valueOf(getResources().getText(R.string.loading)));

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    try {

                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        faqModels = new ArrayList<>();

                        if (status.equalsIgnoreCase("1")) {

                            JSONArray dataArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {

                                JSONObject dataobject = dataArray.getJSONObject(i);

                                String title = dataobject.getString("title");
                                String text = dataobject.getString("text");

                                FaqModels faqmodel = new FaqModels(title,text);
                                faqModels.add(faqmodel);


                            }

                        }



                        FaqAdapter faqAdapter = new FaqAdapter(FAQActivity.this,faqModels);
                        faq_recyclerview.setAdapter(faqAdapter);
                        faqAdapter.notifyDataSetChanged();


                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Log.e("dfsdf ", e.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("akasdf ", t.toString());

            }
        });


    }

}
