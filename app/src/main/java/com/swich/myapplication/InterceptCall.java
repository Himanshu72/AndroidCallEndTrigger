
package com.swich.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CallLog;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import static android.content.Context.MODE_PRIVATE;


public class InterceptCall extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        String state=intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        final TelephonyManager tMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        SharedPreferences prefs = context.getSharedPreferences("swich_info", MODE_PRIVATE);
            if(prefs.getInt("count",0)==0){
                System.exit(0);
            }

            TelephonyInfo tel=TelephonyInfo.getInstance(context);
           // Log.d("Himanshu sim",String.valueOf(tel.getImeiSIM1()));
        Bundle bundle = intent.getExtras();
         String callingSIM =String.valueOf(bundle.getInt("simId", -1));
         if(callingSIM=="0"){

             Toast.makeText(context,"1",Toast.LENGTH_LONG).show();
         }else if(callingSIM=="1"){

             Toast.makeText(context,"2",Toast.LENGTH_LONG).show();

         }

            if (ContextCompat.checkSelfPermission(context,Manifest.permission.READ_PHONE_STATE)==PackageManager.PERMISSION_GRANTED) {

           String getSimSerialNumber = tMgr.getSimOperatorName();

      //  Toast.makeText(context,getSimSerialNumber,Toast.LENGTH_LONG).show();

          }


        try{
               if(state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_IDLE)) {
                   //Toast.makeText(context, "phone Ended", Toast.LENGTH_LONG).show();
                   //initImageBitmaps()

                   final Intent intents = new Intent(context, Popup.class);
                   final Handler handler = new Handler();
                   final String[] ph1 = {null};
                   final String[] ph2 = {null};
                   final double[] d1 = {0};
                   final double[] d2 = {0};
                   final int[] callD = {0};



                   handler.postDelayed(new Runnable() {
                           @Override
                           public void run() {
                               if(ContextCompat.checkSelfPermission(context,Manifest.permission.READ_CALL_LOG)==PackageManager.PERMISSION_GRANTED) {

                               Cursor managedCursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " DESC");
                               int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
                               int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
                               int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);


                               int count=0;
                               while (managedCursor.moveToNext()) {
                                   count++;
                                    if(count==1) {
                                        ph1[0] = managedCursor.getString(number);

                                        d1[0]= managedCursor.getDouble(date);
                                        callD[0] = managedCursor.getInt(duration);


                                    }



                                   if(count==2){
                                    ph2[0] =managedCursor.getString(number);
                                       d2[0]= managedCursor.getDouble(date);
                                       break;
                                 }


                               }


                                   if(ph1[0].equals(ph2[0]) && ph1[0]!=null ){
                             //          Toast.makeText(context,String.valueOf(( ((d1[0]-d2[0])/1000)-callD[0]) ),Toast.LENGTH_LONG).show();
                                       if( ( ((d1[0]-d2[0])/1000)-callD[0])<=31  && d1[0]!=0) {
                                           intents.putExtra("No1",ph1[0] );

                                           intents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                           intents.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                           context.startActivity(intents);

                                       }

                                   }



                               }
                           }
                       }, 1000);




//                    if(ph1.length() >0 && ph1.equals(ph2)){

//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//
//
//
//                            }
//                        }, 1000);








               }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
