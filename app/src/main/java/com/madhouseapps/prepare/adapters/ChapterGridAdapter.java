package com.madhouseapps.prepare.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.madhouseapps.prepare.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Hemant on 11/2/2017.
 */

public class ChapterGridAdapter extends BaseAdapter implements Filterable {
    private static final String TAG = "ChapterGridAdapter";
    private Context context;
    private List<String> chapterList;
    private List<String> searchList;
    private String subject;
    private Typeface poppins_bold;
    private ChapterFilter chapterFilter;

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
            if (subject.equals("Physics")) {
                linearLayout.setBackgroundResource(R.drawable.physics_grid);
            } else if (subject.equals("Biology")) {
                linearLayout.setBackgroundResource(R.drawable.bio_grid);
            } else {
                linearLayout.setBackgroundResource(R.drawable.chem_grid);
            }
            TextView textView = gridView.findViewById(R.id.chpName);
            textView.setText(chapterList.get(i));
            Log.d(TAG, "getView: " + chapterList);
            Log.d(TAG, "getView: " + chapterList.get(i));
            textView.setTypeface(poppins_bold);
        } else {
            gridView = view;
        }
        return gridView;
    }

    @Override
    public Filter getFilter() {
        if (chapterFilter == null) {
            chapterFilter = new ChapterFilter();
        }
        return chapterFilter;
    }

    private class ChapterFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults filterResults = new FilterResults();
            List<String> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filterResults.count = searchList.size();
                filterResults.values = searchList;
            } else {
                charSequence = charSequence.toString().toLowerCase(Locale.getDefault());
                for (int i = 0; i < searchList.size(); i++) {
                    String data = searchList.get(i);
                    if (data.toLowerCase().contains(charSequence.toString())) {
                        filteredList.add(searchList.get(i));
                    }
                }
                Log.d(TAG, "performFiltering: filter List" + filteredList);
                filterResults.count = filteredList.size();
                filterResults.values = filteredList;
                Log.d(TAG, "performFiltering: filter Result" + filterResults.values);
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            chapterList = (List<String>) filterResults.values;
            Log.d(TAG, "publishResults: chapter List" + chapterList);
            notifyDataSetChanged();
        }
    }
}
