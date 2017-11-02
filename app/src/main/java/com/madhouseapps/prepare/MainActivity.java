package com.madhouseapps.prepare;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView preTxt, pareTxt, byTxt, shareTxt;
    private TextView physicsTxt, physicsChps;
    private Typeface poppins, poppins_light, ench_celeb, poppins_bold;
    private FrameLayout physics, chem, bio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preTxt = findViewById(R.id.txt_pre);
        pareTxt = findViewById(R.id.txt_pare);
        byTxt = findViewById(R.id.txt_by);
        shareTxt = findViewById(R.id.txt_share);
        physicsTxt = findViewById(R.id.txt_phy);
        physicsChps = findViewById(R.id.txt_phy_chp);
        physics = findViewById(R.id.physics_layout);
        chem = findViewById(R.id.chem_layout);
        bio = findViewById(R.id.biology_layout);
        poppins = Typeface.createFromAsset(getAssets(), "fonts/poppins.ttf");
        poppins_light = Typeface.createFromAsset(getAssets(), "fonts/poppins_light.ttf");
        poppins_bold = Typeface.createFromAsset(getAssets(), "fonts/poppins_bold.ttf");
        ench_celeb = Typeface.createFromAsset(getAssets(), "fonts/enchanting_celebrations.ttf");
        preTxt.setTypeface(poppins_light);
        pareTxt.setTypeface(poppins);
        shareTxt.setTypeface(poppins);
        byTxt.setTypeface(ench_celeb);
        physicsTxt.setTypeface(poppins_bold);
        physicsChps.setTypeface(poppins_bold);
    }
}
