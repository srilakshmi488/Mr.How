package com.volive.mrhow.adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.volive.mrhow.R;
import com.volive.mrhow.models.StudentProjectModels;
import com.volive.mrhow.util.MainUrl;

import java.util.ArrayList;

public class StudentProjectsAdapter extends RecyclerView.Adapter<StudentProjectsAdapter.Holder> {
    ArrayList<StudentProjectModels> studentProject;
    Context context;

    public StudentProjectsAdapter(Context context, ArrayList<StudentProjectModels> studentProject) {
        this.context = context;
        this.studentProject = studentProject;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = (LayoutInflater.from(viewGroup.getContext())).inflate(R.layout.recycler_student_projects, viewGroup, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {

        String bannertype = studentProject.get(position).getBanner_type();
        if(bannertype.equalsIgnoreCase("video")){
            Glide.with(context).load(MainUrl.imageurl + studentProject.get(position).getProject_thumbnail()).into(holder.image_project);
        }else {
            Glide.with(context).load(MainUrl.imageurl + studentProject.get(position).getProject_banner()).into(holder.image_project);
        }



    }

    @Override
    public int getItemCount() {
        return studentProject.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView image_project;


        public Holder(@NonNull View itemView) {
            super(itemView);
            image_project = itemView.findViewById(R.id.image_project);


        }
    }
}
