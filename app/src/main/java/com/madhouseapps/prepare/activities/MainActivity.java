package com.madhouseapps.prepare.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.madhouseapps.prepare.R;

public class MainActivity extends AppCompatActivity {
    private LinearLayout shareButton;
    private TextView preTxt, pareTxt, byTxt, shareTxt;
    private TextView physicsTxt, physicsChps, bioTxt, bioChps, chemTxt, chemChps;
    private Typeface poppins, poppins_light, ench_celeb, poppins_bold;
    private CardView physics, chem, bio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        shareButton = findViewById(R.id.share_button);
        preTxt = findViewById(R.id.txt_pre);
        pareTxt = findViewById(R.id.txt_pare);
        byTxt = findViewById(R.id.txt_by);
        shareTxt = findViewById(R.id.txt_share);
        physicsTxt = findViewById(R.id.txt_phy);
        physicsChps = findViewById(R.id.txt_phy_chp);
        bioTxt = findViewById(R.id.txt_bio);
        bioChps = findViewById(R.id.txt_bio_chp);
        chemTxt = findViewById(R.id.txt_chem);
        chemChps = findViewById(R.id.txt_chem_chp);
        physics = findViewById(R.id.physics_layout);
        chem = findViewById(R.id.chem_layout);
        bio = findViewById(R.id.biology_layout);
        poppins = Typeface.createFromAsset(getAssets(), "fonts/poppins.ttf");
        poppins_light = Typeface.createFromAsset(getAssets(), "fonts/poppins_light.ttf");
        poppins_bold = Typeface.createFromAsset(getAssets(), "fonts/poppins_bold.ttf");
        ench_celeb = Typeface.createFromAsset(getAssets(), "fonts/enchanting_celebrations.ttf");
        preTxt.setTypeface(poppins_light);
        pareTxt.setTypeface(poppins);
        shareTxt.setTypeface(poppins_bold);
        byTxt.setTypeface(ench_celeb);
        physicsTxt.setTypeface(poppins_bold);
        physicsChps.setTypeface(poppins_bold);
        bioTxt.setTypeface(poppins_bold);
        bioChps.setTypeface(poppins_bold);
        chemTxt.setTypeface(poppins_bold);
        chemChps.setTypeface(poppins_bold);
        physics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PhysicsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_subject, R.anim.exit_subject);
            }
        });
        bio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BiologyActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_subject, R.anim.exit_subject);
            }
        });

        chem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ChemistryActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_subject, R.anim.exit_subject);
            }
        });
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String shareMessage = "Check out this amazing app to score more in your medical exams!!";
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(share, "Share Prepare with everyone"));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPref", 0);
        if (sharedPreferences.getBoolean("LoggedIn", false)) {
            finish();
            System.exit(0);
        }
    }
}
