package com.example.cloudpos.connections;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.cloudpos.adapters.ReceiptListViewAdapter;
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

public class RefundReceiptTask extends AsyncTask<Void, Void, JSONObject> {

    private final String TAG = "RefundReceiptTask>>>";
    private final static String REFUND_RECEIPT_TASK_URL = MiribomInfo.ipAddress + "/receipts/refund";

    private Receipt receipt;
    private int position;
    private Context context;
    private ReceiptListViewAdapter receiptListViewAdapter;


    public RefundReceiptTask(ReceiptListViewAdapter receiptListViewAdapter, int position, Context context) {
        this.receiptListViewAdapter = receiptListViewAdapter;
        this.receipt = receiptListViewAdapter.receipts.get(position);
        this.position = position;
        this.context = context;
    }

    @Override
    protected JSONObject doInBackground(Void... voids) {
        try {
            JSONObject postData = new JSONObject();
            postData.put("no", receipt.getReceiptNo());

            HttpURLConnection conn = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(REFUND_RECEIPT_TASK_URL);
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
            Toast.makeText(context, data.getString("message"

            ), Toast.LENGTH_LONG).show();
            if (data.getInt("code") == HttpURLConnection.HTTP_OK) {

                Receipt receipt = receiptListViewAdapter.receipts.get(position);
//                Log.d(TAG, "onPostExecute: 선택한 영수증\n" + receipt);
                receipt.setRecType(ReceiptList.RECEIPT_REFUND);
//                Log.d(TAG, "onPostExecute: 환불 처리\n" + receipt);

                receiptListViewAdapter.setItem(position, receipt);

//                Log.d(TAG, "onPostExecute: " + receiptListViewAdapter.receipts.toString());
                receiptListViewAdapter.notifyDataSetChanged();
                Log.d(TAG, "onPostExecute: 영수증 환불 완료");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
