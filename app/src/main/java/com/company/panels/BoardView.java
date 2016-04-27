package com.company.panels;

import android.content.Context;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class BoardView extends AppCompatActivity {
    boolean LayoutState = true;
    DBManager mydb;
    RelativeLayout mainLayout;
    TextView title,author,content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mydb = new DBManager(this);
        Log.d("LOGGINGSTUFF","A");

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
        if (LayoutState) {
            inflater.inflate(R.menu.board_stream_menu, menu);
            setContentView(R.layout.activity_board_stream_view);
            LayoutState = false;
            mydb.layoutChange(LayoutState);
            //currentlyViewing();
            Log.d("LOGGINGSTUFF","B");
            loadContent();
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
    public void loadContent() {
        //Generating panels from data
        Log.d("LOGGINGSTUFF","1");
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.d("LOGGINGSTUFF","2");
        ViewGroup parent = (ViewGroup)findViewById(R.id.contentHost);
        View v = inflater.inflate(R.layout.panel_stream, parent,false);

        // fill in any details dynamically here
        Log.d("LOGGINGSTUFF","3");
        TextView title =  (TextView) v.findViewById(R.id.panelTitle);
        title.setText("your text");
        Log.d("LOGGINGSTUFF", "3.4");
        TextView author = (TextView) v.findViewById(R.id.panelAuthor);
        author.setText("your text");
        Log.d("LOGGINGSTUFF", "3.74");
        TextView content = (TextView) v.findViewById(R.id.panelContent);
        content.setText("your text");
        Log.d("LOGGINGSTUFF", "4");

        // insert into main view
        ViewGroup insertPoint = (ViewGroup) findViewById(R.id.contentHost);
        Log.d("LOGGINGSTUFF","5");
        insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        Log.d("LOGGINGSTUFF", "6");


        /*
        Log.d("LOGGINGSTUFF","A");
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.d("LOGGINGSTUFF","B");
        ViewGroup parent = (ViewGroup)findViewById(R.id.mainRLayout);
        Log.d("LOGGINGSTUFF","C");
        inflater.inflate(R.layout.panel_stream, parent);
        Log.d("LOGGINGSTUFF", "D");*/
    }
}
