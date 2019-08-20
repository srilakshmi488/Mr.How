package com.volive.mrhow.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.volive.mrhow.R;


public class QualityAdapter extends RecyclerView.Adapter<QualityAdapter.Holder> {
    String []qualities;
    Context context;
    private int checkedPosition = -1;


    public QualityAdapter(Context context,  String []qualities) {
        this.context = context;
        this.qualities = qualities;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = (LayoutInflater.from(viewGroup.getContext())).inflate(R.layout.item_videoquality, viewGroup, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, final int position) {
        holder.text_quality.setText(qualities[position]);
        if (checkedPosition == -1) {
            holder.image_tick.setVisibility(View.GONE);
        } else {
            if (checkedPosition == position) {
                holder.image_tick.setVisibility(View.VISIBLE);
            } else {
                holder.image_tick.setVisibility(View.GONE);
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.image_tick.setVisibility(View.VISIBLE);
                String strqualityname = qualities[position];
                Log.e("jdfsgdg ",strqualityname);
                if (checkedPosition != position) {
                    notifyItemChanged(checkedPosition);
                    checkedPosition = position;
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return qualities.length;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView image_tick;
        TextView text_quality;

        public Holder(@NonNull View itemView) {
            super(itemView);
            image_tick = itemView.findViewById(R.id.image_tick);
            text_quality = itemView.findViewById(R.id.text_quality);


        }
    }
}

