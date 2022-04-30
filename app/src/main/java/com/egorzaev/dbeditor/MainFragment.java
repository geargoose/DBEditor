package com.egorzaev.dbeditor;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class MainFragment extends Fragment {

    public MainFragment() {
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        // args.putString(ARG_PARAM1, param1);
        // args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // if (getArguments() != null) {
        //     mParam1 = getArguments().getString(ARG_PARAM1);
        //     mParam2 = getArguments().getString(ARG_PARAM2);
        // }
    }


    // ================================begin==============================================


    // private static final String TAG = "ezaev";

    FloatingActionButton add_fab;
    ListView database_list;

    ArrayList<String> names;
    ArrayList<String> paths;
    ArrayList<String> types;

    ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, 1);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE}, 1);
        }

        Db db = new Db(getContext(), "dbfiles", null, 1);
        SQLiteDatabase dbfiles = db.getReadableDatabase();

        if (!isExternalStorageReadable()) {
            Toast.makeText(getContext(), "no read, go away", Toast.LENGTH_SHORT).show();
        }

        if (!isExternalStorageWritable()) {
            Toast.makeText(getContext(), "no write, go away", Toast.LENGTH_SHORT).show();
        }

        names = new ArrayList<>();
        paths = new ArrayList<>();
        types = new ArrayList<>();

        database_list = view.findViewById(R.id.database_list);
        add_fab = view.findViewById(R.id.add_fab);

        names.add("Built in db");
        paths.add("dbfiles");
        types.add("local");

        update_table(dbfiles);

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, names);

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
                // Intent intent = new Intent(MainActivity.this, TableListActivity.class);
                // intent.putExtra("name", names.get(position));
                // intent.putExtra("path", paths.get(position));
                // intent.putExtra("type", types.get(position));
                // startActivity(intent);

                Bundle b = new Bundle();
                b.putString("name", names.get(position));
                b.putString("path", paths.get(position));
                b.putString("type", types.get(position));

                dbfiles.close();
                db.close();

                Navigation.findNavController(view).navigate(R.id.tableListFragment, b, new NavOptions.Builder()
                        .setEnterAnim(android.R.animator.fade_in)
                        .setExitAnim(android.R.animator.fade_out)
                        .build());
            }
        });

        database_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                return false;
            }
        });

        database_list.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        dbfiles.close();
        db.close();


        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();

                Db db = new Db(getContext(), "dbfiles", null, 1);
                SQLiteDatabase dbfiles = db.getWritableDatabase();

                names.add(uri.getPath().substring(uri.getPath().indexOf(':') + 1));
                adapter.notifyDataSetChanged();

                ContentValues cv = new ContentValues();

                cv.put("name", getFilePath(uri));
                cv.put("description", "Another local DB");
                cv.put("type", "local");
                String url;
                if (getFilePath(uri).contains("raw:")) {
                    url = getFilePath(uri).substring(4);
                } else {
                    url = getFilePath(uri);
                }
                cv.put("path", url);

                dbfiles.insert("dbfiles", null, cv);

                update_table(dbfiles);
                dbfiles.close();
            }
        }
    }


    // ============================some==functions========================================


    void update_table(SQLiteDatabase db) {
        try {
            Cursor c = db.query("dbfiles", null, null, null, null, null, null);
            if (c.getCount() > 0) {
                names.clear();
                paths.clear();
                types.clear();

                names.add("Built in db");
                paths.add("dbfiles");
                types.add("local");

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
        if (DocumentsContract.isDocumentUri(getContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                try {
                    uri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.parseLong(id));
                }
                catch (NumberFormatException e){
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
                cursor = getActivity().getContentResolver().query(uri, projection, selection, selectionArgs, null);
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
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }


    // ================================end================================================
}