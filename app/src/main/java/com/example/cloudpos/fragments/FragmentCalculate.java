package com.example.cloudpos.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.cloudpos.R;

/*간단 정산 Fragment*/
//implemented by Yang Insu


public class FragmentCalculate extends Fragment {

    public FragmentCalculate(){
        //default public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.calculate_fragment, container, false);
    }

}