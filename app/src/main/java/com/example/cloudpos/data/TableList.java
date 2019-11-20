package com.example.cloudpos.data;

/*Table List*/
//implemented by Yang Insu

//싱글톤 객체로 모든 fragment에서 참조할 수 있게 구현함

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TableList {
    private static TableList tableList = new TableList();
    public ArrayList<Table> tableArrayList = new ArrayList<>();

    private TableList(){
    }

    public static TableList getInstance(){
        return tableList;
    }

    public void setTableArrayList(JSONArray tableArrayList) throws JSONException {
        for (int i = 0; i < tableArrayList.length(); i++) {
            JSONObject table = tableArrayList.getJSONObject(i);
            this.tableArrayList.add(new Table(table));
        }
    }
}
