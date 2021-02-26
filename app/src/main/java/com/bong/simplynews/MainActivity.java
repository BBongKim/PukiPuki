package com.bong.simplynews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.bong.simplynews.activity.InitialMainActivity;
import com.bong.simplynews.fragment.KeywordNewsFragment;
import com.bong.simplynews.fragment.LatestNewsFragment;
import com.bong.simplynews.fragment.SettingPreferenceFragment;
import com.bong.simplynews.fragment.UserNewsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements DialogInterface.OnCancelListener, SwipeRefreshLayout.OnRefreshListener {
    LatestNewsFragment latestNewsFragment;
    UserNewsFragment userNewsFragment;
    SettingPreferenceFragment moreFragment;
    BottomNavigationView bottomNavigationView;
    SharedPreferences sharedPreferences;

    final String TAG1 = "latestNews";
    final String TAG2 = "userNews";

    @Override
    public void onRefresh() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG1);

        if(fragment instanceof LatestNewsFragment) {
            getSupportFragmentManager().beginTransaction().detach(fragment).attach(fragment).commit();
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG1);

        if(fragment instanceof LatestNewsFragment) {
            getSupportFragmentManager().beginTransaction().detach(fragment).attach(fragment).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_layout);

        if(fragment instanceof UserNewsFragment) {
            Log.d("MainActivity", "fragment is instance of UserNewsFrag");
            getSupportFragmentManager().beginTransaction().detach(fragment).attach(fragment).commit();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setStatusBar(); // 상태바 설정

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);

        // 프래그먼트 매니저 및 패래그먼트 초기화
        FragmentManager fragmentManager = getSupportFragmentManager();
        latestNewsFragment = new LatestNewsFragment();
        fragmentManager.beginTransaction().replace(R.id.main_layout, latestNewsFragment, TAG1).commit(); //기본 프래그먼트 설정

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_layout);

                switch (item.getItemId()) {
                    case R.id.latestNewsTab: {
                        if (latestNewsFragment == null) {
                            latestNewsFragment = new LatestNewsFragment();
                            fragmentManager.beginTransaction().add(R.id.main_layout, latestNewsFragment, TAG1).commit();
                        }
                        if (latestNewsFragment != null)
                            fragmentManager.beginTransaction().show(latestNewsFragment).commit();
                        if (userNewsFragment != null)
                            fragmentManager.beginTransaction().hide(userNewsFragment).commit();
                        if (moreFragment != null)
                            fragmentManager.beginTransaction().hide(moreFragment).commit();
                        if (fragment instanceof KeywordNewsFragment)
                            fragmentManager.popBackStack();
                            fragmentManager.beginTransaction().show(latestNewsFragment).commit();
                        return true;
                    }
                    case R.id.userNewsTab: {
                        if (userNewsFragment == null) {
                            userNewsFragment = new UserNewsFragment();
                            if(!userNewsFragment.isAdded()) {
                                Log.d("fragmentManger", "usernews fragment added");
                                fragmentManager.beginTransaction().add(R.id.main_layout, userNewsFragment, TAG2).commit();
                            }
                        }
                        if (userNewsFragment != null)
                            fragmentManager.beginTransaction().show(userNewsFragment).commit();
                        if (latestNewsFragment != null)
                            fragmentManager.beginTransaction().hide(latestNewsFragment).commit();
                        if (moreFragment != null)
                            fragmentManager.beginTransaction().hide(moreFragment).commit();

                        return true;
                    }
                    case R.id.moreTab: {
                        if (moreFragment == null) {
                            moreFragment = new SettingPreferenceFragment();
                            fragmentManager.beginTransaction().add(R.id.main_layout, moreFragment).commit();
                        }
                        if (moreFragment != null)
                            fragmentManager.beginTransaction().show(moreFragment).commit();
                        if (latestNewsFragment != null)
                            fragmentManager.beginTransaction().hide(latestNewsFragment).commit();
                        if (userNewsFragment != null)
                            fragmentManager.beginTransaction().hide(userNewsFragment).commit();
                        if (fragment instanceof KeywordNewsFragment)
                            fragmentManager.popBackStack();
                            fragmentManager.beginTransaction().show(moreFragment).commit();

                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void setStatusBar() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    private void showInitialSetting() {
        Intent intent = new Intent(getApplicationContext(), InitialMainActivity.class);
        startActivity(intent);
    }

}

