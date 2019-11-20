package com.example.cloudpos.child_fragments;

import android.content.DialogInterface;
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
import com.example.cloudpos.connections.UpdateTableStatusTask;
import com.example.cloudpos.data.Dialog;
import com.example.cloudpos.data.MenuItem;
import com.example.cloudpos.data.Receipt;
import com.example.cloudpos.data.ReceiptLine;
import com.example.cloudpos.data.ReceiptList;
import com.example.cloudpos.data.TableList;
import com.example.cloudpos.data.TableStatusManager;
import com.example.cloudpos.fragments.FragmentDefault;

import java.text.SimpleDateFormat;
import java.util.Date;

/*implemented by Yang Insu*/

public class Cfragment_default_pay extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener, FragmentDefault.TableSender {

    private final static String TAG = "Cfragment_default_pay";

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
    private int remainPrice;
    private int receivedPrice;

    private ReceiptLine selectedReceiptLine;

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

        // ListView 연결
        recLineListViewAdapter = new RecLineListViewAdapter(tableNo);
        recLineListViewAdapter.addRecLines(TableStatusManager.getInstance().getSelectedReceiptLine(tableNo));
        recLineListView = fv.findViewById(R.id.def_pay_rec_listview);
        recLineListView.setAdapter(recLineListViewAdapter);
        recLineListView.setOnItemClickListener(this);


        /*def_pay_buttons*/
        linkPayButtons(fv);


        /*def_pay_info*/
        linkPayInfo(fv);


        // Calculator 연결
        linkCalc(fv);
        menuGridAdapter = new MenuGridAdapter();
        menuGridView.setAdapter(menuGridAdapter);

        menuItemClickHandler();
        receivedPrice = 0;

        int totalPrice = recLineListViewAdapter.getTotalPrice();
        setPriceTV(totalPrice);

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

                if (!TableList.getInstance().tableArrayList.get(tableNo - 1).getisTaken()) {
                    UpdateTableStatusTask updateTableStatusTask = new UpdateTableStatusTask(tableNo, TableList.USING, getContext());
                    updateTableStatusTask.execute();
                }

                int totalPrice = recLineListViewAdapter.getTotalPrice();
                setPriceTV(totalPrice);
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

    private void linkPayButtons(View fv) {
        delAllBtn = fv.findViewById(R.id.def_pay_delall);
        delSelBtn = fv.findViewById(R.id.def_pay_delsel);
        decBtn = fv.findViewById(R.id.def_pay_dec);
        incBtn = fv.findViewById(R.id.def_pay_inc);
        delAllBtn.setOnClickListener(this);
        delSelBtn.setOnClickListener(this);
        decBtn.setOnClickListener(this);
        incBtn.setOnClickListener(this);
    }

