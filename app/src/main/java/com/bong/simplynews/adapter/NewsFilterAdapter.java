package com.bong.simplynews.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bong.simplynews.NewsPress;
import com.bong.simplynews.R;

import java.util.ArrayList;

public class NewsFilterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<NewsPress> newsPresses = new ArrayList<>();
    Context context;

    public NewsFilterAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.news_press_item, parent, false);

        return new NewsPressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final NewsPress item = newsPresses.get(position);
        NewsPressViewHolder viewHolder = (NewsPressViewHolder) holder;
        viewHolder.checkBox.setOnCheckedChangeListener(null);
        viewHolder.checkBox.setChecked(item.isSelected());

        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                item.setSelected(isChecked);
            }
        });

        viewHolder.setItem(item);
    }

    public void addItem(NewsPress item) {
        newsPresses.add(item);
    }

    public NewsPress getItem(int position) {
        return newsPresses.get(position);
    }
    public ArrayList<NewsPress> getItems() {
        return newsPresses;
    }

    @Override
    public int getItemCount() {
        return newsPresses.size();
    }

    private class NewsPressViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView pressName;
        CheckBox checkBox;

        public NewsPressViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.newsPressImage);
            pressName = (TextView) itemView.findViewById(R.id.newsPressName);
            checkBox = (CheckBox) itemView.findViewById(R.id.newsPressCheckBox);
        }

        public void setItem(NewsPress item) {
            Glide.with(context).load(item.getImageID()).override(150,500).fitCenter().into(imageView);

            pressName.setText(item.getName());
        }
    }
}
