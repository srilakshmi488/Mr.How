package com.volive.mrhow.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.volive.mrhow.fragments.AboutFragment;
import com.volive.mrhow.fragments.LessionsFragment;
import com.volive.mrhow.fragments.MoreFragment;


public class PageAdapter extends FragmentStatePagerAdapter {
    int mNoOfTabs;

    public PageAdapter(FragmentManager fm, int NumberOfTabs) {
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                LessionsFragment fragment = new LessionsFragment();
                return fragment;
            case 1:
                AboutFragment aboutFragment = new AboutFragment();
                return aboutFragment;
            case 2:
                MoreFragment moreFragment = new MoreFragment();
                return moreFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}
