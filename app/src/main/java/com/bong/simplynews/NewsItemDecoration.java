package com.bong.simplynews;

import android.content.Context;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewsItemDecoration extends RecyclerView.ItemDecoration{
    private int size5;
    private int size10;
    private int size15;
    private int size20;

    public NewsItemDecoration(Context context) {
        this.size5 = dpToPx(context, 5);
        this.size10 = dpToPx(context, 10);
        this.size15 = dpToPx(context, 15);
        this.size20 = dpToPx(context, 20);
    }

    private int dpToPx(Context context, int dp) {
        return (int) TypedValue.applyDimension
                (TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.top = size5;
        outRect.bottom = size5;

        outRect.left = size15;
        outRect.right = size15;
    }
}
