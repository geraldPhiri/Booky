package com.afripayzm.app.fragment;

import static com.afripayzm.app.model.Buses.FAILED_ADD;
import static com.afripayzm.app.model.Buses.SUCCESS_ADD;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.afripayzm.app.R;
import com.afripayzm.app.activity.CurrencyActivity;
import com.afripayzm.app.activity.MainActivity;
import com.afripayzm.app.model.Bus;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;

public class SeatsFragment extends Fragment {
    private FloatingActionButton fab;
    private ObservableScrollView fabScrollView;
    private LinearLayout currenciesLV;
    private static FirebaseDatabase database;
    private static DatabaseReference dbReference;
    private ChildEventListener cEL;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_main3,null,false);
        view.findViewById(R.id.buy_ticket).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getActivity(), Subscribe.class));
                ((MainActivity)getActivity()).pushFragment(new LoadBusesFragment());
            }
        });
        /*currenciesLV=view.findViewById(R.id.currencies_lv);
        fab=view.findViewById(R.id.fab);
        fabScrollView=view.findViewById(R.id.fab_sv);
        fab.attachToScrollView(fabScrollView);

        database= FirebaseDatabase.getInstance();
        dbReference=database.getReference("Production").child("Buses");
        cEL=new ChildEventListener(){

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                View view=((LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.currency_item_1,null,false);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getActivity().startActivity(new Intent(getActivity(), CurrencyActivity.class));
                    }
                });
                currenciesLV.addView(view);
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
        dbReference.addChildEventListener(cEL);

        fab.show(true);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
        //fetchCurrencies();

        return view;
    }//onCreateView



    private void AddBus(Bus bus) {
        dbReference.push().setValue(bus.toMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(task.isSuccessful()){

                            Toast.makeText(getActivity(),SUCCESS_ADD,Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getActivity(),FAILED_ADD,Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }


    private void fetchCurrencies() {
        View view=((LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.currency_item_1,null,false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(), CurrencyActivity.class));
            }
        });
        currenciesLV.addView(view);
    }//fetchCurrencies


}//HomeFragment
