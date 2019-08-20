package com.volive.mrhow.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonElement;
import com.volive.mrhow.R;
import com.volive.mrhow.util.ApiClass;
import com.volive.mrhow.util.DialogsUtils;
import com.volive.mrhow.util.MainUrl;
import com.volive.mrhow.util.NetworkConnection;
import com.volive.mrhow.util.PreferenceUtils;
import com.volive.mrhow.util.RetrofitClass;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RateReviewActivity extends AppCompatActivity {
    ImageView image_back,imageview,image_hotnew,image_new,image_one,image_two,image_three,image_four,image_five;
    TextView text_duration,text_rating,text_purchased,text_title,text_price,text_offerprice;
    EditText edit_comment;
    Button button_submit;
    PreferenceUtils preferenceUtils;
    NetworkConnection networkConnection;
    String userid="",courseid="",image="",tags="",duration="",ratings="",purchased="",price="",offerprice="",title="",rating="";
    String comment="",language="";
    ProgressDialog progressDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_review);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//only working in api level 26
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.lightgreen));
        }

        preferenceUtils = new PreferenceUtils(RateReviewActivity.this);
        networkConnection = new NetworkConnection(RateReviewActivity.this);

        userid = preferenceUtils.getStringFromPreference(PreferenceUtils.userid,"");
        language = preferenceUtils.getStringFromPreference(PreferenceUtils.Language,"");

         courseid = getIntent().getStringExtra("courseid");
         image = getIntent().getStringExtra("image");
         tags = getIntent().getStringExtra("tags");
         duration = getIntent().getStringExtra("duration");
         ratings = getIntent().getStringExtra("ratings");
         purchased = getIntent().getStringExtra("purchased");
         price = getIntent().getStringExtra("price");
         offerprice = getIntent().getStringExtra("offerprice");
         title = getIntent().getStringExtra("title");


        initializeUI();
        initializeValues();


    }



    private void initializeUI() {
        image_back = findViewById(R.id.image_back);
        imageview = findViewById(R.id.imageview);
        image_hotnew = findViewById(R.id.image_hotnew);
        image_new = findViewById(R.id.image_new);
        text_duration = findViewById(R.id.text_duration);
        text_rating = findViewById(R.id.text_rating);
        text_purchased = findViewById(R.id.text_purchased);
        text_title = findViewById(R.id.text_title);
        text_price= findViewById(R.id.text_price);
        text_offerprice = findViewById(R.id.text_offerprice);
        image_one = findViewById(R.id.image_one);
        image_two = findViewById(R.id.image_two);
        image_three = findViewById(R.id.image_three);
        image_four = findViewById(R.id.image_four);
        image_five = findViewById(R.id.image_five);
        edit_comment = findViewById(R.id.edit_comment);
        button_submit = findViewById(R.id.button_submit);

        Glide.with(RateReviewActivity.this).load(MainUrl.imageurl+image).into(imageview);
        if (tags.equalsIgnoreCase("1")) {
            image_hotnew.setVisibility(View.GONE);
            image_new.setVisibility(View.VISIBLE);
        } else {
           image_hotnew.setVisibility(View.VISIBLE);
           image_new.setVisibility(View.GONE);
        }

        text_title.setText(title);
        text_price.setText(price+" SAR");
        text_offerprice.setText(offerprice);
        text_duration.setText(duration);
        text_rating.setText(ratings);
        text_purchased.setText(purchased);


    }

    private void initializeValues() {
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        image_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating = "1";
                image_one.setImageResource(R.drawable.star);
                image_two.setImageResource(R.drawable.star_gray);
                image_three.setImageResource(R.drawable.star_gray);
                image_four.setImageResource(R.drawable.star_gray);
                image_five.setImageResource(R.drawable.star_gray);

            }
        });

        image_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating = "2";
                image_one.setImageResource(R.drawable.star);
                image_two.setImageResource(R.drawable.star);
                image_three.setImageResource(R.drawable.star_gray);
                image_four.setImageResource(R.drawable.star_gray);
                image_five.setImageResource(R.drawable.star_gray);
            }
        });

        image_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating = "3";
                image_one.setImageResource(R.drawable.star);
                image_two.setImageResource(R.drawable.star);
                image_three.setImageResource(R.drawable.star);
                image_four.setImageResource(R.drawable.star_gray);
                image_five.setImageResource(R.drawable.star_gray);
            }
        });

        image_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating = "4";
                image_one.setImageResource(R.drawable.star);
                image_two.setImageResource(R.drawable.star);
                image_three.setImageResource(R.drawable.star);
                image_four.setImageResource(R.drawable.star);
                image_five.setImageResource(R.drawable.star_gray);
            }
        });

        image_five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating = "5";
                image_one.setImageResource(R.drawable.star);
                image_two.setImageResource(R.drawable.star);
                image_three.setImageResource(R.drawable.star);
                image_four.setImageResource(R.drawable.star);
                image_five.setImageResource(R.drawable.star);
            }
        });



        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 comment = edit_comment.getText().toString();
                if(networkConnection.isConnectingToInternet()){

                    if(!comment.isEmpty()&&!rating.isEmpty()){
                        submitCourseReview();

                    }else {
                        Toast.makeText(RateReviewActivity.this, getResources().getText(R.string.pleaseentercommentrating), Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(RateReviewActivity.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void submitCourseReview() {
        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement> call = apiClass.submitcoursereview(userid,courseid,comment,rating,language,MainUrl.apikey);
        progressDialog = DialogsUtils.showProgressDialog(RateReviewActivity.this, String.valueOf(getResources().getText(R.string.loading)));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    try {

                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if(status.equalsIgnoreCase("1")){
                            Toast.makeText(RateReviewActivity.this, message, Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(RateReviewActivity.this,HomeNavigation.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                        }else {
                            Toast.makeText(RateReviewActivity.this, message, Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Log.e("dsfsdf ",e.toString());
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("sdfdsf ",t.toString());

            }
        });

    }
}
