package com.roundbytes.myportfolio.menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.roundbytes.myportfolio.adapter.PriceAlertRecViewAdapter;
import com.roundbytes.myportfolio.crypto.CryptoModel;
import com.roundbytes.myportfolio.crypto.PriceAlertItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PriceAlertActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText priceET;
    private Spinner spinner;
    private Button btnConfirm;
    private RecyclerView priceAlertRecView;
    private PriceAlertRecViewAdapter recViewAdapter;
    private Handler handler;
    private boolean stop = false;

    //notifications
    NotificationManager notificationManager;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private final static String default_notification_channel_id = "default";

    public static final String CHANNEL_1_ID = "channel1";
    public static final String CHANNEL_2_ID = "channel2";


    ArrayList<String> cryptoSymbols = new ArrayList<>();
    ArrayList<PriceAlertItem> priceAlertItems = new ArrayList<>();
    private ArrayList<CryptoModel> cryptoModelsArrayList = new ArrayList<>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_alert);

        priceET = findViewById(R.id.priceET);
        spinner = findViewById(R.id.spinner);
        btnConfirm = findViewById(R.id.btnConfirm);
        priceAlertRecView = findViewById(R.id.priceAlertRecView);

        handler = new Handler();
        final Runnable r1 = new Runnable() {
            public void run() {
                getCryptoMarketData();
                handler.postDelayed(this, 30000);
            }
        };
        handler.postDelayed(r1, 1000);


        //notificaiton
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();

        cryptoSymbols = MainActivity.getCryptoSymbols();

        //spinner
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, cryptoSymbols);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        //recvyler view
        recViewAdapter = new PriceAlertRecViewAdapter(this);
        recViewAdapter.setPriceAlertItemArrayList(priceAlertItems);
        priceAlertRecView.setAdapter(recViewAdapter);
        priceAlertRecView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String symbol = spinner.getSelectedItem().toString();
                double price = Double.parseDouble(String.valueOf(priceET.getText()));
                priceAlertItems.add(new PriceAlertItem(symbol, price));

                recViewAdapter.setPriceAlertItemArrayList(priceAlertItems);
//                Toast.makeText(PriceAlertActivity.this, "price alert added", Toast.LENGTH_SHORT).show();
                //checkPrice();
            }
        });

        handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                checkPrice();
                if (!stop)
                    handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(r, 1000);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//        Toast.makeText(this, adapterView.getItemAtPosition(position).toString()+" clicked", Toast.LENGTH_SHORT).show();
//        selectedSymbol = adapterView.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void checkPrice() {
        for (int i = 0; i < priceAlertItems.size(); i++) {
            for (int j = 0; j < cryptoModelsArrayList.size(); j++) {
                if (priceAlertItems.get(i).getSymbol().equalsIgnoreCase(cryptoModelsArrayList.get(j).getSymbol())) {

                    double itemPrice = Math.round(priceAlertItems.get(i).getPrice());
                    double modelPrice = Math.round(cryptoModelsArrayList.get(j).getPrice());
                    Log.d("PRICE", itemPrice + " and " + modelPrice);
                    if (itemPrice >= modelPrice) {
                        Toast.makeText(this, "reached", Toast.LENGTH_SHORT).show();
                        sendNotification();
                        stop = true;

                        //sendNotification();
                    } else {
                        Toast.makeText(this, "no yet reached", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private void sendNotification() {
        //notification
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(PriceAlertActivity.this,
                default_notification_channel_id)
                .setSmallIcon(R.drawable.ic_notification)
                .setColor(Color.BLUE)
                .setContentTitle("Price Alert!")
                .setContentText("Price is reached");

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel notificationChannel = new
                    NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            //notificationChannel.setSound(sound , audioAttributes) ;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(notificationChannel);

            assert mNotificationManager != null;
            mNotificationManager.notify((int) System.currentTimeMillis(), mBuilder.build());
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_1_ID, "Channel 1", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("This is channel 1");

            NotificationChannel channel2 = new NotificationChannel(CHANNEL_2_ID, "Channel 2", NotificationManager.IMPORTANCE_LOW);
            channel2.setDescription("This is channel 2");


            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            notificationManager.createNotificationChannel(channel2);
        }
    }

    private void getCryptoMarketData(){
        //loadingPB.setVisibility(View.VISIBLE);
        cryptoModelsArrayList.clear();
        String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        //json object
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //loadingPB.setVisibility(View.GONE);
                try {
                    JSONObject status = response.getJSONObject("status");
                    int elapsed = status.getInt("elapsed");
                    Log.d("elapsed", elapsed+";");

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
                    Log.d("TAG", cryptoModelsArrayList.get(0).getPrice()+" ");
                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Failed to extract json data", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //loadingPB.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Failed to get market data", Toast.LENGTH_SHORT).show();
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