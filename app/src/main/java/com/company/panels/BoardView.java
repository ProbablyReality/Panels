package com.company.panels;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class BoardView extends AppCompatActivity {
    boolean LayoutState = true;
    DBManager mydb;
    RelativeLayout mainLayout;
    TextView title,author,content;

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
            addSpinner();
            //currentlyViewing();
            loadContent("_id DESC");
        } else {
            inflater.inflate(R.menu.board_dash_menu, menu);
            setContentView(R.layout.activity_board_dash_view);
            LayoutState = true;
            mydb.layoutChange(LayoutState);
            //currentlyViewing();
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
        Log.d("PASSED","Gate 3");
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.post:
                toCreatePanel();
                return true;
            case R.id.layout:
                //Either fix this, or scrap it
                //invalidateOptionsMenu();
            case R.id.discover:
                toDiscoverBoards();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Switching to the view panel activity
    public void viewPanel() {
        Intent intent = new Intent(this,ViewPanel.class);
        startActivity(intent);
    }
    //Switching to Dicover boards
    public void toDiscoverBoards() {
        Intent intent = new Intent(this,DiscoverBoards.class);
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
    //Generating panels
    private void loadContent(String sortBY) {
                Cursor cursor = mydb.getAllRows(sortBY);
        String[] fromFieldNames = new String[] {"Title","Author","Content","Time"};
        int[] toViewIDs = new int[] {R.id.panelTitle,R.id.panelAuthor,R.id.panelContent,R.id.dateStamp};
        SimpleCursorAdapter myca;
        myca = new SimpleCursorAdapter(getBaseContext(),R.layout.panel_stream,cursor,fromFieldNames,toViewIDs,0);
        ListView myListView = (ListView) findViewById(R.id.panelHost);
        myListView.setAdapter(myca);

    }
    //Adding the sort by spinner
    public void addSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.sortBy);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_panel_by, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                //TODO REPLACE THIS WITH A SWITCH
                if (parent.getItemAtPosition(pos).toString().equals("Newest")) {
                    loadContent("_id DESC");
                } else if (parent.getItemAtPosition(pos).toString().equals("Oldest")) {
                    loadContent("_id ASC");
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });
    }
}
