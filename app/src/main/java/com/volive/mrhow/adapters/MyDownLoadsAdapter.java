package com.volive.mrhow.adapters;

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
import com.volive.mrhow.activities.MyDownloadsActivity;
import com.volive.mrhow.activities.VideoPlayerActivity;
import com.volive.mrhow.models.MyDownloadModels;
import com.volive.mrhow.util.ApiClass;
import com.volive.mrhow.util.MainUrl;
import com.volive.mrhow.util.NetworkConnection;
import com.volive.mrhow.util.PreferenceUtils;
import com.volive.mrhow.util.RetrofitClass;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyDownLoadsAdapter extends RecyclerView.Adapter<MyDownLoadsAdapter.Holder> {
    Context context;
    ArrayList<MyDownloadModels> myDownloadModels;
    NetworkConnection networkConnection;
    PreferenceUtils preferenceUtils;
    String userid = "",language="";

    public MyDownLoadsAdapter(Context context, ArrayList<MyDownloadModels> myDownloadModels) {
        this.context = context;
        this.myDownloadModels = myDownloadModels;

    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = (LayoutInflater.from(viewGroup.getContext())).inflate(R.layout.item_mydownload, viewGroup, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {

        preferenceUtils = new PreferenceUtils(context);
        networkConnection = new NetworkConnection(context);
        userid = preferenceUtils.getStringFromPreference(PreferenceUtils.userid, "");
        language = preferenceUtils.getStringFromPreference(PreferenceUtils.Language,"");
        holder.text_lessonname.setText(myDownloadModels.get(position).getFile_name());


        final String filetype = myDownloadModels.get(position).getFile_type();
        if(filetype.equalsIgnoreCase("video")) {
            Glide.with(context).load(MainUrl.imageurl + myDownloadModels.get(position).getThumbnail()).into(holder.image_lesson);
        }else if(filetype.equalsIgnoreCase("image")){
            Glide.with(context).load(MainUrl.imageurl+myDownloadModels.get(position).getFile()).into(holder.image_lesson);
        }else if(filetype.equalsIgnoreCase("pdf")){
            holder.image_lesson.setImageResource(R.drawable.pdfimage);
        }else if(filetype.equalsIgnoreCase("doc")||filetype.equalsIgnoreCase("docx")){
            holder.image_lesson.setImageResource(R.drawable.wordimage);
        }else if(filetype.equalsIgnoreCase("xlsx")||filetype.equalsIgnoreCase("xls")){
            holder.image_lesson.setImageResource(R.drawable.excel);
        }else if(filetype.equalsIgnoreCase("ppt")||filetype.equalsIgnoreCase("pptx")){
            holder.image_lesson.setImageResource(R.drawable.ppt);
        }else {
            Glide.with(context).load(MainUrl.imageurl+myDownloadModels.get(position).getFile()).into(holder.image_lesson);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(filetype.equalsIgnoreCase("video")) {
                    Intent intent = new Intent(context, VideoPlayerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("cover", myDownloadModels.get(position).getFile());
                    context.startActivity(intent);
                }else {
                    String url =MainUrl.imageurl+myDownloadModels.get(position).getFile();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    context.startActivity(i);
                }


            }
        });

        holder.image_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String courseid = myDownloadModels.get(position).getCourse_id();
                Log.e("courseid ",courseid);

                if (networkConnection.isConnectingToInternet()) {
                    removeDownloads(courseid);
                } else {
                    Toast.makeText(context, context.getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    @Override
    public int getItemCount() {
        return myDownloadModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
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

                            if (context instanceof MyDownloadsActivity) {
                                ((MyDownloadsActivity)context).getMyDownloads();
                            }

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

    public class Holder extends RecyclerView.ViewHolder {
        ImageView image_lesson, image_delete;
        TextView text_lessonname;

        public Holder(@NonNull View itemView) {
            super(itemView);
            image_lesson = itemView.findViewById(R.id.image_lesson);
            image_delete = itemView.findViewById(R.id.image_delete);
            text_lessonname = itemView.findViewById(R.id.text_lessonname);

        }
    }

}

