package com.egorzaev.dbeditor.fragment;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.egorzaev.dbeditor.Db;
import com.egorzaev.dbeditor.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;

public class ItemEditorFragment extends MyFragment {

    private static final String TAG = "ezaev";

    public ItemEditorFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    LinearLayout ll;

    String name;
    String path;
    String table;
    String[] coords;
    String[] headers;
    String type;

    FloatingActionButton save_fab;

    @SuppressLint("Range")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_editor, container, false);

        ll = view.findViewById(R.id.items_container);
        save_fab = view.findViewById(R.id.save_fab);

        assert getArguments() != null;
        name = getArguments().getString("name");
        table = getArguments().getString("table");
        path = getArguments().getString("path");
        type = getArguments().getString("type");
        coords = getArguments().getStringArray("coords");
        headers = getArguments().getStringArray("headers");

        ArrayList<EditText> edits = new ArrayList<>();

        Db db = new Db(getContext(), path, null, 1);
        SQLiteDatabase database = db.getWritableDatabase();
        Cursor c;

        c = database.query(table, null, null, null, null, null, null);

        ArrayList<String> cols = new ArrayList<>(Arrays.asList(c.getColumnNames()));
        ArrayList<String> vals = new ArrayList<>(Arrays.asList(coords));

        // c.close();

        for (int i = 0; i < cols.size(); i++) {
            EditText e = new EditText(getContext());
            e.setHint(cols.get(i));
            e.setText(vals.get(i));
            edits.add(e);
            ll.addView(e);
        }

        TextView tv = new TextView(getContext());
        tv.setText(Arrays.toString(coords));
        ll.addView(tv);

        StringBuilder query_before = new StringBuilder();
        StringBuilder query_after = new StringBuilder();

        for (int i = 0; i < cols.size(); i++) {
            if (i == cols.size() - 1) {
                query_before.append(cols.get(i)).append(" = '").append(vals.get(i)).append("'");
                query_after.append(cols.get(i)).append(" = '").append(edits.get(i).getText().toString()).append("'");
            } else {
                query_before.append(cols.get(i)).append(" = '").append(vals.get(i)).append("' AND ");
                query_after.append(cols.get(i)).append(" = '").append(edits.get(i).getText().toString()).append("', ");
            }
        }

        Toolbar myToolbar = view.findViewById(R.id.myToolbar);
        myToolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    // Navigate to settings screen
                    Cursor cursor = database.rawQuery("DELETE FROM " + table + " WHERE " + query_before + "", null);
                    Log.d(TAG, "onCreateView: " + "DELETE FROM " + table + " WHERE " + query_before + "");
                    Log.d(TAG, "onCreateView: " + cursor.getColumnCount());
                    cursor.close();
                    Toast.makeText(getContext(), "delete", Toast.LENGTH_SHORT).show();
                    return true;
                default:
                    return false;
            }
        });

        save_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues cv = new ContentValues();

                for (int i = 0; i < edits.size(); i++) {
                    cv.put(cols.get(i), edits.get(i).getText().toString());
                }

                Log.d("MYTAG", "onClick: " + cv);

                Log.d(TAG, "request: " + "UPDATE " + table + " SET " + query_after + " WHERE " + query_before + "");

                Cursor cursor = database.rawQuery("UPDATE " + table + " SET " + query_after + " WHERE " + query_before + "", null);

                try {
                    if (cursor.moveToFirst()) {
                        while (!cursor.isAfterLast()) {
                            for (int i = 0; i < cursor.getColumnCount(); i++) {
                                Log.d("MYTAG", cursor.getString(i));
                            }
                            cursor.moveToNext();
                        }
                    }
                } catch (android.database.sqlite.SQLiteConstraintException e) {
                    Toast.makeText(getContext(), "Ошибка в данных", Toast.LENGTH_SHORT).show();
                }

                cursor.close();

            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Toolbar myToolbar = view.findViewById(R.id.myToolbar);
        //myToolbar.inflateMenu(R.menu.item_editor_top_menu);
    }
}