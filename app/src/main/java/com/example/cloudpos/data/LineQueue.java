package com.example.cloudpos.data;

//implemented by Yang Insu

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class LineQueue {
    private static LineQueue lineQueue = new LineQueue();
    public PriorityQueue<Slot> linePQueue = new PriorityQueue<>();
    public ArrayList<Slot> line = new ArrayList<>();

    private LineQueue(){
    }

    public static LineQueue getInstance(){return lineQueue;}

}
