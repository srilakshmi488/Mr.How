package com.volive.mrhow.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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

public class PaymentActivity extends AppCompatActivity {
    Button button_paynow;
    TextView text_totalprice;
    ImageView image_back;
    PreferenceUtils preferenceUtils;
    NetworkConnection networkConnection;
    String userid = "",price="",strtotalprice="",language ="";
    ProgressDialog progressDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//only working in api level 26
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.lightgreen));
        }

//        price= PurchageDetailsActivity.totalprice;
        strtotalprice = getIntent().getStringExtra("strtotalprice");

        preferenceUtils = new PreferenceUtils(PaymentActivity.this);
        networkConnection = new NetworkConnection(PaymentActivity.this);
        userid = preferenceUtils.getStringFromPreference(PreferenceUtils.userid, "");
        language=preferenceUtils.getStringFromPreference(PreferenceUtils.Language,"");

        initializeUI();
        initializeValues();


    }

    private void initializeValues() {
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        button_paynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (networkConnection.isConnectingToInternet()) {

                    makepayment();
                } else {
                    Toast.makeText(PaymentActivity.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void makepayment() {
        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement> call = apiClass.makepayment(userid, PurchageDetailsActivity.courseid, strtotalprice,
                strtotalprice, "cash", language, MainUrl.apikey);
        progressDialog = DialogsUtils.showProgressDialog(PaymentActivity.this, String.valueOf(getResources().getText(R.string.loading)));
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
                            Dialog();

                        } else {
                            Toast.makeText(PaymentActivity.this, message, Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Log.e("sfdsg ", e.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("kgdfg ", t.toString());
            }
        });


    }

    private void initializeUI() {
        text_totalprice = findViewById(R.id.text_totalprice);
        image_back = findViewById(R.id.image_back);
        button_paynow = findViewById(R.id.button_paynow);
        text_totalprice.setText(strtotalprice + " SAR");
    }


    private void Dialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PaymentActivity.this);
        LayoutInflater inflater = getWindow().getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.success_dialog, null);
        final Button view_course_btn = add_menu_layout.findViewById(R.id.view_course_btn);
        final ImageView cancel_img = add_menu_layout.findViewById(R.id.cancel_img);
        TextView text_price = add_menu_layout.findViewById(R.id.text_price);
        text_price.setText(getResources().getText(R.string.youhavebeenpurchased)+"  "+ strtotalprice+" SAR");
        alertDialog.setView(add_menu_layout);
        alertDialog.setCancelable(false);
        final AlertDialog dialog = alertDialog.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(null);
        cancel_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        view_course_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomeNavigation.class));
                finish();
            }
        });
    }

}
