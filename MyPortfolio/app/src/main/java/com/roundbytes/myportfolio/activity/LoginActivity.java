package com.roundbytes.myportfolio.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roundbytes.myportfolio.MainActivity;
import com.roundbytes.myportfolio.R;

public class LoginActivity extends AppCompatActivity {
    EditText editEmail, editPassword;
    Button btnConfirm;

    //FIREBASE VARIABLE
    public FirebaseDatabase database;
    public DatabaseReference myRef;

    private FirebaseAuth mAuth;
    private String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        editEmail = findViewById(R.id.txtEditEmail);
        editPassword = findViewById(R.id.txtEditPassword);
        btnConfirm = findViewById(R.id.btnConfirm);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO: 6/17/2021 ini function check email with database ini ada bug yang menyebabkan kalau add new item refersh dan masuk login lagi
                String tempEmail = editEmail.getText().toString();
                String tempPassword = editPassword.getText().toString();

                mAuth.signInWithEmailAndPassword(tempEmail,tempPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            //intent
                            Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                            MainActivity.UID = UID;
                            intent1.putExtra("refresh","stock");
                            startActivity(intent1);
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, "Invalid email or password!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    private void checkEmailWithDatabase(String email, String password)
    {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");
        Log.d("TAG Email:", email);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Log.d("TAG Data", dataSnapshot.toString());

                    String databaseEmail = dataSnapshot.child("A_Info").child("email").getValue().toString();
                    String databasePassword = dataSnapshot.child("A_Info").child("password").getValue().toString();
                    String databaseKey = dataSnapshot.getKey();

                    Log.d("TAG Data", databaseEmail+" "+databaseKey+" "+databasePassword);

                    if(email.equalsIgnoreCase(databaseEmail) && password.equals(databasePassword)){
                        UID = databaseKey;
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("username", UID);
                        startActivity(intent);
                        break;
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}