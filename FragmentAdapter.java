package com.example.ccy.assistant;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by ccy on 2017/5/13.
 */

public class FragmentAdapter extends FragmentPagerAdapter {
    List<Fragment> fragments;
    List<String> titles;
    public FragmentAdapter(FragmentManager fm, List<Fragment> fragmentList ,List<String> titles  )
    {
        super(fm);
        fragments = fragmentList;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
