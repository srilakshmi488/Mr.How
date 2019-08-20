package com.volive.mrhow.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.volive.mrhow.R;
import com.volive.mrhow.models.SubCommentsModels;

import java.util.ArrayList;

public class SubCommentsAdapter extends RecyclerView.Adapter<SubCommentsAdapter.Holder> {
    ArrayList<SubCommentsModels> subCommentsModels;
    Context context;
    int adapetrPosition;

    public SubCommentsAdapter(Context context, ArrayList<SubCommentsModels> subCommentsModels, int adapetrPosition) {
        this.context = context;
        this.subCommentsModels = subCommentsModels;
        this.adapetrPosition = adapetrPosition;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = (LayoutInflater.from(viewGroup.getContext())).inflate(R.layout.item_subcomment, viewGroup, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {

        holder.text_subcomment.setText(subCommentsModels.get(position).getSub_comment());

    }

    @Override
    public int getItemCount() {
        return subCommentsModels.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView text_subcomment;

        public Holder(@NonNull View itemView) {
            super(itemView);
            text_subcomment = itemView.findViewById(R.id.text_subcomment);

        }
    }
}

