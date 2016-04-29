package com.company.panels;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

public class DiscoverBoards extends AppCompatActivity {
    DBManager mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mydb = new DBManager(this);
        setContentView(R.layout.activity_discover_boards);
        setTitle("Discover Boards");
        addSpinner();
        loadContent("Panels DESC");
    }
    //@Override
    //public boolean onCreateOptionsMenu(Menu menu) {
    //    MenuInflater inflater = getMenuInflater();
    //        inflater.inflate(R.menu.board_stream_menu, menu);
    //    return true;
    //}
    @Override
    public void onBackPressed() {
        toBoardView();
    }
    //Returning to board view
    public void toBoardView() {
        Intent intent = new Intent(this,BoardView.class);
        startActivity(intent);
    }
    //Generating panels
    private void loadContent(String sortBY) {
        Cursor cursor = mydb.getAllBoards(sortBY);
        String[] fromFieldNames = new String[] {"Name","Panels","Members","Time"};
        int[] toViewIDs = new int[] {R.id.boardTitle,R.id.panelCount,R.id.memberCount,R.id.boardTime};
        SimpleCursorAdapter myca;
        myca = new SimpleCursorAdapter(getBaseContext(),R.layout.board_stream,cursor,fromFieldNames,toViewIDs,0);
        ListView myListView = (ListView) findViewById(R.id.panelHost);
        myListView.setAdapter(myca);

    }
    //Adding the sort by spinner
    public void addSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.sortBy);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_board_by, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                //TODO REPLACE THIS WITH A SWITCH
                if (parent.getItemAtPosition(pos).toString().equals("Most Posts")) {
                    loadContent("Panels DESC");
                } else if (parent.getItemAtPosition(pos).toString().equals("Most Members")) {
                    loadContent("Members DESC");
                } else if (parent.getItemAtPosition(pos).toString().equals("Newest")) {
                    loadContent("_id DESC");
                } else if (parent.getItemAtPosition(pos).toString().equals("Oldest")) {
                    loadContent("_id ASC");
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }
}
