
package com.swich.myapplication;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class InterceptCall extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        String state=intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        try{
               if(state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_IDLE)) {
                   Toast.makeText(context, "phone Ended", Toast.LENGTH_LONG).show();
                   new AlertDialog.Builder(context.getApplicationContext())
                           .setIcon(android.R.drawable.ic_dialog_alert).
                           setTitle("call Drop?").
                           setMessage("is this a call drop")
                           .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   Toast.makeText(context.getApplicationContext(),"yes",Toast.LENGTH_SHORT).show();
                               }
                           }).setNegativeButton("no",new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           Toast.makeText(context.getApplicationContext(),"no",Toast.LENGTH_SHORT).show();
                       }
                   }).show();



               }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
