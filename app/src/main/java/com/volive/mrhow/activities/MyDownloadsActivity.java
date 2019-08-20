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

import com.volive.mrhow.adapters.MyDownLoadsAdapter;
import com.volive.mrhow.models.MyDownloadModels;
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

public class MyDownloadsActivity extends AppCompatActivity {

    ImageView image_back;
    RecyclerView downloads_recyclerview;
    PreferenceUtils preferenceUtils;
    ArrayList<MyDownloadModels>myDownloadModels = new ArrayList<>();
    NetworkConnection networkConnection;
    String userid="",language="";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_downloads);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//only working in api level 26
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.lightgreen));
        }

        preferenceUtils = new PreferenceUtils(MyDownloadsActivity.this);
        networkConnection = new NetworkConnection(MyDownloadsActivity.this);

        userid = preferenceUtils.getStringFromPreference(PreferenceUtils.userid,"");
        language =preferenceUtils.getStringFromPreference(PreferenceUtils.Language,"");

        initializeUI();
        initializeValues();

        if(networkConnection.isConnectingToInternet()){
            getMyDownloads();
        }else {
            Toast.makeText(MyDownloadsActivity.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
        }


    }

    private void initializeUI() {
        image_back = findViewById(R.id.image_back);
        downloads_recyclerview = findViewById(R.id.downloads_recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MyDownloadsActivity.this);
        downloads_recyclerview.setLayoutManager(mLayoutManager);
        downloads_recyclerview.setItemAnimator(new DefaultItemAnimator());


    }

    public void getMyDownloads() {
        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement> call = apiClass.downloads(userid,language, MainUrl.apikey);
        progressDialog = DialogsUtils.showProgressDialog(MyDownloadsActivity.this, String.valueOf(getResources().getText(R.string.loading)));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Log.e("downlodsresponse",response.body().toString());
                progressDialog.dismiss();
                myDownloadModels = new ArrayList<>();
                if(response.isSuccessful()){
                    try {

                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        if(status.equalsIgnoreCase("1")){

                            JSONArray dataArray = jsonObject.getJSONArray("data");

                            if(dataArray.length()>0) {

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject dataobject = dataArray.getJSONObject(i);

                                    String course_id = dataobject.getString("course_id");
                                    String course_title = dataobject.getString("course_title");
                                    String download_id = "";

                                    JSONArray mydownloadsArray = dataobject.getJSONArray("mydownloads");
                                    if(mydownloadsArray.length()>0) {
                                        for (int j = 0; j < mydownloadsArray.length(); j++) {

                                            JSONObject mydownloadsobject = mydownloadsArray.getJSONObject(j);
                                            String file_name = mydownloadsobject.getString("file_name");
                                            String file = mydownloadsobject.getString("file");
                                            String file_type = mydownloadsobject.getString("file_type");
                                            String thumbnail = mydownloadsobject.getString("thumbnail");


                                            MyDownloadModels mydownload = new MyDownloadModels(course_id, course_title, file_name, file,
                                                    file_type, thumbnail, download_id);
                                            myDownloadModels.add(mydownload);

                                        }
                                    }


                                }


                                MyDownLoadsAdapter myDownLoadsAdapter = new MyDownLoadsAdapter(MyDownloadsActivity.this,myDownloadModels);
                                downloads_recyclerview.setAdapter(myDownLoadsAdapter);
                                myDownLoadsAdapter.notifyDataSetChanged();


                            }

                        }


                    }catch (Exception e){
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Log.e("jgfdgfd ",e.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("sdkfgs ",t.toString());

            }
        });

    }


    private void initializeValues() {
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
