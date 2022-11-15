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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;

import java.util.List;

public class LoadBusesFragment extends Fragment {
    private FloatingActionButton fab;
    private ObservableScrollView fabScrollView;
    private LinearLayout currenciesLV;
    private static FirebaseDatabase database;
    private static DatabaseReference dbReference;
    private ChildEventListener cEL;
    TextView from, to;
    List<String> buses, busFares;
    LinearLayout busList;
    private String currency="K";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_main2,null,false);

        to=view.findViewById(R.id.to);
        from=view.findViewById(R.id.from);

        busList=view.findViewById(R.id.busesList);

        from.setText(getArguments().getString("from"));
        to.setText(getArguments().getString("to"));

        buses=getArguments().getStringArrayList("buses");
        busFares=getArguments().getStringArrayList("fares");

        for(int i=0; i<buses.size();++i){
            LayoutInflater layoutInflater=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View convertView=layoutInflater.inflate(R.layout.bus_view,null,true);
            TextView amount=convertView.findViewById(R.id.amnt);
            TextView bus=convertView.findViewById(R.id.bus);
            TextView time=convertView.findViewById(R.id.time);


            bus.setText(buses.get(i));
            amount.setText(currency+busFares.get(i));
            time.setText("05:00 am");

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(getActivity(), Subscribe.class);
                    intent.putExtra("key",)
                    getActivity().startActivityForResult(intent,MainActivity.PAYMENT);
                }
            });

            busList.addView(convertView);
        }

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
