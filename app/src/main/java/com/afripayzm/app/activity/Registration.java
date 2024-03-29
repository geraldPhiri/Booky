package com.afripayzm.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.afripayzm.app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Registration extends Activity {
  FirebaseAuth fa;
  EditText email, password;
  TextView signin;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.signup);
    fa=FirebaseAuth.getInstance();
    signin = findViewById(R.id.signin);
    email=(EditText)findViewById(R.id.email);
    password=(EditText)findViewById(R.id.password);

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
                    Toast.makeText(getApplicationContext(), "we've sent an email verification link to the email account", Toast.LENGTH_SHORT).show();
                  }
                });

                Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Registration.this, MainActivity.class));
              }
              else {
                Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
              }
            }
          });
        }
        else{
          //Toast.makeText(getApplicationContext(), "Check the details you entered, and only northrise university students can register", Toast.LENGTH_SHORT).show();
        }
      }//onClick
    });

  }
}