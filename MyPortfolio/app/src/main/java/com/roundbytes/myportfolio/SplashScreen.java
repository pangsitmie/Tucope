package com.roundbytes.myportfolio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);



        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkUserLoginState();
            }
        },1000);
    }

    private void checkUserLoginState(){
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            // User is signed in (getCurrentUser() will be null if not signed in)
            String UID = mAuth.getUid();
            //intent
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            MainActivity.UID = UID;
            intent.putExtra("refresh","crypto");
            startActivity(intent);
            finish();
            // or do some other stuff that you want to do
        }
        else{
            Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
            startActivity(intent);
            finish();
        }
    }
}