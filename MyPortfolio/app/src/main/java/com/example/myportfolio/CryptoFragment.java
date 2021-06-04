package com.example.myportfolio;

import android.app.AlertDialog;
import android.os.Bundle;
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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CryptoFragment extends Fragment {
    @Nullable

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

    public ArrayList<CryptoList> cryptoList = new ArrayList<>();

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

        //buttons functionality



        //crypto arraylsit initialization
/*        cryptoList.add(new CryptoList("ETH"));
        cryptoList.add(new CryptoList("VET"));
        cryptoList.add(new CryptoList("DOGE"));*/

        //CryptoTotal Initialization
        CryptoTotal cryptoTotal = new CryptoTotal();


        //currency format
        String formatTotalBuyValue = NumberFormat.getCurrencyInstance(new Locale("id","ID")).format(cryptoTotal.getTotalBuyValue());
        String formatTotalCurrentValue = NumberFormat.getCurrencyInstance(new Locale("id","ID")).format(cryptoTotal.getTotalCurrentValue());
        //display value
        editTotalBuyValue.setText(formatTotalBuyValue);
        editTotalCurrentValue.setText(formatTotalCurrentValue);




        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewCryptoItemDialog();

            }
        });
        adapter.setCryptos(cryptoList);

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
                Toast.makeText(getContext(), itemName.getText().toString()+" clicked", Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void setItemName(String str)
    {
        addItemName = str;
        cryptoList.add(new CryptoList(addItemName));
        Toast.makeText(getContext(), "Add btn clicked", Toast.LENGTH_SHORT).show();
        adapter.setCryptos(cryptoList);
    }

}
