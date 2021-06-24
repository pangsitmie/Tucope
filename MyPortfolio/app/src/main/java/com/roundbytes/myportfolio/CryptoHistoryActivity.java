package com.roundbytes.myportfolio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.roundbytes.myportfolio.adapter.CryptoHistoryRecViewAdapter;
import com.roundbytes.myportfolio.adapter.CryptoRecViewAdapter;
import com.roundbytes.myportfolio.crypto.CryptosTransactions;

import java.util.ArrayList;

public class CryptoHistoryActivity extends AppCompatActivity {

    private RecyclerView cryptoHistoryRecView;
    private CryptoHistoryRecViewAdapter adapter;
    private TextView editTotalBuyValue,  editTotalCurrentValue;
    private Button backBtn;

    //FIREBASE REALTIME DATABASE VARIABLE
    public FirebaseDatabase database;
    public DatabaseReference myRef;
    private String username;

    public ArrayList<CryptosTransactions> cryptoHistory = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crypto_history);

        //VIEW INITIALIZATION
        cryptoHistoryRecView = findViewById(R.id.cryptoHistoryRecView);
        adapter = new CryptoHistoryRecViewAdapter(getApplicationContext());
        cryptoHistoryRecView.setAdapter(adapter);
        cryptoHistoryRecView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        CryptosTransactions trans1 = new CryptosTransactions(1,2,"asdf","Sell",0.1,100,100,0);
        CryptosTransactions trans2 = new CryptosTransactions(100,10,"asdf","Sell",0.1,10000,100,0);
        CryptosTransactions trans3 = new CryptosTransactions(12000,4,"asdf","Sell",0.1,3000,100,0);
        CryptosTransactions trans4 = new CryptosTransactions(6000000,4,"asdf","Buy",0.1,3000,100,0);
        cryptoHistory.add(trans1);
        cryptoHistory.add(trans2);
        cryptoHistory.add(trans3);
        cryptoHistory.add(trans4);

        adapter.setCryptoHistory(cryptoHistory);




    }
}