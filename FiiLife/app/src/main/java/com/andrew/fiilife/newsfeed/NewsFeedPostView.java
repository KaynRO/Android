package com.andrew.fiilife.newsfeed;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andrew.fiilife.R;
import com.andrew.fiilife.data.Data;

/**
 * Created by Razvan Gabriel / Dizz on 08-Aug-16.
 */
public class NewsFeedPostView extends RecyclerView.ViewHolder {

    private Context context;
    private LinearLayout createJobContainer;
    private TextView userName;
    private TextView time;
    private TextView content;
    private ImageView postHeaderPicture;
    private TextView courseName;




    public NewsFeedPostView(View view, Context context) {
        super(view);
        this.context = context;
        userName = (TextView) view.findViewById(R.id.userName);
        time = (TextView) view.findViewById(R.id.time);
        content = (TextView) view.findViewById(R.id.content);
        courseName = (TextView) view.findViewById(R.id.courseName);
        postHeaderPicture = (ImageView) view.findViewById(R.id.postHeaderPicture);
    }



    public void bindModel(Data.Post item){

        userName.setText(item.userName);
        time.setText(item.time);
        content.setText(item.content);
        courseName.setText(item.courseName);
        if(item.type == 1) postHeaderPicture.setImageResource(R.mipmap.icon_student);
        else postHeaderPicture.setImageResource(R.mipmap.icon_teacher);


    }

}