package com.volive.mrhow.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.viewpagerindicator.CirclePageIndicator;
import com.volive.mrhow.R;
import com.volive.mrhow.adapters.ViewPagerAdapter;
import com.volive.mrhow.models.SlidingImageModels;
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

public class MainActivity extends AppCompatActivity {
    static int NUM_PAGES = 0;
    static int currentPage = 0;
    ViewPager viewPager;
    CirclePageIndicator circlepageindicator;
    Button btn_next, btn_getstarted, btn_skip;
    PreferenceUtils preferenceUtils;
    NetworkConnection networkConnection;
    ArrayList<SlidingImageModels> slidingImageModels = new ArrayList<>();
    ViewPagerAdapter viewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            if (window != null) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            }
        }

        setContentView(R.layout.activity_main);

        preferenceUtils = new PreferenceUtils(MainActivity.this);
        networkConnection = new NetworkConnection(MainActivity.this);

        initializeUI();
        initializeValues();
        if (networkConnection.isConnectingToInternet()) {
            homeScreens();
        } else {
            Toast.makeText(MainActivity.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
        }


    }


    private void initializeUI() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        circlepageindicator = (CirclePageIndicator) findViewById(R.id.circlepageindicator);
        btn_next = findViewById(R.id.btn_next);
        btn_getstarted = findViewById(R.id.btn_getstarted);
        btn_skip = findViewById(R.id.btn_skip);

    }

    private void initializeValues() {

        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VideoScreenActivity.class);
                startActivity(intent);

            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int current = getItem(+1);
                if (current < slidingImageModels.size()) {
                    viewPager.setCurrentItem(current);
                } else {
                    Intent intent = new Intent(MainActivity.this, VideoScreenActivity.class);
                    startActivity(intent);
                }

            }
        });

        btn_getstarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VideoScreenActivity.class);
                startActivity(intent);

            }
        });


    }


    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }


    private void homeScreens() {
        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement> call = apiClass.homescreens(MainUrl.apikey, "en");
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {

                    slidingImageModels = new ArrayList<>();

                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        Log.e("sdkgdfsg ", response.body().toString());
                        String status = jsonObject.getString("status");

                        JSONArray dataArray = jsonObject.getJSONArray("data");

                        if (dataArray.length() > 0) {

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject dataobject = dataArray.getJSONObject(i);
                                String image = dataobject.getString("image");
                                String title = dataobject.getString("title");
                                String text = dataobject.getString("text");

                                SlidingImageModels slidingimage = new SlidingImageModels(image, title, text);
                                slidingImageModels.add(slidingimage);


                            }
                        }


                        viewPagerAdapter = new ViewPagerAdapter(MainActivity.this, slidingImageModels);
                        viewPager.setAdapter(viewPagerAdapter);
                        circlepageindicator.setViewPager(viewPager);
                        final float density = getResources().getDisplayMetrics().density;
                        circlepageindicator.setRadius(5 * density);
                        circlepageindicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                            @Override
                            public void onPageSelected(int position) {
                                currentPage = position;

                                if (position == slidingImageModels.size() - 1) {
                                    btn_getstarted.setVisibility(View.VISIBLE);
                                    btn_skip.setVisibility(View.GONE);
                                    btn_next.setVisibility(View.GONE);
                                } else {

                                    btn_next.setVisibility(View.VISIBLE);
                                    btn_skip.setVisibility(View.VISIBLE);
                                    btn_getstarted.setVisibility(View.GONE);
                                }


                            }

                            @Override
                            public void onPageScrolled(int pos, float arg1, int arg2) {

                            }

                            @Override
                            public void onPageScrollStateChanged(int pos) {

                            }
                        });

//                        NUM_PAGES = slidingImageModels.size();
//                        final Handler handler = new Handler();
//                        final Runnable Update = new Runnable() {
//                            public void run() {
//                                if (currentPage == NUM_PAGES) {
//                                    currentPage = 0;
//                                }
//                                viewPager.setCurrentItem(currentPage++, true);
//                            }
//                        };
//                        Timer swipeTimer = new Timer();
//                        swipeTimer.schedule(new TimerTask() {
//                            @Override
//                            public void run() {
//                                handler.post(Update);
//                            }
//                        }, 2000, 2000);


                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("dfskdfg ", e.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e("asdkfsdf ", t.toString());

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }


}
