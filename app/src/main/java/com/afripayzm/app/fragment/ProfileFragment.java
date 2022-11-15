package com.afripayzm.app.fragment;

import android.app.Fragment;
import android.content.Intent;
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

public class ProfileFragment extends Fragment {
    private TextView firstNameTV, surnameTV, emailTV, phoneTV;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference firstNameRef, surnameRef, emailRef, phoneRef, profilePictureRef;
    private StorageReference sr;
    private ImageView profilePicture;
    private ValueEventListener firstNameEventListener, surnameEventListener, emailEventListener, phoneEventListener, profilePictureEventListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_profile,null,false);
        firstNameTV=view.findViewById(R.id.first_name);
        surnameTV=view.findViewById(R.id.surname);
        emailTV=view.findViewById(R.id.email);
        phoneTV=view.findViewById(R.id.phone);

        user= FirebaseAuth.getInstance().getCurrentUser();
        database=FirebaseDatabase.getInstance();
        final String uid=user.getUid();
        firstNameRef=database.getReference("ProductionDB/Users/"+uid+"/firstName");
        surnameRef=database.getReference("ProductionDB/Users/"+uid+"/surname");
        phoneRef=database.getReference("ProductionDB/Users/"+uid+"/phone");
        emailRef=database.getReference("ProductionDB/Users/"+uid+"/email");
        profilePictureRef=database.getReference("ProductionDB/Users/"+uid+"/picture");

        profilePicture=view.findViewById(R.id.profile_picture);
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //after user selects picture from gallery MainActivity.onActivityResult(...) is called
                ((MainActivity)getActivity()).onProfilePictureClick();
            }
        });

        fetchProfileDetails();
        return view;
    }//onCreateView


    private void fetchProfileDetails(){
        fetchProfilePicture();
        fetchFullName();
        fetchEmail();
        fetchPhone();
    }//fetchProfileDetails


    private void fetchFullName() {
        fetchFirstName();
        fetchSurnameName();
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


    private void fetchPhone() {
        phoneEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                phoneTV.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        phoneRef.addValueEventListener(phoneEventListener);
    }

    private void fetchEmail() {
        emailEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                emailTV.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        emailRef.addValueEventListener(emailEventListener);
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

    private void fetchSurnameName() {
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


    @Override
    public void onDestroyView() {
        firstNameRef.removeEventListener(firstNameEventListener);
        surnameRef.removeEventListener(surnameEventListener);
        phoneRef.removeEventListener(phoneEventListener);
        emailRef.removeEventListener(emailEventListener);
        profilePictureRef.removeEventListener(profilePictureEventListener);
        super.onDestroyView();
    }
}
