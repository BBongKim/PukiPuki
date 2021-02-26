package com.bong.simplynews.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bong.simplynews.R;
import com.bong.simplynews.fragment.KeywordSettingFragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class ReSettingActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    ArrayList<String> previousKeywords = new ArrayList<>();
    Button button;
    KeywordSettingFragment fragment = new KeywordSettingFragment();
    boolean onlyOnce = true;

    final String TAG1 = "latestNews";
    final String TAG2 = "userNews";

    @Override
    protected void onResume() {
        super.onResume();

        if(onlyOnce) {
            getPreviousKeywords();
            onlyOnce = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_setting);

        initView();
        initFragment();
        buttonListener();
    }

    private void initView() {
        button = (Button) findViewById(R.id.CompleteButton);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    private void initFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
    }

    private void getPreviousKeywords() {
        previousKeywords.addAll(getStringArrayPref(sharedPreferences, "keywords"));

        for (String keyword : previousKeywords)
            fragment.addPrevChip(keyword);
    }

    private void buttonListener() {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSetting();
                finish();
            }
        });


    }

    private ArrayList<String> getStringArrayPref(SharedPreferences sp, String key) {
        String json = sp.getString(key, null);
        ArrayList<String> urls = new ArrayList<String>();
        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
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
        SharedPreferences.Editor editor = sharedPreferences.edit();
        setStringArrayPref(sharedPreferences, "keywords", fragment.getUserKeywords());
        editor.commit();
    }
}