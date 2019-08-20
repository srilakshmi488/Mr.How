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

public class ForgotPasswordActivity extends AppCompatActivity {

    PreferenceUtils preferenceUtils;
    NetworkConnection networkConnection;
    ImageView image_back;
    EditText edit_email;
    Button button_submit;
    ProgressDialog progressDialog;
    String language ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//only working in api level 26
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.lightgreen));
        }

        preferenceUtils = new PreferenceUtils(ForgotPasswordActivity.this);
        networkConnection = new NetworkConnection(ForgotPasswordActivity.this);
        language = preferenceUtils.getStringFromPreference(PreferenceUtils.Language,"");

        initializeUI();
        initializeValues();
    }


    private void initializeUI() {
        image_back = findViewById(R.id.image_back);
        edit_email = findViewById(R.id.edit_email);
        button_submit = findViewById(R.id.button_submit);

    }

    private void initializeValues() {
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edit_email.getText().toString();

                if (networkConnection.isConnectingToInternet()) {
                    if (!email.isEmpty()) {
                        forgotpassword(email);
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, getResources().getText(R.string.pleaseenteremail), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void forgotpassword(String email) {
        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement> call = apiClass.forgotpassword(email, language, MainUrl.apikey);

        progressDialog = DialogsUtils.showProgressDialog(ForgotPasswordActivity.this, String.valueOf(getResources().getText(R.string.loading)));
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
                            Toast.makeText(ForgotPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ForgotPasswordActivity.this, LogInActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(ForgotPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Log.e("sdfdskf ", e.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("sgdfg ", t.toString());

            }
        });


    }


}
