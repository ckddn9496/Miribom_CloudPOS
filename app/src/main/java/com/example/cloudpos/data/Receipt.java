package com.example.cloudpos.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Receipt {
    private int receiptNo;

    private String time;
    private ArrayList<ReceiptLine> receiptLines = new ArrayList<>();
    private int totalPrice;
    private int tableNo;
    private int payType;
    private int recType; //결제인지=0 환불인지=1
    private int originalCode;

    public Receipt(String time, ArrayList<ReceiptLine> receiptLines, int tableNo, int recType) {
        this.time = time;
        this.receiptLines = receiptLines;
        this.tableNo = tableNo;
        this.recType = recType;

        for (int i = 0; i < receiptLines.size(); i++) {
            totalPrice += receiptLines.get(i).getTotPrice();
        }
    }

    public Receipt(JSONObject data) throws JSONException {
        this.receiptNo = data.getInt("no");
        this.tableNo = data.getInt("p_no");
        this.time = data.getString("time");
        this.recType = data.getInt("type");
        this.receiptLines.clear();

        JSONArray receiptLines = data.getJSONArray("receiptlines");
        for (int i = 0; i < receiptLines.length(); i++) {
            JSONObject receiptLineObject = receiptLines.getJSONObject(i);
            ReceiptLine receiptLine = new ReceiptLine(receiptNo, receiptLineObject);
            this.totalPrice += receiptLine.getTotPrice();
            this.receiptLines.add(receiptLine);
        }
    }

    public int getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(int receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getTime() {
        return this.time;
    }

    public int getTotalPrice() {
        return this.totalPrice;
    }

    public int getRecType() {
        return this.recType;
    }

    public int getTableNo() {
        return this.tableNo;
    }

    public void setRecType(int type) {
        recType = type;
    }

    public ArrayList<ReceiptLine> getReceiptLines() {
        return this.receiptLines;
    }

    //서버용 함수
    public void setOriginalCode(int originalCode) {
        this.originalCode = originalCode;
    }

    @Override
    public String toString() {
        return "영수증 번호: " + receiptNo + " 테이블 번호: " + tableNo + " 총 가격: " + totalPrice + "결제 타입: " + recType + receiptLines.toString();
    }
}
