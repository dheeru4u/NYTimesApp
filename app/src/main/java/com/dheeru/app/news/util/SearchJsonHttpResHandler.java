package com.dheeru.app.news.util;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

/**
 * Created by dkthaku on 5/27/16.
 */
public class SearchJsonHttpResHandler extends JsonHttpResponseHandler {
    private HashMap mResultMap;
    static final String TAG = SearchJsonHttpResHandler.class.getSimpleName();


    public HashMap getResultMap() {
        return mResultMap;
    }

    public void setResultMap(JSONArray pResultMap) {
        this.mResultMap = mResultMap;
    }
    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        // Root JSON in response is an dictionary i.e { "data : [ ... ] }
        Log.d(TAG, "onSuccess: response "+response.toString());
        mResultMap=new HashMap();
        mResultMap.put(StingConstants.RESPONSE, response);
        JSONArray results=null;
        try {
            // Handle resulting parsed JSON response here
            results =(JSONArray) response.getJSONObject("response").getJSONArray("docs");
            if (results!=null){
                mResultMap.put(StingConstants.ARTICLE_RESULT_JSONArray, results);

            }

            Log.d(TAG, "onSuccess: results == "+results);
        }catch (Exception e ){
            e.printStackTrace();
        }

    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
        Log.d(TAG, "onFailure: statusCode "+statusCode);
        Log.d(TAG, "onFailure: headers "+headers);
        Log.d(TAG, "onFailure: res "+res);

    }

    @Override
    public String toString() {
        return "SearchJsonHttpResHandler{" +
                "mResultMap=" + mResultMap +
                '}';
    }
}
