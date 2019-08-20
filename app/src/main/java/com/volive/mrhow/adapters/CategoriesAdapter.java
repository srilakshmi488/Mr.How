package com.volive.mrhow.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.volive.mrhow.R;
import com.volive.mrhow.activities.CategiriesActivity;
import com.volive.mrhow.models.CategoriesModels;
import com.volive.mrhow.util.MainUrl;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.Holder> {
    ArrayList<CategoriesModels> categoriesModels;
    Context context;

    public CategoriesAdapter(Context context, ArrayList<CategoriesModels> categoriesModels) {
        this.context = context;
        this.categoriesModels = categoriesModels;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = (LayoutInflater.from(viewGroup.getContext())).inflate(R.layout.recycler_category_home, viewGroup, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {

        Glide.with(context).load(MainUrl.imageurl + categoriesModels.get(position).getIcon()).into(holder.image_circle);
        holder.text_title.setText(categoriesModels.get(position).getCategory_name());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CategiriesActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("catid", categoriesModels.get(position).getCategory_id());
                intent.putExtra("catname", categoriesModels.get(position).getCategory_name());
                intent.putExtra("Action", "categiries");
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return categoriesModels.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        CircleImageView image_circle;
        TextView text_title;

        public Holder(@NonNull View itemView) {
            super(itemView);
            image_circle = itemView.findViewById(R.id.image_circle);
            text_title = itemView.findViewById(R.id.text_title);

        }
    }
}
