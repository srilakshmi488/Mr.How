package com.volive.mrhow.fragments;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.viewpagerindicator.CirclePageIndicator;
import com.volive.mrhow.R;
import com.volive.mrhow.activities.CategiriesActivity;
import com.volive.mrhow.activities.FiltersActivity;
import com.volive.mrhow.adapters.CategoriesAdapter;
import com.volive.mrhow.adapters.NewCourseAdapter;
import com.volive.mrhow.adapters.RecommendedCourseAdapter;
import com.volive.mrhow.adapters.ViewPagerAdapter2;
import com.volive.mrhow.models.BannersModels;
import com.volive.mrhow.models.CategoriesModels;
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
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiscoverFragment extends Fragment {
    static int NUM_PAGES = 0;
    static int currentPage = 0;
    ImageView filter_image, image_search,home_logo;
    ViewPager viewpager;
    CirclePageIndicator circlepageindicator;
    RecyclerView categories_recycler_view, newcourse_recyclerview, recommendedcourse_recyclerview;
    ArrayList<CategoriesModels> categoriesModels = new ArrayList<>();
    ArrayList<NewCourseModels> newCourseModels = new ArrayList<>();
    ArrayList<BannersModels> bannersModels = new ArrayList<>();

    PreferenceUtils preferenceUtils;
    NetworkConnection networkConnection;
    ProgressDialog progressDialog, progressDialog1;
    ViewPagerAdapter2 viewPagerAdapter;
    EditText edit_search;
    String searchstring="",language="";



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        Window window = getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//only working in api level 26
            window.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.lightgreen));
        }

        preferenceUtils = new PreferenceUtils(getActivity());
        networkConnection = new NetworkConnection(getActivity());
        language = preferenceUtils.getStringFromPreference(PreferenceUtils.Language,"");

        initializeUI(view);
        initializeValues();

        if (networkConnection.isConnectingToInternet()) {
            discover();
            discoverCourses();
        } else {
            Toast.makeText(getActivity(), getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
        }

        return view;
    }


    private void initializeUI(View view) {
        edit_search = view.findViewById(R.id.edit_search);
        filter_image = view.findViewById(R.id.filter_image);
        image_search = view.findViewById(R.id.image_search);
        viewpager = (ViewPager) view.findViewById(R.id.viewpager);
        circlepageindicator = (CirclePageIndicator) view.findViewById(R.id.circlepageindicator);
        categories_recycler_view = view.findViewById(R.id.categories_recycler_view);
        newcourse_recyclerview = view.findViewById(R.id.newcourse_recyclerview);
        recommendedcourse_recyclerview = view.findViewById(R.id.recommendedcourse_recyclerview);
        home_logo = view.findViewById(R.id.home_logo);

        categories_recycler_view.setHasFixedSize(true);
        categories_recycler_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


        newcourse_recyclerview.setHasFixedSize(true);
        newcourse_recyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


        recommendedcourse_recyclerview.setHasFixedSize(true);
        recommendedcourse_recyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

    }


    private void initializeValues() {

        image_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchstring=edit_search.getText().toString();
                if(!searchstring.isEmpty()) {
                    Intent intent = new Intent(getActivity(), CategiriesActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("Action", "search");
                    intent.putExtra("searchstring", searchstring);
                    startActivity(intent);

                    edit_search.setText("");
                }else {
                    Toast.makeText(getActivity(), getResources().getText(R.string.pleaseentersearchfield), Toast.LENGTH_SHORT).show();
                }

            }
        });


        filter_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FiltersActivity.class));
            }
        });

    }


    private void discover() {
        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement> call = apiClass.discover(MainUrl.apikey, language);
        View view = getActivity().getWindow().getDecorView();
        view.setBackgroundColor(getResources().getColor(R.color.whitecolour));

        progressDialog = DialogsUtils.showProgressDialog(getActivity(), String.valueOf(getResources().getText(R.string.loading)));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDialog.dismiss();

                Log.e("sdgfg ",response.body().toString());

                if (response.isSuccessful()) {
                    try {

                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        String base_url = jsonObject.getString("base_url");

                        if (status.equalsIgnoreCase("1")) {

                            JSONObject dataobject = jsonObject.getJSONObject("data");

                            String bg_color = dataobject.getString("bg_color");
                            View view = getActivity().getWindow().getDecorView();
                            view.setBackgroundColor(Color.parseColor(bg_color));


                            JSONArray bannerArray = dataobject.getJSONArray("banner");

                            if (bannerArray.length() > 0) {

                                for (int i = 0; i < bannerArray.length(); i++) {
                                    JSONObject bannerobject = bannerArray.getJSONObject(i);
                                    String course_id = bannerobject.getString("course_id");
                                    String category_name = bannerobject.getString("category_name");
                                    String course_title = bannerobject.getString("course_title");
                                    String price = bannerobject.getString("price");
                                    String cover = bannerobject.getString("cover");
                                    String ratings = bannerobject.getString("ratings");
                                    String currency = bannerobject.getString("currency");

                                    BannersModels bannermodel = new BannersModels(course_id, category_name, course_title,
                                            price, cover, ratings,currency);

                                    bannersModels.add(bannermodel);

                                }

                                setImages();


                            }


                            categoriesModels = new ArrayList<>();

                            JSONArray categoriesArray = dataobject.getJSONArray("categories");

                            if (categoriesArray.length() > 0) {
                                for (int j = 0; j < categoriesArray.length(); j++) {

                                    JSONObject catagiriesobject = categoriesArray.getJSONObject(j);
                                    String category_id = catagiriesobject.getString("category_id");
                                    String category_name = catagiriesobject.getString("category_name");
                                    String icon = catagiriesobject.getString("icon");

                                    CategoriesModels catagirymodel = new CategoriesModels(category_id, category_name, icon);
                                    categoriesModels.add(catagirymodel);

                                }
                            }

                            CategoriesAdapter categoriesAdapter = new CategoriesAdapter(getActivity(), categoriesModels);
                            categories_recycler_view.setAdapter(categoriesAdapter);
                            categoriesAdapter.notifyDataSetChanged();


                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Log.e("sdkfdsg ", e.toString());
                    }
                }

            }

            @Override
        public void onFailure(Call<JsonElement> call, Throwable t) {
            progressDialog.dismiss();
            Log.e("kdsfhds", t.toString());

        }
    });


    }

    private void setImages() {

        if (bannersModels != null) {

            viewPagerAdapter = new ViewPagerAdapter2(getActivity(), bannersModels);
            viewpager.setAdapter(viewPagerAdapter);

            circlepageindicator.setViewPager(viewpager);
            final float density = getResources().getDisplayMetrics().density;
            circlepageindicator.setRadius(5 * density);
            circlepageindicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageSelected(int position) {
                    currentPage = position;

                }

                @Override
                public void onPageScrolled(int pos, float arg1, int arg2) {

                }

                @Override
                public void onPageScrollStateChanged(int pos) {

                }
            });

