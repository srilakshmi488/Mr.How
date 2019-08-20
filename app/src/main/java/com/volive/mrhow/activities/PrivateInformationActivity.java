package com.volive.mrhow.activities;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class PrivateInformationActivity extends AppCompatActivity {
    PreferenceUtils preferenceUtils;
    NetworkConnection networkConnection;
    ProgressDialog progressDialog;
    ImageView image_back;
    TextView text_edit, text_save;
    EditText edit_email, edit_phone;
    AutoCompleteTextView actv_gender;
    String strgender = "", useremail = "", userphone = "", usergender = "", userid = "",language="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_information);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//only working in api level 26
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.lightgreen));
        }

        preferenceUtils = new PreferenceUtils(PrivateInformationActivity.this);
        networkConnection = new NetworkConnection(PrivateInformationActivity.this);

        userid = preferenceUtils.getStringFromPreference(PreferenceUtils.userid, "");
        language=preferenceUtils.getStringFromPreference(PreferenceUtils.Language,"");

        useremail = getIntent().getStringExtra("useremail");
        userphone = getIntent().getStringExtra("userphone");
        usergender = getIntent().getStringExtra("usergender");

        initializeUI();
        initializeValues();


    }


    private void initializeUI() {
        image_back = findViewById(R.id.image_back);
        text_edit = findViewById(R.id.text_edit);
        edit_email = findViewById(R.id.edit_email);
        edit_phone = findViewById(R.id.edit_phone);
        actv_gender = findViewById(R.id.actv_gender);
        text_save = findViewById(R.id.text_save);

        edit_email.setText(useremail);
        edit_phone.setText(userphone);
        if (usergender.equalsIgnoreCase("1")) {
            actv_gender.setText(getResources().getText(R.string.male));
            strgender = "1";
        } else if(usergender.equalsIgnoreCase("2")){
            actv_gender.setText(getResources().getText(R.string.female));
            strgender = "2";
        }

        edit_email.setEnabled(false);
        edit_phone.setEnabled(false);
        actv_gender.setEnabled(false);


    }

    private void initializeValues() {
        //gender
        ArrayAdapter<String> genderadapter = new ArrayAdapter<String>(PrivateInformationActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.genderArray));
        genderadapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        actv_gender.setAdapter(genderadapter);

        actv_gender.setCursorVisible(false);
        actv_gender.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                actv_gender.showDropDown();

                if (position == 0) {
                    strgender = "1";

                } else {
                    strgender = "2";

                }

            }
        });

        actv_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                actv_gender.showDropDown();

            }
        });


        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        text_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_edit.setVisibility(View.VISIBLE);
                text_save.setVisibility(View.GONE);
                edit_email.setEnabled(false);
                edit_phone.setEnabled(false);
                actv_gender.setEnabled(false);

                String email = edit_email.getText().toString();
                String phonenumber = edit_phone.getText().toString();

                if (networkConnection.isConnectingToInternet()) {

                    if (!email.isEmpty()) {
                        editProfile(email, phonenumber);
                    } else {
                        Toast.makeText(PrivateInformationActivity.this, getResources().getText(R.string.pleaseenterallfields), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(PrivateInformationActivity.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
                }
            }
        });


        text_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_edit.setVisibility(View.GONE);
                text_save.setVisibility(View.VISIBLE);
                edit_email.setEnabled(true);
                edit_phone.setEnabled(true);
                actv_gender.setEnabled(true);


            }
        });
    }

    private void editProfile(String email, String phonenumber) {

        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement> call = apiClass.editprofile(userid, email, phonenumber, strgender, language, MainUrl.apikey);
        progressDialog = DialogsUtils.showProgressDialog(PrivateInformationActivity.this, String.valueOf(getResources().getText(R.string.loading)));
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

                            Toast.makeText(PrivateInformationActivity.this, message, Toast.LENGTH_SHORT).show();

                            JSONObject dataobject = jsonObject.getJSONObject("data");
                            String user_id = dataobject.getString("user_id");
                            String name = dataobject.getString("name");
                            String email = dataobject.getString("email");
                            String phone = dataobject.getString("phone");
                            String gender = dataobject.getString("gender");
                            String profile_pic = dataobject.getString("profile_pic");
                            String video_quality = dataobject.getString("video_quality");
                            String wifi_only = dataobject.getString("wifi_only");
                            String background_video = dataobject.getString("background_video");
                            String trainer_updates = dataobject.getString("trainer_updates");
                            String recommendations = dataobject.getString("recommendations");

                            preferenceUtils.saveString(PreferenceUtils.userid, user_id);
                            preferenceUtils.saveString(PreferenceUtils.username, name);
                            preferenceUtils.saveString(PreferenceUtils.userimage, profile_pic);

                            edit_email.setText(email);
                            edit_phone.setText(phone);
                            if (gender.equalsIgnoreCase("1")) {
                                actv_gender.setText(getResources().getText(R.string.male));
                            } else {
                                actv_gender.setText(getResources().getText(R.string.female));
                            }


                        } else {
                            Toast.makeText(PrivateInformationActivity.this, message, Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Log.e("afmdsf ", e.toString());
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
