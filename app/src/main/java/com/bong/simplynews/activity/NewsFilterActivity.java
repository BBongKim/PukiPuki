package com.bong.simplynews.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bong.simplynews.R;
import com.bong.simplynews.fragment.NewsFilterSettingFragment;

public class NewsFilterActivity extends AppCompatActivity {

    NewsFilterSettingFragment fragment = new NewsFilterSettingFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_filter);

        fragment.show(getSupportFragmentManager(),"NewsFilter");
    }
}