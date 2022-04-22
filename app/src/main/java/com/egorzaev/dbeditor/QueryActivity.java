package com.egorzaev.dbeditor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class QueryActivity extends AppCompatActivity {

    TextView db_label;
    EditText query_edit;
    Button execute;
    Button clear;

    String name;
    String path;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        name = getIntent().getStringExtra("name");
        path = getIntent().getStringExtra("path");
        type = getIntent().getStringExtra("type");

        execute = findViewById(R.id.executeButton);
        query_edit = findViewById(R.id.queryTextEdit);

        execute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QueryActivity.this, TableViewActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("path", path);
                intent.putExtra("type", type);
                intent.putExtra("query", query_edit.getText().toString());
                startActivity(intent);
            }
        });
    }
}