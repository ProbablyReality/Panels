package com.company.panels;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Switch;

public class CreateBoard extends AppCompatActivity {
    DBManager mydb;
    EditText boardTitle;
    Switch isPublic,restrictPost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_board);
        setTitle("Create a Board");
        mydb = new DBManager(this);
        //Snackbar.make(null, "A Board With That Name Already Exists", Snackbar.LENGTH_SHORT);

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
    public void toCreatePanel() {
        Intent intent = new Intent(this,CreatePanel.class);
        startActivity(intent);
    }
    //Click events for menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.create:
                //Detect if the board already exists before continuing
                boardTitle = (EditText)findViewById(R.id.editBoard);
                Cursor boardAvailable = mydb.checkBoardAvailable(boardTitle.getText().toString().toLowerCase());
                if (boardAvailable.getCount() == 0 ) {
                    isPublic = (Switch)findViewById(R.id.isPublic);
                    restrictPost = (Switch)findViewById(R.id.restrictPost);
                    //This sends the data into the class where it is added to the database
                    mydb.createBoard(boardTitle.getText().toString().toLowerCase(),isPublic.isChecked(),restrictPost.isChecked());
                    toCreatePanel();
                } else {
                    //tell user board already exists
                    TableExists();


                    }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void TableExists() {
        Snackbar.make(boardTitle, "A Board With That Name Already Exists", Snackbar.LENGTH_SHORT)
                .setAction("Action", null)
        .show();
    }
}
