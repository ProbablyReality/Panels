package com.company.panels;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class CreatePanel extends AppCompatActivity {
    DBManager mydb;
    EditText editTitle,editContent,editBoard;
    View.OnClickListener nonexistantBoardListener;
    //TODO MAKE ENTER ON KEYBOARD DOUBLE AS A SUBMIT POST BUTTON

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_panel);
        setTitle("Create A Post");
        mydb = new DBManager(this);
        Cursor res = mydb.retrieveTemp();
        editTitle = (EditText)findViewById(R.id.panelTitle);
        editBoard = (EditText)findViewById(R.id.editBoard);
        editContent = (EditText)findViewById(R.id.editContent);
        while (res.moveToNext()) {
            editTitle.setText(res.getString(1));
            editBoard.setText(res.getString(2));
            editContent.setText(res.getString(3));
        }
        nonexistantBoardListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toCreateBoard();
            }
        };

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_panel_menu, menu);
        return true;
    }
    @Override
    protected void onPause() {
        super.onPause();
        mydb.storeTemp(editTitle.getText().toString(), editBoard.getText().toString(), editContent.getText().toString());
    }
    @Override
    public void onBackPressed() {
        toBoardView();
    }
    public void toBoardView() {
        Intent intent = new Intent(this,BoardView.class);
        startActivity(intent);
    }
    public void toCreateBoard() {
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
            case R.id.new_board:
                toCreateBoard();
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
                        editTitle.setText("");
                        editBoard.setText("");
                        editContent.setText("");
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
        //Test it the board exists
        EditText editBoard = (EditText)findViewById(R.id.editBoard);
        Cursor boardAvailable = mydb.checkBoardAvailable(editBoard.getText().toString().toLowerCase());
        if (boardAvailable.getCount() > 0 ) {
            String Board = editBoard.getText().toString();
            String Author = "Unattributed";
            EditText editTitle = (EditText) findViewById(R.id.panelTitle);
            String panelTitle = editTitle.getText().toString();
            EditText editContent = (EditText) findViewById(R.id.editContent);
            String panelContent = editContent.getText().toString();
            if (panelTitle.length()>0) {
                if (panelContent.length()>0) {
                    mydb.createPanel(Board,Author,panelTitle,panelContent);
                    editTitle.setText("");
                    editBoard.setText("");
                    editContent.setText("");
                    toBoardView();
                } else {
                    noContent();
                }
            } else {
                noTitle();
            }
        } else {
            nonexistantBoard();
        }
    }
    //

    //Snackbars for invalid content
    //TODO integrate these all into a switch statement
    public void nonexistantBoard() {
        Snackbar.make(editBoard, "The Specified Board Doesn't Exist Yet ", Snackbar.LENGTH_LONG)
                .setAction("Create it", nonexistantBoardListener)
                //TODO CHANGE THIS INTO colorAccent
                .setActionTextColor(Color.CYAN)
                .show();
    }
    public void noTitle() {
        Snackbar.make(editBoard, "Your Post Needs A Title", Snackbar.LENGTH_SHORT)
                .setAction("", null)
                .show();
    }
    public void noContent() {
        Snackbar.make(editBoard, "Your Post Needs Content", Snackbar.LENGTH_SHORT)
                .setAction("", null)
                .show();
    }
    //End snackbars
}
