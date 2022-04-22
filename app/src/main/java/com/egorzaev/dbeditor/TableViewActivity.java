package com.egorzaev.dbeditor;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TableViewActivity extends AppCompatActivity {

    TableLayout table_view;
    ProgressBar spinner;

    String name;
    String path;
    String table;
    String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_view);

        table_view = findViewById(R.id.table);
        spinner = findViewById(R.id.spinner);

        name = getIntent().getStringExtra("name");
        table = getIntent().getStringExtra("table");
        path = getIntent().getStringExtra("path");
        query = getIntent().getStringExtra("query");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Db db = new Db(getBaseContext(), path, null, 1);
        SQLiteDatabase database = db.getReadableDatabase();

        Cursor c;

        if (query == null) {
            c = database.query(table, null, null, null, null, null, null);
        }
        else {
            c = database.rawQuery(query, null);
        }

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

        spinner.setVisibility(View.GONE);

    }
}