package com.volive.mrhow.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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

public class PurchageDetailsActivity extends AppCompatActivity {

    public static String totalprice = "", courseid = "";
    RecyclerView items_recyclerview;
    ImageView image_back;
    Button button_proceed, button_applycode;
    EditText edit_discountcode;
    TextView text_subtotal, text_discount, text_total, text_title, text_price;
    String coursetitle = "", strtotalprice = "", language = "";
    PreferenceUtils preferenceUtils;
    NetworkConnection networkConnection;
    ProgressDialog progressDialog;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchage_details);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//only working in api level 26
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.lightgreen));
        }

        preferenceUtils = new PreferenceUtils(PurchageDetailsActivity.this);
        networkConnection = new NetworkConnection(PurchageDetailsActivity.this);

        language = preferenceUtils.getStringFromPreference(PreferenceUtils.Language, "");

        totalprice = getIntent().getStringExtra("totalprice");
        coursetitle = getIntent().getStringExtra("coursetitle");
        courseid = getIntent().getStringExtra("courseid");

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

        button_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PurchageDetailsActivity.this, PaymentActivity.class);
                intent.putExtra("strtotalprice", totalprice);
                startActivity(intent);
            }
        });

        button_applycode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (networkConnection.isConnectingToInternet()) {

                    String strpromocode = edit_discountcode.getText().toString();
                    if (!strpromocode.isEmpty()) {

                        checkValidPromoCode(strpromocode);

                    } else {
                        Toast.makeText(PurchageDetailsActivity.this, getResources().getText(R.string.pleaseenterpromocode), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(PurchageDetailsActivity.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    private void initializeUI() {
        image_back = findViewById(R.id.image_back);
        edit_discountcode = findViewById(R.id.edit_discountcode);
        button_applycode = findViewById(R.id.button_applycode);
        text_subtotal = findViewById(R.id.text_subtotal);
        text_discount = findViewById(R.id.text_discount);
        text_total = findViewById(R.id.text_total);
        button_proceed = findViewById(R.id.button_proceed);
        text_title = findViewById(R.id.text_title);
        text_price = findViewById(R.id.text_price);
        items_recyclerview = findViewById(R.id.items_recyclerview);
        items_recyclerview.setHasFixedSize(true);
        items_recyclerview.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));

        text_subtotal.setText(totalprice + "  SAR");
        text_title.setText(coursetitle);
        text_price.setText(totalprice + "  SAR");
        text_total.setText(totalprice + "  SAR");
        text_discount.setText("0" + " SAR");

    }

    private void checkValidPromoCode(String strpromocode) {
        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement> call = apiClass.validpromocode(strpromocode, MainUrl.apikey, language);
        progressDialog = DialogsUtils.showProgressDialog(PurchageDetailsActivity.this, String.valueOf(getResources().getText(R.string.loading)));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    try {

                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("1")) {

                            JSONObject dataobject = jsonObject.getJSONObject("data");
                            String promocode_id = dataobject.getString("promocode_id");
                            String name = dataobject.getString("name");
                            String promocode = dataobject.getString("promocode");
                            String start_date = dataobject.getString("start_date");
                            String end_date = dataobject.getString("end_date");
                            String discount_type = dataobject.getString("discount_type");
                            String discount = dataobject.getString("discount");
                            String limit_users = dataobject.getString("limit_users");

                            int s1 = Integer.parseInt(totalprice);
                            int s2 = Integer.parseInt(discount);

                            int k = (int) (s1 / 100) * s2;

                            text_discount.setText(String.valueOf(k));

//                            strtotalprice = String.valueOf(s1-k);
                            totalprice = String.valueOf(s1 - k);


                            text_total.setText(totalprice + "  SAR");


                        } else {
                            String message = jsonObject.getString("message");
                            Toast.makeText(PurchageDetailsActivity.this, message, Toast.LENGTH_SHORT).show();

                            String discount = "0";
                            int s1 = Integer.parseInt(totalprice);
                            int s2 = Integer.parseInt(discount);

                            int k = (int) (s1 / 100) * s2;

                            text_discount.setText(String.valueOf(k) + " SAR");

                            totalprice = String.valueOf(s1 - k);

                            text_total.setText(totalprice + "  SAR");
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Log.e("dsjfdf ", e.toString());
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("sdfsdf ", t.toString());

            }
        });

    }

}
