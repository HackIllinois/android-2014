package org.hackillinois.android.database;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.provider.BaseColumns;
import android.util.Log;

import org.hackillinois.android.models.people.Hacker;
import org.hackillinois.android.models.people.Mentor;
import org.hackillinois.android.models.people.Staff;

import java.util.HashMap;

public class DatabaseTable {
    private static final String TAG = "DatabaseTable";

    public static final String COL_NAME = SearchManager.SUGGEST_COLUMN_TEXT_1;
    public static final String COL_EMAIL = SearchManager.SUGGEST_COLUMN_TEXT_2;
    public static final String COL_ID_MINE = "MY_ID";
    public static final String COL_SCHOOL = "SCHOOL";
    public static final String COL_YEAR = "YEAR";
    public static final String COL_COMPANY = "COMPANY";
    public static final String COL_JOB_TITLE = "JOB_TITLE";

    private static final String DATABASE_NAME = "PEOPLE";
    private static final String FTS_VIRTUAL_TABLE_STAFF = "FTS_STAFF";
    private static final String FTS_VIRTUAL_TABLE_MENTOR = "FTS_MENTOR";
    private static final String FTS_VIRTUAL_TABLE_HACKER = "FTS_HACKER";

    private static final int DATABASE_VERSION = 6;

    private final DatabaseOpenHelper mDatabaseOpenHelper;
    private static final HashMap<String,String> mColumnMap = buildColumnMap();

    public DatabaseTable(Context context) {
        mDatabaseOpenHelper = new DatabaseOpenHelper(context);
    }

    private static HashMap<String,String> buildColumnMap() {
        HashMap<String,String> map = new HashMap<String,String>();
        map.put(COL_NAME, COL_NAME);
        map.put(COL_EMAIL, COL_EMAIL);
        map.put(COL_ID_MINE, COL_ID_MINE);
        map.put(BaseColumns._ID, "rowid AS " +
                BaseColumns._ID);
        map.put(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID, "rowid AS " +
                SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID);
        map.put(SearchManager.SUGGEST_COLUMN_SHORTCUT_ID, "rowid AS " +
                SearchManager.SUGGEST_COLUMN_SHORTCUT_ID);
        return map;
    }

    private static class DatabaseOpenHelper extends SQLiteOpenHelper {

        private final Context mHelperContext;
        private SQLiteDatabase mDatabase;

        private static final String FTS_TABLE_CREATE_STAFF =
                "CREATE VIRTUAL TABLE " + FTS_VIRTUAL_TABLE_STAFF +
                        " USING fts3 (" +
                        COL_ID_MINE + ", " +
                        COL_NAME + ", " +
                        COL_EMAIL + ", " +
                        COL_COMPANY + ", " +
                        COL_JOB_TITLE + ", " +
                        COL_YEAR + ")";

        private static final String FTS_TABLE_CREATE_HACKER =
                "CREATE VIRTUAL TABLE " + FTS_VIRTUAL_TABLE_HACKER +
                        " USING fts3 (" +
                        COL_ID_MINE + ", " +
                        COL_NAME + ", " +
                        COL_EMAIL + ", " +
                        COL_SCHOOL + ", " +
                        COL_YEAR + ")";

        private static final String FTS_TABLE_CREATE_MENTOR =
                "CREATE VIRTUAL TABLE " + FTS_VIRTUAL_TABLE_MENTOR +
                        " USING fts3 (" +
                        COL_ID_MINE + ", " +
                        COL_NAME + ", " +
                        COL_EMAIL + ", " +
                        COL_JOB_TITLE + ", " +
                        COL_COMPANY + ")";

        DatabaseOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            mHelperContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            mDatabase = db;
            mDatabase.execSQL(FTS_TABLE_CREATE_STAFF);
            mDatabase.execSQL(FTS_TABLE_CREATE_HACKER);
            mDatabase.execSQL(FTS_TABLE_CREATE_MENTOR);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE_STAFF);
            db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE_HACKER);
            db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE_MENTOR);
            onCreate(db);
        }
    }

    public long addHacker(Hacker hacker) {
        SQLiteDatabase sqLiteDatabase = mDatabaseOpenHelper.getWritableDatabase();
        assert sqLiteDatabase != null;
        ContentValues contentValues = new ContentValues();
        String where = COL_ID_MINE + "=" + hacker.getDatabaseKey();
        contentValues.put(COL_ID_MINE, hacker.getDatabaseKey());
        contentValues.put(COL_NAME, hacker.getName());
        contentValues.put(COL_EMAIL, hacker.getEmail());
        contentValues.put(COL_SCHOOL, hacker.getSchool());
        contentValues.put(COL_YEAR, hacker.getYear());
        try {
            if (sqLiteDatabase.update(FTS_VIRTUAL_TABLE_HACKER, contentValues, where, null) == 0) {
                return sqLiteDatabase.insert(FTS_VIRTUAL_TABLE_HACKER, null, contentValues);
            } else {
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        sqLiteDatabase.close();
        return 1;
    }

    public long addStaff(Staff staff) {
        SQLiteDatabase sqLiteDatabase = mDatabaseOpenHelper.getWritableDatabase();
        assert sqLiteDatabase != null;
        String where = COL_ID_MINE + "=" + staff.getDatabaseKey();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID_MINE, staff.getDatabaseKey());
        contentValues.put(COL_NAME, staff.getName());
        contentValues.put(COL_EMAIL, staff.getEmail());
        contentValues.put(COL_YEAR, staff.getYear());
        contentValues.put(COL_COMPANY, staff.getCompany());
        contentValues.put(COL_JOB_TITLE, staff.getJobTitle());
        try {
            if (sqLiteDatabase.update(FTS_VIRTUAL_TABLE_STAFF, contentValues, where, null) == 0) {
                return sqLiteDatabase.insert(FTS_VIRTUAL_TABLE_STAFF, null, contentValues);
            } else {
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        sqLiteDatabase.close();
        return 1;
    }

    public long addMentor(Mentor mentor) {
        SQLiteDatabase sqLiteDatabase = mDatabaseOpenHelper.getWritableDatabase();
        assert sqLiteDatabase != null;
        String where = COL_ID_MINE + "=" + mentor.getDatabaseKey();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID_MINE, mentor.getDatabaseKey());
        contentValues.put(COL_NAME, mentor.getName());
        contentValues.put(COL_EMAIL, mentor.getEmail());
        contentValues.put(COL_COMPANY, mentor.getCompany());
        contentValues.put(COL_JOB_TITLE, mentor.getJobTitle());
        try {
            if (sqLiteDatabase.update(FTS_VIRTUAL_TABLE_MENTOR, contentValues, where, null) == 0) {
                return sqLiteDatabase.insert(FTS_VIRTUAL_TABLE_MENTOR, null, contentValues);
            } else {
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        sqLiteDatabase.close();
        return 1;
    }

    public Cursor getHackerMatches(String query, String[] columns) {
        return getMatches(query, columns, FTS_VIRTUAL_TABLE_HACKER);
    }
    public Cursor getStaffMatches(String query, String[] columns) {
        return getMatches(query, columns, FTS_VIRTUAL_TABLE_STAFF);
    }
    public Cursor getMentorMatches(String query, String[] columns) {
        return getMatches(query, columns, FTS_VIRTUAL_TABLE_MENTOR);
    }
    public Cursor getMatches(String query, String[] columns, String table) {
        String selection = COL_NAME + " MATCH ?";
        String[] selectionArgs = new String[] {query+"*"};
        return query(selection, selectionArgs, columns, table);
    }

    public Cursor getUserByID(String id) {
        String selection = BaseColumns._ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(id)};
        return query(selection, selectionArgs, null, FTS_VIRTUAL_TABLE_MENTOR);
    }

    private Cursor query(String selection, String[] selectionArgs, String[] columns, String table) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(table);
        builder.setProjectionMap(mColumnMap);
        Cursor cursor = builder.query(mDatabaseOpenHelper.getReadableDatabase(),
                columns, selection, selectionArgs, null, null, null);

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }
}
