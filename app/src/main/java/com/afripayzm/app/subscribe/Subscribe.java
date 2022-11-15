package com.afripayzm.app.subscribe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.afripayzm.app.R;
import com.flutterwave.raveandroid.RavePayActivity;
import com.flutterwave.raveandroid.RaveUiManager;
import com.flutterwave.raveandroid.rave_java_commons.RaveConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.rbddevs.splashy.Splashy;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class Subscribe extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscribe);
        new RaveUiManager(Subscribe.this).setAmount(2)
                .setEmail("geraldaphiri@gmail.com")
                .setfName("Gerald")
                .setlName("Phiri")
                .setCountry("NG")
                .setCurrency("ZMW")
                //setNarration(narration)
                .setPublicKey("FLWPUBK-bde2c89cb96542b99c682129eea76674-X")
                .setEncryptionKey("246902470ae6cb4425a1c24a")
                .setTxRef(UUID.randomUUID().toString())
                //.acceptAccountPayments(true)
                //.acceptCardPayments(true)
                //.acceptMpesaPayments(true)
                //.acceptAchPayments(boolean)
                //.acceptGHMobileMoneyPayments(boolean)
                //.acceptUgMobileMoneyPayments(boolean)
                .acceptZmMobileMoneyPayments(true)
                //.acceptRwfMobileMoneyPayments(boolean)
                //.acceptUkPayments(boolean)
                //.acceptBankTransferPayments(true)
                //.acceptUssdPayments(boolean)
                //.acceptFrancMobileMoneyPayments(boolean)
                //.onStagingEnv(true)
                //.setMeta(List<Meta>)
                // .withTheme(styleId)
                //.isPreAuth(boolean)
                // .setSubAccounts(List<SubAccount>)
                .shouldDisplayFee(true)
                .showStagingLabel(true)
                .initialize();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*
         *  We advise you to do a further verification of transaction's details on your server to be
         *  sure everything checks out before providing service or goods.
         */


        if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null) {
            String message = data.getStringExtra("response");
            if (resultCode == RavePayActivity.RESULT_SUCCESS) {
                //Toast.makeText(this, "SUCCESS " + message, Toast.LENGTH_SHORT).show();
                Map<String, String> map=new HashMap<>();
                FirebaseDatabase.getInstance()
                        .getReference("user/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/history")
                        .push()
                        .setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            setResult(RESULT_OK);
                            finish();
                        }
                        else {
                            //Todo:Tell user to contact support has payment was made but may have not been recorded in database.
                        }
                    }
                });

            }
            else if (resultCode == RavePayActivity.RESULT_ERROR) {
                Toast.makeText(this, "ERROR " + message, Toast.LENGTH_SHORT).show();
                setResult(RESULT_CANCELED);
                finish();
            }
            else if (resultCode == RavePayActivity.RESULT_CANCELLED) {
                //Toast.makeText(this, "CANCELLED " + message, Toast.LENGTH_SHORT).show();
                setResult(RESULT_CANCELED);
                finish();
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    void showTicket(){

    }


}
