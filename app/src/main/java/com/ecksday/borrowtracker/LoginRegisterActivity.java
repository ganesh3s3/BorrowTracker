package com.ecksday.borrowtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

/**
 * A login screen that offers login via email/password.
 */
public class LoginRegisterActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    Button signInButton;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_loginregister);
        signInButton = (Button) findViewById(R.id.signInButton);

        mAuth = FirebaseAuth.getInstance();

        if(isUserLoggedIn()){
            loginUser();
        }

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create and launch sign-in intent
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(
                                        Arrays.asList(
                                                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER)
                                                        .build(),
                                                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER)
                                                        .build()))
                                .build(), RC_SIGN_IN);
            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                loginUser();
            }

            if(resultCode == RESULT_CANCELED){
                displayMessage("Sign in failed!");
            }

        }
    }

    void loginUser(){
        Intent loginIntent = new Intent(LoginRegisterActivity.this, MainActivity.class);
        startActivity(loginIntent);
        finish();
    }

    boolean isUserLoggedIn() {
        if(mAuth.getCurrentUser() != null){
            return true;
        }
        return false;
    }

    private void displayMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}