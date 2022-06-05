package com.egorzaev.dbeditor.fragment;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.egorzaev.dbeditor.R;

public class ReferenceFragment extends Fragment {

    public ReferenceFragment() {
    }

    TextView tv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reference, container, false);
        tv = view.findViewById(R.id.ref_tv);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tv.setText(Html.fromHtml(getResources().getString(R.string.ref_text), Html.FROM_HTML_MODE_LEGACY));
        } else {
            tv.setText(Html.fromHtml(getResources().getString(R.string.ref_text)));
        }
        return view;
    }
}