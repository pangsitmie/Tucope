package com.roundbytes.myportfolio;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.roundbytes.myportfolio.crypto.CryptoItem;

import java.util.Calendar;

public class AddTransaction extends AppCompatActivity {
    TextView editDate;
    EditText editPrice, editAmount;
    Button confirmBtn;
    String date;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    //FIREBASE VARIABLES
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private String username = MainActivity.username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        username = MainActivity.username;

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

                String all = price +" "+amount +" "+date;
                Log.d("TAG", "onClick: "+all);

 /*               database = FirebaseDatabase.getInstance();
                myRef = database.getReference("Users").child(username).child("CryptoTotal").child("CryptoList").child(CODE);
                myRef.child("amount").setValue(amount);
                myRef.child("Transactions").setValue(price);*/

                if(TYPE.equals("stock")){//kalau stocks
                    database = FirebaseDatabase.getInstance();
                    myRef = database.getReference("Users").child(username).child("StockTotal").child("StockList").child(CODE);
                    myRef.child("lot").setValue(amount);
                    myRef.child("Transactions").setValue(price);
                }else{//kalau crypto
                    database = FirebaseDatabase.getInstance();
                    myRef = database.getReference("Users").child(username).child("CryptoTotal").child("CryptoList").child(CODE);
                    myRef.child("amount").setValue(amount);
                    myRef.child("Transactions").setValue(price);
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