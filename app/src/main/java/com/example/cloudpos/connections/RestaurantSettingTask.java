package com.example.cloudpos.connections;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.cloudpos.MainActivity;
import com.example.cloudpos.data.MenuList;
import com.example.cloudpos.data.ReceiptList;
import com.example.cloudpos.data.Restaurant;
import com.example.cloudpos.data.TableList;
import com.example.cloudpos.data.TableStatusManager;

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


public class RestaurantSettingTask extends AsyncTask<Void, Void, JSONObject> {

    private final String TAG = "RestaurantSettingTask>>";
    private final static String RESTAURANT_DETAIL_TASK_URL = MiribomInfo.ipAddress + "/restaurants/";

    private int restaurantNo;
    private Context context;

    public RestaurantSettingTask(int restaurantNo, Context context) {
        this.restaurantNo = restaurantNo;
        this.context = context;
    }

    @Override
    protected JSONObject doInBackground(Void... voids) {

        HttpURLConnection conn = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(RESTAURANT_DETAIL_TASK_URL + this.restaurantNo);
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
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject data) {
        super.onPostExecute(data);
        Log.d(TAG, "onPostExecute: " + data.toString());

        try {
            if (data.getInt("code") == HttpURLConnection.HTTP_OK) {
                JSONObject restaurant = data.getJSONObject("restaurant");
                Restaurant.getInstance().setRestaurantDetail(restaurant);
//                        (restaurant.getInt("no"), restaurant.getString("name")
//                        , restaurant.getString("address"), restaurant.getDouble("longitude"), restaurant.getDouble("latitude")
//                        , restaurant.getString("image"), restaurant.getInt("food_type"));
//                RestaurantDetail restaurantDetail = RestaurantDetail.getInstance();
//                restaurantDetail.setRestaurant(curRestaurant);
//                restaurantDetail.setMobile(restaurant.getString("mobile"));
//                restaurantDetail.setReservationType(restaurant.getInt("reservation_type"));
//                restaurantDetail.setSeatType(restaurant.getInt("seat_type"));
//                String hoursStr = restaurant.getString("hours").replace("\\", "");
//                restaurantDetail.setHours(new JSONObject(hoursStr));
//                restaurantDetail.setOwnerRequest(restaurant.getString("owner_request"));
//                restaurantDetail.setOwnerNo(restaurant.getInt("o_no"));
//
                JSONArray menus = data.getJSONArray("menus");
                MenuList.getInstance().setMenuItemArrayList(menus);

                JSONArray places = data.getJSONArray("places");
                TableList.getInstance().setTableArrayList(places);

                JSONArray receipts = data.getJSONArray("receipts");
                ReceiptList.getInstance().setReceipts(receipts);

                /* 테이블의 주문 내역 상태 불러오기*/
                TableStatusManager.getInstance().initTableStatus();

                /* Not Use */
//                JSONArray reservables = data.getJSONArray("reservables");
//                restaurantDetail.setReservables(reservables);
//                JSONArray reservations = data.getJSONArray("reservations");
//                restaurantDetail.setReservations(reservations);

                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else {
                Toast.makeText(context, data.getString("message"), Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}

