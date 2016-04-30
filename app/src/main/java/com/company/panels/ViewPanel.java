package com.company.panels;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;
import android.widget.TextView;

public class
ViewPanel extends AppCompatActivity {
    DBManager mydb;
    TextView panelTime,panelAuthor,panelContent;
    String thisID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_panel);
        Intent intent = getIntent();
        thisID = intent.getExtras().getString("panelID");
        mydb = new DBManager(this);
        Cursor res = mydb.retrievePanel(thisID);
        panelTime = (TextView)findViewById(R.id.panelTime);
        panelAuthor = (TextView)findViewById(R.id.panelAuthor);
        panelContent = (TextView)findViewById(R.id.panelContent);
        while (res.moveToNext()) {
            panelTime.setText(res.getString(2));
            panelAuthor.setText(res.getString(3));
            setTitle(res.getString(4));
            panelContent.setText(res.getString(5));
        }
        loadComments("_id DESC");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_panel_menu, menu);
        loadComments("_id DESC");
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.post:
                deletePanel();
            case R.id.comment:
                toCreateComment();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void loadComments(String sortBy) {
        Cursor c = mydb.getComments(sortBy,thisID);
        String[] fromFieldNames = new String[] {"Author","Content","Time"};
        int[] toViewIDs = new int[] {R.id.commentAuthor,R.id.commentContent,R.id.commentTime};
        SimpleCursorAdapter myca;
        myca = new SimpleCursorAdapter(getBaseContext(),R.layout.comment,c,fromFieldNames,toViewIDs,0);
        ListView myListView = (ListView) findViewById(R.id.commentHost);
        myListView.setAdapter(myca);
    }
    private void deletePanel() {
    }
    public void toCreateComment() {
        Intent intent = new Intent(this,CreateComment.class);
        intent.putExtra("panelID",thisID);
        startActivity(intent);

    }
}
