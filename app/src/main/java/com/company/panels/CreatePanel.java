package com.company.panels;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class CreatePanel extends AppCompatActivity {
    int postCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_panel);
        setTitle("Post an Update");

        //Adding back button
        //getActionBar().setDisplayHomeAsUpEnabled(true);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_panel_menu, menu);
        return true;
    }
    @Override
    public void onBackPressed() {
        //TODO Save current values in case user returns
        toBoardView();
    }
    public void toBoardView() {
        Intent intent = new Intent(this,BoardView.class);
        startActivity(intent);
    }
    public void toCreateBoard(View view) {
        Intent intent = new Intent(this,CreateBoard.class);
        startActivity(intent);
    }
    //Click events for menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.delete:
                CancelPost();
                return true;
            case R.id.post:
                SubmitPost();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //Cancel Update GUI
    //I pretty much just stole this from stackoverflow, but hey, it works
    public void CancelPost() {
        new AlertDialog.Builder(this)
                .setTitle("Delete draft")
                .setMessage("Are you sure you want to delete this draft?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        toBoardView();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                //TODO REDO ALL OF THIS, IT SUCKS AND I WANT MATERIAL DESIGN ASSETS
                .setIcon(android.R.drawable.ic_menu_delete)
                .show();
    }
    public void SubmitPost() {
        //Implement authors info here
        String Author = "DefaultAuthor";
        //Add all the stuff for posting here
        String Board = "Default";
        EditText editTitle = (EditText)findViewById(R.id.editTitle);
        String panelTitle = editTitle.getText().toString();
        EditText editContent = (EditText)findViewById(R.id.editContent);
        String panelContent = editTitle.getText().toString();
        String panelUUID = String.valueOf(postCount);
        postCount++;
        //Writing the content to

    }
}
