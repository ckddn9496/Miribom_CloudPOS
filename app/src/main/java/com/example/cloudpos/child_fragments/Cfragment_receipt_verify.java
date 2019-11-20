package com.example.cloudpos.child_fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cloudpos.R;
import com.example.cloudpos.adapters.ReceiptPartListViewAdapter;
import com.example.cloudpos.data.Receipt;
import com.example.cloudpos.data.ReceiptList;
import com.example.cloudpos.data.Restaurant;
import com.example.cloudpos.fragments.FragmentReceipt;

import java.lang.reflect.Array;
import java.util.ArrayList;

/* 영수증 관리 Fragment에서 각 영수증 조회하는 기능 담당*/
//implemented by Yang Insu
public class Cfragment_receipt_verify extends Fragment implements FragmentReceipt.Transmitter{

    //View Widget 참조
    TextView ReceiptStoreNameTV;
    TextView ReceiptStoreAddrTV;
    TextView ReceiptRegisterNoTV;
    TextView ReceiptOwnerNameTV;
    TextView ReceiptStorePNTV;
    TextView ReceiptDateTV;
    TextView ReceiptPaymentNoPNTV;
    TextView receiptTotalPriceTV;
    TextView receiptDiscountPriceTV;
    TextView receiptReceivedPriceTV;
    TextView receiptChangePriceTV;
    TextView receiptCashPaymentTV;
    TextView receiptCardPaymentTV;
    ListView receiptitemsListView;
    ReceiptPartListViewAdapter receiptPartListViewAdapter;

    //Data 참조
    Restaurant restaurant = Restaurant.getInstance();
    ArrayList<Receipt> receiptList = ReceiptList.getInstance().receipts;

    int index;




    public static Cfragment_receipt_verify newInstance() {
        return new Cfragment_receipt_verify();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fv = inflater.inflate(R.layout.receipt, container, false);

        ReceiptStoreNameTV = fv.findViewById(R.id.ReceiptStoreNameTV);
        ReceiptStoreAddrTV = fv.findViewById(R.id.ReceiptStoreAddrTV);
        ReceiptRegisterNoTV = fv.findViewById(R.id.ReceiptOwnerNameTV);
        ReceiptOwnerNameTV = fv.findViewById(R.id.ReceiptOwnerNameTV);
        ReceiptStorePNTV = fv.findViewById(R.id.ReceiptStorePNTV);


        ReceiptDateTV = fv.findViewById(R.id.ReceiptDateTV);
        ReceiptPaymentNoPNTV = fv.findViewById(R.id.ReceiptPaymentNoPNTV);
        receiptTotalPriceTV = fv.findViewById(R.id.receiptTotalPriceTV);
        receiptDiscountPriceTV = fv.findViewById(R.id.receiptDiscountPriceTV);
        receiptReceivedPriceTV = fv.findViewById(R.id.receiptReceivedPriceTV);
        receiptChangePriceTV = fv.findViewById(R.id.receiptChangePriceTV);
        receiptCashPaymentTV = fv.findViewById(R.id.receiptCashPaymentTV);
        receiptCardPaymentTV = fv.findViewById(R.id.receiptCardPaymentTV);

        Receipt newReceipt = receiptList.get(index);


        receiptitemsListView = fv.findViewById(R.id.receiptItemListView);
        receiptPartListViewAdapter = new ReceiptPartListViewAdapter(newReceipt);
        receiptitemsListView.setAdapter(receiptPartListViewAdapter);



        setViewWidgets();

        return fv;
    }

    private void setViewWidgets(){

        //Restaurant 객체에서 받아오는 정보
        String storeName;
        String storeAddress;
        String registerNo;
        String ownerName;
        String storePN;

        //Receipt 객체에서 받아오는 정보
        String date;
        String paymentNo;
        String totalPrice;
        String discountPrice;
        String receivedPrice;
        String changePrice;
        String cashPayPrice;
        String cardPayPrice;

        storeName = restaurant.getName();
        storeAddress = restaurant.getAddress();
        registerNo = restaurant.getRegisterNo();
        ownerName = restaurant.getOwnerName();
        storePN = restaurant.getStorePN();

        Receipt readReceipt = ReceiptList.getInstance().receipts.get(index); //읽기용 영수증

        date = readReceipt.getTime();
        //paymentNo는 다음 버전에서 처리하자
        totalPrice = String.valueOf(readReceipt.getTotalPrice());
        discountPrice = String.valueOf(0); // 할인은 나중에 처리하자
        receivedPrice = totalPrice;
        changePrice = String.valueOf(Integer.parseInt(totalPrice) - Integer.parseInt(receivedPrice));
        cashPayPrice = String.valueOf(0);
        cardPayPrice = totalPrice; //아직 기본은 카드 결제

        ReceiptStoreNameTV.setText(storeName);
        ReceiptStoreAddrTV.setText(storeAddress);
        ReceiptRegisterNoTV.setText(registerNo);
        ReceiptOwnerNameTV.setText(ownerName);
        ReceiptStorePNTV.setText(storePN);

        ReceiptDateTV.setText(date);
        //ReceiptPaymentNoPNTV.setText();
        receiptTotalPriceTV.setText(totalPrice);
        receiptDiscountPriceTV.setText(discountPrice);
        receiptReceivedPriceTV.setText(receivedPrice);
        receiptChangePriceTV.setText(changePrice);
        receiptCashPaymentTV.setText(cashPayPrice);
        receiptCardPaymentTV.setText(cardPayPrice);

    }

    @Override
    public void transmit(int position) {
        index = position;

    }

}