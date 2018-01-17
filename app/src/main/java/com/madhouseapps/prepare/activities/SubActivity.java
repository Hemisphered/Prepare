package com.madhouseapps.prepare.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.folioreader.util.FolioReader;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.madhouseapps.prepare.R;
import com.madhouseapps.prepare.adapters.CustomExpandableListAdapter;
import com.madhouseapps.prepare.adapters.ProgressDialogAdapter;
import com.madhouseapps.prepare.fragments.SubjectFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private static final String TAG = "SubActivity";
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    private String[] items;
    private String subject;
    private boolean drawerClosed = true;

    private ExpandableListView mExpandableListView;
    private ExpandableListAdapter mExpandableListAdapter;
    private List<String> mExpandableListTitle;
    private List<String> biologyList = new ArrayList<>();
    private List<String> chemList = new ArrayList<>();
    private List<String> physicsList = new ArrayList<>();


    private Map<String, List<String>> mExpandableListData = new HashMap<>();
    private ProgressDialogAdapter progressDialogAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        Intent intent = getIntent();
        subject = intent.getStringExtra("Subject");
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.subTitle);
        title.setText(subject.toUpperCase());
        Typeface poppins_bold = Typeface.createFromAsset(getAssets(), "fonts/poppins_bold.ttf");
        Typeface poppins = Typeface.createFromAsset(getAssets(), "fonts/poppins.ttf");
        title.setTypeface(poppins_bold);
        ImageButton navigation = findViewById(R.id.navigation);

        Bundle bundle = new Bundle();
        bundle.putString("Subject", subject);
        SubjectFragment subjectFragment = new SubjectFragment();
        subjectFragment.setArguments(bundle);
        FragmentManager fragmentManager =
                getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.container, subjectFragment)
                .commit();
        initItems();
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        mExpandableListTitle = new ArrayList<>(mExpandableListData.keySet());
        mExpandableListView = findViewById(R.id.navList);
        addDrawerItems();
        setupDrawer();
        navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerClosed) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                    drawerClosed = false;
                } else {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    drawerClosed = true;
                }
            }
        });
        if (savedInstanceState == null) {
        }
        TextView home = findViewById(R.id.home);
        TextView bookmark = findViewById(R.id.bookmarks);
        TextView rate = findViewById(R.id.share);
        TextView share = findViewById(R.id.rate);
        home.setTypeface(poppins);
        bookmark.setTypeface(poppins);
        rate.setTypeface(poppins);
        share.setTypeface(poppins);
        home.setTextSize(18);
        bookmark.setTextSize(18);
        rate.setTextSize(18);
        share.setTextSize(18);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SubActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }


    private void initItems() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("biology_chapters");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildAdded: dataSnapshot" + dataSnapshot);
                if (!biologyList.contains(dataSnapshot.getValue(String.class)))
                    biologyList.add(dataSnapshot.getValue(String.class));
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
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("chemistry_chapters");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildAdded: dataSnapshot" + dataSnapshot);
                if (!chemList.contains(dataSnapshot.getValue(String.class)))
                    chemList.add(dataSnapshot.getValue(String.class));
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
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("physics_chapters");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildAdded: dataSnapshot" + dataSnapshot);
                if (!physicsList.contains(dataSnapshot.getValue(String.class)))
                    physicsList.add(dataSnapshot.getValue(String.class));
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
        mExpandableListData.put("Biology", biologyList);
        mExpandableListData.put("Chemistry", chemList);
        mExpandableListData.put("Physics", physicsList);
    }

    private void addDrawerItems() {
        mExpandableListAdapter = new CustomExpandableListAdapter(this, mExpandableListTitle, mExpandableListData);
        mExpandableListView.setAdapter(mExpandableListAdapter);
        mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
            }
        });

        mExpandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
            }
        });

        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                String selectedItem = ((List) (mExpandableListData.get(mExpandableListTitle.get(groupPosition))))
                        .get(childPosition).toString();
                Log.d(TAG, "onChildClick: " + selectedItem);
                final String chapterName = selectedItem;
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference(subject + "_detailed_chapters/" + selectedItem.replace(" ", ""));
                databaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.d(TAG, "onChildAdded: " + dataSnapshot);
                        String fileURL = dataSnapshot.getValue(String.class);
                        Log.d(TAG, "onChildAdded: " + fileURL);
                        displayPDF(fileURL, chapterName);
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
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, 0, 0) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void displayPDF(String fileURL, String fileName) {
        Log.d(TAG, "displayPDF: " + fileURL);
        new DownloadFile().execute(fileURL, fileName);
    }

    private class DownloadFile extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];
            String fileName = strings[1];
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialogAdapter = new ProgressDialogAdapter(SubActivity.this);
                    progressDialogAdapter.showDialog();
                }
            });
            File pdfFile = new File(getApplicationContext().getFilesDir(), fileName);
            try {
                pdfFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fileUrl != null) {
                    Log.d(TAG, "doInBackground: file" + fileUrl);
                    URL url = new URL(fileUrl);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setDoOutput(true);
                    urlConnection.connect();

                    InputStream inputStream = urlConnection.getInputStream();
                    FileOutputStream fileOutputStream = getApplicationContext().openFileOutput(fileName, Context.MODE_PRIVATE);
                    int totalSize = urlConnection.getContentLength();

                    byte[] buffer = new byte[1024 * 1024];
                    int bufferLength;
                    while ((bufferLength = inputStream.read(buffer)) > 0) {
                        fileOutputStream.write(buffer, 0, bufferLength);
                    }
                    fileOutputStream.close();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialogAdapter.hideDialog();
                }
            });
            FolioReader folioReader = new FolioReader(SubActivity.this);
            Log.d(TAG, "doInBackground: " + pdfFile.getPath());
            folioReader.openBook(pdfFile.getPath());
            return null;
        }
    }
}
