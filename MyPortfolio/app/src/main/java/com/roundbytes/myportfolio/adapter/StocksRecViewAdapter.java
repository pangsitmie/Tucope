package com.roundbytes.myportfolio.adapter;

import android.app.Dialog;
import android.content.Context;
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

        holder.btnAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog = new Dialog(mContext);
                myDialog.setContentView(R.layout.crypto_add_transaction_popup);
                myDialog.show();
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
        TextView stockCode, stockAmount;
        Button btnAddTransaction, btnDeleteList, btnMoreDetails;
        RelativeLayout stocksColRelLayout, stocksExpandRelLayout;
        TextView txtEditAmount, txtEditAvgBuyPrice, txtEditAvgBuyValue,txtEditCurrentValue, txtEditUnrealized, txtEditPercentage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            parent = itemView.findViewById(R.id.parent);
            stockCode = itemView.findViewById(R.id.stockCode);
            stockAmount = itemView.findViewById(R.id.lot);
            btnAddTransaction = itemView.findViewById(R.id.btnAddTransaction);
            btnDeleteList = itemView.findViewById(R.id.btnDeleteList);
            btnMoreDetails = itemView.findViewById(R.id.btnMoreDetails);

            stocksColRelLayout = itemView.findViewById(R.id.stocksColRelLayout);
            stocksExpandRelLayout = itemView.findViewById(R.id.stocksExpandRelLayout);

            txtEditAmount = itemView.findViewById(R.id.txtEditAmount);
            txtEditAvgBuyPrice = itemView.findViewById(R.id.txtEditAvgBuyPrice);
            txtEditAvgBuyValue = itemView.findViewById(R.id.txtEditAvgBuyValue);
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



