package com.volive.mrhow.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.bumptech.glide.Glide;
import com.google.gson.JsonElement;
import com.volive.mrhow.BuildConfig;
import com.volive.mrhow.R;
import com.volive.mrhow.adapters.NewCourseAdapter;
import com.volive.mrhow.adapters.ReviewsAdapter;
import com.volive.mrhow.models.NewCourseModels;
import com.volive.mrhow.models.ReviewsModels;
import com.volive.mrhow.util.ApiClass;
import com.volive.mrhow.util.DialogsUtils;
import com.volive.mrhow.util.MainUrl;
import com.volive.mrhow.util.NetworkConnection;
import com.volive.mrhow.util.PreferenceUtils;
import com.volive.mrhow.util.RetrofitClass;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrainerProfileActivity extends AppCompatActivity {

    RecyclerView courses_recyclerview;
    RoundCornerProgressBar progress1, progress2, progress3, progress4, progress5;
    ImageView image_back, share_image;
    CircleImageView image_trainer;
    TextView text_trainername, text_skills, text_studentscount, text_coursescount, text_rating, text_description, text_seemore,
            text_reviewcount, text_averagerating, text_switchtostudentview, text_coursesby;
    PreferenceUtils preferenceUtils;
    NetworkConnection networkConnection;
    ProgressDialog progressDialog;
    RecyclerView rating_recyclerview;
    Button button_viewmore;
    String userid = "", author_id = "",language="";
    ArrayList<NewCourseModels> newCourseModels = new ArrayList<>();
    ArrayList<ReviewsModels> reviewsModels = new ArrayList<>();
    ImageView image_one, image_two, image_three, image_four, image_five;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_profile);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//only working in api level 26
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.lightgreen));
        }

        preferenceUtils = new PreferenceUtils(TrainerProfileActivity.this);
        networkConnection = new NetworkConnection(TrainerProfileActivity.this);
        language = preferenceUtils.getStringFromPreference(PreferenceUtils.Language,"");


        userid = preferenceUtils.getStringFromPreference(PreferenceUtils.userid, "");
        author_id = getIntent().getStringExtra("author_id");

        initializeUI();
        initializeValues();

        if (networkConnection.isConnectingToInternet()) {
            trainerProfile();
        } else {
            Toast.makeText(TrainerProfileActivity.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
        }


    }


    private void initializeUI() {
        image_one = findViewById(R.id.image_one);
        image_two = findViewById(R.id.image_two);
        image_three = findViewById(R.id.image_three);
        image_four = findViewById(R.id.image_four);
        image_five = findViewById(R.id.image_five);
        image_back = findViewById(R.id.image_back);
        image_trainer = findViewById(R.id.image_trainer);
        share_image = findViewById(R.id.share_image);
        progress1 = (RoundCornerProgressBar) findViewById(R.id.progress_1);
        progress2 = (RoundCornerProgressBar) findViewById(R.id.progress_2);
        progress3 = (RoundCornerProgressBar) findViewById(R.id.progress_3);
        progress4 = (RoundCornerProgressBar) findViewById(R.id.progress_4);
        progress5 = (RoundCornerProgressBar) findViewById(R.id.progress_5);
        text_trainername = findViewById(R.id.text_trainername);
        text_skills = findViewById(R.id.text_skills);
        text_studentscount = findViewById(R.id.text_studentscount);
        text_coursescount = findViewById(R.id.text_coursescount);
        text_rating = findViewById(R.id.text_rating);
        text_description = findViewById(R.id.text_description);
        text_seemore = findViewById(R.id.text_seemore);
        text_reviewcount = findViewById(R.id.text_reviewcount);
        text_averagerating = findViewById(R.id.text_averagerating);
        rating_recyclerview = findViewById(R.id.rating_recyclerview);
        button_viewmore = findViewById(R.id.button_viewmore);
        text_switchtostudentview = findViewById(R.id.text_switchtostudentview);
        text_coursesby = findViewById(R.id.text_coursesby);
        courses_recyclerview = findViewById(R.id.courses_recyclerview);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(TrainerProfileActivity.this);
        rating_recyclerview.setLayoutManager(mLayoutManager);
        rating_recyclerview.setItemAnimator(new DefaultItemAnimator());

        courses_recyclerview.setHasFixedSize(true);
        courses_recyclerview.setLayoutManager(new LinearLayoutManager(TrainerProfileActivity.this, LinearLayoutManager.HORIZONTAL, false));


    }

    private void initializeValues() {
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        button_viewmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reviewsModels.size() > 0) {
                    button_viewmore.setVisibility(View.GONE);
                    ReviewsAdapter reviewsAdapter = new ReviewsAdapter(TrainerProfileActivity.this, reviewsModels, "viewmore");
                    rating_recyclerview.setAdapter(reviewsAdapter);
                    reviewsAdapter.notifyDataSetChanged();
                }

            }
        });

        share_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                String shareMessage = "\nLet me recommend you this application\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            }
        });


        progress1.setProgressColor(Color.parseColor("#0DCD78"));
        progress1.setProgressBackgroundColor(Color.parseColor("#E5E2E2"));
        progress1.setMax(100);

        progress2.setProgressColor(Color.parseColor("#0DCD78"));
        progress2.setProgressBackgroundColor(Color.parseColor("#E5E2E2"));
        progress2.setMax(100);


        progress3.setProgressColor(Color.parseColor("#0DCD78"));
        progress3.setProgressBackgroundColor(Color.parseColor("#E5E2E2"));
        progress3.setMax(100);


        progress4.setProgressColor(Color.parseColor("#0DCD78"));
        progress4.setProgressBackgroundColor(Color.parseColor("#E5E2E2"));
        progress4.setMax(100);


        progress5.setProgressColor(Color.parseColor("#0DCD78"));
        progress5.setProgressBackgroundColor(Color.parseColor("#E5E2E2"));
        progress5.setMax(100);


    }


    private void trainerProfile() {
        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement> call = apiClass.trainerprofile(author_id, language, MainUrl.apikey);
        progressDialog = DialogsUtils.showProgressDialog(TrainerProfileActivity.this, String.valueOf(getResources().getText(R.string.loading)));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Log.e("trainerprofile", response.body().toString());
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    try {

                        JSONObject jsonObject = new JSONObject(response.body().toString());

                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status.equalsIgnoreCase("1")) {

                            JSONObject dataobject = jsonObject.getJSONObject("data");

                            String name = dataobject.getString("name");
                            String profile_pic = dataobject.getString("profile_pic");
                            String skills = dataobject.getString("skills");
                            String description = dataobject.getString("description");
                            String courses_count = dataobject.getString("courses_count");
                            String average_rating = dataobject.getString("average_rating");
                            String students_count = dataobject.getString("students_count");
                            String total_reviews = dataobject.getString("total_reviews");

                            text_trainername.setText(name);
                            Glide.with(TrainerProfileActivity.this).load(MainUrl.imageurl + profile_pic).into(image_trainer);
                            text_skills.setText(skills);
                            text_studentscount.setText(students_count);
                            text_coursescount.setText(courses_count);
                            text_rating.setText(average_rating);
                            text_averagerating.setText(average_rating);
                            text_description.setText(description);
                            text_reviewcount.setText(total_reviews);
                            text_coursesby.setText(getResources().getText(R.string.coursesby) + name);


                            if (average_rating.equalsIgnoreCase("0")) {
                                image_one.setImageResource(R.drawable.star_gray);
                                image_two.setImageResource(R.drawable.star_gray);
                                image_three.setImageResource(R.drawable.star_gray);
                                image_four.setImageResource(R.drawable.star_gray);
                                image_five.setImageResource(R.drawable.star_gray);
                            } else if (average_rating.equalsIgnoreCase("1")) {
                                image_one.setImageResource(R.drawable.star);
                                image_two.setImageResource(R.drawable.star_gray);
                                image_three.setImageResource(R.drawable.star_gray);
                                image_four.setImageResource(R.drawable.star_gray);
                                image_five.setImageResource(R.drawable.star_gray);
                            } else if (average_rating.equalsIgnoreCase("2")) {
                                image_one.setImageResource(R.drawable.star);
                                image_two.setImageResource(R.drawable.star);
                                image_three.setImageResource(R.drawable.star_gray);
                                image_four.setImageResource(R.drawable.star_gray);
                                image_five.setImageResource(R.drawable.star_gray);
                            } else if (average_rating.equalsIgnoreCase("3")) {
                                image_one.setImageResource(R.drawable.star);
                                image_two.setImageResource(R.drawable.star);
                                image_three.setImageResource(R.drawable.star);
                                image_four.setImageResource(R.drawable.star_gray);
                                image_five.setImageResource(R.drawable.star_gray);
                            } else if (average_rating.equalsIgnoreCase("4")) {
                                image_one.setImageResource(R.drawable.star);
                                image_two.setImageResource(R.drawable.star);
                                image_three.setImageResource(R.drawable.star);
                                image_four.setImageResource(R.drawable.star);
                                image_five.setImageResource(R.drawable.star_gray);
                            } else if (average_rating.equalsIgnoreCase("5")) {
                                image_one.setImageResource(R.drawable.star);
                                image_two.setImageResource(R.drawable.star);
                                image_three.setImageResource(R.drawable.star);
                                image_four.setImageResource(R.drawable.star);
                                image_five.setImageResource(R.drawable.star);
                            } else {
                                image_one.setImageResource(R.drawable.star_gray);
                                image_two.setImageResource(R.drawable.star_gray);
                                image_three.setImageResource(R.drawable.star_gray);
                                image_four.setImageResource(R.drawable.star_gray);
                                image_five.setImageResource(R.drawable.star_gray);
                            }


                            JSONArray coursesArray = dataobject.getJSONArray("courses");

                            newCourseModels = new ArrayList<>();

                            if (coursesArray.length() > 0) {

                                for (int i = 0; i < coursesArray.length(); i++) {

                                    JSONObject coursesobject = coursesArray.getJSONObject(i);
                                    String course_id = coursesobject.getString("course_id");
                                    String category_name = coursesobject.getString("category_name");
                                    String course_title = coursesobject.getString("course_title");
                                    String price = coursesobject.getString("price");
                                    String offer_price = coursesobject.getString("offer_price");
                                    String cover = coursesobject.getString("cover");
                                    String cover_type = coursesobject.getString("cover_type");
                                    String duration = coursesobject.getString("duration");
                                    String total_ratings = coursesobject.getString("total_ratings");
                                    String purchased = coursesobject.getString("purchased");
                                    String tags = coursesobject.getString("tags");
                                    String thumbnail = coursesobject.getString("thumbnail");
                                    String currency=coursesobject.getString("currency");


                                    NewCourseModels newcourse = new NewCourseModels(course_id, category_name, course_title,
                                            price, offer_price, cover, cover_type, total_ratings, purchased, tags, duration, thumbnail,currency);
                                    newCourseModels.add(newcourse);

                                }

                                NewCourseAdapter newCourseAdapter = new NewCourseAdapter(TrainerProfileActivity.this, newCourseModels);
                                courses_recyclerview.setAdapter(newCourseAdapter);
                                newCourseAdapter.notifyDataSetChanged();


                            }

                            JSONObject ratingsobject = dataobject.getJSONObject("ratings");
                            String rating1 = ratingsobject.getString("rating1");
                            String rating2 = ratingsobject.getString("rating2");
                            String rating3 = ratingsobject.getString("rating3");
                            String rating4 = ratingsobject.getString("rating4");
                            String rating5 = ratingsobject.getString("rating5");

                            progress1.setProgress(Float.parseFloat(rating5));
                            progress2.setProgress(Float.parseFloat(rating4));
                            progress3.setProgress(Float.parseFloat(rating3));
                            progress4.setProgress(Float.parseFloat(rating2));
                            progress5.setProgress(Float.parseFloat(rating1));


                            JSONArray reviewsArray = dataobject.getJSONArray("reviews");
                            reviewsModels = new ArrayList<>();
                            if (reviewsArray.length() > 0) {

                                button_viewmore.setVisibility(View.VISIBLE);

                                for (int k = 0; k < reviewsArray.length(); k++) {

                                    JSONObject reviewsobject = reviewsArray.getJSONObject(k);

                                    String review_id = reviewsobject.getString("review_id");
                                    String user_id = reviewsobject.getString("user_id");
                                    String review = reviewsobject.getString("review");
                                    String rating = reviewsobject.getString("rating");
                                    String created_on = reviewsobject.getString("created_on");
                                    String reviewname = reviewsobject.getString("name");

                                    ReviewsModels reviewmo = new ReviewsModels(review_id, user_id, review, rating, created_on, reviewname);
                                    reviewsModels.add(reviewmo);


                                }

                                ReviewsAdapter reviewsAdapter = new ReviewsAdapter(TrainerProfileActivity.this, reviewsModels, "withoutviewmore");
                                rating_recyclerview.setAdapter(reviewsAdapter);
                                reviewsAdapter.notifyDataSetChanged();


                            } else {
                                button_viewmore.setVisibility(View.GONE);
                            }


                        } else {
                            Toast.makeText(TrainerProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                        }


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
                Log.e("dfsdf ", t.toString());

            }
        });

    }


}
