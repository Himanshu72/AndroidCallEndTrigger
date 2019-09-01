
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
                   //Toast.makeText(context, "phone Ended", Toast.LENGTH_LONG).show();
                   //initImageBitmaps()
                   final Intent intents = new Intent(context, Popup.class);

                   intents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   intents.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                   context.startActivity(intents);

               }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
