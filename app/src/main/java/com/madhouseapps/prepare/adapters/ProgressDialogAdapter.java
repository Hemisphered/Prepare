package com.madhouseapps.prepare.adapters;

import android.app.Dialog;
import android.content.Context;

import com.madhouseapps.prepare.R;

/**
 * Created by HEMANT on 16-01-2018.
 */

public class ProgressDialogAdapter {
    private Context context;
    private Dialog dialog;

    public ProgressDialogAdapter(Context context) {
        this.context = context;
        dialog = new Dialog(context);

    }

    public void showDialog() {
        dialog.setContentView(R.layout.progress_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    public void hideDialog() {
        dialog.dismiss();
    }
}
