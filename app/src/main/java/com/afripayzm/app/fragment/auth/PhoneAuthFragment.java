package com.afripayzm.app.fragment.auth;


import static com.afripayzm.app.utils.UI.showProgressDialog;
import static com.afripayzm.app.utils.UI.stopProgressDialog;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.afripayzm.app.Models.DataFire;
import com.afripayzm.app.R;
import com.afripayzm.app.activity.AuthActivity;
import com.afripayzm.app.activity.MainActivity;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class PhoneAuthFragment extends Fragment {

  private ImageView iv_back_left_phone;
  private EditText edtPhonenumber;
  private TextView tvSendSMSCode;
  private CountryCodePicker ccpPhoneNumber;
  public static String phonenumber,codecountry,codeNamecountry;
  private DataFire dataFire;



  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
    View view=inflater.inflate(R.layout.activity_phone,null,false);
    iv_back_left_phone=view.findViewById(R.id.iv_back_left_phone);
    edtPhonenumber=view.findViewById(R.id.edtPhonenumber);
    tvSendSMSCode=view.findViewById(R.id.tvSendSMSCode);
    ccpPhoneNumber=view.findViewById(R.id.ccpPhoneNumber);/*
    pd = new ProgressDialog(getActivity());
    pd.setMessage(getString(R.string.lod_phone));*/

    dataFire=new DataFire();
    iv_back_left_phone.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent backwelcome = new Intent(getActivity(), AuthActivity.class);
        startActivity(backwelcome);
        Animatoo.animateSlideRight(getActivity());
      }
    });

    edtPhonenumber.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void afterTextChanged(Editable s) {
        if (s.length()>=8){
          tvSendSMSCode.setAlpha(1);
        }else{
          tvSendSMSCode.setAlpha(0.5f);
        }
      }
    });

    tvSendSMSCode.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(tvSendSMSCode.getAlpha()==1){
          if(!TextUtils.isEmpty(edtPhonenumber.getText())){

            showProgressDialog(getActivity());
            phonenumber= ccpPhoneNumber.getSelectedCountryCodeWithPlus()+edtPhonenumber.getText().toString();
            codecountry=ccpPhoneNumber.getSelectedCountryCode().toUpperCase();
            codeNamecountry=ccpPhoneNumber.getSelectedCountryCode().toUpperCase();
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phonenumber,
                    60,
                    TimeUnit.SECONDS,
                    getActivity(),
                    ((AuthActivity)getActivity()).callbacks
            );


          }else{
            Toast.makeText(getActivity(), R.string.plnmpho, Toast.LENGTH_SHORT).show();
            stopProgressDialog();
          }
        }
      }
    });
    
    return view;
  }

  
}
