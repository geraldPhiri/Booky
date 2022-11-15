package com.afripayzm.app.fragment.auth;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
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

import com.afripayzm.app.R;
import com.afripayzm.app.activity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class EmailSignupFragment extends Fragment {
  FirebaseAuth fa;
  EditText email, password;
  ImageView signin;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
    View view=inflater.inflate(R.layout.fragment_emailsignup,null,false);
    fa=FirebaseAuth.getInstance();
    signin = view.findViewById(R.id.signup_btn);
    email=(EditText)view.findViewById(R.id.email);
    password=(EditText)view.findViewById(R.id.password);

    signin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(email.getText().toString().trim().contains("@") && !password.getText().toString().trim().isEmpty()) {
          fa.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
              if (task.isSuccessful()) {
                // FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid() + "/Events").setValue(new ArrayList<String>(Arrays.asList(" ")));
                FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                  @Override
                  public void onSuccess(Void aVoid) {
                    Toast.makeText(getActivity(), "we've sent an email verification link to the email account", Toast.LENGTH_SHORT).show();
                  }
                });

                Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
                getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
              }
              else {
                Toast.makeText(getActivity(), "failed", Toast.LENGTH_SHORT).show();
              }
            }
          });
        }
        else{
          //Toast.makeText(getApplicationContext(), "Check the details you entered, and only northrise university students can register", Toast.LENGTH_SHORT).show();
        }
      }//onClick
    });

    return view;
  }


}