package com.roundbytes.myportfolio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roundbytes.myportfolio.adapter.CryptoHistoryRecViewAdapter;
import com.roundbytes.myportfolio.crypto.CryptoItem;
import com.roundbytes.myportfolio.crypto.CryptosTransactions;

import java.util.ArrayList;

public class CryptoHistoryActivity extends AppCompatActivity {

    private RecyclerView cryptoHistoryRecView;
    private CryptoHistoryRecViewAdapter adapter;
    private TextView editTotalBuyValue,  editTotalCurrentValue;
    private ImageView backBtn;

    //FIREBASE REALTIME DATABASE VARIABLE
    public FirebaseDatabase database;
    public DatabaseReference myRef;
    private String username = MainActivity.username;

    private String CODE;


    public ArrayList<CryptosTransactions> cryptoHistoryArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crypto_history);

        //INTENT EXTRAS
        Intent intent = getIntent();
        CODE = intent.getStringExtra("CODE");
        //Log.d("TAG", "onCreate: "+CODE);

        //VIEW INITIALIZATION
        cryptoHistoryRecView = findViewById(R.id.cryptoHistoryRecView);
        backBtn = findViewById(R.id.btnBack);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                Bundle extras = new Bundle();
                extras.putString("username",username);
                extras.putString("refresh","Crypto");
                intent1.putExtras(extras);
                startActivity(intent1);
            }
        });

        InitializeTransactions();

        adapter = new CryptoHistoryRecViewAdapter(getApplicationContext());
        cryptoHistoryRecView.setAdapter(adapter);
        cryptoHistoryRecView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));



        adapter.setCryptoHistory(cryptoHistoryArray);




    }

    private void InitializeTransactions()
    {
        //RECYCLER VIEW FIREBASE
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users").child(username).child("CryptoTotal");
        DatabaseReference cryptoListRef = myRef.child("CryptoList");
        DatabaseReference cryptoCodeRef = cryptoListRef.child(CODE);
        DatabaseReference cryptoTransactionRef = cryptoCodeRef.child("Transactions");

        cryptoTransactionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    CryptosTransactions transactionItem = dataSnapshot.getValue(CryptosTransactions.class);
                    cryptoHistoryArray.add(transactionItem);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}