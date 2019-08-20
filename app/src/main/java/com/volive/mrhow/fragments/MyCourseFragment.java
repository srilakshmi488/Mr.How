package com.volive.mrhow.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.volive.mrhow.R;
import com.volive.mrhow.activities.HomeNavigation;
import com.volive.mrhow.adapters.SwipeRecyclerAdapter;
import com.volive.mrhow.adapters.WishlistCourseAdapter;
import com.volive.mrhow.models.EnrolledCourseModels;
import com.volive.mrhow.models.WishlistCourseModels;
import com.volive.mrhow.util.ApiClass;
import com.volive.mrhow.util.DialogsUtils;
import com.volive.mrhow.util.MainUrl;
import com.volive.mrhow.util.NetworkConnection;
import com.volive.mrhow.util.PreferenceUtils;
import com.volive.mrhow.util.RetrofitClass;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class MyCourseFragment extends Fragment {

    private static final int SELECT_VIDEO = 3;
    final int CAMERA_CAPTURE = 1;
    final int PICK_IMAGE = 2;
    ArrayList<WishlistCourseModels> whislistcoursemodels;
    ArrayList<EnrolledCourseModels> enrolledCourseModels = new ArrayList<>();
    RecyclerView wishlist_courses_recycler, enrolled_courses_recycler;
    ImageView image_back;
    Button wishlist_courses_btn, enrolled_courses_btn;
    PreferenceUtils preferenceUtils;
    NetworkConnection networkConnection;
    ProgressDialog progressDialog, progressDialog1;
    String userid = "", courseid = "",language="";
    WishlistCourseModels wishlist;
    EnrolledCourseModels enrolled;

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        Log.d("MIME_TYPE_EXT", extension);
        if (extension != null && extension != "") {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            //  Log.d("MIME_TYPE", type);
        } else {
            FileNameMap fileNameMap = URLConnection.getFileNameMap();
            type = fileNameMap.getContentTypeFor(url);
        }
        return type;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_course, container, false);


        preferenceUtils = new PreferenceUtils(getActivity());
        networkConnection = new NetworkConnection(getActivity());

        userid = preferenceUtils.getStringFromPreference(PreferenceUtils.userid, "");
        language = preferenceUtils.getStringFromPreference(PreferenceUtils.Language,"");

        initializeUI(view);
        initializeValues();

