package com.volive.mrhow.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.JsonElement;
import com.volive.mrhow.BuildConfig;
import com.volive.mrhow.R;
import com.volive.mrhow.activities.AboutUsActivity;
import com.volive.mrhow.activities.FAQActivity;
import com.volive.mrhow.activities.HomeNavigation;
import com.volive.mrhow.activities.LogInActivity;
import com.volive.mrhow.activities.MyDownloadsActivity;
import com.volive.mrhow.activities.NotificationActivity;
import com.volive.mrhow.activities.NotificationsActivity;
import com.volive.mrhow.activities.PrivateInformationActivity;
import com.volive.mrhow.activities.VideoOptionsActivity;
import com.volive.mrhow.util.ApiClass;
import com.volive.mrhow.util.DialogsUtils;
import com.volive.mrhow.util.GalleryUriToPath;
import com.volive.mrhow.util.MainUrl;
import com.volive.mrhow.util.NetworkConnection;
import com.volive.mrhow.util.PreferenceUtils;
import com.volive.mrhow.util.RetrofitClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage;

public class ProfileFragment extends Fragment {
    public static String background_video = "";
    final int CAMERA_CAPTURE = 1;
    final int PICK_IMAGE = 2;
    Button button_signout;
    TextView text_privateinformation, text_videooptions, text_aboutus, text_contactus, text_faqs, text_notifications,
            text_sharetheapp, text_username, text_enrolledcourses, text_followedtrainers, text_mydownloads, text_registerastrainer;
    ImageView image_back, image_notification, image_add;
    CircleImageView image_profile;
    PreferenceUtils preferenceUtils;
    NetworkConnection networkConnection;
    String email = "", picturePath = "", userid = "", useremail = "", userphone = "", usergender = "";
    String trainer_updates = "", recommendations = "",language="";
    ProgressDialog progressDialog, progressDialog1, progressDialog2, progressDialog3;

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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        preferenceUtils = new PreferenceUtils(getActivity());
        networkConnection = new NetworkConnection(getActivity());
        userid = preferenceUtils.getStringFromPreference(PreferenceUtils.userid, "");
        language = preferenceUtils.getStringFromPreference(PreferenceUtils.Language,"");

        initializeUI(view);
        initializeValues();


