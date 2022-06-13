package com.egorzaev.dbeditor.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.egorzaev.dbeditor.R;

public class LearningFragment extends Fragment {


    public LearningFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_learning, container, false);


        return view;
    }
}