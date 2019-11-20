package com.example.cloudpos.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cloudpos.R;
import com.example.cloudpos.data.Receipt;
import com.example.cloudpos.data.ReceiptList;

import java.util.ArrayList;

public class ReceiptListViewAdapter extends BaseAdapter {

    public ArrayList<Receipt> receipts = ReceiptList.getInstance().receipts;

    public ReceiptListViewAdapter(){

    }

    public void delReceipt(int position){
        receipts.remove(position);
    }

    @Override
    public int getCount() {
        return receipts.size();
    }

    @Override
    public Object getItem(int position) {
        return receipts.get(position);
    }

    public void setItem(int position, Receipt receipt) {
        this.receipts.set(position, receipt);
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
            convertView = inflater.inflate(R.layout.receipt_item,parent,false);

        }
        TextView receiptNoTV = convertView.findViewById(R.id.receiptNoTV);
        TextView receiptTableNoTV = convertView.findViewById(R.id.receiptTableNoTV);
        TextView receiptPayPriceTV = convertView.findViewById(R.id.receiptPayPriceTV);
        TextView receiptTypeTV = convertView.findViewById(R.id.receiptTypeTV);

        Receipt oneReceipt = receipts.get(position);

        receiptNoTV.setText(String.valueOf(position + 1));
        receiptTableNoTV.setText(String.valueOf(oneReceipt.getTableNo()));
        receiptPayPriceTV.setText(String.valueOf(oneReceipt.getTotalPrice()));
        receiptTypeTV.setText(String.valueOf(oneReceipt.getRecType()));

        return convertView;
    }
}
