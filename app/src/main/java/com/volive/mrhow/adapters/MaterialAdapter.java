package com.volive.mrhow.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.volive.mrhow.R;
import com.volive.mrhow.activities.VideoPlayerActivity;
import com.volive.mrhow.models.MaterialModels;
import com.volive.mrhow.util.MainUrl;

import java.util.ArrayList;

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.Holder> {
    ArrayList<MaterialModels> materialModels;
    Context context;
    String purchase_status;

    public MaterialAdapter(Context context, ArrayList<MaterialModels> materialModels, String purchase_status) {
        this.context = context;
        this.materialModels = materialModels;
        this.purchase_status = purchase_status;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = (LayoutInflater.from(viewGroup.getContext())).inflate(R.layout.recycler_lesson, viewGroup, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {

        holder.text_lessonname.setText(materialModels.get(position).getMaterial_name());

        if (purchase_status.equalsIgnoreCase("1")) {
            holder.image_lock.setVisibility(View.GONE);
        } else {
            holder.image_lock.setVisibility(View.VISIBLE);
        }

        final String materialtype = materialModels.get(position).getMaterial_type();

        if (materialtype.equalsIgnoreCase("video")) {
            Glide.with(context).load(MainUrl.imageurl + materialModels.get(position).getMaterial_thumb()).into(holder.image_lesson);
        } else if (materialtype.equalsIgnoreCase("image")) {
            Glide.with(context).load(MainUrl.imageurl + materialModels.get(position).getMaterial_file()).into(holder.image_lesson);
        } else if (materialtype.equalsIgnoreCase("xlsx")) {
            holder.image_lesson.setImageResource(R.drawable.wordimage);
        } else if (materialtype.equalsIgnoreCase("pdf")) {
            holder.image_lesson.setImageResource(R.drawable.pdfimage);
        } else if (materialtype.equalsIgnoreCase("doc") || materialtype.equalsIgnoreCase("docx")) {
            holder.image_lesson.setImageResource(R.drawable.wordimage);
        } else if (materialtype.equalsIgnoreCase("xlsx") || materialtype.equalsIgnoreCase("xls")) {
            holder.image_lesson.setImageResource(R.drawable.excel);
        } else if (materialtype.equalsIgnoreCase("ppt") || materialtype.equalsIgnoreCase("pptx")) {
            holder.image_lesson.setImageResource(R.drawable.ppt);
        } else {
            Glide.with(context).load(MainUrl.imageurl + materialModels.get(position).getMaterial_file()).into(holder.image_lesson);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(purchase_status.equalsIgnoreCase("1")) {
                    if (materialtype.equalsIgnoreCase ("video")) {
                        Intent intent = new Intent(context, VideoPlayerActivity.class);
                        intent.putExtra("cover", materialModels.get(position).getMaterial_file());
                        context.startActivity(intent);
                    } else {

                        String url = MainUrl.imageurl + materialModels.get(position).getMaterial_file();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        context.startActivity(i);
                    }

                }else {
                    Toast.makeText(context, context.getResources().getText(R.string.pleasepurchaseyourcourse), Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    @Override
    public int getItemCount() {
        return materialModels.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView image_lesson, image_lock;
        TextView text_lessonname;

        public Holder(@NonNull View itemView) {
            super(itemView);
            image_lesson = itemView.findViewById(R.id.image_lesson);
            image_lock = itemView.findViewById(R.id.image_lock);
            text_lessonname = itemView.findViewById(R.id.text_lessonname);

        }
    }
}

