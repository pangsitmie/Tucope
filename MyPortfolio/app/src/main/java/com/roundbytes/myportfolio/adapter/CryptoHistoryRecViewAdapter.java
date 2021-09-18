package com.roundbytes.myportfolio.adapter;

import android.content.Context;
import android.graphics.Color;
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
import com.roundbytes.myportfolio.MainActivity;
import com.roundbytes.myportfolio.R;
import com.roundbytes.myportfolio.crypto.CryptoModel;
import com.roundbytes.myportfolio.crypto.CryptosTransactions;
import com.roundbytes.myportfolio.fragment.CryptoFragment;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CryptoHistoryRecViewAdapter extends RecyclerView.Adapter<CryptoHistoryRecViewAdapter.ViewHolder> {

    private static final String TAG = "CryptoHistoryAdapter";
    private Context mContext;

    private ArrayList<CryptosTransactions> transactionsArray = new ArrayList<>();

    //FIREBASE VARIABLES
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private String UID = MainActivity.UID;





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
        //TEMPORARY STRING FOR SET TEXT
        String txtTransactionType = transactionsArray.get(position).getType();
        String txtTransactionAmount = String.valueOf(transactionsArray.get(position).getAmount())+" "+transactionsArray.get(position).getCryptoCode();
        String txtTransactionDate = String.valueOf(transactionsArray.get(position).getDate());
        String txtTransactionPrice = "$ " + String.valueOf(transactionsArray.get(position).getPrice());

        //
        double valueBeforeFee = transactionsArray.get(position).getValueBeforeFee();
        String txtValueBeforeFee = "$ " + formatCurrency(valueBeforeFee);

        //
        double valueAfterFee = transactionsArray.get(position).getValueAfterFee();
        String txtValueAfterFee = "$ " + formatCurrency(valueAfterFee);
        String txtFee = "$ " + transactionsArray.get(position).getFee();


        /*double unrealized = cryptos.get(position).getCryptoSubTotalBuyValue()*2 - cryptos.get(position).getCryptoSubTotalBuyValue();
        String txtEditUnrealized = "$ " + String.format("%.2f", unrealized);
        double percentage = (unrealized/cryptos.get(position).getCryptoSubTotalBuyValue()*100);
        String txtEditPercentage =  String.format("%.2f", percentage) + "%";*/

        final Handler handler1 = new Handler(Looper.getMainLooper());
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                double cryptoItemCurrentPrice = 0.0;
                for(CryptoModel model: MainActivity.cryptoModelsArrayList){
                    if(model.getSymbol().equalsIgnoreCase(transactionsArray.get(position).getCryptoCode())){
                        cryptoItemCurrentPrice = model.getPrice();
                        break;
                    }
                }

                //CURRENT PNL
                double pnl = transactionsArray.get(position).getPnl();
                String txtEditPnl = "$ " + String.format("%.2f", pnl);
                holder.editPNL.setText(txtEditPnl);
            }
        }, 1000);


        //SET TEXT TO CRYPTOCARD
        holder.transactionType.setText(txtTransactionType);
        holder.editDateTop.setText(txtTransactionDate);



        holder.editValueAfterFeeTop.setText(txtValueAfterFee);
        holder.editAmount.setText(txtTransactionAmount);

        holder.txtEditAmount.setText(txtTransactionAmount);
        holder.editPrice.setText(txtTransactionPrice);
        holder.editValueBeforeFee.setText(txtValueBeforeFee);
        holder.editValueAfterFee.setText(txtValueAfterFee);
        holder.editValueAfterFee.setText(txtValueAfterFee);
        holder.editFee.setText(txtFee);


        //CHANGE COLOR TO RED IF LOSS
        if(txtTransactionType.equals("Sell")){
            holder.transactionType.setTextColor(Color.RED);
        }
        if(txtTransactionType.equals("BUY")){
            holder.transactionType.setTextColor(Color.GREEN);
        }


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
        TextView transactionType, editValueAfterFeeTop, editAmount, txtEditAmount, editPrice;
        TextView editValueBeforeFee, editValueAfterFee,editFee, editPNL, editDateTop;
        Button  btnDeleteList;
        RelativeLayout cryptoColRelLayout, cryptoExpandRelLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            parent = itemView.findViewById(R.id.parent);

            //crypto card layout
            transactionType = itemView.findViewById(R.id.transactionType);
            editValueAfterFeeTop = itemView.findViewById(R.id.editValueAfterFeeTop);
            editAmount = itemView.findViewById(R.id.editAmount);
            txtEditAmount = itemView.findViewById(R.id.txtEditAmount);
            editPrice = itemView.findViewById(R.id.editPrice);
            editValueBeforeFee = itemView.findViewById(R.id.editValueBeforeFee);
            editValueAfterFee = itemView.findViewById(R.id.editValueAfterFee);
            editFee = itemView.findViewById(R.id.editFee);
            editPNL = itemView.findViewById(R.id.editPNL);
            editDateTop = itemView.findViewById(R.id.editDateTop);

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
    public String formatCurrency(double d){
        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        nf.setMinimumFractionDigits(2); // <- the trick is here
        String formatTxtValueAfterFee = nf.format(d); // <- 1,000.00
        return formatTxtValueAfterFee;
    }
}
