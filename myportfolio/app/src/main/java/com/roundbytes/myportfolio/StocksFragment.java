package com.roundbytes.myportfolio;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StocksFragment extends Fragment {
    @Nullable

    private static final String TAG = "FIREBASECALL";
    private RecyclerView stocksRecView;
    private StocksRecViewAdapter adapter;
    Button addBtn;
    TextView editTotalBuyValue,editTotalCurrentValue;

    //FIREBASE REALTIME DATABASE VARIABLE
    public FirebaseDatabase database;
    public DatabaseReference myRef;
    private String username;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_stocks, container , false);

        viewInitialization(v);

        totalBuyValueInitialization();


        adapter = new StocksRecViewAdapter(getActivity());
        stocksRecView.setAdapter(adapter);
        stocksRecView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ArrayList<StocksList> stocks = new ArrayList<>();
        stocks.add(new StocksList("ADRO",55, 1188, 6537445));
        stocks.add(new StocksList("ANTM",10, 1151, 1151970));
        stocks.add(new StocksList("ADRO",19, 5759, 10942500));



        adapter.setStocks(stocks);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //writeNewUser("01","Jeriel", "jerielisaiahksa@gmail.com")

                Toast.makeText(getActivity(), "singin button is clicked", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }
    private void viewInitialization(View v)
    {
        addBtn = v.findViewById(R.id.btnAdd);
        stocksRecView = v.findViewById(R.id.stocksRecView);
        editTotalBuyValue = v.findViewById(R.id.editTotalBuyValue);
        editTotalCurrentValue = v.findViewById(R.id.editTotalCurrentValue);
    }

    private void totalBuyValueInitialization()
    {
        //FIREBASE REALTIME DATABASE INITIALIZATION
        username = MainActivity.username;
        Log.d("Username: ",username);

        database = FirebaseDatabase.getInstance();//ROOT NODE
        myRef = database.getReference("Users");
        DatabaseReference nameRef = myRef.child(username);//JERIEL NODE

        DatabaseReference cryptoTotalRef = nameRef.child("StockTotal");//CryptoTotal Node
        DatabaseReference totalBuyValueRef = cryptoTotalRef.child("totalStockBuyValue");//totalCryptoBuyValue

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
