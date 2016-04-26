package com.company.panels;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class BoardView extends AppCompatActivity {
    boolean LayoutState = true;
    DBManager mydb;
    RelativeLayout mainLayout;

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
            mydb.layoutChange(LayoutState);
            currentlyViewing();
        } else {
            inflater.inflate(R.menu.board_dash_menu, menu);
            setContentView(R.layout.activity_board_dash_view);
            LayoutState = true;
            mydb.layoutChange(LayoutState);
            currentlyViewing();
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
    //Tell the user which board they are currently viewing
    public void currentlyViewing() {
        String board = "All Boards";
        mainLayout = (RelativeLayout)findViewById(R.id.mainRLayout);
        Snackbar.make(mainLayout, "Currently Viewing " + board, Snackbar.LENGTH_SHORT)
                .setAction("Action", null)
                .show();
    }
}
