package com.volive.mrhow.activities;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.volive.mrhow.R;
import com.volive.mrhow.fragments.DiscoverFragment;
import com.volive.mrhow.fragments.MyCourseFragment;
import com.volive.mrhow.fragments.ProfileFragment;
import com.volive.mrhow.fragments.StudioFragment;
import com.volive.mrhow.util.MainUrl;
import com.volive.mrhow.util.PreferenceUtils;

public class HomeNavigation extends AppCompatActivity {
    public static FragmentManager mFragmentManager;
    public static FragmentTransaction mFragmentTransaction;
    public static MenuItem item;
    Fragment fragment;
    PreferenceUtils preferenceUtils;
    String userimage = "", image = "";
    private BottomNavigationView bottom_navigation;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_navigation);

        preferenceUtils = new PreferenceUtils(HomeNavigation.this);
        userimage = preferenceUtils.getStringFromPreference(PreferenceUtils.userimage, "");
        image = MainUrl.imageurl + userimage;


        fragment = new DiscoverFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.containerView, fragment);
        fragmentTransaction.commit();

        setupBottomNavigation();

    }

    private void setupBottomNavigation() {
        bottom_navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottom_navigation.setItemIconTintList(null);

        Menu menu = bottom_navigation.getMenu();
        item = menu.findItem(R.id.profile);
        if (userimage != null && !userimage.isEmpty()) {
            Glide.with(HomeNavigation.this).load(image).apply(RequestOptions.circleCropTransform()).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    item.setIcon(resource);
                }
            });
        } else {
            item.setIcon(R.drawable.profile_image_selector);
        }

        BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) bottom_navigation.getChildAt(0);
        View v2 = bottomNavigationMenuView.getChildAt(2);
        View v = bottomNavigationMenuView.getChildAt(0);
        View v3 = bottomNavigationMenuView.getChildAt(3);
        BottomNavigationItemView itemView2 = (BottomNavigationItemView) v2;
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;
        BottomNavigationItemView itemView3 = (BottomNavigationItemView) v3;
        View activeLabel2 = itemView2.findViewById(R.id.largeLabel);
        View activeLabel = itemView.findViewById(R.id.largeLabel);
        View activeLabel3 = itemView3.findViewById(R.id.largeLabel);

        if (activeLabel2 != null && activeLabel2 instanceof TextView) {
            ((TextView) activeLabel2).setPadding(0, 0, 0, 0);
        }
        if (activeLabel != null && activeLabel instanceof TextView) {
            ((TextView) activeLabel).setPadding(0, 0, 0, 0);
        }
        if (activeLabel3 != null && activeLabel3 instanceof TextView) {
            ((TextView) activeLabel3).setPadding(0, 0, 0, 0);
        }

        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.discover:
                        mFragmentManager = getSupportFragmentManager();
                        mFragmentTransaction = mFragmentManager.beginTransaction();
                        mFragmentTransaction.replace(R.id.containerView, new DiscoverFragment()).commit();

                        return true;

                    case R.id.studio:
                        mFragmentManager = getSupportFragmentManager();
                        mFragmentTransaction = mFragmentManager.beginTransaction();
                        mFragmentTransaction.replace(R.id.containerView, new StudioFragment()).commit();

                        return true;
                    case R.id.my_course:
                        mFragmentManager = getSupportFragmentManager();
                        mFragmentTransaction = mFragmentManager.beginTransaction();
                        mFragmentTransaction.replace(R.id.containerView, new MyCourseFragment()).commit();
                        return true;
                    case R.id.profile:
                        mFragmentManager = getSupportFragmentManager();
                        mFragmentTransaction = mFragmentManager.beginTransaction();
                        mFragmentTransaction.replace(R.id.containerView, new ProfileFragment()).commit();
                        return true;
                }
                return false;
            }
        });
        bottom_navigation.setSelectedItemId(R.id.discover);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        openAlertDialog();
    }

    private void openAlertDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(HomeNavigation.this);
        builder.setTitle(getResources().getText(R.string.alert));
        builder.setMessage(getResources().getText(R.string.areyousureexit));
        builder.setPositiveButton(getResources().getText(R.string.yes), new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
//                preferenceUtils.logOut();
//                Intent intent = new Intent(HomeNavigation.this, LogInActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
            finishAffinity();


        }
    });
        builder.setNegativeButton(getResources().getText(R.string.no), new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    });
        builder.show();

}

}
