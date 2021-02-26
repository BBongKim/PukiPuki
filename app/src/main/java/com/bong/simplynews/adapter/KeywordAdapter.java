package com.bong.simplynews.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bong.simplynews.R;

import java.util.ArrayList;

public class KeywordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<String> keywords = new ArrayList<>();
    Context context;
    OnItemClickListener listener;

    public KeywordAdapter(Context context) {
        this.context = context;
    }

    public static interface OnItemClickListener {
        public void OnItemClick(RecyclerView.ViewHolder holder, View view, int position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.keyword_item, parent, false);

        return new KeywordViewHolder(view);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof KeywordViewHolder) {
            String keyword = keywords.get(position);
            ((KeywordViewHolder) holder).newsKeyword.setTransitionName("transition" + position);
            KeywordViewHolder viewHolder = (KeywordViewHolder) holder;
            viewHolder.setOnItemClickListener(listener);
            viewHolder.setItem(keyword);
        }

    }

    @Override
    public int getItemCount() {
        return keywords.size();
    }

    public void addItem(String keyword) {
        keywords.add(keyword);
    }

    public String getItem(int position) {
        return keywords.get(position);
    }

    public void setOnItemClickListener(KeywordAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    private class KeywordViewHolder extends RecyclerView.ViewHolder {
        KeywordAdapter.OnItemClickListener listener;

        TextView newsKeyword;

        public KeywordViewHolder(@NonNull View itemView) {
            super(itemView);

            newsKeyword = (TextView) itemView.findViewById(R.id.news_keyword);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (listener != null) {
                        listener.OnItemClick(KeywordViewHolder.this, v, position);
                    }
                }
            });
        }

        public void setOnItemClickListener(KeywordAdapter.OnItemClickListener listener) {
            this.listener = listener;
        }

        public void setItem(String keyword) {
            newsKeyword.setText(keyword);
        }
    }

}
