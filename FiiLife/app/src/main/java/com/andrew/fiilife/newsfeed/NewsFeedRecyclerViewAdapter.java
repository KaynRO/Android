package com.andrew.fiilife.newsfeed;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andrew.fiilife.R;
import com.andrew.fiilife.data.Data;

import java.util.ArrayList;

/**
 * Created by Razvan Gabriel / Dizz on 04-Aug-16.
 */
public class NewsFeedRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int POST = 1001;
    private final int HEADER = 1002;
    private Context context;
    private ArrayList<Data.Post> myPosts = new ArrayList<>();

    public NewsFeedRecyclerViewAdapter(Context context, ArrayList<Data.Post> myPosts) {
        this.context = context;
        this.myPosts = myPosts;
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        //return this.items.size();
        if(myPosts.size() == 0 || myPosts == null ) return 1;
        return myPosts.size()+1 ;
    }

    @Override
    public int getItemViewType(int position) {
        //More to come
        if(position == 0) return HEADER;
        if(myPosts.size() > 0 && position > 0) return POST;
        return HEADER;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());


        switch (viewType) {
            case HEADER:
                View headerView = inflater.inflate(R.layout.news_feed_header_view, viewGroup, false);
                viewHolder = new NewsFeedHeaderView(headerView, context);
                break;
            case POST:
                View postView = inflater.inflate(R.layout.news_feed_post_view, viewGroup, false);
                viewHolder = new NewsFeedPostView(postView, context);
                break;
            default:
                View defaultView = inflater.inflate(R.layout.news_feed_header_view, viewGroup, false);
                viewHolder = new NewsFeedHeaderView(defaultView, context);
                break;
        }
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if(position > 0 && myPosts.size() > 0) position--;

        switch (viewHolder.getItemViewType()) {
            case HEADER:
                NewsFeedHeaderView headerView = (NewsFeedHeaderView) viewHolder;
                headerView.bindModel();
                break;
            case POST:
                NewsFeedPostView postView = (NewsFeedPostView) viewHolder;
                postView.bindModel(myPosts.get(position));
                break;

            default:
              //  ViewHolder vh = (ViewHolder) viewHolder;
                break;
        }
    }

}