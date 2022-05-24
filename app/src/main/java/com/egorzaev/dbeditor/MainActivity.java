package com.egorzaev.dbeditor;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import com.egorzaev.dbeditor.fragment.MainFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ezaev";

    FragmentContainerView fcv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String request = "CREATE TABLE IF NOT EXISTS dbfiles (\n" +
                "    ID INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    name text NOT NULL,\n" +
                "    description text NOT NULL,\n" +
                "    type text DEFAULT 'local',\n" +
                "    path text NOT NULL\n" +
                ");\n";

        Db db = new Db(this, "dbfiles", null, 1);
        SQLiteDatabase dbfiles = db.getReadableDatabase();
        dbfiles.execSQL(request);
    }
}