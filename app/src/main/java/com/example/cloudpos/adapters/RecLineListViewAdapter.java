package com.example.cloudpos.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cloudpos.R;
import com.example.cloudpos.data.ReceiptLine;
import com.example.cloudpos.data.TableStatusManager;

import java.util.ArrayList;
import java.util.List;


/*implemented by Yang Insu*/
//영수증 라인 찍히는거 보여주는 ListView 어댑터

public class RecLineListViewAdapter extends BaseAdapter {
    private final static String TAG = "RecLineListViewAdapter>";

    public int tableNo;
    public List<ReceiptLine> receiptLineArrayList = new ArrayList<>();

    public RecLineListViewAdapter(int tableNo) {
        this.tableNo = tableNo;
    }

    public void addRecLine(ReceiptLine receiptLine) {
        for (int i = 0; i < receiptLineArrayList.size(); i++) {
            if (receiptLine.getMenuName().equals(receiptLineArrayList.get(i).getMenuName())) {
                receiptLineArrayList.get(i).incRecNo();
                TableStatusManager.getInstance().changeStatus(tableNo, receiptLineArrayList);
                return;
            }
        }

        receiptLineArrayList.add(receiptLine);
        TableStatusManager.getInstance().changeStatus(tableNo, receiptLineArrayList);
    }

    // 테이블을 처음 눌렀을 때
    public void addRecLines(List<ReceiptLine> receiptLines) {
        receiptLineArrayList.clear();
        if (receiptLines != null)
            receiptLineArrayList.addAll(receiptLines);

        Log.d(TAG, "addRecLines: " + this.receiptLineArrayList);
    }

    public void increaseSelectedReceiptLine(String menuName) {
        for (int i = 0; i < receiptLineArrayList.size(); i++) {
            if (receiptLineArrayList.get(i).getMenuName().equals(menuName)) {
                receiptLineArrayList.get(i).incRecNo();
                break;
            }
        }
        TableStatusManager.getInstance().changeStatus(tableNo, receiptLineArrayList);
    }

    public void decreaseSelectedReceiptLine(String menuName) {
        for (int i = 0; i < receiptLineArrayList.size(); i++) {
            if (receiptLineArrayList.get(i).getMenuName().equals(menuName)) {
                receiptLineArrayList.get(i).decRecNo();
                break;
            }
        }
        TableStatusManager.getInstance().changeStatus(tableNo, receiptLineArrayList);
    }

    public void removeReceiptLine(String menuName) {
        for (int i = 0; i < receiptLineArrayList.size(); i++) {
            if (receiptLineArrayList.get(i).getMenuName().equals(menuName)) {
                receiptLineArrayList.remove(i);
                break;
            }
        }
    }

    public void clear() {
        this.receiptLineArrayList.clear();
    }

    public int getTotalPrice() {
        int totalPrice = 0;
        for (ReceiptLine receiptLine: receiptLineArrayList) {
            totalPrice += receiptLine.getTotPrice();
        }
        return totalPrice;
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

    public void addItem(ReceiptLine receiptLine) { //더 구현 필요
        receiptLineArrayList.add(receiptLine);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.receipt_line, parent, false);
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
