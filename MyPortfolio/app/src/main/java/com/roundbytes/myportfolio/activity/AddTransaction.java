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
import com.roundbytes.myportfolio.crypto.CryptosTransactions;
import com.roundbytes.myportfolio.fragment.CryptoFragment;

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


    //DB VARIABLES
    public double totalBuyVal;
    public long maxid=0;
    public double amountDB;
    public double subTotalBuyValue;

    //DATABASE REFERENCE
    DatabaseReference cryptoStockTotalRef;
    DatabaseReference listRef;
    DatabaseReference transactionRef;
    DatabaseReference codeRef;
    DatabaseReference amountRef;
    DatabaseReference totalBuyValRef;
    DatabaseReference subTotalBuyValueRef;

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

        //TOTAL CRYPTO BUY VALUE INITIALIZTION
        database = FirebaseDatabase.getInstance();
        DatabaseReference root = database.getReference("Users").child(username);




        //DATABASE VARIABLE REFERENCING AND VARIBALE INITIALIZATION
        if(TYPE.equals("stock")){//kalau stocks
            //database = FirebaseDatabase.getInstance();
            myRef = database.getReference("Users").child(username).child("StockTotal").child("StockList").child(CODE);
        }
        else
        {//kalau crypto
            cryptoStockTotalRef = root.child("CryptoTotal");//CryptoTotal
            //INITIALIZE CRYPTO TOTAL BUY VALUE
            totalBuyValRef = cryptoStockTotalRef.child("totalCryptoBuyValue");//totalCryptoBuyValue
            totalBuyValRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    totalBuyVal = snapshot.getValue(Double.class);
                    Log.d("FIREBASECALL", "TOTAL BUY VALUE INIT: " +totalBuyVal);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            //INITIALIZE TRANSACTION MAX ID
            listRef = cryptoStockTotalRef.child("CryptoList");
            codeRef = listRef.child(CODE);

            //----------SUB TOTAL BUY VALUE INIT---------------
            subTotalBuyValueRef = codeRef.child("cryptoSubTotalBuyValue");
            subTotalBuyValueRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    subTotalBuyValue = snapshot.getValue(Double.class);
                    Log.d("FIREBASECALL", "SUBTOTAL BUY VALUE INIT: " +totalBuyVal);

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            //----------TRANSACTION MAX ID INIT---------------
            transactionRef = codeRef.child("Transactions");
            transactionRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists())
                    {
                        maxid = (snapshot.getChildrenCount());
                        Log.d("MAXID", "MAXID: "+maxid);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });

            //----------TOTAL AMOUNT INIT---------------
            amountRef = codeRef.child("amount");
            amountRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    amountDB = snapshot.getValue(Double.class);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });

            Log.d("INITIALIZATION", totalBuyVal + " " + maxid+ " " + amountDB + " " +subTotalBuyValue);
        }



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

                }else{//kalau crypto
                    //NEW TRANSACTION CLASS
                    CryptosTransactions cryptosTransactions = new CryptosTransactions(price,amount,date,type,fee,valueBeforeFee,valueAfterFee,0);

                    //NEW TOTAL BUY VALUE
                    double newTotalBuyValue = totalBuyVal+(price*amount);
                    totalBuyValRef.setValue(newTotalBuyValue);

                    //INSERT NEW TRANSACTION
                    transactionRef.child(String.valueOf(maxid+1)).setValue(cryptosTransactions);//add new transaction with maxid + 1 as autoincrement
                    //--------------------ADD AMOUNT--------------------
                    amountRef.setValue(amount+amountDB);
                    //--------------------ADD SUB TOTAL BUY VALUE--------------------
                    subTotalBuyValueRef.setValue(subTotalBuyValue+(price*amount));
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