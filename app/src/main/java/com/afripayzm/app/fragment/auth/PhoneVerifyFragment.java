package com.afripayzm.app.fragment.auth;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import com.afripayzm.app.Login.InfoRegisterActivity;
import com.afripayzm.app.Models.DataFire;
import com.afripayzm.app.R;
import com.afripayzm.app.activity.AuthActivity;
import com.afripayzm.app.activity.MainActivity;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jkb.vcedittext.VerificationCodeEditText;

public class PhoneVerifyFragment extends Fragment {
  private String mAuthVerificationId;
  private VerificationCodeEditText codeview;
  private TextView tv_verify_code,tv_resend_code;
  private String userID;
  private DataFire dataFire;
  private ImageView imgv_back_left_verifyphone;



  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
    View view=inflater.inflate(R.layout.activity_verify_phone,null,false);



    dataFire=new DataFire();
    codeview=view.findViewById(R.id.codeview);
    tv_verify_code=view.findViewById(R.id.tv_verify_code);
    tv_resend_code=view.findViewById(R.id.tv_resend_code);
    imgv_back_left_verifyphone=view.findViewById(R.id.imgv_back_left_verifyphone);

    mAuthVerificationId = getArguments().getString(AuthActivity.VERIFICATION_ID);

    codeview.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
      }
      @Override
      public void afterTextChanged(Editable s) {
        if (s.length()==6){
          tv_verify_code.setAlpha(1);
        }else{
          tv_verify_code.setAlpha(0.5f);
        }
      }
    });

    imgv_back_left_verifyphone.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        /*Intent backwelcome = new Intent(getActivity(),PhoneAuthActivity.class);
        startActivity(backwelcome);
        Animatoo.animateSlideRight(getActivity());*/
        getActivity().onBackPressed();
      }
    });

    tv_verify_code.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(tv_verify_code.getAlpha()==1){
          tv_resend_code.setVisibility(View.VISIBLE);
          tv_verify_code.setAlpha(0.5f);
          codeview.setEnabled(false);
          counterTimeSendCode();
          if(!TextUtils.isEmpty(codeview.getText().toString())){
            String code = codeview.getText().toString();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mAuthVerificationId, code);
            ((AuthActivity)getActivity()).signInWithPhoneAuthCredential(credential);
          }else{
            Toast.makeText(getActivity(), R.string.enter_verifi_code, Toast.LENGTH_SHORT).show();

          }
        }
      }
    });


    tv_resend_code.setOnClickListener( new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(tv_verify_code.getAlpha()==1){
          tv_verify_code.setAlpha(0.5f);
          tv_resend_code.setAlpha(0.5f);
          codeview.setEnabled(false);
          counterTimeSendCode();
          if(!TextUtils.isEmpty(codeview.getText().toString())){
            String code = codeview.getText().toString();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mAuthVerificationId, code);
            ((AuthActivity)getActivity()).signInWithPhoneAuthCredential(credential);
          }else{
            Toast.makeText(getActivity(), R.string.enter_verifi_code, Toast.LENGTH_SHORT).show();
          }
        }
      }
    });
    return view;
  }


  private void counterTimeSendCode(){
    new CountDownTimer(60000, 1000) {

      @SuppressLint("SetTextI18n")
      public void onTick(long millisUntilFinished) {
        tv_resend_code.setText(getResources().getString(R.string.login_input_resent)+" - "+String.valueOf(millisUntilFinished / 1000));
        //here you can have your logic to set text to edittext
      }
      public void onFinish() {
        tv_resend_code.setText(getResources().getString(R.string.login_input_resent));
        tv_resend_code.setAlpha(1);
        tv_verify_code.setAlpha(1);
        codeview.setEnabled(true);
      }

    }.start();
  }


}
