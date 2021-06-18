package com.roundbytes.myportfolio.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
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
import com.roundbytes.myportfolio.crypto.CryptoItem;
import com.roundbytes.myportfolio.R;

import java.util.ArrayList;

public class CryptoRecViewAdapter extends RecyclerView.Adapter<CryptoRecViewAdapter.ViewHolder> {

    private static final String TAG = "StocksRecViewAdapter";
    private Context mContext;

    private ArrayList<CryptoItem> cryptos = new ArrayList<>();

    //FIREBASE VARIABLES
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private String username = MainActivity.username;

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
        return new CryptoRecViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: Called");

        //TEMPORARY STRING FOR SET TEXT
        String cryptoAmount = cryptos.get(position).getAmount()+" "+cryptos.get(position).getCryptoCode();
        String cryptoValue = "$ " + cryptos.get(position).getCryptoSubTotalCurrentValue();
        String txtEditAvgBuyValue = "$ " + cryptos.get(position).getCryptoSubTotalBuyValue();
        String txtEditCurrentValue = "$ " + cryptos.get(position).getCryptoSubTotalCurrentValue();

        double unrealized = cryptos.get(position).getCryptoSubTotalCurrentValue() - cryptos.get(position).getCryptoSubTotalBuyValue();
        String txtEditUnrealized = "$ " + String.format("%.2f", unrealized);
        double percentage = (unrealized/cryptos.get(position).getCryptoSubTotalBuyValue()*100);
        String txtEditPercentage =  String.format("%.2f", percentage) + "%";

        //SET TEXT TO CRYPTOCARD
        holder.cryptoCode.setText(cryptos.get(position).getCryptoCode());
        holder.cryptoAmount.setText(cryptoAmount);
        holder.cryptoValue.setText(cryptoValue);
        // TODO: 6/18/2021 avg buy price blm diurus
        holder.txtEditAmount.setText(cryptoAmount);
        holder.txtEditAvgBuyValue.setText(txtEditAvgBuyValue);
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
                myDialog = new Dialog(mContext);
                myDialog.setContentView(R.layout.crypto_add_transaction_popup);
                myDialog.show();
            }

        });
        holder.btnDeleteList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = FirebaseDatabase.getInstance();
                myRef = database.getReference("Users").child(username).child("CryptoTotal").child("CryptoList");
                myRef.child(cryptos.get(position).getCryptoCode()).removeValue();
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
    public void setCryptos(ArrayList<CryptoItem> cryptos) {
        this.cryptos = cryptos;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView parent;
        TextView cryptoCode, cryptoAmount, cryptoValue;
        Button btnAddTransaction, btnDeleteList, btnMoreDetails;
        RelativeLayout cryptoColRelLayout, cryptoExpandRelLayout;
        TextView txtEditAmount, txtEditAvgBuyPrice, txtEditAvgBuyValue,txtEditCurrentValue, txtEditUnrealized, txtEditPercentage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            parent = itemView.findViewById(R.id.parent);

            //crypto card layout
            btnAddTransaction = itemView.findViewById(R.id.btnAddTransaction);
            cryptoCode = itemView.findViewById(R.id.cryptoCode);
            cryptoAmount = itemView.findViewById(R.id.cryptoAmount);
            cryptoValue = itemView.findViewById(R.id.cryptoValue);
            btnDeleteList = itemView.findViewById(R.id.btnDeleteList);
            btnMoreDetails = itemView.findViewById(R.id.btnMoreDetails);



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
                    CryptoItem crypto = cryptos.get((getAdapterPosition()));
                    crypto.setExpanded(!crypto.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });



        }
    }
}