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
import com.volive.mrhow.models.NewCourseModels;
import com.volive.mrhow.util.MainUrl;

import java.util.ArrayList;

public class RecommendedCourseAdapter extends RecyclerView.Adapter<RecommendedCourseAdapter.Holder> {
    ArrayList<NewCourseModels> newCourseModels;
    Context context;

    public RecommendedCourseAdapter(Context context, ArrayList<NewCourseModels> newCourseModels) {
        this.context = context;
        this.newCourseModels = newCourseModels;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = (LayoutInflater.from(viewGroup.getContext())).inflate(R.layout.recycler_new_course_home, viewGroup, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {

        String covertype = newCourseModels.get(position).getCover_type();
        if (covertype.equalsIgnoreCase("image")) {
            Glide.with(context).load(MainUrl.imageurl + newCourseModels.get(position).getCover()).into(holder.imageview);
        } else {
            Glide.with(context).load(MainUrl.imageurl + newCourseModels.get(position).getThumbnail()).into(holder.imageview);
        }
        holder.text_title.setText(newCourseModels.get(position).getCourse_title());

        String offer_price = newCourseModels.get(position).getOffer_price();

        if (offer_price.equalsIgnoreCase("") || offer_price.equalsIgnoreCase("0")) {
            holder.text_price.setText(newCourseModels.get(position).getPrice() + " " + newCourseModels.get(position).getCurrency());
        } else {
            holder.text_price.setText(newCourseModels.get(position).getPrice() + " " + newCourseModels.get(position).getCurrency());
            holder.text_offerprice.setText(newCourseModels.get(position).getOffer_price());
            holder.text_price.setBackgroundResource(R.drawable.strikeline);
        }


//        holder.text_price.setText(newCourseModels.get(position).getPrice() + " SAR");
//        holder.text_offerprice.setText(newCourseModels.get(position).getOffer_price());
        holder.text_ratings.setText(newCourseModels.get(position).getTotal_ratings());
        holder.text_views.setText(newCourseModels.get(position).getPurchased());
        holder.text_time.setText(newCourseModels.get(position).getDuration());

        String tags = newCourseModels.get(position).getTags();
        if (tags.equalsIgnoreCase("1")) {
            holder.image_new.setVisibility(View.GONE);
            holder.image_hotnew.setVisibility(View.VISIBLE);
        } else if(tags.equalsIgnoreCase("2")) {
            holder.image_new.setVisibility(View.VISIBLE);
            holder.image_hotnew.setVisibility(View.GONE);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("courseid", newCourseModels.get(position).getCourse_id());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return newCourseModels.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView imageview, image_hotnew, image_new;
        TextView text_title, text_time, text_ratings, text_views, text_price, text_offerprice;

        public Holder(@NonNull View itemView) {
            super(itemView);
            imageview = itemView.findViewById(R.id.imageview);
            image_hotnew = itemView.findViewById(R.id.image_hotnew);
            text_title = itemView.findViewById(R.id.text_title);
            image_new = itemView.findViewById(R.id.image_new);
            text_time = itemView.findViewById(R.id.text_time);
            text_ratings = itemView.findViewById(R.id.text_ratings);
            text_views = itemView.findViewById(R.id.text_views);
            text_price = itemView.findViewById(R.id.text_price);
            text_offerprice = itemView.findViewById(R.id.text_offerprice);


        }
    }
}
