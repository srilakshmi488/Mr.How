package com.volive.mrhow.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonElement;
import com.volive.mrhow.R;
import com.volive.mrhow.activities.PurchageDetailsActivity;
import com.volive.mrhow.activities.TrainerProfileActivity;
import com.volive.mrhow.activities.VideoPlayerActivity;
import com.volive.mrhow.activities.ViewDetailsActivity;
import com.volive.mrhow.adapters.LessonAdapter;
import com.volive.mrhow.adapters.MaterialAdapter;
import com.volive.mrhow.models.LessonModels;
import com.volive.mrhow.models.MaterialModels;
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

public class LessionsFragment extends Fragment {
    RecyclerView lessons_recyclerview, materials_recyclerview;
    ArrayList<LessonModels> lessonModels = new ArrayList<>();
    ArrayList<MaterialModels> materialModels = new ArrayList<>();
    Button button_addtowishlist, follow_btn, buy_now_btn;
    TextView text_coursetitle, text_duration, text_rating, text_purchasedcount, text_trainername, text_followers, text_price, text_offerprice;
    CircleImageView image_trainer;
    PreferenceUtils preferenceUtils;
    NetworkConnection networkConnection;
    ImageView image_minus, image_plus, image_introduction, image_on, image_off, image_play, material_minus, material_plus;
    ProgressDialog progressDialog, progressDialog1, progressDialog2;
    String userid = "", author_id = "", course_title = "", price = "", course_id = "", introduction_video = "",language="";
    LinearLayout linear_trainer;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lessions, container, false);

        preferenceUtils = new PreferenceUtils(getActivity());
        networkConnection = new NetworkConnection(getActivity());
        userid = preferenceUtils.getStringFromPreference(PreferenceUtils.userid, "");
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
        text_coursetitle = view.findViewById(R.id.text_coursetitle);
        text_duration = view.findViewById(R.id.text_duration);
        text_rating = view.findViewById(R.id.text_rating);
        text_purchasedcount = view.findViewById(R.id.text_purchasedcount);
        image_trainer = view.findViewById(R.id.image_trainer);
        text_trainername = view.findViewById(R.id.text_trainername);
        text_followers = view.findViewById(R.id.text_followers);
        lessons_recyclerview = view.findViewById(R.id.lessons_recyclerview);
        follow_btn = view.findViewById(R.id.follow_btn);
        image_minus = view.findViewById(R.id.image_minus);
        image_plus = view.findViewById(R.id.image_plus);
        image_introduction = view.findViewById(R.id.image_introduction);
        image_on = view.findViewById(R.id.image_on);
        image_off = view.findViewById(R.id.image_off);
        linear_trainer = view.findViewById(R.id.linear_trainer);
        image_play = view.findViewById(R.id.image_play);
        materials_recyclerview = view.findViewById(R.id.materials_recyclerview);
        material_minus = view.findViewById(R.id.material_minus);
        material_plus = view.findViewById(R.id.material_plus);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        lessons_recyclerview.setLayoutManager(mLayoutManager);
        lessons_recyclerview.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity());
        materials_recyclerview.setLayoutManager(mLayoutManager1);
        materials_recyclerview.setItemAnimator(new DefaultItemAnimator());


    }

    private void initializeValues() {

        image_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_plus.setVisibility(View.GONE);
                image_minus.setVisibility(View.VISIBLE);
                lessons_recyclerview.setVisibility(View.VISIBLE);

            }
        });

        image_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_minus.setVisibility(View.GONE);
                image_plus.setVisibility(View.VISIBLE);
                lessons_recyclerview.setVisibility(View.GONE);

            }
        });


        material_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                material_plus.setVisibility(View.GONE);
                material_minus.setVisibility(View.VISIBLE);
                materials_recyclerview.setVisibility(View.VISIBLE);

            }
        });

        material_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                material_minus.setVisibility(View.GONE);
                material_plus.setVisibility(View.VISIBLE);
                materials_recyclerview.setVisibility(View.GONE);

            }
        });


        image_trainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TrainerProfileActivity.class);
                intent.putExtra("author_id", author_id);
                getActivity().startActivity(intent);
            }
        });

        linear_trainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TrainerProfileActivity.class);
                intent.putExtra("author_id", author_id);
                getActivity().startActivity(intent);
            }
        });

        image_introduction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
                intent.putExtra("cover", introduction_video);
                intent.putExtra("course_title",course_title);
                startActivity(intent);
            }
        });

        follow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followTrainer();
            }
        });

    }


    private void courseDetails() {
        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement> call = apiClass.coursedetails(ViewDetailsActivity.courseid, preferenceUtils.getStringFromPreference(PreferenceUtils.userid, ""),
                language, MainUrl.apikey);

        progressDialog2 = DialogsUtils.showProgressDialog(getActivity(), String.valueOf(getResources().getText(R.string.loading)));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Log.e("lessson",response.body().toString());

                progressDialog2.dismiss();
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
                            String downloadable = detailsobject.getString("downloadable");
                            String recommended = detailsobject.getString("recommended");
                            String overview = detailsobject.getString("overview");
                            String description = detailsobject.getString("description");
                            String requirements = detailsobject.getString("requirements");
                            author_id = detailsobject.getString("author_id");
                            String name = detailsobject.getString("name");
                            String profile_pic = detailsobject.getString("profile_pic");
                            String followers = detailsobject.getString("followers");
                            introduction_video = detailsobject.getString("introduction_video");
                            String introduction_thumbnail = detailsobject.getString("introduction_thumbnail");
                            String wishlist = detailsobject.getString("wishlist");
                            String followed = detailsobject.getString("followed");
                            String purchase_status = detailsobject.getString("purchase_status");

                            if (followed.equalsIgnoreCase("1")) {
                                follow_btn.setText(getResources().getText(R.string.unfollow));
                            } else if (followed.equalsIgnoreCase("0")) {
                                follow_btn.setText(getResources().getText(R.string.follow));
                            }


                            text_coursetitle.setText(course_title);
                            text_duration.setText(duration);
                            text_rating.setText(total_ratings);
                            text_purchasedcount.setText(purchased);


                            Glide.with(getActivity()).load(MainUrl.imageurl + profile_pic).into(image_trainer);

                            if (cover_type.equalsIgnoreCase("image")) {
                                Glide.with(getActivity()).load(MainUrl.imageurl + introduction_video).into(image_introduction);
                                image_play.setVisibility(View.GONE);
                            } else {
                                Glide.with(getActivity()).load(MainUrl.imageurl + introduction_thumbnail).into(image_introduction);
                                image_play.setVisibility(View.VISIBLE);
                            }


                            if (downloadable.equalsIgnoreCase("1")) {
                                image_on.setVisibility(View.VISIBLE);
                                image_off.setVisibility(View.GONE);
                            } else {
                                image_on.setVisibility(View.GONE);
                                image_off.setVisibility(View.VISIBLE);
                            }

                            text_trainername.setText(name);
                            text_followers.setText(followers + " "+getResources().getText(R.string.followers));

                            lessonModels = new ArrayList<>();

                            JSONArray lessonsArray = dataobject.getJSONArray("lessons");
                            if (lessonsArray.length() > 0) {
                                for (int i = 0; i < lessonsArray.length(); i++) {
                                    JSONObject lessonsobject = lessonsArray.getJSONObject(i);
                                    String lesson_name = lessonsobject.getString("lesson_name");
                                    String video = lessonsobject.getString("video");
                                    String lesson_thumbnail = lessonsobject.getString("lesson_thumbnail");
                                    String lesson_id = lessonsobject.getString("lesson_id");
                                    String lesson_type = lessonsobject.getString("lesson_type");

                                    LessonModels lesson = new LessonModels(lesson_name, video, lesson_thumbnail, lesson_id,lesson_type);
                                    lessonModels.add(lesson);

                                }

                                LessonAdapter lessonAdapter = new LessonAdapter(getActivity(), lessonModels, course_id,purchase_status);
                                lessons_recyclerview.setAdapter(lessonAdapter);
                                lessonAdapter.notifyDataSetChanged();
                            }

                            materialModels = new ArrayList<>();
                            JSONArray materialsArray = dataobject.getJSONArray("materials");
                            if (materialsArray.length() > 0) {
                                for (int i = 0; i < materialsArray.length(); i++) {

                                    JSONObject materialsobject = materialsArray.getJSONObject(i);
                                    String material_name = materialsobject.getString("material_name");
                                    String material_file = materialsobject.getString("material_file");
                                    String material_type = materialsobject.getString("material_type");
                                    String material_thumb = materialsobject.getString("material_thumb");

                                    MaterialModels material = new MaterialModels(material_name, material_file,material_type,material_thumb);
                                    materialModels.add(material);

                                }

                                MaterialAdapter materialAdapter = new MaterialAdapter(getActivity(), materialModels,purchase_status);
                                materials_recyclerview.setAdapter(materialAdapter);
                                materialAdapter.notifyDataSetChanged();

                            }


                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }

                        progressDialog2.dismiss();

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog2.dismiss();
                        Log.e("sdfgsdg ", e.toString());
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog2.dismiss();
                Log.e("sdsd ", t.toString());
            }
        });


    }

    private void followTrainer() {
        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement> call = apiClass.followtrainer(userid, author_id, language, MainUrl.apikey);
        progressDialog = DialogsUtils.showProgressDialog(getActivity(), String.valueOf(getResources().getText(R.string.loading)));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    try {

                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status.equalsIgnoreCase("1")) {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                            String followed = jsonObject.getString("followed");
                            if (followed.equalsIgnoreCase("1")) {
                                follow_btn.setText(getResources().getText(R.string.unfollow));
                            } else if (followed.equalsIgnoreCase("0")) {
                                follow_btn.setText(getResources().getText(R.string.follow));
                            }

                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Log.e("sdgdfg ", e.toString());
                    }


                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("sgkfdgn ", t.toString());

            }
        });


    }

}
