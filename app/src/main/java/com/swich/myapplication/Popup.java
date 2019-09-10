package com.swich.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.provider.CallLog;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Popup extends Activity {
    private Button btnY;
    private  Button btnN;
    String active;
    Spinner spin;
    List<String> phs=new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup);

        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width=dm.widthPixels;
        int hight=dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(hight*.4));
        btnY=findViewById(R.id.yes);
        spin = (Spinner) findViewById(R.id.list);
        btnN=findViewById(R.id.no);

        SharedPreferences prefs = getSharedPreferences("swich_info", MODE_PRIVATE);
        if(prefs.getInt("count",0)==0){
            finish();
        }
        active=prefs.getString("ph1","");

        if(prefs.getInt("count",0)==1)
        {
            phs.add(prefs.getString("ph1",""));

        }else{
            phs.add(prefs.getString("ph1",""));
            phs.add(prefs.getString("ph2",""));

        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, phs);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);

        btnY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              active=String.valueOf(spin.getSelectedItem());
            Log.d("phone",active);

                finish();
                System.exit(0);
            }
        });


        btnN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

                System.exit(0);


            }
        });




    }

}
