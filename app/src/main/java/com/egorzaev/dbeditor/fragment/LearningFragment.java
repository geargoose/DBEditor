package com.egorzaev.dbeditor.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.egorzaev.dbeditor.R;

public class LearningFragment extends MyFragment {


    public LearningFragment() {
    }

    ImageButton btn_select_1;
    ImageButton btn_select_2;
    ImageButton btn_insert_1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { // Выполнение кода для каждого CardView с кодом:
        View view = inflater.inflate(R.layout.fragment_learning, container, false);

        btn_select_1 = view.findViewById(R.id.btn_select_1);
        btn_select_2 = view.findViewById(R.id.btn_select_2);
        btn_insert_1 = view.findViewById(R.id.btn_insert_1);

        btn_select_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigate(getResources().getString(R.string.ref_select_1_code), view);
            }
        });

        btn_select_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigate(getResources().getString(R.string.ref_select_2_code), view);
            }
        });

        btn_insert_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigate(getResources().getString(R.string.ref_insert_1_code), view);
            }
        });

        return view;
    }

    void navigate(String query, View view) {  // Ф-ция, упрощающая выполнение запроса к БД и перехода к фрагменту просмотра таблицы
        Bundle b = new Bundle();
        b.putString("name", "sandbox");
        b.putString("path", "sandbox");
        b.putString("type", "local");
        b.putString("query", query);
        Navigation.findNavController(view).navigate(R.id.tableViewFragment, b, new NavOptions.Builder()
                .setEnterAnim(android.R.animator.fade_in)
                .setExitAnim(android.R.animator.fade_out)
                .build());
    }
}