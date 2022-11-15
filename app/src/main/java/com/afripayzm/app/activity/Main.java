package com.afripayzm.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.afripayzm.app.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.List;

public class Main extends AppCompatActivity {
    LinearLayout linearLayout;

    FirebaseDatabase database;
    DatabaseReference reference;
    ChildEventListener childEventListener;

    private List<String> productName=new ArrayList();
    private List<String> productCount=new ArrayList();
    private ListView listViewStock;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock);
        linearLayout=findViewById(R.id.listview_stock);

        database=FirebaseDatabase.getInstance();
        reference=database.getReference("ProductionDB/Stock/");

        childEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ArrayList<String> item=dataSnapshot.getValue(new GenericTypeIndicator<ArrayList<String>>(){});
                final String itemName=item.get(0);
                final String itemCount=item.get(1);
                productName.add(itemName);
                productCount.add(itemCount);

                LayoutInflater layoutInflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View convertView=layoutInflater.inflate(R.layout.stock_item,null,true);
                TextView textViewName=convertView.findViewById(R.id.product_name);
                TextView textViewCount=convertView.findViewById(R.id.product_count);

                textViewName.setText(itemName);
                textViewCount.setText(itemCount);

                linearLayout.addView(convertView);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        reference.addChildEventListener(childEventListener);


    }


    @Override
    protected void onDestroy() {
        reference.removeEventListener(childEventListener);
        super.onDestroy();
    }


    //ToDo:implement
    public void addToStock(View view){
        startActivity(new Intent(this,AddStock.class));
    }

}
