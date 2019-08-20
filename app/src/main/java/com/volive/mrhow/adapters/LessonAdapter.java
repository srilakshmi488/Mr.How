package com.volive.mrhow.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonElement;
import com.volive.mrhow.R;
import com.volive.mrhow.activities.VideoPlayerActivity;
import com.volive.mrhow.models.LessonModels;
import com.volive.mrhow.util.ApiClass;
import com.volive.mrhow.util.DialogsUtils;
import com.volive.mrhow.util.MainUrl;
import com.volive.mrhow.util.PreferenceUtils;
import com.volive.mrhow.util.RetrofitClass;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.Holder> {
    ArrayList<LessonModels> lessonModels;
    Context context;
    String course_id, userid = "", video,purchase_status="",lessontype="",url="";
    PreferenceUtils preferenceUtils;
    ProgressDialog progressDialog;
    String course_title ="",language="";

    public LessonAdapter(Context context, ArrayList<LessonModels> lessonModels, String course_id,String purchase_status) {
        this.context = context;
        this.lessonModels = lessonModels;
        this.course_id = course_id;
        this.purchase_status=purchase_status;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = (LayoutInflater.from(viewGroup.getContext())).inflate(R.layout.recycler_lesson, viewGroup, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, final int position) {

        preferenceUtils = new PreferenceUtils(context);
        userid = preferenceUtils.getStringFromPreference(PreferenceUtils.userid, "");
        language = preferenceUtils.getStringFromPreference(PreferenceUtils.Language,"");

        holder.text_lessonname.setText(lessonModels.get(position).getLesson_name());
        video = lessonModels.get(position).getVideo();
        course_title = lessonModels.get(position).getLesson_name();

        if(purchase_status.equalsIgnoreCase("1")){
            holder.image_lock.setVisibility(View.GONE);
        }else {
            holder.image_lock.setVisibility(View.VISIBLE);
        }

         lessontype = lessonModels.get(position).getLesson_type();
         url =MainUrl.imageurl+lessonModels.get(position).getVideo();

        if(lessontype.equalsIgnoreCase("video")) {
            Glide.with(context).load(MainUrl.imageurl + lessonModels.get(position).getLesson_thumbnail()).into(holder.image_lesson);
        }else if(lessontype.equalsIgnoreCase("image")){
            Glide.with(context).load(MainUrl.imageurl+lessonModels.get(position).getVideo()).into(holder.image_lesson);
        }else if(lessontype.equalsIgnoreCase("pdf")){
            holder.image_lesson.setImageResource(R.drawable.pdfimage);
        }else if(lessontype.equalsIgnoreCase("doc")||lessontype.equalsIgnoreCase("docx")){
            holder.image_lesson.setImageResource(R.drawable.wordimage);
        }else if(lessontype.equalsIgnoreCase("xlsx")||lessontype.equalsIgnoreCase("xls")){
            holder.image_lesson.setImageResource(R.drawable.excel);
        }else if(lessontype.equalsIgnoreCase("ppt")||lessontype.equalsIgnoreCase("pptx")){
            holder.image_lesson.setImageResource(R.drawable.ppt);
        }else {
            Glide.with(context).load(MainUrl.imageurl+lessonModels.get(position).getVideo()).into(holder.image_lesson);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(purchase_status.equalsIgnoreCase("1")) {
                    String lessonid = lessonModels.get(position).getLesson_id();
                    viewLessonVideo(lessonid);
                }else {
                    Toast.makeText(context, context.getResources().getText(R.string.pleasepurchaseyourcourse), Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void viewLessonVideo(String lessonid) {
        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement> call = apiClass.viewlessonvideo(userid, lessonid, course_id, language, MainUrl.apikey);

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

                            if(lessontype.equalsIgnoreCase("video")) {
                                Intent intent = new Intent(context, VideoPlayerActivity.class);
                                intent.putExtra("cover", video);
                                intent.putExtra("course_title",course_title);
                                context.startActivity(intent);
                            }else {
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                context.startActivity(i);
                            }

                        } else {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }

                        progressDialog.dismiss();

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Log.e("jsdfdsf ", e.toString());
                    }


                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("khgdfjghdf ", t.toString());

            }
        });

    }

    @Override
    public int getItemCount() {
        return lessonModels.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView image_lesson,image_lock;
        TextView text_lessonname;

        public Holder(@NonNull View itemView) {
            super(itemView);
            image_lesson = itemView.findViewById(R.id.image_lesson);
            text_lessonname = itemView.findViewById(R.id.text_lessonname);
            image_lock = itemView.findViewById(R.id.image_lock);

        }
    }
}
