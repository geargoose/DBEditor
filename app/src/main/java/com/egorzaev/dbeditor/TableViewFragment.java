package com.egorzaev.dbeditor;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TableViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TableViewFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TableViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TableViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TableViewFragment newInstance(String param1, String param2) {
        TableViewFragment fragment = new TableViewFragment();
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


    TableLayout table_view;
    ProgressBar spinner;

    String name;
    String path;
    String table;
    String query;
    String type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_table_view, container, false);

        assert getArguments() != null;

        name = getArguments().getString("name");
        table = getArguments().getString("table");
        path = getArguments().getString("path");
        type = getArguments().getString("type");
        query = getArguments().getString("query");

        table_view = view.findViewById(R.id.table);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Db db = new Db(getContext(), path, null, 1);
        SQLiteDatabase database = db.getReadableDatabase();

        Cursor c;

        try {
            if (query == null || query.equals("")) {
                c = database.query(table, null, null, null, null, null, null);
            } else {
                c = database.rawQuery(query, null);
            }

            TableRow titles = new TableRow(getContext());
            for (String name : c.getColumnNames()) {
                TextView textView = new TextView(getContext());
                textView.setText(name);
                titles.addView(textView);
            }
            table_view.addView(titles);

            if (c.getCount() > 0) {
                c.moveToFirst();
                do {
                    TableRow row = new TableRow(getContext());
                    for (int i = 0; i < c.getColumnCount(); i++) {
                        Button button = new Button(getContext()); // c.getString(i)
                        button.setText(c.getString(i));
                        row.addView(button);
                    }
                    table_view.addView(row);
                } while (c.moveToNext());
            }
            c.close();
        }
        catch (SQLiteException e) {
            TextView tv = new TextView(getContext());
            tv.setText("В запросе допущена ошибка!");
            table_view.addView(tv);
        }



    }
}