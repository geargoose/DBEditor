package com.egorzaev.dbeditor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton add_fab;
    ListView database_list;
    ArrayList<String> bugaga;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Db db = new Db(getBaseContext(), "dbfiles", null, 1);
        SQLiteDatabase dbfiles = db.getReadableDatabase();

        bugaga = new ArrayList<>();
        database_list = findViewById(R.id.database_list);
        add_fab = findViewById(R.id.add_fab);
        bugaga.add("Test db");
        adapter = new ArrayAdapter<>(getBaseContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, bugaga);

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
                startActivity(new Intent(MainActivity.this, TableViewActivity.class).putExtra("db_id", position));
            }
        });

        Cursor c = dbfiles.query("dbfiles", null, null, null, null, null, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                bugaga.add(c.getString(1));
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

                bugaga.add(chosenDbUri.getPath());
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