package com.afripayzm.app.Login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.afripayzm.app.Models.DataFire;
import com.afripayzm.app.R;
import com.afripayzm.app.activity.AuthActivity;
import com.afripayzm.app.activity.MainActivity;
import com.google.firebase.database.FirebaseDatabase;
import com.rbddevs.splashy.Splashy;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SplashScreenActivity extends AppCompatActivity {

    private void enablePersistenceStorage(boolean b) {
        FirebaseDatabase.getInstance().setPersistenceEnabled(b);
    }//enablePersistenceStorage

    private DataFire dataFire;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        enablePersistenceStorage(true);

        /*try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.couplesdatingzm.app",
                    PackageManager.GET_SIGNATURES
            );
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("KeyHash:", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }*/


        dataFire=new DataFire();

        /*SharedPreferences prefs = getSharedPreferences("changeLanguage", MODE_PRIVATE);
        String language_key = prefs.getString("language_key", null);
        if(language_key!=null){
            changeLanguage(language_key);
        }
        else{

        }*/

        setSplashy();/*
        setContentView(R.layout.activity_splash_screen);*/
    }

    private void changeLanguage(String codeLang){

        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            conf.setLocale(new Locale(codeLang.toLowerCase()));
        }else{
            conf.locale = new Locale(codeLang.toLowerCase());
        }
        res.updateConfiguration(conf,dm);
    }

    private void  setSplashy(){

       Splashy splashy = new Splashy(this);

       splashy.setLogo(R.drawable.ic_launcher_foreground)
                .setTitle(R.string.app_name)
                .setTitleColor("#00AAFF")
                .showProgress(true)
               .showProgress(false)
                .setSubTitle(getString(R.string.sutitl_splash))
                .setSubTitleColor(R.color.main_text)
                .setBackgroundResource(R.color.white)
                .setFullScreen(true)
                .setTime(5000)
                .setAnimation(Splashy.Animation.GROW_LOGO_FROM_CENTER,800)
                .setTitleFontStyle("fonts/avenir_next_bold.otf")
                .setSubTitleFontStyle("fonts/avenir_next_medium.otf");

        splashy.show();


        new Splashy.OnComplete() {
            @Override
            public void onComplete() {

                /* New Handler to start the MainActivity
                 * and close this Splash-Screen after some seconds.*/
                /** Duration of wait **/ // 3.5 seconds
                int SPLASH_DISPLAY_LENGTH = 5500;
                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {

                        if(dataFire.getCurrentUser()!=null){
                            /* Create an Intent that will start the MainActivity. */
                            Intent homeintent = new Intent(SplashScreenActivity.this, MainActivity.class);
                            startActivity(homeintent);
                            finish();
                        }else{
                            Intent homeintent = new Intent(SplashScreenActivity.this, AuthActivity.class);
                            startActivity(homeintent);
                            finish();
                        }

                    }
                }, SPLASH_DISPLAY_LENGTH);


            }
        }.onComplete();



    }

}
