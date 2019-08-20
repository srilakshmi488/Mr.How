package com.volive.mrhow.activities;

import android.content.Intent;
import android.graphics.Typeface;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;
import com.volive.mrhow.R;
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

public class FiltersActivity extends AppCompatActivity {
    ImageView image_cancel, image_language, image_catagiry, image_subcatagiry;
    TextView text_reset, text_minprice, text_maxprice;
    AutoCompleteTextView actv_language, actv_categiry, actv_subacatgiry;
    RadioButton radio_five, radio_four, radio_three, radio_two, radio_one, radio_populartity, radio_lowtohigh, radio_hightolow;
    Button btn_applyfilter;
    PreferenceUtils preferenceUtils;
    NetworkConnection networkConnection;
    ArrayList<String> catnames = new ArrayList<>();
    ArrayList<String> catids = new ArrayList<>();
    ArrayList<String> subcatnames = new ArrayList<>();
    ArrayList<String> subcatids = new ArrayList<>();
    String strcatid = "", strcatname = "", strsubcatid = "", strsubcatname = "";
    String strrating = "", strsortby = "", strlanguage = "", min = "", max = "",language="";
    RangeSeekBar price_seekbar;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//only working in api level 26
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.lightgreen));
        }

        preferenceUtils = new PreferenceUtils(FiltersActivity.this);
        networkConnection = new NetworkConnection(FiltersActivity.this);
        language = preferenceUtils.getStringFromPreference(PreferenceUtils.Language,"");


        initializeUI();
        initializeValues();

        if (networkConnection.isConnectingToInternet()) {
            catagiries();
            pricerange();

        } else {
            Toast.makeText(FiltersActivity.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
        }

    }


    private void initializeUI() {
        image_cancel = findViewById(R.id.image_cancel);
        btn_applyfilter = findViewById(R.id.btn_applyfilter);
//        text_maxprice = findViewById(R.id.text_maxprice);
//        text_minprice = findViewById(R.id.text_minprice);
        text_reset = findViewById(R.id.text_reset);
        image_language = findViewById(R.id.image_language);
        image_catagiry = findViewById(R.id.image_catagiry);
        image_subcatagiry = findViewById(R.id.image_subcatagiry);
        actv_language = findViewById(R.id.actv_language);
        actv_categiry = findViewById(R.id.actv_categiry);
        actv_subacatgiry = findViewById(R.id.actv_subacatgiry);
        radio_five = findViewById(R.id.radio_five);
        radio_four = findViewById(R.id.radio_four);
        radio_three = findViewById(R.id.radio_three);
        radio_two = findViewById(R.id.radio_two);
        radio_one = findViewById(R.id.radio_one);
        radio_populartity = findViewById(R.id.radio_populartity);
        radio_hightolow = findViewById(R.id.radio_hightolow);
        radio_lowtohigh = findViewById(R.id.radio_lowtohigh);

        price_seekbar = findViewById(R.id.price_seekbar);
        price_seekbar.setTypeface(Typeface.DEFAULT_BOLD);
        price_seekbar.getLeftSeekBar().setTypeface(Typeface.DEFAULT_BOLD);
//        price_seekbar.setIndicatorTextDecimalFormat("0 SAR");


    }


    private void initializeValues() {

        image_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //catagiry
        actv_categiry.setCursorVisible(false);
        actv_categiry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                actv_categiry.showDropDown();
                strcatid = catids.get(position);
                strcatname = catnames.get(position);

                if (!strcatid.isEmpty()) {
                    actv_subacatgiry.setText("");
                    subacatagiries();
                }


            }
        });

        actv_categiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                actv_categiry.showDropDown();

            }
        });


