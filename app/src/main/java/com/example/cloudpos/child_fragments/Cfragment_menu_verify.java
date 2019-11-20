package com.example.cloudpos.child_fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cloudpos.R;
import com.example.cloudpos.data.MenuItem;
import com.example.cloudpos.data.MenuList;
import com.example.cloudpos.fragments.FragmentMenu;

import java.util.ArrayList;

/*메뉴 관리 Fragment에서 각 메뉴들을 조회하는 기능 담당 Fragment*/
//implmented by Yang Insu

public class Cfragment_menu_verify extends Fragment implements FragmentMenu.Transmitter {

    Bitmap menuImage;
    String menuNo;
    String menuCode;
    String menuName;
    String menuImageUrl;
    String menuPrice;

    private int index;
    private MenuItem menuItem = new MenuItem();
    ArrayList<MenuItem> menuList = MenuList.getInstance().menuItemArrayList;


    public static Cfragment_menu_verify newInstance(){
        return new Cfragment_menu_verify();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View fv = inflater.inflate(R.layout.menu_verify_cfragment,container,false);



        //XML View 모듈들과 연결
        ImageView menuImageView = fv.findViewById(R.id.vmenuImage);
        TextView menu_no_TV = (TextView)fv.findViewById(R.id.vmenu_no_TV);
        TextView menu_code_TV = (TextView)fv.findViewById(R.id.vmenu_code_TV);
        TextView menu_name_TV = (TextView)fv.findViewById(R.id.vmenu_name_TV);
        TextView menu_price_TV = (TextView)fv.findViewById(R.id.vmenu_price_TV);

        //기입된 정보 연결 (EditText이나 메뉴 정보 조회하는 Fragment이기 때문에 해당 EditText는 편집불가
        EditText menu_no_ET = (EditText)fv.findViewById(R.id.vmenu_no_entry);
        menu_no_ET.setText(menuNo);
        EditText menu_code_ET = (EditText)fv.findViewById(R.id.vmenu_code_entry);
        menu_code_ET.setText(menuCode);
        EditText menu_name_ET = (EditText)fv.findViewById(R.id.vmenu_name_entry);
        menu_name_ET.setText(menuName);
        EditText menu_price_ET = (EditText)fv.findViewById(R.id.vmenu_price_entry);
        menu_price_ET.setText(menuPrice);

        menuImage = menuList.get(index).getMenuImage();

        Glide.with(getContext()).load(menuImageUrl).into(menuImageView);
        if (menuImage != null)
            menuImageView.setImageBitmap(menuImage);

        return fv;
    }

    @Override
    public void transmit(Bundle bundle) {
        menuNo = bundle.getString("menuNo");
        menuCode = bundle.getString("menuCode");
        menuName = bundle.getString("menuName");
        menuPrice = bundle.getString("menuPrice");
        menuImageUrl = bundle.getString("menuImageUrl");
        index = bundle.getInt("index");


    }
}
