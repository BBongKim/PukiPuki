package com.bong.simplynews.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.bong.simplynews.MainActivity;
import com.bong.simplynews.R;
import com.bong.simplynews.activity.OpenSourceActivity;

public class SettingPreferenceFragment extends PreferenceFragmentCompat {

    SharedPreferences sharedPreferences;
    EditTextPreference editTextPreference;

    final String TAG1 = "latestNews";
    final String TAG2 = "userNews";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.setting_preference, null);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        editTextPreference = (EditTextPreference) findPreference("userName");

        sharedPreferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

                if(key.equals("userName")) {
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    Log.d("setting : ", "이름값 변경!");
                }
            }
        });
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        String key = preference.getKey();

        if(key.equals("userAlarm")) {
            Intent intent = new Intent();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, getContext().getPackageName());
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                intent.putExtra("app_package", getContext().getPackageName());
                intent.putExtra("app_uid", getContext().getApplicationInfo().uid);
            } else {
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + getContext().getPackageName()));
            }
            getContext().startActivity(intent);
        }
        else if(key.equals("FAQ")) {
            String feedBackUrl = "https://blog.naver.com/kbg0099/222252545965";
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setShowTitle(true);
            builder.setShareState(CustomTabsIntent.SHARE_STATE_ON);
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(getContext(), Uri.parse(feedBackUrl));
        }
        else if(key.equals("newsSource")) {
            String feedBackUrl = "https://blog.naver.com/kbg0099/222252578070";
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setShowTitle(true);
            builder.setShareState(CustomTabsIntent.SHARE_STATE_ON);
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(getContext(), Uri.parse(feedBackUrl));
        }
        else if(key.equals("feedBack")) {
            String feedBackUrl = "https://forms.gle/5QzpKQqaamaaXrys9";
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setShowTitle(true);
            builder.setShareState(CustomTabsIntent.SHARE_STATE_ON);
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(getContext(), Uri.parse(feedBackUrl));
        }
        else if(key.equals("openSource")) {
            Intent intent = new Intent(getContext(), OpenSourceActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }

        return super.onPreferenceTreeClick(preference);
    }
}
