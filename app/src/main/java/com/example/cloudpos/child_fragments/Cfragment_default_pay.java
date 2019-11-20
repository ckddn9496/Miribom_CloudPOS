package com.example.cloudpos.child_fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cloudpos.R;
import com.example.cloudpos.adapters.MenuGridAdapter;
import com.example.cloudpos.adapters.RecLineListViewAdapter;
import com.example.cloudpos.connections.InsertReceiptTask;
import com.example.cloudpos.data.MenuItem;
import com.example.cloudpos.data.Receipt;
import com.example.cloudpos.data.ReceiptLine;
import com.example.cloudpos.data.ReceiptList;
import com.example.cloudpos.fragments.FragmentDefault;

import java.text.SimpleDateFormat;
import java.util.Date;

/*implemented by Yang Insu*/

public class Cfragment_default_pay extends Fragment implements View.OnClickListener, FragmentDefault.TableSender {

    /*def_pay_buttons*/
    private Button delAllBtn;
    private Button delSelBtn;
    private Button decBtn; //수량 마이너스 버튼
    private Button incBtn; //수량 플러스 버튼

    /*def_pay_info*/
    private TextView totalPriceTv;
    private TextView remainPriceTV;
    private TextView receivedPriceTV;
    private TextView changePriceTV;

    /*Calculator*/
    private TextView calcScreenTV;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;
    private Button btn6;
    private Button btn7;
    private Button btn8;
    private Button btn9;
    private Button btn0;
    private Button btn00;
    private Button btnCancel;
    private Button btnDel;
    private Button btnEnter;


    /*rightScreen View 위젯*/
    private GridView menuGridView;
    private TextView tableNoTV;
    private Button cashPayBtn;
    private Button cardPayBtn;
    private Button combPayBtn;
    //GridView adapter
    private MenuGridAdapter menuGridAdapter;

    private int tableNo;


    /*Receipt Line 리스트*/
    ListView recLineListView;
    RecLineListViewAdapter recLineListViewAdapter;


    public static Cfragment_default_pay newInstance() {
        return new Cfragment_default_pay();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fv = inflater.inflate(R.layout.default_pay_cfragment, container, false);

        popRightScreen(fv); //오른쪽 화면 처음에 띄어주기

        //Calculator 연결
        linkCalc(fv);
        menuGridAdapter = new MenuGridAdapter();
        menuGridView.setAdapter(menuGridAdapter);


        //ListView 연결
        recLineListViewAdapter = new RecLineListViewAdapter();
        recLineListView = fv.findViewById(R.id.def_pay_rec_listview);
        recLineListView.setAdapter(recLineListViewAdapter);

//        recLineListViewAdapter.addRecLine();

        menuItemClickHandler();


        return fv;
    }

    private void menuItemClickHandler() {
        menuGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MenuItem temp = (MenuItem) menuGridAdapter.getItem(position);
                Toast.makeText(getContext(), temp.getMenuName(), Toast.LENGTH_SHORT).show();
                ReceiptLine newReceiptLine = new ReceiptLine(temp, 1);
                recLineListViewAdapter.addRecLine(newReceiptLine);
                recLineListViewAdapter.notifyDataSetChanged();
            }
        });
    }

    void popRightScreen(View fv) {
        //View Component 연결
        menuGridView = fv.findViewById(R.id.def_pay_menuGridView);
        tableNoTV = fv.findViewById(R.id.def_pay_tableNo);
        cashPayBtn = fv.findViewById(R.id.def_pay_cashPayBtn);
        cardPayBtn = fv.findViewById(R.id.def_pay_cardPayBtn);
        combPayBtn = fv.findViewById(R.id.def_pay_combPayBtn);
        cashPayBtn.setOnClickListener(this);
        cardPayBtn.setOnClickListener(this);
        combPayBtn.setOnClickListener(this);
        tableNoTV.setText(String.valueOf(tableNo));


    }

    void linkCalc(View view) {
        btn1 = view.findViewById(R.id.calc_1_btn);
        btn2 = view.findViewById(R.id.calc_2_btn);
        btn3 = view.findViewById(R.id.calc_3_btn);
        btn4 = view.findViewById(R.id.calc_4_btn);
        btn5 = view.findViewById(R.id.calc_5_btn);
        btn6 = view.findViewById(R.id.calc_6_btn);
        btn7 = view.findViewById(R.id.calc_7_btn);
        btn8 = view.findViewById(R.id.calc_8_btn);
        btn9 = view.findViewById(R.id.calc_9_btn);
        btn0 = view.findViewById(R.id.calc_0_btn);
        btn00 = view.findViewById(R.id.calc_00_btn);
        btnDel = view.findViewById(R.id.calc_backspace_btn);
        btnCancel = view.findViewById(R.id.calc_cancel_btn);
        btnEnter = view.findViewById(R.id.calc_enter_btn);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btn0.setOnClickListener(this);
        btn00.setOnClickListener(this);
        btnDel.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnEnter.setOnClickListener(this);
    }


    void createReceipt() { //여기서 영수증 생성함
        // TODO: 서버로 넘길때 newReceipt를 넘기면 됨 comment by Insu
        //  => 계산이 완료되면 Table이 보이는 Default화면으로 돌아가기 가능?
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String formatDate = simpleDateFormat.format(date);
        Receipt newReceipt = new Receipt(formatDate, recLineListViewAdapter.receiptLineArrayList, tableNo, ReceiptList.RECEIPT_COMPLETE); // 1: 결제완료, 2: 환불

        InsertReceiptTask insertReceiptTask = new InsertReceiptTask(newReceipt, getContext());
        insertReceiptTask.execute();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.def_pay_inc:
                break;
            case R.id.calc_enter_btn:
                createReceipt();
                Toast.makeText(getContext(), "영수증 추가", Toast.LENGTH_SHORT).show();
                break;
        }

    }


    @Override
    public void sendTable(int no) {
        tableNo = no;
    }
}
