package com.ecksday.borrowtracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

import static android.content.Context.MODE_PRIVATE;
import static com.ecksday.borrowtracker.MainActivity.hideKeyboardFrom;

/**
 * Created by G551JK-DM053H on 11-10-2017.
 */

public class LoginFragment extends Fragment {

    EditText LoginEmail, LoginPassword;
    TextView LoginRegisterPrompt;
    String EmailHolder, PasswordHolder;
    String HttpLoginURL="http://ecksday.com/btadmin/UserLogin.php";
    Button Login;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editPreferences;
    RequestQueue requestQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.fragment_login, container, false);
        requestQueue = Volley.newRequestQueue(getActivity());
        sharedPreferences = getActivity().getSharedPreferences("logindetails",MODE_PRIVATE);
        editPreferences = sharedPreferences.edit();

        LoginEmail =(EditText) RootView.findViewById(R.id.login_email);
        LoginPassword =(EditText) RootView.findViewById(R.id.login_password);
        LoginRegisterPrompt = (TextView)RootView.findViewById(R.id.login_reg_prompt);
        Login=(Button)RootView.findViewById(R.id.login);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                hideKeyboardFrom(getActivity(), view);

                // Checking whether EditText is Empty or Not
                if (CheckEditTextIsNotEmpty()) {

                    // If EditText is not empty and CheckEditText = True then this block will execute.
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
                                    String User_Id_Holder = jsonResponse.getString("user_id");
                                    String F_Name_Holder = jsonResponse.getString("user_firstname");
                                    String L_Name_Holder = jsonResponse.getString("user_lastname");
                                    editPreferences.putString("user_id", User_Id_Holder);
                                    editPreferences.putString("first_name",F_Name_Holder);
                                    editPreferences.putString("last_name",L_Name_Holder);
                                    editPreferences.putString("email",EmailHolder);
                                    editPreferences.putString("password",PasswordHolder);
                                    editPreferences.apply();
                                    Snackbar snackbar = Snackbar
                                            .make(view, "Logged In successfully as " + F_Name_Holder + " " + L_Name_Holder, Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                    startActivity(new Intent(getActivity(), MainActivity.class));
                                    getActivity().finish();

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

                    requestQueue.add(stringRequest);

                } else {

                    // If EditText is empty then this block will execute .
                    Snackbar snackbar = Snackbar
                            .make(view, "Please fill all the form fields.", Snackbar.LENGTH_LONG);
                    snackbar.show();

                }

            }
        });

        LoginRegisterPrompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Fragment fragment = new RegisterFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.addToBackStack(null);
                ft.commit();

            }
        });

        return RootView;
    }

    private boolean CheckEditTextIsNotEmpty(){

        EmailHolder = LoginEmail.getText().toString();
        PasswordHolder = LoginPassword.getText().toString();


        if(TextUtils.isEmpty(EmailHolder) || TextUtils.isEmpty(PasswordHolder))
        {
            return false;
        }
        else {

            return true;
        }

    }
}
