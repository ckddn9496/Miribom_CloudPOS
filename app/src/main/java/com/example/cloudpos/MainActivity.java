package com.example.cloudpos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.cloudpos.data.MenuItem;
import com.example.cloudpos.data.MenuList;
import com.example.cloudpos.data.Restaurant;
import com.example.cloudpos.data.Table;
import com.example.cloudpos.data.TableList;
import com.example.cloudpos.fragments.FragmentCalculate;
import com.example.cloudpos.fragments.FragmentDefault;
import com.example.cloudpos.fragments.FragmentMenu;
import com.example.cloudpos.fragments.FragmentReceipt;
import com.example.cloudpos.fragments.FragmentReservation;


//implemented by Yang Insu


/*이 Activity에서 모든 기능 모듈에 대한 제어를 처리함*/

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    /*Fragment flags*/
    private final int DEFAULT_FRAG = 0; //기존 화면: 테이블 배치도
    private final int MENU_FRAG = 1; //메뉴 관리 화면
    private final int RECEIPT_FRAG = 2; //영수증 관리 화면
    private final int RESERVE_FRAG = 3; //예약 관리 화면
    private final int CALC_FRAG = 4; //간단 정산 화면

    /*View Components*/
    private Button defBtn,menuBtn, recBtn,resBtn,calcBtn; //탭에 있는 버튼들


    /*받아오는 데이터들*/
    MenuList menuList = MenuList.getInstance(); //메뉴 리스트



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //탭 버튼 뷰 참조
        defBtn = (Button)findViewById(R.id.tab0_default_btn);
        menuBtn = (Button)findViewById(R.id.tab1_menu_btn); //메뉴 관리 버튼
        recBtn = (Button)findViewById(R.id.tab2_receipt_btn); //영수증 관리 버튼
        resBtn = (Button)findViewById(R.id.tab3_reservation_btn); //예약 관리 버튼
        calcBtn = (Button)findViewById(R.id.tab4_calculate_btn); //간단 정산 버튼

        //탭 버튼 리스너 연결
        defBtn.setOnClickListener(this);
        menuBtn.setOnClickListener(this);
        recBtn.setOnClickListener(this);
        resBtn.setOnClickListener(this);
        calcBtn.setOnClickListener(this);

        //MainActivity 첫 호출 시 default로 띄어주는 Fragment
        callFragment(DEFAULT_FRAG);



        //tableArrayList더미 생성
//        for(int i = 0; i < 10; i++){
//            Table table = new Table(Integer.toString(i+1),false);
//            TableList.getInstance().tableArrayList.add(table);
//        }

        /*서버에서 데이터 받아오기*/
        //TODO: 1. 등록된 User 받아오기 2. 받아온 User의 MenuList, ReceiptList, TableList 받아오기
        // DONE: RestaurantSettingTask와 *List 클래스에 JSONParser기능 추가
        //이거만 해주면 받아오는 부분은 끝


//        //음식점 임시 지정
//        Restaurant.getInstance().setName("테스트 음식점");
//        Restaurant.getInstance().setAddress("서울시 삼성동 학동로 88길 5 진흥아파트 1동 103호");
//        Restaurant.getInstance().setRegisterNo("1");
//        Restaurant.getInstance().setStorePN("010-2224-1049");
//        Restaurant.getInstance().setOwnerName("양인수");


//        createMenuList();//MenuList 임시 생성





        /*Firebase 서버에서 대기 리스트 받아오기*/
        // TODO: LineQueue 받아오기


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tab0_default_btn:
                callFragment(DEFAULT_FRAG);
                break;
            case R.id.tab1_menu_btn:
                callFragment(MENU_FRAG);
                break;
            case R.id.tab2_receipt_btn:
                callFragment(RECEIPT_FRAG);
                break;
            case R.id.tab3_reservation_btn:
                callFragment(RESERVE_FRAG);
                break;
            case R.id.tab4_calculate_btn:
                callFragment(CALC_FRAG);
                break;
        }

    }

    //임시 메뉴리스트 생성
    void createMenuList(){
        Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.spagetti);
        menuList.menuItemArrayList.add(new MenuItem("1","code1","Spagetti 1","1000",bitmap));
        menuList.menuItemArrayList.add(new MenuItem("2","code2","Spagetti 2","2000",bitmap));
        menuList.menuItemArrayList.add(new MenuItem("3","code3","Spagetti 3","3000",bitmap));
        menuList.menuItemArrayList.add(new MenuItem("4","code4","Spagetti 4","4000",bitmap));
        menuList.menuItemArrayList.add(new MenuItem("5","code5","Spagetti 5","5000",bitmap));

    }


    /*Fragment 제어 함수*/
    private void callFragment(int fragment_no){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch(fragment_no){
            case 0: //기존 화면: 테이블 배치도
                FragmentDefault fragmentDefault = new FragmentDefault();
                transaction.replace(R.id.fragment_container,fragmentDefault);
//                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case 1: //메뉴 관리 화면
                FragmentMenu fragmentMenu = new FragmentMenu();
                transaction.replace(R.id.fragment_container,fragmentMenu);
//                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case 2: //영수증 관리 화면
                FragmentReceipt fragmentReceipt = new FragmentReceipt();
                transaction.replace(R.id.fragment_container,fragmentReceipt);
//                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case 3: //예약 관리 화면
                FragmentReservation fragmentReservation = new FragmentReservation();
                transaction.replace(R.id.fragment_container, fragmentReservation);
//                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case 4: //간단 정산 화면
                FragmentCalculate fragmentCalculate = new FragmentCalculate();
                transaction.replace(R.id.fragment_container, fragmentCalculate);
//                transaction.addToBackStack(null);
                transaction.commit();
                break;
        }

    }
}
