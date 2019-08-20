package com.volive.mrhow.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonElement;
import com.volive.mrhow.R;
import com.volive.mrhow.util.ApiClass;
import com.volive.mrhow.util.DialogsUtils;
import com.volive.mrhow.util.MainUrl;
import com.volive.mrhow.util.NetworkConnection;
import com.volive.mrhow.util.PreferenceUtils;
import com.volive.mrhow.util.RetrofitClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final int RC_SIGN_IN = 001;
    private static final String TAG = LogInActivity.class.getSimpleName();
    TextView text_signup, text_forgotpassword;
    Button btn_signin;
    ImageView image_back, image_facebook, image_google;
    EditText edit_email, edit_password;
    CheckBox checkbox_rememberme;
    PreferenceUtils preferenceUtils;
    NetworkConnection networkConnection;
    ProgressDialog progressDialog;
    String strParamtoken = "",facebookid="",facebookemail="",facebookname="",language="";
    CallbackManager callbackManager;
    AccessToken accessToken;
     GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        preferenceUtils = new PreferenceUtils(LogInActivity.this);
        networkConnection = new NetworkConnection(LogInActivity.this);

        strParamtoken = FirebaseInstanceId.getInstance().getToken();
        language = preferenceUtils.getStringFromPreference(PreferenceUtils.Language,"");


        FacebookSdk.sdkInitialize(LogInActivity.this);

        facebookKeyHash();
        accessToken = AccessToken.getCurrentAccessToken();
        callbackManager = CallbackManager.Factory.create();

        initializeUI();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        initializeValues();

        Boolean checklogin = preferenceUtils.getbooleanFromPreference(PreferenceUtils.LOGIN, false);
        if (checklogin == true) {
            edit_email.setText(preferenceUtils.getStringFromPreference(PreferenceUtils.enteremail, ""));
            edit_password.setText(preferenceUtils.getStringFromPreference(PreferenceUtils.enterpassword, ""));
            checkbox_rememberme.setChecked(true);
        } else {
            checkbox_rememberme.setChecked(false);
        }

    }


    private void facebookKeyHash() {
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.volive.mrhow", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }


    private void initializeUI() {
        image_back = findViewById(R.id.image_back);
        edit_email = findViewById(R.id.edit_email);
        edit_password = findViewById(R.id.edit_password);
        checkbox_rememberme = findViewById(R.id.checkbox_rememberme);
        text_forgotpassword = findViewById(R.id.text_forgotpassword);
        image_facebook = findViewById(R.id.image_facebook);
        image_google = findViewById(R.id.image_google);
        text_signup = findViewById(R.id.text_signup);
        btn_signin = findViewById(R.id.btn_signin);

    }

    private void initializeValues() {

        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                Intent intent= new Intent(LogInActivity.this,VideoScreenActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                finish();
            }
        });

        image_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fblogin();
            }
        });

        text_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogInActivity.this, SignUp.class));
            }
        });

        text_forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        image_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edit_email.getText().toString();
                String password = edit_password.getText().toString();


                if (networkConnection.isConnectingToInternet()) {

                    if (!email.isEmpty() && !password.isEmpty()) {

                        login(email, password);

                    } else {
                        Toast.makeText(LogInActivity.this, getResources().getText(R.string.pleaseenterfields), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(LogInActivity.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void Fblogin() {
        LoginManager.getInstance().logInWithReadPermissions(LogInActivity.this, Arrays.asList("email","public_profile"));

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.e("JSON", "" + response.getJSONObject().toString());

                                try {
                                     facebookemail = object.getString("email");
                                     facebookname = object.getString("name");
                                     facebookid = object.getString("id");

                                     LoginWithFacebook();
                                    LoginManager.getInstance().logOut();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email");
                        graphRequest.setParameters(parameters);
                        graphRequest.executeAsync();

                    }

                    @Override
                    public void onCancel() {
                        Log.e("sfsdfg", "On cancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.e("kghfg", error.toString());
                    }
                });
    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {

            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName());

            String personName = acct.getDisplayName();
//            String personPhotoUrl = acct.getPhotoUrl().toString();
            String email = acct.getEmail();
            String userid = acct.getId();

            Log.e(TAG, "Name: " + personName + ", email: " + email + ", Image: " + userid);

//            txtName.setText(personName);
//            txtEmail.setText(email);
//            Glide.with(getApplicationContext()).load(personPhotoUrl)
//                    .thumbnail(0.5f)
//                    .crossFade()
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(imgProfilePic);

            socialLogin(personName, email, userid);
            try {
                Intent signInIntent=Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                mGoogleApiClient.clearDefaultAccountAndReconnect();
            }catch (Exception e){
                e.printStackTrace();
            }

        } else {
            Toast.makeText(LogInActivity.this, "failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent= new Intent(LogInActivity.this,VideoScreenActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
        finish();
    }

    private void login(String email, final String password) {
        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement> call = apiClass.login(email, password, "android", strParamtoken,
                language, MainUrl.apikey);

        progressDialog = DialogsUtils.showProgressDialog(LogInActivity.this, String.valueOf(getResources().getText(R.string.loading)));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        String base_url = jsonObject.getString("base_url");

                        if (status.equalsIgnoreCase("1")) {
                            Toast.makeText(LogInActivity.this, message, Toast.LENGTH_SHORT).show();
                            JSONObject dataobject = jsonObject.getJSONObject("data");
                            String user_id = dataobject.getString("user_id");
                            String name = dataobject.getString("name");
                            String email = dataobject.getString("email");
                            String profile_pic = dataobject.getString("profile_pic");

                            preferenceUtils.saveString(PreferenceUtils.userid, user_id);
                            preferenceUtils.saveString(PreferenceUtils.userimage, profile_pic);
                            preferenceUtils.saveString(PreferenceUtils.username, name);
                            preferenceUtils.saveBoolean(PreferenceUtils.LOGIN, true);

                            if (checkbox_rememberme.isChecked()) {
                                preferenceUtils.saveString(PreferenceUtils.enteremail, email);
                                preferenceUtils.saveString(PreferenceUtils.enterpassword, password);
                            } else {
                                preferenceUtils.saveString(PreferenceUtils.enteremail, "");
                                preferenceUtils.saveString(PreferenceUtils.enterpassword, "");
                            }

                            Intent intent = new Intent(LogInActivity.this, HomeNavigation.class);
                            startActivity(intent);


                        } else {
                            Toast.makeText(LogInActivity.this, message, Toast.LENGTH_SHORT).show();
                        }

                        progressDialog.dismiss();

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Log.e("fdsfds ", e.toString());

                    }

                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("fdfdf ", t.toString());

            }
        });


    }

    private void socialLogin(String personName, String email, String userid) {
        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement> call = apiClass.sociallogin(personName, email, "android", strParamtoken,
                "google", userid, language, MainUrl.apikey);

        progressDialog = DialogsUtils.showProgressDialog(LogInActivity.this, String.valueOf(getResources().getText(R.string.loading)));

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Log.e("gdfg ", response.body().toString());
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    try {

                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        String base_url = jsonObject.getString("base_url");

                        if (status.equalsIgnoreCase("1")) {
                            Toast.makeText(LogInActivity.this, message, Toast.LENGTH_LONG).show();
                            JSONObject dataobject = jsonObject.getJSONObject("data");
                            String user_id = dataobject.getString("user_id");
                            String name = dataobject.getString("name");
                            String email = dataobject.getString("email");
                            String profile_pic = dataobject.getString("profile_pic");

                            preferenceUtils.saveString(PreferenceUtils.userid, user_id);
                            preferenceUtils.saveString(PreferenceUtils.userimage, profile_pic);
                            preferenceUtils.saveString(PreferenceUtils.username, name);
                            preferenceUtils.saveBoolean(PreferenceUtils.LOGIN, true);


                            Intent intent = new Intent(LogInActivity.this, HomeNavigation.class);
                            startActivity(intent);


                        } else {
                            Toast.makeText(LogInActivity.this, message, Toast.LENGTH_SHORT).show();
                        }

                        progressDialog.dismiss();


                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Log.e("dfdshf ", e.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("dskfshdf ", t.toString());

            }
        });


    }

    private void LoginWithFacebook() {

        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement>call = apiClass.sociallogin(facebookname,facebookemail,"android",strParamtoken,
                "facebook",facebookid,language,MainUrl.apikey);
        progressDialog= DialogsUtils.showProgressDialog(LogInActivity.this, String.valueOf(getResources().getText(R.string.loading)));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    try {

                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        String base_url = jsonObject.getString("base_url");

                        if (status.equalsIgnoreCase("1")) {
                            Toast.makeText(LogInActivity.this, message, Toast.LENGTH_LONG).show();
                            JSONObject dataobject = jsonObject.getJSONObject("data");
                            String user_id = dataobject.getString("user_id");
                            String name = dataobject.getString("name");
                            String email = dataobject.getString("email");
                            String profile_pic = dataobject.getString("profile_pic");

                            preferenceUtils.saveString(PreferenceUtils.userid, user_id);
                            preferenceUtils.saveString(PreferenceUtils.userimage, profile_pic);
                            preferenceUtils.saveString(PreferenceUtils.username, name);
                            preferenceUtils.saveBoolean(PreferenceUtils.LOGIN, true);


                            Intent intent = new Intent(LogInActivity.this, HomeNavigation.class);
                            startActivity(intent);


                        } else {
                            Toast.makeText(LogInActivity.this, message, Toast.LENGTH_SHORT).show();
                        }

                        progressDialog.dismiss();

                    }catch (Exception e){
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Log.e("sdfsdf ",e.toString());
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("sdfdsjg ",t.toString());

            }
        });

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }
}
