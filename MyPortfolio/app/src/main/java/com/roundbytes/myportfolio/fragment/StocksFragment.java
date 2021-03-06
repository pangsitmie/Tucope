package com.roundbytes.myportfolio.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import com.roundbytes.myportfolio.R;
import com.roundbytes.myportfolio.stock.StockItem;
import com.roundbytes.myportfolio.adapter.StocksRecViewAdapter;
import com.roundbytes.myportfolio.MainActivity;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class StocksFragment extends Fragment {
    @Nullable

    private static final String TAG = "FIREBASECALL";
    private RecyclerView stocksRecView;
    private StocksRecViewAdapter adapter;
    Button addBtn;
    TextView editTotalBuyValue,editTotalCurrentValue;

    //popup
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText itemName;
    private Button saveBtn;

    //FIREBASE REALTIME DATABASE VARIABLE
    public FirebaseDatabase database;
    public DatabaseReference myRef;

    //ARRAYLIST FOR RECYCLER VIEW
    public ArrayList<StockItem> stocksArray = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_stocks, container , false);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Stocks");

        //VIEW INITIALIZATION
        viewInitialization(v);
        //TOTAL BUY VALUE INITIALIZATION
        //stockTopCardInitialization();

        //CURRENT VALUE INITIALIZATION
        // TODO: 6/13/2021 ambil api trs cocokkan dengan total buy valu

        Log.d("STOCKFRAGMENT: "," the uid" +MainActivity.UID);

        //RECYCLER VIEW FIREBASE
        refreshStockRecView();

        //ADD NEW STOCK BUTTON
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewStockItemDialog();
            }
        });

        adapter.setStocks(stocksArray);

        return v;
    }

    //VOID METHODS -----------------------------------------------------------
    private void viewInitialization(View v)
    {
        addBtn = v.findViewById(R.id.btnAdd);
        stocksRecView = v.findViewById(R.id.stocksRecView);
        editTotalBuyValue = v.findViewById(R.id.editTotalBuyValue);
        editTotalCurrentValue = v.findViewById(R.id.editTotalCurrentValue);
    }
    private void createNewStockItemDialog()
    {
        dialogBuilder = new AlertDialog.Builder(getActivity());
        final View itemPopupView = getLayoutInflater().inflate(R.layout.popup,null);
        itemName = (EditText) itemPopupView.findViewById(R.id.editItemName);
        saveBtn = (Button) itemPopupView.findViewById(R.id.saveBtn);

        dialogBuilder.setView(itemPopupView);
        dialog = dialogBuilder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.background_dark);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add the STOCK code to database
                StockItem stockItem = new StockItem(itemName.getText().toString());
                database = FirebaseDatabase.getInstance();
                myRef = database.getReference("Users").child(String.valueOf(MainActivity.UID)).child("StockTotal").child("StockList");
                myRef.child(itemName.getText().toString()).setValue(stockItem);
                stocksArray.clear();
                dialog.cancel();
            }
        });
    }
    private void stockTopCardInitialization()
    {
        //FIREBASE REALTIME DATABASE INITIALIZATION


        database = FirebaseDatabase.getInstance();//ROOT NODE
        myRef = database.getReference("Users");
        DatabaseReference nameRef = myRef.child(String.valueOf(MainActivity.UID));

        DatabaseReference cryptoTotalRef = nameRef.child("StockTotal");//
        DatabaseReference totalBuyValueRef = cryptoTotalRef.child("totalStockBuyValue");//totalCryptoBuyValue
        DatabaseReference totalCurrentValueRef = cryptoTotalRef.child("totalStockCurrentValue");//totalCryptoBuyValue

        //UPDATE TOTAL BUYVALUE FROM FIREBASE
        totalBuyValueRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                double val =snapshot.getValue(Double.class);

                NumberFormat nf = NumberFormat.getInstance(Locale.US);
                nf.setMinimumFractionDigits(2); // <- the trick is here
                String formatVal = nf.format(val); // <- 1,000.00
                editTotalBuyValue.setText(formatVal);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: firebase fail");
            }
        });

        //UPDATE TOTAL CURRENT VALUE FROM FIREBASE
        totalCurrentValueRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                double val = Double.parseDouble(snapshot.getValue().toString());

                NumberFormat nf = NumberFormat.getInstance(Locale.US);
                nf.setMinimumFractionDigits(2); // <- the trick is here
                String formatVal = nf.format(val); // <- 1,000.00

                editTotalCurrentValue.setText(formatVal);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: firebase fail");
            }
        });
    }
    private void refreshStockRecView()
    {
        //RECYCLER VIEW FIREBASE
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users").child(String.valueOf(MainActivity.UID)).child("StockTotal");
        DatabaseReference stockListRef = myRef.child("StockList");

        stockListRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    StockItem stockItem = dataSnapshot.getValue(StockItem.class);
                    stocksArray.add(stockItem);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //SET RECYCLERVIEW ADAPTER
        adapter = new StocksRecViewAdapter(getActivity());
        stocksRecView.setAdapter(adapter);
        stocksRecView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

}
