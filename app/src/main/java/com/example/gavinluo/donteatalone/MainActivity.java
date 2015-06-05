package com.example.gavinluo.donteatalone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
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

import java.io.InputStream;


public class MainActivity extends ActionBarActivity {

    CallbackManager callbackManager;

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

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

                Profile profile = Profile.getCurrentProfile();
                String profileInfo = profile.toString();
//                info.setText(profileInfo);

                ImageView image = (ImageView)findViewById(R.id.image);
//                image.setImageURI(profile.getLinkUri());

//                new DownloadImageTask((ImageView) findViewById(R.id.image))
//                        .execute("http://java.sogeti.nl/JavaBlog/wp-content/uploads/2009/04/android_icon_256.png");

                String output = "";
                output = "ID: " + profile.getId() +"\n";
                output += "FirstName: " + profile.getFirstName() + "\n";
                output += "MiddleName: " + profile.getMiddleName() + "\n";
                output += "LastName: " + profile.getLastName() + "\n";
                output += "ProfilePic: " + profile.getProfilePictureUri(100, 100) + "\n";

                info.setText(output);

//                Uri uri = Uri.parse(profile.getLinkUri().toString());
//                Uri uri = Uri.parse("https://java.sogeti.nl/JavaBlog/wp-content/uploads/2009/04/android_icon_256.png");
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intent);

                ProfilePictureView profilePictureView = (ProfilePictureView) findViewById(R.id.profilePicture);
                profilePictureView.setProfileId(profile.getId());

                Log.i("PICTURE", profile.getLinkUri().toString());

//                info.setText(Html.fromHtml("<a href=http://www.stackoverflow.com> STACK OVERFLOW "));
//                info.setText(Html.fromHtml("<a href=" + profile.getLinkUri() +"> STACK OVERFLOW "));
//                info.setMovementMethod(LinkMovementMethod.getInstance());

//                ImageView image = (ImageView)findViewById(R.id.image);
//                image.setImageURI(profile.getLinkUri());
//                image.setImageURI("https://java.sogeti.nl/JavaBlog/wp-content/uploads/2009/04/android_icon_256.png");
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
