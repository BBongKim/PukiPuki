package com.bong.simplynews.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.bong.simplynews.ClearEditText;
import com.bong.simplynews.R;
import com.google.android.material.textfield.TextInputLayout;

public class NameSettingFragment extends Fragment {
    TextInputLayout textInputLayout;
    EditText nameText;
    Button parentNextButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_name_setting, container, false);

        initView(view);
        return view;
    }

    @SuppressLint("ResourceAsColor")
    private void initView(View view) {
        textInputLayout = (TextInputLayout) view.findViewById(R.id.textInputLayoutName);
        nameText = (EditText) view.findViewById(R.id.nameText);
        parentNextButton = getActivity().findViewById(R.id.nextButton);

        textInputLayout.setCounterEnabled(true);
        textInputLayout.setCounterMaxLength(10);

        nameText.setTextColor(Color.BLACK);
        nameText.setHintTextColor(R.color.default_grey);

        nameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String nameInput = nameText.getText().toString().trim();

                if(nameInput.isEmpty() || nameInput.length() > 10) {
                    parentNextButton.setEnabled(false);
                }else {
                    parentNextButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public String getUserName() {
        return nameText.getText().toString();
    }
}