package com.afripayzm.app.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.afripayzm.app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TransactionsFragment extends Fragment {
    private LinearLayout transactionsLV;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference transactionsRef;
    private ChildEventListener transactionsEventListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_transactions,null,false);
        transactionsLV=view.findViewById(R.id.transactions_lv);

        user= FirebaseAuth.getInstance().getCurrentUser();
        database= FirebaseDatabase.getInstance();
        transactionsRef=database.getReference("ProductionDB/Users/"+user.getUid()+"/transactions");

        fetchTransactions();
        return view;
    }//onCreateView


    private void fetchTransactions() {
        View view=((LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.transaction_item_1,null,false);
        transactionsLV.addView(view);

        transactionsEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                View view=((LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.transaction_item_1,null,false);
                //...
                view.setTag(dataSnapshot.getKey());
                transactionsLV.addView(view);
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
        transactionsRef.addChildEventListener(transactionsEventListener);


    }//fetchTransactions


    @Override
    public void onDestroyView() {
        transactionsRef.removeEventListener(transactionsEventListener);
        super.onDestroyView();
    }
}
