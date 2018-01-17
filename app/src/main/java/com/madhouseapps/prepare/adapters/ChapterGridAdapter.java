package com.madhouseapps.prepare.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.madhouseapps.prepare.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hemant on 11/2/2017.
 */

public class ChapterGridAdapter extends BaseAdapter {
    private static final String TAG = "ChapterGridAdapter";
    private Context context;
    private List<String> chapterList;
    private List<String> searchList;
    private String subject;
    private Typeface poppins_bold;

    public ChapterGridAdapter(Context context, List<String> chapterList, String subject) {
        this.context = context;
        this.chapterList = chapterList;
        this.subject = subject;
        if (searchList == null) {
            searchList = new ArrayList<>(chapterList);
        }
        poppins_bold = Typeface.createFromAsset(context.getAssets(), "fonts/poppins_bold.ttf");
    }

    @Override
    public int getCount() {
        return chapterList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View gridView;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null) {
            gridView = layoutInflater.inflate(R.layout.chapter_grid_layout, viewGroup, false);
            LinearLayout linearLayout = gridView.findViewById(R.id.chapter_background_layout);
            if (subject.equals("physics")) {
                linearLayout.setBackgroundResource(R.drawable.ripple_physics_grid);
            } else if (subject.equals("biology")) {
                linearLayout.setBackgroundResource(R.drawable.ripple_biology_grid);
            } else {
                linearLayout.setBackgroundResource(R.drawable.ripple_chem_grid);
            }
            TextView textView = gridView.findViewById(R.id.chpName);
            textView.setText(chapterList.get(i).toUpperCase());
            Log.d(TAG, "getView: " + chapterList);
            Log.d(TAG, "getView: " + chapterList.get(i));
            textView.setTypeface(poppins_bold);
        } else {
            gridView = view;
        }
        return gridView;
    }

}
