package com.company.panels;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Switch;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class CreateBoard extends AppCompatActivity {
    DBManager mydb;
    EditText boardTitle;
    Switch isPublic,restrictPost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_board);
        setTitle("Create a Board");
        Firebase.setAndroidContext(this);
        mydb = new DBManager(this);
        Cursor res = mydb.retrieveTemp();
        boardTitle = (EditText)findViewById(R.id.panelTitle);
        while (res.moveToNext()) {
            boardTitle.setText(res.getString(2));
        }
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
                final Firebase myFirebaseRef = new Firebase("https://popping-heat-1795.firebaseio.com/");
                //Detect if the board already exists before continuing
                myFirebaseRef.child("boards/" + boardTitle.getText().toString().toLowerCase() + "/isPublic").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.getValue() == null) {
                            isPublic = (Switch)findViewById(R.id.isPublic);
                            restrictPost = (Switch)findViewById(R.id.restrictPost);
                            Log.d("WEWLAD","2");
                            myFirebaseRef.child("boards/" + boardTitle.getText().toString().toLowerCase() + "/isPublic")
                                    .setValue(java.lang.Boolean.toString(isPublic.isChecked()));
                            myFirebaseRef.child("boards/" + boardTitle.getText().toString().toLowerCase() + "/restrictPost")
                                    .setValue(java.lang.Boolean.toString(restrictPost.isChecked()));
                            toCreatePanel();
                        } else {
                            //tell user board already exists
                            Log.d("WEWLAD","1");
                            TableExists();
                        }
                    }
                    @Override
                    public void onCancelled(FirebaseError arg0) {
                    }
                });
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
