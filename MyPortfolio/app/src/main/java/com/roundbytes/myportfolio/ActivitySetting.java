package com.roundbytes.myportfolio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ActivitySetting extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Spinner spinner;

    //FIREBASE REALTIME DATABASE VARIABLE
    public FirebaseDatabase database;
    public DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.currency, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        String selectedCurrency = adapterView.getItemAtPosition(position).toString();
        MainActivity.CURRENCY = selectedCurrency;

        //CALL UPDATE FIREBASE CURRENCY VALUE FUN
        updateFirebaseCurrency(selectedCurrency);
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    //UPDATE FIREBASE CURRENCY VALUE
    public void updateFirebaseCurrency(String selectedCurrency){
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users").child(MainActivity.UID).child("A_Info");
        DatabaseReference currencyRef = myRef.child("currency");
        currencyRef.setValue(selectedCurrency);
        Toast.makeText(getApplicationContext(), "Currency has been set to "+selectedCurrency, Toast.LENGTH_SHORT).show();
    }
}