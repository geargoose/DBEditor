package com.egorzaev.dbeditor;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

public class EditorActivity extends AppCompatActivity {

    // Db db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().penaltyDialog().build();
        //StrictMode.setThreadPolicy(policy);
        //db = new Db(this, getIntent().getStringExtra("path"), null, 1);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    // @Override
    // protected void onStop() {
    //     super.onStop();
    //     db.close();
    // }

    public Intent getParamsFromIntent() {
        return getIntent();
    }

    // public Db getDb() {
    //     return db;
    // }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //getSupportFragmentManager().popBackStack();
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}