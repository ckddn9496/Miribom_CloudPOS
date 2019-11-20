package com.example.cloudpos.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cloudpos.R;
import com.example.cloudpos.data.TableList;

import static com.example.cloudpos.R.layout.table;

public class GridAdapter extends BaseAdapter {

    LayoutInflater inflater;
    Activity act;
    private Context mContext;


    public GridAdapter() {

    }

    @Override
    public int getCount() {
        return TableList.getInstance().tableArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return TableList.getInstance().tableArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = (View) inflater.inflate(table, viewGroup, false);
        }
        TextView textView = (TextView) view.findViewById(R.id.tableNoTV);
        textView.setText("테이블 " + TableList.getInstance().tableArrayList.get(i).getTableNo().toString());


        return view;
    }
}
