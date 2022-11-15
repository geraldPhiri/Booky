package com.afripayzm.app.Login;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.afripayzm.app.Models.DataFire;
import com.afripayzm.app.R;
import com.afripayzm.app.activity.MainActivity;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.auth.UserInfo;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class InfoRegisterActivity extends AppCompatActivity {

    private TextView tvNextInfoRegister;
    private EditText edtNameInfoRegister;
    public static String name, photoUrlfb;
    private DataFire dataFire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_register);
        dataFire=new DataFire();
        wedgets();


        tvNextInfoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=edtNameInfoRegister.getText().toString();

                    if (edtNameInfoRegister.getText().length() < 6) {
                        Toast.makeText(InfoRegisterActivity.this, R.string.greater6name, Toast.LENGTH_SHORT).show();
                        return;
                    }

                Intent continueAddPic = new Intent(InfoRegisterActivity.this, MainActivity.class);
                startActivity(continueAddPic);
                Animatoo.animateSlideLeft(InfoRegisterActivity.this);
            }
        });

       edtNameInfoRegister.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
               if(count>0){
                   tvNextInfoRegister.setEnabled(true);
               }else {
                   tvNextInfoRegister.setEnabled(false);
               }
           }

           @Override
           public void afterTextChanged(Editable s) {
           }
       });

       /*if(WelcomeActivity.typeAccount!=null) {
           if (WelcomeActivity.typeAccount.equals("facebook")) {
               //getDataFromFacebook();
           }
       }*/

    }

    private void wedgets() {
        tvNextInfoRegister =findViewById(R.id.tvNextInfoRegister);
        edtNameInfoRegister = findViewById(R.id.edtNameInfoRegister);
    }



}
