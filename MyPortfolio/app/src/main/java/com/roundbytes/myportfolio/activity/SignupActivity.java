package com.roundbytes.myportfolio.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.roundbytes.myportfolio.MainActivity;
import com.roundbytes.myportfolio.crypto.CryptoTotal;
import com.roundbytes.myportfolio.DAOUser;
import com.roundbytes.myportfolio.R;
import com.roundbytes.myportfolio.stock.StockTotal;
import com.roundbytes.myportfolio.User;

public class SignupActivity extends AppCompatActivity {

    EditText editTextEmail, editTextName, editTextPassword, editTextConfirmPassword;
    Button btnConfirm;

    private FirebaseAuth mAuth;
    private String UID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.txtEditEmail);
        editTextName = findViewById(R.id.txtEditName);
        editTextPassword = findViewById(R.id.txtEditPassword);
        editTextConfirmPassword = findViewById(R.id.txtEditConfirmPassword);


        btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tempName = editTextName.getText().toString().trim();
                String tempEmail = editTextEmail.getText().toString().trim();
                String tempPassword = editTextPassword.getText().toString().trim();
                String tempConfirmPassword = editTextConfirmPassword.getText().toString().trim();

                //DATA VALIDATION
                if(tempName.isEmpty()){
                    editTextName.setError("Full name is required!");
                    editTextName.requestFocus();
                    return;
                }
                if(tempEmail.isEmpty()){
                    editTextEmail.setError("Email is required!");
                    editTextEmail.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(tempEmail).matches()){
                    editTextEmail.setError("Please provide valid email!");
                    editTextEmail.requestFocus();
                    return;
                }
                if(tempPassword.isEmpty()){
                    editTextPassword.setError("Password is required!");
                    editTextPassword.requestFocus();
                    return;
                }
                if(tempPassword.length()<6){
                    editTextPassword.setError("Min password length should be 6 characters!");
                    editTextPassword.requestFocus();
                    return;
                }
                if(!tempConfirmPassword.equals(tempPassword)) {
                    editTextConfirmPassword.setError("Password does not match!");
                    editTextConfirmPassword.requestFocus();
                    return;
                }


                mAuth.createUserWithEmailAndPassword(tempEmail,tempPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){

                                    //INITIALIZE THE USER CLASS
                                    User user = new User(tempName,tempEmail,tempPassword);

                                    UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    
                                    //DATABASE INITIALIZATION
                                    //CREATE NEW USER CLASS IN DATABASE (NAME)------------------
                                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users");
                                    DatabaseReference usersRef = myRef.child(UID);
                                    //PUSH USER A_VALUE(PERSONAL INFO) TO DATABASE--------------
                                    usersRef.child("A_Info").setValue(user);
                                    //INITIALIZE CRYPTOTOTAL AND STOCKTOTAL
                                    CryptoTotal cryptoTotal = new CryptoTotal();
                                    StockTotal stockTotal = new StockTotal();
                                    //PUSH CRYPTOTOTAL AND STOCKTOTAL TO DATABASE---------------
                                    usersRef.child("CryptoTotal").setValue(cryptoTotal);
                                    usersRef.child("StockTotal").setValue(stockTotal);


                                    //intent
                                    Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                                    intent1.putExtra("refresh", "stock");
                                    MainActivity.UID = UID;
                                    startActivity(intent1);
                                }
                                else{
                                    Toast.makeText(SignupActivity.this, "Failed to register! Please try again!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}