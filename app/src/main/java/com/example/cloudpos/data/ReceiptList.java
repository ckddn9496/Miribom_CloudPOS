package com.example.cloudpos.data;

/*Receipt List*/
//implemented by Yang Insu

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReceiptList {
    public static final int RECEIPT_COMPLETE = 1;
    public static final int RECEIPT_REFUND = 0;


    private static ReceiptList receiptList = new ReceiptList();
    public ArrayList<Receipt> receipts = new ArrayList<>();

    private ReceiptList(){

    }

    public static ReceiptList getInstance(){
        return receiptList;
    }

    public void setReceipts(JSONArray receipts) throws JSONException {
        for (int i = 0; i < receipts.length(); i++) {
            JSONObject receipt = receipts.getJSONObject(i);
            this.receipts.add(new Receipt(receipt));
        }
    }

}
