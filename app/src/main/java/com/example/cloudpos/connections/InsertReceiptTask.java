package com.example.cloudpos.connections;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.cloudpos.data.Receipt;
import com.example.cloudpos.data.ReceiptLine;
import com.example.cloudpos.data.ReceiptList;
import com.example.cloudpos.data.Restaurant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class InsertReceiptTask extends AsyncTask<Void, Void, JSONObject> {

    private final String TAG = "InsertReceiptTask>>>";
    private final static String SIGN_UP_TASK_URL = MiribomInfo.ipAddress + "/receipts/create";

    private Receipt receipt;
    private Context context;


    public InsertReceiptTask(Receipt receipt, Context context) {
        this.receipt = receipt;
        this.context = context;
    }

    @Override
    protected JSONObject doInBackground(Void... voids) {
        try {
            JSONObject postData = new JSONObject();
            postData.put("r_no", Restaurant.getInstance().getRegisterNo());
            postData.put("p_no", receipt.getTableNo());
            postData.put("time", receipt.getTime());
            postData.put("type", receipt.getRecType());
            JSONArray receiptLinesArray = new JSONArray();
            List<ReceiptLine> receiptLines = receipt.getReceiptLines();
            for (int i = 0; i < receiptLines.size(); i++) {
                ReceiptLine receiptLine = receiptLines.get(i);
                JSONObject receiptLineObject = new JSONObject();
                receiptLineObject.put("menu_no", receiptLine.getMenuItem().getMenuNo());
                receiptLineObject.put("count", receiptLine.getItemNo());
                receiptLineObject.put("price", receiptLine.getTotPrice());
                receiptLinesArray.put(receiptLineObject);
            }
            postData.put("receiptline", receiptLinesArray);


            HttpURLConnection conn = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(SIGN_UP_TASK_URL);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Cache-Control", "no-cache");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/text");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.connect();

                OutputStream outputStream = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
                writer.write(postData.toString());
                writer.flush();
                writer.close();

                InputStream stream = conn.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                return new JSONObject(buffer.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    protected void onPostExecute(JSONObject data) {
        super.onPostExecute(data);
        Log.d(TAG, "onPostExecute: " + data.toString());
        try {
            if (data.getInt("code") == HttpURLConnection.HTTP_OK) {
                Toast.makeText(context, data.getString("message"), Toast.LENGTH_LONG).show();
                Log.d(TAG, "onPostExecute: 영수증 생성 완료");
                receipt.setReceiptNo(data.getInt("receipt_no"));
                ReceiptList.getInstance().receipts.add(receipt);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
