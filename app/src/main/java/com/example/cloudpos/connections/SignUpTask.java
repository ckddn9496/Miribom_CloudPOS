package com.example.cloudpos.connections;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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

public class SignUpTask extends AsyncTask<Void, Void, JSONObject> {

    private final String TAG = "SignUpTask>>>";
    private final static String SIGN_UP_TASK_URL = MiribomInfo.ipAddress + "/owners/register";

    private String id, pw, name, mobile;

    private Context context;

    public SignUpTask(String id, String pw, String name, String mobile, Context context) {
        this.id = id;
        this.pw = pw;
        this.name = name;
        this.mobile = mobile;
        this.context = context;
    }


    @Override
    protected JSONObject doInBackground(Void... voids) {
        try {
            JSONObject postData = new JSONObject();
            postData.put("id", this.id);
            postData.put("pw", this.pw);
            postData.put("name", this.name);
            postData.put("mobile", this.mobile);

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
//        try {
//            Toast.makeText(context, data.getString("message"), Toast.LENGTH_LONG).show();
//            if (data.getInt("code") == HttpURLConnection.HTTP_OK) {
//                Intent intent = new Intent(context, LoginActivity.class);
//                intent.putExtra("id", this.id);
//                context.startActivity(intent);
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

    }
}
