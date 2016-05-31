package com.dheeru.app.news.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dheeru.app.news.R;
import com.dheeru.app.news.listner.ArticlesEndlessRvScrollListener;
import com.dheeru.app.news.modal.Article;
import com.dheeru.app.news.view.ArticleResizableImageView;
import com.dheeru.app.news.view.TextArticleViewHolder;
import com.dheeru.app.news.view.ThumbnailArticleViewHolder;

import java.util.ArrayList;

/**
 * Created by dkthaku on 5/28/16.
 */
public class ArticlesRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<Article> articles =new ArrayList<Article>();
    private static Context mContext;
    ArticlesEndlessRvScrollListener endlessScrollListener;

    private final int TEXT_ONLY = 0, THUMB_NAIL = 1;

    public ArticlesRecyclerViewAdapter(ArrayList<Article> articleList) {
        Log.d("MovieDynaItemAdapater", "MovieDynaItemAdapater: "+articleList);
        articles =(ArrayList) articleList;

    }
    public ArticlesRecyclerViewAdapter(Context context, ArrayList<Article> articles) {
        this.articles = articles;
        mContext = context;
    }

    public void setEndlessScrollListener(ArticlesEndlessRvScrollListener endlessScrollListener) {
        this.endlessScrollListener = endlessScrollListener;
    }

/*
    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ArticlesRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View view = inflater.inflate(R.layout.article_list_items, parent, false);
        //   Log.d(this.getClass().getSimpleName(), "onCreateViewHolder: view"+view.getId());
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ArticlesRecyclerViewAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Article article = articles.get(position);
        if(article==null ){
            Log.d(this.getClass().getSimpleName(), "onBindViewHolder: in if "+article);
            return;
        }
        // Log.d(this.getClass().getSimpleName(), "onBindViewHolder: "+movie.toString());
        // Set item views based on the data model

        ImageView mvImage = viewHolder.ivArticleThumnailImage;
        // Picasso.with(mvImage.getContext()).load(movie.getPosterPath()).into(mvImage);
        // mvImage.setImageResource(0);
        // Log.d(this.getClass().getSimpleName(), "mvImage: "+mvImage);

        if(mvImage!=null) {

        if(TextUtils.isEmpty(article.getArticleThumbnailUrl())){
            Picasso.with(mvImage.getContext()).load("https://lh3.googleusercontent.com/-6tbNiaOWCb0/AAAAAAAAAAI/AAAAAAAAAAA/i87QAmTWS9o/s120-c/photo.jpg")
                    .transform(new RoundedCornersTransformation(20, 20)).fit().centerCrop()
                    .placeholder(R.drawable.myphoto)
                    .error(R.drawable.donaldtrump)
                    .into(mvImage);

        }else {


                Glide.with(mvImage.getContext()).load(article.getArticleThumbnailUrl())
                        .placeholder(R.drawable.myphoto).error(R.drawable.donaldtrump)
                        .into(mvImage);

        }


        }
        TextView textViewTitle = viewHolder.tvArticleHeadlingTitle;
        //  Log.d(this.getClass().getSimpleName(), "textViewTitle: XXXXXXX "+textViewTitle);
        if(textViewTitle!=null && article.getHeadline()!=null)
            textViewTitle.setText(article.getHeadline().getMain());




    }
      */

    // Return the total count of items
    @Override
    public int getItemCount() {
        return articles.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row

        public ImageView ivArticleThumnailImage ;
        public TextView tvArticleHeadlingTitle;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            ivArticleThumnailImage = (ArticleResizableImageView)itemView.findViewById(R.id.articleThumNailwebUrl);
            // Picasso.with(getContext()).load(movie.getPosterPath()).into(mvImage);
            // mvImage.setImageResource(0);
            tvArticleHeadlingTitle = (TextView)itemView.findViewById(R.id.artHeadLineTextView);

        }
    }

    @Override
    public int getItemViewType(int position) {
        Article article = articles.get(position);
        if (TextUtils.isEmpty(article.getArticleThumbnailUrl())){
            return TEXT_ONLY;
        }
        return THUMB_NAIL;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case TEXT_ONLY:
                View v1 = inflater.inflate(R.layout.article_onlytext_gridview,
                        viewGroup, false);
                viewHolder = new TextArticleViewHolder(mContext, v1, articles);
                break;
            case THUMB_NAIL:
                View v2 = inflater.inflate(R.layout.article_list_items,
                        viewGroup, false);
                viewHolder = new ThumbnailArticleViewHolder(mContext, v2, articles);
                break;
            default:
                v2 = inflater.inflate(R.layout.article_list_items,
                        viewGroup, false);
                viewHolder = new ThumbnailArticleViewHolder(mContext, v2, articles);
                break;
        }
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case TEXT_ONLY:
                TextArticleViewHolder vh1 = (TextArticleViewHolder) viewHolder;
                configureTextArticleViewHolder(vh1, position);
                break;
            case THUMB_NAIL:
                ThumbnailArticleViewHolder vh2 = (ThumbnailArticleViewHolder) viewHolder;
                configureThumbnailArticleViewHolder(vh2, position);
                break;
            default:
                ThumbnailArticleViewHolder vh = (ThumbnailArticleViewHolder) viewHolder;
                configureThumbnailArticleViewHolder(vh, position);
                break;
        }
    }

    private void configureTextArticleViewHolder(TextArticleViewHolder viewHolder, int position) {
        Article article = articles.get(position);
        Log.d(this.getClass().getSimpleName(), "configureTextArticleViewHolder: viewHolder.getTvArticleText() "+viewHolder.getTvArticleText());
        Log.d(this.getClass().getSimpleName(), "configureTextArticleViewHolder: article.getHeadline() "+article.getHeadline());
        if(viewHolder.getTvArticleText()!=null && article!=null && article.getHeadline()!=null )
        viewHolder.getTvArticleText().setText(article.getHeadline().getMain());
        //viewHolder.tvArticleText.setText(article.getHeadline().getMain());
    }

    private void configureThumbnailArticleViewHolder(ThumbnailArticleViewHolder viewHolder,
                                                     int position) {
        Article article = articles.get(position);

        viewHolder.getIvArticle().setImageResource(0); //clearoff
        if (!TextUtils.isEmpty(article.getArticleThumbnailUrl())) {
            Glide.with(mContext).load(article.getArticleThumbnailUrl())
                    .placeholder(R.mipmap.ic_loading)
                    .error(R.drawable.myphoto)
                    .into(viewHolder.getIvArticle());
        }
        viewHolder.getTvArticle().setText(article.getHeadline().getMain());
       // viewHolder.getTvArticleTex().setText(article.getHeadline().getMain());
    }

}
