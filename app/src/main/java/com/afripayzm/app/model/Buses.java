package com.afripayzm.app.model;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Buses {
    public static final String SUCCESS_ADD = "Bus added Successfully";
    public static final String FAILED_ADD = "Failed to add Bus";

    private static FirebaseDatabase database;
    private static DatabaseReference dbReference;

    static {
        database=FirebaseDatabase.getInstance();
        dbReference=database.getReference("Production").child("Buses");
    }

    static public void fetchAll(String filter){

    }

    static public void addBus(Activity act, Bus bus){

        dbReference.push().setValue(bus.toMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(act,SUCCESS_ADD,Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(act,FAILED_ADD,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
