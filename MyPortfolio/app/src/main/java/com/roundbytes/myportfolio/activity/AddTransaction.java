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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roundbytes.myportfolio.MainActivity;
import com.roundbytes.myportfolio.R;
import com.roundbytes.myportfolio.crypto.CryptosTransactions;
import com.roundbytes.myportfolio.stock.StocksTransactions;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTransaction extends AppCompatActivity {
    TextView editDate, editSubTotal;
    EditText editPrice, editAmount, editFee;
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button confirmBtn;
    String date;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    //FIREBASE VARIABLES
    private FirebaseDatabase database;
    private DatabaseReference myRef;


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
        editSubTotal = findViewById(R.id.editSubTotal);
        confirmBtn = findViewById(R.id.btnConfirm);
        editDate = findViewById(R.id.editDate);
        editFee = findViewById(R.id.editFee);
        radioGroup = findViewById(R.id.radioGroup);



        //TOTAL CRYPTO BUY VALUE INITIALIZTION
        database = FirebaseDatabase.getInstance();
        DatabaseReference root = database.getReference("Users").child(MainActivity.UID);




        //DATABASE VARIABLE REFERENCING AND VARIBALE INITIALIZATION
        if(TYPE.equalsIgnoreCase("stock")){//kalau stocks
            cryptoStockTotalRef = root.child("StockTotal");//CryptoTotal
            //INITIALIZE CRYPTO TOTAL BUY VALUE
            totalBuyValRef = cryptoStockTotalRef.child("totalStockBuyValue");//totalCryptoBuyValue
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
            listRef = cryptoStockTotalRef.child("StockList");
            codeRef = listRef.child(CODE);

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

            //----------SUB TOTAL BUY VALUE INIT---------------
            subTotalBuyValueRef = codeRef.child("stockSubTotalBuyValue");
            subTotalBuyValueRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    subTotalBuyValue = snapshot.getValue(Double.class);
                    Log.d("FIREBASECALL", "SUBTOTAL BUY VALUE INIT: " +subTotalBuyValue);

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            //----------TOTAL LOT INIT---------------
            amountRef = codeRef.child("lot");
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

            //----------SUB TOTAL BUY VALUE INIT---------------
            subTotalBuyValueRef = codeRef.child("cryptoSubTotalBuyValue");
            subTotalBuyValueRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    subTotalBuyValue = snapshot.getValue(Double.class);
                    Log.d("FIREBASECALL", "SUBTOTAL BUY VALUE INIT: " +subTotalBuyValue);

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
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




        //DATE BUTTON ON CLICK
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //edit subtotal
                double price =  Double.parseDouble(editPrice.getText().toString());
                double amount =  Double.parseDouble(editAmount.getText().toString());
                NumberFormat nf = NumberFormat.getInstance(Locale.US);
                nf.setMinimumFractionDigits(2); // <- the trick is here
                String formatVal;
                if(TYPE.equals("stock"))
                    formatVal = nf.format(price*amount*100); // <- 1,000.00
                else
                    formatVal = nf.format(price*amount); // <- 1,000.00
                editSubTotal.setText(formatVal);


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
                //check editText
                String checkPrice =editPrice.getText().toString().trim();
/*                String checkAmount =editAmount.getText().toString().trim();
                String checkDate =editDate.getText().toString().trim();*/

                if (checkPrice.length()!=0)
                {
                    double price =  Double.parseDouble(editPrice.getText().toString());
                    double amount =  Double.parseDouble(editAmount.getText().toString());
                    double fee = Double.parseDouble(editFee.getText().toString())/100;//dalam percent

                    //radio button
                    int radioId = radioGroup.getCheckedRadioButtonId();
                    radioButton = findViewById(radioId);
                    String type = radioButton.getText().toString();

                    double valueBeforeFee = price*amount;
                    double valueAfterFee = (1-fee)*valueBeforeFee;

                    String all = price +" "+amount +" "+date +" "+fee;
                    Log.d("TAG", "onClick: "+all);

                    //BRANCHING TO STOCK OR CRYPTO
                    if(TYPE.equals("stock"))
                    {
                        //NEW TRANSACTION CLASS
                        StocksTransactions stocksTransactions = new StocksTransactions(price,amount,date,type,fee,valueBeforeFee,valueAfterFee,0,CODE);

                        //INSERT NEW TRANSACTION
                        transactionRef.child(String.valueOf(maxid+1)).setValue(stocksTransactions);//add new transaction with maxid + 1 as autoincrement

                        //BUY OR SELL TYPE
                        if(type.equals("Buy")){
                            totalBuyValRef.setValue(totalBuyVal+(price*amount*100));
                            amountRef.setValue(amountDB+amount);//ADD AMOUNT
                            subTotalBuyValueRef.setValue(subTotalBuyValue+(price*amount*100));
                        }else{
                            totalBuyValRef.setValue(totalBuyVal-(price*amount*100));
                            amountRef.setValue(amountDB-amount);//ADD AMOUNT
                            subTotalBuyValueRef.setValue(subTotalBuyValue-(price*amount*100));//SUBTOTAL BUY VALUE
                        }

                    }
                    else
                    {
                        //NEW TRANSACTION CLASS
                        CryptosTransactions cryptosTransactions = new CryptosTransactions(price,amount,date,type,fee,valueBeforeFee,valueAfterFee,0,CODE);
                        //INSERT NEW TRANSACTION
                        transactionRef.child(String.valueOf(maxid+1)).setValue(cryptosTransactions);//add new transaction with maxid + 1 as autoincrement

                        //BUY OR SELL TYPE
                        if(type.equals("Buy")){
                            totalBuyValRef.setValue(totalBuyVal+(price*amount));
                            amountRef.setValue(amountDB+amount);//ADD AMOUNT
                            subTotalBuyValueRef.setValue(subTotalBuyValue+(price*amount));
                        }else{
                            totalBuyValRef.setValue(totalBuyVal-(price*amount));
                            amountRef.setValue(amountDB-amount);//ADD AMOUNT
                            subTotalBuyValueRef.setValue(subTotalBuyValue-(price*amount));//SUBTOTAL BUY VALUE
                        }
                    }

                    //TOAST AND RETURN TO MAIN ACTIVITY ACCORDING TO FRAGMENT
                    Toast.makeText(getApplicationContext(), "Transaction Added", Toast.LENGTH_SHORT).show();
                    //INTENT
                    Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                    intent1.putExtra("refresh",TYPE);
                    startActivity(intent1);
                }
                else {
                    editPrice.requestFocus();
                    editPrice.setError("Enter The Price");
                    Toast.makeText(getApplicationContext(),"Enter The Price",Toast.LENGTH_SHORT).show();
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
    
    //FUNCTIONS
    public void checkButton(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
    }




}