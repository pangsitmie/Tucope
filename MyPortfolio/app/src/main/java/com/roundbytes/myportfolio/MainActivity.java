package com.roundbytes.myportfolio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.roundbytes.myportfolio.crypto.CryptoModel;
import com.roundbytes.myportfolio.fragment.CryptoFragment;
import com.roundbytes.myportfolio.fragment.StocksFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String refresh = intent.getStringExtra("refresh");

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        if(refresh.equalsIgnoreCase("stock"))
        {
            View view = bottomNav.findViewById(R.id.nav_stocks);
            view.performClick();
        }
        else
        {
            View view = bottomNav.findViewById(R.id.nav_crypto);
            view.performClick();
            //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new StocksFragment()).commit();
        }


    }
    //bottom nav listener
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()){
                case R.id.nav_stocks:
                    selectedFragment = new StocksFragment();
                    break;
                case R.id.nav_crypto:
                    selectedFragment = new CryptoFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }

    };

}