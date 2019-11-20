package com.example.cloudpos.connections;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.cloudpos.data.MenuItem;
import com.example.cloudpos.data.Restaurant;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class InsertMenuTask {

    private final String TAG = "InsertMenuTask>>>";
    private final static String INSERT_MENU_TASK_URL = MiribomInfo.ipAddress + "/menus/register";

    private String restaurantNo;
    private String name;
    private String price;
    private String absolutePath;
    private String imageName;
    private File imageFile;

    private MenuItem menuItem;

    public InsertMenuTask(String restaurantNo, String name, String price, String image) {
        this.restaurantNo = restaurantNo;
        this.name = name;
        this.price = price;
        this.absolutePath = image;
        this.imageName = image.split("/")[image.split("/").length - 1];
        this.imageFile = new File(image);
    }

    public InsertMenuTask(MenuItem newMenuItem, String image) {
        this.menuItem = newMenuItem;
        this.restaurantNo = Restaurant.getInstance().getRegisterNo();
        this.name = newMenuItem.getMenuName();
        this.price = newMenuItem.getMenuPrice();
        this.absolutePath = image;
        this.imageName = image.split("/")[image.split("/").length - 1];
        this.imageFile = new File(image);
    }

    public void insertMenu() {

        Log.d(TAG, "doInBackground: " + imageName);

        OkHttpClient client = new OkHttpClient();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(60, TimeUnit.SECONDS);
        builder.readTimeout(60, TimeUnit.SECONDS);
        builder.writeTimeout(60, TimeUnit.SECONDS);

        client = builder.build();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("r_no", this.restaurantNo)
                .addFormDataPart("name", this.name)
                .addFormDataPart("price", this.price)
                .addFormDataPart("image", imageName
                        , RequestBody.create(MultipartBody.FORM, new File(absolutePath)))
                .build();

        Request request = new Request.Builder()
                .url(INSERT_MENU_TASK_URL)
                .post(requestBody)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("TEST : " + response.body().string());
            }
        });



    }


}
