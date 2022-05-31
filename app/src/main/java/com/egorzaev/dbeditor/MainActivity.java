package com.egorzaev.dbeditor;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ezaev";

    FloatingActionButton add_fab;
    ListView database_list;

    ArrayList<String> names;
    ArrayList<String> paths;
    ArrayList<String> types;

    ArrayAdapter<String> adapter;

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


        names = new ArrayList<>();
        paths = new ArrayList<>();
        types = new ArrayList<>();

        database_list = findViewById(R.id.db_list);

        update_table(dbfiles);

        adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, names);

        database_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                intent.putExtra("name", names.get(position));
                intent.putExtra("path", paths.get(position));
                intent.putExtra("type", types.get(position));
                startActivity(intent);

                // Bundle b = new Bundle();
                // b.putString("name", names.get(position));
                // b.putString("path", paths.get(position));
                // b.putString("type", types.get(position));

                dbfiles.close();
                db.close();
            }
        });

        database_list.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        dbfiles.close();
        db.close();
    }

    void update_table(SQLiteDatabase db) {
        try {
            Cursor c = db.query("dbfiles", null, null, null, null, null, null);
            if (c.getCount() > 0) {
                names.clear();
                paths.clear();
                types.clear();
                // names.add("Built in db");
                // paths.add("dbfiles");
                // types.add("local");
                c.moveToFirst();
                do {
                    names.add(c.getString(1));
                    types.add(c.getString(3));
                    paths.add(c.getString(4));
                } while (c.moveToNext());
            }
            c.close();
            // adapter.notifyDataSetChanged();
        } catch (SQLException e) {
            // Toast.makeText(getContext(), "Can't open DB list", Toast.LENGTH_SHORT).show();
        }
    }

    public String getFilePath(Uri uri) {
        String selection = null;
        String[] selectionArgs = null;
        if (DocumentsContract.isDocumentUri(getBaseContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                try {
                    uri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.parseLong(id));
                } catch (NumberFormatException e) {
                    if (id.contains("raw:")) {
                        return id.substring(4);
                    } else {
                        return id;
                    }
                }
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{split[1]};
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = null;
            try {
                cursor = this.getContentResolver().query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                Log.e("MY", "getFilePath: ", e);
            } finally {
                if (cursor != null) cursor.close();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public String getFileExtension(String path) {
        int pos = path.lastIndexOf(".");
        if (pos != -1) return path.substring(pos + 1);
        else return "";
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

}