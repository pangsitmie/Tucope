package com.roundbytes.myportfolio.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roundbytes.myportfolio.crypto.AddNewCryptoActivity;
import com.roundbytes.myportfolio.crypto.CryptoItem;
import com.roundbytes.myportfolio.adapter.CryptoRecViewAdapter;
import com.roundbytes.myportfolio.R;
import com.roundbytes.myportfolio.MainActivity;
import com.roundbytes.myportfolio.crypto.CryptoModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CryptoFragment extends Fragment {
    @Nullable

    private static final String TAG = "FIREBASECALL";

    private RecyclerView cryptosRecView;
    private CryptoRecViewAdapter adapter;
    private CardView cryptoDetails;
    private TextView editTotalBuyValue,  editTotalCurrentValue;
    private Button addBtn;


    //popup
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText itemName;
    private Button saveBtn;

    public String addItemName;

    //FIREBASE REALTIME DATABASE VARIABLE
    public FirebaseDatabase database;
    public DatabaseReference myRef;
    private String UID = MainActivity.UID;


    public ArrayList<CryptoItem> cryptoArray = new ArrayList<>();
    public static ArrayList<CryptoModel> cryptoModelsArrayList = new ArrayList<>();;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crypto, container , false);

        //get currency data
        getCurrencyData();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                Log.d(TAG, "size: "+cryptoModelsArrayList.size());
                Log.d(TAG, "btc price: "+cryptoModelsArrayList.get(0).getPrice());
            }
        }, 5000);

        //VIEW INITIALIZATION
        viewInitialization(v);
        //TOTAL BUY VALUE INITIALIZATION
        CryptoTopCardInitialization();
        
        //TOTAL CURRENT VALUE INITIALIZATION
        // TODO: 6/12/2021 ambil api trs cocokno dengan total buy value

        Log.d("UID: ", "The uid is: "+MainActivity.UID);
        //RECYCLER VIEW FIREBASE
        refreshCryptoRecView();

        //----------------ADD NEW CRYPTO BUTTON---------------
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddNewCryptoActivity.class);
                startActivity(intent);
            }
        });

        //SET RECYCLERVIEW ADAPTER
        adapter = new CryptoRecViewAdapter(getActivity());
        cryptosRecView.setAdapter(adapter);
        cryptosRecView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.setCryptos(cryptoArray);


        return v;

    }

    //VOID METHODS -----------------------------------------------------------
    /*
    private void createNewCryptoItemDialog()
    {
        dialogBuilder = new AlertDialog.Builder(getActivity());
        final View itemPopupView = getLayoutInflater().inflate(R.layout.popup,null);
        itemName = (EditText) itemPopupView.findViewById(R.id.editItemName);
        saveBtn = (Button) itemPopupView.findViewById(R.id.saveBtn);

        dialogBuilder.setView(itemPopupView);
        dialog = dialogBuilder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.background_dark);


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setItemName(itemName.getText().toString());
                //add the crypto code to database
                CryptoItem cryptoItem = new CryptoItem(itemName.getText().toString());
                database = FirebaseDatabase.getInstance();
                myRef = database.getReference("Users").child(UID).child("CryptoTotal").child("CryptoList");
                myRef.child(itemName.getText().toString()).setValue(cryptoItem);
                cryptoArray.clear();
                dialog.cancel();
            }
        });
    }
    */

    private void viewInitialization(View v)
    {
        cryptoDetails = v.findViewById(R.id.cryptoDetailsCard);
        editTotalBuyValue = v.findViewById(R.id.editTotalBuyValue);
        editTotalCurrentValue = v.findViewById(R.id.editTotalCurrentValue);
        addBtn = v.findViewById(R.id.btnAdd);
        cryptosRecView = v.findViewById(R.id.cryptoRecView);
    }
    private void CryptoTopCardInitialization()
    {
        database = FirebaseDatabase.getInstance();//ROOT NODE
        myRef = database.getReference("Users");//USERS NODE

        DatabaseReference nameRef = myRef.child(String.valueOf(MainActivity.UID));//JERIEL NODE
        DatabaseReference cryptoTotalRef = nameRef.child("CryptoTotal");//CryptoTotal Node
        DatabaseReference totalBuyValueRef = cryptoTotalRef.child("totalCryptoBuyValue");//totalCryptoBuyValue
        DatabaseReference totalCurrentValueRef = cryptoTotalRef.child("totalCryptoCurrentValue");

        //UPDATE TOTAL BUYVALUE FROM FIREBASE
        totalBuyValueRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                double val = snapshot.getValue(Double.class);

                NumberFormat nf = NumberFormat.getInstance(Locale.US);
                nf.setMinimumFractionDigits(2); // <- the trick is here
                String formatVal = nf.format(val); // <- 1,000.00
                editTotalBuyValue.setText(formatVal);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        //UPDATE TOTAL CURRENT VALUE FROM FIREBASE
        totalCurrentValueRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                double val = snapshot.getValue(Double.class);

                NumberFormat nf = NumberFormat.getInstance(Locale.US);
                nf.setMinimumFractionDigits(2); // <- the trick is here
                String formatVal = nf.format(val); // <- 1,000.00

                editTotalCurrentValue.setText(formatVal);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    private void refreshCryptoRecView()
    {
        //RECYCLER VIEW FIREBASE
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users").child(UID).child("CryptoTotal");
        //myRef = database.getReference("Users").child("X0Ij0PseJXSIKu0uRfRroBLSWgg1").child("CryptoTotal");
        DatabaseReference cryptoListRef = myRef.child("CryptoList");
        cryptoListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        CryptoItem cryptoItem = dataSnapshot.getValue(CryptoItem.class);
                        cryptoArray.add(cryptoItem);
                    }
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getCurrencyData(){
        //loadingPB.setVisibility(View.VISIBLE);
        String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //json object
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //loadingPB.setVisibility(View.GONE);
                try {
                    JSONArray dataArray = response.getJSONArray("data");
                    for (int i=0;i<dataArray.length(); i++){
                        JSONObject dataObj = dataArray.getJSONObject(i);
                        String name = dataObj.getString("name");
                        String symbol = dataObj.getString("symbol");

                        JSONObject quote = dataObj.getJSONObject("quote");
                        JSONObject USD = quote.getJSONObject("USD");
                        double price = USD.getDouble("price");

                        cryptoModelsArrayList.add(new CryptoModel(name, symbol, price));
                    }
                    adapter.notifyDataSetChanged();
                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed to extract json data", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //loadingPB.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Failed to get data", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("X-CMC_PRO_API_KEY","af47668c-30ad-42e4-9461-7cf302924267");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }



}
