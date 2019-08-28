package com.swich.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.widget.TextView;
import android.widget.Toast;

import java.security.Permission;
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
        Manifest.permission.READ_CONTACTS,
            
};
private static final int PERMISSINOS_STATE=1240;

@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

if(checkAndRequestPermissions()){


    textView = (TextView) findViewById(R.id.textview_call);
    getCallDetails();


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
                        //cool
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

    private void getCallDetails() {
        StringBuffer sb = new StringBuffer();
        Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null, null, null, null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        sb.append("Call Log :");
        while (managedCursor.moveToNext()) {
            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString(duration);
            String dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
            }
            sb.append("\nPhone Number:--- " + phNumber + " \nCall Type:--- " + dir + " \nCall Date:--- " + callDayTime + " \nCall duration in sec :--- " + callDuration);
            sb.append("\n----------------------------------");
        } //managedCursor.close();
        textView.setText(sb);
    }
}
