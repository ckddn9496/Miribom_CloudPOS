package com.example.cloudpos.data;

/*등록 식당 정보*/
//implemented by Yang Insu
//싱글톤


import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

public class Restaurant {

    private static Restaurant restaurant = new Restaurant();

    private String name = "";        //음식점 이름
    private String address = "";     //음식점 주소
    private String registerNo = "";  //등록 번호 (이건 추후 추가해도 됨)
    private String storePN = "";     //음식점 전화번호
    private String ownerName = "";   //음식점 사장 이름

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }


    private Restaurant() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setRegisterNo(String registerNo) {
        this.registerNo = registerNo;
    }

    public void setStorePN(String storePN) {
        this.storePN = storePN;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getRegisterNo() {
        return registerNo;
    }

    public String getStorePN() {
        return storePN;
    }

    public static Restaurant getInstance() {
        return restaurant;
    }


    public void setRestaurantDetail(JSONObject restaurant) throws JSONException {
        this.name = restaurant.getString("name");
        this.address = restaurant.getString("address");
        this.registerNo = String.valueOf(restaurant.getInt("no"));
        this.storePN = restaurant.getString("mobile");
//        {
//            "no": 3,
//            "name": "우뇽파스타",
//            "address": "서울 동작구 서달로 10길 10",
//            "mobile": "02-816-6338",
//            "latitude": 37.5062924,
//            "longitude": 126.961629,
//            "reservation_type": 1,
//            "food_type": 4,
//            "seat_type": 1,
//            "hours": {
//                "t": 2,
//                "cd": 6,
//                "time": "12:00 ~ 21:00"
//            },
//            "image": "https://miribomimage.s3.ap-northeast-2.amazonaws.com/%EC%9A%B0%EB%87%BD.jpg",
//                    "owner_request": "안녕하세요. 우뇽뚝배기스파게티를 찾아주셔서 감사합니다.",
//                    "o_no": 24
//        },

    }
}
