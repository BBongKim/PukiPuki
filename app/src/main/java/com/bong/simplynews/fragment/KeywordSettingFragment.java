package com.bong.simplynews.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.bong.simplynews.ClearEditText;
import com.bong.simplynews.NetworkStatus;
import com.bong.simplynews.R;
import com.github.kimcore.inko.Inko;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class KeywordSettingFragment extends Fragment {
    TextInputLayout textInputLayout;
    ClearEditText keywordText;
    FlexboxLayout flexboxLayout;
    ArrayList<String> keywords = new ArrayList<>();
    TextView keywordCounter, maxKeywordText;
    NetworkStatus networkStatus;

    final int MAX_KEYWORD_NUMBER = 10;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_keyword_setting, container, false);
        networkStatus = new NetworkStatus();

        initView(view);
        inputListener();

        return view;
    }

    private void initView(View view) {
        textInputLayout = (TextInputLayout) view.findViewById(R.id.textInputLayoutKeyword);
        keywordText = (ClearEditText) view.findViewById(R.id.keywordText);
        flexboxLayout = (FlexboxLayout) view.findViewById(R.id.flexBox);
        keywordCounter = (TextView) view.findViewById(R.id.keywordCount);

        maxKeywordText = (TextView) view.findViewById(R.id.maxKeywordNumber);
        maxKeywordText.setText("/"+ MAX_KEYWORD_NUMBER);
        textInputLayout.setCounterEnabled(true);
        textInputLayout.setCounterMaxLength(20);

        keywordText = (ClearEditText) view.findViewById(R.id.keywordText);
        keywordText.setTextColor(Color.BLACK);
    }

    private void inputListener() {
        // 완료 버튼 리스너
        keywordText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //do something
                    // 네트워크 검사
                    if(networkStatus.isConnected(getContext())) {
                        String keyword = keywordText.getText().toString();
                        // 키워드 중복 검사
                        if (!keywords.contains(keyword)) {
                            // 키워드 등록 수 검사
                            if (keywords.size() < MAX_KEYWORD_NUMBER) {
                                // 키워드 초성 및 특수문자 검사
                                if (!choSungCheck(keyword)) {
                                    // 키워드 문자열 길이 검사
                                    if(keyword.length() < 21) {
                                        // Chip에 데이터 추가
                                        textInputLayout.setError(null);
                                        if (!keyword.equals("")) {
                                            addChip(keyword);
                                        }
                                        keywordText.setText(null);
                                    } else {
                                        Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
                                        keywordText.startAnimation(shake);
                                        textInputLayout.setError("키워드는 최대 20글자까지 입력 가능합니다.");
                                    }
                                } else {
                                    Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
                                    keywordText.startAnimation(shake);
                                    textInputLayout.setError("키워드에 사용할 수 없는 문자가 포함되어 있습니다.");
                                }

                            } else {
                                Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
                                keywordText.startAnimation(shake);
                                textInputLayout.setError("키워드는 최대 "+ MAX_KEYWORD_NUMBER +"개까지 등록 가능합니다.");
                            }
                        } else {
                            Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
                            keywordText.startAnimation(shake);
                            textInputLayout.setError("이미 중복된 키워드가 있습니다.");
                        }
                        handled = true;
                    } else {
                        Snackbar.make(v, "네트워크 연결상태를 확인해주세요", Snackbar.LENGTH_SHORT).show();
                    }

                }
                return handled;
            }
        });

        // 스페이스, ; 키 눌렀을 때 리스너
        keywordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String string = s.toString();
                if (!string.equals(";") && !string.equals(" ")) {
                    if (string.contains(";") || string.contains(" ")) {
                        //do something
                        String keyword = keywordText.getText().toString();
                        if (!keywords.contains(keyword)) {
                            keyword = keyword.substring(0, keyword.length() - 1);
                            addChip(keyword);
                            keywordText.setText(null);
                        }
                    }
                } else {
                    keywordText.setText(null);
                }
            }
        });
    }

    private void addChip(String text) {
        Chip chip = (Chip) getLayoutInflater().inflate(R.layout.chip_item, null);
        chip.setText(text);
        keywords.add(text);  // 액티비티로 보낼 데이터 리스트에 저장
        keywordCounter.setText(Integer.toString(keywords.size()));

        // 알림 설정
        registerFireBaseKeyword(text);
        registerMessageKeyword(text);

        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
        layoutParams.rightMargin = dpToPx(4);

        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(networkStatus.isConnected(getContext())) {
                    flexboxLayout.removeView(chip);
                    keywords.remove(chip.getText());
                    keywordCounter.setText(Integer.toString(keywords.size()));
                    removeFireBaseKeyword((String) chip.getText());
                    removeMessageKeyword((String) chip.getText());
                } else {
                    Snackbar.make(v, "네트워크 연결상태를 확인해주세요", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        flexboxLayout.addView(chip, flexboxLayout.getChildCount(), layoutParams);
    }

    public void addPrevChip(String text) {
        Chip chip = (Chip) getLayoutInflater().inflate(R.layout.chip_item, null);
        chip.setText(text);
        keywords.add(text);
        keywordCounter.setText(Integer.toString(keywords.size()));
        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
        layoutParams.rightMargin = dpToPx(4);

        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flexboxLayout.removeView(chip);
                keywords.remove(chip.getText());
                keywordCounter.setText(Integer.toString(keywords.size()));
                removeFireBaseKeyword((String) chip.getText());
                removeMessageKeyword((String) chip.getText());
            }
        });

        flexboxLayout.addView(chip, flexboxLayout.getChildCount(), layoutParams);
    }

    private int dpToPx(int dp) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getContext().getResources().getDisplayMetrics());
        return px;
    }

    public ArrayList<String> getUserKeywords() {
        return keywords;
    }

    private void removeFireBaseKeyword(String keyword) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("keywords");

        // 키워드가 있으면 -1 값이 0이거나 값이 아예 없으면 아무 동작 하지 않음
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(keyword)) {
                    long count = snapshot.child(keyword).getValue(Long.class);
                    if (count > 0) {
                        long newValue = count - 1;
                        myRef.child(keyword).setValue(newValue);
                    }
                } else {
                    // Do nothing.
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("FireBase", "Error");
            }
        });
    }

    private void removeMessageKeyword(String keyword) {
        Inko inko = new Inko();

        // 해당 키워드로 알림 구독
        FirebaseMessaging.getInstance().unsubscribeFromTopic(inko.ko2en(keyword))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("FireBaseMessage", "unSubscribed");
                    }
                });
    }

    private boolean choSungCheck(String str) {
        if (str.matches("^[0-9가-힣a-zA-Z]*$")) {
            return false;
        } else {
            return true;
        }
    }

    private void registerMessageKeyword(String keyword) {
        Inko inko = new Inko();

        // 해당 키워드로 알림 구독
        FirebaseMessaging.getInstance().subscribeToTopic(inko.ko2en(keyword))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("FireBaseMessage", "subscribed");
                    }
                });
    }

    private void registerFireBaseKeyword(String keyword) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("keywords");

        // 키워드가 있으면 +1 없으면 새로 만들고 1로 초기화
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(keyword)) {
                    long count = snapshot.child(keyword).getValue(Long.class);
                    long newValue = count + 1;
                    myRef.child(keyword).setValue(newValue);
                } else {
                    myRef.child(keyword).setValue(1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("FireBase", "Error");
            }
        });
    }
}