package com.example.gavinluo.donteatalone;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.facebook.*;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


public class MainActivity extends ActionBarActivity {

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_main);

        LoginButton loginButton = (LoginButton)findViewById(R.id.login_button);
        //TextView info = (TextView)findViewById(R.id.info);
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                TextView info = (TextView)findViewById(R.id.info);
                info.setText("User ID: " + loginResult.getAccessToken().getUserId() + "\n" +
                                "AuthToken: " + loginResult.getAccessToken().getToken() + "\n" +
                                    "ToString: " + loginResult.getAccessToken().toString());
            }

            @Override
            public void onCancel() {
                // App code
                TextView info = (TextView)findViewById(R.id.info);
                info.setText("Login attempt canceled");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                TextView info = (TextView)findViewById(R.id.info);
                info.setText("Login attempt failed" + exception.toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
