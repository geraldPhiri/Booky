package com.afripayzm.app.activity;

import static com.afripayzm.app.utils.UI.showErrorMsg;
import static com.afripayzm.app.utils.UI.showSuccessMsg;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.afripayzm.app.R;
import com.afripayzm.app.fragment.HistoryFragment;
import com.afripayzm.app.fragment.HomeFragment;
import com.afripayzm.app.fragment.NotificationsFragment;
import com.afripayzm.app.fragment.SettingFragment;
import com.afripayzm.app.fragment.TransactionsFragment;
import com.afripayzm.app.fragment.auth.PhoneAuthFragment;
import com.afripayzm.app.fragment.auth.PhoneVerifyFragment;
import com.emredavarci.noty.Noty;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.saadahmedsoft.popupdialog.PopupDialog;
import com.saadahmedsoft.popupdialog.Styles;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;


public class MainActivity extends Activity {
    private String currentFragment;
    private LinearLayout bottomBar;
    public static int PROFILE_PICTURE=1;
    public static int PROFILE_PICTURE_CROP=2;
    public static int PAYMENT=4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //set up bottombar
        bottomBar=findViewById(R.id.bottom_bar);
        onTabClick(bottomBar.getChildAt(0));
    }//onCreate


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==PROFILE_PICTURE) {
            if(resultCode == RESULT_OK){
                final Uri uri = data.getData();
                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                final String uid = user.getUid();
                StorageReference sr = FirebaseStorage.getInstance().getReference().child(uid);
                sr.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            sr.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(final Uri uri) {
                                    //upload image url to database
                                    FirebaseDatabase.getInstance().getReference("ProductionDB/Users/"+uid+"/picture").setValue(uri.toString())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                showSuccessMsg(MainActivity.this,"Upload successful");
                                            }
                                            else {
                                                showErrorMsg(MainActivity.this, "failed to upload photo");
                                            }
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    showErrorMsg(MainActivity.this, "failed to upload photo");
                                }
                            });

                            showSuccessMsg(MainActivity.this, "upload successful");
                        } else {
                            showErrorMsg(MainActivity.this, "upload failed");
                        }
                    }
                });

            }

        }
        else if(requestCode==PAYMENT){
            final RelativeLayout rl=findViewById(R.id.main_layout);
            final int height=54;
            if(resultCode==RESULT_OK){
                Noty.init(MainActivity.this, "Payment Success", rl,
                        Noty.WarningStyle.ACTION)
                        .setActionText("")
                        .setWarningBoxBgColor("#00AAFF")
                        .setWarningTappedColor("#00AAEE")
                        .setHeightDp(height)
                        .setWarningBoxPosition(Noty.WarningPos.TOP)
                        .setWarningInset(0,0,0,0)
                        .setWarningBoxRadius(0,0,0,0)
                        .setAnimation(Noty.RevealAnim.SLIDE_DOWN, Noty.DismissAnim.BACK_TO_TOP, 400,400)
                        .show();
            }
            else {
                Noty.init(MainActivity.this, "Payment Failed", rl,
                        Noty.WarningStyle.ACTION)
                        .setActionText("")
                        .setWarningBoxBgColor("#ff5c33")
                        .setWarningTappedColor("#ff704d")
                        .setHeightDp(height)
                        .setWarningBoxPosition(Noty.WarningPos.TOP)
                        .setWarningInset(0,0,0,0)
                        .setWarningBoxRadius(0,0,0,0)
                        .setAnimation(Noty.RevealAnim.SLIDE_DOWN, Noty.DismissAnim.BACK_TO_TOP, 400,400)
                        .show();
            }
        }
    }//onActivityResult


    public void onProfilePictureClick(){
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, MainActivity.PROFILE_PICTURE);
    }//onProfilePictureClick


    public void loadFragment(Fragment fragment) {
        currentFragment=fragment.getClass().toString();
        getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
    }//loadFragment


    public void pushFragment(Fragment fragment){
        currentFragment=fragment.getClass().toString();
        getFragmentManager().beginTransaction().add(R.id.fragment_container,fragment).commit();
    }


    public void popFragment(Fragment fragment){
        currentFragment=fragment.getClass().toString();
        getFragmentManager().beginTransaction().remove(fragment).commit();
    }


    public void onTabClick(View view){
        LinearLayout tab=(LinearLayout)view;
        TextView text= (TextView) tab.getChildAt(1);
        setTabColors(tab);
        switch (text.getText().toString().trim().toLowerCase()){
            case "home":
                loadFragment(new HomeFragment());
                break;
            case "history":
                loadFragment(new HistoryFragment());
                break;
            case "notification":
                loadFragment(new NotificationsFragment());
                break;
            case "more":
                loadFragment(new SettingFragment());
                break;
        }

    }//onTabClicked


    private void setTabColors(LinearLayout tab) {
        ((ImageView) tab.getChildAt(0)).setColorFilter(getColor(R.color.active));
        ((TextView) tab.getChildAt(1)).setTextColor(getColor(R.color.active));

        for(int i=0;i<bottomBar.getChildCount();i++){
            LinearLayout currentTab= (LinearLayout) bottomBar.getChildAt(i);
            ImageView icon= (ImageView) currentTab.getChildAt(0);
            TextView title= (TextView) currentTab.getChildAt(1);
            if(currentTab!=tab){
                icon.setColorFilter(getColor(R.color.inactive));
                title.setTextColor(getColor(R.color.inactive));
            }
        }
    }//setTabColors


    @Override
    public void onBackPressed() {
        if(currentFragment.equals(HomeFragment.class.toString())){
            showExitDialog();
        }
        else{
            loadFragment(new HomeFragment());
        }
    }//onBackPresssed


    private void showExitDialog() {
        PopupDialog.getInstance(this)
                .setStyle(Styles.STANDARD)
                .setHeading("Exit")
                .setHeading("Exit Booky")
                .setPopupDialogIcon(R.drawable.icon_call_cancel)
                .setPopupDialogIconTint(R.color.white)
                .setCancelable(false)
                .setPositiveButtonText("Exit")
                .showDialog(new OnDialogButtonClickListener() {
                    @Override
                    public void onPositiveClicked(Dialog dialog) {
                        /*super.onPositiveClicked(dialog);*/
                        finish();
                    }

                    @Override
                    public void onNegativeClicked(Dialog dialog) {
                        super.onNegativeClicked(dialog);
                    }
                });
    }//showExitDialog


}//MainActivity

