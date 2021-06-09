package com.roundbytes.myportfolio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    EditText editTextEmail, editTextName, editTextPassword, editTextConfirmPassword;
    Button btnConfirm;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        editTextEmail = findViewById(R.id.txtEditEmail);
        editTextName = findViewById(R.id.txtEditName);
        editTextPassword = findViewById(R.id.txtEditPassword);
        editTextConfirmPassword = findViewById(R.id.txtEditConfirmPassword);


        btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tempName = editTextName.getText().toString();
                String tempEmail = editTextEmail.getText().toString();
                String tempPassword = editTextPassword.getText().toString();

                //initialize new user
                User user = new User(tempName,tempEmail,tempPassword);

                //initial StockList and CryptoList
                //StocksList stocksList = new StocksList();
                CryptoList cryptoList = new CryptoList();

                //Initialize CryptoTotal
                CryptoTotal cryptoTotal = new CryptoTotal();





                //push new database to firebase
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference(tempName);
                myRef.child("A_Info").setValue(user);
                //myRef.child("StocksList").setValue(stocksList);
                myRef.child("CryptoTotal").setValue(cryptoTotal);
                myRef.child("CryptoTotal").child("CryptoList").setValue(cryptoTotal);


                //--------------------------
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}