package com.company.panels;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBManager extends SQLiteOpenHelper {

    public DBManager(Context context) {
        super(context, "Data.db", null, 2);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE boards (Name VARCHAR(16), IsPublic BOOLEAN, RestrictPosts BOOLEAN, Author VARCHAR(100) DEFAULT 'Unattributed');");
        db.execSQL("CREATE TABLE panels (Board VARCHAR(16), Time DATETIME DEFAULT CURRENT_TIMESTAMP, Author VARCHAR(100) DEFAULT 'Unattributed', Title VARCHAR(32), Content TEXT);");
        db.execSQL("CREATE TABLE temp (ID INT, Title VARCHAR(32), Board VARCHAR(16), Content Text, LayoutIsStream BOOLEAN);");
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", "1");
        contentValues.put("Title", "");
        contentValues.put("Board", "");
        contentValues.put("Content", "");
        db.insert("temp", null, contentValues);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS boards");
        db.execSQL("DROP TABLE IF EXISTS panels");
        db.execSQL("DROP TABLE IF EXISTS temp");
        onCreate(db);
    }
    //The method for creating a new board
    public void createBoard(String name, Boolean isPublic, Boolean restrictPosts) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", name);
        contentValues.put("IsPublic", isPublic);
        contentValues.put("RestrictPosts", restrictPosts);
        db.insert("boards", null, contentValues);
        ContentValues contentValuesB = new ContentValues();
        contentValuesB.put("Board", name);
        db.update("temp", contentValuesB, "ID = ?", new String[]{"1"});
    }
    //Checking whether or not a board name has been taken
    public Cursor checkBoardAvailable(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT Name FROM boards WHERE Name ='" + title + "'",null);
        return res;
    }
    //The method for creating a panel
    public void createPanel(String board, String author, String title, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Board", board);
        contentValues.put("Author", author);
        contentValues.put("Title", title);
        contentValues.put("Content", content);
        db.insert("panels", null, contentValues);
    }
    //Storing Temp values for panel drafts
    public void storeTemp(String title, String board, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", 1);
        contentValues.put("Title", title);
        contentValues.put("Board", board);
        contentValues.put("Content", content);
        db.update("temp", contentValues, "ID = ?", new String[]{"1"});
    }
    //retrieving draft values from temp
    public Cursor retrieveTemp() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM temp",null);
        return res;
    }
    public void layoutChange(Boolean isStream) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", 1);
        contentValues.put("LayoutIsStream", isStream);
        db.update("temp", contentValues, "ID = ?", new String[]{"1"});
    }
}



