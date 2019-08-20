package com.volive.mrhow.adapters;

import android.content.Context;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.volive.mrhow.R;


import com.volive.mrhow.models.ReviewsModels;


import java.util.ArrayList;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.Holder> {
    ArrayList<ReviewsModels> reviewsModels;
    Context context;
    String from;

    public ReviewsAdapter(Context context,   ArrayList<ReviewsModels> reviewsModels,String from) {
        this.context = context;
        this.reviewsModels = reviewsModels;
        this.from=from;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = (LayoutInflater.from(viewGroup.getContext())).inflate(R.layout.item_review, viewGroup, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {

        holder.text_review.setText(reviewsModels.get(position).getReview());
        holder.text_name.setText(reviewsModels.get(position).getReviewname());
        holder.text_date.setText(reviewsModels.get(position).getCreated_on());

        String rating = reviewsModels.get(position).getRating();

        if (rating.equalsIgnoreCase("0") || rating.isEmpty()) {
            holder.image_one.setImageResource(R.drawable.star_gray);
            holder.image_two.setImageResource(R.drawable.star_gray);
            holder.image_three.setImageResource(R.drawable.star_gray);
            holder.image_four.setImageResource(R.drawable.star_gray);
            holder.image_five.setImageResource(R.drawable.star_gray);

        } else if (rating.equalsIgnoreCase("1")) {
            holder.image_one.setImageResource(R.drawable.star);
            holder.image_two.setImageResource(R.drawable.star_gray);
            holder.image_three.setImageResource(R.drawable.star_gray);
            holder.image_four.setImageResource(R.drawable.star_gray);
            holder.image_five.setImageResource(R.drawable.star_gray);

        } else if (rating.equalsIgnoreCase("2")) {
            holder.image_one.setImageResource(R.drawable.star);
            holder.image_two.setImageResource(R.drawable.star);
            holder.image_three.setImageResource(R.drawable.star_gray);
            holder.image_four.setImageResource(R.drawable.star_gray);
            holder.image_five.setImageResource(R.drawable.star_gray);

        } else if (rating.equalsIgnoreCase("3")) {
            holder.image_one.setImageResource(R.drawable.star);
            holder.image_two.setImageResource(R.drawable.star);
            holder.image_three.setImageResource(R.drawable.star);
            holder.image_four.setImageResource(R.drawable.star_gray);
            holder.image_five.setImageResource(R.drawable.star_gray);

        } else if (rating.equalsIgnoreCase("4")) {
            holder.image_one.setImageResource(R.drawable.star);
            holder.image_two.setImageResource(R.drawable.star);
            holder.image_three.setImageResource(R.drawable.star);
            holder.image_four.setImageResource(R.drawable.star);
            holder.image_five.setImageResource(R.drawable.star_gray);

        } else if (rating.equalsIgnoreCase("5")) {
            holder.image_one.setImageResource(R.drawable.star);
            holder.image_two.setImageResource(R.drawable.star);
            holder.image_three.setImageResource(R.drawable.star);
            holder.image_four.setImageResource(R.drawable.star);
            holder.image_five.setImageResource(R.drawable.star);
        } else {
            holder.image_one.setImageResource(R.drawable.star_gray);
            holder.image_two.setImageResource(R.drawable.star_gray);
            holder.image_three.setImageResource(R.drawable.star_gray);
            holder.image_four.setImageResource(R.drawable.star_gray);
            holder.image_five.setImageResource(R.drawable.star_gray);
        }

    }

    @Override
    public int getItemCount() {
        if(from.equalsIgnoreCase("viewmore")) {
            return reviewsModels.size();
        }else {
            if (reviewsModels != null) {
                if (reviewsModels.size() > 1)
                    return 1;
                return reviewsModels.size();
            }
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView image_one,image_two,image_three,image_four,image_five;
        TextView text_review,text_name,text_date;

        public Holder(@NonNull View itemView) {
            super(itemView);
            image_one = itemView.findViewById(R.id.image_one);
            image_two = itemView.findViewById(R.id.image_two);
            image_three = itemView.findViewById(R.id.image_three);
            image_four = itemView.findViewById(R.id.image_four);
            image_five = itemView.findViewById(R.id.image_five);
            text_review = itemView.findViewById(R.id.text_review);
            text_name = itemView.findViewById(R.id.text_name);
            text_date = itemView.findViewById(R.id.text_date);

        }
    }
}
