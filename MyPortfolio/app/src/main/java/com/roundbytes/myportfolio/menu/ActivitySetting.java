package com.roundbytes.myportfolio.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roundbytes.myportfolio.MainActivity;
import com.roundbytes.myportfolio.R;

public class ActivitySetting extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Spinner spinner;
    Button btnConfirm;
    String defaultCurrency=MainActivity.CURRENCY;

    //FIREBASE REALTIME DATABASE VARIABLE
    public FirebaseDatabase database;
    public DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        spinner = findViewById(R.id.spinner);
        btnConfirm = findViewById(R.id.btnConfirm);



        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.currency, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        int spinnerDefaultPosition =adapter.getPosition(defaultCurrency);
        //set the default according to value
        spinner.setSelection(spinnerDefaultPosition);

        spinner.setOnItemSelectedListener(this);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //CALL UPDATE CURRENCY IN MAIN ACTIVITY
                MainActivity.CURRENCY = defaultCurrency;

                //CALL UPDATE FIREBASE CURRENCY VALUE FUN
                updateFirebaseCurrency(defaultCurrency);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        String selectedCurrency = adapterView.getItemAtPosition(position).toString();
        defaultCurrency = selectedCurrency;
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