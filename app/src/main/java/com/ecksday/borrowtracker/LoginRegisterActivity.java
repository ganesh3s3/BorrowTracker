package com.ecksday.borrowtracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import static com.ecksday.borrowtracker.MainActivity.hideKeyboardFrom;

/**
 * A login screen that offers login via email/password.
 */
public class LoginRegisterActivity extends AppCompatActivity {

    EditText LREmail, LRPassword, LRFirstName, LRLastName, LRPhone;
    String EmailHolder, PasswordHolder, FirstNameHolder, LastNameHolder, PhoneHolder;
    String HttpLoginURL, HttpRegisterURL;
    Button LRLogin, LRRegister;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editPreferences;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        requestQueue = Volley.newRequestQueue(this);
        sharedPreferences = getSharedPreferences("logindetails",MODE_PRIVATE);
        editPreferences = sharedPreferences.edit();

        LREmail=(EditText) findViewById(R.id.lr_email);
        LRPassword=(EditText) findViewById(R.id.lr_password);
        LRLogin=(Button) findViewById(R.id.lr_login);

        LRLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                        hideKeyboardFrom(LoginRegisterActivity.this, view);

                        // Checking whether EditText is Empty or Not
                        if (CheckEditTextIsNotEmpty()) {

                            // If EditText is not empty and CheckEditText = True then this block will execute.
                            startActivity(new Intent(LoginRegisterActivity.this, MainActivity.class));
                            /*
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpLoginURL, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String stringResponse) {
                                    if(stringResponse.equals("Invalid Username or Password.")){
                                        Snackbar snackbar = Snackbar
                                                .make(view, stringResponse, Snackbar.LENGTH_LONG);
                                        snackbar.show();
                                    }
                                    else {
                                        try {
                                            JSONObject jsonResponse = new JSONObject(stringResponse);
                                            String F_Name_Holder = jsonResponse.getString("first_name");
                                            String L_Name_Holder = jsonResponse.getString("last_name");
                                            editPreferences.putString("first_name",F_Name_Holder);
                                            editPreferences.putString("last_name",L_Name_Holder);
                                            editPreferences.putString("email",EmailHolder);
                                            editPreferences.putString("password",PasswordHolder);
                                            editPreferences.apply();
                                            Snackbar snackbar = Snackbar
                                                    .make(view, "Logged In successfully as " + F_Name_Holder + " " + L_Name_Holder, Snackbar.LENGTH_LONG);
                                            snackbar.show();

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
                                    parameters.put("email", EmailHolder);
                                    parameters.put("password", PasswordHolder);
                                    return parameters;
                                }
                            };

                            requestQueue.add(stringRequest);*/

                        } else {

                            // If EditText is empty then this block will execute .
                            Snackbar snackbar = Snackbar
                                    .make(view, "Please fill all the form fields.", Snackbar.LENGTH_LONG);
                            snackbar.show();

                        }

            }
        });

    }

    protected boolean CheckEditTextIsNotEmpty(){

        EmailHolder = LREmail.getText().toString();
        PasswordHolder = LRPassword.getText().toString();


        if(TextUtils.isEmpty(EmailHolder) || TextUtils.isEmpty(PasswordHolder))
        {
            return false;
        }
        else {

            return true;
        }

    }
}

