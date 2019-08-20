package com.volive.mrhow.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.volive.mrhow.R;
import com.volive.mrhow.fragments.StudioFragment;
import com.volive.mrhow.models.TagsModels;

import java.util.ArrayList;

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.Holder> {
    ArrayList<TagsModels> tagsModels;
    Context context;
    StudioFragment studioFragment;
    int row_index;
    boolean flagvalue;


    public TagsAdapter(Context context, ArrayList<TagsModels> tagsModels, StudioFragment studioFragment) {
        this.context = context;
        this.tagsModels = tagsModels;
        this.studioFragment = studioFragment;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = (LayoutInflater.from(viewGroup.getContext())).inflate(R.layout.tagitem_layout, viewGroup, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final Holder holder, final int position) {

        holder.text_hashitem.setText(tagsModels.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index=position;
                notifyDataSetChanged();
                String hashtag = tagsModels.get(position).getHashtag_id();
                studioFragment.getArticlesList(hashtag);

            }
        });

        if (row_index == position) {
            holder.text_hashitem.setBackgroundResource(R.drawable.hashtagbackground);
            holder.text_hashitem.setTextColor(R.color.white);

        } else {
            holder.text_hashitem.setBackgroundResource(R.drawable.hashbackground);
            holder.text_hashitem.setTextColor(R.color.blackcolour);

        }


    }

    @Override
    public int getItemCount() {
        return tagsModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView text_hashitem;

        public Holder(@NonNull View itemView) {
            super(itemView);
            text_hashitem = itemView.findViewById(R.id.text_hashitem);


        }
    }
}

