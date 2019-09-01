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
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.util.Log;
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
        Manifest.permission.READ_CONTACTS,
            
};
private static final int PERMISSINOS_STATE=1240;

    private static final String TAG = "MainActivity";
    private ArrayList<String> mNames=new ArrayList<>();
    private ArrayList<String> mImageUrls=new ArrayList<>();
    private ArrayList<String> mtype=new ArrayList<>();
    private ArrayList<String> mdate=new ArrayList<>();
    private ArrayList<String> mduration=new ArrayList<>();


@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

if(checkAndRequestPermissions()){


    //textView = (TextView) findViewById(R.id.textview_call);
    getCallDetails();
    //initImageBitmaps();

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
        RecyclerViewAdaptor adaptor=new RecyclerViewAdaptor(this,mNames,mImageUrls,mtype,mduration,mdate);
        recyler.setAdapter(adaptor);
        recyler.setLayoutManager(new LinearLayoutManager(this));

    }

    private void getCallDetails() {
        StringBuffer sb = new StringBuffer();
        Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " DESC");
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
       // sb.append("Call Log :");
        while (managedCursor.moveToNext()) {
            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
            Date callDayTime = new Date(Long.valueOf(callDate));
            String strDate=sf.format(callDayTime);


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
            mImageUrls.add("https://upload.wikimedia.org/wikipedia/commons/b/b7/Google_Contacts_logo.png");
            String name=getContactName(phNumber,this);
            if(!name.equals("")) {

                mNames.add(name);
            }else{
                mNames.add(phNumber);
            }

            mtype.add(dir);
            mdate.add(strDate);

            long longVal=Long.parseLong(callDuration);
            int hours = (int) longVal / 3600;
            int remainder = (int) longVal - hours * 3600;
            int mins = remainder / 60;
            remainder = remainder - mins * 60;
            int secs = remainder;
            if(hours > 0) {
                callDuration = hours + "hr " + mins + "min " + secs + "sec";
            }else if(mins >0){
                callDuration=mins+"min "+secs+"sec";
            }else{
                callDuration=secs+"sec";
            }
            mduration.add(callDuration);

        }
        initRecyclerView();
    }

    public String getContactName(final String phoneNumber, Context context)
    {
        Uri uri=Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,Uri.encode(phoneNumber));

        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};

        String contactName="";
        Cursor cursor=context.getContentResolver().query(uri,projection,null,null,null);

        if (cursor != null) {
            if(cursor.moveToFirst()) {
                contactName=cursor.getString(0);
            }
            cursor.close();
        }

        return contactName;
    }
}
