package com.volive.mrhow.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonElement;
import com.volive.mrhow.BuildConfig;
import com.volive.mrhow.R;
import com.volive.mrhow.adapters.PageAdapter;
import com.volive.mrhow.models.MoreCommentsModels;
import com.volive.mrhow.models.StudentProjectModels;
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

public class ViewDetailsActivity extends AppCompatActivity {

    public static String courseid = "";
    public static ArrayList<StudentProjectModels> studentProjectModels = new ArrayList<>();
    public static ArrayList<MoreCommentsModels> moreCommentsModels = new ArrayList<>();
    public static String purchase_status = "";
    ImageView image_back, share_image, image_detailpic, image_play;
    TabLayout tabLayout;
    ViewPager viewPager;
    PreferenceUtils preferenceUtils;
    NetworkConnection networkConnection;
    ProgressDialog progressDialog, progressDialog1;
    String userid = "", cover_type = "", cover = "", course_title = "", course_id = "", price = "",language="";
    Button button_addtowishlist, buy_now_btn;
    TextView text_price, text_offerprice;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//only working in api level 26
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.lightgreen));
        }

        preferenceUtils = new PreferenceUtils(ViewDetailsActivity.this);
        networkConnection = new NetworkConnection(ViewDetailsActivity.this);
        userid = preferenceUtils.getStringFromPreference(PreferenceUtils.userid, "");
        language = preferenceUtils.getStringFromPreference(PreferenceUtils.Language,"");

        courseid = getIntent().getStringExtra("courseid");
