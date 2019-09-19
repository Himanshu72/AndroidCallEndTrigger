package com.swich.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Popup extends Activity {
    private Button btnY;
    private  Button btnN;
    String active1,active2;
    String dur,date;
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


        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        if(b!=null)
        {
            active2 =(String) b.get("No1");
            dur=(String) b.get("du") ;
            date=(String) b.get("date");
        }


        SharedPreferences prefs = getSharedPreferences("swich_info", MODE_PRIVATE);
        if(prefs.getInt("count",0)==0){
            finish();
        }


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

              active1=String.valueOf(spin.getSelectedItem());
              Long timestamp=Long.valueOf(date)+Long.valueOf(dur)  ;
                SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat sf2=new SimpleDateFormat("HH:mm:ssr    ");

                Date callDayTime = new Date(timestamp);
                String strDate = sf.format(callDayTime);
                String strTime=sf2.format(timestamp);



            Log.d("himanshu","+91"+active1);
            Log.d("himanshu",active2);
            Log.d("himanshu",strDate);
            Log.d("himanshu",strTime);


                // Log.d("himanshu",dur);

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
