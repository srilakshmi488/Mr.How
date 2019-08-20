package com.volive.mrhow.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.bumptech.glide.Glide;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.google.gson.JsonElement;
import com.volive.mrhow.R;
import com.volive.mrhow.activities.CertificateActivity;
import com.volive.mrhow.activities.MyDownloadsActivity;
import com.volive.mrhow.activities.RateReviewActivity;
import com.volive.mrhow.activities.ViewDetailsActivity;
import com.volive.mrhow.fragments.MyCourseFragment;
import com.volive.mrhow.models.EnrolledCourseModels;
import com.volive.mrhow.util.ApiClass;
import com.volive.mrhow.util.DialogsUtils;
import com.volive.mrhow.util.MainUrl;
import com.volive.mrhow.util.NetworkConnection;
import com.volive.mrhow.util.PreferenceUtils;
import com.volive.mrhow.util.RetrofitClass;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SwipeRecyclerAdapter extends RecyclerSwipeAdapter<SwipeRecyclerAdapter.SimpleViewHolder> {
    ArrayList<EnrolledCourseModels> enrolledCourseModels;
    Context context;
    OnItemClick onItemClick;
    PreferenceUtils preferenceUtils;
    NetworkConnection networkConnection;
    ProgressDialog progressDialog;
    String userid = "", language = "";
    MyCourseFragment myCourseFragment;
    private SwipeLayout preswipes = null;


    public SwipeRecyclerAdapter(Context context, ArrayList<EnrolledCourseModels> enrolledCourseModels, MyCourseFragment myCourseFragment) {
        this.context = context;
        this.enrolledCourseModels = enrolledCourseModels;
        this.myCourseFragment = myCourseFragment;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_enrolled_courses, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {

        preferenceUtils = new PreferenceUtils(context);
        networkConnection = new NetworkConnection(context);
        userid = preferenceUtils.getStringFromPreference(PreferenceUtils.userid, "");
        language = preferenceUtils.getStringFromPreference(PreferenceUtils.Language, "");

        String covertype = enrolledCourseModels.get(position).getCover_type();
        if (covertype.equalsIgnoreCase("image")) {
            Glide.with(context).load(MainUrl.imageurl + enrolledCourseModels.get(position).getCover()).into(viewHolder.image_view);
        } else {
            Glide.with(context).load(MainUrl.imageurl + enrolledCourseModels.get(position).getThumbnail()).into(viewHolder.image_view);
        }
        viewHolder.text_duration.setText(enrolledCourseModels.get(position).getDuration());
        viewHolder.text_rating.setText(enrolledCourseModels.get(position).getTotal_ratings());
        viewHolder.text_purchased.setText(enrolledCourseModels.get(position).getPurchased());
        viewHolder.text_title.setText(enrolledCourseModels.get(position).getCourse_title());
        viewHolder.txt_name.setText(enrolledCourseModels.get(position).getName());

        String tags = enrolledCourseModels.get(position).getTags();
        if (tags.equalsIgnoreCase("1")) {
            viewHolder.image_hotnew.setVisibility(View.GONE);
            viewHolder.image_new.setVisibility(View.VISIBLE);
        } else if (tags.equalsIgnoreCase("2")) {
            viewHolder.image_hotnew.setVisibility(View.VISIBLE);
            viewHolder.image_new.setVisibility(View.GONE);
        }

        String downloadable = enrolledCourseModels.get(position).getDownloadable();
        String coursecompletion = enrolledCourseModels.get(position).getCourse_completion();
        String downloadstatus = enrolledCourseModels.get(position).getDownload_status();

//        if (downloadable.equalsIgnoreCase("1") && !coursecompletion.equalsIgnoreCase("100")) {
//            viewHolder.download_image.setVisibility(View.VISIBLE);
//            viewHolder.image_downloaded.setVisibility(View.GONE);
//        } else {
//            viewHolder.download_image.setVisibility(View.GONE);
//        }
//
//        if (downloadstatus.equalsIgnoreCase("1")) {
//            viewHolder.image_downloaded.setVisibility(View.VISIBLE);
//            viewHolder.download_image.setVisibility(View.GONE);
//        } else {
//            viewHolder.image_downloaded.setVisibility(View.GONE);
//        }


        viewHolder.progress_1.setProgressColor(Color.parseColor("#0DCD78"));
        viewHolder.progress_1.setProgressBackgroundColor(Color.parseColor("#E5E2E2"));
        viewHolder.progress_1.setMax(100);
        viewHolder.progress_1.setProgress(Float.parseFloat(coursecompletion));

        if (coursecompletion.equalsIgnoreCase("100")) {
            viewHolder.txt_percentage.setText(context.getResources().getText(R.string.coursecompleted));
            viewHolder.image_certificate.setVisibility(View.VISIBLE);
            viewHolder.button_uploadprojects.setVisibility(View.VISIBLE);
            viewHolder.image_rating.setVisibility(View.VISIBLE);

            viewHolder.image_certificate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CertificateActivity.class);
                    intent.putExtra("courseid", enrolledCourseModels.get(position).getCourse_id());
                    context.startActivity(intent);
                }
            });

        } else {
            viewHolder.txt_percentage.setText(coursecompletion + "%" + "" + context.getResources().getText(R.string.completed));
            viewHolder.image_certificate.setVisibility(View.GONE);
        }


        if (coursecompletion.equalsIgnoreCase("100")) {
            viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
            if(language.equalsIgnoreCase("en")) {
                viewHolder.swipeLayout.setLeftSwipeEnabled(false);
                viewHolder.swipeLayout.setRightSwipeEnabled(true);
                viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, viewHolder.swipeLayout.findViewById(R.id.bottom_wrapper));
            }else if(language.equalsIgnoreCase("ar")){
                viewHolder.swipeLayout.setLeftSwipeEnabled(true);
                viewHolder.swipeLayout.setRightSwipeEnabled(false);
                viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, viewHolder.swipeLayout.findViewById(R.id.bottom_wrapper));
            }else {
                viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, viewHolder.swipeLayout.findViewById(R.id.bottom_wrapper));
            }
            viewHolder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
                @Override
                public void onClose(SwipeLayout layout) {

                }

                @Override
                public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

                }

                @Override
                public void onStartOpen(SwipeLayout layout) {

                }

                @Override
                public void onOpen(SwipeLayout layout) {

                }

                @Override
                public void onStartClose(SwipeLayout layout) {

                }

                @Override
                public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

                }
            });
        } else {

            viewHolder.swipeLayout.setSwipeEnabled(false);
        }


        viewHolder.image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewDetailsActivity.class);
                intent.putExtra("courseid", enrolledCourseModels.get(position).getCourse_id());
                context.startActivity(intent);

            }
        });


        viewHolder.download_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String courseid = enrolledCourseModels.get(position).getCourse_id();
                if (networkConnection.isConnectingToInternet()) {
                    addToDownloads(courseid);
                } else {
                    Toast.makeText(context, context.getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
                }

            }
        });


        viewHolder.image_downloaded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String courseid = enrolledCourseModels.get(position).getCourse_id();
                if (networkConnection.isConnectingToInternet()) {
                    removeDownloads(courseid);
                } else {
                    Toast.makeText(context, context.getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
                }

            }
        });


        viewHolder.button_uploadprojects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onItemClick.getPosition(enrolledCourseModels.get(position).getCourse_id());

            }
        });

        viewHolder.image_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RateReviewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("courseid", enrolledCourseModels.get(position).getCourse_id());
                intent.putExtra("image", enrolledCourseModels.get(position).getCover());
                intent.putExtra("tags", enrolledCourseModels.get(position).getTags());
                intent.putExtra("duration", enrolledCourseModels.get(position).getDuration());
                intent.putExtra("ratings", enrolledCourseModels.get(position).getTotal_ratings());
                intent.putExtra("purchased", enrolledCourseModels.get(position).getPurchased());
                intent.putExtra("price", enrolledCourseModels.get(position).getPrice());
                intent.putExtra("offerprice", enrolledCourseModels.get(position).getOffer_price());
                intent.putExtra("title", enrolledCourseModels.get(position).getCourse_title());
                context.startActivity(intent);
            }
        });

    }


    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @Override
    public int getItemCount() {
        return enrolledCourseModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipeLayout;
    }

    private void addToDownloads(String courseid) {
        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement> call = apiClass.addtodownloads(userid, courseid, language, MainUrl.apikey);
        progressDialog = DialogsUtils.showProgressDialog(context, String.valueOf(context.getResources().getText(R.string.loading)));
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
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(context, MyDownloadsActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);
                        } else {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Log.e("sdfkdsfg ", e.toString());
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("sdfdf ", t.toString());

            }
        });


    }

    private void removeDownloads(final String courseid) {
        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement> call = apiClass.removedownloads(userid, courseid, language, MainUrl.apikey);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    try {

                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        if (status.equalsIgnoreCase("1")) {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                            myCourseFragment.myCourses();


                        } else {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("sdfsdf ", e.toString());
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e("sdfdsf ", t.toString());

            }
        });
    }

    public interface OnItemClick {
        void getPosition(String data); //pass any things
    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        ImageView image_view, image_hotnew, image_new, download_image, image_rating, image_certificate, image_downloaded;
        TextView text_title, txt_percentage, txt_name;
        RoundCornerProgressBar progress_1;
        Button button_uploadprojects;
        TextView text_duration, text_rating, text_purchased;


        public SimpleViewHolder(View itemView) {
            super(itemView);

            context = itemView.getContext();
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipeLayout);
            image_view = itemView.findViewById(R.id.image_view);
            image_hotnew = itemView.findViewById(R.id.image_hotnew);
            image_new = itemView.findViewById(R.id.image_new);
            text_duration = itemView.findViewById(R.id.text_duration);
            text_rating = itemView.findViewById(R.id.text_rating);
            text_purchased = itemView.findViewById(R.id.text_purchased);
            text_title = itemView.findViewById(R.id.text_title);
            txt_percentage = itemView.findViewById(R.id.txt_percentage);
            txt_name = itemView.findViewById(R.id.txt_name);
            progress_1 = itemView.findViewById(R.id.progress_1);
            download_image = itemView.findViewById(R.id.download_image);
            image_certificate = itemView.findViewById(R.id.image_certificate);
            image_rating = itemView.findViewById(R.id.image_rating);
            button_uploadprojects = itemView.findViewById(R.id.button_uploadprojects);
            image_downloaded = itemView.findViewById(R.id.image_downloaded);


        }

    }

}
