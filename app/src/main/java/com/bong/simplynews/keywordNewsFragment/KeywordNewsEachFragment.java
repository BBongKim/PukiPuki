package com.bong.simplynews.keywordNewsFragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bong.simplynews.NewsItem;
import com.bong.simplynews.R;
import com.bong.simplynews.adapter.ReverseRecyclerAdapter;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Collections;

public class KeywordNewsEachFragment extends Fragment {
    ShimmerFrameLayout shimmerFrameLayout;
    BottomNavigationView bottomNavigationView;

    SwipeRefreshLayout swipeRefreshLayout;

    RecyclerView recyclerView;
    ReverseRecyclerAdapter adapter;
    String keyword;
    String siteName;

    ArrayList<NewsItem> temp = new ArrayList<>();

    public KeywordNewsEachFragment(String keyword, String siteName) {
        this.keyword = keyword;
        this.siteName = siteName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_keyword_each, container, false);

        Log.d("Donga", "onCreateView");

        initView(view);
        initAdapter();
        initAdapterClickListener();
        initSwipeRefreshRListener();

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        setNewsData();

    }

    private void initView(View view) {
        shimmerFrameLayout = (ShimmerFrameLayout) view.findViewById(R.id.shimmer_view_container_each);
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();

        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottomNavigation);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshKeyword_each);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.mainGreen));

        recyclerView = (RecyclerView) view.findViewById(R.id.keywordRecyclerView_each);
        recyclerView.setAlpha(0f);
        //recyclerView.addItemDecoration(new NewsItemDecoration(getContext())); // 마진 설정을 위해 decoration 객체 설정
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        //layoutManager.setReverseLayout(true);
        //layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

    }

    private void initAdapter() {
        adapter = new ReverseRecyclerAdapter(getContext());
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        recyclerView.setAdapter(adapter);
    }

    private void initAdapterClickListener() {
        // 어댑터 아이템 클릭 리스너
        adapter.setOnItemClickListener(new ReverseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, View view, int position) {

                int id = view.getId();

                NewsItem newsItem = adapter.getItem(adapter.getItemCount() - position - 1);
                String url = newsItem.getLink();

                if (id == R.id.shareButton) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT, url);
                    intent.setType("text/plain");

                    Intent shareIntent = Intent.createChooser(intent, null);
                    shareIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                    startActivity(shareIntent);

                } else if (id == R.id.shareButton_small) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT, url);
                    intent.setType("text/plain");

                    Intent shareIntent = Intent.createChooser(intent, null);
                    shareIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                    startActivity(shareIntent);
                } else {
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    builder.setShowTitle(true);
                    builder.setShareState(CustomTabsIntent.SHARE_STATE_ON);
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.launchUrl(getContext(), Uri.parse(url));

                    //Intent intent = new Intent(getContext(), WebActivity.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    //intent.putExtra("URL", newsItem.getLink());
                    //intent.putExtra("title", newsItem.getTitle());

                    //startActivity(intent);
                }

            }
        });
    }

    private void initSwipeRefreshRListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                shimmerFrameLayout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                recyclerView.setAlpha(0f);
                shimmerFrameLayout.startShimmer();
                adapter.clearItems();
                setNewsData();

                // 새로고침 아이콘 flickering 현상 때문에 post Delay 줌
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 100);
            }
        });
    }

    private void setNewsData() {

        temp.clear();

        Query query;

        query = FirebaseDatabase.getInstance().getReference().child("종합뉴스").child(siteName);

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                NewsItem item = snapshot.getValue(NewsItem.class);

                String title = item.getTitle();
                String des = item.getDescription();

                if (title.contains(keyword) || des.contains(keyword)) {
                    //Log.d("아이템 제목: ", title);
                    //Log.d("아이템 요약: ", des);
                    //Log.d("아이템 시간: ", item.getTime());

                    temp.add(item);
                }


                Collections.sort(temp);
                adapter.addItems(temp);
                adapter.notifyDataSetChanged();

                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.animate().alpha(1f)
                        .setDuration(400)
                        .setListener(null);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}