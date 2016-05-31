package com.dheeru.app.news.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;

import java.io.IOException;

/**
 * Created by dkthaku on 5/27/16.
 */
public class CommonUtilty {


   private final  String CommonUtilty = CommonUtilty.class.getSimpleName();

     private AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    private  static final CommonUtilty commonUtilty=new CommonUtilty();

    public  AsyncHttpClient getAsyncHttpClient() {
        if(asyncHttpClient==null){
            asyncHttpClient= new AsyncHttpClient();
        }
        return asyncHttpClient;
    }

    static public CommonUtilty getCommonUtilty() {
        return commonUtilty;
    }

    public Boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        Log.d(this.getClass().getSimpleName(), "isNetworkAvailable: activeNetworkInfo "+activeNetworkInfo);
        if(activeNetworkInfo!=null)
        Log.d(this.getClass().getSimpleName(), "isNetworkAvailable: activeNetworkInfo.isConnected() "+activeNetworkInfo.isConnected());
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            Log.d(this.getClass().getSimpleName(), "isOnline: exitValue"+exitValue);
            return (exitValue == 0);
        } catch (InterruptedException | IOException e) { e.printStackTrace(); }
        return false;
    }


}
