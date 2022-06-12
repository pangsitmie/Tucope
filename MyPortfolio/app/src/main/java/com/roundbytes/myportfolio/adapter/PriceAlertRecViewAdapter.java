package com.roundbytes.myportfolio.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.roundbytes.myportfolio.R;
import com.roundbytes.myportfolio.crypto.PriceAlertItem;

import java.util.ArrayList;

public class PriceAlertRecViewAdapter extends RecyclerView.Adapter<PriceAlertRecViewAdapter.ViewHolder> {


    Context mContext;
    private ArrayList<PriceAlertItem> priceAlertItemArrayList = new ArrayList<>();

    public PriceAlertRecViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public PriceAlertRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.price_alert_card,parent, false);
        return new PriceAlertRecViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PriceAlertRecViewAdapter.ViewHolder holder, int position) {
        holder.symbol.setText(priceAlertItemArrayList.get(position).getSymbol());
        holder.price.setText(String.valueOf(priceAlertItemArrayList.get(position).getPrice()));
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priceAlertItemArrayList.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return priceAlertItemArrayList.size();
    }

    public void setPriceAlertItemArrayList(ArrayList<PriceAlertItem> priceAlertItemArrayList) {
        this.priceAlertItemArrayList = priceAlertItemArrayList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CardView parent;
        TextView symbol;
        TextView price;
        Button btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            parent = itemView.findViewById(R.id.parent);

            symbol = itemView.findViewById(R.id.symbol);
            price = itemView.findViewById(R.id.price);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}


