package com.example.cloudpos.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.example.cloudpos.R;
import com.example.cloudpos.adapters.GridAdapter;
import com.example.cloudpos.child_fragments.Cfragment_default_pay;
import com.example.cloudpos.data.Table;
import com.example.cloudpos.data.TableList;

import java.util.ArrayList;
import java.util.HashMap;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/*기존 테이블 배치도 나오는 Fragment*/
//implemented by Yang Insu

public class FragmentDefault extends Fragment{

    GridView gridView;
    GridAdapter adapter = new GridAdapter();

    int tno; //테이블 갯수

    public FragmentDefault(){
        //default public constructor
    }

    public interface TableSender{
        void sendTable(int no);
    }
    static TableSender tableSender;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        tno = TableList.getInstance().tableArrayList.size(); //테이블 갯수

        View defView = inflater.inflate(R.layout.default_fragment, container, false);


        gridView = defView.findViewById(R.id.defGrid);
        gridView.setAdapter(adapter);
        setSingleEvent(); //클릭 이벤트 처리함

        Toast.makeText(getContext(), String.valueOf(tno), Toast.LENGTH_SHORT).show();
        return defView;
    }

    private void setSingleEvent(){ //테이블 클릭 시 이벤트
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentTransaction transaction = getActivity()
                .getSupportFragmentManager().beginTransaction();
                Fragment fragment = Cfragment_default_pay.newInstance();
                ((Cfragment_default_pay) fragment).sendTable(position+1);
                transaction.replace(R.id.fragment_container,fragment);
                transaction.commit();
                Toast.makeText(getContext(), String.valueOf(position+1), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
