package com.afripayzm.app.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.afripayzm.app.R;
import com.afripayzm.app.activity.MainActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

public class SettingFragment extends Fragment {
    private TextView firstNameTV, surnameTV;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference firstNameRef, surnameRef, profilePictureRef;
    private StorageReference sr;
    private ImageView profilePicture;
    private ValueEventListener firstNameEventListener, surnameEventListener, profilePictureEventListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_setting,null,false);
        view.findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).loadFragment(new ProfileFragment());
            }
        });
        view.findViewById(R.id.term_and_conditions).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //((MainActivity)getActivity()).loadFragment(new ProfileFragment());
            }
        });
        view.findViewById(R.id.privacy_policy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //((MainActivity)getActivity()).loadFragment(new ProfileFragment());
            }
        });
        firstNameTV=view.findViewById(R.id.first_name);
        surnameTV=view.findViewById(R.id.surname);

        user= FirebaseAuth.getInstance().getCurrentUser();
        database= FirebaseDatabase.getInstance();
        final String uid=user.getUid();
        firstNameRef=database.getReference("ProductionDB/Users/"+uid+"/firstName");
        surnameRef=database.getReference("ProductionDB/Users/"+uid+"/surname");
        profilePictureRef=database.getReference("ProductionDB/Users/"+uid+"/picture");

        profilePicture=view.findViewById(R.id.profile_picture);

        fetchProfileDetails();
        return view;
    }


    public void fetchProfileDetails(){
        fetchProfilePicture();
        fetchFullName();
    }//fetchProfileDetails


    private void fetchFullName() {
        fetchFirstName();
        fetchSurname();
    }


    private void fetchFirstName() {
        firstNameEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                firstNameTV.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        firstNameRef.addValueEventListener(firstNameEventListener);
    }

    private void fetchSurname() {
        surnameEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                surnameTV.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        surnameRef.addValueEventListener(surnameEventListener);
    }


    private void fetchProfilePicture() {
        profilePictureEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Glide.with(profilePicture.getContext()).load(dataSnapshot.getValue(String.class)).apply(new RequestOptions().circleCrop()).into(profilePicture);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        profilePictureRef.addValueEventListener(profilePictureEventListener);
    }


    @Override
    public void onDestroyView() {
        firstNameRef.removeEventListener(firstNameEventListener);
        surnameRef.removeEventListener(surnameEventListener);
        profilePictureRef.removeEventListener(profilePictureEventListener);
        super.onDestroyView();
    }
}
