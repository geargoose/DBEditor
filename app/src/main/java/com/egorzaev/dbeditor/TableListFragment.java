package com.egorzaev.dbeditor;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TableListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TableListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TableListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TableListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TableListFragment newInstance(String param1, String param2) {
        TableListFragment fragment = new TableListFragment();
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


    // ================================begin==============================================


    String name;
    String path;
    String type;

    ListView tables_list;
    FloatingActionButton query_fab;
    ArrayAdapter<String> adapter;
    TextView table_label;

    @SuppressLint("Range")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_table_list, container, false);

        assert getArguments() != null;

        name = getArguments().getString("name");
        path = getArguments().getString("path");
        type = getArguments().getString("type");

        query_fab = view.findViewById(R.id.query_fab);
        tables_list = view.findViewById(R.id.tables_list);
        table_label = view.findViewById(R.id.tableLabelTextView);

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

        Db db = new Db(getContext(), path, null, 1);
        SQLiteDatabase database = null;

        try {
            database = db.getReadableDatabase();
        }
        catch (SQLException e) {
            Toast.makeText(getContext(), "Failed to open database", Toast.LENGTH_LONG).show();
            // startActivity(new Intent(TableListActivity.this, MainActivity.class));
        }

        assert (database != null);

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

                Bundle b =  new Bundle();
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
        db.close();

        return view;
    }



    // ================================end================================================
}