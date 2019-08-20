package com.volive.mrhow.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.volive.mrhow.R;
import com.volive.mrhow.activities.ViewDetailsActivity;
import com.volive.mrhow.models.WishlistCourseModels;
import com.volive.mrhow.util.MainUrl;

import java.util.ArrayList;

public class WishlistCourseAdapter extends RecyclerView.Adapter<WishlistCourseAdapter.Holder> {
    ArrayList<WishlistCourseModels> whislistcoursemodels;
    Context context;

    public WishlistCourseAdapter(Context context, ArrayList<WishlistCourseModels> whislistcoursemodels) {
        this.context = context;
        this.whislistcoursemodels = whislistcoursemodels;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = (LayoutInflater.from(viewGroup.getContext())).inflate(R.layout.recycler_wishlist_courses, viewGroup, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {

        final String cover = whislistcoursemodels.get(position).getCover_type();
        if (cover.equalsIgnoreCase("image")) {
            Glide.with(context).load(MainUrl.imageurl + whislistcoursemodels.get(position).getCover()).into(holder.image_wishlist);
        } else {
            Glide.with(context).load(MainUrl.imageurl + whislistcoursemodels.get(position).getThumbnail()).into(holder.image_wishlist);
        }
        holder.text_title.setText(whislistcoursemodels.get(position).getCourse_title());
        holder.text_duration.setText(whislistcoursemodels.get(position).getDuration());
        holder.text_rating.setText(whislistcoursemodels.get(position).getTotal_ratings());
        holder.text_purchasedcount.setText(whislistcoursemodels.get(position).getPurchased());
        holder.text_price.setText(whislistcoursemodels.get(position).getPrice() + " "+whislistcoursemodels.get(position).getCurrency());
        holder.text_offerprice.setText(whislistcoursemodels.get(position).getOffer_price());

        String tags = whislistcoursemodels.get(position).getTags();
        if (tags.equalsIgnoreCase("1")) {

            holder.image_hotnew.setVisibility(View.GONE);
            holder.image_new.setVisibility(View.VISIBLE);
        } else if(tags.equalsIgnoreCase("2")){
            holder.image_hotnew.setVisibility(View.VISIBLE);
            holder.image_new.setVisibility(View.GONE);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewDetailsActivity.class);
                intent.putExtra("courseid", whislistcoursemodels.get(position).getCourse_id());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return whislistcoursemodels.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView image_wishlist, image_hotnew, image_new;
        TextView text_duration, text_rating, text_purchasedcount, text_title, text_price, text_offerprice;

        public Holder(@NonNull View itemView) {
            super(itemView);
            image_wishlist = itemView.findViewById(R.id.image_wishlist);
            image_hotnew = itemView.findViewById(R.id.image_hotnew);
            image_new = itemView.findViewById(R.id.image_new);
            text_duration = itemView.findViewById(R.id.text_duration);
            text_rating = itemView.findViewById(R.id.text_rating);
            text_purchasedcount = itemView.findViewById(R.id.text_purchasedcount);
            text_title = itemView.findViewById(R.id.text_title);
            text_price = itemView.findViewById(R.id.text_price);
            text_offerprice = itemView.findViewById(R.id.text_offerprice);

        }
    }
}

