package com.bong.simplynews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

public class ClearEditText extends AppCompatEditText implements TextWatcher, View.OnTouchListener , View.OnFocusChangeListener{
    Drawable cancle;
    private OnFocusChangeListener onFocusChangeListener;
    private OnTouchListener onTouchListener;

    @SuppressLint("ResourceAsColor")
    public ClearEditText(@NonNull Context context) {
        super(context);
        init();
    }

    @SuppressLint("ResourceAsColor")
    public ClearEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @SuppressLint("ResourceAsColor")
    public ClearEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void setOnTouchListener( OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }

    @Override
    public void setOnFocusChangeListener( OnFocusChangeListener onFocusChangeListener) {
        this.onFocusChangeListener = onFocusChangeListener;
    }

    private void init() {
        cancle = ContextCompat.getDrawable(getContext(), R.drawable.ic_fi_rr_cross_circle);
        cancle.setBounds(0, 0, cancle.getIntrinsicWidth(), cancle.getIntrinsicHeight());

        setClearIconVisible(false);
        addTextChangedListener(this);

        super.setOnTouchListener(this);
    }

    private void setClearIconVisible(boolean visible) {
        cancle.setVisible(visible, false);
        setCompoundDrawables(null,null, visible ? cancle : null,null);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(isFocused()) {
            setClearIconVisible(s.length() > 0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int x = (int) event.getX();
        if (cancle.isVisible() && x > getWidth() - getPaddingRight() - cancle.getIntrinsicWidth()) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                setError(null);
                setText(null);
            }
            return true;
        }

        if (onTouchListener != null) {
            return onTouchListener.onTouch(v, event);
        } else {
            return false;
        }

    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }

        if (onFocusChangeListener != null) {
            onFocusChangeListener.onFocusChange(v, hasFocus);
        }
    }
}

