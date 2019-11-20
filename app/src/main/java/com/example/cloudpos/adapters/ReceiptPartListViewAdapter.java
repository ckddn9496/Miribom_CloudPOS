package com.example.cloudpos.adapters;

/*영수증 출력 때 필요한 부분의 어댑터*/
//implemented by Yang Insu

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cloudpos.R;
import com.example.cloudpos.data.Receipt;
import com.example.cloudpos.data.ReceiptLine;

import java.util.ArrayList;
import java.util.List;


public class ReceiptPartListViewAdapter extends BaseAdapter {

    List<ReceiptLine> receiptLines = new ArrayList<>();


    public ReceiptPartListViewAdapter(Receipt receipt){
        receiptLines = receipt.getReceiptLines();
    }

    @Override
    public int getCount() {
        return receiptLines.size();
    }

    @Override
    public Object getItem(int position) {
        return receiptLines.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.receipt_view_liner,parent,false);
        }

        TextView itemNameTV = convertView.findViewById(R.id.receipt_view_liner_itemNameTV);
        TextView itemCountTV = convertView.findViewById(R.id.receipt_view_liner_itemCountTV);
        TextView itemOnePriceTV = convertView.findViewById(R.id.receipt_view_liner_oneItemPriceTV);
        TextView itemTotalPriceTV = convertView.findViewById(R.id.receipt_view_liner_totItemPriceTV);

        receiptLines.get(position);

        itemNameTV.setText(receiptLines.get(position).getMenuName());
        itemCountTV.setText(String.valueOf(receiptLines.get(position).getItemNo()));
        itemOnePriceTV.setText(String.valueOf(receiptLines.get(position).getOnePrice()));
        itemTotalPriceTV.setText(String.valueOf(receiptLines.get(position).getTotPrice()));


        return convertView;
    }
}
