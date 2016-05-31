package com.dheeru.app.news.service;

import android.util.Log;

import com.dheeru.app.news.activity.SearchActivity;
import com.dheeru.app.news.util.CommonUtilty;
import com.dheeru.app.news.util.SearchJsonHttpResHandler;
import com.dheeru.app.news.util.StingConstants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;

import java.util.HashMap;

/**
 * Created by dkthaku on 5/27/16.
 */
public class SearchService {

    static final String TAG = SearchService.class.getSimpleName();
    static private final SearchService searchService= new SearchService();
    CommonUtilty commonUtilty;

    public SearchService() {
        this.commonUtilty = CommonUtilty.getCommonUtilty();
    }

    public Object fetchNewsArticles(HashMap map){
     if(map==null) {
         Log.d(TAG, "fetchNewArticles: map is null !!!!!! ");
         return null;
     }
        JSONArray resultsObj=null;
       String searchQuery = (String) map.get(StingConstants.QUERY);
        SearchActivity searchAC= (SearchActivity) map.get(StingConstants.SEARCH_ACTIVITY_OBJECT);
        Log.d(TAG, "fetchNewsArticles: query == "+searchQuery);
        AsyncHttpClient asyncHttpClient= commonUtilty.getAsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put(StingConstants.QUERY, searchQuery);
        params.put(StingConstants.APP_KEY, StingConstants.NY_Key);
        SearchJsonHttpResHandler searchJsonRepHandler = new SearchJsonHttpResHandler();
        asyncHttpClient.get(StingConstants.url, params,  searchJsonRepHandler );
        Log.d(TAG, "fetchNewsArticles: searchJsonRepHandler "+searchJsonRepHandler);
        if (searchJsonRepHandler!=null && searchJsonRepHandler.getResultMap()!=null) {
            resultsObj = (JSONArray) searchJsonRepHandler.getResultMap().get(StingConstants.ARTICLE_RESULT_JSONArray);
            Log.d(TAG, "fetchNewArticles: resultsObj " + resultsObj);
        }
        // final results
        return resultsObj;
    }

    public static SearchService getSearchService(){
        return searchService;

    }


}
