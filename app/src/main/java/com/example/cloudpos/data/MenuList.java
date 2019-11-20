package com.example.cloudpos.data;

/*메뉴 List*/
//implemented by Yang Insu

//싱글톤 객체로 모든 fragment에서 참조할 수 있게 구현함


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MenuList {
    private static MenuList menuList;
    public ArrayList<MenuItem> menuItemArrayList = new ArrayList<>();

    private MenuList() {
    }
    public static MenuList getInstance(){
        if(menuList == null){
            menuList = new MenuList();
        }
        return menuList;
    }

    public void setMenuItemArrayList(JSONArray menuItemJSONArray) throws JSONException {
        for (int i = 0; i < menuItemJSONArray.length(); i++) {
            JSONObject menu = menuItemJSONArray.getJSONObject(i);
            this.menuItemArrayList.add(new MenuItem(menu));
        }
    }
}
