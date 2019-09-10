package com.swich.myapplication;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdaptor extends RecyclerView.Adapter<RecyclerViewAdaptor.ViewHolder> {

    private static final String TAG = "RecyclerViewAdaptor";
    private ArrayList<String> mImageNames=new ArrayList<>();
    private ArrayList<String> mImages=new ArrayList<>();
    private ArrayList<String>  mType=new ArrayList<>();
    private ArrayList<String> mDuration=new ArrayList<>();
    private ArrayList<String>  mDate=new ArrayList<>();
    private  ArrayList<String> mPhone=new ArrayList<>();
    private Context mContex;

    public void getCallDetails(Context mContext) {

        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
            Cursor managedCursor = mContext.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " DESC");
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
                String strDate = sf.format(callDayTime);


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
                mImages.add("https://upload.wikimedia.org/wikipedia/commons/b/b7/Google_Contacts_logo.png");
                mImageNames.add(phNumber);
                mType.add(dir);
                mDate.add(strDate);

                long longVal = Long.parseLong(callDuration);
                int hours = (int) longVal / 3600;
                int remainder = (int) longVal - hours * 3600;
                int mins = remainder / 60;
                remainder = remainder - mins * 60;
                int secs = remainder;
                if (hours > 0) {
                    callDuration = hours + "hr " + mins + "min " + secs + "sec";
                } else if (mins > 0) {
                    callDuration = mins + "min " + secs + "sec";
                } else {
                    callDuration = secs + "sec";
                }
                mDuration.add(callDuration);

            }
        }
    }





public  RecyclerViewAdaptor(Context mContex)
{
this.mContex=mContex;
getCallDetails(mContex);
}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem,parent,false);
        ViewHolder holder=new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG,"onBinViewHolder called...");
        Glide.with(mContex)
                .asBitmap()
                .load(mImages.get(position))
                .into(holder.image);
        String name=getContactName(mImageNames.get(position),mContex);
        if(!name.equals("")) {
            holder.imageName.setText(name);
        }else{
            holder.imageName.setText(mImageNames.get(position));
        }

        holder.duration.setText(mDuration.get(position));

        if(mType.get(position)=="MISSED"){

      holder.type.setTextColor(Color.RED);
        }else if(mType.get(position)=="INCOMING"){

            holder.type.setTextColor(Color.BLUE);
        }else{

        }

        holder.type.setText(mType.get(position));
        holder.date.setText(mDate.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"oncliked ok"+mImageNames.get(position)  );
                String dial = "tel:" + mImageNames.get(position);
                mContex.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
            }
        });
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

    @Override
    public int getItemCount() {
        return mImageNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView image;
        TextView imageName;
        TextView type;
        TextView duration;
        TextView date;

        RelativeLayout parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.image);
            imageName=itemView.findViewById(R.id.image_text);
            parentLayout=itemView.findViewById(R.id.parent_layout);
            type=itemView.findViewById(R.id.type);
            duration=itemView.findViewById(R.id.dur);
            date=itemView.findViewById(R.id.date);

        }


    }



}


