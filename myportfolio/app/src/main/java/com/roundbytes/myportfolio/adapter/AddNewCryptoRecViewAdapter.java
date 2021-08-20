package com.roundbytes.myportfolio.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.roundbytes.myportfolio.R;
import com.roundbytes.myportfolio.crypto.CryptoModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

//https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest
//key= X-CMC_PRO_API_KEY
//af47668c-30ad-42e4-9461-7cf302924267
public class AddNewCryptoRecViewAdapter extends RecyclerView.Adapter<AddNewCryptoRecViewAdapter.ViewHolder> {

    ArrayList<CryptoModel> currencyModelArrayList;
    private Context mcontext;
    private static DecimalFormat df2 = new DecimalFormat("#.##");


    public AddNewCryptoRecViewAdapter(ArrayList<CryptoModel> currencyModelArrayList, Context mcontext) {
        this.currencyModelArrayList = currencyModelArrayList;
        this.mcontext = mcontext;
    }

    public void filterList(ArrayList<CryptoModel> filteredList){
        currencyModelArrayList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AddNewCryptoRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.new_crypto_card,parent,false);
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

    }

    @Override
    public int getItemCount() {
        return currencyModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView cryptoName, cryptoSymbol, cryptoPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cryptoSymbol= itemView.findViewById(R.id.cryptoSymbol);
            cryptoName= itemView.findViewById(R.id.cryptoName);
            cryptoPrice= itemView.findViewById(R.id.cryptoPrice);
        }
    }
}
