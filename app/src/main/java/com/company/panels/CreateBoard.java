package com.company.panels;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class CreateBoard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_board);
        setTitle("Create a Board");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_board_menu, menu);
        return true;
    }
    @Override
    public void onBackPressed() {
        //TODO Save current values in case user returns
        toCreatePanel();
    }
    //Click events for menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.create:
                //Detect if board already exists else continue
                //Creating the board in sql

                toCreatePanel();
                return true;
            //case R.id.layout:
                //TODO implement layout switching
                //ERASE THIS FOR TESTING
                //return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void toCreatePanel() {
        Intent intent = new Intent(this,CreatePanel.class);
        startActivity(intent);
    }
}
