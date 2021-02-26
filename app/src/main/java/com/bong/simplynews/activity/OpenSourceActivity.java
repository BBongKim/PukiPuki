package com.bong.simplynews.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.util.Linkify;
import android.view.MenuItem;
import android.widget.TextView;

import com.bong.simplynews.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OpenSourceActivity extends AppCompatActivity {
    Toolbar toolbar;

    TextView open_algolia;
    TextView open_firebase;
    TextView open_flexbox;
    TextView open_glide;
    TextView open_gmarket;
    TextView open_inko;
    TextView open_lottie;
    TextView open_dotindi;
    TextView open_roundedImage;
    TextView open_shimmer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_source);

        initView();

        Pattern pattern = Pattern.compile("Algolia");
        Pattern pattern2 = Pattern.compile("Firebase");
        Pattern pattern3 = Pattern.compile("Flexbox Layout");
        Pattern pattern4 = Pattern.compile("Glide");
        Pattern pattern5 = Pattern.compile("Inko");
        Pattern pattern6 = Pattern.compile("Lottie");
        Pattern pattern7 = Pattern.compile("Material View Pager Dots Indicator");
        Pattern pattern8 = Pattern.compile("RoundedImageView");
        Pattern pattern9 = Pattern.compile("Shimmer");
        Pattern pattern10 = Pattern.compile("Gmarket sans ttf");

        Linkify.TransformFilter transformFilter = new Linkify.TransformFilter() {
            @Override
            public String transformUrl(Matcher match, String url) {
                return "";
            }
        };

        Linkify.addLinks(open_algolia, pattern, "https://www.algolia.com/", null, transformFilter);
        Linkify.addLinks(open_firebase, pattern2, "https://firebase.google.com/?hl=ko", null, transformFilter);
        Linkify.addLinks(open_flexbox, pattern3, "https://github.com/google/flexbox-layout", null, transformFilter);
        Linkify.addLinks(open_glide, pattern4, "https://bumptech.github.io/glide/", null, transformFilter);
        Linkify.addLinks(open_inko, pattern5, "https://inko.js.org/", null, transformFilter);
        Linkify.addLinks(open_lottie, pattern6, "https://airbnb.design/lottie/", null, transformFilter);
        Linkify.addLinks(open_dotindi, pattern7, "https://github.com/tommybuonomo/dotsindicator", null, transformFilter);
        Linkify.addLinks(open_roundedImage, pattern8, "https://github.com/vinc3m1/RoundedImageView", null, transformFilter);
        Linkify.addLinks(open_shimmer, pattern9, "https://facebook.github.io/shimmer-android/", null, transformFilter);
        Linkify.addLinks(open_gmarket, pattern10, "http://company.gmarket.co.kr/company/about/company/company--font.asp", null, transformFilter);
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.openToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("오픈소스 라이선스");

        open_algolia = (TextView) findViewById(R.id.open_algolia);
        open_firebase = (TextView) findViewById(R.id.open_firebase);
        open_flexbox = (TextView) findViewById(R.id.open_flexbox);
        open_glide = (TextView) findViewById(R.id.open_glide);
        open_gmarket = (TextView) findViewById(R.id.open_gmarket);
        open_inko = (TextView) findViewById(R.id.open_inko);
        open_lottie = (TextView) findViewById(R.id.open_lottie);
        open_dotindi = (TextView) findViewById(R.id.open_dotindi);
        open_roundedImage = (TextView) findViewById(R.id.open_roundedImage);
        open_shimmer = (TextView) findViewById(R.id.open_shimmer);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}