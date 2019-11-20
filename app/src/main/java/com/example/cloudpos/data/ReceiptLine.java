package com.example.cloudpos.data;

/*implemented by Yang Insu*/
//영수증 한 줄

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReceiptLine {
    private String recNo;
    private MenuItem menuItem = new MenuItem();
    private int itemNo; //메뉴 갯수

    //보여줄 정보
    private int onePrice; //하나당 가격
    private int totPrice; //총 가격
    private String menuName;

    public ReceiptLine(MenuItem menuItem, int itemNo) {

        this.menuItem = menuItem;
        this.itemNo = itemNo;

        onePrice = Integer.parseInt(menuItem.getMenuPrice());
        totPrice = onePrice * itemNo;
        menuName = menuItem.getMenuName();
    }

    public ReceiptLine(int recNo, JSONObject data) throws JSONException {
        this.recNo = String.valueOf(recNo);

        int menuNo = data.getInt("menu_no");
        ArrayList<MenuItem> menuItems = MenuList.getInstance().menuItemArrayList;
        for (MenuItem menuItem : menuItems) {
            if (menuItem.getMenuNo().equals(String.valueOf(menuNo))) {
                this.menuItem = menuItem;
                break;
            }
        }

        this.itemNo = data.getInt("count");

        this.onePrice = data.getInt("price");
        this.totPrice = this.onePrice * this.itemNo;
        this.menuName = this.menuItem.getMenuName();
    }

    public void incRecNo() {
        itemNo++;
        totPrice += onePrice;
    }

    public void decRecNo() {
        itemNo--;
        totPrice -= onePrice;
    }

    public String getRecNo() {
        return this.recNo;
    }

    public int getItemNo() {
        return this.itemNo;
    }

    public String getMenuName() {
        return menuName;
    }

    public int getOnePrice() {
        return onePrice;
    }

    public int getTotPrice() {
        return totPrice;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    @Override
    public String toString() {
        return menuName + ", " + itemNo + " 개: " + totPrice + " 원 ";
    }


}
