package com.madhouseapps.prepare.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.folioreader.util.FolioReader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.madhouseapps.prepare.R;
import com.madhouseapps.prepare.adapters.BookmarksListViewAdapter;
import com.madhouseapps.prepare.adapters.ProgressDialogAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BookmarkActivity extends AppCompatActivity {
    private List<String> bookmaksList = new ArrayList<>();
    private List<String> subjectList = new ArrayList<>();
    private static final String TAG = "BookmarkActivity";
    private File pdfFile;
    private String subject;
    private ProgressDialogAdapter progressDialogAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Bookmarks", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        bookmaksList = gson.fromJson(sharedPreferences.getString("bookmarksList", ""), ArrayList.class);
        subjectList = gson.fromJson(sharedPreferences.getString("subjectList", ""), ArrayList.class);
        if (bookmaksList.isEmpty() || subjectList.isEmpty()) {

        } else {
            ListView listView = findViewById(R.id.bookmarksList);
            BookmarksListViewAdapter bookmarksListViewAdapter = new BookmarksListViewAdapter(getApplicationContext(), bookmaksList, subjectList);
            listView.setAdapter(bookmarksListViewAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    final String chapterName = bookmaksList.get(i);
                    subject = subjectList.get(i);
                    DatabaseReference databaseReference = firebaseDatabase.getReference(subjectList.get(i) + "_detailed_chapters/" + bookmaksList.get(i).replace(" ", ""));
                    databaseReference.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Log.d(TAG, "onChildAdded: " + dataSnapshot);
                            String fileURL = dataSnapshot.getValue(String.class);
                            Log.d(TAG, "onChildAdded: " + fileURL);
                            displayPDF(fileURL, chapterName,subject);
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
            });
        }
    }

    private void displayPDF(String fileURL, String fileName, String subject) {
        Log.d(TAG, "displayPDF: " + fileURL);
        Intent intent = new Intent(BookmarkActivity.this, SubActivity.class);
        intent.putExtra("Activity", "Bookmarks");
        intent.putExtra("fileName", fileName);
        intent.putExtra("fileURL", fileURL);
        intent.putExtra("Subject",subject);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (progressDialogAdapter != null)
            progressDialogAdapter.hideDialog();
    }

    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
