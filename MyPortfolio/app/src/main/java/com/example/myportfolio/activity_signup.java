package com.example.myportfolio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class activity_signup extends AppCompatActivity {

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





/*                //FIREBASE DAO SCRIPT
                DAOUser daoUser = new DAOUser();
                //INITIAL
                User user = new User(editTextName.getText().toString(),editTextEmail.getText().toString(),editTextPassword.getText().toString());
                //ADD TO FIREBASE
                daoUser.add(user).addOnSuccessListener(suc ->
                {
                    Toast.makeText(activity_signup.this, "crypto record is inserted", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(er ->{
                    Toast.makeText(activity_signup.this, "ERROR", Toast.LENGTH_SHORT).show();
                });*/



                Intent intent = new Intent(activity_signup.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}