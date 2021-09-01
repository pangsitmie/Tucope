package com.roundbytes.myportfolio.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roundbytes.myportfolio.crypto.AddNewCryptoActivity;
import com.roundbytes.myportfolio.crypto.CryptoItem;
import com.roundbytes.myportfolio.adapter.CryptoRecViewAdapter;
import com.roundbytes.myportfolio.R;
import com.roundbytes.myportfolio.MainActivity;
import com.roundbytes.myportfolio.crypto.CryptoModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CryptoFragment extends Fragment {
    @Nullable

    private static final String TAG = "FIREBASECALL";

    private RecyclerView cryptosRecView;
    private CryptoRecViewAdapter adapter;
    private CardView cryptoDetails;
    private TextView editTotalBuyValue,  editTotalCurrentValue;
    private Button addBtn;

    //FIREBASE REALTIME DATABASE VARIABLE
    public FirebaseDatabase database;
    public DatabaseReference myRef;
    private String UID = MainActivity.UID;

    //ARRAY LIST
    public ArrayList<CryptoItem> cryptoArray = new ArrayList<>();
    //public static ArrayList<CryptoModel> cryptoModelsArrayList = new ArrayList<>();



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crypto, container , false);

        //VIEW INITIALIZATION
        viewInitialization(v);

        //REQUEST CURRENCY DATA API
        //getCurrencyData();

        //REFRESH RECYCLER VIEW
        refreshCryptoRecView();

        //CRYPTO TOP CARD INITIALIZATION
        CryptoTopCardInitialization();

        //ADD NEW CRYPTO BUTTON LISTENER
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddNewCryptoActivity.class);
                startActivity(intent);
            }
        });

        //SET RECYCLERVIEW ADAPTER
        adapter = new CryptoRecViewAdapter(getActivity());
        cryptosRecView.setAdapter(adapter);
        cryptosRecView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.setCryptos(cryptoArray);


        return v;
    }

    //VOID METHODS -----------------------------------------------------------
    private void viewInitialization(View v)
    {
        cryptoDetails = v.findViewById(R.id.cryptoDetailsCard);
        editTotalBuyValue = v.findViewById(R.id.editTotalBuyValue);
        editTotalCurrentValue = v.findViewById(R.id.editTotalCurrentValue);
        addBtn = v.findViewById(R.id.btnAdd);
        cryptosRecView = v.findViewById(R.id.cryptoRecView);
    }
    private void CryptoTopCardInitialization()
    {
        //FIREBASE REFERENCE
        database = FirebaseDatabase.getInstance();//ROOT NODE
        myRef = database.getReference("Users");//USERS NODE

        DatabaseReference nameRef = myRef.child(String.valueOf(MainActivity.UID));//JERIEL NODE
        DatabaseReference cryptoTotalRef = nameRef.child("CryptoTotal");//CryptoTotal Node
        DatabaseReference totalCryptoBuyValue = cryptoTotalRef.child("totalCryptoBuyValue");//totalCryptoBuyValue
        DatabaseReference totalCryptoCurrentValue = cryptoTotalRef.child("totalCryptoCurrentValue");//totalCryptoBuyValue


        //SET TEXT TOTAL BUY VALUE FROM FIREBASE
        totalCryptoBuyValue.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                double val = snapshot.getValue(Double.class);

                NumberFormat nf = NumberFormat.getInstance(Locale.US);
                nf.setMinimumFractionDigits(2); // <- the trick is here
                String formatVal = nf.format(val); // <- 1,000.00
                editTotalBuyValue.setText(formatVal);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        //SET TEXT TOTAL CURRENT VALUE FROM FIREBASE (BEFORE UPDATED WITH NEW MARKET PRICE)
        totalCryptoCurrentValue.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double val = snapshot.getValue(Double.class);

                NumberFormat nf = NumberFormat.getInstance(Locale.US);
                nf.setMinimumFractionDigits(2); // <- the trick is here
                String formatVal = nf.format(val); // <- 1,000.00
                editTotalCurrentValue.setText(formatVal);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //WAIT FOR ... SEC TO UPDATE CURRENT VALUE
        final Handler handler1 = new Handler(Looper.getMainLooper());
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                //INITIATE CRYPTO TOP CARD
                double updateNewCurrentValue = updateCryptoTotalValue();
                totalCryptoCurrentValue.setValue(updateNewCurrentValue);

                //TEXT FORMATTING AND SET TEXT AGAIN TO UPDATE
                NumberFormat nf = NumberFormat.getInstance(Locale.US);
                nf.setMinimumFractionDigits(2); // <- the trick is here
                String formatVal = nf.format(updateNewCurrentValue); // <- 1,000.00
                editTotalCurrentValue.setText(formatVal);

            }
        }, 3000);



    }
    private double updateCryptoTotalValue(){
        //UPDATE THE CURRENT VALUE BASED ON MARKET PRICE
        double newCurrentValue = 0.0;
        for (int i=0;i<cryptoArray.size();i++){

            double amount = cryptoArray.get(i).getAmount();
            Log.d(TAG, "AMOUNT: "+amount);

            for(int j=0;j<MainActivity.cryptoModelsArrayList.size();j++)
            {
                if(cryptoArray.get(i).getCryptoCode().equalsIgnoreCase(MainActivity.cryptoModelsArrayList.get(j).getSymbol())){
                    newCurrentValue += amount*MainActivity.cryptoModelsArrayList.get(j).getPrice();
                    break;
                }
            }
        }
        return newCurrentValue;
    }
    private void refreshCryptoRecView(){
        //RECYCLER VIEW FIREBASE
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users").child(UID).child("CryptoTotal");
        DatabaseReference cryptoListRef = myRef.child("CryptoList");
        cryptoListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        CryptoItem cryptoItem = dataSnapshot.getValue(CryptoItem.class);
                        cryptoArray.add(cryptoItem);
                    }
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




}
