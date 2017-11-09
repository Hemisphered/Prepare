package com.madhouseapps.prepare.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.madhouseapps.prepare.R;
import com.madhouseapps.prepare.adapters.ChapterGridAdapter;

import java.util.ArrayList;
import java.util.List;

public class BiologyActivity extends AppCompatActivity {

    private static final String TAG = "BiologyActivity";
    private GridView gridView;
    private ChapterGridAdapter chapterGridAdapter;
    private TextView biologyTitle;
    private EditText biologySearch;
    private Typeface poppins_bold, poppins;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar;
    private List<String> chapterList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biology);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        gridView = findViewById(R.id.biology_grid);
        progressBar = findViewById(R.id.biology_progress);
        biologyTitle = findViewById(R.id.bio_title);
        biologySearch = findViewById(R.id.biology_search);
        biologySearch.setTypeface(poppins_bold);
        poppins_bold = Typeface.createFromAsset(getAssets(), "fonts/poppins_bold.ttf");
        poppins = Typeface.createFromAsset(getAssets(), "fonts/poppins.ttf");
        biologySearch.setTypeface(poppins);
        biologyTitle.setTypeface(poppins_bold);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("biology_chapters");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildAdded: dataSnapshot" + dataSnapshot);
                chapterList.add(dataSnapshot.getValue(String.class));
                progressBar.setVisibility(View.GONE);
                chapterGridAdapter.notifyDataSetChanged();
                Log.d(TAG, "onChildAdded: " + chapterList);
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
        chapterGridAdapter = new ChapterGridAdapter(getApplicationContext(), chapterList, "Biology");
        gridView.setAdapter(chapterGridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String chapterKey = chapterList.get(i).toString().replace(" ", "").replace(",", "");
                Intent intent = new Intent(BiologyActivity.this, ChapterActivity.class);
                intent.putExtra("chapterKey", chapterKey);
                intent.putExtra("subject", "Biology");
                intent.putExtra("chapterName", chapterList.get(i));
                intent.putStringArrayListExtra("chapterList", (ArrayList<String>) chapterList);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_chapter, R.anim.exit_chapter);
            }
        });
        //search function
        biologySearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                chapterGridAdapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_main, R.anim.exit_main);
    }
}