//        covertype = getIntent().getStringExtra("covertype");


        initializeUI();
        initializeValues();

        if (networkConnection.isConnectingToInternet()) {
            courseDetails();
        } else {
            Toast.makeText(ViewDetailsActivity.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
        }

    }


    private void initializeValues() {

        image_detailpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cover_type.equalsIgnoreCase("video")) {
                    Intent intent = new Intent(ViewDetailsActivity.this, VideoPlayerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("cover", cover);
                    intent.putExtra("course_title", course_title);
                    startActivity(intent);
                }
            }
        });

        button_addtowishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToWishList();

            }
        });

        buy_now_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewDetailsActivity.this, PurchageDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("totalprice", price);
                intent.putExtra("coursetitle", course_title);
                intent.putExtra("courseid", course_id);
                startActivity(intent);
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

        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                Intent intent = new Intent(ViewDetailsActivity.this, HomeNavigation.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
            }
        });

        tabLayout.addTab(tabLayout.newTab().setText(getResources().getText(R.string.lessons)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getText(R.string.about)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getText(R.string.more)));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        PageAdapter adapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {


            }
        });

    }

    private void initializeUI() {
        share_image = findViewById(R.id.share_image);
        image_back = findViewById(R.id.image_back);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.pager);
        image_detailpic = findViewById(R.id.image_detailpic);
        image_play = findViewById(R.id.image_play);

        text_price = findViewById(R.id.text_price);
        text_offerprice = findViewById(R.id.text_offerprice);
        buy_now_btn = findViewById(R.id.buy_now_btn);
        button_addtowishlist = findViewById(R.id.button_addtowishlist);

    }

    private void addToWishList() {
        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement> call = apiClass.addtowishlist(userid, ViewDetailsActivity.courseid, language, MainUrl.apikey);
        progressDialog1 = DialogsUtils.showProgressDialog(ViewDetailsActivity.this, String.valueOf(getResources().getText(R.string.loading)));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Log.e("wishlist  ", response.body().toString());

                progressDialog1.dismiss();
                Log.e("sdgfsdf ", response.body().toString());

                if (response.isSuccessful()) {
                    progressDialog1.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        if (status.equalsIgnoreCase("1")) {
                            String wishlist = jsonObject.getString("wishlist");
                            Toast.makeText(ViewDetailsActivity.this, message, Toast.LENGTH_SHORT).show();

                            if (wishlist.equalsIgnoreCase("1")) {
                                button_addtowishlist.setText(getResources().getText(R.string.removewishlist));
                            } else if (wishlist.equalsIgnoreCase("0")) {
                                button_addtowishlist.setText(getResources().getText(R.string.addtowishlist));
                            }

                        } else {
                            Toast.makeText(ViewDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog1.dismiss();
                        Log.e("sdfdsg ", e.toString());
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog1.dismiss();
                Log.e("sgdfg ", t.toString());

            }
        });

    }

    private void courseDetails() {
        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement> call = apiClass.coursedetails(courseid, userid, language, MainUrl.apikey);
        progressDialog = DialogsUtils.showProgressDialog(ViewDetailsActivity.this, String.valueOf(getResources().getText(R.string.loading)));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Log.e("dgfdg ", response.body().toString());
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    try {

                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status.equalsIgnoreCase("1")) {

                            JSONObject dataobject = jsonObject.getJSONObject("data");

                            JSONObject detailsobject = dataobject.getJSONObject("details");
                            course_id = detailsobject.getString("course_id");
                            String category_name = detailsobject.getString("category_name");
                            course_title = detailsobject.getString("course_title");
                            price = detailsobject.getString("price");
                            String offer_price = detailsobject.getString("offer_price");
                            String cover = detailsobject.getString("cover");
                            String cover_type = detailsobject.getString("cover_type");
                            String duration = detailsobject.getString("duration");
                            String ratings = detailsobject.getString("ratings");
                            String total_ratings = detailsobject.getString("total_ratings");
                            String purchased = detailsobject.getString("purchased");
                            String tags = detailsobject.getString("tags");
                            String name = detailsobject.getString("name");
                            String profile_pic = detailsobject.getString("profile_pic");
                            String followers = detailsobject.getString("followers");
                            String wishlist = detailsobject.getString("wishlist");
                            purchase_status = detailsobject.getString("purchase_status");
                            String thumbnail = detailsobject.getString("thumbnail");
                            String currency = detailsobject.getString("currency");


                            if (wishlist.equalsIgnoreCase("1")) {
                                button_addtowishlist.setText(getResources().getText(R.string.removewishlist));
                            } else if (wishlist.equalsIgnoreCase("0")) {
                                button_addtowishlist.setText(getResources().getText(R.string.addtowishlist));
                            }

                            if (purchase_status.equalsIgnoreCase("1")) {
                                buy_now_btn.setVisibility(View.GONE);
                            } else {
                                buy_now_btn.setVisibility(View.VISIBLE);
                            }

                            if(offer_price.equalsIgnoreCase("")||offer_price.equalsIgnoreCase("0")){
                                text_price.setText(price + " " + currency);
                            }else {
                                text_price.setText(price + " " + currency);
                                text_offerprice.setText(offer_price + " " + currency);
//                                text_price.setPaintFlags(text_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                text_price.setBackgroundResource(R.drawable.strikeline);
                            }



                            if (cover_type.equalsIgnoreCase("video")) {
                                image_play.setVisibility(View.VISIBLE);
                                Glide.with(ViewDetailsActivity.this).load(MainUrl.imageurl + thumbnail).into(image_detailpic);
                            } else {
                                Glide.with(ViewDetailsActivity.this).load(MainUrl.imageurl + cover).into(image_detailpic);
                                image_play.setVisibility(View.GONE);
                            }


                            moreCommentsModels = new ArrayList<>();
                            JSONArray commentsArray = dataobject.getJSONArray("comments");
                            if (commentsArray.length() > 0) {
                                for (int i = 0; i < commentsArray.length(); i++) {
                                    JSONObject commentsobject = commentsArray.getJSONObject(i);

                                    String comment_id = commentsobject.getString("comment_id");
                                    String user_id = commentsobject.getString("user_id");
                                    String comment = commentsobject.getString("comment");
                                    String comment_rating = commentsobject.getString("comment_rating");
                                    String created_on = commentsobject.getString("created_on");
                                    String time = commentsobject.getString("time");

                                    MoreCommentsModels morecomment = new MoreCommentsModels(comment_id, user_id, comment,
                                            comment_rating, created_on, time);
                                    moreCommentsModels.add(morecomment);

                                }
                            }

                            studentProjectModels = new ArrayList<>();
                            JSONArray projectsArray = dataobject.getJSONArray("projects");
                            if (projectsArray.length() > 0) {

                                for (int j = 0; j < projectsArray.length(); j++) {

                                    JSONObject projectsobject = projectsArray.getJSONObject(j);
                                    String project_id = projectsobject.getString("project_id");
                                    String project_banner = projectsobject.getString("project_banner");
                                    String banner_type = projectsobject.getString("banner_type");
                                    String project_thumbnail = projectsobject.getString("project_thumbnail");

                                    StudentProjectModels studentproject = new StudentProjectModels(project_id, project_banner, banner_type, project_thumbnail);
                                    studentProjectModels.add(studentproject);

                                }

                            }


                        } else {
                            Toast.makeText(ViewDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                        }

                        progressDialog.dismiss();

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Log.e("sdfgsdg ", e.toString());
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("sdsd ", t.toString());
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
//        Intent intent = new Intent(ViewDetailsActivity.this,HomeNavigation.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
    }
}
