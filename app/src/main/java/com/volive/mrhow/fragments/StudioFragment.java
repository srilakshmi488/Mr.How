package com.volive.mrhow.fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonElement;
import com.volive.mrhow.R;
import com.volive.mrhow.adapters.StudioAdapter;
import com.volive.mrhow.adapters.TagsAdapter;
import com.volive.mrhow.models.StudioModels;
import com.volive.mrhow.models.TagsModels;
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


public class StudioFragment extends Fragment {
    public static TextView text_all;
    RecyclerView articles_recyclerview;
    ArrayList<StudioModels> studioModels;
    ImageView image_studio;
    PreferenceUtils preferenceUtils;
    NetworkConnection networkConnection;
    ProgressDialog progressDialog, progressDialog1;
    String userid = "";
    LinearLayout linear_studio;
    RecyclerView tags_recyclerview;
    ArrayList<TagsModels> tagsModels = new ArrayList<>();
    String hashtag = "", language = "";


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_studio, container, false);

        preferenceUtils = new PreferenceUtils(getActivity());
        networkConnection = new NetworkConnection(getActivity());

        userid = preferenceUtils.getStringFromPreference(PreferenceUtils.userid, "");
        language = preferenceUtils.getStringFromPreference(PreferenceUtils.Language, "");

        initializeUI(view);


        if (networkConnection.isConnectingToInternet()) {
            getArticlesList(hashtag);
            gethashTags();
        } else {
            Toast.makeText(getActivity(), getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
        }

        return view;
    }


    private void initializeUI(View view) {
        text_all = view.findViewById(R.id.text_all);

        image_studio = view.findViewById(R.id.image_studio);
        articles_recyclerview = view.findViewById(R.id.articles_recyclerview);
        linear_studio = view.findViewById(R.id.linear_studio);
        articles_recyclerview.setHasFixedSize(true);
        articles_recyclerview.setLayoutManager(new GridLayoutManager(getContext(), 2));

        tags_recyclerview = view.findViewById(R.id.tags_recyclerview);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        tags_recyclerview.setLayoutManager(horizontalLayoutManagaer);

        text_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getArticlesList(hashtag);
            }
        });

    }

    public void getArticlesList(final String hashtag) {
        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement> call = apiClass.articleslist(userid, language, MainUrl.apikey, hashtag);
        View view = getActivity().getWindow().getDecorView();
        view.setBackgroundColor(getResources().getColor(R.color.whitecolour));
        progressDialog = DialogsUtils.showProgressDialog(getActivity(), String.valueOf(getResources().getText(R.string.loading)));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDialog.dismiss();
                Log.e("studiooo ", response.body().toString());
                if (response.isSuccessful()) {
                    try {

                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        studioModels = new ArrayList<>();
                        if (status.equalsIgnoreCase("1")) {

                            JSONObject bannerobject = jsonObject.getJSONObject("banner");
                            String image = bannerobject.getString("image");
                            String type = bannerobject.getString("type");
                            String bg_color = bannerobject.getString("bg_color");


                            View view = getActivity().getWindow().getDecorView();
                            view.setBackgroundColor(Color.parseColor(bg_color));


                            Glide.with(getActivity()).load(MainUrl.imageurl + image).into(image_studio);

                            JSONArray articlesArray = jsonObject.getJSONArray("articles");

                            if (articlesArray.length() > 0) {
                                for (int i = 0; i < articlesArray.length(); i++) {

                                    JSONObject articlesobject = articlesArray.getJSONObject(i);
                                    String article_id = articlesobject.getString("article_id");
                                    String article_title = articlesobject.getString("article_title");
                                    String article_description = articlesobject.getString("article_description");
                                    String article_image = articlesobject.getString("article_image");
                                    String image_type = articlesobject.getString("image_type");
                                    String writer = articlesobject.getString("writer");
                                    String created_on = articlesobject.getString("created_on");
                                    String likes_count = articlesobject.getString("likes_count");
                                    String comments_count = articlesobject.getString("comments_count");
                                    String views_count = articlesobject.getString("views_count");
                                    String date = articlesobject.getString("date");
                                    String thumbnail = articlesobject.getString("thumbnail");


                                    StudioModels studiomodel = new StudioModels(article_id, article_title, article_description, article_image,
                                            image_type, writer, created_on, likes_count, comments_count, views_count, date, thumbnail);
                                    studioModels.add(studiomodel);


                                }
                            }


                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }

                        StudioAdapter studioAdapter = new StudioAdapter(getActivity(), studioModels, StudioFragment.this, hashtag);
                        articles_recyclerview.setAdapter(studioAdapter);
                        studioAdapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Log.e("ssgfdkg", e.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("safsdf ", t.toString());
            }
        });

    }

    private void gethashTags() {
        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement> call = apiClass.hashtags(language, MainUrl.apikey);
        progressDialog1 = DialogsUtils.showProgressDialog(getActivity(), String.valueOf(getResources().getText(R.string.loading)));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDialog1.dismiss();
                if (response.isSuccessful()) {
                    try {

                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("1")) {

                            JSONArray dataArray = jsonObject.getJSONArray("data");

                            tagsModels.clear();
                            if (dataArray.length() > 0) {
                                TagsModels tagsModel = new TagsModels("", (String) getResources().getText(R.string.all));
                                tagsModels.add(tagsModel);
                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject dataobject = dataArray.getJSONObject(i);
                                    String hashtag_id = dataobject.getString("hashtag_id");
                                    String name = dataobject.getString("name");
                                    TagsModels tagsModel1 = new TagsModels(hashtag_id, name);
                                    tagsModels.add(tagsModel1);


                                }
                            }

                            TagsAdapter tagsAdapter = new TagsAdapter(getActivity(), tagsModels, StudioFragment.this);
                            tags_recyclerview.setAdapter(tagsAdapter);
                            tagsAdapter.notifyDataSetChanged();

                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog1.dismiss();
                        Log.e("sdfsdf ", e.toString());
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog1.dismiss();
                Log.e("sdfdsf ", t.toString());

            }
        });

    }

}