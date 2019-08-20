package com.volive.mrhow.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.volive.mrhow.R;
import com.volive.mrhow.models.NotificationModels;
import com.volive.mrhow.util.MainUrl;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.Holder> {
    ArrayList<NotificationModels> notificationModels;
    Context context;

    public NotificationsAdapter(Context context, ArrayList<NotificationModels> notificationModels) {
        this.context = context;
        this.notificationModels = notificationModels;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = (LayoutInflater.from(viewGroup.getContext())).inflate(R.layout.recycler_notifications, viewGroup, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {

        Glide.with(context).load(MainUrl.imageurl + notificationModels.get(position).getProfile_pic()).into(holder.image_notification);
        holder.text_title.setText(notificationModels.get(position).getTitle());
        holder.txt_msg.setText(notificationModels.get(position).getText());
        holder.txt_time.setText(notificationModels.get(position).getTime());

    }

    @Override
    public int getItemCount() {
        return notificationModels.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        CircleImageView image_notification;
        TextView text_title, txt_msg, txt_time;

        public Holder(@NonNull View itemView) {
            super(itemView);
            image_notification = itemView.findViewById(R.id.image_notification);
            text_title = itemView.findViewById(R.id.text_title);
            txt_msg = itemView.findViewById(R.id.txt_msg);
            txt_time = itemView.findViewById(R.id.txt_time);

        }
    }
}
