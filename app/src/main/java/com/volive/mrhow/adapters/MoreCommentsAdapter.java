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
import com.volive.mrhow.models.MoreCommentsModels;

import java.util.ArrayList;

public class MoreCommentsAdapter extends RecyclerView.Adapter<MoreCommentsAdapter.Holder> {
    ArrayList<MoreCommentsModels> moreComments;
    Context context;
    String from;

    public MoreCommentsAdapter(Context context, ArrayList<MoreCommentsModels> moreComments,String from) {
        this.context = context;
        this.moreComments = moreComments;
        this.from=from;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = (LayoutInflater.from(viewGroup.getContext())).inflate(R.layout.recycler_comments_more, viewGroup, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {

        holder.text_comment.setText(moreComments.get(position).getComment());
        holder.text_date.setText(moreComments.get(position).getTime());

        String commentrating = moreComments.get(position).getComment_rating();
        if (commentrating.equalsIgnoreCase("0") || commentrating.isEmpty()) {
            holder.image_one.setImageResource(R.drawable.star_gray);
            holder.image_two.setImageResource(R.drawable.star_gray);
            holder.image_three.setImageResource(R.drawable.star_gray);
            holder.image_four.setImageResource(R.drawable.star_gray);
            holder.image_five.setImageResource(R.drawable.star_gray);

        } else if (commentrating.equalsIgnoreCase("1")) {
            holder.image_one.setImageResource(R.drawable.star);
            holder.image_two.setImageResource(R.drawable.star_gray);
            holder.image_three.setImageResource(R.drawable.star_gray);
            holder.image_four.setImageResource(R.drawable.star_gray);
            holder.image_five.setImageResource(R.drawable.star_gray);

        } else if (commentrating.equalsIgnoreCase("2")) {
            holder.image_one.setImageResource(R.drawable.star);
            holder.image_two.setImageResource(R.drawable.star);
            holder.image_three.setImageResource(R.drawable.star_gray);
            holder.image_four.setImageResource(R.drawable.star_gray);
            holder.image_five.setImageResource(R.drawable.star_gray);

        } else if (commentrating.equalsIgnoreCase("3")) {
            holder.image_one.setImageResource(R.drawable.star);
            holder.image_two.setImageResource(R.drawable.star);
            holder.image_three.setImageResource(R.drawable.star);
            holder.image_four.setImageResource(R.drawable.star_gray);
            holder.image_five.setImageResource(R.drawable.star_gray);

        } else if (commentrating.equalsIgnoreCase("4")) {
            holder.image_one.setImageResource(R.drawable.star);
            holder.image_two.setImageResource(R.drawable.star);
            holder.image_three.setImageResource(R.drawable.star);
            holder.image_four.setImageResource(R.drawable.star);
            holder.image_five.setImageResource(R.drawable.star_gray);

        } else if (commentrating.equalsIgnoreCase("5")) {
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
        if(from.equalsIgnoreCase("withsee")) {
            return moreComments.size();
        }else {
            if (moreComments != null) {
                if (moreComments.size() > 1)
                    return 1;
                return moreComments.size();
            }
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView text_comment, text_date;
        ImageView image_one, image_two, image_three, image_four, image_five;


        public Holder(@NonNull View itemView) {
            super(itemView);
            text_comment = itemView.findViewById(R.id.text_comment);
            text_date = itemView.findViewById(R.id.text_date);
            image_one = itemView.findViewById(R.id.image_one);
            image_two = itemView.findViewById(R.id.image_two);
            image_three = itemView.findViewById(R.id.image_three);
            image_four = itemView.findViewById(R.id.image_four);
            image_five = itemView.findViewById(R.id.image_five);


        }
    }
}
