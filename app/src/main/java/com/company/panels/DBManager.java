package com.company.panels;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBManager extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Boards.db";
    public static final String TABLE_NAME = "board_settings";
    public static final String COL1 = "NAME";
    public static final String COL2 = "IS_PUBLIC";
    public static final String COL3 = "RESTRICT_POSTS";


    public DBManager(Context context) {
        super(context, DATABASE_NAME, null, 1);
        Log.d("Stuff", "5");
        SQLiteDatabase db = this.getWritableDatabase();
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("Stuff","1");
        db.execSQL("DROP TABLE IF EXISTS board_settings");
        db.execSQL("CREATE TABLE board_settings (NAME VARCHAR(16), IS_PUBLIC BOOLEAN, RESTRICT_POSTS BOOLEAN);");
        Log.d("Stuff", "2");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXITS" + TABLE_NAME);
        onCreate(db);
    }
}
