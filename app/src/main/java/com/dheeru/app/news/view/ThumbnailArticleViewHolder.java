package com.dheeru.app.news.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dheeru.app.news.R;
import com.dheeru.app.news.activity.ArticleDetailActivity;
import com.dheeru.app.news.modal.Article;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dkthaku on 5/29/16.
 */
public class ThumbnailArticleViewHolder  extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    @Bind(R.id.articleThumNailwebUrl) ImageView ivArticle;
    @Bind(R.id.artHeadLineTextView) TextView tvArticle;

    List<Article> articles;
    Context mContext;

    public ThumbnailArticleViewHolder(Context context, View view, List<Article> mArticles) {
        super(view);

        this.articles = mArticles;
        this.mContext = context;
        ivArticle = (ArticleResizableImageView)itemView.findViewById(R.id.articleThumNailwebUrl);
        // Picasso.with(getContext()).load(movie.getPosterPath()).into(mvImage);
        // mvImage.setImageResource(0);
        tvArticle = (TextView)itemView.findViewById(R.id.artHeadLineTextView);
        // Attach a click listener to the entire row view
        view.setOnClickListener(this);
        ButterKnife.bind(this, view);
    }

    // Handles the row being being clicked
    @Override
    public void onClick(View view) {
        int position = getLayoutPosition(); // gets item position
        Article article = articles.get(position);
        // We can access the data within the views
        Toast.makeText(mContext, "Loading article...", Toast.LENGTH_SHORT).show();

        Intent i = new Intent(mContext, ArticleDetailActivity.class);
        i.putExtra("webUrl", article.webUrl);
        mContext.startActivity(i);
    }

    public ImageView getIvArticle() {
        if(ivArticle==null){
            Log.d(this.getClass().getSimpleName(), "getIvArticle:ivArticle "+ivArticle);
        }

        return ivArticle;
    }

    public void setIvArticle(ImageView ivArticle) {
        this.ivArticle = ivArticle;
    }

    public TextView getTvArticle() {
        return tvArticle;
    }

    public void setTvArticle(TextView tvArticle) {
        this.tvArticle = tvArticle;
    }
}