        return view;
    }


    private void initializeUI(View view) {
        image_back = view.findViewById(R.id.image_back);
        text_videooptions = view.findViewById(R.id.text_videooptions);
        image_notification = view.findViewById(R.id.image_notification);
        image_profile = view.findViewById(R.id.image_profile);
        image_add = view.findViewById(R.id.image_add);
        text_privateinformation = view.findViewById(R.id.text_privateinformation);
        text_aboutus = view.findViewById(R.id.text_aboutus);
        text_contactus = view.findViewById(R.id.text_contactus);
        text_faqs = view.findViewById(R.id.text_faqs);
        text_notifications = view.findViewById(R.id.text_notifications);
        button_signout = view.findViewById(R.id.button_signout);
        text_sharetheapp = view.findViewById(R.id.text_sharetheapp);
        text_username = view.findViewById(R.id.text_username);
        text_enrolledcourses = view.findViewById(R.id.text_enrolledcourses);
        text_followedtrainers = view.findViewById(R.id.text_followedtrainers);
        text_mydownloads = view.findViewById(R.id.text_mydownloads);
        text_registerastrainer = view.findViewById(R.id.text_registerastrainer);

    }

    private void initializeValues() {
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), HomeNavigation.class));
                getActivity().finish();
            }
        });


        text_mydownloads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyDownloadsActivity.class);
                startActivity(intent);

            }
        });


        image_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUp();
            }
        });


        text_videooptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), VideoOptionsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("background_video", background_video);
                getActivity().startActivity(intent);
            }
        });

        text_aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AboutUsActivity.class));
            }
        });

        text_contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", email, null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "MrHow");
                getActivity().startActivity(Intent.createChooser(emailIntent, null));


            }
        });

        text_faqs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FAQActivity.class));
            }
        });

        text_notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NotificationsActivity.class));
            }
        });

        text_registerastrainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://volive.in/mrhow_dev/home/switch_to_trainer?user_id=" + userid;
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);


            }
        });

        text_privateinformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PrivateInformationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("useremail", useremail);
                intent.putExtra("userphone", userphone);
                intent.putExtra("usergender", usergender);
                getActivity().startActivity(intent);
            }
        });

        text_notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NotificationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("trainer_updates", trainer_updates);
                intent.putExtra("recommendations", recommendations);
                getActivity().startActivity(intent);
            }
        });

        button_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferenceUtils.saveString(PreferenceUtils.userid, "");
                preferenceUtils.saveBoolean(PreferenceUtils.LOGIN, false);
                signOut();
            }
        });

        image_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NotificationsActivity.class);
                startActivity(intent);
            }
        });

        text_sharetheapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                String shareMessage = "\nLet me recommend you this application\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            }
        });


    }


    private void signOut() {
        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement> call = apiClass.logout(userid, language, MainUrl.apikey);
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

                            if (preferenceUtils.getStringFromPreference(PreferenceUtils.Language, "").equalsIgnoreCase("ar")) {
                                String languageToLoad = "ar"; // your language
                                Locale locale = new Locale(languageToLoad);
                                Locale.setDefault(locale);
                                Configuration config = new Configuration();
                                config.locale = locale;
                                getResources().updateConfiguration(config, getResources().getDisplayMetrics());
                                preferenceUtils.setLanguage("ar");
                                Intent intent = new Intent(getActivity(), LogInActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                getActivity().startActivity(intent);
                                getActivity().finish();

                            } else {
                                String languageToLoad = "en"; // your language
                                Locale locale = new Locale(languageToLoad);
                                Locale.setDefault(locale);
                                Configuration config = new Configuration();
                                config.locale = locale;
                                getResources().updateConfiguration(config, getResources().getDisplayMetrics());
                                preferenceUtils.setLanguage("en");
                                Intent intent = new Intent(getActivity(), LogInActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                getActivity().startActivity(intent);
                                getActivity().finish();

                            }

                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog1.dismiss();
                        Log.e("dgdfg ", e.toString());

                    }

                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog1.dismiss();
                Log.e("jsdfghdfjg", t.toString());

            }
        });
    }

    private void userProfile() {
        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement> call = apiClass.userprofile(userid, language, MainUrl.apikey);
        progressDialog2 = DialogsUtils.showProgressDialog(getActivity(), String.valueOf(getResources().getText(R.string.loading)));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDialog2.dismiss();
                if (response.isSuccessful()) {
                    try {

                        JSONObject jsonObject = new JSONObject(response.body().toString());

                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status.equalsIgnoreCase("1")) {

                            JSONObject dataobject = jsonObject.getJSONObject("data");
                            String user_id = dataobject.getString("user_id");
                            String name = dataobject.getString("name");
                            useremail = dataobject.getString("email");
                            userphone = dataobject.getString("phone");
                            usergender = dataobject.getString("gender");
                            String profile_pic = dataobject.getString("profile_pic");
                            String video_quality = dataobject.getString("video_quality");
                            String wifi_only = dataobject.getString("wifi_only");
                            background_video = dataobject.getString("background_video");
                            trainer_updates = dataobject.getString("trainer_updates");
                            recommendations = dataobject.getString("recommendations");
                            String enrolled_courses = dataobject.getString("enrolled_courses");
                            String followed_trainers = dataobject.getString("followed_trainers");

                            preferenceUtils.saveString(PreferenceUtils.userimage, profile_pic);
                            preferenceUtils.saveString(PreferenceUtils.backgroundvideo, background_video);

                            Glide.with(getActivity()).load(MainUrl.imageurl + profile_pic).into(image_profile);

                            Glide.with(getActivity()).load(MainUrl.imageurl + profile_pic).apply(RequestOptions.circleCropTransform()).into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    HomeNavigation.item.setIcon(resource);
                                }
                            });
                            text_enrolledcourses.setText(enrolled_courses);
                            text_followedtrainers.setText(followed_trainers);
                            text_username.setText(name);

                        }

                        progressDialog2.dismiss();

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog2.dismiss();
                        Log.e("dfsdf ", e.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog2.dismiss();
                Log.e("sdjfsdkfsd ", t.toString());

            }
        });
    }

    private void contactus() {
        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement> call = apiClass.contactemail(MainUrl.apikey, language);
        progressDialog3 = DialogsUtils.showProgressDialog(getActivity(), String.valueOf(getResources().getText(R.string.loading)));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDialog3.dismiss();
                if (response.isSuccessful()) {
                    try {

                        JSONObject jsonObject = new JSONObject(response.body().toString());

                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status.equalsIgnoreCase("1")) {

                            JSONObject dataobject = jsonObject.getJSONObject("data");
                            email = dataobject.getString("email");


                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }

                        progressDialog3.dismiss();


                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog3.dismiss();
                        Log.e("sdkfdsf ", e.toString());
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog3.dismiss();
                Log.e("sdkfsdf ", t.toString());

            }
        });


    }

    private void showPopUp() {
        final CharSequence[] items = {getResources().getText(R.string.camera), getResources().getText(R.string.gallery), getResources().getText(R.string.cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getText(R.string.addphoto));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getResources().getText(R.string.camera))) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA_CAPTURE);

                } else if (items[item].equals(getResources().getText(R.string.gallery))) {

                    try {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, PICK_IMAGE);
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }


                } else if (items[item].equals(getResources().getText(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_CAPTURE) {
            if (resultCode == RESULT_OK) {
                onCaptureImageResult(data);
            }
        } else if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                onSelectFromGalleryResult(data);
            }
        }

    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
            picturePath = destination.getAbsolutePath();

            updateProfile();
            Log.e("Camera Path", destination.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ExifInterface ei = new ExifInterface(picturePath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            Bitmap rotatedBitmap = null;
            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(thumbnail, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(thumbnail, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(thumbnail, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = thumbnail;
            }

            image_profile.setImageBitmap(rotatedBitmap);
            Glide.with(getActivity()).load(rotatedBitmap).apply(RequestOptions.circleCropTransform()).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    HomeNavigation.item.setIcon(resource);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                picturePath = GalleryUriToPath.getPath(getActivity(), selectedImage);

                updateProfile();

                Log.e("Gallery Path", picturePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        image_profile.setImageBitmap(bm);
        Glide.with(getActivity()).load(bm).apply(RequestOptions.circleCropTransform()).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                HomeNavigation.item.setIcon(resource);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (networkConnection.isConnectingToInternet()) {
            contactus();
            userProfile();

        } else {
            Toast.makeText(getActivity(), getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateProfile() {
        File file = null;
        MultipartBody.Part image_profile = null;
        if (picturePath != null && !picturePath.isEmpty()) {
            file = new File(picturePath);
            RequestBody requestBody = RequestBody.create(MediaType.parse(getMimeType(picturePath)), file);
            image_profile = MultipartBody.Part.createFormData("profile_pic", file.getName(), requestBody);
            Log.d("Image", ">>>>>>>>>>" + image_profile);
        }

        RequestBody r_api_key = RequestBody.create(MediaType.parse("multipart/form-data"), MainUrl.apikey);
        RequestBody r_lang = RequestBody.create(MediaType.parse("multipart/form-data"), language);
        RequestBody r_userID = RequestBody.create(MediaType.parse("multipart/form-data"), preferenceUtils.getStringFromPreference(PreferenceUtils.userid, ""));

        ApiClass service = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.updateprofile(r_userID, r_api_key, r_lang, image_profile);
        progressDialog = DialogsUtils.showProgressDialog(getActivity(), String.valueOf(getResources().getText(R.string.loading)));

        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    try {
                        JSONObject regObject = new JSONObject(response.body().toString());
                        String status = regObject.getString("status");
                        String message = regObject.getString("message");


                        if (status.equalsIgnoreCase("1")) {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                            JSONObject dataobject = regObject.getJSONObject("data");
                            String profile_pic = dataobject.getString("profile_pic");

                            preferenceUtils.saveString(PreferenceUtils.userimage, profile_pic);

                            Glide.with(getActivity()).load(MainUrl.imageurl + profile_pic).apply(RequestOptions.circleCropTransform()).into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    HomeNavigation.item.setIcon(resource);
                                }
                            });


                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        progressDialog.dismiss();
                        Log.e("error", e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
            }
        });

    }


}
