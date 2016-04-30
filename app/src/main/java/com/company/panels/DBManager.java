package com.company.panels;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBManager extends SQLiteOpenHelper {

    public DBManager(Context context) {
        super(context, "Data.db", null, 17);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE boards (_id INTEGER PRIMARY KEY, Name VARCHAR(16), IsPublic BOOLEAN, RestrictPosts BOOLEAN, Owner VARCHAR(100) DEFAULT 'Unattributed', Panels INT DEFAULT 0, Members INT DEFAULT 1, Time DATETIME DEFAULT CURRENT_TIMESTAMP);");
        db.execSQL("CREATE TABLE panels (_id INTEGER PRIMARY KEY, Board VARCHAR(16), Time DATETIME DEFAULT CURRENT_TIMESTAMP, Author VARCHAR(100) DEFAULT 'Unattributed', Title VARCHAR(32), Content TEXT, Comments INT);");
        db.execSQL("CREATE TABLE comments (_id INTEGER PRIMARY KEY, PanelID INT, Time DATETIME DEFAULT CURRENT_TIMESTAMP, Author VARCHAR(100) DEFAULT 'Unattributed', Content TEXT);");
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
        db.execSQL("DROP TABLE IF EXISTS comments");
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
        contentValues.put("Comments", 0);
        db.execSQL("UPDATE boards SET Panels = Panels + 1 WHERE NAME = '" + board + "'");
        db.insert("panels", null, contentValues);
    }
    // TODO THIS IS UNFINISHED! DO THIS IMMEDIATELY
    // The method for creating a comment
    public void createComment(String panel, String author, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("PanelID", panel);
        contentValues.put("Author", author);
        contentValues.put("Content", content);
        db.execSQL("UPDATE panels SET Comments = Comments + 1 WHERE PanelID = '" + panel + "'");
        db.insert("comments", null, contentValues);
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
        return db.rawQuery("SELECT * FROM temp",null);
    }
    //Retrieving data from panels to place in panels
    public Cursor getAllPanels(String sortBY) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] retrieveRows = new String[] {"_id","Title","Author","Content","Time","Comments"};
        Cursor c = db.query(true,"panels",retrieveRows,null,null,null,null,sortBY,null);
        if (c != null){
            c.moveToFirst();
        }
        return c;
    }
    //retrieving specific panels for panel view
    public Cursor retrievePanel(String panelID) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM panels WHERE _id ="+ panelID,null);
    }
    public Cursor getAllBoards(String sortBY) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] retrieveRows = new String[] {"_id","Name","Panels","Members","Time"};
        Cursor c = db.query(true,"boards",retrieveRows,null,null,null,null,sortBY,null);
        if (c != null){
            c.moveToFirst();
        }
        return c;
        //return db.rawQuery("select * from panels",null);
    }
}