//subcatagiry
        actv_subacatgiry.setCursorVisible(false);
        actv_subacatgiry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                actv_subacatgiry.showDropDown();
                strsubcatid = catids.get(position);
                strsubcatname = catnames.get(position);


            }
        });

        actv_subacatgiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                if (!strcatid.isEmpty()) {
                    actv_subacatgiry.showDropDown();
                } else {
                    Toast.makeText(FiltersActivity.this, getResources().getText(R.string.pleaseselectcatagiry), Toast.LENGTH_SHORT).show();
                }

            }
        });


        //language
        ArrayAdapter<String> languageAdapter = new ArrayAdapter<String>(FiltersActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.languageArray));
        languageAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        actv_language.setAdapter(languageAdapter);
        actv_language.setCursorVisible(false);
        actv_language.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                actv_language.showDropDown();
                String language = String.valueOf(position);
                if (language.equalsIgnoreCase("0")) {
                    strlanguage = "en";

                } else {
                    strlanguage = "ar";
                }
            }
        });

        actv_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                actv_language.showDropDown();

            }
        });


        text_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                finish();
                Intent intent = new Intent(FiltersActivity.this,FiltersActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


        price_seekbar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                max = String.valueOf(leftValue);
                min = String.valueOf(rightValue);


            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }
        });

        radio_five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strrating = "5";
                radio_four.setChecked(false);
                radio_three.setChecked(false);
                radio_two.setChecked(false);
                radio_one.setChecked(false);

            }
        });

        radio_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strrating = "4";
                radio_five.setChecked(false);
                radio_three.setChecked(false);
                radio_two.setChecked(false);
                radio_one.setChecked(false);
            }
        });

        radio_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strrating = "3";
                radio_four.setChecked(false);
                radio_five.setChecked(false);
                radio_two.setChecked(false);
                radio_one.setChecked(false);
            }
        });

        radio_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strrating = "2";
                radio_four.setChecked(false);
                radio_three.setChecked(false);
                radio_five.setChecked(false);
                radio_one.setChecked(false);
            }
        });

        radio_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strrating = "1";
                radio_four.setChecked(false);
                radio_three.setChecked(false);
                radio_two.setChecked(false);
                radio_five.setChecked(false);
            }
        });

        radio_populartity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strsortby = "1";
                radio_hightolow.setChecked(false);
                radio_lowtohigh.setChecked(false);
            }
        });

        radio_lowtohigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strsortby = "2";
                radio_hightolow.setChecked(false);
                radio_populartity.setChecked(false);
            }
        });

        radio_hightolow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strsortby = "3";
                radio_populartity.setChecked(false);
                radio_lowtohigh.setChecked(false);
            }
        });


        btn_applyfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FiltersActivity.this, CategiriesActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("strcatid", strcatid);
                intent.putExtra("strsubcatid", strsubcatid);
                intent.putExtra("min", min);
                intent.putExtra("max", max);
                intent.putExtra("strrating", strrating);
                intent.putExtra("strsortby", strsortby);
                intent.putExtra("Action", "filters");
                startActivity(intent);


            }
        });


    }

    private void pricerange() {

        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement> call = apiClass.pricerange(MainUrl.apikey, language);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Log.e("sgkfsg ", response.body().toString());
                if (response.isSuccessful()) {
                    try {

                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        if (status.equalsIgnoreCase("1")) {

                            JSONObject dataobject = jsonObject.getJSONObject("data");
                            String max = dataobject.getString("max");
                            String min = dataobject.getString("min");

                            price_seekbar.setRange(Float.parseFloat(min), Float.parseFloat(max));


                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("dsfksdf ", e.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e("dslfsdf ", t.toString());

            }
        });

    }

    private void subacatagiries() {
        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement> call = apiClass.subcatagirieslist(strcatid, MainUrl.apikey, language);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    try {

                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        subcatnames = new ArrayList<>();
                        subcatids = new ArrayList<>();

                        if (status.equalsIgnoreCase("1")) {

                            JSONArray dataArray = jsonObject.getJSONArray("data");
                            if (dataArray.length() > 0) {
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataobject = dataArray.getJSONObject(i);
                                    String sub_category_id = dataobject.getString("sub_category_id");
                                    String sub_category_name_en = dataobject.getString("sub_category_name_en");

                                    subcatnames.add(sub_category_name_en);
                                    subcatids.add(sub_category_id);

                                }
                            }


                            if (subcatnames != null && !subcatnames.isEmpty()) {
                                ArrayAdapter<String> subcatagiriesAdapter = new ArrayAdapter<String>(FiltersActivity.this, android.R.layout.simple_list_item_1, subcatnames);
                                subcatagiriesAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                                actv_subacatgiry.setAdapter(subcatagiriesAdapter);
                            } else {
                                ArrayAdapter<String> subcatagiriesAdapter = new ArrayAdapter<String>(FiltersActivity.this, android.R.layout.simple_list_item_1, subcatnames);
                                subcatagiriesAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                                actv_subacatgiry.setAdapter(subcatagiriesAdapter);
                                actv_subacatgiry.setText("");
                            }


                        } else {

                            Toast.makeText(FiltersActivity.this, message, Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("asfdjsf ", e.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e("fksdf ", t.toString());

            }
        });


    }


    private void catagiries() {
        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement> call = apiClass.categorieslist(MainUrl.apikey, language);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    try {

                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        String base_url = jsonObject.getString("base_url");

                        catnames = new ArrayList<>();
                        catids = new ArrayList<>();

                        if (status.equalsIgnoreCase("1")) {

                            JSONArray dataArray = jsonObject.getJSONArray("data");

                            if (dataArray.length() > 0) {
                                for (int j = 0; j < dataArray.length(); j++) {

                                    JSONObject dataobject = dataArray.getJSONObject(j);
                                    String category_id = dataobject.getString("category_id");
                                    String category_name = dataobject.getString("category_name");
                                    String icon = dataobject.getString("icon");

                                    catnames.add(category_name);
                                    catids.add(category_id);

                                }
                            }


                            if (catnames != null && !catnames.isEmpty()) {
                                ArrayAdapter<String> catadapter = new ArrayAdapter<String>(FiltersActivity.this, android.R.layout.simple_list_item_1, catnames);
                                catadapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                                actv_categiry.setAdapter(catadapter);
                            } else {
                                ArrayAdapter<String> catadapter = new ArrayAdapter<String>(FiltersActivity.this, android.R.layout.simple_list_item_1, catnames);
                                catadapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                                actv_categiry.setAdapter(catadapter);
                                actv_categiry.setText("");
                            }


                        } else {

                            Toast.makeText(FiltersActivity.this, message, Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("sdkfdsg ", e.toString());
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e("asfdsfd ", t.toString());


            }
        });


    }


}
