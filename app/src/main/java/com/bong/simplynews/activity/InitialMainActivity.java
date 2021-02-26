package com.bong.simplynews.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.Button;

import com.bong.simplynews.MainActivity;
import com.bong.simplynews.R;
import com.bong.simplynews.adapter.SettingPagerAdapter;
import com.bong.simplynews.fragment.InitialFragment1;
import com.bong.simplynews.fragment.InitialFragment2;
import com.bong.simplynews.fragment.InitialFragment3;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

public class InitialMainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;

    ViewPager viewPager;
    WormDotsIndicator wormDotsIndicator;
    InitialFragment1 fragment1;
    InitialFragment2 fragment2;
    InitialFragment3 fragment3;

    Button startButton;

    SettingPagerAdapter pagerAdapter;

    String uid;

    final static String channelId = "channelId";
    final static String channelName = "keywordAlarm";
    final static String GROUP_KEY = "notificationGroup";


    // 앱 처음 실행시 1회만 초기설정 화면 보여줌
    @Override
    protected void onResume() {
        super.onResume();

        NotificationManager notiManger = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // 오레오 버전 이후에는 채널이 필요하다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notiManger.createNotificationChannel(channel);
        }

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null) {
                uid = user.getUid();
        } else {
            mAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    uid = mAuth.getCurrentUser().getUid();
                }
            });
        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        if (sharedPreferences.getBoolean("initialSetting", false)) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();

        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_main);

        setStatusBar();
        initViewAndFragment();
        initPagerAdapter();
        setFragmentItem();
        setAdapter();
        buttonListener();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setStatusBar() {
        getWindow().setStatusBarColor(Color.WHITE);
    }

    private void initViewAndFragment() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        wormDotsIndicator = (WormDotsIndicator) findViewById(R.id.worm_dots_indicator);
        startButton = (Button) findViewById(R.id.startButton);

        fragment1 = new InitialFragment1();
        fragment2 = new InitialFragment2();
        fragment3 = new InitialFragment3();
    }

    private void initPagerAdapter() {
        pagerAdapter = new SettingPagerAdapter(getSupportFragmentManager());
    }

    private void setFragmentItem() {
        pagerAdapter.addItem(fragment1);
        pagerAdapter.addItem(fragment2);
        pagerAdapter.addItem(fragment3);
    }

    private void setAdapter() {
        viewPager.setAdapter(pagerAdapter);
        wormDotsIndicator.setViewPager(viewPager);
    }

    private void buttonListener() {
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
            }
        });
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
}