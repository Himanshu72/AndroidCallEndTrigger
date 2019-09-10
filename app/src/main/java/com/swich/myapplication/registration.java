package com.swich.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class registration extends AppCompatActivity {
    TextView error,sim1,sim2;
    EditText ph1,ph2;
    LinearLayout g2;
    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        error=findViewById(R.id.error);
        error.setVisibility(View.INVISIBLE);
        sim1=findViewById(R.id.ph1t);
        sim2=findViewById(R.id.ph2t);
        ph1=findViewById(R.id.ph1);
        ph2=findViewById(R.id.ph2);
        g2=findViewById(R.id.group2);
        b1=findViewById(R.id.sub);
        TelephonyInfo tel=TelephonyInfo.getInstance(this);
        final Intent main=new Intent(this,MainActivity.class);
       final SharedPreferences.Editor editor = getSharedPreferences("swich_info",MODE_PRIVATE).edit();
        SharedPreferences prefs = getSharedPreferences("swich_info", MODE_PRIVATE);
        if(prefs.getInt("count",0)!=0){
            startActivity(main);
        }

        if(tel.isSIM2Ready() && tel.isDualSIM()){

            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String p1=ph1.getText().toString();
                    String p2=ph2.getText().toString();
                    if(!p1.isEmpty()&& !p2.isEmpty() && p1.length()==10 && p2.length()==10){

                        editor.putInt("count", 2);
                        editor.putString("ph1", p1);
                        editor.putString("ph2", p2);
                        editor.apply();
                        startActivity(main);
                        finish();

                    }else{
                        error.setText("don't use +91 and please fill all field");
                        error.setVisibility(View.VISIBLE);
                    }

                }
            });


        }else{
            g2.setVisibility(View.GONE);
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String p1=ph1.getText().toString();

                    if(!p1.isEmpty() && p1.length()==10){

                        editor.putInt("count", 1);
                        editor.putString("ph1", p1);
                        editor.apply();
                        startActivity(main);
                        finish();

                    }else{
                        error.setText("don't use +91 and please fill all field");
                        error.setVisibility(View.VISIBLE);
                    }

                }
            });


        }


    }
}
