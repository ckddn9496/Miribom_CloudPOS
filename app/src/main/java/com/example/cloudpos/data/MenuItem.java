package com.example.cloudpos.data;


/*메뉴 아이템 클래스*/
//메뉴판에 등록된 하나의 음식에 대한 객체라고 생각하면 된다

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

//Implemented by Yang Insu
public class MenuItem {
    private String menuNo;    //새롭게 등록 시 차례대로 들어간다
    private String menuCode;  //나중에 혹시라도 추가 사항을 위해 고유 식별번호를 넣어둠
    private String menuName;  //음식 이름
    private String menuPrice; //메뉴 값
    private String imageUrl;  //이미지 url
    private Bitmap menuImage; //메뉴 담을 bitmap

    public MenuItem(){

    }

    public MenuItem(String no, String code, String name, String price, String imageUrl){
        menuNo = no;
        menuCode = code;
        menuName = name;
        menuPrice = price;
        this.imageUrl = imageUrl;

    }

    public MenuItem(String no, String code, String name, String price, File image){
        menuNo = no;
        menuCode = code;
        menuName = name;
        menuPrice = price;
        menuImage = BitmapFactory.decodeFile(image.getAbsolutePath());


    }

    public MenuItem(String no, String code, String name, String price, Bitmap image){
        menuNo = no;
        menuCode = code;
        menuName = name;
        menuPrice = price;
        menuImage = image;
    }

    public MenuItem(JSONObject menu) throws JSONException {
        this.menuNo = String.valueOf(menu.getInt("no"));
        this.menuCode = String.valueOf(menu.getInt("no"));
        this.menuName = menu.getString("name");
        this.menuPrice = menu.getString("price");
        this.imageUrl = menu.getString("image");
    }

    public void setImageUrl(String url){imageUrl = url;}
    public void setMenuImage(Bitmap image){ menuImage = image;}
    public void setMenuNo(String no){
        menuNo = no;
    }
    public void setMenuName(String name){
        menuName = name;
    }
    public void setMenuCode(String code){
        menuCode = code;
    }
    public void setMenuPrice(String price){
        menuPrice = price;
    }

    public String getImageUrl(){return this.imageUrl;}
    public Bitmap getMenuImage(){ return this.menuImage;}
    public String getMenuNo(){
        return this.menuNo;
    }
    public String getMenuCode(){
        return this.menuCode;
    }
    public String getMenuName(){
        return this.menuName;
    }
    public String getMenuPrice(){
        return this.menuPrice;
    }
}
