package com.roundbytes.myportfolio.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.roundbytes.myportfolio.activity.AddTransaction;
import com.roundbytes.myportfolio.MainActivity;
import com.roundbytes.myportfolio.R;
import com.roundbytes.myportfolio.stock.StockItem;

import java.util.ArrayList;

public class StocksRecViewAdapter extends RecyclerView.Adapter<StocksRecViewAdapter.ViewHolder> {

    private static final String TAG = "StocksRecViewAdapter";

    private Context mContext;

    //FIREBASE VARIABLES
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private String username = MainActivity.username;

    //dialog
    Dialog myDialog;

    //Array List
    private ArrayList<StockItem> stocks = new ArrayList<>();

    //constructor
    public StocksRecViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stocks_card,parent,false);
        return new StocksRecViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: Called");

        holder.stockCode.setText(stocks.get(position).getStockCode());
        holder.stockAmount.setText(String.valueOf(stocks.get(position).getLot()));

        //TEMPORARY STRING FOR SET TEXT
        String txtLot = stocks.get(position).getLot()+" Lot";
        String txtStockTotalBuyValue = "$ " + stocks.get(position).getTotalBuyValue();
        String txtEditAvgBuyPrice = "$ " + stocks.get(position).getAvgBuyPrice();
        String txtEditCurrentValue = "$ " + "NO DATA";

        double unrealized = stocks.get(position).getTotalBuyValue()*2 - stocks.get(position).getTotalBuyValue();
        String txtEditUnrealized = "$ " + String.format("%.2f", unrealized);
        double percentage = (unrealized/stocks.get(position).getTotalBuyValue()*100);
        String txtEditPercentage =  String.format("%.2f", percentage) + "%";

        //SET TEXT TO STOCK CARD
        holder.stockValue.setText(txtEditCurrentValue);
        holder.stockCode.setText(stocks.get(position).getStockCode());
        holder.stockAmount.setText(txtLot);
        holder.txtEditAmount.setText(txtLot);
        holder.txtEditAvgBuyPrice.setText(txtEditAvgBuyPrice);
        holder.txtEditTotalBuyValue.setText(txtStockTotalBuyValue);
        holder.txtEditCurrentValue.setText(txtEditCurrentValue);
        //CHANGE COLOR TO RED IF LOSS
        if(percentage<=0.0){
            holder.txtEditPercentage.setTextColor(Color.RED);
            holder.txtEditUnrealized.setTextColor(Color.RED);
        }
        holder.txtEditUnrealized.setText(txtEditUnrealized);
        holder.txtEditPercentage.setText(txtEditPercentage);

        holder.btnAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddTransaction.class);
                Bundle extras = new Bundle();
                extras.putString("TYPE","stock");
                extras.putString("CODE",stocks.get(position).getStockCode());
                intent.putExtras(extras);
                mContext.startActivity(intent);
            }

        });

        //DELETE BUTTON WHEN PRESSED -> DELETE THAT ITEM FROM DATABASE
        holder.btnDeleteList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = FirebaseDatabase.getInstance();
                myRef = database.getReference("Users").child(username).child("StockTotal").child("StockList");
                myRef.child(stocks.get(position).getStockCode()).removeValue();
            }
        });

        if(stocks.get(position).isExpanded()){
            holder.stocksExpandRelLayout.setVisibility(View.VISIBLE);
        }else{
            holder.stocksExpandRelLayout.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {return stocks.size();}

    //set arraylist setter
    public void setStocks(ArrayList<StockItem> stocks) {
        this.stocks = stocks;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView parent;
        TextView stockCode, stockAmount, stockValue;
        Button btnAddTransaction, btnDeleteList, btnMoreDetails;
        RelativeLayout stocksColRelLayout, stocksExpandRelLayout;
        TextView txtEditAmount, txtEditAvgBuyPrice, txtEditTotalBuyValue,txtEditCurrentValue, txtEditUnrealized, txtEditPercentage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            parent = itemView.findViewById(R.id.parent);
            stockCode = itemView.findViewById(R.id.stockCode);
            stockAmount = itemView.findViewById(R.id.lot);
            stockValue = itemView.findViewById(R.id.stockValue);
            btnAddTransaction = itemView.findViewById(R.id.btnAddTransaction);
            btnDeleteList = itemView.findViewById(R.id.btnDeleteList);
            btnMoreDetails = itemView.findViewById(R.id.btnMoreDetails);

            stocksColRelLayout = itemView.findViewById(R.id.stocksColRelLayout);
            stocksExpandRelLayout = itemView.findViewById(R.id.stocksExpandRelLayout);

            txtEditAmount = itemView.findViewById(R.id.txtEditAmount);
            txtEditAvgBuyPrice = itemView.findViewById(R.id.txtEditAvgBuyPrice);
            txtEditTotalBuyValue = itemView.findViewById(R.id.txtEditAvgBuyValue);
            txtEditCurrentValue = itemView.findViewById(R.id.txtEditCurrentValue);
            txtEditUnrealized = itemView.findViewById(R.id.txtEditUnrealized);
            txtEditPercentage = itemView.findViewById(R.id.txtEditPercentage);

            stocksColRelLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StockItem stock = stocks.get((getAdapterPosition()));
                    stock.setExpanded(!stock.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });


        }
    }





}




