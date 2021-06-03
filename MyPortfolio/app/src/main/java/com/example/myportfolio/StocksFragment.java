package com.example.myportfolio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StocksFragment extends Fragment {
    @Nullable


    private RecyclerView stocksRecView;
    private StocksRecViewAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_stocks, container , false);

        stocksRecView = v.findViewById(R.id.stocksRecView);
        adapter = new StocksRecViewAdapter(getActivity());

        stocksRecView.setAdapter(adapter);
        stocksRecView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ArrayList<StocksData> stocks = new ArrayList<>();
        stocks.add(new StocksData("ADRO",55, 1188, 6537445));
        stocks.add(new StocksData("ANTM",10, 1151, 1151970));
        stocks.add(new StocksData("ADRO",19, 5759, 10942500));

        adapter.setStocks(stocks);

        return v;


    }
}