    private void linkPayInfo(View fv) {
        totalPriceTv = fv.findViewById(R.id.def_pay_totalPrice);
        remainPriceTV = fv.findViewById(R.id.def_pay_remainPrice);
        receivedPriceTV = fv.findViewById(R.id.def_pay_receivedPrice);
        changePriceTV = fv.findViewById(R.id.def_pay_changePrice);
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

    @Override
    public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
        selectedReceiptLine = (ReceiptLine) adapter.getItemAtPosition(position);
        Toast.makeText(getContext(), selectedReceiptLine.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        int totalPrice = recLineListViewAdapter.getTotalPrice();


        switch (v.getId()) {
            case R.id.calc_1_btn:
                receivedPrice = receivedPrice * 10 + 1;
                break;
            case R.id.calc_2_btn:
                receivedPrice = receivedPrice * 10 + 2;
                break;
            case R.id.calc_3_btn:
                receivedPrice = receivedPrice * 10 + 3;
                break;
            case R.id.calc_4_btn:
                receivedPrice = receivedPrice * 10 + 4;
                break;
            case R.id.calc_5_btn:
                receivedPrice = receivedPrice * 10 + 5;
                break;
            case R.id.calc_6_btn:
                receivedPrice = receivedPrice * 10 + 6;
                break;
            case R.id.calc_7_btn:
                receivedPrice = receivedPrice * 10 + 7;
                break;
            case R.id.calc_8_btn:
                receivedPrice = receivedPrice * 10 + 8;
                break;
            case R.id.calc_9_btn:
                receivedPrice = receivedPrice * 10 + 9;
                break;
            case R.id.calc_0_btn:
                receivedPrice = receivedPrice * 10;
                break;
            case R.id.calc_00_btn:
                receivedPrice = receivedPrice * 100;
                break;
            case R.id.calc_backspace_btn:
                receivedPrice = receivedPrice / 10;
                break;
            case R.id.calc_cancel_btn:
                receivedPrice = 0;
                break;
            case R.id.calc_enter_btn:
                Dialog.getInstance(getContext()).alertDialog("계산을 확정하시겠습니까?", "선택하세요."
                        , R.drawable.table, "결제", "취소", posOnClickListener, negOnClickListener);
                break;
        }
        setPriceTV(totalPrice);


        switch (v.getId()) {
            case R.id.def_pay_delall:
                deleteAll();
                break;
            case R.id.def_pay_delsel:
                deleteSelectedMenuItem();
                break;
            case R.id.def_pay_dec:
                decreaseSelectedMenuItem();
                break;
            case R.id.def_pay_inc:
                increaseSelectedMenuItem();
                break;
        }

    }

    private void setPriceTV(int totalPrice) {
        totalPriceTv.setText(totalPrice + "원");
        receivedPriceTV.setText(receivedPrice + "원");
        remainPrice = totalPrice - receivedPrice;
        if (remainPrice > 0) {
            remainPriceTV.setText(remainPrice + "원");
        } else {
            remainPriceTV.setText("0원");
        }
        int change = (receivedPrice - totalPrice);
        if (change > 0) {
            changePriceTV.setText((receivedPrice - totalPrice) + "원");
        } else {
            changePriceTV.setText("0원");
        }
    }

    private void increaseSelectedMenuItem() {
        if (selectedReceiptLine == null) {
            Toast.makeText(getContext(), "메뉴를 선택 해주세요.", Toast.LENGTH_LONG).show();
        } else {
            recLineListViewAdapter.increaseSelectedReceiptLine(selectedReceiptLine.getMenuName());
            recLineListViewAdapter.notifyDataSetChanged();

            int totalPrice = recLineListViewAdapter.getTotalPrice();
            setPriceTV(totalPrice);
        }
    }

    private void decreaseSelectedMenuItem() {
        if (selectedReceiptLine == null) {
            Toast.makeText(getContext(), "메뉴를 선택 해주세요.", Toast.LENGTH_LONG).show();
        } else {
            if (selectedReceiptLine.getItemNo() == 1) {
                deleteSelectedMenuItem();
            } else {
                recLineListViewAdapter.decreaseSelectedReceiptLine(selectedReceiptLine.getMenuName());
                recLineListViewAdapter.notifyDataSetChanged();

                int totalPrice = recLineListViewAdapter.getTotalPrice();
                setPriceTV(totalPrice);
            }
        }
    }

    private void deleteSelectedMenuItem() {
        if (selectedReceiptLine == null) {
            Toast.makeText(getContext(), "메뉴를 선택 해주세요.", Toast.LENGTH_LONG).show();
        } else {
            recLineListViewAdapter.removeReceiptLine(selectedReceiptLine.getMenuName());
            TableStatusManager.getInstance().removeSelectedReceiptLine(tableNo, selectedReceiptLine.getMenuName());
            recLineListViewAdapter.notifyDataSetChanged();
            selectedReceiptLine = null;

            int totalPrice = recLineListViewAdapter.getTotalPrice();
            setPriceTV(totalPrice);
        }
    }

    private void deleteAll() {
        TableStatusManager.getInstance().removeSelectedTableReceiptLines(tableNo);
        recLineListViewAdapter.clear();
        recLineListViewAdapter.notifyDataSetChanged();

        remainPrice = 0;
        receivedPrice = 0;
        setPriceTV(0);
    }

    void createReceipt() { //여기서 영수증 생성함
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String formatDate = simpleDateFormat.format(date);
        Receipt newReceipt = new Receipt(formatDate, recLineListViewAdapter.receiptLineArrayList, tableNo, ReceiptList.RECEIPT_COMPLETE); // 1: 결제완료, 2: 환불
        InsertReceiptTask insertReceiptTask = new InsertReceiptTask(newReceipt, recLineListViewAdapter, getContext());
        insertReceiptTask.execute();

        UpdateTableStatusTask updateTableStatusTask = new UpdateTableStatusTask(tableNo, TableList.CLEAR, getContext());
        updateTableStatusTask.execute();
    }


    DialogInterface.OnClickListener posOnClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Log.d(TAG, "onClick: " + which);
            createReceipt();
            Toast.makeText(getContext(), "정상적으로 결제 되었습니다.", Toast.LENGTH_SHORT).show();
        }
    };


    DialogInterface.OnClickListener negOnClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Log.d(TAG, "onClick: " + which);
            Toast.makeText(getContext(), "결제가 취소되었습니다.", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void sendTable(int no) {
        tableNo = no;
    }
}
