package com.egorzaev.dbeditor.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.egorzaev.dbeditor.R;

public class QueryFragment extends Fragment {


    public QueryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    TextView db_label;
    EditText query_edit;
    Button execute;
    Button clear;

    String name;
    String path;
    String type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_query, container, false);

        assert getArguments() != null;
        setHasOptionsMenu(true);

        name = getArguments().getString("name");
        path = getArguments().getString("path");
        type = getArguments().getString("type");

        execute = view.findViewById(R.id.executeButton);
        clear = view.findViewById(R.id.clearButton);
        query_edit = view.findViewById(R.id.queryTextEdit);

        execute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(query_edit.getText().toString().equals(""))) {
                    Bundle b = new Bundle();
                    b.putString("name", name);
                    b.putString("path", path);
                    b.putString("type", type);
                    b.putString("query", query_edit.getText().toString());
                    Navigation.findNavController(view).navigate(R.id.tableViewFragment, b, new NavOptions.Builder()
                            .setEnterAnim(android.R.animator.fade_in)
                            .setExitAnim(android.R.animator.fade_out)
                            .build());
                } else {
                    Toast.makeText(requireContext(), R.string.empty_request_not_allowed, Toast.LENGTH_SHORT).show();
                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query_edit.setText("");
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.sample_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_help:  {
                // TODO: help fragment
                Toast.makeText(requireContext(), "Пока не могу помочь, поищите в гугле", Toast.LENGTH_LONG).show();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}