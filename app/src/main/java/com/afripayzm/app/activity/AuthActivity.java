package com.afripayzm.app.activity;

import static com.afripayzm.app.utils.UI.showErrorMsg;
import static com.afripayzm.app.utils.UI.showProgressDialog;
import static com.afripayzm.app.utils.UI.stopProgressDialog;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.afripayzm.app.Login.InfoRegisterActivity;
import com.afripayzm.app.Models.DataFire;
import com.afripayzm.app.R;
import com.afripayzm.app.fragment.auth.PhoneAuthFragment;
import com.afripayzm.app.fragment.auth.PhoneVerifyFragment;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AuthActivity extends Activity {
    List<Fragment> fragments=new ArrayList<Fragment>();
    static public final String VERIFICATION_ID="verification_id";
    public PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            try {
                stopProgressDialog();
            }
            catch (Exception e){

            }
            signInWithPhoneAuthCredential(phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            try {
                stopProgressDialog();
            }
            catch (Exception exception){

            }
            showErrorMsg(AuthActivity.this,e.toString());
        }

        @Override
        public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            stopProgressDialog();
            Bundle bundle=new Bundle();
            bundle.putString(AuthActivity.VERIFICATION_ID,verificationId);
            PhoneVerifyFragment phoneVerifyFragment=new PhoneVerifyFragment();
            phoneVerifyFragment.setArguments(bundle);
            loadFragment(phoneVerifyFragment);
        }

    };
    private DataFire dataFire;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        dataFire=new DataFire();
        loadFragment(new PhoneAuthFragment());
    }


    public void loadFragment(Fragment fragment) {
        fragments.add(fragment);
        getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
    }//loadFragment


    public void popFragment(Fragment fragment) {
        fragments.remove(fragment);
        getFragmentManager().beginTransaction().remove(fragment).commit();
    }//loadFragment


    @Override
    public void onBackPressed() {
        if(fragments.size()>1){
            popFragment(new PhoneVerifyFragment());
        }
        else {
            super.onBackPressed();
        }
    }//onBackPressed


    public void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        showProgressDialog(AuthActivity.this);
        /*FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(AuthActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        stopProgressDialog();
                        String errorMsg="Error Occurred";
                        if (task.isSuccessful()) {
                            startActivity(new Intent(AuthActivity.this,MainActivity.class));
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                errorMsg="Invalid Code";
                            }
                            Toast.makeText(AuthActivity.this,errorMsg,Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/
        dataFire.getmAuth().signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(AuthActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        stopProgressDialog();
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            String userID=user.getUid();
                            dataFire.getDbRefUsers().child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChild("username")){

                                        Toast.makeText(AuthActivity.this, getString(R.string.auth_success), Toast.LENGTH_SHORT).show();
                                        Intent continueintent = new Intent(AuthActivity.this, MainActivity.class);
                                        int mPendingIntentId = 123456;
                                        PendingIntent mPendingIntent = PendingIntent.getActivity(AuthActivity.this, mPendingIntentId, continueintent, PendingIntent.FLAG_CANCEL_CURRENT);
                                        AlarmManager mgr = (AlarmManager)AuthActivity.this.getSystemService(Context.ALARM_SERVICE);
                                        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                                        System.exit(0);

                                    }else {
                                        Intent continueintent = new Intent(AuthActivity.this, InfoRegisterActivity.class);
                                        startActivity(continueintent);
                                        Animatoo.animateSlideLeft(AuthActivity.this);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });

                        } else {
                            if (task.getException() instanceof
                                    FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(AuthActivity.this, R.string.vercodenovalid, Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                });
    }//signInWithPhoneAuthCredential


}
