package com.ecksday.borrowtracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.ecksday.borrowtracker.MainActivity.hideKeyboardFrom;

/**
 * Created by G551JK-DM053H on 24-09-2017.
 */

public class AccountActivity extends AppCompatActivity {

    Button Update,Delete;
    EditText First_Name, Last_Name, Email, Password, PasswordMatch ;
    String User_Id_Holder,F_Name_Holder, L_Name_Holder, EmailHolder, PasswordHolder,PasswordMatchHolder;
    String HttpUpdateURL = "http://ecksday.com/btadmin/AccountUpdate.php";
    String HttpDetailsURL = "http://ecksday.com/btadmin/AccountDetails.php";
    String HttpDeleteURL = "http://ecksday.com/btadmin/AccountDelete.php";
    Boolean CheckEditText ;
    RequestQueue requestQueue;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        requestQueue = Volley.newRequestQueue(this);

        //Assign Id'S
        First_Name = (EditText)findViewById(R.id.account_firstname);
        Last_Name = (EditText)findViewById(R.id.account_lastname);
        Email = (EditText)findViewById(R.id.account_email);
        Password = (EditText)findViewById(R.id.account_password);
        PasswordMatch = (EditText)findViewById(R.id.account_password_match);

        sharedPreferences = getSharedPreferences("logindetails",MODE_PRIVATE);
        User_Id_Holder= sharedPreferences.getString("user_id","");

        StringRequest detailsRequest = new StringRequest(Request.Method.POST, HttpDetailsURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String stringResponse) {
                if(stringResponse.equals("User not found.")){
                    Snackbar snackbar = Snackbar
                            .make(First_Name, stringResponse, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                else {
                    try {
                        JSONObject jsonResponse = new JSONObject(stringResponse);
                        F_Name_Holder = jsonResponse.getString("user_firstname");
                        L_Name_Holder = jsonResponse.getString("user_lastname");
                        EmailHolder = jsonResponse.getString("user_email");
                        PasswordHolder = jsonResponse.getString("user_password");
                        First_Name.setText(F_Name_Holder);
                        Last_Name.setText(L_Name_Holder);
                        Email.setText(EmailHolder);
                        Password.setText(PasswordHolder);
                        PasswordMatch.setText(PasswordHolder);

                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse errorRes = error.networkResponse;
                String stringData = "";
                try{
                    if(errorRes != null && errorRes.data != null){
                        stringData = new String(errorRes.data,"UTF-8");
                    }}
                catch (UnsupportedEncodingException e){

                }
                Log.e("Error",stringData);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("user_id", User_Id_Holder);
                return parameters;
            }
        };

        requestQueue.add(detailsRequest);

        Update = (Button)findViewById(R.id.account_update);
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                hideKeyboardFrom(AccountActivity.this, view);

                // Checking whether EditText is Empty or Not
                CheckEditTextIsEmptyOrNot();

                if(CheckEditText){
                    if(PasswordHolder.equals(PasswordMatchHolder)) {

                        // If EditText is not empty and CheckEditText = True then this block will execute.
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpUpdateURL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String stringResponse) {
                                Snackbar snackbar = Snackbar
                                        .make(view, stringResponse, Snackbar.LENGTH_LONG);
                                snackbar.show();

                            }

                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                NetworkResponse errorRes = error.networkResponse;
                                String stringData = "";
                                try{
                                    if(errorRes != null && errorRes.data != null){
                                        stringData = new String(errorRes.data,"UTF-8");
                                    }}
                                catch (UnsupportedEncodingException e){

                                }
                                Log.e("Error",stringData);
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> parameters = new HashMap<String, String>();
                                parameters.put("user_id", User_Id_Holder);
                                parameters.put("f_name", F_Name_Holder);
                                parameters.put("L_name", L_Name_Holder);
                                parameters.put("email", EmailHolder);
                                parameters.put("password", PasswordHolder);
                                return parameters;
                            }
                        };

                        requestQueue.add(stringRequest);
                    }
                    else{
                        Snackbar snackbar = Snackbar
                                .make(view, "Passwords do not match. Try again.", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }

                }
                else {

                    // If EditText is empty then this block will execute .
                    Snackbar snackbar = Snackbar
                            .make(view, "Please fill all the form fields.", Snackbar.LENGTH_LONG);
                    snackbar.show();

                }
            }
            });

        Delete=(Button)findViewById(R.id.account_delete);
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                StringRequest deleteRequest= new StringRequest(Request.Method.POST, HttpDeleteURL , new Response.Listener<String>(){
                    @Override
                    public void onResponse(String stringResponse){

                        Snackbar snackbar = Snackbar
                                .make(view, stringResponse, Snackbar.LENGTH_LONG);
                        snackbar.show();

                        if(stringResponse.equals("Account deleted successfully!")) {
                            getSharedPreferences("logindetails", 0).edit().clear().apply();

                            Intent intent = new Intent(AccountActivity.this, LoginRegisterActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse errorRes = error.networkResponse;
                        String stringData = "";
                        try{
                            if(errorRes != null && errorRes.data != null){
                                stringData = new String(errorRes.data,"UTF-8");
                            }}
                        catch (UnsupportedEncodingException e){

                        }
                        Log.e("Error",stringData);
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> parameters = new HashMap<String,String>();
                        parameters.put("user_id",User_Id_Holder);
                        return parameters;
                    }
                };
                requestQueue.add(deleteRequest);
            }
        });
    }

    public void CheckEditTextIsEmptyOrNot(){

        F_Name_Holder = First_Name.getText().toString();
        L_Name_Holder = Last_Name.getText().toString();
        EmailHolder = Email.getText().toString();
        PasswordHolder = Password.getText().toString();
        PasswordMatchHolder = PasswordMatch.getText().toString();

        if(TextUtils.isEmpty(F_Name_Holder) || TextUtils.isEmpty(L_Name_Holder) || TextUtils.isEmpty(EmailHolder) || TextUtils.isEmpty(PasswordHolder) || TextUtils.isEmpty(PasswordMatchHolder)) {
            CheckEditText = false;
        }
        else {
            CheckEditText = true ;
        }
    }
}


