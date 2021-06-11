package com.roundbytes.myportfolio;

import android.app.AlertDialog;
import android.icu.text.CaseMap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CryptoFragment extends Fragment {
    @Nullable

    private static final String TAG = "FIREBASECALL";

    private RecyclerView cryptosRecView;
    private CryptoRecViewAdapter adapter;
    private CardView cryptoDetails;
    private TextView editTotalBuyValue,  editTotalCurrentValue;
    private Button addBtn, deleteBtn;
    private String username;

    //popup
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText itemName;
    private Button saveBtn;

    public String addItemName;
    //FIREBASE REALTIME DATABASE VARIABLE
    public FirebaseDatabase database;
    public DatabaseReference myRef;


    public ArrayList<CryptoItem> cryptoArray = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crypto, container , false);
        viewInitialization(v);

        //SET RECYCLERVIEW ADAPTER
        adapter = new CryptoRecViewAdapter(getActivity());
        cryptosRecView.setAdapter(adapter);
        cryptosRecView.setLayoutManager(new LinearLayoutManager(getActivity()));


        //FIREBASE REALTIME DATABASE INITIALIZATION
        username = MainActivity.username;
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(username);


        //CryptoTotal Initialization
        //CryptoTotal cryptoTotal = new CryptoTotal();

        DatabaseReference jerielRef = FirebaseDatabase.getInstance().getReference("Jeriel");
        DatabaseReference cryptoTotalRef = jerielRef.child("CryptoTotal");
        DatabaseReference totalBuyValueRef = cryptoTotalRef.child("totalBuyValue");

        totalBuyValueRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String val = snapshot.getValue().toString();
                Log.d(TAG,val);
                editTotalBuyValue.setText(val);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewCryptoItemDialog();

            }
        });
        adapter.setCryptos(cryptoArray);

        return v;
    }

    private void createNewCryptoItemDialog()
    {
        dialogBuilder = new AlertDialog.Builder(getActivity());
        final View itemPopupView = getLayoutInflater().inflate(R.layout.popup,null);
        itemName = (EditText) itemPopupView.findViewById(R.id.editItemName);
        saveBtn = (Button) itemPopupView.findViewById(R.id.saveBtn);

        dialogBuilder.setView(itemPopupView);
        dialog = dialogBuilder.create();
        dialog.show();


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                setItemName(itemName.getText().toString());
                //add the crypto code to database
                CryptoItem cryptoItem = new CryptoItem(itemName.getText().toString());
                myRef.child("CryptoTotal").child("CryptoList").child(itemName.getText().toString()).setValue(cryptoItem);
                Toast.makeText(getContext(), itemName.getText().toString()+" clicked", Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void setItemName(String str)
    {
        addItemName = str;

        cryptoArray.add(new CryptoItem(addItemName));

        Toast.makeText(getContext(), "Add btn clicked", Toast.LENGTH_SHORT).show();
        adapter.setCryptos(cryptoArray);
    }
    private void viewInitialization(View v)
    {
        cryptoDetails = v.findViewById(R.id.cryptoDetailsCard);
        editTotalBuyValue = v.findViewById(R.id.editTotalBuyValue);
        editTotalCurrentValue = v.findViewById(R.id.editTotalCurrentValue);
        addBtn = v.findViewById(R.id.btnAdd);
        deleteBtn = v.findViewById(R.id.btnDelete);
        cryptosRecView = v.findViewById(R.id.cryptoRecView);
    }

}
