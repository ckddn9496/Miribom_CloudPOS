package com.example.cloudpos.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cloudpos.R;
import com.example.cloudpos.data.MenuItem;
import com.example.cloudpos.data.MenuList;

import java.util.ArrayList;

/*Menu Fragment에서 식당 전체 메뉴 리스트 관리 Adapter*/
//주의사항: 수정 시 MenuItem 주의 기존 안드로이드 view 패키지에 동일한 이름으로 같은 클래스 있어서 cloudpos.data의 MenuItem을 사용하자

//implemented by Yang Insu

public class MenuListViewAdapter extends BaseAdapter{

    private ArrayList<MenuItem> menulistViewItemList = MenuList.getInstance().menuItemArrayList; //싱글톤 객체 생성


    //가짜 addItem 함수 - 나중에 서버에서 받아오는 형식 대로 하기
   public void addItem(String no, String code, String name, String price) {
        MenuItem item = new MenuItem();
        item.setMenuNo(no);
        item.setMenuCode(code);
        item.setMenuName(name);
        item.setMenuPrice(price);
        menulistViewItemList.add(item);

    }

    //아이템 삭제 함수
    public void delItem(int pos){
       menulistViewItemList.remove(pos);
    }

    @Override
    public int getCount() {
        return menulistViewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return menulistViewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.menu_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView menuNoTextView = (TextView) convertView.findViewById(R.id.menu_no);
        TextView menuCodeTextView = (TextView) convertView.findViewById(R.id.menu_code);
        TextView menuNameTextView = (TextView) convertView.findViewById(R.id.menu_name);
        TextView menuPriceTextView = (TextView) convertView.findViewById(R.id.menu_price);

        // Data Set(filteredItemList)에서 position에 위치한 데이터 참조 획득
        MenuItem menuItem = menulistViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        menuNoTextView.setText(menuItem.getMenuNo());
        menuCodeTextView.setText(menuItem.getMenuCode());
        menuNameTextView.setText(menuItem.getMenuName());
        menuPriceTextView.setText(menuItem.getMenuPrice());
        return convertView;
    }


}
