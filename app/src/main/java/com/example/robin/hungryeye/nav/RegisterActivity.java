package com.example.robin.hungryeye.nav;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.robin.hungryeye.MainActivity;
import com.example.robin.hungryeye.R;
import com.example.robin.hungryeye.config.AppConfig;
import com.example.robin.hungryeye.config.AppController;
import com.example.robin.hungryeye.helper.SQLiteHandler;
import com.example.robin.hungryeye.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    //for logcat
    private static final String TAG = LoginActivity.class.getSimpleName();

    //UI variables
    private EditText name,email,password,re_password;
    private Button register,goToLogin;
    private ProgressBar progressBar;

    //session and db object from helper package
    private SessionManager session;
    private SQLiteHandler localDBHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        //name of the person
        name =(EditText)findViewById(R.id.name);
        //email of the person
        email =(EditText)findViewById(R.id.re_email);
        //password of the person
        password =(EditText)findViewById(R.id.rg_password);
        //retype password [for now it not used so check // TODO: 12/4/2015
        re_password =(EditText)findViewById(R.id.re_password);
        //register button
        register =(Button)findViewById(R.id.re_email_sign_in_button);
        //go to login button
        goToLogin =(Button)findViewById(R.id.re_go_to_login);
        //progress bar
        progressBar = (ProgressBar)findViewById(R.id.register_progress);

        session = new SessionManager(getApplicationContext());
        localDBHandler = new SQLiteHandler(getApplicationContext());

        //if the user is already logged in
        if(session.isLoggedIn()){
            Intent i = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        //if login button is pressed
        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
        //when register button is pressed
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String r_name = name.getText().toString();
                String r_email = email.getText().toString();
                String r_password =password.getText().toString();

                if(r_name!=null && r_email!=null && r_password!=null){
                    registerCheck(r_name,r_email,r_password);
                }
            }
        });

    }

    public void registerCheck(final String name,final String email,final String password){
        //tag request
        String tag_request ="register_request";
        Log.d(TAG, "Below show progress 1.");
        //A canned request for retrieving the response body at a given URL as a String[from documentations]
        //two listener should be implemented which are responce.Listener and Response.errorListener
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.REGISTER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // on response set the progress barr to false
                //progressBar.GONE;

                //api returns a json responce which should be catch
                try{
                    Log.d(TAG, "Below show progress 2.");
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    if(!error){
                        String uid = jsonObject.getString("uid");
                        JSONObject jsonUser = jsonObject.getJSONObject("user");
                        String name = jsonUser.getString("name");
                        String email=jsonUser.getString("email");
                        String created_at = jsonUser.getString("created_at");
                        //add to the  sqlite database
                        localDBHandler.addUser(name, email, uid, created_at);
                        //start the session
                        session.setLogin(true);
                        Intent i = new Intent(RegisterActivity.this,MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else{
                        Log.d(TAG, "Below show progress 3.");
                        String error_msg = jsonObject.getString("error_msg");
                        Toast.makeText(RegisterActivity.this, error_msg, Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Below show progress 4.");
                //if error check the logcat
                String msg = (error.getMessage()==null)?"register failed!":error.getMessage();
               Log.d(TAG, msg);
            }
        })//bracket close for StringRequest
        {//this is the curly braces of the StringRequest which should override getParams method from Volley
            @Override
            public Map<String, String> getParams(){
                Map<String,String> p = new HashMap<>();
                Log.d(TAG, "Below show progress 5.");
                //the value that gets sent is 'login' and the key is 'tag'
                p.put("tag","register");
                p.put("name",name);
                p.put("email",email);
                p.put("password",password);
                return p;
            }
        };
        Log.d(TAG, "Below show progress 6.");
        //add to the instance of the appcontrollerthe string request and the tag request
        AppController.getInstance().addToRequestQueue(stringRequest,tag_request);
    }

}
