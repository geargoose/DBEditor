package com.egorzaev.dbeditor.fragment;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.egorzaev.dbeditor.Db;
import com.egorzaev.dbeditor.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ItemEditorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemEditorFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ItemEditorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ItemEditorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ItemEditorFragment newInstance(String param1, String param2) {
        ItemEditorFragment fragment = new ItemEditorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

        // TODO: make it works
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

        save_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues cv = new ContentValues();

                for (int i = 0; i < edits.size(); i++) {
                    cv.put(cols.get(i), edits.get(i).getText().toString());
                }

                Log.d("MYTAG", "onClick: " + cv);

                StringBuilder query_before = new StringBuilder();
                StringBuilder query_after = new StringBuilder();

                for (int i = 0; i < cols.size(); i++) {
                    if (i == cols.size() - 1) {
                        query_before.append(cols.get(i)).append(" = '").append(vals.get(i)).append("'");
                        query_after.append(cols.get(i)).append(" = '").append(edits.get(i).getText().toString()).append("'");
                    }
                    else {
                        query_before.append(cols.get(i)).append(" = '").append(vals.get(i)).append("' AND ");
                        query_after.append(cols.get(i)).append(" = '").append(edits.get(i).getText().toString()).append("', ");
                    }
                }

                Log.d("MYTAG", "request: " + "UPDATE " + table + " SET " + query_after + " WHERE " + query_before + "");

                Cursor cursor = database.rawQuery("UPDATE " + table + " SET " + query_after + " WHERE " + query_before + "", null);

                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        for (int i = 0; i < cursor.getColumnCount(); i++) {
                            Log.d("MYTAG", cursor.getString(i));
                        }
                        cursor.moveToNext();
                    }
                }

                cursor.close();

            }
        });

        return view;
    }
}