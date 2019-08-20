package com.volive.mrhow.activities;

import android.app.ProgressDialog;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.volive.mrhow.R;
import com.volive.mrhow.adapters.NotificationsAdapter;
import com.volive.mrhow.models.NotificationModels;
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

public class NotificationsActivity extends AppCompatActivity {

    RecyclerView notification_recyclerview;
    ArrayList<NotificationModels> notificationModels = new ArrayList<>();
    ImageView image_back;
    TextView text_unreadmessages;
    PreferenceUtils preferenceUtils;
    NetworkConnection networkConnection;
    ProgressDialog progressDialog;
    String userid = "",language="";
    RelativeLayout relative_unread;
    NotificationsAdapter notificationsAdapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//only working in api level 26
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.lightgreen));
        }

        preferenceUtils = new PreferenceUtils(NotificationsActivity.this);
        networkConnection = new NetworkConnection(NotificationsActivity.this);

        userid = preferenceUtils.getStringFromPreference(PreferenceUtils.userid, "");
        language= preferenceUtils.getStringFromPreference(PreferenceUtils.Language,"");


        initializeUI();
        initializeValues();

        if (networkConnection.isConnectingToInternet()) {
            getNotifications();
        } else {
            Toast.makeText(NotificationsActivity.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
        }


    }


    private void initializeUI() {
        relative_unread = findViewById(R.id.relative_unread);
        image_back = findViewById(R.id.image_back);
        text_unreadmessages = findViewById(R.id.text_unreadmessages);
        notification_recyclerview = findViewById(R.id.notification_recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(NotificationsActivity.this);
        notification_recyclerview.setLayoutManager(mLayoutManager);
        notification_recyclerview.setItemAnimator(new DefaultItemAnimator());


    }

    private void initializeValues() {
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void getNotifications() {
        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement> call = apiClass.notifications(userid,language,MainUrl.apikey);
        progressDialog = DialogsUtils.showProgressDialog(NotificationsActivity.this, String.valueOf(getResources().getText(R.string.loading)));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Log.e("sdfsdf ",response.body().toString());
                progressDialog.dismiss();
                if (response.isSuccessful()) {

                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        int status = jsonObject.getInt("status");
                        String message = jsonObject.getString("message");


                        if (status == 1) {

                            String unread = jsonObject.getString("unread");
                            if (unread.length() > 0) {
                                relative_unread.setVisibility(View.VISIBLE);
                                text_unreadmessages.setText(unread+" "+getResources().getText(R.string.messagesunread));
                            } else {
                                relative_unread.setVisibility(View.GONE);
                            }

                            notificationModels = new ArrayList<>();
                            JSONArray dataArray = jsonObject.getJSONArray("data");

                            if (dataArray.length() > 0) {

                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataobject = dataArray.getJSONObject(i);

                                    String title = dataobject.getString("title");
                                    String text = dataobject.getString("text");
                                    String type = dataobject.getString("type");
                                    String seen_status = dataobject.getString("seen_status");
                                    String name = dataobject.getString("name");
                                    String profile_pic = dataobject.getString("profile_pic");
                                    String created_on = dataobject.getString("created_on");
                                    String time = dataobject.getString("time");

                                    NotificationModels notificationmodel = new NotificationModels(title, text, type, seen_status, name, profile_pic,
                                            created_on, time);
                                    notificationModels.add(notificationmodel);


                                }

                                notificationsAdapter = new NotificationsAdapter(NotificationsActivity.this, notificationModels);
                                notification_recyclerview.setAdapter(notificationsAdapter);
                                notificationsAdapter.notifyDataSetChanged();
                            }


                        } else {

                            Toast.makeText(NotificationsActivity.this, message, Toast.LENGTH_LONG).show();
                        }




                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Log.e("sdfdsf ", e.toString());
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("sdfdsf ", t.toString());
            }
        });

    }


}
