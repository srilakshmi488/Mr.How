package com.volive.mrhow.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonElement;
import com.volive.mrhow.R;
import com.volive.mrhow.adapters.ArticleDetailAdapter;
import com.volive.mrhow.adapters.CommentsAdapter;
import com.volive.mrhow.models.ArticleDetailModels;
import com.volive.mrhow.models.CommentsModels;
import com.volive.mrhow.models.SubCommentsModels;
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

public class DetailsActivity extends AppCompatActivity {

    ArrayList<CommentsModels> commentsModels;
    ArrayList<SubCommentsModels> subCommentsModels;
    RecyclerView comments_recyclerview;
    ImageView image_back;
    PreferenceUtils preferenceUtils;
    NetworkConnection networkConnection;
    ProgressDialog progressDialog, progressDialog1;
    String userid = "", articleid = "", article_image = "", image_type = "",language="",redirect_url="";
    Button button_postcomment;
    EditText edit_comment;
    RecyclerView articledetail_recyclerview;
    ArrayList<ArticleDetailModels>articleDetailModels= new ArrayList<>();
    TextView text_link;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//only working in api level 26
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.lightgreen));
        }

        preferenceUtils = new PreferenceUtils(DetailsActivity.this);
        networkConnection = new NetworkConnection(DetailsActivity.this);

        language = preferenceUtils.getStringFromPreference(PreferenceUtils.Language,"");
        userid = preferenceUtils.getStringFromPreference(PreferenceUtils.userid, "");

        articleid = getIntent().getStringExtra("articleid");

        initializeUI();
        initializeValues();

        if (networkConnection.isConnectingToInternet()) {
            articleDetails();
        } else {
            Toast.makeText(DetailsActivity.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
        }

    }


    private void initializeUI() {
        image_back = findViewById(R.id.image_back);
        button_postcomment = findViewById(R.id.button_postcomment);
        edit_comment = findViewById(R.id.edit_comment);
        comments_recyclerview = findViewById(R.id.comments_recyclerview);
        articledetail_recyclerview = findViewById(R.id.articledetail_recyclerview);
        text_link = findViewById(R.id.text_link);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(DetailsActivity.this);
        comments_recyclerview.setLayoutManager(mLayoutManager);
        comments_recyclerview.setItemAnimator(new DefaultItemAnimator());


        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        articledetail_recyclerview.setLayoutManager(mLayoutManager1);
        articledetail_recyclerview.setHasFixedSize(true);


    }

    private void initializeValues() {

        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        button_postcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String strcomment = edit_comment.getText().toString().trim();

                if (networkConnection.isConnectingToInternet()) {
                    if (!strcomment.isEmpty()) {

                        articleComment(strcomment);

                    } else {
                        Toast.makeText(DetailsActivity.this, getResources().getText(R.string.pleasewriteyourcomment), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(DetailsActivity.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
                }

            }
        });

        text_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(redirect_url));
                startActivity(browserIntent);
            }
        });

    }

    private void articleComment(String strcomment) {

        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement> call = apiClass.articlecomment(userid, articleid, strcomment, language, MainUrl.apikey);
        progressDialog1 = DialogsUtils.showProgressDialog(DetailsActivity.this, String.valueOf(getResources().getText(R.string.loading)));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDialog1.dismiss();
                Log.e("articlecomment ", response.body().toString());
                if (response.isSuccessful()) {
                    try {

                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        if (status.equalsIgnoreCase("1")) {
                            Toast.makeText(DetailsActivity.this, message, Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(DetailsActivity.this, DetailsActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("articleid", articleid);
                            startActivity(intent);

                        } else {
                            Toast.makeText(DetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog1.dismiss();
                        Log.e("sfkdsf ", e.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog1.dismiss();
                Log.e("fsdf ", t.toString());

            }
        });

    }

    public void articleDetails() {
        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement> call = apiClass.articledetails(userid, articleid, language, MainUrl.apikey);
        progressDialog = DialogsUtils.showProgressDialog(DetailsActivity.this, String.valueOf(getResources().getText(R.string.loading)));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDialog.dismiss();

                Log.e("fdgdfgdfbggfhfg ", response.body().toString());

                if (response.isSuccessful()) {
                    try {

                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status.equalsIgnoreCase("1")) {

                            JSONObject dataobject = jsonObject.getJSONObject("data");
                            String article_id = dataobject.getString("article_id");
                            String article_title = dataobject.getString("article_title");
                            String article_description = dataobject.getString("article_description");
                            article_image = dataobject.getString("article_image");
                            image_type = dataobject.getString("image_type");
                            String thumbnail = dataobject.getString("thumbnail");
                            String writer = dataobject.getString("writer");
                            String created_on = dataobject.getString("created_on");
                            String date = dataobject.getString("date");
                            String likes_count = dataobject.getString("likes_count");
                            String comments_count = dataobject.getString("comments_count");
                            String views_count = dataobject.getString("views_count");
                             redirect_url = dataobject.getString("redirect_url");

                            text_link.setText(redirect_url);


                            JSONArray detailsArray = dataobject.getJSONArray("details");
                            if(detailsArray.length()>0) {
                                for (int j = 0; j < detailsArray.length(); j++) {
                                    JSONObject detailsobject = detailsArray.getJSONObject(j);
                                    String details_id = detailsobject.getString("details_id");
                                    String detail_image = detailsobject.getString("detail_image");
                                    String detail_thumb = detailsobject.getString("detail_thumb");
                                    String detail_image_type = detailsobject.getString("detail_image_type");
                                    String detail_description_en = detailsobject.getString("detail_description");

                                    ArticleDetailModels artcledetail = new ArticleDetailModels(details_id,detail_image,detail_thumb,detail_image_type,detail_description_en);
                                    articleDetailModels.add(artcledetail);

                                }

                                ArticleDetailAdapter articleDetailAdapter = new ArticleDetailAdapter(DetailsActivity.this,articleDetailModels);
                                articledetail_recyclerview.setAdapter(articleDetailAdapter);
                                articleDetailAdapter.notifyDataSetChanged();

                            }



                            JSONArray commentsArray = dataobject.getJSONArray("comments");
                            commentsModels = new ArrayList<>();
                            if (commentsArray.length() > 0) {
                                for (int i = 0; i < commentsArray.length(); i++) {

                                    JSONObject commentsobject = commentsArray.getJSONObject(i);

                                    String article_comment_id = commentsobject.getString("article_comment_id");
                                    String main_comment = commentsobject.getString("main_comment");
                                    String name = commentsobject.getString("name");
                                    String profile_pic = commentsobject.getString("profile_pic");
                                    String commented_on = commentsobject.getString("commented_on");
                                    String time = commentsobject.getString("time");
                                    String article_comment_likes = commentsobject.getString("article_comment_likes");
                                    String article_sub_comment_count = commentsobject.getString("article_sub_comment_count");


                                    subCommentsModels = new ArrayList<>();

                                    JSONArray subcommentsArray = commentsobject.getJSONArray("sub_comments");

                                    if (subcommentsArray.length() > 0) {

                                        for (int j = 0; j < subcommentsArray.length(); j++) {

                                            JSONObject subcommentsobject = subcommentsArray.getJSONObject(j);

                                            String sub_comment = subcommentsobject.getString("sub_comment");
                                            String subcoomentname = subcommentsobject.getString("name");

                                            SubCommentsModels subcomment = new SubCommentsModels(sub_comment, subcoomentname);
                                            subCommentsModels.add(subcomment);

                                        }

                                    }


                                    CommentsModels commentmodel = new CommentsModels(article_comment_id, main_comment, name,
                                            profile_pic, commented_on, time, article_comment_likes, article_sub_comment_count, subCommentsModels);
                                    commentsModels.add(commentmodel);


                                }


                            }


                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(DetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                        }


                        CommentsAdapter commentsAdapter = new CommentsAdapter(DetailsActivity.this, commentsModels, subCommentsModels);
                        comments_recyclerview.setAdapter(commentsAdapter);
                        commentsAdapter.notifyDataSetChanged();


                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Log.e("dfkdsf ", e.toString());
                    }
                }


            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("kfsdf ", t.toString());
            }
        });

    }

}
