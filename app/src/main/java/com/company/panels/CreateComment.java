package com.company.panels;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Switch;

public class CreateComment extends AppCompatActivity {

    DBManager mydb;
    String thisID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_comment);
        Intent intent = getIntent();
        thisID = intent.getExtras().getString("panelID");
        setTitle("Post Comment");
        mydb = new DBManager(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_comment_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.post:
                postComment();
                break;
            case R.id.delete:
                CancelPost();
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
    public void postComment() {
        EditText editText = (EditText)findViewById(R.id.editContent);
        Switch s = (Switch)findViewById(R.id.anon);
        String author = mydb.getUsername();
        if (s.isChecked()) {
            author = "Unattributed";
        }
        mydb.createComment(thisID,author,editText.getText().toString());
        toViewPanel();
    }
    public void toViewPanel() {
        Intent intent = new Intent(this,ViewPanel.class);
        intent.putExtra("panelID",thisID);
        startActivity(intent);
    }
    public void CancelPost() {
        new AlertDialog.Builder(this)
                .setTitle("Delete draft")
                .setMessage("Are you sure you want to cancel this post?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        toViewPanel();
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
}
