package com.example.cloudpos.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cloudpos.R;
import com.example.cloudpos.data.LineQueue;
import com.example.cloudpos.data.Slot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LineGridAdapter extends BaseAdapter {

    private final static String TAG = "LineGridAdapter>>";

    LayoutInflater inflater;
    Activity act;
    private Context context;

    public LineGridAdapter(){
    }

    @Override
    public int getCount() {
        return LineQueue.getInstance().line.size();
    }

    @Override
    public Object getItem(int position) {
        return LineQueue.getInstance().line.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.slot_layout,parent,false);

        }
        TextView slotNoTV = convertView.findViewById(R.id.slotNoTV);
        TextView personCountTV = convertView.findViewById(R.id.personCountTV);
        TextView phoneNumberTV = convertView.findViewById(R.id.phoneNumberTV);
        TextView waitingTimeTV = convertView.findViewById(R.id.waitTimeTV);

        Slot pickedSlot = LineQueue.getInstance().line.get(position);

        String startTime = pickedSlot.getTime();
        slotNoTV.setText(String.valueOf(pickedSlot.getNo()));
        personCountTV.setText(String.valueOf(pickedSlot.getPersonCount()));
        phoneNumberTV.setText(String.valueOf(pickedSlot.getPhoneNo()));
        try {
            waitingTimeTV.setText(calcWaitingTime(startTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    private String calcWaitingTime(String starter) throws ParseException {
        String waitingTime;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date startDay = new Date();
        Date now = new Date();

        //웨이팅 시작 시간
        startDay = sdf.parse(starter);

        long startTime = startDay.getTime();

        //현재 시간
        now = sdf.parse(sdf.format(now));
        long nowTime = now.getTime();
        long minute = (nowTime - startTime)/60000;

        return String.valueOf(minute);
    }
}
