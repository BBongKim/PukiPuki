package com.bong.simplynews.fragment;


import android.os.Bundle;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bong.simplynews.R;
import com.bong.simplynews.adapter.NewsPagerAdapter;
import com.bong.simplynews.keywordNewsFragment.KeywordNewsEachFragment;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;


public class KeywordNewsFragment extends Fragment {
    ViewPager viewPager;
    NewsPagerAdapter adapter;
    TextView keywordTitle;

    KeywordNewsEachFragment donga;
    KeywordNewsEachFragment hani;
    KeywordNewsEachFragment khan;
    KeywordNewsEachFragment kmib;
    KeywordNewsEachFragment segye;
    KeywordNewsEachFragment seoulilbo;
    KeywordNewsEachFragment mbn;
    KeywordNewsEachFragment yonhapnewstv;
    KeywordNewsEachFragment newsis;
    KeywordNewsEachFragment mk;
    KeywordNewsEachFragment edaily;
    KeywordNewsEachFragment fnnews;
    KeywordNewsEachFragment hankyung;
    KeywordNewsEachFragment heraldcorp;
    KeywordNewsEachFragment asiae;
    KeywordNewsEachFragment nocutnews;
    KeywordNewsEachFragment ohmynews;
    KeywordNewsEachFragment etnews;
    KeywordNewsEachFragment datanet;
    KeywordNewsEachFragment starinnews;
    KeywordNewsEachFragment sdonga;
    KeywordNewsEachFragment skhan;
    KeywordNewsEachFragment inven;


    String keyword;

    public KeywordNewsFragment(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_keyword_news, container, false);

        initView(view);
        setTitle();
        initFragment(keyword);
        setAdapter();

        return view;
    }

    private void setTitle() {
        keywordTitle.setText(keyword);
    }

    public void initView(View view) {
        keywordTitle = (TextView) view.findViewById(R.id.keywordTitle);
        viewPager = (ViewPager) view.findViewById(R.id.newsViewPager);
        adapter = new NewsPagerAdapter(getChildFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    public void initFragment(String keyword) {
        donga = new KeywordNewsEachFragment(keyword, "DONGA");
        hani = new KeywordNewsEachFragment(keyword, "HANI");
        khan = new KeywordNewsEachFragment(keyword, "KHAN");
        kmib = new KeywordNewsEachFragment(keyword, "KMIB");
        segye = new KeywordNewsEachFragment(keyword, "SEGYE");
        seoulilbo = new KeywordNewsEachFragment(keyword, "SEOUL");
        mbn = new KeywordNewsEachFragment(keyword, "MBN");
        yonhapnewstv = new KeywordNewsEachFragment(keyword, "YONHAP");
        newsis = new KeywordNewsEachFragment(keyword, "NEWSIS");
        mk = new KeywordNewsEachFragment(keyword, "MK");
        edaily = new KeywordNewsEachFragment(keyword, "EDAILY");
        fnnews = new KeywordNewsEachFragment(keyword, "FNNEWS");
        hankyung = new KeywordNewsEachFragment(keyword, "HANKYOUNG");
        heraldcorp = new KeywordNewsEachFragment(keyword, "HERALD");
        asiae = new KeywordNewsEachFragment(keyword, "ASIAE");
        nocutnews = new KeywordNewsEachFragment(keyword, "NOCUT");
        ohmynews = new KeywordNewsEachFragment(keyword, "OHMY");
        etnews = new KeywordNewsEachFragment(keyword, "ETNEWS");
        datanet = new KeywordNewsEachFragment(keyword, "DATANET");
        starinnews = new KeywordNewsEachFragment(keyword, "STARIN");
        sdonga = new KeywordNewsEachFragment(keyword, "SPORTS_DONGA");
        skhan = new KeywordNewsEachFragment(keyword, "SPORTS_KHAN");
        inven = new KeywordNewsEachFragment(keyword, "INVEN");
    }

    public void setAdapter() {
        adapter.addItem(donga);
        adapter.addItem(hani);
        adapter.addItem(khan);
        adapter.addItem(kmib);
        adapter.addItem(segye);
        adapter.addItem(seoulilbo);
        adapter.addItem(mbn);
        adapter.addItem(yonhapnewstv);
        adapter.addItem(newsis);
        adapter.addItem(mk);
        adapter.addItem(edaily);
        adapter.addItem(fnnews);
        adapter.addItem(hankyung);
        adapter.addItem(heraldcorp);
        adapter.addItem(asiae);
        adapter.addItem(nocutnews);
        adapter.addItem(ohmynews);
        adapter.addItem(etnews);
        adapter.addItem(datanet);
        adapter.addItem(starinnews);
        adapter.addItem(sdonga);
        adapter.addItem(skhan);
        adapter.addItem(inven);

        viewPager.setAdapter(adapter);
    }


}