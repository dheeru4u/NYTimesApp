package com.dheeru.app.news.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.dheeru.app.news.R;
import com.dheeru.app.news.adapter.ArticlesRecyclerViewAdapter;
import com.dheeru.app.news.listner.ArticlesEndlessRvScrollListener;
import com.dheeru.app.news.modal.Article;
import com.dheeru.app.news.util.CommonUtilty;
import com.dheeru.app.news.util.StingConstants;
import com.dheeru.app.news.view.MarginDecoration;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    EditText etQuery;
    Button btnSearch;
    GridView gvResults;

   @Bind(R.id.wvArticle)
    RecyclerView rvArticles;
    String searchQuery;
    String queryForScrolldown;
    JSONArray mArticles;
    ArrayList<Article> articles;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    boolean mIsFilterQuery;
    ArticlesRecyclerViewAdapter mArticlesRecyclerViewAdapter;
    String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
    static String TAG = SearchActivity.class.getSimpleName();

    int page = -1;

    public enum SortOrder {
        NEWEST("newest"), OLDEST("oldest");
        private String sortOrder;
        SortOrder(String sortOrder) {
            this.sortOrder = sortOrder;
        }
        public String getSortOrder() {
            return sortOrder;
        }
    }

    public enum NewsDesk {
        ARTS("Arts"), FASHION_AND_STYLE("Fashion & Style"), SPORTS("Sports");
        private String newsDesk;
        NewsDesk(String newsDesk) {
            this.newsDesk = newsDesk;
        }
        public String getNewsDesk() {
            return newsDesk;
        }
    }

    public static class FilterAttributes {
        //YYYYMMDD
        static String beginDate = "";
        static String beginDateDisplay = "";
        static SortOrder sortOrder = SortOrder.NEWEST;
        static List<NewsDesk> newsDesks = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
         toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setUpView();
    }

    public void setUpView() {
        articles = new ArrayList<Article>();
        etQuery = (EditText) findViewById(R.id.etQuery);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        //gvResults=(GridView)findViewById(R.id.rvArticles);
        rvArticles = (RecyclerView) findViewById(R.id.rvArticles);
        rvArticles.addItemDecoration(new MarginDecoration(this));
        rvArticles.setHasFixedSize(true);
        articles = new ArrayList<Article>();
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        // GridLayoutManager gridLayoutManager = new GridLayoutManager(getBaseContext(), 5);
        gridLayoutManager.scrollToPosition(5);
        Log.d(TAG, "setUpView: rvArticles.hasFixedSize() " + rvArticles.hasFixedSize());
        rvArticles.addOnScrollListener(
                new ArticlesEndlessRvScrollListener(gridLayoutManager) {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount) {
                        // Triggered only when new data needs to be appended to the list
                        // Add whatever code is needed to append new items to the bottom of the list
                        customLoadMoreDataFromApi(page);
                        // return false;
                    }
                });

        rvArticles.setLayoutManager(gridLayoutManager);
        mArticlesRecyclerViewAdapter = new ArticlesRecyclerViewAdapter(this, articles);
        rvArticles.setAdapter(mArticlesRecyclerViewAdapter);
       // mArticlesRecyclerViewAdapter.notifyDataSetChanged();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case(R.id.action_search_filter):
                mIsFilterQuery=true;
                launchFilterDialog();
            case(R.id.action_settings):
                Toast.makeText(this, "action_settings", Toast.LENGTH_LONG);
            default:
                Toast.makeText(this, "default", Toast.LENGTH_LONG);
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void launchFilterDialog() {
        SearchFilterFragmentActivity searchFilterDialog = new SearchFilterFragmentActivity();
        FragmentManager fm = getSupportFragmentManager();
        searchFilterDialog.show(fm, "filter");
    }
    public void onArcticleSearch(View v) {

        searchQuery = this.etQuery.getText().toString();
        if (!searchQuery.isEmpty()) {
            queryForScrolldown = searchQuery;
            page = 1;
            articles.clear();
        }
        Toast.makeText(this, searchQuery, Toast.LENGTH_LONG).show();
        com.loopj.android.http.AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put(StingConstants.QUERY, searchQuery);
        params.put(StingConstants.NY_Key, StingConstants.APP_KEY);
        params.put(StingConstants.PAGE, page);
        asyncHttpClient.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Root JSON in response is an dictionary i.e { "data : [ ... ] }
                Log.d(TAG, "onSuccess: response " + response.toString());
                JSONArray results = null;
                String stringResult = null;
                try {
                    // Handle resulting parsed JSON response here
                    results = (JSONArray) response.getJSONObject("response").getJSONArray("docs");
                    Log.d(TAG, "onSuccess: results == " + results);
                    // papulate objects here
                    if (response != null) {
                        stringResult = response.toString();
                        Gson gson = new GsonBuilder().create();
                        JsonObject jsonObject = gson.fromJson(stringResult, JsonObject.class);
                        if (jsonObject.has(StingConstants.RESPONSE)) {
                            JsonObject jsonResponseObject = jsonObject.getAsJsonObject(StingConstants.RESPONSE);
                            if (jsonResponseObject != null) {
                                JsonArray jsonDocsArray = jsonResponseObject.getAsJsonArray(StingConstants.DOCS);
                                Type collectionType = new TypeToken<List<Article>>() {
                                }.getType();

                                List<Article> fetchedArticles = gson.fromJson(jsonDocsArray,
                                        collectionType);
                                articles.addAll(fetchedArticles);
                                mArticlesRecyclerViewAdapter.notifyDataSetChanged();
                                Log.i("SearchActivity", articles.size() + " articles found");
                                Log.d(TAG, "onSuccess: articles == \n " + articles);
                            }
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }
        });


        Toast.makeText(this, "loading " + articles.size() + ".. Arctiles", Toast.LENGTH_LONG);

    }

    public void customLoadMoreDataFromApi(int offset) {
        offset = offset % 100;
        Toast.makeText(this, "Loading more...", Toast.LENGTH_SHORT).show();
        int curSize = mArticlesRecyclerViewAdapter.getItemCount();
        onArcticleSearch(offset);
        mArticlesRecyclerViewAdapter.notifyItemRangeInserted(curSize, mArticlesRecyclerViewAdapter.getItemCount() - 1);
    }

    public void onArcticleSearch(int offset) {
        Log.d(TAG, "onArcticleSearch: queryForScrolldown " + queryForScrolldown+"  mIsFilterQuery == "+mIsFilterQuery);
        int resutlSize = articles.size();
        if ((!queryForScrolldown.isEmpty())) {
            ++page;
            Toast.makeText(this, "Searching for :"+queryForScrolldown, Toast.LENGTH_LONG).show();
            com.loopj.android.http.AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            RequestParams params =null;
            if(mIsFilterQuery){
                params= constructQueryRequestParams(queryForScrolldown, offset);
            }else {
                params=new RequestParams();
                params.put(StingConstants.QUERY, queryForScrolldown);
                params.put(StingConstants.NY_Key, StingConstants.APP_KEY);
                params.put(StingConstants.PAGE, offset);
            }
            Log.d(TAG, "onArcticleSearch:params "+params);
            asyncHttpClient.get(url, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // Root JSON in response is an dictionary i.e { "data : [ ... ] }
                    Log.d(TAG, "onSuccess: response " + response.toString());
                    JSONArray results = null;
                    String stringResult = null;
                    try {
                        // Handle resulting parsed JSON response here
                        results = (JSONArray) response.getJSONObject("response").getJSONArray("docs");
                        int beforeResult = articles.size();
                        Log.d(TAG, "onSuccess: results == " + results);
                        // papulate objects here
                        if (response != null) {
                            stringResult = response.toString();
                            Gson gson = new GsonBuilder().create();
                            JsonObject jsonObject = gson.fromJson(stringResult, JsonObject.class);
                            if (jsonObject.has(StingConstants.RESPONSE)) {
                                JsonObject jsonResponseObject = jsonObject.getAsJsonObject(StingConstants.RESPONSE);
                                if (jsonResponseObject != null) {
                                    JsonArray jsonDocsArray = jsonResponseObject.getAsJsonArray(StingConstants.DOCS);
                                    Type collectionType = new TypeToken<List<Article>>() {
                                    }.getType();
                                    List<Article> fetchedArticles = gson.fromJson(jsonDocsArray,
                                            collectionType);
                                    articles.addAll(fetchedArticles);
                                    mArticlesRecyclerViewAdapter.notifyDataSetChanged();
                                    Log.i("SearchActivity", articles.size() + " articles found");
                                    Log.d(TAG, "onSuccess: articles == \n " + articles);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    if (!CommonUtilty.getCommonUtilty().isNetworkAvailable(getBaseContext())) {
                        Toast.makeText(getApplicationContext(), "Opps looks like " +
                                        "network connectivity problem",
                                Toast.LENGTH_LONG).show();
                    }

                    if (!CommonUtilty.getCommonUtilty().isOnline()) {
                        Toast.makeText(getApplicationContext(), "Your device is not online, " +
                                        "check wifi and try again!",
                                Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[]headers, Throwable t, JSONObject json){
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    if (!CommonUtilty.getCommonUtilty().isNetworkAvailable(getBaseContext())) {
                        Toast.makeText(getApplicationContext(), "Opps looks like " +
                                        "network connectivity problem",
                                Toast.LENGTH_LONG).show();
                    }

                    if (!CommonUtilty.getCommonUtilty().isOnline()) {
                        Toast.makeText(getApplicationContext(), "Your device is not online, " +
                                        "check wifi and try again!",
                                Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onUserException(Throwable error) {
                    super.onUserException(error);
                    Toast.makeText(getApplicationContext(), "Your device is not online, " +
                                    "check wifi and try again!",
                            Toast.LENGTH_LONG).show();
                }


                public void onFailure(Throwable t, JSONObject j){
                    Toast.makeText(getApplicationContext(), "Your device is not online, " +
                                    "check wifi and try again!",
                            Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                   // mArticlesRecyclerViewAdapter.notifyDataSetChanged();
                    int curSize = mArticlesRecyclerViewAdapter.getItemCount();
                   // mArticlesRecyclerViewAdapter.notifyItemRangeInserted(curSize, mArticlesRecyclerViewAdapter.getItemCount() - 1);
                    Log.d(TAG, "onFinish: @@@@@@@@@@@@@@@ bfore "+curSize);
                }


            });
        }
        Log.d(TAG, "onArcticleSearch: $$$$$$$$$$$$$$$$$$$$ Before search "+resutlSize);
        int newResults = articles.size() - resutlSize;
        Log.d(TAG, "onArcticleSearch: $$$$$$$$$$$$$$$$$$$$ After search "+resutlSize);
        if (newResults > 0) {
            Toast.makeText(this, "loading .." + newResults + "..more", Toast.LENGTH_LONG);
        } else {
            Toast.makeText(this, "No more ..last total =" + newResults + ".try different keywords ", Toast.LENGTH_LONG);
        }
    }

    public void repeatSearch() {
        this.mIsFilterQuery=true;
        int total =articles.size();
        articles.clear();
        mArticlesRecyclerViewAdapter.notifyItemRangeRemoved(0, total);
        onArcticleSearch(1);
        mArticlesRecyclerViewAdapter.notifyDataSetChanged();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$:query "+query);
                int totItem=mArticlesRecyclerViewAdapter.getItemCount();
                mIsFilterQuery=false;
                if (!TextUtils.isEmpty(query)) {
                    queryForScrolldown = query;
                    page = 1;
                    articles.clear();
                    mArticlesRecyclerViewAdapter.notifyItemRangeRemoved(0, totItem);
                }
                onArcticleSearch(1);
                searchView.clearFocus();
                mArticlesRecyclerViewAdapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle("News Article Search");
    }

    public RequestParams constructQueryRequestParams(String searchText, int pageNumber) {
        RequestParams requestParams = new RequestParams();
        requestParams.put(StingConstants.APP_KEY, StingConstants.NY_Key);

        if (!TextUtils.isEmpty(FilterAttributes.beginDate)) {
            requestParams.put(StingConstants.NY_BEGIN_DATE, FilterAttributes.beginDate);
        }

        if (FilterAttributes.newsDesks.size() > 0) {
            StringBuilder newsDeskBuilder = new StringBuilder();
            newsDeskBuilder.append("news_desk:(");

            if (FilterAttributes.newsDesks.size() > 1) {
                for (int i = 0; i < FilterAttributes.newsDesks.size() - 1; i++) {
                    newsDeskBuilder.append(FilterAttributes.newsDesks.get(i).getNewsDesk());
                    newsDeskBuilder.append(" ");
                }
                newsDeskBuilder.append(FilterAttributes.newsDesks
                        .get(FilterAttributes.newsDesks.size() - 1).getNewsDesk());
            } else {
                newsDeskBuilder.append(FilterAttributes.newsDesks.get(0).getNewsDesk());
            }

            newsDeskBuilder.append(")");
            requestParams.put(StingConstants.NY_NEWS_DESK, newsDeskBuilder.toString());
        }


        requestParams.put(StingConstants.NY_SORT_ORDER, FilterAttributes.sortOrder.getSortOrder());
        requestParams.put(StingConstants.PAGE, pageNumber);
        requestParams.put(StingConstants.QUERY, searchText);

        return requestParams;
    }
}
