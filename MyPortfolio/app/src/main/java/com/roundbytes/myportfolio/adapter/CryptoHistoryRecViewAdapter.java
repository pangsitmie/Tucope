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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.roundbytes.myportfolio.MainActivity;
import com.roundbytes.myportfolio.R;
import com.roundbytes.myportfolio.activity.AddTransaction;
import com.roundbytes.myportfolio.crypto.CryptoItem;
import com.roundbytes.myportfolio.crypto.CryptoTotal;
import com.roundbytes.myportfolio.crypto.CryptosTransactions;

import java.util.ArrayList;

public class CryptoHistoryRecViewAdapter extends RecyclerView.Adapter<CryptoHistoryRecViewAdapter.ViewHolder> {

    private static final String TAG = "CryptoHistoryAdapter";
    private Context mContext;

    private ArrayList<CryptosTransactions> transactionsArray = new ArrayList<>();

    //FIREBASE VARIABLES
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private String username = MainActivity.username;

    //dialog
    Dialog myDialog;

    //constructor
    public CryptoHistoryRecViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.crypto_history_card,parent,false);
        return new CryptoHistoryRecViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: Called");

        //TEMPORARY STRING FOR SET TEXT
        String txtTransactionType = transactionsArray.get(position).getType();
        String txtTransactionAmount = String.valueOf(transactionsArray.get(position).getAmount());
        String txtTransactionDate = String.valueOf(transactionsArray.get(position).getDate());
        String txtTransactionPrice =  String.valueOf(transactionsArray.get(position).getPrice());

        String txtValueBeforeFee = String.valueOf(transactionsArray.get(position).getValueBeforeFee());
        String txtValueAfterFee = String.valueOf(transactionsArray.get(position).getValueAfterFee());
        String txtFee = String.valueOf(transactionsArray.get(position).getFee());
        String txtPNL = String.valueOf(transactionsArray.get(position).getPnl());

        /*double unrealized = cryptos.get(position).getCryptoSubTotalBuyValue()*2 - cryptos.get(position).getCryptoSubTotalBuyValue();
        String txtEditUnrealized = "$ " + String.format("%.2f", unrealized);
        double percentage = (unrealized/cryptos.get(position).getCryptoSubTotalBuyValue()*100);
        String txtEditPercentage =  String.format("%.2f", percentage) + "%";*/


        //SET TEXT TO CRYPTOCARD
        holder.transactionType.setText(txtTransactionType);
        holder.editValueAfterFeeTop.setText(txtValueAfterFee);
        holder.editAmount.setText(txtTransactionAmount);

        holder.editDate.setText(txtTransactionDate);
        holder.editDate.setText(txtTransactionDate);
        holder.txtEditAmount.setText(txtTransactionAmount);
        holder.editPrice.setText(txtTransactionPrice);
        holder.editValueBeforeFee.setText(txtValueBeforeFee);
        holder.editValueAfterFee.setText(txtValueAfterFee);
        holder.editValueAfterFee.setText(txtValueAfterFee);
        holder.editFee.setText(txtFee);
        holder.editPNL.setText(txtPNL);

        //CHANGE COLOR TO RED IF LOSS
        if(txtTransactionType.equals("Sell")){
            holder.transactionType.setTextColor(Color.RED);
        }

        /*holder.btnDeleteList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = FirebaseDatabase.getInstance();
                myRef = database.getReference("Users").child(username).child("CryptoTotal").child("CryptoList");
                myRef.child(cryptos.get(position).getCryptoCode()).removeValue();

                //TOAST AND RETURN TO MAIN ACTIVITY ACCORDING TO FRAGMENT
                Toast.makeText(mContext, cryptos.get(position).getCryptoCode()+" removed", Toast.LENGTH_SHORT).show();
                //INTENT
                Intent intent1 = new Intent(mContext, MainActivity.class);
                Bundle extras = new Bundle();
                extras.putString("username",username);
                extras.putString("refresh","crypto");
                intent1.putExtras(extras);
                mContext.startActivity(intent1);
            }
        });*/

        if(transactionsArray.get(position).isExpanded()){
            holder.cryptoExpandRelLayout.setVisibility(View.VISIBLE);
        }else{
            holder.cryptoExpandRelLayout.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {return transactionsArray.size();}

    //set arraylist setter
    public void setCryptoHistory(ArrayList<CryptosTransactions> transactionsArray) {
        this.transactionsArray = transactionsArray;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView parent;
        TextView transactionType, editValueAfterFeeTop, editAmount, editDate, txtEditAmount, editPrice, editValueBeforeFee, editValueAfterFee,editFee, editPNL;
        Button  btnDeleteList;
        RelativeLayout cryptoColRelLayout, cryptoExpandRelLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            parent = itemView.findViewById(R.id.parent);

            //crypto card layout
            transactionType = itemView.findViewById(R.id.transactionType);
            editValueAfterFeeTop = itemView.findViewById(R.id.editValueAfterFeeTop);
            editAmount = itemView.findViewById(R.id.editAmount);
            editDate = itemView.findViewById(R.id.editDate);
            txtEditAmount = itemView.findViewById(R.id.txtEditAmount);
            editPrice = itemView.findViewById(R.id.editPrice);
            editValueBeforeFee = itemView.findViewById(R.id.editValueBeforeFee);
            editValueAfterFee = itemView.findViewById(R.id.editValueAfterFee);
            editFee = itemView.findViewById(R.id.editFee);
            editPNL = itemView.findViewById(R.id.editPNL);

            btnDeleteList = itemView.findViewById(R.id.btnDeleteList);

            cryptoColRelLayout = itemView.findViewById(R.id.cryptoColRelLayout);
            cryptoExpandRelLayout = itemView.findViewById(R.id.cryptoExpandRelLayout);



            cryptoColRelLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CryptosTransactions crypto = transactionsArray.get((getAdapterPosition()));
                    crypto.setExpanded(!crypto.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });



        }
    }
}
