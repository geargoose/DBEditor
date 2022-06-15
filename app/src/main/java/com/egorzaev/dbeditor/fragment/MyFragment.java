package com.egorzaev.dbeditor.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.egorzaev.dbeditor.R;

public class MyFragment extends Fragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {  // Основа, которую наследуют все фрагменты моего приложения
        Toolbar myToolbar = view.findViewById(R.id.myToolbar);
        myToolbar.setTitle(R.string.app_name);
        myToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            } // При нажатии кнопки "назад" на ToolBar'е, возвращаемся к предыдущему фрагменту в BackStack
        });
    }
}
