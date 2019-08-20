package com.volive.mrhow.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.volive.mrhow.R;
import com.volive.mrhow.adapters.NewCourseAdapter;
import com.volive.mrhow.models.NewCourseModels;
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

public class  CategiriesActivity extends AppCompatActivity {
    RecyclerView categiries_recyclerview;
    ArrayList<NewCourseModels> newCourseModels = new ArrayList<>();
    ImageView image_filter, image_back;
    PreferenceUtils preferenceUtils;
    NetworkConnection networkConnection;
    ProgressDialog progressDialog;
    String strcatid = "", strsubcatid = "", min = "", max = "", strrating = "", strsortby = "", catname = "",language="";
    TextView text_name;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categiries);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//only working in api level 26
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.lightgreen));
        }

        preferenceUtils = new PreferenceUtils(CategiriesActivity.this);
        networkConnection = new NetworkConnection(CategiriesActivity.this);

        language = preferenceUtils.getStringFromPreference(PreferenceUtils.Language,"");

        initializeUI();
        initializeValues();

        if (getIntent().getExtras() != null && getIntent().getStringExtra("Action") != null && getIntent().getStringExtra("Action").equalsIgnoreCase("filters")) {
            strcatid = getIntent().getStringExtra("strcatid");
            strsubcatid = getIntent().getStringExtra("strsubcatid");
            min = getIntent().getStringExtra("min");
            max = getIntent().getStringExtra("max");
            strrating = getIntent().getStringExtra("strrating");
            strsortby = getIntent().getStringExtra("strsortby");

            text_name.setText(getResources().getText(R.string.filterresults));

            if (networkConnection.isConnectingToInternet()) {
                courseListWithFilter();
            } else {
                Toast.makeText(CategiriesActivity.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
            }

        } else if(getIntent().getExtras()!=null&&getIntent().getStringExtra("Action")!=null&&getIntent().getStringExtra("Action").equalsIgnoreCase("about")){

            String course_id1= getIntent().getStringExtra("course_id1");
            text_name.setText(getResources().getText(R.string.relatedcourses));


            if (networkConnection.isConnectingToInternet()) {
                courseDetails(course_id1);
            } else {
                Toast.makeText(CategiriesActivity.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
            }


        } else if(getIntent().getExtras()!=null&&getIntent().getStringExtra("Action")!=null&&getIntent().getStringExtra("Action").equalsIgnoreCase("search")){

            String searchstring = getIntent().getStringExtra("searchstring");
            text_name.setText(getResources().getText(R.string.searchresults));
            if(networkConnection.isConnectingToInternet()){
                searchResults(searchstring);
            }else {
                Toast.makeText(CategiriesActivity.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
            }


        } else {
            strcatid = getIntent().getStringExtra("catid");
            catname = getIntent().getStringExtra("catname");

            text_name.setText(catname);

            if (networkConnection.isConnectingToInternet()) {
                courseListWithFilter();
            } else {
                Toast.makeText(CategiriesActivity.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void searchResults(String searchstring) {
        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement> call = apiClass.search(searchstring, language, MainUrl.apikey);
        progressDialog = DialogsUtils.showProgressDialog(CategiriesActivity.this, String.valueOf(getResources().getText(R.string.loading)));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDialog.dismiss();

                Log.e("searchresults ",response.body().toString());

                if (response.isSuccessful()) {
                    try {

                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        newCourseModels = new ArrayList<>();

                        if (status.equalsIgnoreCase("1")) {

                            JSONArray dataArray = jsonObject.getJSONArray("data");

                            if (dataArray.length() > 0) {

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject dataobject = dataArray.getJSONObject(i);
                                    String course_id = dataobject.getString("course_id");
                                    String category_name = dataobject.getString("category_name");
                                    String course_title = dataobject.getString("course_title");
                                    String price = dataobject.getString("price");
                                    String cover = dataobject.getString("cover");
                                    String cover_type = dataobject.getString("cover_type");
                                    String duration = dataobject.getString("duration");
                                    String offer_price = dataobject.getString("offer_price");
                                    String tags =dataobject.getString("tags");
                                    String total_ratings =dataobject.getString("total_ratings");
                                    String purchased = dataobject.getString("purchased");
                                    String thumbnail = dataobject.getString("thumbnail");
                                    String currency = dataobject.getString("currency");


                                    NewCourseModels newcourse = new NewCourseModels(course_id, category_name, course_title,
                                            price, offer_price, cover, cover_type, total_ratings, purchased, tags, duration,thumbnail,currency);
                                    newCourseModels.add(newcourse);

                                }
                            }

                        } else {
                            Toast.makeText(CategiriesActivity.this, message, Toast.LENGTH_SHORT).show();
                        }

                        NewCourseAdapter newCourseAdapter = new NewCourseAdapter(CategiriesActivity.this, newCourseModels);
                        categiries_recyclerview.setAdapter(newCourseAdapter);
                        newCourseAdapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Log.e("akdfa ", e.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("sdfsdf ", t.toString());

            }
        });


    }


    private void initializeUI() {
        categiries_recyclerview = findViewById(R.id.categiries_recyclerview);
        image_back = findViewById(R.id.image_back);
        image_filter = findViewById(R.id.image_filter);
        text_name = findViewById(R.id.text_name);

        categiries_recyclerview.setLayoutManager(new GridLayoutManager(CategiriesActivity.this, 2));

    }

    private void initializeValues() {
        image_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FiltersActivity.class));
            }
        });

        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void courseListWithFilter() {
        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement> call = apiClass.courseslistfilter(strcatid, strsubcatid, max, min, strrating, strsortby, language, MainUrl.apikey);
        progressDialog = DialogsUtils.showProgressDialog(CategiriesActivity.this, String.valueOf(getResources().getText(R.string.loading)));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDialog.dismiss();

                Log.e("dfdf ",response.body().toString());

                if (response.isSuccessful()) {
                    try {

                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        newCourseModels = new ArrayList<>();

                        if (status.equalsIgnoreCase("1")) {

                            JSONArray dataArray = jsonObject.getJSONArray("data");

                            if (dataArray.length() > 0) {

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject dataobject = dataArray.getJSONObject(i);
                                    String course_id = dataobject.getString("course_id");
                                    String category_name = dataobject.getString("category_name");
                                    String course_title = dataobject.getString("course_title");
                                    String price = dataobject.getString("price");
                                    String cover = dataobject.getString("cover");
                                    String cover_type = dataobject.getString("cover_type");
                                    String duration = dataobject.getString("duration");
                                    String offer_price = dataobject.getString("offer_price");
                                    String tags =dataobject.getString("tags");
                                    String total_ratings =dataobject.getString("total_ratings");
                                    String purchased = dataobject.getString("purchased");
                                    String thumbnail = dataobject.getString("thumbnail");
                                    String currency=dataobject.getString("currency");


                                    NewCourseModels newcourse = new NewCourseModels(course_id, category_name, course_title,
                                            price, offer_price, cover, cover_type, total_ratings, purchased, tags, duration,thumbnail,currency);
                                    newCourseModels.add(newcourse);

                                }
                            }

                        } else {
                            Toast.makeText(CategiriesActivity.this, message, Toast.LENGTH_SHORT).show();
                        }

                        NewCourseAdapter newCourseAdapter = new NewCourseAdapter(CategiriesActivity.this, newCourseModels);
                        categiries_recyclerview.setAdapter(newCourseAdapter);
                        newCourseAdapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Log.e("akdfa ", e.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("sdfsdf ", t.toString());

            }
        });
    }

    private void courseDetails(String course_id1) {
        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement> call = apiClass.coursedetails(course_id1, preferenceUtils.getStringFromPreference(PreferenceUtils.userid, ""),
                language, MainUrl.apikey);

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Log.e("dsgfd ", response.body().toString());

                if (response.isSuccessful()) {
                    try {

                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status.equalsIgnoreCase("1")) {

                            JSONObject dataobject = jsonObject.getJSONObject("data");

                            JSONObject detailsobject = dataobject.getJSONObject("details");
                            String course_id1 = detailsobject.getString("course_id");
                            String category_name1 = detailsobject.getString("category_name");
                            String course_title1 = detailsobject.getString("course_title");
                            String price1 = detailsobject.getString("price");
                            String offer_price1 = detailsobject.getString("offer_price");
                            String cover1 = detailsobject.getString("cover");
                            String cover_type1 = detailsobject.getString("cover_type");
                            String duration1 = detailsobject.getString("duration");
                            String ratings1 = detailsobject.getString("ratings");
                            String total_ratings1 = detailsobject.getString("total_ratings");
                            String purchased1 = detailsobject.getString("purchased");
                            String tags1 = detailsobject.getString("tags");
                            String downloadable = detailsobject.getString("downloadable");
                            String recommended = detailsobject.getString("recommended");
                            String overview = detailsobject.getString("overview");
                            String description = detailsobject.getString("description");
                            String requirements = detailsobject.getString("requirements");
                            String author_id = detailsobject.getString("author_id");
                            String name = detailsobject.getString("name");
                            String profile_pic = detailsobject.getString("profile_pic");
                            String followers = detailsobject.getString("followers");



                            newCourseModels = new ArrayList<>();

                            JSONArray relatedArray = dataobject.getJSONArray("related");
                            if (relatedArray.length() > 0) {
                                for (int i = 0; i < relatedArray.length(); i++) {
                                    JSONObject relatedobject = relatedArray.getJSONObject(i);

                                    String course_id = relatedobject.getString("course_id");
                                    String category_name = relatedobject.getString("category_name");
                                    String course_title = relatedobject.getString("course_title");
                                    String price = relatedobject.getString("price");
                                    String offer_price = relatedobject.getString("offer_price");
                                    String cover = relatedobject.getString("cover");
                                    String total_ratings = relatedobject.getString("ratings");
                                    String tags = relatedobject.getString("tags");
                                    String cover_type = relatedobject.getString("cover_type");
                                    String purchased = relatedobject.getString("purchased");
                                    String duration = relatedobject.getString("duration");
                                    String thumbnail = relatedobject.getString("thumbnail");
                                    String currency =relatedobject.getString("currency");

                                    NewCourseModels newcourse = new NewCourseModels(course_id, category_name, course_title,
                                            price, offer_price, cover, cover_type, total_ratings, purchased, tags, duration,thumbnail,currency);
                                    newCourseModels.add(newcourse);

                                }

                                NewCourseAdapter newCourseAdapter = new NewCourseAdapter(CategiriesActivity.this, newCourseModels);
                                categiries_recyclerview.setAdapter(newCourseAdapter);
                                newCourseAdapter.notifyDataSetChanged();
                            }


                        } else {
                            Toast.makeText(CategiriesActivity.this, message, Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("sdfgsdg ", e.toString());
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e("sdsd ", t.toString());
            }
        });


    }


}