//            NUM_PAGES = bannersModels.size();
//            final Handler handler = new Handler();
//            final Runnable Update = new Runnable() {
//                public void run() {
//                    if (currentPage == NUM_PAGES) {
//                        currentPage = 0;
//                    }
//                    viewpager.setCurrentItem(currentPage++, true);
//                }
//            };
//            Timer swipeTimer = new Timer();
//            swipeTimer.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    handler.post(Update);
//                }
//            }, 2000, 2000);


        }


    }

    private void discoverCourses() {
        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement> call = apiClass.discovercourses(MainUrl.apikey, language);
        progressDialog1 = DialogsUtils.showProgressDialog(getActivity(), String.valueOf(getResources().getText(R.string.loading)));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDialog1.dismiss();

                Log.e("discoverfdh ",response.body().toString());

                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        if (status.equalsIgnoreCase("1")) {

                            JSONObject dataobject = jsonObject.getJSONObject("data");

                            JSONArray newcoursesArray = dataobject.getJSONArray("new_courses");

                            newCourseModels = new ArrayList<>();

                            if (newcoursesArray.length() > 0) {
                                for (int i = 0; i < newcoursesArray.length(); i++) {

                                    JSONObject newcoursesobject = newcoursesArray.getJSONObject(i);
                                    String course_id = newcoursesobject.getString("course_id");
                                    String category_name = newcoursesobject.getString("category_name");
                                    String course_title = newcoursesobject.getString("course_title");
                                    String price = newcoursesobject.getString("price");
                                    String offer_price = newcoursesobject.getString("offer_price");
                                    String cover = newcoursesobject.getString("cover");
                                    String cover_type = newcoursesobject.getString("cover_type");
                                    String total_ratings = newcoursesobject.getString("total_ratings");
                                    String purchased = newcoursesobject.getString("purchased");
                                    String tags = newcoursesobject.getString("tags");
                                    String duration = newcoursesobject.getString("duration");
                                    String thumbnail = newcoursesobject.getString("thumbnail");
                                    String currency = newcoursesobject.getString("currency");

                                    NewCourseModels newcourse = new NewCourseModels(course_id, category_name, course_title,
                                            price, offer_price, cover,cover_type, total_ratings, purchased, tags,duration,thumbnail,currency);
                                    newCourseModels.add(newcourse);


                                }


                                NewCourseAdapter newCourseAdapter = new NewCourseAdapter(getActivity(), newCourseModels);
                                newcourse_recyclerview.setAdapter(newCourseAdapter);
                                newCourseAdapter.notifyDataSetChanged();


                            }


                            newCourseModels = new ArrayList<>();
                            JSONArray recommendedArray = dataobject.getJSONArray("recommended");

                            if (recommendedArray.length() > 0) {
                                for (int j = 0; j < recommendedArray.length(); j++) {
                                    JSONObject recommendedobject = recommendedArray.getJSONObject(j);
                                    String course_id = recommendedobject.getString("course_id");
                                    String category_name = recommendedobject.getString("category_name");
                                    String course_title = recommendedobject.getString("course_title");
                                    String price = recommendedobject.getString("price");
                                    String offer_price = recommendedobject.getString("offer_price");
                                    String cover = recommendedobject.getString("cover");
                                    String total_ratings = recommendedobject.getString("total_ratings");
                                    String tags = recommendedobject.getString("tags");
                                    String cover_type = recommendedobject.getString("cover_type");
                                    String purchased=recommendedobject.getString("purchased");
                                    String duration =recommendedobject.getString("duration");
                                    String thumbnail = recommendedobject.getString("thumbnail");
                                    String currency =recommendedobject.getString("currency");

                                    NewCourseModels newcourse = new NewCourseModels(course_id, category_name, course_title,
                                            price, offer_price, cover,cover_type, total_ratings, purchased, tags,duration,thumbnail,currency);
                                    newCourseModels.add(newcourse);


                                }

                                RecommendedCourseAdapter recommendedCourseAdapter = new RecommendedCourseAdapter(getActivity(), newCourseModels);
                                recommendedcourse_recyclerview.setAdapter(recommendedCourseAdapter);
                                recommendedCourseAdapter.notifyDataSetChanged();


                            }


                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog1.dismiss();
                        Log.e("sdkfsdf ", e.toString());
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog1.dismiss();
                Log.e("dskfsdf ", t.toString());

            }
        });


    }

}
