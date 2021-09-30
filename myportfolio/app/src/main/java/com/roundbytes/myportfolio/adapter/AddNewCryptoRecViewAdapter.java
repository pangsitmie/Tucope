package com.roundbytes.myportfolio.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.roundbytes.myportfolio.MainActivity;
import com.roundbytes.myportfolio.R;
import com.roundbytes.myportfolio.crypto.CryptoItem;
import com.roundbytes.myportfolio.crypto.CryptoModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

//https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest
//key= X-CMC_PRO_API_KEY
//af47668c-30ad-42e4-9461-7cf302924267


public class AddNewCryptoRecViewAdapter extends RecyclerView.Adapter<AddNewCryptoRecViewAdapter.ViewHolder> {
    //FIREBASE REALTIME DATABASE VARIABLE
    public FirebaseDatabase database;
    public DatabaseReference myRef;
    private String UID = MainActivity.UID;

    ArrayList<CryptoModel> currencyModelArrayList;
    private Context mContext;
    private static DecimalFormat df2 = new DecimalFormat("#.##");


    public AddNewCryptoRecViewAdapter(ArrayList<CryptoModel> currencyModelArrayList, Context mcontext) {
        this.currencyModelArrayList = currencyModelArrayList;
        this.mContext = mcontext;
    }

    public void filterList(ArrayList<CryptoModel> filteredList){
        currencyModelArrayList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AddNewCryptoRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.new_crypto_card,parent,false);
        return new AddNewCryptoRecViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddNewCryptoRecViewAdapter.ViewHolder holder, int position) {
        //item object
        CryptoModel currencyModel = currencyModelArrayList.get(position);
        //masukno nama price dll
        holder.cryptoName.setText(currencyModel.getName());
        holder.cryptoSymbol.setText(currencyModel.getSymbol());
        holder.cryptoPrice.setText("$"+df2.format(currencyModel.getPrice()));

        //button pressed
        holder.addNewCryptoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add to database
                CryptoItem cryptoItem = new CryptoItem(currencyModel.getSymbol(), currencyModel.getName());
                database = FirebaseDatabase.getInstance();
                myRef = database.getReference("Users").child(UID).child("CryptoTotal").child("CryptoList");
                myRef.child(currencyModel.getSymbol()).setValue(cryptoItem);

                Toast.makeText(mContext, currencyModel.getSymbol()+" has been added", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra("refresh","Crypto");
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return currencyModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView cryptoName, cryptoSymbol, cryptoPrice;
        private Button addNewCryptoBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cryptoSymbol= itemView.findViewById(R.id.cryptoSymbol);
            cryptoName= itemView.findViewById(R.id.cryptoName);
            cryptoPrice= itemView.findViewById(R.id.cryptoPrice);
            addNewCryptoBtn = itemView.findViewById(R.id.addNewCryptoBtn);
        }
    }
}
