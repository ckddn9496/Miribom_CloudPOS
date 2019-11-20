package com.example.cloudpos.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cloudpos.R;
import com.example.cloudpos.data.ReceiptLine;

import java.util.ArrayList;


/*implemented by Yang Insu*/
//영수증 라인 찍히는거 보여주는 ListView 어댑터

public class RecLineListViewAdapter extends BaseAdapter {

    public ArrayList<ReceiptLine> receiptLineArrayList = new ArrayList<>();

    public void addRecLine(ReceiptLine receiptLine){
        for(int i = 0;i<receiptLineArrayList.size();i++){
            if(receiptLine.getMenuName() == receiptLineArrayList.get(i).getMenuName()) {
                receiptLineArrayList.get(i).incRecNo();
                return;
            }
        }


        receiptLineArrayList.add(receiptLine);


    }

    @Override
    public int getCount() {
        return receiptLineArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return receiptLineArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(ReceiptLine receiptLine){ //더 구현 필요
        receiptLineArrayList.add(receiptLine);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.receipt_line,parent,false);
        }

        //View 위젯들 연결
        TextView recLineNoTV = convertView.findViewById(R.id.recLine_noTV);
        TextView recLineMenuNameTV = convertView.findViewById(R.id.recLine_menuNameTV);
        TextView recLineOnePriceTV = convertView.findViewById(R.id.recLine_onePriceTV);
        TextView recLineMenuCountTV = convertView.findViewById(R.id.recLine_menuCountTV);
        TextView recLineTotPriceTV = convertView.findViewById(R.id.recLine_totPriceTV);

        ReceiptLine oneReceiptLine = receiptLineArrayList.get(position);


        recLineNoTV.setText(oneReceiptLine.getRecNo());
        recLineMenuNameTV.setText(oneReceiptLine.getMenuName());
        recLineOnePriceTV.setText(String.valueOf(oneReceiptLine.getOnePrice()));
        recLineMenuCountTV.setText(String.valueOf(oneReceiptLine.getItemNo()));
        recLineTotPriceTV.setText(String.valueOf(oneReceiptLine.getTotPrice()));

        return convertView;
    }
}
