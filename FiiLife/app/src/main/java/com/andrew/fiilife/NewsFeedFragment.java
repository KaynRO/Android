package com.andrew.fiilife;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.andrew.fiilife.data.Constant;
import com.andrew.fiilife.data.Data;
import com.andrew.fiilife.data.SharedPref;
import com.andrew.fiilife.newsfeed.NewsFeedRecyclerViewAdapter;
import com.andrew.fiilife.support.EndlessRecyclerViewScrollListener;
import com.andrew.fiilife.support.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Razvan Gabriel / Dizz on 02-Jul-16.
 */
public class NewsFeedFragment extends Fragment {

    /**
     * The source of truth for the activity.
     */

    private Context context;

    private View rootView;
    private GridView categoriesGridView;
    private View postView ;
    public SharedPref manager = new SharedPref() ;
    public ArrayList<Data.Post> myPosts = new ArrayList() ;

    private RecyclerView recyclerView;
    private NewsFeedRecyclerViewAdapter adapter;
    private EndlessRecyclerViewScrollListener recyclerViewScrollListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_news_feed, container, false);
        this.context = getActivity().getApplicationContext();
        SharedPref manager = new SharedPref() ;





        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        this.recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new NewsFeedRecyclerViewAdapter(getActivity(),myPosts);
        recyclerView.setAdapter(adapter);

// Add the scroll listener
        recyclerViewScrollListener = new EndlessRecyclerViewScrollListener( linearLayoutManager, 10) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                /** Triggered only when new data needs to be appended to the list
                 Add whatever code is needed to append new items to the bottom of the list
                 */
                requestPosts(this.visibleThreshold * page);
            }
        };
        this.recyclerView.addOnScrollListener(this.recyclerViewScrollListener);


        requestPosts(0) ;
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                requestPosts(0) ;
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    public void requestPosts(final int offset){

        StringRequest requestName= new StringRequest(Request.Method.POST, Constant.RetrievePost, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Response",response) ;
                try {
                    JSONObject serverResponse = new JSONObject(response) ;
                    String status = serverResponse.getString("status") ;
                    if ( status.equals("negative") ){
                        String body = serverResponse.getString("body") ;
                        Toast.makeText(context, "No Posts", Toast.LENGTH_SHORT).show();
                    }

                    else{
                        JSONArray list = new JSONArray(serverResponse.getString("body") ) ;
                        for ( int i = 0 ; i < list.length() ; i++ ){

                            JSONObject item = list.getJSONObject(i) ;
                            Data.Post post = new Data.Post() ;
                            post.ID = item.getInt("ID") ;
                            post.userID = item.getInt("userID") ;
                            post.courseID = item.getInt("courseID") ;
                            post.content = item.getString("content") ;
                            post.time = item.getString("time") ;
                            post.userName = item.getString("name") ;
                            post.courseName = item.getString("course");
                            post.type = item.getInt("type");
                            myPosts.add(post) ;
                            adapter.notifyDataSetChanged();
                        }



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, "ServerError", Toast.LENGTH_SHORT).show();
                //If there is no internet connection or the server is idle, the request will return an error
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("userID", String.valueOf(manager.getUserID(context)) ) ;
                params.put("token", manager.getUserToken(context)) ;
                params.put("limit", "10");
                params.put("offset", String.valueOf(offset));
                params.put("type" , String.valueOf(manager.getUserType(context))) ;
                Log.e("Type" , String.valueOf(manager.getUserType(context))) ;
                Log.e("Token" , manager.getUserToken(context)) ;
                return params;
            }
        };

        VolleySingleton.getInstance(context).getRequestQueue().add(requestName);
    }

}