package com.company.panels;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;

public class
ViewPanel extends AppCompatActivity {
    DBManager mydb;
    TextView panelTime,panelAuthor,panelContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_panel);

        Intent intent = getIntent();
        String panelID = intent.getExtras().getString("panelID");
        mydb = new DBManager(this);
        Cursor res = mydb.retrievePanel(panelID);
        panelTime = (TextView)findViewById(R.id.panelTime);
        panelAuthor = (TextView)findViewById(R.id.panelAuthor);
        panelContent = (TextView)findViewById(R.id.panelContent);
        while (res.moveToNext()) {
            panelTime.setText(res.getString(2));
            panelAuthor.setText(res.getString(3));
            setTitle(res.getString(4));
            panelContent.setText(res.getString(5));
        }
    }
}
