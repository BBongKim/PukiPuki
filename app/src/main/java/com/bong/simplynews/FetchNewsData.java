package com.bong.simplynews;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import com.bong.simplynews.adapter.RecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class FetchNewsData {
    SharedPreferences sharedPreferences;
    Activity parentActivity;
    Context context;
    int ITEM_LOAD_COUNT;
    int checkedNewsNumber;

    ArrayList<NewsItem> previousList_Donga = new ArrayList<>();
    ArrayList<NewsItem> previousList_Hani = new ArrayList<>();
    ArrayList<NewsItem> previousList_Khan = new ArrayList<>();
    ArrayList<NewsItem> previousList_Kmib = new ArrayList<>();
    ArrayList<NewsItem> previousList_Segye = new ArrayList<>();
    ArrayList<NewsItem> previousList_Seoul = new ArrayList<>();
    ArrayList<NewsItem> previousList_MBN = new ArrayList<>();
    ArrayList<NewsItem> previousList_Yonhap = new ArrayList<>();
    ArrayList<NewsItem> previousList_Newsis = new ArrayList<>();
    ArrayList<NewsItem> previousList_Mk = new ArrayList<>();
    ArrayList<NewsItem> previousList_Edaily = new ArrayList<>();
    ArrayList<NewsItem> previousList_Fnnews = new ArrayList<>();
    ArrayList<NewsItem> previousList_Hankyoung = new ArrayList<>();
    ArrayList<NewsItem> previousList_Herald = new ArrayList<>();
    ArrayList<NewsItem> previousList_Asiae = new ArrayList<>();
    ArrayList<NewsItem> previousList_Nocut = new ArrayList<>();
    ArrayList<NewsItem> previousList_Ohmy = new ArrayList<>();
    ArrayList<NewsItem> previousList_Etnews = new ArrayList<>();
    ArrayList<NewsItem> previousList_Datanet = new ArrayList<>();
    ArrayList<NewsItem> previousList_Starin = new ArrayList<>();
    ArrayList<NewsItem> previousList_SDonga = new ArrayList<>();
    ArrayList<NewsItem> previousList_Skhan = new ArrayList<>();
    ArrayList<NewsItem> previousList_Inven = new ArrayList<>();

    boolean check_Donga;
    boolean check_Hani;
    boolean check_Khan;
    boolean check_Kmib;
    boolean check_Segye;
    boolean check_Seoul;
    boolean check_MBN;
    boolean check_Yonhap;
    boolean check_Newsis;
    boolean check_Mk;
    boolean check_Edaily;
    boolean check_Fnnews;
    boolean check_Hankyoung;
    boolean check_Herald;
    boolean check_Asiae;
    boolean check_Nocut;
    boolean check_Ohmy;
    boolean check_Etnews;
    boolean check_Datanet;
    boolean check_Starin;
    boolean check_SDonga;
    boolean check_Skhan;
    boolean check_Inven;


    String first_key_Donga;
    String first_key_Hani;
    String first_key_Khan;
    String first_key_Kmib;
    String first_key_Segye;
    String first_key_Seoul;
    String first_key_MBN;
    String first_key_Yonhap;
    String first_key_Newsis;
    String first_key_Mk;
    String first_key_Edaily;
    String first_key_Fnnews;
    String first_key_Hankyoung;
    String first_key_Herald;
    String first_key_Asiae;
    String first_key_Nocut;
    String first_key_Ohmy;
    String first_key_Etnews;
    String first_key_Datanet;
    String first_key_Starin;
    String first_key_SDonga;
    String first_key_Skhan;
    String first_key_Inven;

    RecyclerAdapter adapter;

    LoadFinished loadFinished;
    LoadedMoreFinished loadedMoreFinished;

    public FetchNewsData(LoadFinished loadFinished, LoadedMoreFinished loadedMoreFinished, Activity activity, Context context, RecyclerAdapter adapter) {
        this.parentActivity = activity;
        this.context = context;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(parentActivity);
        this.check_Donga = sharedPreferences.getBoolean("동아일보", true);
        this.check_Hani = sharedPreferences.getBoolean("한겨레", true);
        this.check_Khan = sharedPreferences.getBoolean("경향신문", true);
        this.check_Kmib = sharedPreferences.getBoolean("국민일보", true);
        this.check_Segye = sharedPreferences.getBoolean("세계일보", true);
        this.check_Seoul = sharedPreferences.getBoolean("서울일보", true);
        this.check_MBN = sharedPreferences.getBoolean("MBN", true);
        this.check_Yonhap = sharedPreferences.getBoolean("연합뉴스TV", true);
        this.check_Newsis = sharedPreferences.getBoolean("뉴시스", true);
        this.check_Mk = sharedPreferences.getBoolean("매일경제", true);
        this.check_Edaily = sharedPreferences.getBoolean("이데일리", true);
        this.check_Fnnews = sharedPreferences.getBoolean("파이낸셜뉴스", true);
        this.check_Hankyoung = sharedPreferences.getBoolean("한국경제", true);
        this.check_Herald = sharedPreferences.getBoolean("헤럴드경제", true);
        this.check_Asiae = sharedPreferences.getBoolean("아시아경제", true);
        this.check_Nocut = sharedPreferences.getBoolean("노컷뉴스", true);
        this.check_Ohmy = sharedPreferences.getBoolean("오마이뉴스", true);
        this.check_Etnews = sharedPreferences.getBoolean("전자신문", true);
        this.check_Datanet = sharedPreferences.getBoolean("데이터넷", true);
        this.check_Starin = sharedPreferences.getBoolean("스타인뉴스", true);
        this.check_SDonga = sharedPreferences.getBoolean("스포츠동아", true);
        this.check_Skhan = sharedPreferences.getBoolean("스포츠경향", true);
        this.check_Inven = sharedPreferences.getBoolean("인벤", true);

        this.adapter = adapter;

        this.loadFinished = loadFinished;
        this.loadedMoreFinished = loadedMoreFinished;
    }

    public void loadFirstData() {
        checkedNewsNumber = CountNewsNumber();

        if (checkedNewsNumber == 0) {
            loadFinished.onLoadFinished();
            return;
        }

        ITEM_LOAD_COUNT = 23 / checkedNewsNumber;

        if (check_Donga) {
            loadDonga(ITEM_LOAD_COUNT, adapter);
        }
        if (check_Hani) {
            loadHani(ITEM_LOAD_COUNT, adapter);
        }
        if (check_Khan) {
            loadKhan(ITEM_LOAD_COUNT, adapter);
        }

        if (check_Kmib) {
            loadKmib(ITEM_LOAD_COUNT, adapter);
        }
        if (check_Segye) {
            loadSegye(ITEM_LOAD_COUNT, adapter);
        }
        if (check_Seoul) {
            loadSeoul(ITEM_LOAD_COUNT, adapter);
        }
        if (check_MBN) {
            loadMbn(ITEM_LOAD_COUNT, adapter);
        }
        if (check_Yonhap) {
            loadYonhap(ITEM_LOAD_COUNT, adapter);
        }
        if (check_Newsis) {
            loadNewsis(ITEM_LOAD_COUNT, adapter);
        }
        if (check_Mk) {
            loadMk(ITEM_LOAD_COUNT, adapter);
        }
        if (check_Edaily) {
            loadEdaily(ITEM_LOAD_COUNT, adapter);
        }
        if (check_Fnnews) {
            loadFnnews(ITEM_LOAD_COUNT, adapter);
        }
        if (check_Hankyoung) {
            loadHankyoung(ITEM_LOAD_COUNT, adapter);
        }
        if (check_Herald) {
            loadHerald(ITEM_LOAD_COUNT, adapter);
        }
        if (check_Asiae) {
            loadAsiae(ITEM_LOAD_COUNT, adapter);
        }
        if (check_Nocut) {
            loadNocut(ITEM_LOAD_COUNT, adapter);
        }
        if (check_Ohmy) {
            loadOhmy(ITEM_LOAD_COUNT, adapter);
        }
        if (check_Etnews) {
            loadEtnews(ITEM_LOAD_COUNT, adapter);
        }
        if (check_Datanet) {
            loadDatanet(ITEM_LOAD_COUNT, adapter);
        }
        if (check_Starin) {
            loadStarin(ITEM_LOAD_COUNT, adapter);
        }
        if (check_SDonga) {
            loadSDonga(ITEM_LOAD_COUNT, adapter);
        }
        if (check_Skhan) {
            loadSKhan(ITEM_LOAD_COUNT, adapter);
        }
        if (check_Inven) {
            loadInven(ITEM_LOAD_COUNT, adapter);
        }


    }

    public void loadMoreData() {

        ITEM_LOAD_COUNT = 23 / checkedNewsNumber;

        if (check_Donga) {
            loadMoreDonga(ITEM_LOAD_COUNT, adapter);
        }
        if (check_Hani) {
            loadMoreHani(ITEM_LOAD_COUNT, adapter);
        }
        if (check_Khan) {
            loadMoreKhan(ITEM_LOAD_COUNT, adapter);
        }
        if (check_Kmib) {
            loadMoreKmib(ITEM_LOAD_COUNT, adapter);
        }
        if (check_Segye) {
            loadMoreSegye(ITEM_LOAD_COUNT, adapter);
        }
        if (check_Seoul) {
            loadMoreSeoul(ITEM_LOAD_COUNT, adapter);
        }
        if (check_MBN) {
            loadMoreMbn(ITEM_LOAD_COUNT, adapter);
        }
        if (check_Yonhap) {
            loadMoreYonhap(ITEM_LOAD_COUNT, adapter);
        }
        if (check_Newsis) {
            loadMoreNewsis(ITEM_LOAD_COUNT, adapter);
        }
        if (check_Mk) {
            loadMoreMk(ITEM_LOAD_COUNT, adapter);
        }
        if (check_Edaily) {
            loadMoreEdaily(ITEM_LOAD_COUNT, adapter);
        }
        if (check_Fnnews) {
            loadMoreFnnews(ITEM_LOAD_COUNT, adapter);
        }
        if (check_Hankyoung) {
            loadMoreHankyoung(ITEM_LOAD_COUNT, adapter);
        }
        if (check_Herald) {
            loadMoreHerald(ITEM_LOAD_COUNT, adapter);
        }
        if (check_Asiae) {
            loadMoreAsiae(ITEM_LOAD_COUNT, adapter);
        }
        if (check_Nocut) {
            loadMoreNocut(ITEM_LOAD_COUNT, adapter);
        }
        if (check_Ohmy) {
            loadMoreOhmy(ITEM_LOAD_COUNT, adapter);
        }
        if (check_Etnews) {
            loadMoreEtnews(ITEM_LOAD_COUNT, adapter);
        }
        if (check_Datanet) {
            loadMoreDatanet(ITEM_LOAD_COUNT, adapter);
        }
        if (check_Starin) {
            loadMoreStarin(ITEM_LOAD_COUNT, adapter);
        }
        if (check_SDonga) {
            loadMoreSDonga(ITEM_LOAD_COUNT, adapter);
        }
        if (check_Skhan) {
            loadMoreSKhan(ITEM_LOAD_COUNT, adapter);
        }
        if (check_Inven) {
            loadMoreInven(ITEM_LOAD_COUNT, adapter);
        }
    }

    private void loadDonga(int ITEM_LOAD_COUNT, RecyclerAdapter adapter) {
        Query query;

        query = FirebaseDatabase.getInstance().getReference()
                .child("종합뉴스")
                .child("DONGA")
                .orderByChild("time")
                .limitToLast(ITEM_LOAD_COUNT);

        // 파이어베이스 리스너
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NewsItem> temp = new ArrayList<>();
                boolean getFirstKey = false;

                if (snapshot.hasChildren()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NewsItem item = data.getValue(NewsItem.class);

                        previousList_Donga.add(item);
                        temp.add(item);

                        if (!getFirstKey) {
                            first_key_Donga = item.getTime();
                            getFirstKey = true;
                        }
                    }
                }
                Collections.reverse(temp);
                adapter.appendItems(temp);
                loadFinished.onLoadFinished();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "동아일보 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadHani(int ITEM_LOAD_COUNT, RecyclerAdapter adapter) {
        Query query;

        query = FirebaseDatabase.getInstance().getReference()
                .child("종합뉴스")
                .child("HANI")
                .orderByChild("time")
                .limitToLast(ITEM_LOAD_COUNT);

        // 파이어베이스 리스너
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NewsItem> temp = new ArrayList<>();
                boolean getFirstKey = false;

                if (snapshot.hasChildren()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NewsItem item = data.getValue(NewsItem.class);

                        temp.add(item);

                        if (!getFirstKey) {
                            first_key_Hani = item.getTime();
                            getFirstKey = true;
                        }
                    }
                    previousList_Hani.addAll(temp);
                }
                Collections.reverse(temp);
                adapter.appendItems(temp);
                loadFinished.onLoadFinished();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "한겨레 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadKhan(int ITEM_LOAD_COUNT, RecyclerAdapter adapter) {
        Query query;

        query = FirebaseDatabase.getInstance().getReference()
                .child("종합뉴스")
                .child("KHAN")
                .orderByChild("time")
                .limitToLast(ITEM_LOAD_COUNT);

        // 파이어베이스 리스너
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NewsItem> temp = new ArrayList<>();
                boolean getFirstKey = false;

                if (snapshot.hasChildren()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NewsItem item = data.getValue(NewsItem.class);

                        temp.add(item);

                        if (!getFirstKey) {
                            first_key_Khan = item.getTime();
                            getFirstKey = true;
                        }
                    }
                    previousList_Khan.addAll(temp);
                }
                Collections.reverse(temp);
                adapter.appendItems(temp);
                loadFinished.onLoadFinished();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "경향신문 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadKmib(int ITEM_LOAD_COUNT, RecyclerAdapter adapter) {
        Query query;

        query = FirebaseDatabase.getInstance().getReference()
                .child("종합뉴스")
                .child("KMIB")
                .orderByChild("time")
                .limitToLast(ITEM_LOAD_COUNT);

        // 파이어베이스 리스너
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NewsItem> temp = new ArrayList<>();
                boolean getFirstKey = false;

                if (snapshot.hasChildren()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NewsItem item = data.getValue(NewsItem.class);

                        temp.add(item);

                        if (!getFirstKey) {
                            first_key_Kmib = item.getTime();
                            getFirstKey = true;
                        }
                    }
                    previousList_Kmib.addAll(temp);
                }
                Collections.reverse(temp);
                adapter.appendItems(temp);
                loadFinished.onLoadFinished();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "국민일보 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadSegye(int ITEM_LOAD_COUNT, RecyclerAdapter adapter) {
        Query query;

        query = FirebaseDatabase.getInstance().getReference()
                .child("종합뉴스")
                .child("SEGYE")
                .orderByChild("time")
                .limitToLast(ITEM_LOAD_COUNT);

        // 파이어베이스 리스너
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NewsItem> temp = new ArrayList<>();

                boolean getFirstKey = false;

                if (snapshot.hasChildren()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NewsItem item = data.getValue(NewsItem.class);

                        temp.add(item);

                        if (!getFirstKey) {
                            first_key_Segye = item.getTime();
                            getFirstKey = true;
                        }
                    }
                    previousList_Segye.addAll(temp);
                }
                Collections.reverse(temp);
                adapter.appendItems(temp);
                loadFinished.onLoadFinished();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "세계일보 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadSeoul(int ITEM_LOAD_COUNT, RecyclerAdapter adapter) {


        Query query;

        query = FirebaseDatabase.getInstance().getReference()
                .child("종합뉴스")
                .child("SEOUL")
                .orderByChild("time")
                .limitToLast(ITEM_LOAD_COUNT);

        // 파이어베이스 리스너
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NewsItem> temp = new ArrayList<>();
                boolean getFirstKey = false;

                if (snapshot.hasChildren()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NewsItem item = data.getValue(NewsItem.class);

                        temp.add(item);

                        if (!getFirstKey) {
                            first_key_Seoul = item.getTime();
                            getFirstKey = true;
                        }
                    }
                    previousList_Seoul.addAll(temp);
                }
                Collections.reverse(temp);
                adapter.appendItems(temp);
                loadFinished.onLoadFinished();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "서울일보 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void loadMbn(int ITEM_LOAD_COUNT, RecyclerAdapter adapter) {
        Query query;

        query = FirebaseDatabase.getInstance().getReference()
                .child("종합뉴스")
                .child("MBN")
                .orderByChild("time")
                .limitToLast(ITEM_LOAD_COUNT);

        // 파이어베이스 리스너
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NewsItem> temp = new ArrayList<>();
                boolean getFirstKey = false;

                if (snapshot.hasChildren()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NewsItem item = data.getValue(NewsItem.class);

                        temp.add(item);

                        if (!getFirstKey) {
                            first_key_MBN = item.getTime();
                            getFirstKey = true;
                        }
                    }
                    previousList_MBN.addAll(temp);
                }
                Collections.reverse(temp);
                adapter.appendItems(temp);
                loadFinished.onLoadFinished();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "MBN 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void loadYonhap(int ITEM_LOAD_COUNT, RecyclerAdapter adapter) {
        Query query;

        query = FirebaseDatabase.getInstance().getReference()
                .child("종합뉴스")
                .child("YONHAP")
                .orderByChild("time")
                .limitToLast(ITEM_LOAD_COUNT);

        // 파이어베이스 리스너
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NewsItem> temp = new ArrayList<>();
                boolean getFirstKey = false;

                if (snapshot.hasChildren()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NewsItem item = data.getValue(NewsItem.class);

                        temp.add(item);

                        if (!getFirstKey) {
                            first_key_Yonhap = item.getTime();
                            getFirstKey = true;
                        }
                    }
                    previousList_Yonhap.addAll(temp);
                }
                Collections.reverse(temp);
                adapter.appendItems(temp);
                loadFinished.onLoadFinished();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "연합뉴스TV 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void loadNewsis(int ITEM_LOAD_COUNT, RecyclerAdapter adapter) {
        Query query;

        query = FirebaseDatabase.getInstance().getReference()
                .child("종합뉴스")
                .child("NEWSIS")
                .orderByChild("time")
                .limitToLast(ITEM_LOAD_COUNT);

        // 파이어베이스 리스너
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NewsItem> temp = new ArrayList<>();
                boolean getFirstKey = false;

                if (snapshot.hasChildren()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NewsItem item = data.getValue(NewsItem.class);

                        temp.add(item);

                        if (!getFirstKey) {
                            first_key_Newsis = item.getTime();
                            getFirstKey = true;
                        }
                    }
                    previousList_Newsis.addAll(temp);
                }
                Collections.reverse(temp);
                adapter.appendItems(temp);
                loadFinished.onLoadFinished();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "뉴시스 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void loadMk(int ITEM_LOAD_COUNT, RecyclerAdapter adapter) {
        Query query;

        query = FirebaseDatabase.getInstance().getReference()
                .child("종합뉴스")
                .child("MK")
                .orderByChild("time")
                .limitToLast(ITEM_LOAD_COUNT);

        // 파이어베이스 리스너
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NewsItem> temp = new ArrayList<>();
                boolean getFirstKey = false;

                if (snapshot.hasChildren()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NewsItem item = data.getValue(NewsItem.class);

                        temp.add(item);

                        if (!getFirstKey) {
                            first_key_Mk = item.getTime();
                            getFirstKey = true;
                        }
                    }
                    previousList_Mk.addAll(temp);
                }
                Collections.reverse(temp);
                adapter.appendItems(temp);
                loadFinished.onLoadFinished();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "매일경제 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadEdaily(int ITEM_LOAD_COUNT, RecyclerAdapter adapter) {
        Query query;

        query = FirebaseDatabase.getInstance().getReference()
                .child("종합뉴스")
                .child("EDAILY")
                .orderByChild("time")
                .limitToLast(ITEM_LOAD_COUNT);

        // 파이어베이스 리스너
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NewsItem> temp = new ArrayList<>();
                boolean getFirstKey = false;

                if (snapshot.hasChildren()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NewsItem item = data.getValue(NewsItem.class);

                        temp.add(item);

                        if (!getFirstKey) {
                            first_key_Edaily = item.getTime();
                            getFirstKey = true;
                        }
                    }
                    previousList_Edaily.addAll(temp);
                }
                Collections.reverse(temp);
                adapter.appendItems(temp);
                loadFinished.onLoadFinished();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "이데일리 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadFnnews(int ITEM_LOAD_COUNT, RecyclerAdapter adapter) {
        Query query;

        query = FirebaseDatabase.getInstance().getReference()
                .child("종합뉴스")
                .child("FNNEWS")
                .orderByChild("time")
                .limitToLast(ITEM_LOAD_COUNT);

        // 파이어베이스 리스너
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NewsItem> temp = new ArrayList<>();
                boolean getFirstKey = false;

                if (snapshot.hasChildren()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NewsItem item = data.getValue(NewsItem.class);

                        temp.add(item);

                        if (!getFirstKey) {
                            first_key_Fnnews = item.getTime();
                            getFirstKey = true;
                        }
                    }
                    previousList_Fnnews.addAll(temp);
                }
                Collections.reverse(temp);
                adapter.appendItems(temp);
                loadFinished.onLoadFinished();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "파이낸셜뉴스 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadHankyoung(int ITEM_LOAD_COUNT, RecyclerAdapter adapter) {
        Query query;

        query = FirebaseDatabase.getInstance().getReference()
                .child("종합뉴스")
                .child("HANKYOUNG")
                .orderByChild("time")
                .limitToLast(ITEM_LOAD_COUNT);

        // 파이어베이스 리스너
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NewsItem> temp = new ArrayList<>();
                boolean getFirstKey = false;

                if (snapshot.hasChildren()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NewsItem item = data.getValue(NewsItem.class);

                        temp.add(item);

                        if (!getFirstKey) {
                            first_key_Hankyoung = item.getTime();
                            getFirstKey = true;
                        }
                    }
                    previousList_Hankyoung.addAll(temp);
                }
                Collections.reverse(temp);
                adapter.appendItems(temp);
                loadFinished.onLoadFinished();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "한국경제 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadHerald(int ITEM_LOAD_COUNT, RecyclerAdapter adapter) {
        Query query;

        query = FirebaseDatabase.getInstance().getReference()
                .child("종합뉴스")
                .child("HERALD")
                .orderByChild("time")
                .limitToLast(ITEM_LOAD_COUNT);

        // 파이어베이스 리스너
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NewsItem> temp = new ArrayList<>();
                boolean getFirstKey = false;

                if (snapshot.hasChildren()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NewsItem item = data.getValue(NewsItem.class);

                        temp.add(item);

                        if (!getFirstKey) {
                            first_key_Herald = item.getTime();
                            getFirstKey = true;
                        }
                    }
                    previousList_Herald.addAll(temp);
                }
                Collections.reverse(temp);
                adapter.appendItems(temp);
                loadFinished.onLoadFinished();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "헤럴드경제 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAsiae(int ITEM_LOAD_COUNT, RecyclerAdapter adapter) {
        Query query;

        query = FirebaseDatabase.getInstance().getReference()
                .child("종합뉴스")
                .child("ASIAE")
                .orderByChild("time")
                .limitToLast(ITEM_LOAD_COUNT);

        // 파이어베이스 리스너
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NewsItem> temp = new ArrayList<>();
                boolean getFirstKey = false;

                if (snapshot.hasChildren()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NewsItem item = data.getValue(NewsItem.class);

                        temp.add(item);

                        if (!getFirstKey) {
                            first_key_Asiae = item.getTime();
                            getFirstKey = true;
                        }
                    }
                    previousList_Asiae.addAll(temp);
                }
                Collections.reverse(temp);
                adapter.appendItems(temp);
                loadFinished.onLoadFinished();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "아시아경제 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadNocut(int ITEM_LOAD_COUNT, RecyclerAdapter adapter) {
        Query query;

        query = FirebaseDatabase.getInstance().getReference()
                .child("종합뉴스")
                .child("NOCUT")
                .orderByChild("time")
                .limitToLast(ITEM_LOAD_COUNT);

        // 파이어베이스 리스너
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NewsItem> temp = new ArrayList<>();
                boolean getFirstKey = false;

                if (snapshot.hasChildren()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NewsItem item = data.getValue(NewsItem.class);

                        temp.add(item);

                        if (!getFirstKey) {
                            first_key_Nocut = item.getTime();
                            getFirstKey = true;
                        }
                    }
                    previousList_Nocut.addAll(temp);
                }
                Collections.reverse(temp);
                adapter.appendItems(temp);
                loadFinished.onLoadFinished();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "노컷뉴스 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadOhmy(int ITEM_LOAD_COUNT, RecyclerAdapter adapter) {
        Query query;

        query = FirebaseDatabase.getInstance().getReference()
                .child("종합뉴스")
                .child("OHMY")
                .orderByChild("time")
                .limitToLast(ITEM_LOAD_COUNT);

        // 파이어베이스 리스너
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NewsItem> temp = new ArrayList<>();
                boolean getFirstKey = false;

                if (snapshot.hasChildren()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NewsItem item = data.getValue(NewsItem.class);

                        temp.add(item);

                        if (!getFirstKey) {
                            first_key_Ohmy = item.getTime();
                            getFirstKey = true;
                        }
                    }
                    previousList_Ohmy.addAll(temp);
                }
                Collections.reverse(temp);
                adapter.appendItems(temp);
                loadFinished.onLoadFinished();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "오마이뉴스 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadEtnews(int ITEM_LOAD_COUNT, RecyclerAdapter adapter) {
        Query query;

        query = FirebaseDatabase.getInstance().getReference()
                .child("종합뉴스")
                .child("ETNEWS")
                .orderByChild("time")
                .limitToLast(ITEM_LOAD_COUNT);

        // 파이어베이스 리스너
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NewsItem> temp = new ArrayList<>();
                boolean getFirstKey = false;

                if (snapshot.hasChildren()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NewsItem item = data.getValue(NewsItem.class);

                        temp.add(item);

                        if (!getFirstKey) {
                            first_key_Etnews = item.getTime();
                            getFirstKey = true;
                        }
                    }
                    previousList_Etnews.addAll(temp);
                }
                Collections.reverse(temp);
                adapter.appendItems(temp);
                loadFinished.onLoadFinished();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "전자신문 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDatanet(int ITEM_LOAD_COUNT, RecyclerAdapter adapter) {
        Query query;

        query = FirebaseDatabase.getInstance().getReference()
                .child("종합뉴스")
                .child("DATANET")
                .orderByChild("time")
                .limitToLast(ITEM_LOAD_COUNT);

        // 파이어베이스 리스너
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NewsItem> temp = new ArrayList<>();
                boolean getFirstKey = false;

                if (snapshot.hasChildren()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NewsItem item = data.getValue(NewsItem.class);

                        temp.add(item);

                        if (!getFirstKey) {
                            first_key_Datanet = item.getTime();
                            getFirstKey = true;
                        }
                    }
                    previousList_Datanet.addAll(temp);
                }
                Collections.reverse(temp);
                adapter.appendItems(temp);
                loadFinished.onLoadFinished();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "데이터넷 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadStarin(int ITEM_LOAD_COUNT, RecyclerAdapter adapter) {
        Query query;

        query = FirebaseDatabase.getInstance().getReference()
                .child("종합뉴스")
                .child("STARIN")
                .orderByChild("time")
                .limitToLast(ITEM_LOAD_COUNT);

        // 파이어베이스 리스너
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NewsItem> temp = new ArrayList<>();
                boolean getFirstKey = false;

                if (snapshot.hasChildren()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NewsItem item = data.getValue(NewsItem.class);

                        temp.add(item);

                        if (!getFirstKey) {
                            first_key_Starin = item.getTime();
                            getFirstKey = true;
                        }
                    }
                    previousList_Starin.addAll(temp);
                }
                Collections.reverse(temp);
                adapter.appendItems(temp);
                loadFinished.onLoadFinished();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "스타인뉴스 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadSDonga(int ITEM_LOAD_COUNT, RecyclerAdapter adapter) {
        Query query;

        query = FirebaseDatabase.getInstance().getReference()
                .child("종합뉴스")
                .child("SPORTS_DONGA")
                .orderByChild("time")
                .limitToLast(ITEM_LOAD_COUNT);

        // 파이어베이스 리스너
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NewsItem> temp = new ArrayList<>();
                boolean getFirstKey = false;

                if (snapshot.hasChildren()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NewsItem item = data.getValue(NewsItem.class);

                        temp.add(item);

                        if (!getFirstKey) {
                            first_key_SDonga = item.getTime();
                            getFirstKey = true;
                        }
                    }
                    previousList_SDonga.addAll(temp);
                }
                Collections.reverse(temp);
                adapter.appendItems(temp);
                loadFinished.onLoadFinished();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "스포츠동아 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadSKhan(int ITEM_LOAD_COUNT, RecyclerAdapter adapter) {
        Query query;

        query = FirebaseDatabase.getInstance().getReference()
                .child("종합뉴스")
                .child("SPORTS_KHAN")
                .orderByChild("time")
                .limitToLast(ITEM_LOAD_COUNT);

        // 파이어베이스 리스너
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NewsItem> temp = new ArrayList<>();
                boolean getFirstKey = false;

                if (snapshot.hasChildren()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NewsItem item = data.getValue(NewsItem.class);

                        temp.add(item);

                        if (!getFirstKey) {
                            first_key_Skhan = item.getTime();
                            getFirstKey = true;
                        }
                    }
                    previousList_Skhan.addAll(temp);
                }
                Collections.reverse(temp);
                adapter.appendItems(temp);
                loadFinished.onLoadFinished();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "스포츠경향 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadInven(int ITEM_LOAD_COUNT, RecyclerAdapter adapter) {
        Query query;

        query = FirebaseDatabase.getInstance().getReference()
                .child("종합뉴스")
                .child("INVEN")
                .orderByChild("time")
                .limitToLast(ITEM_LOAD_COUNT);

        // 파이어베이스 리스너
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NewsItem> temp = new ArrayList<>();
                boolean getFirstKey = false;

                if (snapshot.hasChildren()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NewsItem item = data.getValue(NewsItem.class);

                        temp.add(item);

                        if (!getFirstKey) {
                            first_key_Inven = item.getTime();
                            getFirstKey = true;
                        }
                    }
                    previousList_Inven.addAll(temp);
                }
                Collections.reverse(temp);
                adapter.appendItems(temp);
                loadFinished.onLoadFinished();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "인벤 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMoreDonga(int ITEM_LOAD_COUNT, RecyclerAdapter adapter) {
        Query query;

        query = FirebaseDatabase.getInstance().getReference()
                .child("종합뉴스")
                .child("DONGA")
                .orderByChild("time")
                .endAt(first_key_Donga)
                .limitToLast(ITEM_LOAD_COUNT + 1);

        // 파이어베이스 리스너
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NewsItem> temp = new ArrayList<>();
                ArrayList<NewsItem> tempForPrevious = new ArrayList<>();
                boolean getFirstKey = false;

                int count = adapter.getItemCount();

                if (snapshot.hasChildren()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NewsItem item = data.getValue(NewsItem.class);

                        // 전 페이지 마지막 뉴스 링크와 중복 방지
                        tempForPrevious.add(item);
                        if (!previousList_Donga.contains(item)) {
                            temp.add(item);
                        }
                        if (!getFirstKey) {
                            first_key_Donga = item.getTime();
                            getFirstKey = true;
                        }
                    }

                    previousList_Donga.clear();
                    previousList_Donga.addAll(tempForPrevious);
                }
                //Collections.reverse(temp);
                //adapter.appendItems(temp);
                loadedMoreFinished.onLoadMoreFinished(temp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "동아일보 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMoreHani(int ITEM_LOAD_COUNT, RecyclerAdapter adapter) {
        Query query;

        query = FirebaseDatabase.getInstance().getReference()
                .child("종합뉴스")
                .child("HANI")
                .orderByChild("time")
                .endAt(first_key_Hani)
                .limitToLast(ITEM_LOAD_COUNT + 1);

        // 파이어베이스 리스너
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NewsItem> temp = new ArrayList<>();
                ArrayList<NewsItem> tempForPrevious = new ArrayList<>();
                boolean getFirstKey = false;

                int count = adapter.getItemCount();

                if (snapshot.hasChildren()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NewsItem item = data.getValue(NewsItem.class);

                        // 전 페이지 마지막 뉴스 링크와 중복 방지
                        tempForPrevious.add(item);
                        if (!previousList_Hani.contains(item)) {
                            temp.add(item);
                        }
                        if (!getFirstKey) {
                            first_key_Hani = item.getTime();
                            getFirstKey = true;
                        }
                    }

                    previousList_Hani.clear();
                    previousList_Hani.addAll(tempForPrevious);
                }

                //Collections.reverse(temp);
                //adapter.appendItems(temp);
                loadedMoreFinished.onLoadMoreFinished(temp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "한겨레 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadMoreKhan(int ITEM_LOAD_COUNT, RecyclerAdapter adapter) {
        Query query;

        query = FirebaseDatabase.getInstance().getReference()
                .child("종합뉴스")
                .child("KHAN")
                .orderByChild("time")
                .endAt(first_key_Khan)
                .limitToLast(ITEM_LOAD_COUNT + 1);

        // 파이어베이스 리스너
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NewsItem> temp = new ArrayList<>();
                ArrayList<NewsItem> tempForPrevious = new ArrayList<>();
                boolean getFirstKey = false;
                int count = adapter.getItemCount();

                if (snapshot.hasChildren()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NewsItem item = data.getValue(NewsItem.class);

                        // 전 페이지 마지막 뉴스 링크와 중복 방지
                        tempForPrevious.add(item);
                        if (!previousList_Khan.contains(item)) {
                            temp.add(item);
                        }
                        if (!getFirstKey) {
                            first_key_Khan = item.getTime();
                            getFirstKey = true;
                        }
                    }

                    previousList_Khan.clear();
                    previousList_Khan.addAll(tempForPrevious);
                }

                //Collections.reverse(temp);
                //adapter.appendItems(temp);

                loadedMoreFinished.onLoadMoreFinished(temp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "경향일보 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadMoreKmib(int ITEM_LOAD_COUNT, RecyclerAdapter adapter) {
        Query query;

        query = FirebaseDatabase.getInstance().getReference()
                .child("종합뉴스")
                .child("KMIB")
                .orderByChild("time")
                .endAt(first_key_Kmib)
                .limitToLast(ITEM_LOAD_COUNT + 1);

        // 파이어베이스 리스너
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NewsItem> temp = new ArrayList<>();
                ArrayList<NewsItem> tempForPrevious = new ArrayList<>();
                boolean getFirstKey = false;
                int count = adapter.getItemCount();

                if (snapshot.hasChildren()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NewsItem item = data.getValue(NewsItem.class);

                        // 전 페이지 마지막 뉴스 링크와 중복 방지
                        tempForPrevious.add(item);
                        if (!previousList_Kmib.contains(item)) {
                            temp.add(item);
                        }
                        if (!getFirstKey) {
                            first_key_Kmib = item.getTime();
                            getFirstKey = true;
                        }
                    }

                    previousList_Kmib.clear();
                    previousList_Kmib.addAll(tempForPrevious);
                }

                //Collections.reverse(temp);
                //adapter.appendItems(temp);

                loadedMoreFinished.onLoadMoreFinished(temp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "국민일보오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadMoreSegye(int ITEM_LOAD_COUNT, RecyclerAdapter adapter) {
        Query query;

        query = FirebaseDatabase.getInstance().getReference()
                .child("종합뉴스")
                .child("SEGYE")
                .orderByChild("time")
                .endAt(first_key_Segye)
                .limitToLast(ITEM_LOAD_COUNT + 1);

        // 파이어베이스 리스너
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NewsItem> temp = new ArrayList<>();
                ArrayList<NewsItem> tempForPrevious = new ArrayList<>();
                boolean getFirstKey = false;
                int count = adapter.getItemCount();

                if (snapshot.hasChildren()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NewsItem item = data.getValue(NewsItem.class);

                        // 전 페이지 마지막 뉴스 링크와 중복 방지
                        tempForPrevious.add(item);
                        if (!previousList_Segye.contains(item)) {
                            temp.add(item);
                        }
                        if (!getFirstKey) {
                            first_key_Segye = item.getTime();
                            getFirstKey = true;
                        }
                    }

                    previousList_Segye.clear();
                    previousList_Segye.addAll(tempForPrevious);
                }

                //Collections.reverse(temp);
                //adapter.appendItems(temp);

                loadedMoreFinished.onLoadMoreFinished(temp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "세계일보 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadMoreSeoul(int ITEM_LOAD_COUNT, RecyclerAdapter adapter) {

        Query query;

        query = FirebaseDatabase.getInstance().getReference()
                .child("종합뉴스")
                .child("SEOUL")
                .orderByChild("time")
                .endAt(first_key_Seoul)
                .limitToLast(ITEM_LOAD_COUNT + 1);

        // 파이어베이스 리스너
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NewsItem> temp = new ArrayList<>();
                ArrayList<NewsItem> tempForPrevious = new ArrayList<>();
                boolean getFirstKey = false;
                int count = adapter.getItemCount();

                if (snapshot.hasChildren()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NewsItem item = data.getValue(NewsItem.class);

                        // 전 페이지 마지막 뉴스 링크와 중복 방지
                        tempForPrevious.add(item);
                        if (!previousList_Seoul.contains(item)) {
                            temp.add(item);
                        }
                        if (!getFirstKey) {
                            first_key_Seoul = item.getTime();
                            getFirstKey = true;
                        }
                    }

                    previousList_Seoul.clear();
                    previousList_Seoul.addAll(tempForPrevious);
                }

                //Collections.reverse(temp);
                //adapter.appendItems(temp);

                loadedMoreFinished.onLoadMoreFinished(temp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "서울일보 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadMoreMbn(int ITEM_LOAD_COUNT, RecyclerAdapter adapter) {
        Query query;

        query = FirebaseDatabase.getInstance().getReference()
                .child("종합뉴스")
                .child("MBN")
                .orderByChild("time")
                .endAt(first_key_MBN)
                .limitToLast(ITEM_LOAD_COUNT + 1);

        // 파이어베이스 리스너
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NewsItem> temp = new ArrayList<>();
                ArrayList<NewsItem> tempForPrevious = new ArrayList<>();
                boolean getFirstKey = false;
                int count = adapter.getItemCount();

                if (snapshot.hasChildren()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NewsItem item = data.getValue(NewsItem.class);

                        // 전 페이지 마지막 뉴스 링크와 중복 방지
                        tempForPrevious.add(item);
                        if (!previousList_MBN.contains(item)) {
                            temp.add(item);
                        }
                        if (!getFirstKey) {
                            first_key_MBN = item.getTime();
                            getFirstKey = true;
                        }
                    }

                    previousList_MBN.clear();
                    previousList_MBN.addAll(tempForPrevious);
                }

                //Collections.reverse(temp);
                //adapter.appendItems(temp);

                loadedMoreFinished.onLoadMoreFinished(temp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "MBN 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadMoreYonhap(int ITEM_LOAD_COUNT, RecyclerAdapter adapter) {
        Query query;

        query = FirebaseDatabase.getInstance().getReference()
                .child("종합뉴스")
                .child("YONHAP")
                .orderByChild("time")
                .endAt(first_key_Yonhap)
                .limitToLast(ITEM_LOAD_COUNT + 1);

        // 파이어베이스 리스너
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NewsItem> temp = new ArrayList<>();
                ArrayList<NewsItem> tempForPrevious = new ArrayList<>();
                boolean getFirstKey = false;
                int count = adapter.getItemCount();

                if (snapshot.hasChildren()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NewsItem item = data.getValue(NewsItem.class);

                        // 전 페이지 마지막 뉴스 링크와 중복 방지
                        tempForPrevious.add(item);
                        if (!previousList_Yonhap.contains(item)) {
                            temp.add(item);
                        }
                        if (!getFirstKey) {
                            first_key_Yonhap = item.getTime();
                            getFirstKey = true;
                        }
                    }

                    previousList_Yonhap.clear();
                    previousList_Yonhap.addAll(tempForPrevious);
                }

                //Collections.reverse(temp);
                //adapter.appendItems(temp);

                loadedMoreFinished.onLoadMoreFinished(temp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "연합뉴스TV 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadMoreNewsis(int ITEM_LOAD_COUNT, RecyclerAdapter adapter) {
        Query query;

        query = FirebaseDatabase.getInstance().getReference()
                .child("종합뉴스")
                .child("NEWSIS")
                .orderByChild("time")
                .endAt(first_key_Newsis)
                .limitToLast(ITEM_LOAD_COUNT + 1);

        // 파이어베이스 리스너
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NewsItem> temp = new ArrayList<>();
                ArrayList<NewsItem> tempForPrevious = new ArrayList<>();
                boolean getFirstKey = false;
                int count = adapter.getItemCount();

                if (snapshot.hasChildren()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NewsItem item = data.getValue(NewsItem.class);

                        // 전 페이지 마지막 뉴스 링크와 중복 방지
                        tempForPrevious.add(item);
                        if (!previousList_Newsis.contains(item)) {
                            temp.add(item);
                        }
                        if (!getFirstKey) {
                            first_key_Newsis = item.getTime();
                            getFirstKey = true;
                        }
                    }

                    previousList_Newsis.clear();
                    previousList_Newsis.addAll(tempForPrevious);
                }

                //Collections.reverse(temp);
                //adapter.appendItems(temp);

                loadedMoreFinished.onLoadMoreFinished(temp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "뉴시스 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadMoreMk(int ITEM_LOAD_COUNT, RecyclerAdapter adapter) {
        Query query;

        query = FirebaseDatabase.getInstance().getReference()
                .child("종합뉴스")
                .child("MK")
                .orderByChild("time")
                .endAt(first_key_Mk)
                .limitToLast(ITEM_LOAD_COUNT + 1);

        // 파이어베이스 리스너
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NewsItem> temp = new ArrayList<>();
                ArrayList<NewsItem> tempForPrevious = new ArrayList<>();
                boolean getFirstKey = false;
                int count = adapter.getItemCount();

                if (snapshot.hasChildren()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NewsItem item = data.getValue(NewsItem.class);

                        // 전 페이지 마지막 뉴스 링크와 중복 방지
                        tempForPrevious.add(item);
                        if (!previousList_Mk.contains(item)) {
                            temp.add(item);
                        }
                        if (!getFirstKey) {
                            first_key_Mk = item.getTime();
                            getFirstKey = true;
                        }
                    }

                    previousList_Mk.clear();
                    previousList_Mk.addAll(tempForPrevious);
                }

                //Collections.reverse(temp);
                //adapter.appendItems(temp);

                loadedMoreFinished.onLoadMoreFinished(temp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "매일경제 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadMoreEdaily(int ITEM_LOAD_COUNT, RecyclerAdapter adapter) {

        for(NewsItem item : previousList_Edaily) {
            Log.d("Previous Edaily", item.getTitle());
        }

        Query query;

        query = FirebaseDatabase.getInstance().getReference()
                .child("종합뉴스")
                .child("EDAILY")
                .orderByChild("time")
                .endAt(first_key_Edaily)
                .limitToLast(ITEM_LOAD_COUNT + 1);

        // 파이어베이스 리스너
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NewsItem> temp = new ArrayList<>();
                ArrayList<NewsItem> tempForPrevious = new ArrayList<>();
                boolean getFirstKey = false;
                int count = adapter.getItemCount();

                if (snapshot.hasChildren()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NewsItem item = data.getValue(NewsItem.class);

                        // 전 페이지 마지막 뉴스 링크와 중복 방지
                        tempForPrevious.add(item);
                        if (!previousList_Edaily.contains(item)) {
                            temp.add(item);
                        }
                        if (!getFirstKey) {
                            first_key_Edaily = item.getTime();
                            getFirstKey = true;
                        }
                    }

                    previousList_Edaily.clear();
                    previousList_Edaily.addAll(tempForPrevious);
                }

                //Collections.reverse(temp);
                //adapter.appendItems(temp);

                loadedMoreFinished.onLoadMoreFinished(temp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "이데일리 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadMoreFnnews(int ITEM_LOAD_COUNT, RecyclerAdapter adapter) {
        Query query;

        query = FirebaseDatabase.getInstance().getReference()
                .child("종합뉴스")
                .child("FNNEWS")
                .orderByChild("time")
                .endAt(first_key_Fnnews)
                .limitToLast(ITEM_LOAD_COUNT + 1);

        // 파이어베이스 리스너
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NewsItem> temp = new ArrayList<>();
                ArrayList<NewsItem> tempForPrevious = new ArrayList<>();
                boolean getFirstKey = false;
                int count = adapter.getItemCount();

                if (snapshot.hasChildren()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NewsItem item = data.getValue(NewsItem.class);

                        // 전 페이지 마지막 뉴스 링크와 중복 방지
                        tempForPrevious.add(item);
                        if (!previousList_Fnnews.contains(item)) {
                            temp.add(item);
                        }
                        if (!getFirstKey) {
                            first_key_Fnnews = item.getTime();
                            getFirstKey = true;
                        }
                    }

                    previousList_Fnnews.clear();
                    previousList_Fnnews.addAll(tempForPrevious);
                }

                //Collections.reverse(temp);
                //adapter.appendItems(temp);

                loadedMoreFinished.onLoadMoreFinished(temp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "파이낸셜뉴스 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadMoreHankyoung(int ITEM_LOAD_COUNT, RecyclerAdapter adapter) {
        Query query;

        query = FirebaseDatabase.getInstance().getReference()
                .child("종합뉴스")
                .child("HANKYOUNG")
                .orderByChild("time")
                .endAt(first_key_Hankyoung)
                .limitToLast(ITEM_LOAD_COUNT + 1);

        // 파이어베이스 리스너
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NewsItem> temp = new ArrayList<>();
                ArrayList<NewsItem> tempForPrevious = new ArrayList<>();
                boolean getFirstKey = false;
                int count = adapter.getItemCount();

                if (snapshot.hasChildren()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NewsItem item = data.getValue(NewsItem.class);

                        // 전 페이지 마지막 뉴스 링크와 중복 방지
                        tempForPrevious.add(item);
                        if (!previousList_Hankyoung.contains(item)) {
                            temp.add(item);
                        }
                        if (!getFirstKey) {
                            first_key_Hankyoung = item.getTime();
                            getFirstKey = true;
                        }
                    }

                    previousList_Hankyoung.clear();
                    previousList_Hankyoung.addAll(tempForPrevious);
                }

                //Collections.reverse(temp);
                //adapter.appendItems(temp);

                loadedMoreFinished.onLoadMoreFinished(temp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "한국경제 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadMoreHerald(int ITEM_LOAD_COUNT, RecyclerAdapter adapter) {
        Query query;

        query = FirebaseDatabase.getInstance().getReference()
                .child("종합뉴스")
                .child("HERALD")
                .orderByChild("time")
                .endAt(first_key_Herald)
                .limitToLast(ITEM_LOAD_COUNT + 1);

        // 파이어베이스 리스너
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NewsItem> temp = new ArrayList<>();
                ArrayList<NewsItem> tempForPrevious = new ArrayList<>();
                boolean getFirstKey = false;
                int count = adapter.getItemCount();

                if (snapshot.hasChildren()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NewsItem item = data.getValue(NewsItem.class);

                        // 전 페이지 마지막 뉴스 링크와 중복 방지
                        tempForPrevious.add(item);
                        if (!previousList_Herald.contains(item)) {
                            temp.add(item);
                        }
                        if (!getFirstKey) {
                            first_key_Herald = item.getTime();
                            getFirstKey = true;
                        }
                    }

                    previousList_Herald.clear();
                    previousList_Herald.addAll(tempForPrevious);
                }

                //Collections.reverse(temp);
                //adapter.appendItems(temp);

                loadedMoreFinished.onLoadMoreFinished(temp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "헤럴드경제 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadMoreAsiae(int ITEM_LOAD_COUNT, RecyclerAdapter adapter) {
        Query query;

        query = FirebaseDatabase.getInstance().getReference()
                .child("종합뉴스")
                .child("ASIAE")
                .orderByChild("time")
                .endAt(first_key_Asiae)
                .limitToLast(ITEM_LOAD_COUNT + 1);

        // 파이어베이스 리스너
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NewsItem> temp = new ArrayList<>();
                ArrayList<NewsItem> tempForPrevious = new ArrayList<>();
                boolean getFirstKey = false;
                int count = adapter.getItemCount();

                if (snapshot.hasChildren()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NewsItem item = data.getValue(NewsItem.class);

                        // 전 페이지 마지막 뉴스 링크와 중복 방지
                        tempForPrevious.add(item);
                        if (!previousList_Asiae.contains(item)) {
                            temp.add(item);
                        }
                        if (!getFirstKey) {
                            first_key_Asiae = item.getTime();
                            getFirstKey = true;
                        }
                    }

                    previousList_Asiae.clear();
                    previousList_Asiae.addAll(tempForPrevious);
                }

                //Collections.reverse(temp);
                //adapter.appendItems(temp);

                loadedMoreFinished.onLoadMoreFinished(temp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "아시아경제 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadMoreNocut(int ITEM_LOAD_COUNT, RecyclerAdapter adapter) {
        Query query;

        query = FirebaseDatabase.getInstance().getReference()
                .child("종합뉴스")
                .child("NOCUT")
                .orderByChild("time")
                .endAt(first_key_Nocut)
                .limitToLast(ITEM_LOAD_COUNT + 1);

        // 파이어베이스 리스너
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NewsItem> temp = new ArrayList<>();
                ArrayList<NewsItem> tempForPrevious = new ArrayList<>();
                boolean getFirstKey = false;
                int count = adapter.getItemCount();

                if (snapshot.hasChildren()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NewsItem item = data.getValue(NewsItem.class);

                        // 전 페이지 마지막 뉴스 링크와 중복 방지
                        tempForPrevious.add(item);
                        if (!previousList_Nocut.contains(item)) {
                            temp.add(item);
                        }
                        if (!getFirstKey) {
                            first_key_Nocut = item.getTime();
                            getFirstKey = true;
                        }
                    }

                    previousList_Nocut.clear();
                    previousList_Nocut.addAll(tempForPrevious);
                }

                //Collections.reverse(temp);
                //adapter.appendItems(temp);

                loadedMoreFinished.onLoadMoreFinished(temp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "노컷뉴스 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadMoreOhmy(int ITEM_LOAD_COUNT, RecyclerAdapter adapter) {
        Query query;

        query = FirebaseDatabase.getInstance().getReference()
                .child("종합뉴스")
                .child("OHMY")
                .orderByChild("time")
                .endAt(first_key_Ohmy)
                .limitToLast(ITEM_LOAD_COUNT + 1);

        // 파이어베이스 리스너
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NewsItem> temp = new ArrayList<>();
                ArrayList<NewsItem> tempForPrevious = new ArrayList<>();
                boolean getFirstKey = false;
                int count = adapter.getItemCount();

                if (snapshot.hasChildren()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NewsItem item = data.getValue(NewsItem.class);

                        // 전 페이지 마지막 뉴스 링크와 중복 방지
                        tempForPrevious.add(item);
                        if (!previousList_Ohmy.contains(item)) {
                            temp.add(item);
                        }
                        if (!getFirstKey) {
                            first_key_Ohmy = item.getTime();
                            getFirstKey = true;
                        }
                    }

                    previousList_Ohmy.clear();
                    previousList_Ohmy.addAll(tempForPrevious);
                }

                //Collections.reverse(temp);
                //adapter.appendItems(temp);

                loadedMoreFinished.onLoadMoreFinished(temp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "오마이뉴스 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadMoreEtnews(int ITEM_LOAD_COUNT, RecyclerAdapter adapter) {
        Query query;

        query = FirebaseDatabase.getInstance().getReference()
                .child("종합뉴스")
                .child("ETNEWS")
                .orderByChild("time")
                .endAt(first_key_Etnews)
                .limitToLast(ITEM_LOAD_COUNT + 1);

        // 파이어베이스 리스너
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NewsItem> temp = new ArrayList<>();
                ArrayList<NewsItem> tempForPrevious = new ArrayList<>();
                boolean getFirstKey = false;
                int count = adapter.getItemCount();

                if (snapshot.hasChildren()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NewsItem item = data.getValue(NewsItem.class);

                        // 전 페이지 마지막 뉴스 링크와 중복 방지
                        tempForPrevious.add(item);
                        if (!previousList_Etnews.contains(item)) {
                            temp.add(item);
                        }
                        if (!getFirstKey) {
                            first_key_Etnews = item.getTime();
                            getFirstKey = true;
                        }
                    }

                    previousList_Etnews.clear();
                    previousList_Etnews.addAll(tempForPrevious);
                }

                //Collections.reverse(temp);
                //adapter.appendItems(temp);

                loadedMoreFinished.onLoadMoreFinished(temp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "전자신문 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadMoreDatanet(int ITEM_LOAD_COUNT, RecyclerAdapter adapter) {
        Query query;

        query = FirebaseDatabase.getInstance().getReference()
                .child("종합뉴스")
                .child("DATANET")
                .orderByChild("time")
                .endAt(first_key_Datanet)
                .limitToLast(ITEM_LOAD_COUNT + 1);

        // 파이어베이스 리스너
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NewsItem> temp = new ArrayList<>();
                ArrayList<NewsItem> tempForPrevious = new ArrayList<>();
                boolean getFirstKey = false;
                int count = adapter.getItemCount();

                if (snapshot.hasChildren()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NewsItem item = data.getValue(NewsItem.class);

                        // 전 페이지 마지막 뉴스 링크와 중복 방지
                        tempForPrevious.add(item);
                        if (!previousList_Datanet.contains(item)) {
                            temp.add(item);
                        }
                        if (!getFirstKey) {
                            first_key_Datanet = item.getTime();
                            getFirstKey = true;
                        }
                    }

                    previousList_Datanet.clear();
                    previousList_Datanet.addAll(tempForPrevious);
                }

                //Collections.reverse(temp);
                //adapter.appendItems(temp);

                loadedMoreFinished.onLoadMoreFinished(temp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "데이터넷 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadMoreStarin(int ITEM_LOAD_COUNT, RecyclerAdapter adapter) {
        Query query;

        query = FirebaseDatabase.getInstance().getReference()
                .child("종합뉴스")
                .child("STARIN")
                .orderByChild("time")
                .endAt(first_key_Starin)
                .limitToLast(ITEM_LOAD_COUNT + 1);

        // 파이어베이스 리스너
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NewsItem> temp = new ArrayList<>();
                ArrayList<NewsItem> tempForPrevious = new ArrayList<>();
                boolean getFirstKey = false;
                int count = adapter.getItemCount();

                if (snapshot.hasChildren()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NewsItem item = data.getValue(NewsItem.class);

                        // 전 페이지 마지막 뉴스 링크와 중복 방지
                        tempForPrevious.add(item);
                        if (!previousList_Starin.contains(item)) {
                            temp.add(item);
                        }
                        if (!getFirstKey) {
                            first_key_Starin = item.getTime();
                            getFirstKey = true;
                        }
                    }

                    previousList_Starin.clear();
                    previousList_Starin.addAll(tempForPrevious);
                }

                //Collections.reverse(temp);
                //adapter.appendItems(temp);

                loadedMoreFinished.onLoadMoreFinished(temp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "스타인뉴스 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadMoreSDonga(int ITEM_LOAD_COUNT, RecyclerAdapter adapter) {
        Query query;

        query = FirebaseDatabase.getInstance().getReference()
                .child("종합뉴스")
                .child("SPORTS_DONGA")
                .orderByChild("time")
                .endAt(first_key_SDonga)
                .limitToLast(ITEM_LOAD_COUNT + 1);

        // 파이어베이스 리스너
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NewsItem> temp = new ArrayList<>();
                ArrayList<NewsItem> tempForPrevious = new ArrayList<>();
                boolean getFirstKey = false;
                int count = adapter.getItemCount();

                if (snapshot.hasChildren()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NewsItem item = data.getValue(NewsItem.class);

                        // 전 페이지 마지막 뉴스 링크와 중복 방지
                        tempForPrevious.add(item);
                        if (!previousList_SDonga.contains(item)) {
                            temp.add(item);
                        }
                        if (!getFirstKey) {
                            first_key_SDonga = item.getTime();
                            getFirstKey = true;
                        }
                    }

                    previousList_SDonga.clear();
                    previousList_SDonga.addAll(tempForPrevious);
                }

                //Collections.reverse(temp);
                //adapter.appendItems(temp);

                loadedMoreFinished.onLoadMoreFinished(temp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "스포츠동아 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadMoreSKhan(int ITEM_LOAD_COUNT, RecyclerAdapter adapter) {
        Query query;

        query = FirebaseDatabase.getInstance().getReference()
                .child("종합뉴스")
                .child("SPORTS_KHAN")
                .orderByChild("time")
                .endAt(first_key_Skhan)
                .limitToLast(ITEM_LOAD_COUNT + 1);

        // 파이어베이스 리스너
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NewsItem> temp = new ArrayList<>();
                ArrayList<NewsItem> tempForPrevious = new ArrayList<>();
                boolean getFirstKey = false;
                int count = adapter.getItemCount();

                if (snapshot.hasChildren()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NewsItem item = data.getValue(NewsItem.class);

                        // 전 페이지 마지막 뉴스 링크와 중복 방지
                        tempForPrevious.add(item);
                        if (!previousList_Skhan.contains(item)) {
                            temp.add(item);
                        }
                        if (!getFirstKey) {
                            first_key_Skhan = item.getTime();
                            getFirstKey = true;
                        }
                    }

                    previousList_Skhan.clear();
                    previousList_Skhan.addAll(tempForPrevious);
                }

                //Collections.reverse(temp);
                //adapter.appendItems(temp);

                loadedMoreFinished.onLoadMoreFinished(temp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "스포츠경향 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadMoreInven(int ITEM_LOAD_COUNT, RecyclerAdapter adapter) {
        Query query;

        query = FirebaseDatabase.getInstance().getReference()
                .child("종합뉴스")
                .child("INVEN")
                .orderByChild("time")
                .endAt(first_key_Inven)
                .limitToLast(ITEM_LOAD_COUNT + 1);

        // 파이어베이스 리스너
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NewsItem> temp = new ArrayList<>();
                ArrayList<NewsItem> tempForPrevious = new ArrayList<>();
                boolean getFirstKey = false;
                int count = adapter.getItemCount();

                if (snapshot.hasChildren()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NewsItem item = data.getValue(NewsItem.class);

                        // 전 페이지 마지막 뉴스 링크와 중복 방지
                        tempForPrevious.add(item);
                        if (!previousList_Inven.contains(item)) {
                            temp.add(item);
                        }
                        if (!getFirstKey) {
                            first_key_Inven = item.getTime();
                            getFirstKey = true;
                        }
                    }

                    previousList_Inven.clear();
                    previousList_Inven.addAll(tempForPrevious);
                }

                //Collections.reverse(temp);
                //adapter.appendItems(temp);

                loadedMoreFinished.onLoadMoreFinished(temp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "인벤 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public int getITEM_LOAD_COUNT() {
        return ITEM_LOAD_COUNT;
    }

    public int getCheckedNewsNumber() {
        return checkedNewsNumber;
    }


    private int CountNewsNumber() {
        int count = 0;

        for (String siteName : SiteName.siteNames) {
            if (sharedPreferences.getBoolean(siteName, true)) {
                count++;
            }
        }
        return count;
    }

    private boolean newsFiltered(String link) {

        if (link.contains(SiteName.DONGA)) {
            return !sharedPreferences.getBoolean("동아일보", true);
        } else if (link.contains(SiteName.HANI)) {
            return !sharedPreferences.getBoolean("한겨레", true);
        } else if (link.contains(SiteName.KHAN)) {
            return !sharedPreferences.getBoolean("경향신문", true);
        } else if (link.contains(SiteName.KMIB)) {
            return !sharedPreferences.getBoolean("국민일보", true);
        } else if (link.contains(SiteName.SEGYE)) {
            return !sharedPreferences.getBoolean("세계일보", true);
        } else if (link.contains(SiteName.SEOUL)) {
            return !sharedPreferences.getBoolean("서울일보", true);
        } else if (link.contains(SiteName.MBN)) {
            return !sharedPreferences.getBoolean("MBN", true);
        } else if (link.contains(SiteName.MK)) {
            return !sharedPreferences.getBoolean("매일경제", true);
        } else if (link.contains(SiteName.EDAILY)) {
            return !sharedPreferences.getBoolean("이데일리", true);
        } else if (link.contains(SiteName.FNNEWS)) {
            return !sharedPreferences.getBoolean("파이낸셜뉴스", true);
        } else if (link.contains(SiteName.HANKYOUNG)) {
            return !sharedPreferences.getBoolean("한국경제", true);
        } else if (link.contains(SiteName.HERALD)) {
            return !sharedPreferences.getBoolean("헤럴드경제", true);
        } else if (link.contains(SiteName.ASIAE)) {
            return !sharedPreferences.getBoolean("아시아경제", true);
        } else if (link.contains(SiteName.NOCUT)) {
            return !sharedPreferences.getBoolean("노컷뉴스", true);
        } else if (link.contains(SiteName.OHMY)) {
            return !sharedPreferences.getBoolean("오마이뉴스", true);
        } else if (link.contains(SiteName.ETNEWS)) {
            return !sharedPreferences.getBoolean("전자신문", true);
        } else if (link.contains(SiteName.DATANET)) {
            return !sharedPreferences.getBoolean("데이터넷", true);
        } else if (link.contains(SiteName.STARIN)) {
            return !sharedPreferences.getBoolean("스타인뉴스", true);
        } else if (link.contains(SiteName.SPORTS_DONGA)) {
            return !sharedPreferences.getBoolean("스포츠동아", true);
        } else if (link.contains(SiteName.SPORTS_KHAN)) {
            return !sharedPreferences.getBoolean("스포츠경향", true);
        } else if (link.contains(SiteName.INVEN)) {
            return !sharedPreferences.getBoolean("인벤", true);
        }
        return false;
    }
}
