package com.ecksday.borrowtracker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ecksday.borrowtracker.MainActivity.hideKeyboardFrom;

/**
 * Created by G551JK-DM053H on 24-09-2017.
 */

public class AccountActivity extends AppCompatActivity {

    Button Delete, Update;
    EditText DisplayName, Email;
    RequestQueue requestQueue;
    //FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onStart() {
        super.onStart();

        //mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Assign Id'S
        DisplayName = (EditText)findViewById(R.id.account_firstname);
        Email = (EditText)findViewById(R.id.account_email);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        displayUserDetails(user);

        Update = (Button) findViewById(R.id.account_update);
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                updateAccountDetails(user);
            }
            });

        Delete=(Button)findViewById(R.id.account_delete);
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                hideKeyboardFrom(AccountActivity.this, view);
                AuthUI.getInstance()
                        .delete(AccountActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    //signOut();
                                }else {
                                    displayMessage("Error Signing Out!");
                                }
                            }
                        });
            }
        });
    }

    private void updateAccountDetails(FirebaseUser user) {
        hideKeyboardFrom(this, findViewById(R.id.account_layout));
        if(!CheckEditTextIsEmptyOrNot()) {

            if(!isValidName()) {
                displayMessage("Please enter a valid name");
            }
             else if(!isValidEmail()) {
                displayMessage("Please enter a valid email address");
            }
            else {

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(DisplayName.getText().toString()).build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    displayMessage("Profile updated!");
                                } else {
                                    displayMessage("Error while updating profile");
                                }
                            }
                        });
            }
        }
        else {
            displayMessage(getString(R.string.please_fill));
        }
    }

    private boolean isValidEmail() {
        return Patterns.EMAIL_ADDRESS.matcher(Email.getText().toString()).matches();
    }

    /*private boolean isUserLoggedIn(){
        if(mAuth.getCurrentUser() != null){
            return true;
        }
        return false;
    }*/
private boolean isValidName(){
    String regex = "^([a-z]+[,.]?[ ]?|[a-z]+['-]?)+$";
    Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(DisplayName.getText().toString());
    return matcher.find();
}
    private void displayMessage(String message){
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.account_layout), message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void displayUserDetails(FirebaseUser user) {

        if (user != null) {
            DisplayName.setText(user.getDisplayName());
            Email.setText(user.getEmail());
        }
    }

    public boolean CheckEditTextIsEmptyOrNot(){

        if(TextUtils.isEmpty(DisplayName.getText().toString()) || TextUtils.isEmpty(Email.getText().toString())) {
            return true;
        }
        else {
            return false;
        }
    }
}


