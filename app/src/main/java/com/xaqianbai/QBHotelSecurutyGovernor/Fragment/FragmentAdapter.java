package com.xaqianbai.QBHotelSecurutyGovernor.Fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.xaqianbai.QBHotelSecurutyGovernor.Utils.LogUtils;

import java.util.List;

/**
 * Created by lenovo on 2017/4/20.
 */

public class FragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> mList;
    public FragmentAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        mList = list;
        LogUtils.e("FragmentAdapter++++++++++++++++++++++++"+mList.size());
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }
}
