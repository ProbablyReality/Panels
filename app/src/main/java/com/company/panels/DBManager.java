package com.company.panels;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBManager extends SQLiteOpenHelper {

    public DBManager(Context context) {
        super(context, "Data.db", null, 18);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE boards (_id INTEGER PRIMARY KEY, Name VARCHAR(16), IsPublic BOOLEAN, RestrictPosts BOOLEAN, Owner VARCHAR(100) DEFAULT 'Unattributed', Panels INT DEFAULT 0, Members INT DEFAULT 1, Time DATETIME DEFAULT CURRENT_TIMESTAMP);");
        db.execSQL("CREATE TABLE panels (_id INTEGER PRIMARY KEY, Board VARCHAR(16), Time DATETIME DEFAULT CURRENT_TIMESTAMP, Author VARCHAR(100) DEFAULT 'Unattributed', Title VARCHAR(32), Content TEXT, Comments INT);");
        db.execSQL("CREATE TABLE comments (_id INTEGER PRIMARY KEY, PanelID INT, Time DATETIME DEFAULT CURRENT_TIMESTAMP, Author VARCHAR(100) DEFAULT 'Unattributed', Content TEXT);");
        db.execSQL("CREATE TABLE users (_id INTEGER PRIMARY KEY, Name VARCHAR(16), Time DATETIME DEFAULT CURRENT_TIMESTAMP );");
        db.execSQL("CREATE TABLE user (UserID INTEGER);");
        db.execSQL("CREATE TABLE temp (ID INT, Title VARCHAR(32), Board VARCHAR(16), Content Text, LayoutIsStream BOOLEAN);");
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", "1");
        contentValues.put("Title", "");
        contentValues.put("Board", "");
        contentValues.put("Content", "");
        db.insert("temp", null, contentValues);
        db.execSQL("INSERT INTO user VALUES (-1)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS boards");
        db.execSQL("DROP TABLE IF EXISTS panels");
        db.execSQL("DROP TABLE IF EXISTS comments");
        db.execSQL("DROP TABLE IF EXISTS temp");
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS user");
        onCreate(db);
    }
    //The method for creating a new board
    public void createBoard(String name, Boolean isPublic, Boolean restrictPosts,String owner) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", name);
        contentValues.put("IsPublic", isPublic);
        contentValues.put("RestrictPosts", restrictPosts);
        contentValues.put("Owner", owner);
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
    // The method for creating a comment
    public void createComment(String panel, String author, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("PanelID", panel);
        contentValues.put("Author", author);
        contentValues.put("Content", content);
        db.execSQL("UPDATE panels SET Comments = Comments + 1 WHERE _id = '" + panel + "'");
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
    public Cursor getComments(String sortBY,String panelID) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] retrieveRows = new String[] {"_id","Content","Author","Time"};
        Cursor c = db.query(true,"comments",retrieveRows,"PanelID = '"+panelID+"'",null,null,null,sortBY,null);
        if (c != null){
            c.moveToFirst();
        }
        return c;
        //return db.rawQuery("select * from panels",null);
    }
    //The method for deleting a panel
    public void deletePanel(String panelID,String board) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM comments WHERE PanelID = '" + panelID + "'");
        db.execSQL("DELETE FROM panels WHERE _id = '" + panelID + "'");
        db.execSQL("UPDATE boards SET Panels = Panels - 1 WHERE NAME = '" + board + "'");
    }
    public Cursor getBoard(String panelID) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT Board FROM panels WHERE _id ='"+ panelID +"'",null);
    }
    public boolean existingUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        String id;
        Cursor c = db.rawQuery("SELECT UserID FROM user",null);
        c.moveToFirst();
        id = c.getString(0);
        Log.d("USERNAME",id);
        if (Integer.parseInt(id) < 0) {
            return true;
        } else {
            return false;
        }
    }
    //Checking whether or not a board name has been taken
    public boolean checkUserAvailable(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT Name FROM users WHERE Name ='" + title + "'",null);
        if (c.getCount()>0) {
            return false;
        } else {
            return true;
        }
    }
    //The method for creating a new board
    public void createUser(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", name);
        db.insert("users", null, contentValues);
        Cursor c = db.rawQuery("SELECT _id FROM users WHERE Name ='" +name + "'",null);
        c.moveToFirst();
        db.execSQL("UPDATE user SET UserID=" + c.getString(0) + " WHERE UserID=-1");

    }
    public String getUsername() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT UserID FROM user",null);
        c.moveToFirst();
        Cursor d = db.rawQuery("SELECT Name FROM users WHERE _id ='" + c.getString(0) + "'",null);
        d.moveToFirst();
        return d.getString(0);
    }
}



