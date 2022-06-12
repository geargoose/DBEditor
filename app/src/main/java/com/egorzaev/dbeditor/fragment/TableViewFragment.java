package com.egorzaev.dbeditor.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.egorzaev.dbeditor.DbWorker;
import com.egorzaev.dbeditor.R;

import java.util.List;

public class TableViewFragment extends MyFragment {

    public TableViewFragment() {
    }


    TableLayout table_view;
    ProgressBar spinner;
    TextView empty;

    private WorkManager mWorkManager;
    OneTimeWorkRequest queryRequest;

    String name;
    String path;
    String table;
    String query;
    String type;
    String[] headers;
    Cursor c;

    private LiveData<List<WorkInfo>> mSavedWorkInfo;

    LiveData<List<WorkInfo>> getOutputWorkInfo() {
        return mSavedWorkInfo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_table_view, container, false);

        assert getArguments() != null;

        setHasOptionsMenu(true);

        mWorkManager = WorkManager.getInstance(requireContext());
        mSavedWorkInfo = mWorkManager.getWorkInfosByTagLiveData("queryWork");

        name = getArguments().getString("name");
        table = getArguments().getString("table");
        path = getArguments().getString("path");
        type = getArguments().getString("type");
        query = getArguments().getString("query");

        table_view = view.findViewById(R.id.table);
        spinner = view.findViewById(R.id.spinner);
        empty = view.findViewById(R.id.nothing_found_tv);

        spinner.setVisibility(View.VISIBLE);
        empty.setVisibility(View.GONE);

        if (query != null) {
            runQuery(query);
        } else {
            runQuery("SELECT * FROM " + table);
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.table_view_top_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_update: {
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // clear_table();
        // spinner.setVisibility(View.VISIBLE);

        mWorkManager.getWorkInfoByIdLiveData(queryRequest.getId()).observe(getViewLifecycleOwner(), new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                boolean finished = workInfo.getState().isFinished();
                if (finished) {
                    Data output = workInfo.getOutputData();
                    Log.d("ezaev", "onResume: " + output.toString());

                    clear_table();
                    fill_table(output);
                }
            }
        });

    }

    void fill_table(Data output) {
        int rows = output.getInt("qty", 0);
        String[] cols = output.getStringArray("cols");

        assert cols != null;

        TableRow titles = new TableRow(getContext());
        for (String name : cols) {
            TextView textView = new TextView(getContext());
            textView.setText(name);
            titles.addView(textView);
        }
        table_view.addView(titles);

        for (int i = 0; i < rows; i++) {
            TableRow row = new TableRow(getContext());
            String[] vals = output.getStringArray(String.valueOf(i));

            View.OnClickListener edit_action = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle b = new Bundle();
                    b.putString("name", name);
                    b.putString("path", path);
                    b.putString("type", type);
                    b.putString("table", table);
                    b.putStringArray("coords", vals);
                    Navigation.findNavController(requireView()).navigate(R.id.itemEditorFragment, b, new NavOptions.Builder()
                            .setEnterAnim(android.R.animator.fade_in)
                            .setExitAnim(android.R.animator.fade_out)
                            .build());
                }
            };

            if (vals != null) {
                for (String val : vals) {
                    Button button = new Button(getContext());
                    button.setText(val);
                    button.setOnClickListener(edit_action);
                    row.addView(button);
                }
            }
            table_view.addView(row);
        }

        spinner.setVisibility(View.GONE);
    }

    void clear_table() {
        table_view.removeAllViews();
    }


    //@Override
    //public void onChanged(List<WorkInfo> workInfos) {
    //    WorkInfo workInfo = workInfos.get(queryRequest.getId());
    //    Log.d("ezaev", "onResume: " + workInfos.size());

    // boolean finished = workInfo.getState().isFinished();
    // if (finished) {
    //
    // }
    //    }
    //});

        /*
        listOfWorkInfos -> {

            // If there are no matching work info, do nothing
            if (listOfWorkInfos == null || listOfWorkInfos.isEmpty()) {
                return;
            }

            // We only care about the first output status.
            // Every continuation has only one worker tagged TAG_OUTPUT
            listOfWorkInfos.size();
            WorkInfo workInfo = listOfWorkInfos.get(0);
            Log.d("ezaev", "onResume: " + listOfWorkInfos.size());

            boolean finished = workInfo.getState().isFinished();
            if (!finished) {
                //showWorkInProgress();

                Log.d("ezaev", "onResume: task in process");
                spinner.setVisibility(View.VISIBLE);
            } else {
                //showWorkFinished();

                Data output = workInfo.getOutputData();

                // Generate table
                /-*
                if (c.getCount() > 0) {
                        c.moveToFirst();
                        do {
                            TableRow row = new TableRow(getContext());
                            String[] al = new String[c.getColumnCount()];
                            for (int i = 0; i < c.getColumnCount(); i++) {
                                al[i] = c.getString(i);
                            }
                            View.OnClickListener edit_action = new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Bundle b = new Bundle();
                                    b.putString("name", name);
                                    b.putString("path", path);
                                    b.putString("type", type);
                                    b.putString("table", table);
                                    b.putStringArray("coords", al);
                                    Navigation.findNavController(getView()).navigate(R.id.itemEditorFragment, b, new NavOptions.Builder()
                                            .setEnterAnim(android.R.animator.fade_in)
                                            .setExitAnim(android.R.animator.fade_out)
                                            .build());
                                }
                            };
                            for (int i = 0; i < c.getColumnCount(); i++) {
                                Button button = new Button(getContext()); // c.getString(i)
                                button.setText(c.getString(i));
                                button.setOnClickListener(edit_action);
                                row.addView(button);
                            }
                            table_view.addView(row);
                        } while (c.moveToNext());
                    }
                *-/
                Log.d("ezaev", "onResume: " + output.toString());

                // empty.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.GONE);
                mWorkManager.cancelAllWork();
            }
        }
        */

    void runQuery(String request) {
        queryRequest =
                new OneTimeWorkRequest.Builder(DbWorker.class)
                        .setInputData(
                                new Data.Builder()
                                        .putString("name", name)
                                        .putString("table", table)
                                        .putString("path", path)
                                        .putString("type", type)
                                        .putString("query", request)
                                        .build()
                        )
                        .addTag("queryWork" + query)
                        .build();

        mWorkManager.enqueue(queryRequest);
    }

    /*
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
                    String[] al = new String[c.getColumnCount()];
                    for (int i = 0; i < c.getColumnCount(); i++) {
                        al[i] = c.getString(i);
                    }
                    View.OnClickListener edit_action = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle b = new Bundle();
                            b.putString("name", name);
                            b.putString("path", path);
                            b.putString("type", type);
                            b.putString("table", table);
                            b.putStringArray("coords", al);
                            Navigation.findNavController(getView()).navigate(R.id.itemEditorFragment, b, new NavOptions.Builder()
                                    .setEnterAnim(android.R.animator.fade_in)
                                    .setExitAnim(android.R.animator.fade_out)
                                    .build());
                        }
                    };
                    for (int i = 0; i < c.getColumnCount(); i++) {
                        Button button = new Button(getContext()); // c.getString(i)
                        button.setText(c.getString(i));
                        button.setOnClickListener(edit_action);
                        row.addView(button);
                    }
                    table_view.addView(row);
                } while (c.moveToNext());
            }
            c.close();
        } catch (SQLiteException e) {
            TextView tv = new TextView(getContext());
            tv.setText("В запросе допущена ошибка!");
            table_view.addView(tv);
        }



    }
     */
}