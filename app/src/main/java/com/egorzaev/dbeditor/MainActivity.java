package com.egorzaev.dbeditor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton add_fab;
    ListView database_list;
    ArrayList<String> names;
    ArrayList<String> tables;
    ArrayList<String> paths;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Db db = new Db(getBaseContext(), "dbfiles", null, 1);
        SQLiteDatabase dbfiles = db.getReadableDatabase();

        names = new ArrayList<>();
        database_list = findViewById(R.id.database_list);
        add_fab = findViewById(R.id.add_fab);
        names.add("Test db");
        tables.add("dbfiles");
        paths.add("dbfiles");
        adapter = new ArrayAdapter<>(getBaseContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, names);

        add_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent picker = new Intent(Intent.ACTION_GET_CONTENT);
                picker.setType("*/*");
                startActivityForResult(picker, 1);
            }
        });

        database_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, TableViewActivity.class);
                intent.putExtra("name", names.get(position));
                intent.putExtra("path", paths.get(position));
                intent.putExtra("table", tables.get(position));
                startActivity(intent);
            }
        });

        Cursor c = dbfiles.query("dbfiles", null, null, null, null, null, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                names.add(c.getString(1));
            } while (c.moveToNext());
        }
        c.close();

        database_list.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        dbfiles.close();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Uri chosenDbUri = data.getData();

                Db db = new Db(getBaseContext(), "dbfiles", null, 1);
                SQLiteDatabase dbfiles = db.getWritableDatabase();

                names.add(chosenDbUri.getPath());
                adapter.notifyDataSetChanged();

                ContentValues cv = new ContentValues();
                cv.put("name", chosenDbUri.toString());
                cv.put("description", "Local db, look at it:" + data.getType());
                cv.put("type", "local");
                cv.put("path", chosenDbUri.getPath());
                dbfiles.insert("dbfiles", null, cv);
                dbfiles.close();
            }
        }
    }
}