package com.roundbytes.myportfolio.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.roundbytes.myportfolio.R;

public class ActivityDonate extends AppCompatActivity {

    private Button btnBtc, btnEth, btnXrp, btnDoge, btnRvn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        btnBtc = findViewById(R.id.btnBtc);
        btnEth = findViewById(R.id.btnEth);
        btnXrp = findViewById(R.id.btnXrp);
        btnDoge = findViewById(R.id.btnDoge);
        btnRvn = findViewById(R.id.btnRvn);

        btnBtc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("addr", "1MkCzoY12bcfN11xM2bAmat7577wUJA37d");
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "BTC Address Copied", Toast.LENGTH_SHORT).show();
            }
        });

        btnEth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("addr", "0x4553B379e75c8C7879D2221D71B99DA2a3fD8B2E");
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "ETH Address Copied", Toast.LENGTH_SHORT).show();
            }
        });

        btnXrp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("addr", "r4vRuNUEKLsunCcchczLgmZugZvUuMcbE6");
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "XRP Address Copied", Toast.LENGTH_SHORT).show();
            }
        });

        btnDoge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("addr", "DTSXdU7TSk43MXSshTbk1UwhBn2Evzw8cz");
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "DOGE Address Copied", Toast.LENGTH_SHORT).show();
            }
        });

        btnRvn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("addr", "DTSXdU7TSk43MXSshTbk1UwhBn2Evzw8cz");
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "RVN Address Copied", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
