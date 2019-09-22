package com.swich.myapplication;

public class CallDrop {
// Log.d("himanshu","+91"+active1);
//            Log.d("himanshu",active2);
//            Log.d("himanshu",strDate);
//            Log.d("himanshu",strTime);

private String Host_Phone_Number;
private  String Other_Phone_Number;
private  String Call_Drop_Date;
private  String Call_Drop_Time;
CallDrop(){

}

    public String getHost_Phone_Number() {
        return Host_Phone_Number;
    }

    public void setHost_Phone_Number(String host_Phone_Number) {
        Host_Phone_Number = host_Phone_Number;
    }

    public String getOther_Phone_Number() {
        return Other_Phone_Number;
    }

    public void setOther_Phone_Number(String other_Phone_Number) {
        Other_Phone_Number = other_Phone_Number;
    }

    public String getCall_Drop_Date() {
        return Call_Drop_Date;
    }

    public void setCall_Drop_Date(String call_Drop_Date) {
        Call_Drop_Date = call_Drop_Date;
    }

    public String getCall_Drop_Time() {
        return Call_Drop_Time;
    }

    public void setCall_Drop_Time(String call_Drop_Time) {
        Call_Drop_Time = call_Drop_Time;
    }
}
