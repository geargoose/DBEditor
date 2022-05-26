package com.egorzaev.dbeditor.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.egorzaev.dbeditor.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QueryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QueryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public QueryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QueryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QueryFragment newInstance(String param1, String param2) {
        QueryFragment fragment = new QueryFragment();
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
                }
                else {
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
}