package com.example.cloudpos.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.example.cloudpos.R;
import com.example.cloudpos.adapters.LineGridAdapter;
import com.example.cloudpos.data.LineQueue;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/*예약 관리 Fragment*/
//implemented by Yang Insu


public class FragmentWaiting extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private final static String TAG = "FragmentWaiting>>>";

    GridView lineGridView;
    LineGridAdapter lineGridAdapter = new LineGridAdapter();
    Button smsSenderBtn;
    Button enterBtn;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference("server/phoneNo");

    ArrayList line = LineQueue.getInstance().line;

    private int position;

    public FragmentWaiting(){
        //default public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View waitView = inflater.inflate(R.layout.waiting_fragment, container, false);

        lineGridView = waitView.findViewById(R.id.waitingSlotGridView);
        lineGridView.setAdapter(lineGridAdapter);
        lineGridView.setOnItemClickListener(this);


        //View Widget 연결
        smsSenderBtn = waitView.findViewById(R.id.sendTextBtn);
        smsSenderBtn.setOnClickListener(this);
        enterBtn = waitView.findViewById(R.id.enterBtn);
        enterBtn.setOnClickListener(this);


        return  waitView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sendTextBtn: //알림 문자 보내기
                reference.setValue(LineQueue.getInstance().line.get(position).getPhoneNo());
                break;
        }
    }

    private void sendSMS(int position){
        String phoneNo;
        String message = "5분 안에 입장 해주세요.";

        phoneNo = LineQueue.getInstance().line.get(position).getPhoneNo();

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("+82"+phoneNo, null, message, null, null);
            Toast.makeText(getContext(), "문자 전송 성공", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(getContext(), "실패", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.position = position;
    }
}