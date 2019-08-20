package com.volive.mrhow.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.volive.mrhow.R;
import com.volive.mrhow.models.ArticleDetailModels;
import com.volive.mrhow.util.MainUrl;

import java.util.ArrayList;

public class ArticleDetailAdapter extends RecyclerView.Adapter<ArticleDetailAdapter.Holder> {
    ArrayList<ArticleDetailModels>articleDetailModels;
    Context context;


    public ArticleDetailAdapter(Context context, ArrayList<ArticleDetailModels>articleDetailModels) {
        this.context = context;
        this.articleDetailModels = articleDetailModels;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = (LayoutInflater.from(viewGroup.getContext())).inflate(R.layout.item_articledetail, viewGroup, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, final int position) {

        holder.text_description.setText(articleDetailModels.get(position).getDetail_description_en());
        String type = articleDetailModels.get(position).getDetail_image_type();
        if(type.equalsIgnoreCase("image")){
            Glide.with(context).load(MainUrl.imageurl+articleDetailModels.get(position).getDetail_image()).into(holder.image_article);
        }else {
            Glide.with(context).load(MainUrl.imageurl+articleDetailModels.get(position).getDetail_thumb()).into(holder.image_article);
        }


    }

    @Override
    public int getItemCount() {
        return articleDetailModels.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView text_description;
        ImageView image_article;

        public Holder(@NonNull View itemView) {
            super(itemView);
            image_article = itemView.findViewById(R.id.image_article);
            text_description = itemView.findViewById(R.id.text_description);


        }
    }
}
