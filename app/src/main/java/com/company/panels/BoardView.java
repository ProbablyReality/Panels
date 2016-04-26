package com.company.panels;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class BoardView extends AppCompatActivity {
    boolean LayoutState = true;
    DBManager mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mydb = new DBManager(this);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
        if (LayoutState) {
            inflater.inflate(R.menu.board_stream_menu, menu);
            setContentView(R.layout.activity_board_stream_view);
            LayoutState = false;
        } else {
            inflater.inflate(R.menu.board_dash_menu, menu);
            setContentView(R.layout.activity_board_dash_view);
            LayoutState = true;
        }
            return true;
    }
    //Switching layouts
    @Override
    public void onBackPressed() {

    }
    //Switching to the create panel activity
    public void toCreatePanel() {
        Intent intent = new Intent(this,CreatePanel.class);
        startActivity(intent);
    }
    //Click events for menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.post:
                toCreatePanel();
                return true;
            case R.id.layout:
                //TODO implement layout switching
                invalidateOptionsMenu();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Switching to the view panel activity
    public void viewPanel() {
        Intent intent = new Intent(this,ViewPanel.class);
        startActivity(intent);
    }


    //BOARD CONSTRUCTOR
    public class Board {
        public String name;
        public String[] owner;
        public String[] contributor;
        public String[] member;
        public Boolean isPublic;
        public Panel[] panel;
    }
    //PANEL CONSTRUCTOR
    public class Panel {
        public String board;
        public int uuid;
        public int timeStamp;
        public User author;
        public String[] views;
        public String title;
        public String content;
        public Comment[] comment;
    }
    //USER CONSTRUCTOR
    public class User {
        public String id;
        public String password;
        public String avatar;
        public String nick;
        public String[] boards;
    }
    //COMMENT CONSTRUCTOR
    public class Comment {
        public User author;
        public String content;
        public String timeStamp;
    }

}
