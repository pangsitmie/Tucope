package com.roundbytes.myportfolio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roundbytes.myportfolio.activity.WelcomeActivity;
import com.roundbytes.myportfolio.crypto.CryptoModel;
import com.roundbytes.myportfolio.fragment.CryptoFragment;
import com.roundbytes.myportfolio.menu.ActivityDonate;
import com.roundbytes.myportfolio.menu.ActivitySecurity;
import com.roundbytes.myportfolio.menu.ActivitySetting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView navigationView;

    public String USERNAME;
    public static String UID;
    public static String CURRENCY = "USD";

    //FIREBASE REALTIME DATABASE VARIABLE
    public FirebaseDatabase database;
    public DatabaseReference myRef;

    public static ArrayList<CryptoModel> cryptoModelsArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);




        /*Toolbar*/
        setSupportActionBar(toolbar);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);

        //UPDATE HEADER INFO
        updateNavHeader();


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

        //GET CRYPTO MARKET DATA
        getCryptoMarketData();

        //GET CURRENCY CONVERSION DATA
        getConversionRate();


    }
    //DRAWER NAV LISTENER
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_profile:
                Toast.makeText(this, "Stay tune for the next update!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_privacy:
                Intent intent2 = new Intent(getApplicationContext(), ActivitySecurity.class);
                startActivity(intent2);
                break;
            case R.id.nav_help:
                Toast.makeText(this, "Stay tune for the next update!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_donate:
                Intent intent4  = new Intent(getApplicationContext(), ActivityDonate.class);
                startActivity(intent4);
                break;
            case R.id.nav_setting:
                /*Intent intent5  = new Intent(getApplicationContext(), ActivitySetting.class);
                startActivity(intent5);*/
                Toast.makeText(this, "Stay tune for the next update!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_share:
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "Your Subject");
                    String sAux = "\nCheck out this amazing application\n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=com.roundbytes.myportfolio \n"; // here define package name of you app
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "choose one"));
                } catch (Exception e) {
                    Log.e(">>>", "Error: " + e);
                }
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent1 = new Intent(getApplicationContext(), WelcomeActivity.class);
                startActivity(intent1);
                Toast.makeText(this, "Log Out Successful", Toast.LENGTH_SHORT).show();
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //UPDATE LEFT NAVIGATION MENU HEADER
    public void updateNavHeader(){
        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView headerUserName = headerView.findViewById(R.id.navUserName);
        TextView headerUserID = headerView.findViewById(R.id.navUserID);
        TextView headerCurrency = headerView.findViewById(R.id.navCurrency);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users").child(UID).child("A_Info");
        DatabaseReference usernameRef = myRef.child("name");
        DatabaseReference currencyRef = myRef.child("currency");

        //set CURRENCY VARIABLE VALUE


        usernameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                USERNAME = snapshot.getValue(String.class);
                headerUserName.setText("Hi, "+USERNAME);
                headerUserID.setText("UID: "+UID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        currencyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CURRENCY = snapshot.getValue(String.class);
                headerCurrency.setText("CURRENCY: "+CURRENCY);


                Log.d("CURRENCY", "onCreate: "+CURRENCY);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //bottom nav listener
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()){
                case R.id.nav_stocks:
                    /*selectedFragment = new StocksFragment();*/
                    Toast.makeText(getApplicationContext(), "Stay tune for the next update", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.nav_crypto:
                    selectedFragment = new CryptoFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    break;

            }
            /*getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();*/
            return true;
        }

    };

    public void getCryptoMarketData(){
        //loadingPB.setVisibility(View.VISIBLE);
        String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        //json object
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //loadingPB.setVisibility(View.GONE);
                try {
                    JSONObject status = response.getJSONObject("status");
                    int elapsed = status.getInt("elapsed");
                    Log.d("elapsed", elapsed+";");

                    JSONArray dataArray = response.getJSONArray("data");
                    for (int i=0;i<dataArray.length(); i++){
                        JSONObject dataObj = dataArray.getJSONObject(i);
                        String name = dataObj.getString("name");
                        String symbol = dataObj.getString("symbol");

                        JSONObject quote = dataObj.getJSONObject("quote");
                        JSONObject USD = quote.getJSONObject("USD");
                        double price = USD.getDouble("price");

                        cryptoModelsArrayList.add(new CryptoModel(name, symbol, price));
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Failed to extract json data", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //loadingPB.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Failed to get data", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("X-CMC_PRO_API_KEY","af47668c-30ad-42e4-9461-7cf302924267");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    public void getConversionRate(){
        String convertUrl = "http://api.currencylayer.com/live?access_key=ea96fc40df17f730d076f56d056a069d";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        if(!CURRENCY.equals("USD")){
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, convertUrl, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject mainOBJ = response.getJSONObject("quotes");
                        Log.d("ALL", mainOBJ+"");
                        String requestCurrency = "USD"+CURRENCY;
                        double rate = response.getJSONObject("quotes").getDouble(requestCurrency);
                        Log.d(requestCurrency, rate+"");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("currency", "onResponse: FAIL");
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Toast.makeText(getApplicationContext(), "FAILED TO GET CURRENCY DATA", Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    //KALAU GA ADA HEADER TTP HARUS BUAT HEADER TAPI GA USA DIISI HEADER E
                    HashMap<String, String> headers = new HashMap<>();
                    return headers;
                }
            };
            requestQueue.add(jsonObjectRequest);
        }
    }

    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }


}