//        if (networkConnection.isConnectingToInternet()) {
//            myCourses();
//        } else {
//            Toast.makeText(getActivity(), "Connect To InterNet", Toast.LENGTH_SHORT).show();
//        }

        return view;
    }

    private void initializeValues() {
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), HomeNavigation.class));
                getActivity().finish();
            }
        });

        wishlist_courses_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wishlist_courses_btn.setTextColor(Color.parseColor("#0DCD78"));
                enrolled_courses_btn.setTextColor(Color.parseColor("#FFFFFF"));
                enrolled_courses_recycler.setVisibility(View.GONE);
                wishlist_courses_recycler.setVisibility(View.VISIBLE);

                if (networkConnection.isConnectingToInternet()) {
                    myCourses();
                } else {
                    Toast.makeText(getActivity(), getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
                }
            }
        });

        enrolled_courses_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wishlist_courses_btn.setTextColor(Color.parseColor("#FFFFFF"));
                enrolled_courses_btn.setTextColor(Color.parseColor("#0DCD78"));
                enrolled_courses_recycler.setVisibility(View.VISIBLE);
                wishlist_courses_recycler.setVisibility(View.GONE);

                if (networkConnection.isConnectingToInternet()) {
                    myCourses();
                    if (enrolledCourseModels.size() == 0) {
                        Toast.makeText(getActivity(), getResources().getText(R.string.yourenrolledcoursesempty), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getActivity(), getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    private void initializeUI(View view) {
        image_back = view.findViewById(R.id.image_back);
        wishlist_courses_btn = view.findViewById(R.id.wishlist_courses_btn);
        enrolled_courses_btn = view.findViewById(R.id.enrolled_courses_btn);
        enrolled_courses_recycler = view.findViewById(R.id.enrolled_courses_recycler);
        wishlist_courses_recycler = view.findViewById(R.id.wishlist_courses_recycler);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        enrolled_courses_recycler.setLayoutManager(mLayoutManager);
        enrolled_courses_recycler.setItemAnimator(new DefaultItemAnimator());


        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity());
        wishlist_courses_recycler.setLayoutManager(mLayoutManager1);
        wishlist_courses_recycler.setItemAnimator(new DefaultItemAnimator());

    }

    public void myCourses() {
        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement> call = apiClass.mycourses(userid, language, MainUrl.apikey);
        progressDialog = DialogsUtils.showProgressDialog(getActivity(), String.valueOf(getResources().getText(R.string.loading)));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDialog.dismiss();

                Log.e("asdfsdf ", response.body().toString());

                if (response.isSuccessful()) {
                    try {

                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status.equalsIgnoreCase("1")) {

                            JSONObject dataobject = jsonObject.getJSONObject("data");

                            JSONArray wishlistArray = dataobject.getJSONArray("wishlist");

                            whislistcoursemodels = new ArrayList<>();

                            if (wishlistArray.length() > 0) {
                                for (int i = 0; i < wishlistArray.length(); i++) {

                                    JSONObject wishlistobject = wishlistArray.getJSONObject(i);

                                    String course_id = wishlistobject.getString("course_id");
                                    String category_name = wishlistobject.getString("category_name");
                                    String course_title = wishlistobject.getString("course_title");
                                    String price = wishlistobject.getString("price");
                                    String offer_price = wishlistobject.getString("offer_price");
                                    String cover = wishlistobject.getString("cover");
                                    String cover_type = wishlistobject.getString("cover_type");
                                    String total_ratings = wishlistobject.getString("total_ratings");
                                    String purchased = wishlistobject.getString("purchased");
                                    String tags = wishlistobject.getString("tags");
                                    String duration = wishlistobject.getString("duration");
                                    String thumbnail = wishlistobject.getString("thumbnail");
                                    String currency = wishlistobject.getString("currency");

                                    wishlist = new WishlistCourseModels(course_id, category_name, course_title, price, offer_price,
                                            cover, cover_type, total_ratings, purchased, tags, duration,thumbnail,currency);
                                    whislistcoursemodels.add(wishlist);


                                }

                                setUpRecyclerview();

                            } else {
                                Toast.makeText(getActivity(), getResources().getText(R.string.yourfavoritesempty), Toast.LENGTH_SHORT).show();
                            }

                            enrolledCourseModels = new ArrayList<>();

                            JSONArray enrolledcoursesArray = dataobject.getJSONArray("enrolled_courses");

                            enrolledCourseModels = new ArrayList<>();

                            if (enrolledcoursesArray.length() > 0) {
                                for (int j = 0; j < enrolledcoursesArray.length(); j++) {

                                    JSONObject enrolledcoursesobject = enrolledcoursesArray.getJSONObject(j);

                                    String course_id = enrolledcoursesobject.getString("course_id");
                                    String category_name = enrolledcoursesobject.getString("category_name");
                                    String course_title = enrolledcoursesobject.getString("course_title");
                                    String price = enrolledcoursesobject.getString("price");
                                    String offer_price = enrolledcoursesobject.getString("offer_price");
                                    String cover = enrolledcoursesobject.getString("cover");
                                    String cover_type = enrolledcoursesobject.getString("cover_type");
                                    String total_ratings = enrolledcoursesobject.getString("total_ratings");
                                    String purchased = enrolledcoursesobject.getString("purchased");
                                    String tags = enrolledcoursesobject.getString("tags");
                                    String duration = enrolledcoursesobject.getString("duration");
                                    String downloadable = enrolledcoursesobject.getString("downloadable");
                                    String course_completion = enrolledcoursesobject.getString("course_completion");
                                    String name = enrolledcoursesobject.getString("name");
                                    String thumbnail = enrolledcoursesobject.getString("thumbnail");
                                    String currency = enrolledcoursesobject.getString("currency");
                                    String download_status = enrolledcoursesobject.getString("download_status");

                                    enrolled = new EnrolledCourseModels(course_id, category_name, course_title, price,
                                            offer_price, cover, cover_type, total_ratings, purchased, tags, duration, downloadable,
                                            course_completion, name,thumbnail,currency,download_status);
                                    enrolledCourseModels.add(enrolled);

                                }

                                SwipeRecyclerAdapter swipeRecyclerAdapter = new SwipeRecyclerAdapter(getActivity(), enrolledCourseModels,MyCourseFragment.this);
                                enrolled_courses_recycler.setAdapter(swipeRecyclerAdapter);
                                swipeRecyclerAdapter.notifyDataSetChanged();
                                swipeRecyclerAdapter.setOnItemClick(new SwipeRecyclerAdapter.OnItemClick() {
                                    @Override
                                    public void getPosition(String data) {
                                        courseid = data;
                                        Intent intent = new Intent();
                                        intent.setType("video/*");
                                        intent.setAction(Intent.ACTION_GET_CONTENT);
                                        startActivityForResult(Intent.createChooser(intent, "Select a Video "), SELECT_VIDEO);
                                    }
                                });


                            }


                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Log.e("sfsdf ", e.toString());
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

    private void setUpRecyclerview() {
        WishlistCourseAdapter wishlistCourseAdapter = new WishlistCourseAdapter(getActivity(), whislistcoursemodels);
        wishlist_courses_recycler.setAdapter(wishlistCourseAdapter);
        wishlistCourseAdapter.notifyDataSetChanged();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            if (requestCode == SELECT_VIDEO) {
                System.out.println("SELECT_VIDEO");
                Uri selectedVideoUri = data.getData();
                String selectedPath = getPath(selectedVideoUri);
                System.out.println("SELECT_VIDEO Path : " + selectedPath);

                uploadVideo(selectedPath);
            }
        }
    }

    private String getPath(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA, MediaStore.Video.Media.SIZE, MediaStore.Video.Media.DURATION};
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        cursor.moveToFirst();
        String filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
        int fileSize = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
        long duration = TimeUnit.MILLISECONDS.toSeconds(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)));


        //some extra potentially useful data to help with filtering if necessary
        System.out.println("size: " + fileSize);
        System.out.println("path: " + filePath);
        System.out.println("duration: " + duration);

        return filePath;
    }

    private void uploadVideo(String selectedPath) {
        File file = null;
        MultipartBody.Part image_profile = null;
        if (selectedPath != null && !selectedPath.isEmpty()) {
            file = new File(selectedPath);
            RequestBody requestBody = RequestBody.create(MediaType.parse(getMimeType(selectedPath)), file);
            image_profile = MultipartBody.Part.createFormData("project_file", file.getName(), requestBody);
            Log.d("videopathhh", ">>>>>>>>>>" + image_profile);
        }

        RequestBody r_api_key = RequestBody.create(MediaType.parse("multipart/form-data"), MainUrl.apikey);
        RequestBody r_lang = RequestBody.create(MediaType.parse("multipart/form-data"), "en");
        RequestBody r_userID = RequestBody.create(MediaType.parse("multipart/form-data"), preferenceUtils.getStringFromPreference(PreferenceUtils.userid, ""));
        RequestBody r_courseid = RequestBody.create(MediaType.parse("multipart/form-data"), courseid);


        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement> call = apiClass.uploadprojects(r_userID, r_courseid, r_api_key, r_lang, image_profile);
        progressDialog1 = DialogsUtils.showProgressDialog(getActivity(), String.valueOf(getResources().getText(R.string.loading)));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDialog1.dismiss();
                if (response.isSuccessful()) {
                    try {

                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        if (status.equalsIgnoreCase("1")) {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog1.dismiss();
                        Log.e("dskfds ", e.toString());
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog1.dismiss();
                Log.e("sdfsdf ", t.toString());

            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        if (networkConnection.isConnectingToInternet()) {
            myCourses();
        } else {
            Toast.makeText(getActivity(), getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
        }
    }
}