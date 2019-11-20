package com.example.cloudpos.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cloudpos.R;
import com.example.cloudpos.adapters.ReceiptListViewAdapter;
import com.example.cloudpos.child_fragments.Cfragment_receipt_verify;
import com.example.cloudpos.connections.RefundReceiptTask;
import com.example.cloudpos.data.ReceiptList;


/*영수증 관리 Fragment*/
//implemented by Yang Insu


public class FragmentReceipt extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private final static String TAG = "FragmentReceipt>>";

    ListView listView;
    ReceiptListViewAdapter receiptListViewAdapter;

    public FragmentReceipt(){
        //default public constructor
    }


    public interface Transmitter{
        void transmit(int position);
    }

    static Transmitter transmitter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.receipt_fragment,container,false);

        receiptListViewAdapter = new ReceiptListViewAdapter();
        listView = (ListView)view.findViewById(R.id.receiptListView);
        listView.setAdapter(receiptListViewAdapter);
        listView.setOnItemClickListener(this);

        view.findViewById(R.id.receiptReprintBtn).setOnClickListener(this);
        view.findViewById(R.id.receiptCashBtn).setOnClickListener(this);
        view.findViewById(R.id.receiptReturnBtn).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.receiptReprintBtn:
                break;
            case R.id.receiptCashBtn:
                break;
            case R.id.receiptReturnBtn: //영수증 환불 부분
                int checkedItemIndex = listView.getCheckedItemPosition();
                if (checkedItemIndex == AdapterView.INVALID_POSITION) {
                    Toast.makeText(getContext(), "환불하고자 하는 영수증을 목록에서 선택 후 환불 버튼을 눌러주세요.", Toast.LENGTH_LONG).show();
                } else if (receiptListViewAdapter.receipts.get(checkedItemIndex).getRecType() == ReceiptList.RECEIPT_REFUND) {
                    Toast.makeText(getContext(), "이미 환불 처리가 완료된 영수증 입니다.", Toast.LENGTH_LONG).show();
                } else {
                    RefundReceiptTask refundReceiptTask = new RefundReceiptTask(receiptListViewAdapter, checkedItemIndex, getContext());
                    refundReceiptTask.execute();
                    receiptListViewAdapter.notifyDataSetChanged();
                }

                break;


        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cfragment_receipt_verify cfragmentReceiptVerify = Cfragment_receipt_verify.newInstance();
        cfragmentReceiptVerify.transmit(position);
        setChildFragment(cfragmentReceiptVerify);
    }

    private void setChildFragment(Fragment childFragment){
        FragmentTransaction childFt = getChildFragmentManager().beginTransaction();
        if(!childFragment.isAdded()){
            childFt.replace(R.id.receipt_framelayout,childFragment);
            childFt.addToBackStack(null);
            childFt.commit();
        }
    }
}