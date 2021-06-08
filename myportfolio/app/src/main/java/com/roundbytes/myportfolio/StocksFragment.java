package com.roundbytes.myportfolio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class StocksFragment extends Fragment {
    @Nullable


    private RecyclerView stocksRecView;
    private StocksRecViewAdapter adapter;
    Button addBtn;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_stocks, container , false);

        addBtn = v.findViewById(R.id.btnAdd);
        stocksRecView = v.findViewById(R.id.stocksRecView);
        adapter = new StocksRecViewAdapter(getActivity());

        stocksRecView.setAdapter(adapter);
        stocksRecView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ArrayList<StocksData> stocks = new ArrayList<>();
        stocks.add(new StocksData("ADRO",55, 1188, 6537445));
        stocks.add(new StocksData("ANTM",10, 1151, 1151970));
        stocks.add(new StocksData("ADRO",19, 5759, 10942500));



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

}
