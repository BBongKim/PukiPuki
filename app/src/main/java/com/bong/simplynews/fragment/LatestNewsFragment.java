package com.bong.simplynews.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bong.simplynews.FetchNewsData;
import com.bong.simplynews.LoadFinished;
import com.bong.simplynews.LoadedMoreFinished;
import com.bong.simplynews.NetworkStatus;
import com.bong.simplynews.NewsItem;
import com.bong.simplynews.NewsItemDecoration;
import com.bong.simplynews.R;
import com.bong.simplynews.SetAdapterListener;
import com.bong.simplynews.SetAllLoadedFinished;
import com.bong.simplynews.adapter.RecyclerAdapter;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class LatestNewsFragment extends Fragment{
    NewsFilterSettingFragment fragment = new NewsFilterSettingFragment();

    ShimmerFrameLayout shimmerFrameLayout;

    LottieAnimationView loadingView;

    TextView userName;
    RecyclerView recyclerView;
    RecyclerAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;

    Button filterButton;

    ArrayList<NewsItem> newsItems = new ArrayList<NewsItem>();
    ArrayList<NewsItem> previousList = new ArrayList<>();
    ArrayList<NewsItem> newsTemp = new ArrayList<>();

    boolean isLoading = false;
    SharedPreferences sharedPreferences;

    ProgressBar progressBar;

    NetworkStatus networkStatus;

    FetchNewsData fetchNewsData;

    //파이어 베이스
    //FirebaseDatabase database = FirebaseDatabase.getInstance();
    //DatabaseReference myRef = database.getReference().child("종합뉴스");
    String first_key = "";
    String last_link = "";
    long total_key_count;
    int last_key_count;
    int ITEM_LOAD_COUNT = 300;
    boolean isMaxData = false;
    int scrollLoadTime;

    public LatestNewsFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        scrollLoadTime = 0;
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_latest_news, container, false);
        networkStatus = new NetworkStatus();

        initView(view);
        initNetwork();

        setUserName();
        initAdapter();

        loadFirstData();
        //getLastKeyFromFirebase();

        filterButtonClickListener();
        initAdapterClickListener();
        initRecyclerViewScrollListener();

        initSwipeRefreshRListener();

        return view;
    }

    private void initView(View view) {
        shimmerFrameLayout = (ShimmerFrameLayout) view.findViewById(R.id.shimmer_view_container);
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();

        loadingView = (LottieAnimationView) view.findViewById(R.id.lottieView);

        userName = (TextView) view.findViewById(R.id.userName);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setAlpha(0f);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        filterButton = (Button) view.findViewById(R.id.filterButton);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.mainGreen));

        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), 1)); 구분선 추가 코드
        //recyclerView.addItemDecoration(new NewsItemDecoration(getContext())); // 마진 설정을 위해 decoration 객체 설정

        LinearLayoutManager layoutManager = new LinearLayoutManagerWrapper(getContext(), LinearLayoutManager.VERTICAL, false);
        //layoutManager.setReverseLayout(true);
        //layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setItemAnimator(null);

    }

    private void initNetwork() {
        if (!networkStatus.isConnected(getContext())) {
            Snackbar.make(getActivity().findViewById(R.id.main_layout), "네트워크 연결상태를 확인해주세요", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void setUserName() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userName.setText(sharedPreferences.getString("userName", "Guest"));
    }

    private void initAdapter() {
        adapter = new RecyclerAdapter(getContext());
        recyclerView.setAdapter(adapter);
    }

    private void loadFirstData() {
        ArrayList<NewsItem> newsItems = new ArrayList<>();

        LoadFinished loadFinished = new LoadFinished() {
            @Override
            public void onLoadFinished() {
                adapter.sortItem();
                adapter.notifyDataSetChanged();
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.animate().alpha(1f)
                        .setDuration(400)
                        .setListener(null);
            }
        };

        LoadedMoreFinished loadedMoreFinished = new LoadedMoreFinished() {
            @Override
            public void onLoadMoreFinished(ArrayList<NewsItem> temp) {

                newsItems.addAll(temp);

                SetAdapterListener listener = adapter.getListener(fetchNewsData.getCheckedNewsNumber());
                listener.onAdapterListener(newsItems);

                isLoading = false;
                loadingView.setVisibility(View.GONE);
            }
        };

        fetchNewsData = new FetchNewsData(loadFinished,loadedMoreFinished, getActivity(), getContext(), adapter);

        fetchNewsData.loadFirstData();
    }

    /*private void getLastKeyFromFirebase() {
        Query getLastKey = FirebaseDatabase.getInstance().getReference()
                .child("종합뉴스");

        getLastKey.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                total_key_count = snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "can not get last key", Toast.LENGTH_SHORT).show();
            }
        });
    }*/

    private void filterButtonClickListener() {
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fragment.isVisible()) {
                    fragment.show(getActivity().getSupportFragmentManager(), "newsFilter");
                    fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.BaseBottomSheetDialog);
                }
            }
        });
    }

    private void initAdapterClickListener() {// 어댑터 아이템 클릭 리스너
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, View view, int position) {

                int id = view.getId();

                NewsItem newsItem = adapter.getItem(position);
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

    private void initRecyclerViewScrollListener() {
        // 리사이클러뷰 스크롤 리스너
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dx == 0 && dy == 0) return;

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading && dy > 0 /*&& (last_key_count - 1) != (int) total_key_count*/) {
                    if (layoutManager != null && !recyclerView.canScrollVertically(1)) {
                        isLoading = true;
                        loadMore();
                        scrollLoadTime++;
                    }
                }
            }
        });
    }

    private void loadMore() {

        if (!networkStatus.isConnected(getContext())) {
            Snackbar.make(getActivity().findViewById(R.id.main_layout), "네트워크 연결상태를 확인해주세요", Snackbar.LENGTH_LONG).show();
        }
        Handler handler = new Handler();
        loadingView.setVisibility(View.VISIBLE);
        handler.postDelayed(() -> {
            loadMoreData();
        }, 1000);

    }

    private void loadMoreData() {
        if (!isMaxData) {
            adapter.setAllLoadedListener(new SetAllLoadedFinished() {
                @Override
                public void onAllLoadedFinished(ArrayList<NewsItem> temp) {

                    Log.d("AllLoadedFinished", "모든 데이터 로드 완료");

                    int insertPosition = adapter.getItemCount();

                    Collections.sort(temp, new Comparator<NewsItem>() {
                        @Override
                        public int compare(NewsItem o1, NewsItem o2) {
                            if(o1.getTimeStamp() < o2.getTimeStamp()) {
                                return 1;
                            } else if(o1.getTimeStamp() > o2.getTimeStamp()) {
                                return -1;
                            }
                            return 0;
                        }
                    });

                    for(NewsItem item : temp) {
                        Log.d("AllLoaded Sorted", item.getTitle());
                    }

                    adapter.appendItems(temp);
                    adapter.notifyItemInserted(insertPosition);
                    temp.clear();
                }
            });

            fetchNewsData.loadMoreData();
        }
    }

    private void initSwipeRefreshRListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //((SwipeRefreshLayout.OnRefreshListener)getActivity()).onRefresh();
                scrollLoadTime = 0;
                shimmerFrameLayout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                recyclerView.setAlpha(0f);
                shimmerFrameLayout.startShimmer();
                adapter.clearItems();
                loadFirstData();

                swipeRefreshLayout.setRefreshing(false);

            }
        });
    }

    public class LinearLayoutManagerWrapper extends LinearLayoutManager {
        private AnticipateOvershootInterpolator enterInterpolator = new AnticipateOvershootInterpolator(2f);

        public LinearLayoutManagerWrapper(Context context) {
            super(context);
        }

        public LinearLayoutManagerWrapper(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public LinearLayoutManagerWrapper(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        public boolean supportsPredictiveItemAnimations() {
            return false;
        }

    }
}