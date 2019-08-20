package com.volive.mrhow.adapters;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonElement;
import com.volive.mrhow.BuildConfig;
import com.volive.mrhow.R;
import com.volive.mrhow.activities.DetailsActivity;
import com.volive.mrhow.models.CommentsModels;
import com.volive.mrhow.models.SubCommentsModels;
import com.volive.mrhow.util.ApiClass;
import com.volive.mrhow.util.DialogsUtils;
import com.volive.mrhow.util.MainUrl;
import com.volive.mrhow.util.NetworkConnection;
import com.volive.mrhow.util.PreferenceUtils;
import com.volive.mrhow.util.RetrofitClass;

import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.Holder> {
    ArrayList<CommentsModels> commentsModels;
    ArrayList<SubCommentsModels> subCommentsModels;
    Context context;
    Dialog dialog;
    NetworkConnection networkConnection;
    PreferenceUtils preferenceUtils;
    String userid="",articlecommentid="",language="";
    ProgressDialog progressDialog;

    public CommentsAdapter(Context context, ArrayList<CommentsModels> commentsModels,ArrayList<SubCommentsModels> subCommentsModels) {
        this.context = context;
        this.commentsModels = commentsModels;
        this.subCommentsModels= subCommentsModels;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = (LayoutInflater.from(viewGroup.getContext())).inflate(R.layout.recycler_comments, viewGroup, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {

        preferenceUtils = new PreferenceUtils(context);
        networkConnection = new NetworkConnection(context);

        userid= preferenceUtils.getStringFromPreference(PreferenceUtils.userid,"");
        language = preferenceUtils.getStringFromPreference(PreferenceUtils.Language,"");

        Glide.with(context).load(MainUrl.imageurl + commentsModels.get(position).getProfile_pic()).into(holder.image_user);
        holder.text_username.setText(commentsModels.get(position).getName());
        holder.text_time.setText(commentsModels.get(position).getTime());
        holder.text_comment.setText(commentsModels.get(position).getMain_comment());
        holder.text_commentscount.setText(commentsModels.get(position).getArticle_sub_comment_count());
        holder.text_likescount.setText(commentsModels.get(position).getArticle_comment_likes());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        holder.subcomments_recyclerview.setLayoutManager(mLayoutManager);
        holder.subcomments_recyclerview.setItemAnimator(new DefaultItemAnimator());

        SubCommentsAdapter subCommentsAdapter = new SubCommentsAdapter(context,commentsModels.get(position).getSubCommentsModels(),holder.getAdapterPosition());
        holder.subcomments_recyclerview.setAdapter(subCommentsAdapter);
//        subCommentsAdapter.notifyDataSetChanged();


        holder.text_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                articlecommentid = commentsModels.get(position).getArticle_comment_id();
                Log.e("articlecommentid ",articlecommentid);
                openDialog();

            }
        });

        holder.image_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                articlecommentid = commentsModels.get(position).getArticle_comment_id();
                articlecommentlike();
            }
        });


    }



    @Override
    public int getItemCount() {
        return commentsModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class Holder extends RecyclerView.ViewHolder {
        CircleImageView image_user;
        TextView text_username,text_time,text_comment,text_commentscount,text_likescount,text_reply;
        RecyclerView subcomments_recyclerview;
        ImageView image_like;

        public Holder(@NonNull View itemView) {
            super(itemView);
            image_user = itemView.findViewById(R.id.image_user);
            image_like = itemView.findViewById(R.id.image_like);
            text_username = itemView.findViewById(R.id.text_username);
            text_time = itemView.findViewById(R.id.text_time);
            text_comment = itemView.findViewById(R.id.text_comment);
            text_commentscount = itemView.findViewById(R.id.text_commentscount);
            text_likescount = itemView.findViewById(R.id.text_likescount);
            text_reply = itemView.findViewById(R.id.text_reply);
            subcomments_recyclerview = itemView.findViewById(R.id.subcomments_recyclerview);

        }
    }

    private void openDialog() {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.replycomment);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        final EditText edit_comment = (EditText) dialog.findViewById(R.id.edit_comment);
        Button button_send = (Button) dialog.findViewById(R.id.button_send);
        Button button_cancel = (Button)dialog.findViewById(R.id.button_cancel);


        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        dialog.show();

        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = edit_comment.getText().toString().trim();

                if (networkConnection.isConnectingToInternet()) {
                    if (!comment.isEmpty()) {
                        sendReply(userid, articlecommentid, comment);
                    } else {
                        Toast.makeText(context, context.getResources().getText(R.string.pleasewriteyourcomment), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context,context.getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
                }

            }
        });


        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    private void sendReply(String userid, String articlecommentid, String comment) {
        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement> call = apiClass.articlesubcomment(userid, articlecommentid, comment, language, MainUrl.apikey);
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
                            dialog.dismiss();

                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                            ((DetailsActivity) context).articleDetails();


                        } else {
                            dialog.dismiss();
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }

                        dialog.dismiss();
                        progressDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Log.e("sdkfgdsg", e.toString());
                    }


                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("dsfkdsf", t.toString());

            }
        });

    }

    private void articlecommentlike() {

        ApiClass apiClass = RetrofitClass.getRetrofitInstance().create(ApiClass.class);
        Call<JsonElement>call = apiClass.articlecommentlike(userid,articlecommentid,language,MainUrl.apikey);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.isSuccessful()){
                    try {

                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        if(status.equalsIgnoreCase("1")){
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            ((DetailsActivity) context).articleDetails();

                        }else {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }


                    }catch (Exception e){
                        e.printStackTrace();
                        Log.e("gfdkgd",e.toString());
                    }

                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e("dsfsdfkgs ",t.toString());

            }
        });

    }
}
