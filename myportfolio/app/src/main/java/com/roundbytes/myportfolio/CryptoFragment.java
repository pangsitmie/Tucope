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


    //popup
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText itemName;
    private Button saveBtn;

    public String addItemName;
    //FIREBASE REALTIME DATABASE VARIABLE
    public FirebaseDatabase database;
    public DatabaseReference myRef;
    private String username;


    public ArrayList<CryptoItem> cryptoArray = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crypto, container , false);

        //VIEW INITIALIZATION
        viewInitialization(v);
        //TOTAL BUY VALUE INITIALIZATION
        totalBuyValueInitialization();
        
        //TOTAL CURRENT VALUE INITIALIZATION
        // TODO: 6/12/2021 ambil api trs cocokno dengan total buy value

        //RECYCLER VIEW FIREBASE
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users").child(username).child("CryptoTotal");
        DatabaseReference cryptoListRef = myRef.child("CryptoList");
        //cryptosRecView.setHasFixedSize(true);
        cryptoListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Log.d("RECYCLER VIEW CALL", dataSnapshot.getValue().toString());
                    //CryptoItem cryptoItem = new CryptoItem(dataSnapshot.getValue().toString());
                    CryptoItem cryptoItem = dataSnapshot.getValue(CryptoItem.class);
                    //Log.d("CRYPTO CODE", cryptoItem.cryptoCode);
                    cryptoArray.add(cryptoItem);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //cryptoArray.add(new CryptoItem(addItemName));

        //SET RECYCLERVIEW ADAPTER
        adapter = new CryptoRecViewAdapter(getActivity());
        cryptosRecView.setAdapter(adapter);
        cryptosRecView.setLayoutManager(new LinearLayoutManager(getActivity()));


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewCryptoItemDialog();

            }
        });
        adapter.setCryptos(cryptoArray);

        return v;
    }

    //VOID METHODS -----------------------------------------------------------
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
                //setItemName(itemName.getText().toString());
                //add the crypto code to database
                CryptoItem cryptoItem = new CryptoItem(itemName.getText().toString());
                database = FirebaseDatabase.getInstance();
                myRef = database.getReference("Users").child(username).child("CryptoTotal").child("CryptoList");
                myRef.child(itemName.getText().toString()).setValue(cryptoItem);
                Toast.makeText(getContext(), itemName.getText().toString()+" aded", Toast.LENGTH_SHORT).show();

            }
        });
    }
/*    private void setItemName(String str)
    {
        addItemName = str;

        //cryptoArray.add(new CryptoItem(addItemName));

        Toast.makeText(getContext(), "Add btn clicked", Toast.LENGTH_SHORT).show();
        adapter.setCryptos(cryptoArray);
    }*/
    private void viewInitialization(View v)
    {
        cryptoDetails = v.findViewById(R.id.cryptoDetailsCard);
        editTotalBuyValue = v.findViewById(R.id.editTotalBuyValue);
        editTotalCurrentValue = v.findViewById(R.id.editTotalCurrentValue);
        addBtn = v.findViewById(R.id.btnAdd);
        deleteBtn = v.findViewById(R.id.btnDelete);
        cryptosRecView = v.findViewById(R.id.cryptoRecView);
    }
    private void totalBuyValueInitialization()
    {
        //FIREBASE REALTIME DATABASE INITIALIZATION
        username = MainActivity.username;
        Log.d("Username: ",username);

        database = FirebaseDatabase.getInstance();//ROOT NODE
        myRef = database.getReference("Users");//USERS NODE

        DatabaseReference nameRef = myRef.child(username);//JERIEL NODE
        DatabaseReference cryptoTotalRef = nameRef.child("CryptoTotal");//CryptoTotal Node
        DatabaseReference totalBuyValueRef = cryptoTotalRef.child("totalCryptoBuyValue");//totalCryptoBuyValue

        //UPDATE TOTAL BUYVALUE FROM FIREBASE
        totalBuyValueRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String val = snapshot.getValue().toString();
                Log.d(TAG,val);
                editTotalBuyValue.setText(val);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: firebase fail");
            }
        });
    }

}
