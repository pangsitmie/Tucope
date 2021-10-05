package com.roundbytes.myportfolio.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.roundbytes.myportfolio.MainActivity;
import com.roundbytes.myportfolio.R;

public class ActivitySecurity extends AppCompatActivity {

    Button btnResetPassword;
    EditText txtEditPassword,txtEditConfirmPassword;

    //CHECK USER LOGIN STATE (FIREBASE)
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);

        //GET CURRENT FIREBASE USER
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        //GET TEXT


        btnResetPassword = findViewById(R.id.btnResetPassword);
        txtEditPassword = findViewById(R.id.txtEditPassword);
        txtEditConfirmPassword = findViewById(R.id.txtEditConfirmPassword);




        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //CROSSCHECK PASSWORD INPUT
                String tempPassword = txtEditPassword.getText().toString().trim();
                String tempConfirmPassword = txtEditConfirmPassword.getText().toString().trim();

                if(tempPassword.length()<6){
                    txtEditPassword.setError("Min password length should be 6 characters!");
                    txtEditPassword.requestFocus();
                    return;
                }
                if(!tempConfirmPassword.equals(tempPassword)) {
                    txtEditConfirmPassword.setError("Password does not match!");
                    txtEditConfirmPassword.requestFocus();
                    return;
                }

                user.updatePassword(tempPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Password Reset Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("refresh","crypto");
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Password Reset Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}