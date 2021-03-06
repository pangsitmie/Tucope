package com.roundbytes.myportfolio.stock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roundbytes.myportfolio.MainActivity;
import com.roundbytes.myportfolio.R;
import com.roundbytes.myportfolio.adapter.StockHistoryRecViewAdapter;
import com.roundbytes.myportfolio.crypto.CryptosTransactions;
import com.roundbytes.myportfolio.stock.StocksTransactions;

import java.util.ArrayList;

public class StockHistoryActivity extends AppCompatActivity {
    private RecyclerView stockHistoryRecView;
    private StockHistoryRecViewAdapter adapter;
    private TextView editTotalBuyValue,  editTotalCurrentValue;
    private ImageView backBtn;

    //FIREBASE REALTIME DATABASE VARIABLE
    public FirebaseDatabase database;
    public DatabaseReference myRef;

    private String CODE;

    public ArrayList<StocksTransactions> stockHistoryArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_history);

        Log.d("STOCKHISTORY", MainActivity.UID);
        //INTENT EXTRAS
        Intent intent = getIntent();
        CODE = intent.getStringExtra("CODE");
        //Log.d("TAG", "onCreate: "+CODE);

        //VIEW INITIALIZATION
        stockHistoryRecView = findViewById(R.id.stockHistoryRecView);
        backBtn = findViewById(R.id.btnBack);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                intent1.putExtra("refresh","stock");
                startActivity(intent1);
            }
        });

        InitializeTransactions();

        adapter = new StockHistoryRecViewAdapter(getApplicationContext());
        stockHistoryRecView.setAdapter(adapter);
        stockHistoryRecView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        adapter.setStockHistory(stockHistoryArray);
    }
    private void InitializeTransactions()
    {
        //RECYCLER VIEW FIREBASE
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users").child(MainActivity.UID).child("StockTotal");
        DatabaseReference stockListRef = myRef.child("StockList");
        DatabaseReference stockCodeRef = stockListRef.child(CODE);
        DatabaseReference stockTransactionRef = stockCodeRef.child("Transactions");

        stockTransactionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    StocksTransactions stocksTransactions = dataSnapshot.getValue(StocksTransactions.class);
                    stockHistoryArray.add(stocksTransactions);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /*//
        //RECYCLER VIEW FIREBASE
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users").child(MainActivity.UID).child("StockTotal");
        DatabaseReference cryptoListRef = myRef.child("StockList");
        DatabaseReference cryptoCodeRef = cryptoListRef.child(CODE);
        DatabaseReference cryptoTransactionRef = cryptoCodeRef.child("Transactions");

        cryptoTransactionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    StocksTransactions transactionItem = dataSnapshot.getValue(StocksTransactions.class);
                    stockHistoryArray.add(transactionItem);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
    }
}