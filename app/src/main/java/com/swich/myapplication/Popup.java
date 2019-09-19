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
import android.os.Handler;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    DatabaseReference reff;
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

        reff= FirebaseDatabase.getInstance().getReference().child("Member");
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
        final CallDrop calldrop=new CallDrop();
        final Handler handler = new Handler();

        btnY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Log.d("here",active2);
                calldrop.setPersonPH(active2);
                active1=String.valueOf(spin.getSelectedItem());
                Log.d("here",active1);
                calldrop.setHostPH("+91"+active1);
                Long timestamp=Long.valueOf(date)+Long.valueOf(dur);
                SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
                Date callDayTime = new Date(timestamp);
                String strDate = sf.format(callDayTime);
                Log.d("here",strDate);
                calldrop.setDate(strDate);
                SimpleDateFormat sf2=new SimpleDateFormat("HH:mm:ss");
                String strDur = sf2.format(callDayTime);
                Log.d("here",strDur);
                calldrop.setTime(strDur);



                reff.push().setValue(calldrop);

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
