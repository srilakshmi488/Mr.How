package com.volive.mrhow.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
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

public class SignUp extends AppCompatActivity {

    TextView text_termsandconditions, text_signin;
    ImageView image_back, image_facebook, image_google;
    Button btn_signup;
    EditText edit_fullname, edit_email, edit_password, edit_conformpassword;
    CheckBox checkbox_terms;
    PreferenceUtils preferenceUtils;
    NetworkConnection networkConnection;
    String ischecked = "",language="",strParamtoken = "";
    ProgressDialog progressDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        preferenceUtils = new PreferenceUtils(SignUp.this);
        networkConnection = new NetworkConnection(SignUp.this);
        language = preferenceUtils.getStringFromPreference(PreferenceUtils.Language,"");

        strParamtoken = FirebaseInstanceId.getInstance().getToken();

        initializeUI();
        initializeValues();


    }

    private void initializeValues() {
        text_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent = new Intent(SignUp.this,LogInActivity.class);
              intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
              startActivity(intent);
              finish();
            }
        });
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              finish();
            }
        });

        checkbox_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkbox_terms.isChecked() == true) {

                    ischecked = "1";

                } else {
                    ischecked = "0";
                }
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edit_fullname.getText().toString();
                String email = edit_email.getText().toString();
                String password = edit_password.getText().toString();
                String conformpassword = edit_conformpassword.getText().toString();

                if (networkConnection.isConnectingToInternet()) {

                    if (!name.isEmpty() && !email.isEmpty()) {

                        if (password.equalsIgnoreCase(conformpassword)) {

                            if (ischecked.equalsIgnoreCase("1")) {

                                signUp(name, email, password);

                            } else {
                                Toast.makeText(SignUp.this, getResources().getText(R.string.pleaseagreeterms), Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            Toast.makeText(SignUp.this, getResources().getText(R.string.passwordandconformpassword), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(SignUp.this, getResources().getText(R.string.pleaseenterfields), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignUp.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
                }


            }
        });
        text_termsandconditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent = new Intent(SignUp.this,TermsConditionsActivity.class);
              startActivity(intent);


            }
        });

    }

    private void signUp(String name, String email, String password) {
        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement> call = apiClass.registration(name, email, password, "android",
                strParamtoken, ischecked,language, MainUrl.apikey);

        progressDialog = DialogsUtils.showProgressDialog(SignUp.this, String.valueOf(getResources().getText(R.string.loading)));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    try {

                        Log.e("fgfdgjkdf",response.body().toString());
                        JSONObject jsonObject = new JSONObject(response.body().toString());

                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        String base_url = jsonObject.getString("base_url");

                        if (status.equalsIgnoreCase("1")) {
                            Toast.makeText(SignUp.this, message, Toast.LENGTH_SHORT).show();

                            JSONObject dataobject = jsonObject.getJSONObject("data");
                            String user_id = dataobject.getString("user_id");
                            String name = dataobject.getString("name");
                            String email = dataobject.getString("email");
                            String profile_pic = dataobject.getString("profile_pic");

                            preferenceUtils.saveString(PreferenceUtils.userid,user_id);
                            preferenceUtils.saveString(PreferenceUtils.userimage,profile_pic);
                            preferenceUtils.saveString(PreferenceUtils.username,name);
                            preferenceUtils.saveBoolean(PreferenceUtils.LOGIN, true);

                            Intent intent = new Intent(SignUp.this, HomeNavigation.class);
                            startActivity(intent);

                        } else {
                            Toast.makeText(SignUp.this, message, Toast.LENGTH_SHORT).show();
                        }

                        progressDialog.dismiss();

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Log.e("dgfgd", e.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("dskfdsf ", t.toString());

            }
        });

    }

    private void initializeUI() {
        edit_fullname = findViewById(R.id.edit_fullname);
        edit_email = findViewById(R.id.edit_email);
        edit_password = findViewById(R.id.edit_password);
        edit_conformpassword = findViewById(R.id.edit_conformpassword);
        checkbox_terms = findViewById(R.id.checkbox_terms);
        image_facebook = findViewById(R.id.image_facebook);
        image_google = findViewById(R.id.image_google);
        text_signin = findViewById(R.id.text_signin);
        image_back = findViewById(R.id.image_back);
        btn_signup = findViewById(R.id.btn_signup);
        text_termsandconditions = findViewById(R.id.text_termsandconditions);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }
}
