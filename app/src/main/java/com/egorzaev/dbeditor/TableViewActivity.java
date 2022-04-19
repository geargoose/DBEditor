package com.egorzaev.dbeditor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class TableViewActivity extends AppCompatActivity {

    TableLayout table_view;
    ProgressBar spinner;

    String name;
    String path;
    String table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_view);

        table_view = findViewById(R.id.table);
        spinner = findViewById(R.id.spinner);

        name = getIntent().getStringExtra("name");
        table = getIntent().getStringExtra("table");
        path = getIntent().getStringExtra("path");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Db db = new Db(getBaseContext(), path, null, 1);
        SQLiteDatabase database = db.getReadableDatabase();

        Cursor c = database.query(table, null, null, null, null, null, null);

        TableRow titles = new TableRow(this);
        for (String name : c.getColumnNames()) {
            TextView textView = new TextView(this);
            textView.setText(name);
            titles.addView(textView);
        }
        table_view.addView(titles);

        if (c.getCount() > 0) {
            c.moveToFirst();

            do {
                TableRow row = new TableRow(this);

                for (int i = 0; i < c.getColumnCount(); i++) {
                    Button button = new Button(this); // c.getString(i)
                    button.setText(c.getString(i));
                    row.addView(button);
                }

                table_view.addView(row);

            } while (c.moveToNext());
        }
        c.close();

        /*Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        spinner.setVisibility(View.GONE);
    }

    //@Override
    //protected void onDestroy() {
    //    super.onDestroy();
    //    database.close();
    //    db.close();
    //}
}