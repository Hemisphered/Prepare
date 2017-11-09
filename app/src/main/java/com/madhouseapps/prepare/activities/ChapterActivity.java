package com.madhouseapps.prepare.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.madhouseapps.prepare.R;

import java.util.ArrayList;
import java.util.List;

public class ChapterActivity extends AppCompatActivity {

    private static final String TAG = "ChapterActivity";
    private LinearLayout chapterLayout;
    private ScrollView chapterScroll;
    private ProgressBar progressBar;
    private TextView chapterTitle, nextChp, prevChp;
    private android.support.v7.widget.Toolbar chapterToolbar;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Typeface poppins, poppins_bold;
    private ImageView nightMode;
    private LinearLayout bottomLayout;
    private List<TextView> textViewList = new ArrayList<>();
    private ArrayList<String> chapterList = new ArrayList<>();
    private int primaryColor;
    private String subject;
    private boolean night_mode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);
        chapterTitle = findViewById(R.id.chapter_title);
        nextChp = findViewById(R.id.next_chp);
        prevChp = findViewById(R.id.previous_chp);
        progressBar = findViewById(R.id.chapter_progress);
        chapterLayout = findViewById(R.id.chapter_layout);
        chapterScroll = findViewById(R.id.chapter_scroll);
        bottomLayout = findViewById(R.id.bottom_layout);
        nightMode = findViewById(R.id.night_mode);
        chapterToolbar = findViewById(R.id.chapter_toolbar);
        firebaseDatabase = FirebaseDatabase.getInstance();
        poppins_bold = Typeface.createFromAsset(getAssets(), "fonts/poppins_bold.ttf");
        Intent intent = getIntent();
        chapterList = intent.getStringArrayListExtra("chapterList");
        final String chapterKey = intent.getStringExtra("chapterKey");
        subject = intent.getStringExtra("subject");
        String chapterName = intent.getStringExtra("chapterName");
        final int chapterPosition = chapterList.indexOf(chapterName);
        if (chapterName.length() > 14) {
            chapterTitle.setTextSize(18);
        } else {
            chapterTitle.setTextSize(21);
        }
        if (chapterPosition == chapterList.size() - 1) {
            nextChp.setVisibility(View.INVISIBLE);
            nextChp.setClickable(false);
        }
        if (chapterPosition == 0) {
            prevChp.setVisibility(View.INVISIBLE);
        }
        nextChp.setTypeface(poppins_bold);
        prevChp.setTypeface(poppins_bold);
        nextChp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextChp = new Intent(ChapterActivity.this, ChapterActivity.class);
                nextChp.putExtra("chapterKey", chapterKey);
                nextChp.putExtra("subject", "Biology");
                nextChp.putExtra("chapterName", chapterList.get(chapterPosition + 1));
                nextChp.putStringArrayListExtra("chapterList", (ArrayList<String>) chapterList);
                startActivity(nextChp);
                overridePendingTransition(R.anim.enter_chapter, R.anim.exit_chapter);
            }
        });
        prevChp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent prevChp = new Intent(ChapterActivity.this, ChapterActivity.class);
                prevChp.putExtra("chapterKey", chapterKey);
                prevChp.putExtra("subject", "Biology");
                prevChp.putExtra("chapterName", chapterList.get(chapterPosition - 1));
                prevChp.putStringArrayListExtra("chapterList", (ArrayList<String>) chapterList);
                startActivity(prevChp);
                overridePendingTransition(R.anim.enter_subject_chp, R.anim.exit_subject_chp);
            }
        });
        nightMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!night_mode) {
                    night_mode = true;
                    chapterLayout.setBackgroundColor(getResources().getColor(R.color.night_mode_background));
                    for (int i = 0; i < textViewList.size(); i++) {
                        TextView textView = textViewList.get(i);
                        textView.setTextColor(Color.parseColor("#FFFFFF"));
                    }
                } else {
                    night_mode = false;
                    chapterLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    for (int i = 0; i < textViewList.size(); i++) {
                        TextView textView = textViewList.get(i);
                        textView.setTextColor(getResources().getColor(R.color.material_black));
                    }
                }
            }
        });
        chapterTitle.setGravity(View.TEXT_ALIGNMENT_CENTER);
        chapterTitle.setText(chapterName);
        chapterTitle.setTypeface(poppins_bold);
        if (subject.equals("Biology")) {
            chapterToolbar.setBackgroundResource(R.drawable.biology_top);
            databaseReference = firebaseDatabase.getReference("biology_detailed_chapters/" + chapterKey);
            progressBar.setIndeterminateTintList(ColorStateList.valueOf(getResources().getColor(R.color.primary_biology)));
            bottomLayout.setBackgroundResource(R.drawable.biology_top);
            primaryColor = R.color.primary_biology;
        } else if (subject.equals("Physics")) {
            chapterToolbar.setBackgroundResource(R.drawable.physics_top);
            databaseReference = firebaseDatabase.getReference("physics_detailed_chapters/" + chapterKey);
            progressBar.setIndeterminateTintList(ColorStateList.valueOf(getResources().getColor(R.color.primary_physics)));
            primaryColor = R.color.primary_physics;
            bottomLayout.setBackgroundResource(R.drawable.physics_top);
        } else if (subject.equals("Chemistry")) {
            chapterToolbar.setBackgroundResource(R.drawable.chem_top);
            databaseReference = firebaseDatabase.getReference("chemistry_detailed_chapters/" + chapterKey);
            progressBar.setIndeterminateTintList(ColorStateList.valueOf(getResources().getColor(R.color.primary_chemistry)));
            bottomLayout.setBackgroundResource(R.drawable.chem_top);
            primaryColor = R.color.primary_chemistry;
        }
        poppins = Typeface.createFromAsset(getAssets(), "fonts/poppins.ttf");
        Log.d(TAG, "onCreate: dataRef" + databaseReference);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildAdded: dataSnapshot" + dataSnapshot);
                progressBar.setVisibility(View.GONE);
                if (!dataSnapshot.getKey().toLowerCase().contains("image")) {
                    final TextView textView = new TextView(getApplicationContext());
                    if (dataSnapshot.getKey().toLowerCase().contains("bullet")) {
                        textView.setText("\u25CF" + dataSnapshot.getValue(String.class));
                    } else {
                        textView.setText(dataSnapshot.getValue(String.class));
                    }
                    if (!night_mode) {
                        textView.setTextColor(getResources().getColor(R.color.night_mode_text));
                    } else {
                        textView.setTextColor(Color.parseColor("#383838"));
                    }
                    textView.setTypeface(poppins);
                    textView.setTextSize(14);
                    textView.setTextIsSelectable(true);
                    textView.setTextColor(getResources().getColor(R.color.material_black));
                    textView.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
                        @Override
                        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                            menu.removeItem(android.R.id.copy);
                            menu.removeItem(android.R.id.cut);
                            menu.removeItem(android.R.id.shareText);
                            menu.add("Highlight");
                            menu.add("Underline");
                            return true;
                        }

                        @Override
                        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                            return false;
                        }

                        @Override
                        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                            Log.d(TAG, "onActionItemClicked: " + menuItem.getTitle());
                            switch (menuItem.getTitle().toString()) {
                                case "Highlight": {
                                    int min = 0;
                                    int max = textView.getText().length();
                                    if (textView.isFocused()) {
                                        final int selStart = textView.getSelectionStart();
                                        final int selEnd = textView.getSelectionEnd();

                                        min = Math.max(0, Math.min(selStart, selEnd));
                                        max = Math.max(0, Math.max(selStart, selEnd));
                                    }
                                    String fullText = textView.getText().toString();
                                    Spannable spannable = new SpannableString(fullText);
                                    BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(primaryColor);
                                    spannable.setSpan(backgroundColorSpan, min, max, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    textView.setText(spannable);
                                    break;
                                }
                                case "Underline": {
                                    int min = 0;
                                    int max = textView.getText().length();
                                    if (textView.isFocused()) {
                                        final int selStart = textView.getSelectionStart();
                                        final int selEnd = textView.getSelectionEnd();

                                        min = Math.max(0, Math.min(selStart, selEnd));
                                        max = Math.max(0, Math.max(selStart, selEnd));
                                    }
                                    String fullText = textView.getText().toString();
                                    Spannable spannable = new SpannableString(fullText);
                                    UnderlineSpan underlineSpan = new UnderlineSpan();
                                    spannable.setSpan(underlineSpan, min, max, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    textView.setText(spannable);
                                    break;
                                }
                            }
                            return true;
                        }

                        @Override
                        public void onDestroyActionMode(ActionMode actionMode) {

                        }
                    });
                    textViewList.add(textView);
                    chapterLayout.addView(textView);
                    Log.d(TAG, "onChildAdded: " + dataSnapshot.getKey().toLowerCase().contains("table"));
                } else if (dataSnapshot.getKey().toLowerCase().contains("image")) {
                    ImageView imageView = new ImageView(getApplicationContext());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(10, 10, 10, 10);
                    layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
                    imageView.setLayoutParams(layoutParams);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    Glide.with(getApplicationContext()).load(dataSnapshot.getValue(String.class)).into(imageView);
                    chapterLayout.addView(imageView);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (subject.equals("Biology")) {
            Intent intent = new Intent(ChapterActivity.this, BiologyActivity.class);
            startActivity(intent);
        } else if (subject.equals("Physics")) {
            Intent intent = new Intent(ChapterActivity.this, PhysicsActivity.class);
            startActivity(intent);
        } else if (subject.equals("Chemistry")) {
            Intent intent = new Intent(ChapterActivity.this, ChemistryActivity.class);
            startActivity(intent);
        }
        overridePendingTransition(R.anim.enter_subject_chp, R.anim.exit_subject_chp);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                bottomLayout.setVisibility(View.GONE);
                chapterToolbar.setVisibility(View.GONE);
                Log.d(TAG, "onTouchEvent: down");
                break;
            case MotionEvent.ACTION_UP:
                bottomLayout.setVisibility(View.VISIBLE);
                chapterToolbar.setVisibility(View.VISIBLE);
                Log.d(TAG, "onTouchEvent: up");
                break;
        }
        return super.onTouchEvent(event);
    }
}
