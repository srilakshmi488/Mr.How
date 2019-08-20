package com.volive.mrhow.adapters;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.volive.mrhow.activities.DetailsActivity;
import com.volive.mrhow.fragments.StudioFragment;
import com.volive.mrhow.models.StudioModels;
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

public class StudioAdapter extends RecyclerView.Adapter<StudioAdapter.Holder> {
    ArrayList<StudioModels> studioModels;
    Context context;
    ProgressDialog progressDialog;
    NetworkConnection networkConnection;
    PreferenceUtils preferenceUtils;
    String userid = "", hashtag = "",language="";
    StudioFragment studioFragment;


    public StudioAdapter(Context context, ArrayList<StudioModels> studioModels, StudioFragment studioFragment, String hashtag) {
        this.context = context;
        this.studioModels = studioModels;
        this.studioFragment = studioFragment;
        this.hashtag = hashtag;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = (LayoutInflater.from(viewGroup.getContext())).inflate(R.layout.recycler_studio, viewGroup, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {

        preferenceUtils = new PreferenceUtils(context);
        networkConnection = new NetworkConnection(context);
        userid = preferenceUtils.getStringFromPreference(PreferenceUtils.userid, "");
        language = preferenceUtils.getStringFromPreference(PreferenceUtils.Language,"");

        String imagetype = studioModels.get(position).getImage_type();
        if (imagetype.equalsIgnoreCase("image")) {
            Glide.with(context).load(MainUrl.imageurl + studioModels.get(position).getArticle_image()).into(holder.imageview);
        } else {
            Glide.with(context).load(MainUrl.imageurl + studioModels.get(position).getThumbnail()).into(holder.imageview);
        }
        holder.text_favoritecount.setText(studioModels.get(position).getLikes_count());
        holder.text_commentscount.setText(studioModels.get(position).getComments_count());
        holder.text_viewscount.setText(studioModels.get(position).getViews_count());
        holder.text_description.setText(studioModels.get(position).getArticle_description());
        holder.text_writer.setText(studioModels.get(position).getWriter());
        holder.text_date.setText(studioModels.get(position).getDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("articleid", studioModels.get(position).getArticle_id());
                context.startActivity(intent);
            }
        });

        holder.image_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (networkConnection.isConnectingToInternet()) {
                    articleImageLike(studioModels.get(position).getArticle_id());
                } else {
                    Toast.makeText(context, context.getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    @Override
    public int getItemCount() {
        return studioModels.size();
    }

    private void articleImageLike(String article_id) {
        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement> call = apiClass.articlelike(userid, article_id, language, MainUrl.apikey);
//        progressDialog = DialogsUtils.showProgressDialog(context, "Loading..");
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

//                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    try {

                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status.equalsIgnoreCase("1")) {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                            studioFragment.getArticlesList(hashtag);

                        } else {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
//                        progressDialog.dismiss();
                        Log.e("dsfhdsf ", e.toString());
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
//                progressDialog.dismiss();
                Log.e("dfdsf ", t.toString());

            }
        });


    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView imageview, image_like;
        TextView text_favoritecount, text_commentscount, text_viewscount, text_description, text_writer, text_date;

        public Holder(@NonNull View itemView) {
            super(itemView);
            imageview = itemView.findViewById(R.id.imageview);
            text_favoritecount = itemView.findViewById(R.id.text_favoritecount);
            text_commentscount = itemView.findViewById(R.id.text_commentscount);
            text_viewscount = itemView.findViewById(R.id.text_viewscount);
            text_description = itemView.findViewById(R.id.text_description);
            text_writer = itemView.findViewById(R.id.text_writer);
            text_date = itemView.findViewById(R.id.text_date);
            image_like = itemView.findViewById(R.id.image_like);


        }
    }
}

