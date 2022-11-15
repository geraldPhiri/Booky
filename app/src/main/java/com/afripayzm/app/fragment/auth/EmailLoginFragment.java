package com.afripayzm.app.fragment.auth;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

//import com.facebook.CallbackManager;
import com.afripayzm.app.R;
import com.afripayzm.app.activity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class EmailLoginFragment extends Fragment {
  //  CallbackManager callbackManager;
  FirebaseAuth fa;
  EditText email=null;
  EditText password=null;
  ImageView loginBtn=null;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
    View view=inflater.inflate(R.layout.fragment_emaillogin,null,false);

    email=view.findViewById(R.id.l_email);
    password=view.findViewById(R.id.l_password);
    loginBtn=view.findViewById(R.id.login_btn);
    fa=FirebaseAuth.getInstance();
    //callbackManager = CallbackManager.Factory.create();

    loginBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        final String emailString=email.getText().toString();
        if (!emailString.trim().isEmpty() && !password.getText().toString().trim().isEmpty()) {
          //if some requirements met create a signed in user
          fa.signInWithEmailAndPassword(emailString, password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
              if (task.isSuccessful()) {
                getActivity().getPreferences(0).edit().putString("email",emailString).commit();
                Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
                getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
              } else {
                Toast.makeText(getActivity(), "failed", Toast.LENGTH_SHORT).show();
              }
            }
          });
        }
        else{
          Toast.makeText(getActivity(),"please fill in email and password(8 characters long)",Toast.LENGTH_SHORT).show();
        }
      }
    });
    return view;
  }


}
