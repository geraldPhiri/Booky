package com.afripayzm.app.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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

public class HistoryFragment extends Fragment {
    private LinearLayout notificationsLV;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference notificationsRef;
    private ChildEventListener notificationsEventListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_history,null,false);
        /*notificationsLV=view.findViewById(R.id.notifications_lv);
        user= FirebaseAuth.getInstance().getCurrentUser();
        database= FirebaseDatabase.getInstance();
        notificationsRef=database.getReference("ProductionDB/Users/"+user.getUid()+"/notifications");

        fetchNotifications();*/
        return view;
    }


    private void fetchNotifications() {
        View view=((LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.notify_item_1,null,false);
        notificationsLV.addView(view);

        notificationsEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                View view=((LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.notify_item_1,null,false);
                //...
                view.setTag(dataSnapshot.getKey());
                notificationsLV.addView(view);
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
        notificationsRef.addChildEventListener(notificationsEventListener);


    }//fetchNotifications


    @Override
    public void onDestroyView() {
//        notificationsRef.removeEventListener(notificationsEventListener);
        super.onDestroyView();
    }//onDestroyView

}
