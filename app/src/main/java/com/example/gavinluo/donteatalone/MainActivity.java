package com.example.gavinluo.donteatalone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;

import com.facebook.*;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.Arrays;


public class MainActivity extends ActionBarActivity {

    CallbackManager callbackManager;
    Profile profile;

    private static final String NAME = "name";
    private static final String ID = "id";
    private static final String PICTURE = "picture";
    private static final String FIELDS = "fields";
    private static final String GENDER = "gender";
    private static final String BIRTHDAY = "birthday";
    private static final String AGE_RANGE = "age_range";
    private static final String EMAIL = "email";
    private static final String USER_FRIENDS = "user_friends";

    private static final String REQUEST_FIELDS =
            TextUtils.join(",", new String[]{ID, NAME, GENDER, AGE_RANGE, EMAIL, "friends"});
    private JSONObject user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_main);

        profile = Profile.getCurrentProfile();
        if(profile != null){
            updateUI();
        }

        LoginButton loginButton = (LoginButton)findViewById(R.id.login_button);

        // set permission for login button
//        loginButton.setReadPermissions(Arrays.asList("public_profile, user_birthday"));
        loginButton.setReadPermissions("user_friends");


        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                updateUI();

                final AccessToken accessToken = AccessToken.getCurrentAccessToken();
                if (accessToken != null) {
                    GraphRequest request = GraphRequest.newMeRequest(
                            accessToken, new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject me, GraphResponse response) {
                                    user = me;
                                    updateUI();
                                }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString(FIELDS, REQUEST_FIELDS);
//                    parameters.putString("fields", "id,name, birthday");
                    request.setParameters(parameters);
                    GraphRequest.executeBatchAsync(request);
                } else {
                    user = null;
                }
            }

            @Override
            public void onCancel() {
                // App code
                TextView info = (TextView) findViewById(R.id.info);
                info.setText("Login attempt canceled");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                TextView info = (TextView) findViewById(R.id.info);
                info.setText("Login attempt failed" + exception.toString());
            }
        });
    }

    public void updateUI(){
        TextView info = (TextView)findViewById(R.id.info);
//        info.setText("User ID: " + loginResult.getAccessToken().getUserId() + "\n" +
//                "AuthToken: " + loginResult.getAccessToken().getToken() + "\n" +
//                "ToString: " + loginResult.getAccessToken().toString());

        profile = Profile.getCurrentProfile();

        String output = "";
        output = "ID: " + profile.getId() +"\n";
        output += "FirstName: " + profile.getFirstName() + "\n";
        output += "MiddleName: " + profile.getMiddleName() + "\n";
        output += "LastName: " + profile.getLastName() + "\n";
        output += "ProfilePic: " + profile.getProfilePictureUri(100, 100) + "\n";

        if(user == null){
            output += "User: null \n";
        } else {
//            output += "User: " + user.toString() + "\n";
            output += "User: " + user.toString() + "\n";
        }

        info.setText(output);

        ProfilePictureView profilePictureView = (ProfilePictureView) findViewById(R.id.profilePicture);
        profilePictureView.setProfileId(profile.getId());
    }

    public void getUserInfo(){

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
