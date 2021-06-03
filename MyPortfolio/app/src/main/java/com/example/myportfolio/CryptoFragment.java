package com.example.myportfolio;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

public class CryptoFragment extends Fragment {
    @Nullable

    private RecyclerView cryptosRecView;
    private CryptoRecViewAdapter adapter;
    private CardView cryptoDetails;
    private TextView editTotalBuyValue,  editTotalCurrentValue;
    private Button addBtn, deleteBtn;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crypto, container , false);


        cryptoDetails = v.findViewById(R.id.cryptoDetailsCard);
        editTotalBuyValue = v.findViewById(R.id.editTotalBuyValue);
        editTotalCurrentValue = v.findViewById(R.id.editTotalCurrentValue);
        addBtn = v.findViewById(R.id.btnAdd);
        deleteBtn = v.findViewById(R.id.btnDelete);

        cryptosRecView = v.findViewById(R.id.cryptoRecView);
        adapter = new CryptoRecViewAdapter(getActivity());

        cryptosRecView.setAdapter(adapter);
        cryptosRecView.setLayoutManager(new LinearLayoutManager(getActivity()));


        //crypto arraylsit initialization
        ArrayList<CryptosData> cryptos = new ArrayList<>();
        cryptos.add(new CryptosData("ETH"));
        cryptos.add(new CryptosData("VET"));
        cryptos.add(new CryptosData("DOGE"));

        //CryptoTotal Initialization
        CryptoTotal cryptoTotal = new CryptoTotal();


        //currency format
        String formatTotalBuyValue = NumberFormat.getCurrencyInstance(new Locale("id","ID")).format(cryptoTotal.getTotalBuyValue());
        String formatTotalCurrentValue = NumberFormat.getCurrencyInstance(new Locale("id","ID")).format(cryptoTotal.getTotalCurrentValue());
        //display value
        editTotalBuyValue.setText(formatTotalBuyValue);
        editTotalCurrentValue.setText(formatTotalCurrentValue);


        adapter.setCryptos(cryptos);

        return v;
    }

}
