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

public class PhysicsActivity extends AppCompatActivity {

    private static final String TAG = "PhysicsActivity";
    private GridView gridView;
    private ChapterGridAdapter chapterGridAdapter;
    private TextView physicsTitle;
    private EditText physicsSearch;
    private Typeface poppins_bold, poppins;
    private List<String> chapterList = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physics);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        progressBar = findViewById(R.id.physics_progress);
        gridView = findViewById(R.id.physics_grid);
        physicsTitle = findViewById(R.id.physics_title);
        physicsSearch = findViewById(R.id.physics_search);
        physicsSearch.setTypeface(poppins_bold);
        poppins_bold = Typeface.createFromAsset(getAssets(), "fonts/poppins_bold.ttf");
        poppins = Typeface.createFromAsset(getAssets(), "fonts/poppins.ttf");
        physicsSearch.setTypeface(poppins);
        physicsTitle.setTypeface(poppins_bold);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("physics_chapters");
        Log.d(TAG, "onCreate: databaseRef" + databaseReference);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildAdded: dataSnapshot" + dataSnapshot);
                chapterList.add(dataSnapshot.getValue(String.class));
                progressBar.setVisibility(View.GONE);
                chapterGridAdapter.notifyDataSetChanged();
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
        chapterGridAdapter = new ChapterGridAdapter(getApplicationContext(), chapterList, "Physics");
        gridView.setAdapter(chapterGridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String chapterKey = chapterList.get(i).toString().replace(" ", "").replace(",", "");
                Intent intent = new Intent(PhysicsActivity.this, ChapterActivity.class);
                intent.putExtra("chapterKey", chapterKey);
                intent.putExtra("subject", "Physics");
                intent.putExtra("chapterName", chapterList.get(i));
                intent.putStringArrayListExtra("chapterList", (ArrayList<String>) chapterList);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_chapter, R.anim.exit_chapter);
            }
        });
        //search function
        physicsSearch.addTextChangedListener(new TextWatcher() {
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
