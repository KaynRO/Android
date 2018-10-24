package com.andrew.fiilife.timetable;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andrew.fiilife.R;
import com.andrew.fiilife.TimeTableFragment;
import com.andrew.fiilife.data.Data;
import com.andrew.fiilife.newsfeed.NewsFeedHeaderView;

import java.util.ArrayList;

/**
 * Created by Razvan Gabriel / Dizz on 04-Aug-16.
 */
public class TimeTableRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int ITEM = 1001;
    private final int HEADER = 1002;
    private Context context;
    private ArrayList<Data.TableItem> myPosts = new ArrayList<>();

    public TimeTableRecyclerViewAdapter(Context context, ArrayList<Data.TableItem> myPosts) {
        this.context = context;
        this.myPosts = myPosts;
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return myPosts.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());


        View headerView = inflater.inflate(R.layout.time_table_view, viewGroup, false);
        viewHolder = new TimeTableView(headerView, context);


        return viewHolder;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
                TimeTableView headerView = (TimeTableView) viewHolder;
                headerView.bindModel(myPosts.get(position));

    }

}