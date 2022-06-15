package com.egorzaev.dbeditor;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

public class EditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        try {
            getSupportActionBar().hide(); // Т. к. фрагменты используют свой TollBar, то системный просто не нужен, прячем его.
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public Intent getParamsFromIntent() {  // Передача Intent фрагменту
        return getIntent();
    }
}