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
import com.volive.mrhow.models.BannersModels;
import com.volive.mrhow.util.MainUrl;

import java.util.ArrayList;

public class ViewPagerAdapter2 extends PagerAdapter {
    ArrayList<BannersModels> bannersModels;
    Context context;


    public ViewPagerAdapter2(Context context, ArrayList<BannersModels> bannersModels) {
        this.context = context;
        this.bannersModels = bannersModels;


    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


    @Override
    public int getCount() {
        return bannersModels.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View imageLayout = null;
        imageLayout = LayoutInflater.from(context).inflate(R.layout.bannerslayout, container, false);
        assert imageLayout != null;
        ImageView image = (ImageView) imageLayout.findViewById(R.id.image);
        TextView text_catagiryname = (TextView) imageLayout.findViewById(R.id.text_catagiryname);
        TextView text_coursetitle = (TextView) imageLayout.findViewById(R.id.text_coursetitle);
        TextView text_price = (TextView) imageLayout.findViewById(R.id.text_price);
        ImageView image_one = (ImageView) imageLayout.findViewById(R.id.image_one);
        ImageView image_two = (ImageView) imageLayout.findViewById(R.id.image_two);
        ImageView image_three = (ImageView) imageLayout.findViewById(R.id.image_three);
        ImageView image_four = (ImageView) imageLayout.findViewById(R.id.image_four);
        ImageView image_five = (ImageView) imageLayout.findViewById(R.id.image_five);

        Glide.with(context).load(MainUrl.imageurl + bannersModels.get(position).getCover()).into(image);
        text_catagiryname.setText(bannersModels.get(position).getCategory_name());
        text_coursetitle.setText(bannersModels.get(position).getCourse_title());
        text_price.setText(bannersModels.get(position).getPrice() +" "+bannersModels.get(position).getCurrency());

        String rating = bannersModels.get(position).getRatings();

        if (rating.equalsIgnoreCase("0") || rating.isEmpty()) {
            image_one.setImageResource(R.drawable.star_gray);
            image_two.setImageResource(R.drawable.star_gray);
            image_three.setImageResource(R.drawable.star_gray);
            image_four.setImageResource(R.drawable.star_gray);
            image_five.setImageResource(R.drawable.star_gray);

        } else if (rating.equalsIgnoreCase("1")) {
            image_one.setImageResource(R.drawable.star);
            image_two.setImageResource(R.drawable.star_gray);
            image_three.setImageResource(R.drawable.star_gray);
            image_four.setImageResource(R.drawable.star_gray);
            image_five.setImageResource(R.drawable.star_gray);

        } else if (rating.equalsIgnoreCase("2")) {
            image_one.setImageResource(R.drawable.star);
            image_two.setImageResource(R.drawable.star);
            image_three.setImageResource(R.drawable.star_gray);
            image_four.setImageResource(R.drawable.star_gray);
            image_five.setImageResource(R.drawable.star_gray);

        } else if (rating.equalsIgnoreCase("3")) {
            image_one.setImageResource(R.drawable.star);
            image_two.setImageResource(R.drawable.star);
            image_three.setImageResource(R.drawable.star);
            image_four.setImageResource(R.drawable.star_gray);
            image_five.setImageResource(R.drawable.star_gray);

        } else if (rating.equalsIgnoreCase("4")) {
            image_one.setImageResource(R.drawable.star);
            image_two.setImageResource(R.drawable.star);
            image_three.setImageResource(R.drawable.star);
            image_four.setImageResource(R.drawable.star);
            image_five.setImageResource(R.drawable.star_gray);

        } else if (rating.equalsIgnoreCase("5")) {
            image_one.setImageResource(R.drawable.star);
            image_two.setImageResource(R.drawable.star);
            image_three.setImageResource(R.drawable.star);
            image_four.setImageResource(R.drawable.star);
            image_five.setImageResource(R.drawable.star);
        } else {
            image_one.setImageResource(R.drawable.star_gray);
            image_two.setImageResource(R.drawable.star_gray);
            image_three.setImageResource(R.drawable.star_gray);
            image_four.setImageResource(R.drawable.star_gray);
            image_five.setImageResource(R.drawable.star_gray);
        }

        container.addView(imageLayout, 0);
        return imageLayout;


    }


}
