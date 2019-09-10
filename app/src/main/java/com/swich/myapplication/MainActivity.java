package com.swich.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import java.security.Permission;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextView textView = null;
    String[] appPermissions={
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.READ_CALL_LOG,
        Manifest.permission.READ_CONTACTS
};
private static final int PERMISSINOS_STATE=1240;

    private static final String TAG = "MainActivity";


@Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
    getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_main);

if(checkAndRequestPermissions()){


    //textView = (TextView) findViewById(R.id.textview_call);
    //getCallDetails();
    initRecyclerView();
    //initImageBitmaps();
    final Intent intents = new Intent(this, Popup.class);


}

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(PERMISSINOS_STATE==requestCode){
            HashMap<String,Integer> PermissionResult=new HashMap<>();
            int deniedCount=0;
            for(int i=0;i<grantResults.length;i++){
                    if(grantResults[i]==PackageManager.PERMISSION_DENIED){
                        PermissionResult.put(permissions[i],grantResults[i]);
                        deniedCount++;
                    }
                    if(deniedCount==0){
                        recreate();
                    }else{
                        for(Map.Entry<String,Integer> entry:PermissionResult.entrySet()){
                            String perName=entry.getKey();
                            int perValue=entry.getValue();
                            if(ActivityCompat.shouldShowRequestPermissionRationale(this,perName)){

                                new  AlertDialog.Builder(this)
                                        .setIcon(android.R.drawable.ic_dialog_alert).
                                        setTitle("need Permission?").
                                        setMessage("this app need to read your call logs and call status plz give permissions")
                                        .setPositiveButton("yes,Grant", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                checkAndRequestPermissions();

                                            }
                                        }).setNegativeButton("no,Denie", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                }).show();
                            }
                            }
                        }
                    }
            }
        }

   public boolean checkAndRequestPermissions(){
       List<String> listPermissionNeeded=new ArrayList<>();
       for(String per : appPermissions){
           if(ContextCompat.checkSelfPermission(this,per)!=PackageManager.PERMISSION_GRANTED){
               listPermissionNeeded.add(per);
           }
       }

       if(!listPermissionNeeded.isEmpty()){
           ActivityCompat.requestPermissions(this,
                   listPermissionNeeded.toArray(new String[listPermissionNeeded.size()]),PERMISSINOS_STATE);

           return false;
       }
        return true;
    }



    private  void  initRecyclerView(){
        RecyclerView recyler=findViewById(R.id.recycler_view);
        RecyclerViewAdaptor adaptor=new RecyclerViewAdaptor(this);
        recyler.setAdapter(adaptor);
        recyler.setLayoutManager(new LinearLayoutManager(this));

    }



}
