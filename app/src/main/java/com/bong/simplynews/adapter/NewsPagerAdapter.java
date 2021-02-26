package com.bong.simplynews.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class NewsPagerAdapter extends FragmentStatePagerAdapter {
    ArrayList<Fragment> items = new ArrayList<>();


    public NewsPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    public NewsPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public void addItem(Fragment item) {
        items.add(item);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0 :
                return "동아일보";
            case 1 :
                return "한겨레";
            case 2 :
                return "경향신문";
            case 3 :
                return "국민일보";
            case 4 :
                return "세계일보";
            case 5 :
                return "서울일보";
            case 6 :
                return "MBN";
            case 7 :
                return "연합뉴스TV";
            case 8 :
                return "뉴시스";
            case 9 :
                return "매일경제";
            case 10 :
                return "이데일리";
            case 11 :
                return "파이낸셜뉴스";
            case 12 :
                return "한국경제";
            case 13 :
                return "헤럴드경제";
            case 14 :
                return "아시아경제";
            case 15 :
                return "노컷뉴스";
            case 16 :
                return "오마이뉴스";
            case 17 :
                return "전자신문";
            case 18 :
                return "데이터넷";
            case 19 :
                return "스타인뉴스";
            case 20 :
                return "스포츠동아";
            case 21 :
                return "스포츠경향";
            case 22 :
                return "인벤";
        }
        return null;
    }
}
