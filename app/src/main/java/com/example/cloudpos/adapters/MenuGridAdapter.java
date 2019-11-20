package com.example.cloudpos.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cloudpos.R;
import com.example.cloudpos.data.MenuItem;
import com.example.cloudpos.data.MenuList;

public class MenuGridAdapter extends BaseAdapter {

    LayoutInflater inflater;
    Activity act;
    private Context context;

    @Override
    public int getCount() {
        return MenuList.getInstance().menuItemArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return MenuList.getInstance().menuItemArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.grid_menu_item,parent,false);
        }

        TextView menuNameTV = convertView.findViewById(R.id.gMenuItem_NameTV);
        TextView menuPriceTV = convertView.findViewById(R.id.gMenuItem_PriceTV);
        menuNameTV.setText(MenuList.getInstance().menuItemArrayList.get(position).getMenuName());
        menuPriceTV.setText(MenuList.getInstance().menuItemArrayList.get(position).getMenuPrice());



        return convertView;
    }
}
