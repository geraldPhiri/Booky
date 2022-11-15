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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.afripayzm.app.R;
import com.afripayzm.app.activity.CurrencyActivity;
import com.afripayzm.app.activity.MainActivity;
import com.afripayzm.app.model.Bus;
import com.afripayzm.app.subscribe.Subscribe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class HomeFragment extends Fragment {
    private FloatingActionButton fab;
    private ObservableScrollView fabScrollView;
    private LinearLayout currenciesLV;
    private static FirebaseDatabase database;
    private static DatabaseReference dbReference;
    private ChildEventListener cEL;
    private Spinner spinner, spinnerTo;
    private ArrayAdapter<String> adapter, adapterTo;
    private List<String> cities=new ArrayList<String>();
    private List<String> citiesTo=new ArrayList<String>();
    private List<DataSnapshot> snapshots=new ArrayList<DataSnapshot>();

    ArrayList buses=new ArrayList(), busFares=new ArrayList();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_main1,null,false);
        spinner=view.findViewById(R.id.spinner);
        spinnerTo=view.findViewById(R.id.spinnerTo);
        adapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line,cities);
        adapterTo=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line,citiesTo);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String from=cities.get(i);
                //show to
                for(DataSnapshot dataSnapshot:snapshots){
                    for(DataSnapshot snap:dataSnapshot.getChildren()) {
                        if(snap.getKey().equals(from)) {
                            Map<String, Integer> snapCities = snap.getValue(new GenericTypeIndicator<Map<String, Integer>>() {
                            });
                            snapCities.forEach(new BiConsumer() {
                                @Override
                                public void accept(Object o, Object o2) {
                                    String city = o.toString();
                                    if (!citiesTo.contains(city)) {
                                        citiesTo.add(city);
                                        spinnerTo.setAdapter(adapterTo);
                                    }
                                }
                            });
                        }

                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        view.findViewById(R.id.buy_ticket).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getActivity(), Subscribe.class));
                LoadBusesFragment fragment=new LoadBusesFragment();

                Bundle bundle=new Bundle();
                String from=((TextView)spinner.getSelectedView()).getText().toString();
                String to=((TextView)spinnerTo.getSelectedView()).getText().toString();
                bundle.putString("to",to);
                bundle.putString("from",from);

                /*String from=cities.get(i);*/
                //show to
                for(DataSnapshot dataSnapshot:snapshots){
                    final String bus=dataSnapshot.getKey();
                    for(DataSnapshot snap:dataSnapshot.getChildren()) {
                        if(snap.getKey().equals(from)) {
                            Map<String, Integer> snapCities = snap.getValue(new GenericTypeIndicator<Map<String, Integer>>() {
                            });
                            snapCities.forEach(new BiConsumer() {
                                @Override
                                public void accept(Object o, Object o2) {
                                    String city = o.toString();
                                    if (city.equals(to)) {
                                        buses.add(bus);
                                        busFares.add(o2.toString());
                                    }
                                }
                            });
                        }

                    }
                }

                bundle.putStringArrayList("buses",buses);
                bundle.putStringArrayList("fares",busFares);

                fragment.setArguments(bundle);
                ((MainActivity)getActivity()).loadFragment(fragment);
            }
        });
        /*currenciesLV=view.findViewById(R.id.currencies_lv);
        fab=view.findViewById(R.id.fab);
        fabScrollView=view.findViewById(R.id.fab_sv);
        fab.attachToScrollView(fabScrollView);*/


        database= FirebaseDatabase.getInstance();
        dbReference=database.getReference("ProductionDB").child("Buses");
        cEL=new ChildEventListener(){

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        snapshots.add(dataSnapshot);
                        for(DataSnapshot snap:dataSnapshot.getChildren()) {
                            String city=snap.getKey();
                            System.out.println("city:" +city );
                            if(!cities.contains(city)) {
                                cities.add(city);
                                spinner.setAdapter(adapter);
                            }
                        }
                    }
                });

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


        /*fab.show(true);
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
