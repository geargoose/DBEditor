package com.egorzaev.dbeditor.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.egorzaev.dbeditor.Db;
import com.egorzaev.dbeditor.EditorActivity;
import com.egorzaev.dbeditor.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TableListFragment extends Fragment {

    public TableListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    // ================================begin==============================================


    String name;
    String path;
    String type;

    ListView tables_list;
    FloatingActionButton query_fab;
    ArrayAdapter<String> adapter;
    TextView table_label;
    TextView error_label;

    @SuppressLint("Range")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_table_list, container, false);

        // assert getArguments() != null;

        EditorActivity activity = (EditorActivity) getActivity();

        // name = getArguments().getString("name");
        // path = getArguments().getString("path");
        // type = getArguments().getString("type");

        name = activity.getParamsFromIntent().getStringExtra("name");
        path = activity.getParamsFromIntent().getStringExtra("path");
        type = activity.getParamsFromIntent().getStringExtra("type");

        query_fab = view.findViewById(R.id.query_fab);
        tables_list = view.findViewById(R.id.tables_list);
        table_label = view.findViewById(R.id.tableLabelTextView);
        error_label = view.findViewById(R.id.errorTextView);

        table_label.setText(path);

        query_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle b =  new Bundle();
                b.putString("name", name);
                b.putString("path", path);
                b.putString("type", type);
                Navigation.findNavController(view).navigate(R.id.queryFragment, b, new NavOptions.Builder()
                        .setEnterAnim(android.R.animator.fade_in)
                        .setExitAnim(android.R.animator.fade_out)
                        .build());

            }
        });

        ArrayList<String> tableNames = new ArrayList<>();

        // Log.e("PAUK", "onCreate: "+Environment.getExternalStorageDirectory());
        // Log.e("PAUK", "onCreate: "+Environment.getExternalStorageState());

        // getContext().getActionBar().setDisplayHomeAsUpEnabled(true);

        Db db = new Db(getContext(), path, null, 1);
        SQLiteDatabase database = null;

        try {
            database = db.getReadableDatabase();
        }
        catch (SQLException e) {
            Toast.makeText(getContext(), "Failed to open database", Toast.LENGTH_LONG).show();
            // startActivity(new Intent(TableListActivity.this, MainActivity.class));
        }

        if (database != null) {

            Cursor c = database.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    tableNames.add(c.getString(c.getColumnIndex("name")));
                    c.moveToNext();
                }
            }

            c.close();

            adapter = new ArrayAdapter<>(
                    getContext(),
                    com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
                    tableNames
            );

            tables_list.setAdapter(adapter);

            adapter.notifyDataSetChanged();

            tables_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    // Intent intent = new Intent(TableListActivity.this, TableViewActivity.class);
                    // intent.putExtra("name", name);
                    // intent.putExtra("path", path);
                    // intent.putExtra("type", type);
                    // intent.putExtra("table", tableNames.get(i));
                    // startActivity(intent);

                    Bundle b = new Bundle();
                    b.putString("name", name);
                    b.putString("path", path);
                    b.putString("type", type);
                    b.putString("table", tableNames.get(i));
                    // database.close();
                    db.close();
                    Navigation.findNavController(view).navigate(R.id.tableViewFragment, b, new NavOptions.Builder()
                            .setEnterAnim(android.R.animator.fade_in)
                            .setExitAnim(android.R.animator.fade_out)
                            .build());
                }
            });

            database.close();
        }
        else {
            error_label.setText(R.string.db_open_error_message);
            error_label.setVisibility(View.VISIBLE);
            query_fab.setClickable(false);
        }


        db.close();

        return view;
    }


    // ================================end================================================
}