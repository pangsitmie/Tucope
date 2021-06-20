package com.roundbytes.myportfolio.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roundbytes.myportfolio.MainActivity;
import com.roundbytes.myportfolio.R;
import com.roundbytes.myportfolio.crypto.CryptoItem;
import com.roundbytes.myportfolio.crypto.CryptosTransactions;

import java.util.Calendar;

public class AddTransaction extends AppCompatActivity {
    TextView editDate;
    EditText editPrice, editAmount, editFee;
    Button confirmBtn;
    String date;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    //FIREBASE VARIABLES
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private String username = MainActivity.username;
    public double totalBuyVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        username = MainActivity.username;

        double total;

        //INTENT EXTRAS
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String TYPE = extras.getString("TYPE");
        String CODE = extras.getString("CODE");
        Log.d("TYPE", TYPE + " " + CODE);
        //INIT VIEW
        editPrice = findViewById(R.id.editPrice);
        editAmount = findViewById(R.id.editAmount);
        confirmBtn = findViewById(R.id.btnConfirm);
        editDate = findViewById(R.id.editDate);
        editFee = findViewById(R.id.editFee);

        //TOTAL BUY VALUE INITIALIZTION

        database = FirebaseDatabase.getInstance();
        DatabaseReference cryptoTotalRef = database.getReference("Users").child(username).child("CryptoTotal");
        DatabaseReference totalCryptoBuyValueRef = cryptoTotalRef.child("totalCryptoBuyValue");
        totalCryptoBuyValueRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                totalBuyVal = snapshot.getValue(Double.class);
                Log.d("TAG", "TOTAL BUY VALUE INIT: " +totalBuyVal);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        //BUTTON ON CLICK
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddTransaction.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double price =  Double.parseDouble(editPrice.getText().toString());
                double amount =  Double.parseDouble(editAmount.getText().toString());
                double fee = Double.parseDouble(editFee.getText().toString())/100;//dalam percent
                String type ="Buy";
                double valueBeforeFee = price*amount;
                double valueAfterFee = (1-fee)*valueBeforeFee;

                String all = price +" "+amount +" "+date +" "+fee;
                Log.d("TAG", "onClick: "+all);


                if(TYPE.equals("stock")){//kalau stocks
                    database = FirebaseDatabase.getInstance();
                    myRef = database.getReference("Users").child(username).child("StockTotal").child("StockList").child(CODE);
                    myRef.child("lot").setValue(amount);
                    myRef.child("Transactions").setValue(price);
                }else{//kalau crypto
                    CryptosTransactions cryptosTransactions = new CryptosTransactions(price,amount,date,type,fee,valueBeforeFee,valueAfterFee,0);
                    database = FirebaseDatabase.getInstance();
                    DatabaseReference cryptoTotalRef = database.getReference("Users").child(username).child("CryptoTotal");
                    //SET NEW CRYPTO TOTAL BUY VALUE
                    double newTotalBuyValue = totalBuyVal+(price*amount);
                    Log.d("TAG", "newTotalBuyValue before insert: "+newTotalBuyValue);
                    cryptoTotalRef.child("totalCryptoBuyValue").setValue(newTotalBuyValue);
                    Log.d("TAG", "newTotalBuyValue after insert: "+newTotalBuyValue);
                    myRef = cryptoTotalRef.child("CryptoList").child(CODE);
                    myRef.child("amount").setValue(amount);
                    myRef.child("Transactions").child("1").setValue(cryptosTransactions);
                }


            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener(){
            @Override
            //january is 0 december = 11
            public void onDateSet(DatePicker datePicker, int year, int month , int day){
                month = month+1;
                date = day + "/" + month +"/" + year;
                Log.d("DATE CALL", "onDateSet: mm/dd/yyyy" + date);
                editDate.setText(date);
            }
        };
    }
}