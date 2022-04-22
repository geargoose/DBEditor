package com.egorzaev.dbeditor;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.VolumeShaper;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class TableListActivity extends AppCompatActivity {

    String name;
    String path;
    String type;
    ListView tables_list;
    ArrayAdapter<String> adapter;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_list);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        //    this.requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, 1);
        //}
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE}, 1);
        }

        name = getIntent().getStringExtra("name");
        path = getIntent().getStringExtra("path");
        type = getIntent().getStringExtra("type");

        tables_list = findViewById(R.id.tables_list);

        ArrayList<String> tableNames = new ArrayList<>();

        Log.e("PAUK", "onCreate: "+Environment.getExternalStorageDirectory());
        Log.e("PAUK", "onCreate: "+Environment.getExternalStorageState());

        Db db = new Db(peekAvailableContext(), path, null, 1);
        Log.d("PAUK", "onCreate: " + db);
        SQLiteDatabase database = db.getReadableDatabase();

        Cursor c = database.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                tableNames.add(c.getString(c.getColumnIndex("name")));
                c.moveToNext();
            }
        }

        c.close();

        adapter = new ArrayAdapter<>(
                getBaseContext(),
                com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
                tableNames
        );

        tables_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(TableListActivity.this, TableViewActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("path", path);
                intent.putExtra("type", type);
                intent.putExtra("table", tableNames.get(i));
                startActivity(intent);
            }
        });

        tables_list.setAdapter(adapter);

        database.close();
        db.close();
    }
}