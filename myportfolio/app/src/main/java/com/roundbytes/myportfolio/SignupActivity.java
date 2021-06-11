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

        DAOUser dao = new DAOUser();


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

                //DATABASE INITIALIZATION
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                //INITIALIZE THE USER CLASS
                User user = new User(tempName,tempEmail,tempPassword);
                //CREATE NEW USER CLASS IN DATABASE (NAME)------------------
                DatabaseReference myRef = database.getReference(tempName);
                //PUSH USER A_VALUE(PERSONAL INFO) TO DATABASE--------------
                myRef.child("A_Info").setValue(user);
                //INITIALIZE CRYPTOTOTAL AND STOCKTOTAL
                CryptoTotal cryptoTotal = new CryptoTotal();
                StockTotal stockTotal = new StockTotal();
                //PUSH CRYPTOTOTAL AND STOCKTOTAL TO DATABASE---------------
                myRef.child("CryptoTotal").setValue(cryptoTotal);
                myRef.child("StockTotal").setValue(stockTotal);

                //----------INTENT----------------
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("username",tempName);
                startActivity(intent);
            }
        });
    }
}