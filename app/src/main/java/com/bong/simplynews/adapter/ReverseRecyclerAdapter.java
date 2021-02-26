package com.bong.simplynews.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bong.simplynews.NewsItem;
import com.bong.simplynews.R;
import com.bong.simplynews.SiteName;

import java.util.ArrayList;
import java.util.Collections;

public class ReverseRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    //private final int VIEW_TYPE_LOADING = 1;
    private final int VIEW_TYPE_EMPTY = 2;
    private final int VIEW_TYPE_ITEM_SMALL = 3;

    Context context;

    ArrayList<NewsItem> newsItems = new ArrayList<NewsItem>();

    OnItemClickListener listener;

    int width;
    int height;

    public ReverseRecyclerAdapter(Context context) {
        this.context = context;
    }

    public static interface OnItemClickListener {
        public void onItemClick(RecyclerView.ViewHolder holder, View view, int position);
    }

    @Override
    public int getItemCount() {
        if (newsItems.size() == 0) {
            return 1;
        } else {
            return newsItems.size();
        }
    }

    public void sortItem() {
        Collections.sort(newsItems);
    }

    public void addItem(NewsItem item) {
        newsItems.add(item);
    }

    public void addItems(ArrayList<NewsItem> items) {
        newsItems = items;
    }

    public void appendItems(ArrayList<NewsItem> items) {
        newsItems.addAll(items);
    }

    public NewsItem getItem(int position) {
        return newsItems == null ? null : newsItems.get(position);
    }

    public void addNull() {
        newsItems.add(null);
        notifyItemInserted(newsItems.size() - 1);
    }

    public void removeNull() {
        newsItems.remove(newsItems.size() - 1);
        notifyItemRemoved(newsItems.size());
    }

    public void clearItems() {
        newsItems.clear();
    }

    // View 타입 확인
    @Override
    public int getItemViewType(int position) {
        if (newsItems.size() == 0) {
            return VIEW_TYPE_EMPTY;
        } else {
            //if(newsItems.get(position) == null) {
            //return VIEW_TYPE_LOADING;
            //}
            //else {
            NewsItem item = newsItems.get(getItemCount() - position - 1);
            String imageLink = item.getImageLink();

            Glide.with(context).asBitmap().load(imageLink).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    width = resource.getWidth();
                    height = resource.getHeight();
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }
            });

            if(width < 200 || height < 200) {
                return  VIEW_TYPE_ITEM_SMALL;
            } else {
                return  VIEW_TYPE_ITEM;
            }
            //}
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_ITEM) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.news_item, parent, false);

            return new ItemViewHolder(itemView);
        }
        //else if (viewType == VIEW_TYPE_LOADING) {
        //LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View itemView = inflater.inflate(R.layout.loading_item, parent, false);

        //return new LoadingViewHolder(itemView);
        //}
        else if (viewType == VIEW_TYPE_EMPTY) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.news_empty_item, parent, false);

            return new EmptyViewHolder(itemView);
        } else if (viewType == VIEW_TYPE_ITEM_SMALL) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.news_item_smalls, parent, false);

            return new ItemSmallViewHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            NewsItem newsItem = newsItems.get(getItemCount() - position - 1);
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            viewHolder.setOnItemClickListener(listener);
            viewHolder.setItem(newsItem);
        } //else if (holder instanceof LoadingViewHolder) { }

        else if (holder instanceof EmptyViewHolder) { }

        else if (holder instanceof ItemSmallViewHolder) {
            NewsItem newsItem = newsItems.get(getItemCount() - position - 1);
            ItemSmallViewHolder viewHolder = (ItemSmallViewHolder) holder;
            viewHolder.setOnItemClickListener(listener);
            viewHolder.setItem(newsItem);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

// 아이템 뷰홀더 클래스스

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView textTitle, textLink, textDescription, textTime, siteName;
        ImageView imageView;
        ImageView favicon;
        ImageView shareButton;

        ReverseRecyclerAdapter.OnItemClickListener listener;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            textTitle = (TextView) itemView.findViewById(R.id.news_title);
            textDescription = (TextView) itemView.findViewById(R.id.news_description);
            textTime = (TextView) itemView.findViewById(R.id.news_time);

            imageView = (ImageView) itemView.findViewById(R.id.news_image);

            favicon = (ImageView) itemView.findViewById(R.id.favicon);
            siteName = (TextView) itemView.findViewById(R.id.siteName);

            shareButton = (ImageView) itemView.findViewById(R.id.shareButton);

            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(listener != null) {
                        listener.onItemClick(ItemViewHolder.this, v, position);
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (listener != null) {
                        listener.onItemClick(ItemViewHolder.this, v, position);
                    }
                }
            });

        }

        public void setItem(NewsItem newsItem) {

            textTitle.setText(newsItem.getTitle());
            textDescription.setText(newsItem.getDescription());

            // 시간 설정
            String time = formatTimeString(newsItem.getTimeStamp());
            textTime.setText(time);

            // 이미지 설정
            Glide.with(itemView).load(newsItem.getImageLink()).override(800, 800).transform(new CenterCrop(), new RoundedCorners(20)).into(imageView);

            // 파비콘 설정
            String link = newsItem.getLink();
            setFaviconAndSiteName(favicon, siteName, link);
        }

        public void setOnItemClickListener(ReverseRecyclerAdapter.OnItemClickListener listener) {
            this.listener = listener;
        }
    }

    // 로딩 뷰 홀더
    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            //progressBar = itemView.findViewById(R.id.progressBar);
        }

        private void showLoadingView(LoadingViewHolder viewHolder, int position) {
            //
        }
    }

    // empty view
    private class EmptyViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        public EmptyViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.news_empty_image);

        }
    }

    // item_small view
    private class ItemSmallViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle, textDescription, textTime, siteName;
        ImageView imageView;
        ImageView favicon;

        ImageView shareButton;

        ReverseRecyclerAdapter.OnItemClickListener listener;

        public ItemSmallViewHolder(@NonNull View itemView) {
            super(itemView);

            textTitle = (TextView) itemView.findViewById(R.id.news_title_smalls);
            textDescription = (TextView) itemView.findViewById(R.id.news_description_smalls);
            textTime = (TextView) itemView.findViewById(R.id.news_time_smalls);
            siteName = (TextView) itemView.findViewById(R.id.siteName_smalls);
            imageView = (ImageView) itemView.findViewById(R.id.news_image_smalls);
            favicon = (ImageView) itemView.findViewById(R.id.favicon_smalls);

            shareButton = (ImageView) itemView.findViewById(R.id.shareButton_small);

            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(listener != null) {
                        listener.onItemClick(ItemSmallViewHolder.this, v, position);
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (listener != null) {
                        listener.onItemClick(ItemSmallViewHolder.this, v, position);
                    }
                }
            });
        }

        public void setItem(NewsItem newsItem) {
            textTitle.setText(newsItem.getTitle());
            textDescription.setText(newsItem.getDescription());

            // 시간 설정
            String time = formatTimeString(newsItem.getTimeStamp());
            textTime.setText(time);

            // 이미지
            Glide.with(itemView).load(newsItem.getImageLink()).override(350, 350).transform(new CenterCrop(), new RoundedCorners(20)).into(imageView);

            // 파비콘 설정
            String link = newsItem.getLink();
            setFaviconAndSiteName(favicon, siteName, link);
        }


        public void setOnItemClickListener(ReverseRecyclerAdapter.OnItemClickListener listener) {
            this.listener = listener;
        }
    }

    private static class TIME_MAXIMUM {
        public static final int SEC = 60;
        public static final int MIN = 60;
        public static final int HOUR = 24;
        public static final int DAY = 30;
        public static final int MONTH = 12;
    }

    public static String formatTimeString(long regTime) {
        long curTime = System.currentTimeMillis() / 1000;
        long diffTime = (curTime - regTime);
        String msg = null;
        if (diffTime < TIME_MAXIMUM.SEC) {
            msg = "방금 전";
        } else if ((diffTime /= TIME_MAXIMUM.SEC) < TIME_MAXIMUM.MIN) {
            msg = diffTime + "분 전";
        } else if ((diffTime /= TIME_MAXIMUM.MIN) < TIME_MAXIMUM.HOUR) {
            msg = (diffTime) + "시간 전";
        } else if ((diffTime /= TIME_MAXIMUM.HOUR) < TIME_MAXIMUM.DAY) {
            msg = (diffTime) + "일 전";
        } else if ((diffTime /= TIME_MAXIMUM.DAY) < TIME_MAXIMUM.MONTH) {
            msg = (diffTime) + "달 전";
        } else {
            msg = (diffTime) + "년 전";
        }
        return msg;
    }

    private void setFaviconAndSiteName(ImageView favicon, TextView siteName, String link) {
        if (link.contains(SiteName.DONGA)) {
            favicon.setImageResource(R.drawable.favicon_donga);
            siteName.setText("동아일보");
        } else if (link.contains(SiteName.HANI)) {
            favicon.setImageResource(R.drawable.favicon_hani);
            siteName.setText("한겨레");
        } else if (link.contains(SiteName.KHAN)) {
            favicon.setImageResource(R.drawable.favicon_khan);
            siteName.setText("경향신문");
        } else if (link.contains(SiteName.KMIB)) {
            favicon.setImageResource(R.drawable.favicon_kmib);
            siteName.setText("국민일보");
        } else if (link.contains(SiteName.SEGYE)) {
            favicon.setImageResource(R.drawable.favicon_segye);
            siteName.setText("세계일보");
        } else if (link.contains(SiteName.SEOUL)) {
            favicon.setImageResource(R.drawable.icon_seoulilbo);
            siteName.setText("서울일보");
        } else if (link.contains(SiteName.MBN)) {
            favicon.setImageResource(R.drawable.favicon_mk);
            siteName.setText("MBN");
        } else if (link.contains(SiteName.YONHAP)) {
            favicon.setImageResource(R.drawable.favicon_yonhap);
            siteName.setText("연합뉴스TV");
        } else if (link.contains(SiteName.NEWSIS)) {
            favicon.setImageResource(R.drawable.favicon_newsis);
            siteName.setText("뉴시스");
        } else if (link.contains(SiteName.MK)) {
            favicon.setImageResource(R.drawable.favicon_mk);
            siteName.setText("매일경제");
        } else if (link.contains(SiteName.EDAILY)) {
            favicon.setImageResource(R.drawable.favicon_edaily);
            siteName.setText("이데일리");
        } else if (link.contains(SiteName.FNNEWS)) {
            favicon.setImageResource(R.drawable.favicon_fnnews);
            siteName.setText("파이낸셜뉴스");
        } else if (link.contains(SiteName.HANKYOUNG)) {
            favicon.setImageResource(R.drawable.favicon_hankyoung);
            siteName.setText("한국경제");
        } else if (link.contains(SiteName.HERALD)) {
            favicon.setImageResource(R.drawable.favicon_herald);
            siteName.setText("헤럴드경제");
        } else if (link.contains(SiteName.ASIAE)) {
            favicon.setImageResource(R.drawable.favicon_asiae);
            siteName.setText("아시아경제");
        } else if (link.contains(SiteName.NOCUT)) {
            favicon.setImageResource(R.drawable.favicon_nocut);
            siteName.setText("노컷뉴스");
        } else if (link.contains(SiteName.OHMY)) {
            favicon.setImageResource(R.drawable.favicon_ohmynews);
            siteName.setText("오마이뉴스");
        } else if (link.contains(SiteName.ETNEWS)) {
            favicon.setImageResource(R.drawable.favicon_etnews);
            siteName.setText("전자신문");
        } else if (link.contains(SiteName.DATANET)) {
            favicon.setImageResource(R.drawable.favicon_datanet);
            siteName.setText("데이터넷");
        } else if (link.contains(SiteName.STARIN)) {
            favicon.setImageResource(R.drawable.favicon_starinnews);
            siteName.setText("스타인뉴스");
        } else if (link.contains(SiteName.SPORTS_DONGA)) {
            favicon.setImageResource(R.drawable.favicon_sportsdonga);
            siteName.setText("스포츠동아");
        } else if (link.contains(SiteName.SPORTS_KHAN)) {
            favicon.setImageResource(R.drawable.favicon_sportskhan);
            siteName.setText("스포츠경향");
        } else if (link.contains(SiteName.INVEN)) {
            favicon.setImageResource(R.drawable.favicon_inven);
            siteName.setText("인벤");
        }
    }
}
