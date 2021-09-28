package com.roundbytes.myportfolio;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro2);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_intro1()).commit();
    }
}