package com.roundbytes.myportfolio;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CryptoRecViewAdapter extends RecyclerView.Adapter<CryptoRecViewAdapter.ViewHolder> {

    private static final String TAG = "StocksRecViewAdapter";
    private Context mContext;

    private ArrayList<CryptoList> cryptos = new ArrayList<>();


    //dialog
    Dialog myDialog;

    //constructor
    public CryptoRecViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.crypto_card,parent,false);
        return new com.roundbytes.myportfolio.CryptoRecViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: Called");

        holder.cryptoCode.setText(cryptos.get(position).getCryptoCode());
        holder.cryptoAmount.setText(String.valueOf(cryptos.get(position).getAmount()));
        holder.cryptoValue.setText(String.valueOf(cryptos.get(position).getCurrentValue()));

        holder.addTransactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog = new Dialog(mContext);
                myDialog.setContentView(R.layout.crypto_add_transaction_popup);
                myDialog.show();
            }

        });

        if(cryptos.get(position).isExpanded()){
            holder.cryptoExpandRelLayout.setVisibility(View.VISIBLE);
        }else{
            holder.cryptoExpandRelLayout.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {return cryptos.size();}

    //set arraylist setter
    public void setCryptos(ArrayList<CryptoList> cryptos) {
        this.cryptos = cryptos;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView parent;
        TextView cryptoCode, cryptoName, cryptoAmount, cryptoValue;
        Button addTransactionBtn;
        RelativeLayout cryptoColRelLayout, cryptoExpandRelLayout;
        ImageView upArrow;
        TextView txtEditAmount, txtEditAvgBuyPrice, txtEditAvgBuyValue,txtEditCurrentValue, txtEditUnrealized, txtEditPercentage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            parent = itemView.findViewById(R.id.parent);

            addTransactionBtn = itemView.findViewById(R.id.addTransactionBtn);
            cryptoCode = itemView.findViewById(R.id.cryptoCode);
            cryptoAmount = itemView.findViewById(R.id.cryptoAmount);
            cryptoValue = itemView.findViewById(R.id.cryptoValue);

            cryptoColRelLayout = itemView.findViewById(R.id.cryptoColRelLayout);
            cryptoExpandRelLayout = itemView.findViewById(R.id.cryptoExpandRelLayout);

            txtEditAmount = itemView.findViewById(R.id.txtEditAmount);
            txtEditAvgBuyPrice = itemView.findViewById(R.id.txtEditAvgBuyPrice);
            txtEditAvgBuyValue = itemView.findViewById(R.id.txtEditAvgBuyValue);
            txtEditCurrentValue = itemView.findViewById(R.id.txtEditCurrentValue);
            txtEditUnrealized = itemView.findViewById(R.id.txtEditUnrealized);
            txtEditPercentage = itemView.findViewById(R.id.txtEditPercentage);

            cryptoColRelLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CryptoList crypto = cryptos.get((getAdapterPosition()));
                    crypto.setExpanded(!crypto.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });



        }
    }
}
