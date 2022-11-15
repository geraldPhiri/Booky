package com.afripayzm.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.afripayzm.app.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class AddStock extends AppCompatActivity {
    EditText editTextName,editTextCount;

    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_stock);
        editTextName=findViewById(R.id.product_name);
        editTextCount=findViewById(R.id.product_count);
        database= FirebaseDatabase.getInstance();
        reference=database.getReference("ProductionDB/Stock/");

    }


    public void onSubmit(View view){
        String name=editTextName.getText().toString();
        String count=editTextCount.getText().toString();

        /*try {
            FileOutputStream fos = openFileOutput(getIntent().getStringExtra("barcode") + ".txt", MODE_PRIVATE);
            PrintWriter pw=new PrintWriter(fos);
            pw.println(editTextName.getText().toString()+"\n"+editTextCount.getText().toString());
            pw.close();
            fos.close();
        }
        catch (Exception ex){
            Toast.makeText(getApplicationContext(),ex.toString(),Toast.LENGTH_LONG).show();
        }*/

        reference.push().setValue(new ArrayList<String>(Arrays.asList(name,count)));

        finish();
    }


}
