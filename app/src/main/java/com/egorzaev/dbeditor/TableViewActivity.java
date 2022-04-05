package com.egorzaev.dbeditor;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;

public class TableViewActivity extends AppCompatActivity {

    TableLayout table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_view);

        table = findViewById(R.id.table);

        ArrayList<String[]> items = new ArrayList<>();

        Db db = new Db(getBaseContext(), "Vegetables", null, 1);
        SQLiteDatabase projects = db.getReadableDatabase();

        Cursor c = projects.query("projects", null, null, null, null, null, null);

        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                String[] s = new String[c.getColumnCount()];
                for (int i = 0; i < c.getColumnCount(); i++) {
                    s[i] = c.getString(i);
                }
                items.add(s);
            } while (c.moveToNext());
        }

        c.close();

        for (String[] i : items) {
            TableRow row = new TableRow(this);
            for (String s: i) {
                Button button = new Button(this);
                button.setText(s);
                row.addView(button);
            }

            table.addView(row);
        }

    }
}