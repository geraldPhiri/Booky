package com.afripayzm.app.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

public class UI {
    private static ProgressDialog progressDialog;

    public static void showProgressDialog(Activity activity){
        progressDialog=new ProgressDialog(activity);
        progressDialog.show();
    }//showProgressDialog


    public static void stopProgressDialog(){
        progressDialog.cancel();
    }//stopProgressDialog


    public static void showErrorMsg(Activity activity, String msg){
        Toast.makeText(activity,msg,Toast.LENGTH_SHORT).show();
    }//


    public static void showSuccessMsg(Activity activity, String msg){
        Toast.makeText(activity,msg,Toast.LENGTH_SHORT).show();
    }//
}
