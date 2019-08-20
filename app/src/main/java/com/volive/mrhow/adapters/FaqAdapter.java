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
import com.volive.mrhow.models.FaqModels;

import java.util.ArrayList;

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.Holder> {
    ArrayList<FaqModels> faqModels;
    Context context;
    int selectedposition;

    public FaqAdapter(Context context, ArrayList<FaqModels> faqModels) {
        this.context = context;
        this.faqModels = faqModels;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = (LayoutInflater.from(viewGroup.getContext())).inflate(R.layout.faq_layout, viewGroup, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, final int position) {

        selectedposition = position;

        holder.text_title.setText(faqModels.get(position).getTitle());
        holder.text_text.setText(faqModels.get(position).getText());


        holder.image_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.image_plus.setVisibility(View.GONE);
                holder.image_minus.setVisibility(View.VISIBLE);
                holder.text_text.setVisibility(View.VISIBLE);
            }
        });

        holder.image_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.image_minus.setVisibility(View.GONE);
                holder.image_plus.setVisibility(View.VISIBLE);
                holder.text_text.setVisibility(View.GONE);
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return faqModels.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView image_plus, image_minus;
        TextView text_title, text_text;

        public Holder(@NonNull View itemView) {
            super(itemView);
            image_plus = itemView.findViewById(R.id.image_plus);
            image_minus = itemView.findViewById(R.id.image_minus);
            text_title = itemView.findViewById(R.id.text_title);
            text_text = itemView.findViewById(R.id.text_text);

        }
    }
}

