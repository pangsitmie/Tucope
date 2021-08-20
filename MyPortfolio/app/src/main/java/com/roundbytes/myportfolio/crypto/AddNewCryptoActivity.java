package com.roundbytes.myportfolio.crypto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.roundbytes.myportfolio.MainActivity;
import com.roundbytes.myportfolio.R;
import com.roundbytes.myportfolio.adapter.AddNewCryptoRecViewAdapter;
import com.roundbytes.myportfolio.fragment.CryptoFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddNewCryptoActivity extends AppCompatActivity {
    private EditText searchEdit;
    private RecyclerView currenciesRecView;
    private ProgressBar loadingPB;

    private ArrayList<CryptoModel> cryptoModelsArrayList;
    private AddNewCryptoRecViewAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_crypto);

        //initialize view
        searchEdit = findViewById(R.id.idEditSearch);
        currenciesRecView = findViewById(R.id.idRVCurrencies);
        loadingPB = findViewById(R.id.idPBLoading);

        cryptoModelsArrayList = new ArrayList<>();
        adapter = new AddNewCryptoRecViewAdapter(cryptoModelsArrayList,this);

        currenciesRecView.setLayoutManager(new LinearLayoutManager(this));
        currenciesRecView.setAdapter(adapter);


        //get currency data
        getCurrencyData();

        //filter list
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterCurrencies(s.toString());
            }
        });
    }

    //VOIDS

    //serach coin filter
    private void filterCurrencies(String currency) {
        ArrayList<CryptoModel> filteredList = new ArrayList<>();
        for (CryptoModel item : cryptoModelsArrayList) {
            if (item.getSymbol().toLowerCase().contains(currency.toLowerCase())) {
                filteredList.add(item);
            }
            else
            {
                adapter.filterList(filteredList);
            }
        }
    }

    private void getCurrencyData(){
        loadingPB.setVisibility(View.VISIBLE);
        String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //json object
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingPB.setVisibility(View.GONE);
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
                    Toast.makeText(getApplicationContext(), "Failed to extract json data", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingPB.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Failed to get data", Toast.LENGTH_SHORT).show();
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