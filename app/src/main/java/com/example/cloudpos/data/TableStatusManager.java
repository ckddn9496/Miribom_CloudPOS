package com.example.cloudpos.data;


import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TableStatusManager {
    private final static String TAG = "TableStatusManager>>>";
    private static TableStatusManager uniqueInstance = null;

    private final static String FILE_NAME = "table_status.txt";

    private TableStatusManager() {
    }

    public static TableStatusManager getInstance(Context context) {
        if (uniqueInstance == null) {
            uniqueInstance = new TableStatusManager();
            tableReceipts = new HashMap<>();
            file = getTempFile(context);
        }
        return uniqueInstance;
    }

    public static TableStatusManager getInstance() {
        return uniqueInstance;
    }

    private static HashMap<Integer, List<ReceiptLine>> tableReceipts;
    private static File file;
    private JSONObject tablesStatus;


    public void initTableStatus() {
        if (tablesStatus == null) {
            Log.d(TAG, "initTableStatus: 테이블 상태정보를 읽어오지 못했습니다.");
            return;
        }

        try {
            Iterator<String> it = tablesStatus.keys();
            while (it.hasNext()) {
                String key = it.next();
                JSONArray receiptLinesJsonArray = tablesStatus.getJSONObject(key).getJSONArray("receipt_lines");
                List<ReceiptLine> receiptLines = new ArrayList<>();
                for (int i = 0; i < receiptLinesJsonArray.length(); i++) {
                    JSONObject receiptLineJsonObject = receiptLinesJsonArray.getJSONObject(i);
                    String menuNo = receiptLineJsonObject.getString("menu_no");
                    String menuName = receiptLineJsonObject.getString("menu_name");
                    String menuPrice = receiptLineJsonObject.getString("menu_price");
                    String menuImage = receiptLineJsonObject.getString("menu_image");
                    int count = receiptLineJsonObject.getInt("count");
                    MenuItem menuItem = new MenuItem(menuNo, menuNo, menuName, menuPrice, menuImage);
                    ReceiptLine receiptLine = new ReceiptLine(menuItem, count);
                    receiptLines.add(receiptLine);
                }
                tableReceipts.put(Integer.parseInt(key), receiptLines);
            }
            Log.d(TAG, "initTableStatus: " + tableReceipts.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    // read all table receipt info from cache
    public void read() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line = "";
            StringBuffer stringBuffer = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                stringBuffer.append(line);
            }

            tablesStatus = new JSONObject(stringBuffer.toString());

            Log.d(TAG, "read: " + tablesStatus.toString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private static File getTempFile(Context context) {
        File file;
        file = new File(context.getCacheDir(), FILE_NAME);
        return file;
    }

    // write all table receipt info from cache
    public synchronized void write() {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            JSONObject mainJsonObject = new JSONObject();
            for (Map.Entry<Integer, List<ReceiptLine>> entry : tableReceipts.entrySet()) {
                int tableNo = (int) entry.getKey();
                List<ReceiptLine> receiptLines = (List<ReceiptLine>) entry.getValue();
                JSONObject tableStatusJsonObject = new JSONObject();
                JSONArray receiptLinesJsonArray = new JSONArray();
                for (int i = 0; i < receiptLines.size(); i++) {
                    ReceiptLine receiptLine = receiptLines.get(i);
                    JSONObject line = new JSONObject();
                    line.put("menu_no", receiptLine.getMenuItem().getMenuNo()); // 메뉴
                    line.put("menu_price", receiptLine.getOnePrice());
                    line.put("menu_name", receiptLine.getMenuName());
                    line.put("menu_image", receiptLine.getMenuItem().getImageUrl());
                    line.put("count", receiptLine.getItemNo()); // 갯수
                    receiptLinesJsonArray.put(line);
                }
//                tableStatusJsonObject.put("table_no", tableNo);
                tableStatusJsonObject.put("receipt_lines", receiptLinesJsonArray);
                mainJsonObject.accumulate(String.valueOf(tableNo), tableStatusJsonObject);
            }
            Log.d(TAG, "write: mainJSONObject: " + mainJsonObject.toString());
            writer.write(mainJsonObject.toString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void changeStatus(int tableNo, List<ReceiptLine> receiptLines) {
        tableReceipts.put(tableNo, receiptLines);
        Log.d(TAG, "changeStatus: " + tableReceipts.toString());
        write();
    }

    public List<ReceiptLine> getSelectedReceiptLine(int tableNo) {
        return this.tableReceipts.get(tableNo);
    }

    public void removeSelectedTableReceiptLines(int tableNo) {
        this.tableReceipts.remove(tableNo);
        write();
    }

    public ReceiptLine removeSelectedReceiptLine(int tableNo, String menuName) {
        ReceiptLine receiptLine = null;
        List<ReceiptLine> receiptLines = this.tableReceipts.get(tableNo);
        for (int i = 0; i < receiptLines.size(); i++) {
            if (receiptLines.get(i).getMenuName().equals(menuName)) {
                receiptLine = receiptLines.remove(i);
                break;
            }
        }
        write();
        return receiptLine;
    }

    public void increaseSelectedReceiptLine(int tableNo, String menuName) {
        List<ReceiptLine> receiptLines = this.tableReceipts.get(tableNo);
        for (int i = 0; i < receiptLines.size(); i++) {
            if (receiptLines.get(i).getMenuName().equals(menuName)) {
                receiptLines.get(i).incRecNo();
                break;
            }
        }
        write();
    }

    public void decreaseSelectedReceiptLine(int tableNo, String menuName) {
        List<ReceiptLine> receiptLines = this.tableReceipts.get(tableNo);
        for (int i = 0; i < receiptLines.size(); i++) {
            if (receiptLines.get(i).getMenuName().equals(menuName)) {
                receiptLines.get(i).decRecNo();
                break;
            }
        }
        write();
    }
}