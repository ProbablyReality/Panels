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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class BoardView extends AppCompatActivity{
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
        inflater.inflate(R.menu.board_stream_menu, menu);
        setContentView(R.layout.activity_board_stream_view);
        addSpinner();
        //currentlyViewing();
        loadContent("_id DESC");
        ListView listView = (ListView)findViewById(R.id.panelHost);
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                TextView panelID = (TextView)view.findViewById(R.id.panelID) ;
                toViewPanel(panelID.getText().toString());
            }
        });
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
            //case R.id.layout:
                //Either fix this, or scrap it
                //invalidateOptionsMenu();
            case R.id.discover:
                toDiscoverBoards();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Switching to the view panel activity
    public void toViewPanel(String panelID) {
        Intent intent = new Intent(this,ViewPanel.class);
        intent.putExtra("panelID",panelID);
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
                Cursor cursor = mydb.getAllPanels(sortBY);
        String[] fromFieldNames = new String[] {"_id","Title","Author","Content","Time","Comments"};
        int[] toViewIDs = new int[] {R.id.panelID,R.id.panelTitle,R.id.panelAuthor,R.id.panelContent,R.id.dateStamp,R.id.commentCount};
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
