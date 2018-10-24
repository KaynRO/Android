package com.andrew.fiilife;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.andrew.fiilife.data.Constant;
import com.andrew.fiilife.data.SharedPref;
import com.andrew.fiilife.support.MD5;
import com.andrew.fiilife.support.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LogInActivity extends AppCompatActivity {

    private EditText userEmail ;
    private EditText userPassword ;
    private Button ButtonStudent ;
    private Button ButtonTeacher ;
    private Context context ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        context = LogInActivity.this ;
        userEmail = (EditText) findViewById(R.id.email) ;
        userPassword = (EditText) findViewById(R.id.passwordField) ;
        ButtonStudent = (Button) findViewById(R.id.loginButtonStudent) ;
        ButtonStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = userEmail.getText().toString() ;
                String password = userPassword.getText().toString() ;
                password = email + password ;
                password = MD5.createMD5(password) ;
                sendRequest(email,password, 1) ;
                Log.e("Send" , password) ;
            }
        });
        ButtonTeacher = (Button) findViewById(R.id.loginButtonTeacher) ;
        ButtonTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = userEmail.getText().toString() ;
                String password = userPassword.getText().toString() ;
                password = email + password ;
                password = MD5.createMD5(password) ;
                sendRequest(email,password, 2) ;
                Log.e("Send" , password) ;
            }
        });

    }

    private void sendRequest( final String email , final String password , final int type ) {

        StringRequest requestName= new StringRequest(Request.Method.POST, Constant.LogInURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Response",response) ;
                try {
                    JSONObject serverResponse = new JSONObject(response) ;
                    String status = serverResponse.getString("status") ;
                    if ( status.equals("negative") ){
                        String body = serverResponse.getString("body") ;
                        Toast.makeText(LogInActivity.this, "Username or Password incorrect", Toast.LENGTH_SHORT).show();
                    }

                    else{
                        JSONObject user = new JSONObject(serverResponse.getString("body") ) ;
                        int ID = user.getInt("ID") ;
                        String name = user.getString("name") ;
                        String email = user.getString("email") ;
                        String token = user.getString("token") ;
                        SharedPref manager = new SharedPref() ;
                        manager.setUserName(context, name);
                        manager.setUserEmail(context,email);
                        manager.setUserToken(context,token);
                        manager.setUserID(context,ID);
                        manager.setUserType(context,type) ;
                        Intent intent = new Intent(LogInActivity.this, MainActivity.class) ;
                        startActivity(intent) ;
                        finish() ;

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(LogInActivity.this, "ServerError", Toast.LENGTH_SHORT).show();
                //If there is no internet connection or the server is idle, the request will return an error
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("email", email) ;
                params.put("password", password) ;
                String token= MD5.createMD5(email) ;
                params.put("token", token) ;
                params.put("type",String.valueOf(type)) ;

                return params;
            }
        };

        VolleySingleton.getInstance(LogInActivity.this).getRequestQueue().add(requestName);
    }

}
