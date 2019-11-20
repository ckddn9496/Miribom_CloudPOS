package com.example.cloudpos.data;

public class Slot implements Comparable<Slot>{

    private long no;
    private String phoneNo;
    private int type;
    private String time;
    private long personCount = 1;

    public Slot(){
    }
    public Slot(int no, String phoneNo, String time, int count){
        this.no = no;
        this.phoneNo = phoneNo;
        this.time = time;
        this.personCount = count;
    }

    public Slot(Object no, Object personCount, Object phoneNo, Object time){
        this.no = (long)no;
        this.personCount = (long)personCount;
        this.phoneNo = String.valueOf(phoneNo);
        this.time = String.valueOf(time);

    }
    public long getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getPersonCount() {
        return personCount;
    }

    public void setPersonCount(int personCount) {
        this.personCount = personCount;
    }

    @Override
    public int compareTo(Slot slot) {
        return this.no >= slot.no ? 1: -1;
    }
}
