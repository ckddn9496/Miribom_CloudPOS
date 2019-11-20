package com.example.cloudpos.connections;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.cloudpos.MainActivity;
import com.example.cloudpos.data.Restaurant;

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

public class SignInTask extends AsyncTask<Void, Void, JSONObject> {
    private final String TAG = "SignInTask>>>";
    private final static String SIGN_IN_TASK_URL = MiribomInfo.ipAddress + "/owners/login";

    private String id, pw;

    private Context context;

    public SignInTask(String id, String pw, Context context) {
        this.id = id;
        this.pw = pw;
        this.context = context;
    }

    @Override
    protected JSONObject doInBackground(Void... voids) {
        try {
            JSONObject postData = new JSONObject();
            postData.put("id", this.id);
            postData.put("pw", this.pw);

            HttpURLConnection conn = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(SIGN_IN_TASK_URL);
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
                Log.d(TAG, "onPostExecute: " + data.toString());
                Toast.makeText(context, data.getString("message"), Toast.LENGTH_LONG).show();
                if (data.getInt("code") == HttpURLConnection.HTTP_OK) {
                    if (Restaurant.getInstance().getRegisterNo().equals("")) {
                        Restaurant.getInstance().setName(data.getString("owner_name"));
                        RestaurantSettingTask restaurantSettingTask = new RestaurantSettingTask(data.getInt("r_no"), context);
                        restaurantSettingTask.execute();
                    } else {
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }


}
