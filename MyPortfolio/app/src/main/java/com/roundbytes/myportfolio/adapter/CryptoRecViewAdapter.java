package com.roundbytes.myportfolio.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.roundbytes.myportfolio.crypto.CryptoHistoryActivity;
import com.roundbytes.myportfolio.activity.AddTransaction;
import com.roundbytes.myportfolio.MainActivity;
import com.roundbytes.myportfolio.crypto.CryptoItem;
import com.roundbytes.myportfolio.R;
import com.roundbytes.myportfolio.crypto.CryptoModel;

import java.util.ArrayList;

public class CryptoRecViewAdapter extends RecyclerView.Adapter<CryptoRecViewAdapter.ViewHolder> {

    private static final String TAG = "CryptoRecViewAdapter";
    private Context mContext;

    private ArrayList<CryptoItem> cryptos = new ArrayList<>();

    //FIREBASE VARIABLES
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private String UID = MainActivity.UID;

    //dialog
    Dialog myDialog;

    //VARIABLE
    public double currentValue=0.0;
    public double unrealized=0.0;
    public double percentage=0.0;

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

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Log.d(TAG, "onBindViewHolder: Called");

        //TEMPORARY STRING FOR SET TEXT
        String cryptoAmount = cryptos.get(position).getAmount()+" "+cryptos.get(position).getCryptoCode();
        String txtEditAvgBuyPrice = "$ " + String.format("%.2f", cryptos.get(position).getCryptoAvgBuyPrice());

        String txtEditAvgBuyValue = "$ " + String.format("%.2f",cryptos.get(position).getCryptoSubTotalBuyValue());





        //UPDATE CURRENT VALUE, UNREALIZED AND PERCENTAGE AFTER API IS CALLED

        final Handler handler1 = new Handler(Looper.getMainLooper());
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                double cryptoItemCurrentPrice = 0.0;
                for(CryptoModel model: MainActivity.cryptoModelsArrayList){
                    if(model.getSymbol().equalsIgnoreCase(cryptos.get(position).getCryptoCode())){
                        cryptoItemCurrentPrice = model.getPrice();
                        break;
                    }
                }

                //CURRENT VALUE
                currentValue = cryptoItemCurrentPrice* cryptos.get(position).getAmount();
                String txtEditCurrentValue = "$ " + String.format("%.2f", currentValue);
                holder.cryptoValue.setText(txtEditCurrentValue);
                holder.txtEditCurrentValue.setText(txtEditCurrentValue);


                unrealized = currentValue - cryptos.get(position).getCryptoSubTotalBuyValue();
                String txtEditUnrealized = "$ " + String.format("%.2f", unrealized);


                percentage = (unrealized/cryptos.get(position).getCryptoSubTotalBuyValue()*100);
                String txtEditPercentage =  String.format("%.2f", percentage) + "%";

                //CHANGE COLOR TO RED IF LOSS
                if(percentage<0){
                    holder.txtEditPercentage.setTextColor(Color.RED);
                    Log.d(TAG, "onBindViewHolder Percent: "+"Loss");
                    Log.d(TAG, "onBindViewHolder Percent: "+percentage);
                    holder.txtEditUnrealized.setTextColor(Color.RED);
                }
                holder.txtEditUnrealized.setText(txtEditUnrealized);
                holder.txtEditPercentage.setText(txtEditPercentage);
            }
        }, 1000);





        //SET TEXT CURRENT VALUE
        String txtEditCurrentValue = "$ " + String.format("%.2f", currentValue);
        holder.cryptoValue.setText(txtEditCurrentValue);
        holder.txtEditCurrentValue.setText(txtEditCurrentValue);

        //SET TEXT UNREALIZED
        String txtEditUnrealized = "$ " + String.format("%.2f", unrealized);
        holder.txtEditUnrealized.setText(txtEditUnrealized);

        //SET TEXT PERCENTAGE
        if(percentage<0){
            holder.txtEditPercentage.setTextColor(Color.RED);
            holder.txtEditUnrealized.setTextColor(Color.RED);
        }
        String txtEditPercentage =  String.format("%.2f", percentage) + "%";
        holder.txtEditPercentage.setText(txtEditPercentage);




        //SET TEXT TO CRYPTOCARD
        holder.cryptoCode.setText(cryptos.get(position).getCryptoCode());
        holder.cryptoName.setText(cryptos.get(position).getCryptoName());
        holder.cryptoAmount.setText(cryptoAmount);

        holder.txtEditAmount.setText(cryptoAmount);
        holder.txtEditAvgBuyPrice.setText(txtEditAvgBuyPrice);
        holder.txtEditAvgBuyValue.setText(txtEditAvgBuyValue);


        holder.btnAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, AddTransaction.class);
                Bundle extras = new Bundle();
                extras.putString("TYPE","crypto");
                extras.putString("CODE",cryptos.get(position).getCryptoCode());
                intent.putExtras(extras);
                mContext.startActivity(intent);

            }

        });
        holder.btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CryptoHistoryActivity.class);
                intent.putExtra("CODE", cryptos.get(position).getCryptoCode());
                mContext.startActivity(intent);
            }
        });
        holder.btnDeleteList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = FirebaseDatabase.getInstance();
                myRef = database.getReference("Users").child(UID).child("CryptoTotal").child("CryptoList");
                myRef.child(cryptos.get(position).getCryptoCode()).removeValue();

                //TOAST AND RETURN TO MAIN ACTIVITY ACCORDING TO FRAGMENT
                Log.d(TAG, "CRYPTO REC VIEW UID DELETE"+MainActivity.UID);

                //Toast.makeText(mContext, cryptos.get(position).getCryptoCode()+" removed", Toast.LENGTH_SHORT).show();
                //INTENT
                MainActivity.UID= UID;
                Intent intent1 = new Intent(mContext, MainActivity.class);
                intent1.putExtra("refresh","crypto");
                mContext.startActivity(intent1);

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
        TextView cryptoCode, cryptoName, cryptoAmount, cryptoValue;
        Button btnAddTransaction, btnDeleteList, btnHistory;
        RelativeLayout cryptoColRelLayout, cryptoExpandRelLayout;
        TextView txtEditAmount, txtEditAvgBuyPrice, txtEditAvgBuyValue,txtEditCurrentValue, txtEditUnrealized, txtEditPercentage ;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            parent = itemView.findViewById(R.id.parent);

            //crypto card layout
            btnAddTransaction = itemView.findViewById(R.id.btnAddTransaction);
            cryptoCode = itemView.findViewById(R.id.cryptoSymbol);
            cryptoName = itemView.findViewById(R.id.cryptoName);
            cryptoAmount = itemView.findViewById(R.id.amount);
            cryptoValue = itemView.findViewById(R.id.valueAfterFee);
            btnDeleteList = itemView.findViewById(R.id.btnDeleteList);
            btnHistory = itemView.findViewById(R.id.btnHistory);



            cryptoColRelLayout = itemView.findViewById(R.id.cryptoColRelLayout);
            cryptoExpandRelLayout = itemView.findViewById(R.id.cryptoExpandRelLayout);

            txtEditAmount = itemView.findViewById(R.id.txtEditAmount);
            txtEditAvgBuyPrice = itemView.findViewById(R.id.editValueBeforeFee);
            txtEditAvgBuyValue = itemView.findViewById(R.id.EditValueAfterFee);
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
