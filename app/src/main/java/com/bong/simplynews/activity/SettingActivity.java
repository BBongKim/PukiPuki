package com.bong.simplynews.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;

import com.bong.simplynews.MainActivity;
import com.bong.simplynews.R;
import com.bong.simplynews.adapter.SettingPagerAdapter;
import com.bong.simplynews.fragment.KeywordSettingFragment;
import com.bong.simplynews.fragment.NameSettingFragment;

import org.json.JSONArray;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;

    Toolbar toolbar;
    ProgressBar progressBar;

    Button button;

    ViewPager viewPager;
    SettingPagerAdapter pagerAdapter;

    NameSettingFragment fragment1;
    KeywordSettingFragment fragment2;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setStatusBar();
        initViewAndFragment();
        initPagerAdapter();
        setFragmentItem();
        setAdapter();
        setProgressbar();
        buttonListener(viewPager.getCurrentItem());   // 리스너 초기설정
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setStatusBar() {
        getWindow().setStatusBarColor(Color.WHITE);
    }

    private void initViewAndFragment() {
        toolbar = (Toolbar) findViewById(R.id.initialToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        viewPager = (ViewPager) findViewById(R.id.settingViewPager);
        progressBar = (ProgressBar) findViewById(R.id.settingProgressBar);
        button = (Button) findViewById(R.id.nextButton);

        fragment1 = new NameSettingFragment();
        fragment2 = new KeywordSettingFragment();

    }

    private void initPagerAdapter() {
        pagerAdapter = new SettingPagerAdapter(getSupportFragmentManager());
    }

    private void setFragmentItem() {
        pagerAdapter.addItem(fragment1);
        pagerAdapter.addItem(fragment2);
    }

    private void setAdapter() {
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void onBackPressed() {
        int position = viewPager.getCurrentItem();
        if (position == 0) {
            super.onBackPressed();

        } else {
            viewPager.setCurrentItem(position - 1);
        }
    }

    private void setProgressbar() {

        setProgressMax(progressBar, 100);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                buttonListener(position);  // 페이지에 따른 버튼 리스너 동작 재설정
                switch (position) {
                    case 0:
                        setProgressAnimate(progressBar, 50);
                        button.setText("다음");
                        break;
                    case 1:
                        setProgressAnimate(progressBar, 100);
                        button.setText("나가기");
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setProgressMax(ProgressBar pb, int max) {
        pb.setMax(max * 100);
        pb.setProgress(max * 50);
    }

    private void setProgressAnimate(ProgressBar pb, int progressTo) {
        ObjectAnimator animation = ObjectAnimator.ofInt(pb, "progress", pb.getProgress(), progressTo * 100);
        animation.setDuration(500);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }

    private void buttonListener(int position) {
        switch (position) {
            case 0: {
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(position + 1);
                    }
                });
                break;
            }

            case 1: {
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("Data : ", fragment1.getUserName());

                        for (String str : fragment2.getUserKeywords()) {
                            Log.d("Data : ", str);
                        }

                        saveSetting();
                        finish();

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                    }
                });
                break;
            }
        }
    }

    // ArrayList to Json
    private void setStringArrayPref(SharedPreferences sp, String key, ArrayList<String> values) {
        SharedPreferences.Editor editor = sp.edit();
        JSONArray a = new JSONArray();
        for (int i = 0; i < values.size(); i++) {
            a.put(values.get(i));
        }
        if (!values.isEmpty()) {
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, null);
        }
        editor.commit();
    }

    private void saveSetting() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("initialSetting", true);
        editor.putString("userName", fragment1.getUserName());
        setStringArrayPref(sharedPreferences, "keywords", fragment2.getUserKeywords());
        editor.commit();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                if (viewPager.getCurrentItem() == 0) {
                    finish();
                } else {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}