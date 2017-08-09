package com.madao.datastorage;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

public class SQLActivity extends Activity {
    private DbOpenHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDbHelper = new DbOpenHelper(this);
    }

    public void insert(String title, String subtitle) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbEntry.COLUMN_NAME_TITLE, title);
        values.put(DbEntry.COLUMN_NAME_SUBTITLE, subtitle);

        long newRowId = db.insert(DbEntry.TABLE_NAME, null, values);
    }

    public void read() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                DbEntry._ID,
                DbEntry.COLUMN_NAME_TITLE,
                DbEntry.COLUMN_NAME_SUBTITLE
        };

        String selection = DbEntry.COLUMN_NAME_TITLE + " = ?";
        String[] selectionArgs = { "My Title" };

        String sortOrder =
                DbEntry.COLUMN_NAME_SUBTITLE + " DESC";

        Cursor c = db.query(
                DbEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        c.moveToFirst();
        long itemId = c.getLong(
                c.getColumnIndexOrThrow(DbEntry._ID)
        );
    }

    public void delete() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String selection = DbEntry.COLUMN_NAME_TITLE + " LIKE ?";
        String[] selectionArgs = { "My Title" };

        db.delete(DbEntry.TABLE_NAME, selection, selectionArgs);
    }

    public void update(String title) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbEntry.COLUMN_NAME_TITLE, title);

        String selection = DbEntry.COLUMN_NAME_TITLE + " LIKE ?";
        String[] selectionArgs = { "MyTitle" };

        int count = db.update(
                DbEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
    }

    static class DbEntry {
        private static final String TEXT_TYPE = " TEXT";
        private static final String COMMA_SEP = ",";

        private static final String _ID = "id";

        private static final String TABLE_NAME = "entry";
        private static final String COLUMN_NAME_TITLE = "title";
        private static final String COLUMN_NAME_SUBTITLE = "subtitle";
    }

    static class DbOpenHelper extends SQLiteOpenHelper {

        private static final int DATABASE_VERSION = 2;
        private static final String DATABASE_NAME = "madao.db";


        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + DbEntry.TABLE_NAME + " (" +
                        DbEntry._ID + " INTEGER PRIMARY KEY" + DbEntry.COMMA_SEP +
                        DbEntry.COLUMN_NAME_TITLE + DbEntry.TEXT_TYPE + DbEntry.COMMA_SEP +
                        DbEntry.COLUMN_NAME_SUBTITLE + DbEntry.TEXT_TYPE + " )";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + DbEntry.TABLE_NAME;

        DbOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }
}
