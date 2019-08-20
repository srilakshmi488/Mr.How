package com.volive.mrhow.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.volive.mrhow.R;
import com.volive.mrhow.models.SlidingImageModels;
import com.volive.mrhow.util.MainUrl;

import java.util.ArrayList;


public class ViewPagerAdapter extends PagerAdapter {
    ArrayList<SlidingImageModels> slidingImageModels;
    Context context;


    public ViewPagerAdapter(Context context, ArrayList<SlidingImageModels> slidingImageModels) {
        this.context = context;
        this.slidingImageModels = slidingImageModels;


    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


    @Override
    public int getCount() {
        return slidingImageModels.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View imageLayout = null;
        imageLayout = LayoutInflater.from(context).inflate(R.layout.slidinglayout, container, false);
        assert imageLayout != null;
        ImageView image = (ImageView) imageLayout.findViewById(R.id.image);
        TextView text_title = (TextView) imageLayout.findViewById(R.id.text_title);
        TextView text_mesaage = (TextView) imageLayout.findViewById(R.id.text_mesaage);


        Glide.with(context).load(MainUrl.imageurl + slidingImageModels.get(position).getImage()).into(image);
        text_title.setText(slidingImageModels.get(position).getTitle());
        text_mesaage.setText(slidingImageModels.get(position).getText());

        container.addView(imageLayout, 0);
        return imageLayout;


    }


}
