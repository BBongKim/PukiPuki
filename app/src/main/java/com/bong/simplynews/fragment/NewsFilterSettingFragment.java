package com.bong.simplynews.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.bong.simplynews.NewsPress;
import com.bong.simplynews.R;
import com.bong.simplynews.adapter.NewsFilterAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class NewsFilterSettingFragment extends BottomSheetDialogFragment {

    SharedPreferences sharedPreferences;
    RecyclerView recyclerView;
    NewsFilterAdapter adapter;

    final String TAG1 = "latestNews";
    final String TAG2 = "userNews";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news_filter_setting, container, false);
        setWhiteNavigationBar(getDialog());
        initView(view);
        initAdapter();
        setRecyclerView();

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog d = super.onCreateDialog(savedInstanceState);

        //Toast.makeText(getContext(), "생성", Toast.LENGTH_SHORT).show();
        return d;
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();

        for(NewsPress item : adapter.getItems()) {
            editor.putBoolean(item.getName(), item.isSelected());
            editor.commit();
            Log.d("BottomSheet", item.getName() + Boolean.toString(item.isSelected()));
        }

        Activity parentActivity = getActivity();
        if (parentActivity instanceof DialogInterface.OnCancelListener) {
            ((DialogInterface.OnCancelListener) parentActivity).onCancel(dialog);
        }
    }

    private void initView(View view) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView = view.findViewById(R.id.newsFilterRecyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), 1)); //구분선 추가 코드
        recyclerView.setLayoutManager(layoutManager);
    }

    private void initAdapter(){
        adapter = new NewsFilterAdapter(getContext());
    }

    private void setRecyclerView() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        adapter.addItem(new NewsPress("동아일보" , R.drawable.logo_donga, sharedPreferences.getBoolean("동아일보", true)));
        adapter.addItem(new NewsPress("한겨레" , R.drawable.logo_hani, sharedPreferences.getBoolean("한겨레", true)));
        adapter.addItem(new NewsPress("경향신문" , R.drawable.logo_khan, sharedPreferences.getBoolean("경향신문", true)));
        adapter.addItem(new NewsPress("국민일보" , R.drawable.logo_kmib, sharedPreferences.getBoolean("국민일보", true)));
        adapter.addItem(new NewsPress("세계일보" , R.drawable.logo_segye, sharedPreferences.getBoolean("세계일보", true)));
        adapter.addItem(new NewsPress("서울일보" , R.drawable.icon_seoulilbo, sharedPreferences.getBoolean("서울일보", true)));
        adapter.addItem(new NewsPress("MBN" , R.drawable.logo_mbn, sharedPreferences.getBoolean("MBN", true)));
        adapter.addItem(new NewsPress("연합뉴스TV" , R.drawable.logo_yonhap, sharedPreferences.getBoolean("연합뉴스TV", true)));
        adapter.addItem(new NewsPress("뉴시스" , R.drawable.logo_newsis, sharedPreferences.getBoolean("뉴시스", true)));
        adapter.addItem(new NewsPress("매일경제" , R.drawable.logo_mk, sharedPreferences.getBoolean("매일경제", true)));
        adapter.addItem(new NewsPress("이데일리" , R.drawable.logo_edaily, sharedPreferences.getBoolean("이데일리", true)));
        adapter.addItem(new NewsPress("파이낸셜뉴스" , R.drawable.logo_financial, sharedPreferences.getBoolean("파이낸셜뉴스", true)));
        adapter.addItem(new NewsPress("한국경제" , R.drawable.logo_hankyung, sharedPreferences.getBoolean("한국경제", true)));
        adapter.addItem(new NewsPress("헤럴드경제" , R.drawable.logo_herald, sharedPreferences.getBoolean("헤럴드경제", true)));
        adapter.addItem(new NewsPress("아시아경제" , R.drawable.logo_asiae, sharedPreferences.getBoolean("아시아경제", true)));
        adapter.addItem(new NewsPress("노컷뉴스" , R.drawable.logo_nocut, sharedPreferences.getBoolean("노컷뉴스", true)));
        adapter.addItem(new NewsPress("오마이뉴스" , R.drawable.logo_ohmy, sharedPreferences.getBoolean("오마이뉴스", true)));
        adapter.addItem(new NewsPress("전자신문" , R.drawable.logo_etnews, sharedPreferences.getBoolean("전자신문", true)));
        adapter.addItem(new NewsPress("데이터넷" , R.drawable.logo_datanet, sharedPreferences.getBoolean("데이터넷", true)));
        adapter.addItem(new NewsPress("스타인뉴스" , R.drawable.favicon_starinnews, sharedPreferences.getBoolean("스타인뉴스", true)));
        adapter.addItem(new NewsPress("스포츠동아" , R.drawable.logo_sportsdonga, sharedPreferences.getBoolean("스포츠동아", true)));
        adapter.addItem(new NewsPress("스포츠경향" , R.drawable.logo_skhan, sharedPreferences.getBoolean("스포츠경향", true)));
        adapter.addItem(new NewsPress("인벤" , R.drawable.logo_inven, sharedPreferences.getBoolean("인벤", true)));

        recyclerView.setAdapter(adapter);
    }

    // Navigation 바 흰색으로 바꾸는 코드
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setWhiteNavigationBar(@NonNull Dialog dialog) {
        Window window = dialog.getWindow();
        if (window != null) {
            DisplayMetrics metrics = new DisplayMetrics();
            window.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            GradientDrawable dimDrawable = new GradientDrawable();
            // ...customize your dim effect here

            GradientDrawable navigationBarDrawable = new GradientDrawable();
            navigationBarDrawable.setShape(GradientDrawable.RECTANGLE);
            navigationBarDrawable.setColor(Color.WHITE);

            Drawable[] layers = {dimDrawable, navigationBarDrawable};

            LayerDrawable windowBackground = new LayerDrawable(layers);
            windowBackground.setLayerInsetTop(1, metrics.heightPixels);

            window.setBackgroundDrawable(windowBackground);
        }
    }
}