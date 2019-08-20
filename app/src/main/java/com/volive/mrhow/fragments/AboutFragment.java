package com.volive.mrhow.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.volive.mrhow.R;
import com.volive.mrhow.activities.CategiriesActivity;
import com.volive.mrhow.activities.ViewDetailsActivity;
import com.volive.mrhow.adapters.NewCourseAdapter;
import com.volive.mrhow.models.NewCourseModels;
import com.volive.mrhow.util.ApiClass;
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

public class AboutFragment extends Fragment {

    RecyclerView related_recyclerview;
    ArrayList<NewCourseModels> newCourseModels = new ArrayList<>();
    PreferenceUtils preferenceUtils;
    NetworkConnection networkConnection;
    TextView text_courseoverview, text_seemore, text_description, text_requirments, text_seeall;

    String course_id1="",language="";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        preferenceUtils = new PreferenceUtils(getActivity());
        networkConnection = new NetworkConnection(getActivity());
        language = preferenceUtils.getStringFromPreference(PreferenceUtils.Language,"");


        initializeUI(view);
        initializeValues();

        if (networkConnection.isConnectingToInternet()) {
            courseDetails();
        } else {
            Toast.makeText(getActivity(), getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
        }

        return view;
    }


    private void initializeUI(View view) {
        text_courseoverview = view.findViewById(R.id.text_courseoverview);
        text_seemore = view.findViewById(R.id.text_seemore);
        text_description = view.findViewById(R.id.text_description);
        text_requirments = view.findViewById(R.id.text_requirments);
        text_seeall = view.findViewById(R.id.text_seeall);
        related_recyclerview = view.findViewById(R.id.related_recyclerview);

        related_recyclerview.setHasFixedSize(true);
        related_recyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


    }

    private void initializeValues() {
        text_seeall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CategiriesActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("course_id1",course_id1);
                intent.putExtra("Action","about");
                startActivity(intent);
            }
        });

    }


    private void courseDetails() {
        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement> call = apiClass.coursedetails(ViewDetailsActivity.courseid, preferenceUtils.getStringFromPreference(PreferenceUtils.userid, ""),
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
                             course_id1 = detailsobject.getString("course_id");
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


                            text_description.setText(Html.fromHtml(description));
                            text_courseoverview.setText(Html.fromHtml(overview));
                            text_requirments.setText(Html.fromHtml(requirements));

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

                                NewCourseAdapter newCourseAdapter = new NewCourseAdapter(getActivity(), newCourseModels);
                                related_recyclerview.setAdapter(newCourseAdapter);
                                newCourseAdapter.notifyDataSetChanged();
                            }


                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
