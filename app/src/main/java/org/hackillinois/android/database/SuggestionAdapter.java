package org.hackillinois.android.database;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;

import org.hackillinois.android.R;

public class SuggestionAdapter extends SimpleCursorAdapter {

    private static final String [] from = new String[] {DatabaseTable.COL_NAME,
            DatabaseTable.COL_EMAIL};
    private static final int[] to = new int[] {R.id.name, R.id.email};

    public SuggestionAdapter(Context context, int layout, Cursor c, int flags) {
        super(context, layout, c, from, to, flags);
    }
}
