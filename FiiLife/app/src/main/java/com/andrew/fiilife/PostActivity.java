package com.andrew.fiilife;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.andrew.fiilife.data.Constant;
import com.andrew.fiilife.data.SharedPref;
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

public class PostActivity extends AppCompatActivity {

    Context context;
    SharedPref manager = new SharedPref();
    public ArrayList<Integer> courseIDs = new ArrayList<>() ;
    EditText chosenCourse ;
    public Boolean found = false ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        context = PostActivity.this;
        getAvailableCourses(manager.getUserID(context) ,manager.getUserToken(context) , manager.getUserType(context)) ;

        chosenCourse = (EditText) findViewById(R.id.post_course_text) ;
        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Button button = (Button) findViewById(R.id.post_button);
        final EditText postText = (EditText) findViewById(R.id.activity_post_text);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int chosenID = Integer.valueOf(chosenCourse.getText().toString()) ;
                Log.e("chosenID", chosenID+"");
                for ( int i = 0 ; i <= courseIDs.size() - 1 ; i++ )
                    if(courseIDs.get(i) == chosenID) {
                        sendPostRequest(postText.getText().toString(), manager.getUserToken(context), manager.getUserID(context), manager.getUserType(context), chosenID);
                        found = true ;
                        break;
                    }
                if ( !found ){
                    Toast.makeText(context, "Course not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void sendPostRequest(final String postText, final String token, final int userID, final int userType, final int courseID) {

        StringRequest requestName = new StringRequest(Request.Method.POST, Constant.sendPostText, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Response", response);
                try {
                    JSONObject serverResponse = new JSONObject(response);
                    String status = serverResponse.getString("status");
                    if (status.equals("negative")) {
                        String body = serverResponse.getString("body");
                        Toast.makeText(context, "Failed to upload post", Toast.LENGTH_SHORT).show();
                    } else if (status.equals("ok")) {
                        Toast.makeText(context, "You just posted something new", Toast.LENGTH_SHORT).show();
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();
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
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("content", postText);
                params.put("token", token);
                params.put("type", String.valueOf(userType));
                params.put("userID", String.valueOf(userID));
                params.put("courseID", String.valueOf(courseID));

                return params;
            }
        };

        VolleySingleton.getInstance(context).getRequestQueue().add(requestName);
    };

    public void getAvailableCourses( final int userID , final String userToken , final int userType){

        StringRequest requestName = new StringRequest(Request.Method.POST, Constant.getCourses, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Response", response);
                try {
                    String print = "" ;
                    JSONObject serverResponse = new JSONObject(response);
                    String body = serverResponse.getString("body");
                    String status = serverResponse.getString("status");
                    if (status.equals("negative")) {
                        Toast.makeText(context, "Unexpected Error", Toast.LENGTH_SHORT).show();
                    } else if (status.equals("ok")) {
                        JSONArray coursesList = new JSONArray(body) ;
                        for ( int i = 0 ; i < coursesList.length() ; i++ ){
                            JSONObject object = coursesList.getJSONObject(i) ;
                            int ID = object.getInt("ID") ;
                            String name = object.getString("name") ;
                            String day = object.getString("day") ;
                            courseIDs.add(ID) ;
                            print += ID + " -> " + name + " -> " + day + '\n' ;
                        }
                        TextView txt = (TextView) findViewById(R.id.courseList) ;
                        txt.setText(print);
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
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("token", userToken);
                params.put("type", String.valueOf(userType));
                params.put("userID", String.valueOf(userID));


                return params;
            }
        };

        VolleySingleton.getInstance(context).getRequestQueue().add(requestName);
    }
}
