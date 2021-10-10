package com.roundbytes.myportfolio.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
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
import com.roundbytes.myportfolio.R;
import com.roundbytes.myportfolio.stock.StockTotal;
import com.roundbytes.myportfolio.User;

import java.util.Calendar;

public class SignupActivity extends AppCompatActivity {

    TextView dateOfBirthText,singInText;
    EditText editTextEmail, editTextName, editTextPassword, editTextConfirmPassword;
    Button btnConfirm;
    String date, uploadDateOfBirth;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

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
        dateOfBirthText = findViewById(R.id.txtEditDateOfBirth);
        singInText=findViewById(R.id.singInText);

        dateOfBirthText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datepickerdialog = new DatePickerDialog(SignupActivity.this,
                        AlertDialog.THEME_DEVICE_DEFAULT_DARK,mDateSetListener,year,month,day);

                //SET THE DIALOG VIEW TO 90%
                int width = (int)(getResources().getDisplayMetrics().widthPixels*0.85);
                int height = WindowManager.LayoutParams.WRAP_CONTENT;
                datepickerdialog.getWindow().setLayout(width,height);

                datepickerdialog.show();
                datepickerdialog.getWindow().setLayout(width,height);
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener(){
            @Override
            //january is 0 december = 11
            public void onDateSet(DatePicker datePicker, int year, int month , int day){
                month = month+1;
                date = day + "/" + month +"/" + year;
                Log.d("DATE CALL", "onDateSet: mm/dd/yyyy" + date);
                dateOfBirthText.setText(date);
                uploadDateOfBirth = date;
            }
        };

        btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tempName = editTextName.getText().toString().trim();
                String tempEmail = editTextEmail.getText().toString().trim();
                String tempDate= dateOfBirthText.getText().toString();
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
                if(tempDate.equals("")){
                    uploadDateOfBirth = "None";
                }

                mAuth.createUserWithEmailAndPassword(tempEmail,tempPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){

                                    //INITIALIZE THE USER CLASS
                                    User user = new User(tempName,tempEmail,uploadDateOfBirth,"USD");

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
                                    intent1.putExtra("refresh", "crypto");
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

        singInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });
    }

}