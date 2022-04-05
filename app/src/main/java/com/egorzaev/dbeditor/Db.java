package com.egorzaev.dbeditor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Db extends SQLiteOpenHelper {

    public Db(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String request = "CREATE TABLE projects (\n" +
                "    ID INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    title text NOT NULL,\n" +
                "    name text NOT NULL,\n" +
                "    description text NOT NULL,\n" +
                "    date text\n" +
                ");\n";
        db.execSQL(request);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
