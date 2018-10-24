package com.andrew.fiilife;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andrew.fiilife.data.Constant;
import com.andrew.fiilife.data.Data;
import com.andrew.fiilife.data.SharedPref;
import com.andrew.fiilife.support.VolleySingleton;
import com.andrew.fiilife.timetable.TimeTableRecyclerViewAdapter;
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
public class TimeTableFragment extends Fragment {

    private View rootView;
    private Context context;

    private View postView ;
    public SharedPref manager = new SharedPref() ;
    public ArrayList<Data.TableItem> myData = new ArrayList() ;

    private RecyclerView recyclerView;
    private TimeTableRecyclerViewAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.fragment_time_table, container, false);
        context = getActivity().getApplicationContext();
        SharedPref manager = new SharedPref() ;



        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        this.recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new TimeTableRecyclerViewAdapter(getActivity(),myData);
        recyclerView.setAdapter(adapter);


        requestPosts();
        return rootView;
    }







    public void requestPosts(){

        StringRequest requestName= new StringRequest(Request.Method.POST, Constant.retrieveTimeTable, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("timetable",response) ;
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
                            Data.TableItem ti = new Data.TableItem() ;
                            ti.ID = item.getInt("ID");
                            ti.day = item.getInt("day");
                            ti.description = item.getString("description");
                            ti.name = item.getString("name");
                            myData.add(ti) ;
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
                params.put("type" , String.valueOf(manager.getUserType(context))) ;
                return params;
            }
        };

        VolleySingleton.getInstance(context).getRequestQueue().add(requestName);
    }





}