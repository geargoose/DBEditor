package com.egorzaev.dbeditor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton add_fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Db db = new Db(getBaseContext(), "Vegetables", null, 1);
        SQLiteDatabase projects = db.getWritableDatabase();

        add_fab = findViewById(R.id.add_fab);

        add_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                projects.execSQL(
                        "INSERT INTO projects (title, name, description, date) VALUES (\"Title\", \"Name\", \"Description\", \"Date\")");
                startActivity(new Intent(MainActivity.this, TableViewActivity.class));
            }
        });


    }
}