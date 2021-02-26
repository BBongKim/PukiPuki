package com.bong.simplynews.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.algolia.search.saas.Client;
import com.algolia.search.saas.Index;
import com.bong.simplynews.NetworkStatus;
import com.bong.simplynews.NewsItem;
import com.bong.simplynews.NewsItemDecoration;
import com.bong.simplynews.R;
import com.bong.simplynews.activity.ReSettingActivity;
import com.bong.simplynews.adapter.KeywordAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class UserNewsFragment extends Fragment {
    TextView title;
    SharedPreferences sharedPreferences;
    RecyclerView recyclerViewForKeyword;
    View emptyView;
    KeywordAdapter adapterForKeyword;
    RecyclerView.AdapterDataObserver emptyObserver;
    ArrayList<NewsItem> userNewsItems = new ArrayList<>();
    String keyword;
    ArrayList<String> keywords = new ArrayList<>();
    Client client;
    Index index;

    FloatingActionButton fab;

    NetworkStatus networkStatus;

    boolean once = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_news, container, false);
        networkStatus = new NetworkStatus();

        //initAlgolia();
        initView(view);
        initNetwork();
        initAdapter();
        setKeywords();
        initRecyclerViewScrollListener();
        initFabClickListener();
        initAdapterClickListener();
        //checkKeywordAndSearch();


        return view;
    }

    private void initAlgolia() {
        client = new Client("N1GCBA16G0", "");  // 테스트용 나중에는 서버에서 키를 받도록 변경한다.
        index = client.getIndex("dev_NEWS");
    }

    private void initView(View view) {
        title = (TextView) view.findViewById(R.id.latestNewsText);
        recyclerViewForKeyword = (RecyclerView) view.findViewById(R.id.recyclerViewUserNews);
        recyclerViewForKeyword.addItemDecoration(new NewsItemDecoration(getContext()));
        LinearLayoutManager layoutManagerForRecent = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerViewForKeyword.setLayoutManager(layoutManagerForRecent);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);

        emptyView = (View) view.findViewById(R.id.emptyKeywordScreen);
    }

    private void initNetwork() {
        if (!networkStatus.isConnected(getContext())) {
            Snackbar.make(getActivity().findViewById(R.id.main_layout), "네트워크 연결상태를 확인해주세요", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void initAdapter() {
        adapterForKeyword = new KeywordAdapter(getContext());
        recyclerViewForKeyword.setAdapter(adapterForKeyword);
    }

    private void initRecyclerViewScrollListener() {
        recyclerViewForKeyword.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy < 0) {
                    fab.show();
                } else if (dy > 0) {
                    fab.hide();
                }
            }
        });
    }

    private void initFabClickListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!once) {
                    Intent intent = new Intent(getContext(), ReSettingActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    once = true;
                }
            }
        });
        once = false;
    }

    private void initAdapterClickListener() {
        // 어댑터 아이템 클릭 리스너

        adapterForKeyword.setOnItemClickListener(new KeywordAdapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void OnItemClick(RecyclerView.ViewHolder holder, View view, int position) {
                String keyword = adapterForKeyword.getItem(position);
                //String transitionName = "transition" + position;

                //TextView sharedView = view.findViewById(R.id.news_keyword);

                KeywordNewsFragment keywordNewsFragment = new KeywordNewsFragment(keyword);

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.main_layout, keywordNewsFragment, "keywordNewsFrag")
                        .commit();
            }
        });
    }

    private void setKeywords() {
        keywords.clear();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        keywords.addAll(getStringArrayPref(sharedPreferences, "keywords"));

        // 키워드가 없다면 empty 화면 띄우기
        if (keywords.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerViewForKeyword.setVisibility(View.GONE);
        } else {
            recyclerViewForKeyword.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);

            for (String keyword : keywords) {
                adapterForKeyword.addItem(keyword);
                Log.d("adapterForkeyword :", keyword);
            }
            adapterForKeyword.notifyDataSetChanged();
        }
    }

    private void setKeyword() {
        sharedPreferences = this.getActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);
        keyword = sharedPreferences.getString("keyword", null);
    }

    private void checkKeywordAndSearch() {
        if (!keywords.isEmpty()) {
            loadNewsData();
        } else {
            //
        }
    }

    public void loadNewsData() {

    }


    public void setAdapter() {
        //recyclerView.setAdapter(adapter);
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
}