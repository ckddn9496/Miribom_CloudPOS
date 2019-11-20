package com.example.cloudpos.data;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class Dialog {

    private static Dialog uniqueInstance = null;
    private AlertDialog.Builder builder;
//    private Context context;

    private Dialog(Context context) {
//        this.context = context;
        builder = new AlertDialog.Builder(context);
    }

    public static Dialog getInstance(Context context) {
        if (uniqueInstance == null) {
            uniqueInstance = new Dialog(context);
        }
        return uniqueInstance;
    }

    public static Dialog getInstance() {
        return uniqueInstance;
    }

    public void alertDialog(String title, String message, String positiveString, String negativeString, DialogInterface.OnClickListener posOnClickListener, DialogInterface.OnClickListener negOnClickListener) {
        builder.setTitle(title).setMessage(message);
        builder.setPositiveButton(positiveString, posOnClickListener);
        builder.setNegativeButton(negativeString, negOnClickListener);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void alertDialog(String title, String message, int drawable, String positiveString, String negativeString, DialogInterface.OnClickListener posOnClickListener, DialogInterface.OnClickListener negOnClickListener) {
        builder.setTitle(title).setMessage(message);
        builder.setIcon(drawable);
        builder.setPositiveButton(positiveString, posOnClickListener);
        builder.setNegativeButton(negativeString, negOnClickListener);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
