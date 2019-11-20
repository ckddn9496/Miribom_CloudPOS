package com.example.cloudpos.child_fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cloudpos.R;

public class Cfragment_menu_edit extends Fragment implements View.OnClickListener {
    String menuNo;
    String menuCode;
    String menuName;
    String menuPrice;

    public static Cfragment_menu_edit newInstance(){
        return new Cfragment_menu_edit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View fv = inflater.inflate(R.layout.menu_edit_cfragment,container,false);

        /*XML View 모듈들과 연결 시작*/
        TextView menu_no_TV = (TextView)fv.findViewById(R.id.emenu_no_TV);
        TextView menu_code_TV = (TextView)fv.findViewById(R.id.emenu_code_TV);
        TextView menu_name_TV = (TextView)fv.findViewById(R.id.emenu_name_TV);
        TextView menu_price_TV = (TextView)fv.findViewById(R.id.emenu_price_TV);

        //기입된 정보 연결
        EditText menu_no_ET = (EditText)fv.findViewById(R.id.emenu_no_entry);
        menu_no_ET.setText(menuNo);
        EditText menu_code_ET = (EditText)fv.findViewById(R.id.emenu_code_entry);
        menu_code_ET.setText(menuCode);
        EditText menu_name_ET = (EditText)fv.findViewById(R.id.emenu_name_entry);
        menu_name_ET.setText(menuName);
        EditText menu_price_ET = (EditText)fv.findViewById(R.id.emenu_price_entry);
        menu_price_ET.setText(menuPrice);

        //Button
        Button menu_edit_Btn = (Button)fv.findViewById(R.id.menu_edit_cbtn);
        menu_edit_Btn.setOnClickListener(this);
        /*XML View 모듈들과 연결 끝*/

        return fv;
    }


    @Override
    public void onClick(View v) {
        /*기입 정보 비었을때 예외처리 해놓기*/
    }
}
