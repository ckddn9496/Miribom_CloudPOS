package com.example.cloudpos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cloudpos.data.LineQueue;
import com.example.cloudpos.data.MenuItem;
import com.example.cloudpos.data.MenuList;
import com.example.cloudpos.data.Restaurant;
import com.example.cloudpos.data.Slot;
import com.example.cloudpos.fragments.FragmentCalculate;
import com.example.cloudpos.fragments.FragmentDefault;
import com.example.cloudpos.fragments.FragmentMenu;
import com.example.cloudpos.fragments.FragmentReceipt;
import com.example.cloudpos.fragments.FragmentWaiting;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;


//implemented by Yang Insu


/*이 Activity에서 모든 기능 모듈에 대한 제어를 처리함*/

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = "MainActivity>>>";

    /*Fragment flags*/
    private final int DEFAULT_FRAG = 0; //기존 화면: 테이블 배치도
    private final int MENU_FRAG = 1; //메뉴 관리 화면
    private final int RECEIPT_FRAG = 2; //영수증 관리 화면
    private final int RESERVE_FRAG = 3; //예약 관리 화면
    private final int CALC_FRAG = 4; //간단 정산 화면

    /*View Components*/
    private Button defBtn, menuBtn, recBtn, resBtn, calcBtn; //탭에 있는 버튼들
    private TextView userNameTv;
    private TextView dateTV;


    /*받아오는 데이터들*/
    MenuList menuList = MenuList.getInstance(); //메뉴 리스트
    PriorityQueue line = LineQueue.getInstance().linePQueue;

    /*Firebase References*/
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference slotRef = database.getReference("server/restaurants/test");
    List arrayList = new ArrayList();
    HashMap input = new HashMap();
    private int accessCounter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //탭 버튼 뷰 참조
        defBtn = (Button) findViewById(R.id.tab0_default_btn);
        menuBtn = (Button) findViewById(R.id.tab1_menu_btn); //메뉴 관리 버튼
        recBtn = (Button) findViewById(R.id.tab2_receipt_btn); //영수증 관리 버튼
        resBtn = (Button) findViewById(R.id.tab3_reservation_btn); //예약 관리 버튼
        calcBtn = (Button) findViewById(R.id.tab4_calculate_btn); //간단 정산 버튼

        //탭 버튼 리스너 연결
        defBtn.setOnClickListener(this);
        menuBtn.setOnClickListener(this);
        recBtn.setOnClickListener(this);
        resBtn.setOnClickListener(this);
        calcBtn.setOnClickListener(this);



        //MainActivity 첫 호출 시 default로 띄어주는 Fragment
        callFragment(DEFAULT_FRAG);

        /* Firebase 서버에서 대기 리스트 받아오기 */
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Get Post object 받아옴
                accessCounter++;
                arrayList = (List) dataSnapshot.getValue();
                if (arrayList != null) {

                    if (accessCounter == 1) { //처음 들어온거면
                        //줄 전체 받아오기
                        for (int i = 1; i < arrayList.size(); i++) {
                            input = (HashMap) arrayList.get(i);
                            Object no = input.get("no");
                            Object personCount = input.get("personCount");
                            Object phoneNo = input.get("phoneNo");
                            Object time = input.get("time");

                            Slot newSlot = new Slot(no, personCount, phoneNo, time);
                            LineQueue.getInstance().linePQueue.add(newSlot);
                            LineQueue.getInstance().line.add(newSlot);


                        }
                    } else {
                        //마지막 슬롯만 넣기
                        input = (HashMap) arrayList.get(arrayList.size());
                        Object no = input.get("no");
                        Object personCount = input.get("personCount");
                        Object phoneNo = input.get("phoneNo");
                        Object time = input.get("time");

                        Slot newSlot = new Slot(no, personCount, phoneNo, time);
                        LineQueue.getInstance().linePQueue.add(newSlot);
                        LineQueue.getInstance().line.add(newSlot);
                        Toast.makeText(MainActivity.this, String.valueOf(newSlot.getPhoneNo()), Toast.LENGTH_SHORT).show();
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        slotRef.addValueEventListener(listener);

        userNameTv = findViewById(R.id.main_chef_nameTV);
        dateTV = findViewById(R.id.main_dateTV);

        userNameTv.setText(Restaurant.getInstance().getOwnerName());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        String dateStr = sdf.format(today);
        dateTV.setText(dateStr);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
    void createMenuList() {
        Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.spagetti);
        menuList.menuItemArrayList.add(new MenuItem("1", "code1", "Spagetti 1", "1000", bitmap));
        menuList.menuItemArrayList.add(new MenuItem("2", "code2", "Spagetti 2", "2000", bitmap));
        menuList.menuItemArrayList.add(new MenuItem("3", "code3", "Spagetti 3", "3000", bitmap));
        menuList.menuItemArrayList.add(new MenuItem("4", "code4", "Spagetti 4", "4000", bitmap));
        menuList.menuItemArrayList.add(new MenuItem("5", "code5", "Spagetti 5", "5000", bitmap));

    }


    /*Fragment 제어 함수*/
    private void callFragment(int fragment_no) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (fragment_no) {
            case 0: //기존 화면: 테이블 배치도
                FragmentDefault fragmentDefault = new FragmentDefault();
                transaction.replace(R.id.fragment_container, fragmentDefault);
//                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case 1: //메뉴 관리 화면
                FragmentMenu fragmentMenu = new FragmentMenu();
                transaction.replace(R.id.fragment_container, fragmentMenu);
//                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case 2: //영수증 관리 화면
                FragmentReceipt fragmentReceipt = new FragmentReceipt();
                transaction.replace(R.id.fragment_container, fragmentReceipt);
//                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case 3: //예약 관리 화면
                FragmentWaiting fragmentWaiting = new FragmentWaiting();
                transaction.replace(R.id.fragment_container, fragmentWaiting);
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
