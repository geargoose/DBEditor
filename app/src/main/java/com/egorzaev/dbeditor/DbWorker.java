package com.egorzaev.dbeditor;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class DbWorker extends Worker {

    public DbWorker(
            @NonNull Context appContext,
            @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("ezaev", "doWork: begin task");

        String query = getInputData().getString("query");
        String name = getInputData().getString("name");
        String table = getInputData().getString("table");
        String path = getInputData().getString("path");
        String type = getInputData().getString("type");

        try {

            Db db = new Db(getApplicationContext(), path, null, 1);
            SQLiteDatabase database = db.getReadableDatabase();

            Cursor cursor;

            if (query == null || query.equals("")) {
                cursor = database.query(table, null, null, null, null, null, null);
            } else {
                cursor = database.rawQuery(query, null);
            }


            Log.d("ezaev", "doWork: task complete");
            Log.d("ezaev", "doWork: query: " + query);
            Log.d("ezaev", "doWork: columns: " + cursor.getColumnCount());
            Log.d("ezaev", "doWork: items: " + cursor.getCount());

            int qty = cursor.getColumnCount();
            int c = 0;

            Data.Builder data = new Data.Builder();

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    String[] row = new String[cursor.getColumnCount()];
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        row[i] = cursor.getString(i);
                    }
                    data.putStringArray(String.valueOf(c), row);
                    c++;
                } while (cursor.moveToNext());
            }

            data.putStringArray("cols", cursor.getColumnNames());
            data.putInt("qty", c);

            cursor.close();

            Log.d("ezaev", "doWork: " + data.build().toString());

            return Result.success(data.build());

        } catch (Exception e) {

            e.printStackTrace();
            Log.e("ezaev", "doWork: " + e.toString());
            return Result.failure(new Data.Builder().putString("error", e.toString()).build());

        }
    }
}