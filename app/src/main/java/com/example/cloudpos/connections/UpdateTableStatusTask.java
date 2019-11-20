package com.example.cloudpos.connections;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.cloudpos.data.Restaurant;
import com.example.cloudpos.data.TableList;

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

public class UpdateTableStatusTask extends AsyncTask<Void, Void, JSONObject> {
    private final String TAG = "UpdateTableStatusTask>";
    private final static String UPDATE_TABLE_STATUS_TASK_URL = MiribomInfo.ipAddress + "/places/update";

    private int tableNo;
    private int isTaken;

    private Context context;

    public UpdateTableStatusTask(int tableNo, int isTaken, Context context) {
        this.tableNo = tableNo;
        this.isTaken = isTaken;
        this.context = context;
    }

    @Override
    protected JSONObject doInBackground(Void... voids) {
        try {
            JSONObject postData = new JSONObject();
            postData.put("r_no", Restaurant.getInstance().getRegisterNo());
            postData.put("no", tableNo);
            postData.put("istaken", isTaken);

            HttpURLConnection conn = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(UPDATE_TABLE_STATUS_TASK_URL);
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
        if (data == null) {
            Toast.makeText(context, "서버로 부터 응답이 없습니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_LONG).show();
        } else {
            try {
                if (data.getInt("code") == HttpURLConnection.HTTP_OK) {
                    Log.d(TAG, "onPostExecute: 성공");
                    TableList.getInstance().tableArrayList.get(tableNo - 1).setIsTaken(isTaken);
                } else {
                    Toast.makeText(context, data.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


}
