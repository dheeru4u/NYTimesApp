package com.dheeru.app.news.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dheeru.app.news.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dkthaku on 5/29/16.
 */
public class ArticleDetailActivity extends AppCompatActivity {

    @Bind(R.id.wvArticle)
    WebView articleWebView;
    private String currentUrl ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.arcticle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        // Enable up icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        articleWebView.getSettings().setLoadsImagesAutomatically(true);
        articleWebView.getSettings().setJavaScriptEnabled(true);
        articleWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        // Configure the client to use when opening URLs
        articleWebView.setWebViewClient(new MyBrowser());

        // Load the initial URL
        Intent i = getIntent();
        currentUrl = i.getStringExtra("webUrl");
        articleWebView.loadUrl(currentUrl);
    }

    // Manages the behavior when URLs are loaded
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_article, menu);

        MenuItem item = menu.findItem(R.id.menu_article_share);
        ShareActionProvider miShare = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

        shareIntent.putExtra(Intent.EXTRA_TEXT, currentUrl);

        miShare.setShareIntent(shareIntent);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // This is the up button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}