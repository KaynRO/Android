package com.andrew.fiilife.newsfeed;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.andrew.fiilife.PostActivity;
import com.andrew.fiilife.R;

/**
 * Created by Razvan Gabriel / Dizz on 08-Aug-16.
 */
public class NewsFeedHeaderView extends RecyclerView.ViewHolder {

    private Context context;
    private LinearLayout createJobContainer;
    View postView ;



    public NewsFeedHeaderView(View view, Context context) {
        super(view);
        this.context = context;
        postView = view.findViewById(R.id.fragment_post_text) ;
    }



    public void bindModel(){
        postView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PostActivity.class) ;
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ((Activity) context).startActivityForResult(intent,111);
            }
        });

    }

}