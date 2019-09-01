package com.swich.myapplication;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

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

    public RecyclerViewAdaptor( Context mContex,ArrayList<String> mImageNames, ArrayList<String> mImages,ArrayList<String> mType,ArrayList<String> mDuration,ArrayList<String> mDate ) {
        this.mImageNames = mImageNames;
        this.mImages = mImages;
        this.mContex = mContex;
        this.mType=mType;
        this.mDate=mDate;
        this.mDuration=mDuration;

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


