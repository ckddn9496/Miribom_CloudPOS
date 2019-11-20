package com.example.cloudpos.data;

/*테이블 클래스*/
//implemented by Yang Insu

import org.json.JSONException;
import org.json.JSONObject;

public class Table {
    private String tableNo;
    private boolean isTaken;

    public Table(String no, boolean isTaken) {
        tableNo = no;
        this.isTaken = isTaken;
    }

    public Table(JSONObject data) throws JSONException {
        this.tableNo = String.valueOf(data.getInt("no"));
        this.isTaken = data.getInt("istaken") == 1 ? true : false;
    }

    public void setTableNo(String no) {
        tableNo = no;
    }

    public void setIsTaken(int isTaken) {
        this.isTaken = isTaken == 1 ? true : false;
    }
    public void setIsTaken(boolean isTaken) {
        this.isTaken = isTaken;
    }

    public String getTableNo() {
        return this.tableNo;
    }

    public boolean getisTaken() {
        return this.isTaken;
    }

    @Override
    public String toString() {
        return this.tableNo + ":" + this.isTaken;
    }
}
