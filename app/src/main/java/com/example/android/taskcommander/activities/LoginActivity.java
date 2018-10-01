package com.example.android.taskcommander.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

//Auth
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.example.android.taskcommander.model.User;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.Date;


import com.example.android.taskcommander.R;

public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private boolean newUser;
    private String loginMethod;

    private static final int GOOGLE_SIGN_IN = 9001;
    private static int TWITTER_SIGN_IN;
    private static final int FACEBOOK_SIGN_IN = 1200;

    private FirebaseAuth mAuth;
    private FirebaseMessaging mMessaging;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private GoogleApiClient googleApiClient;
    private TwitterAuthClient twitterAuthClient;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initTwitterClient();

        mAuth = FirebaseAuth.getInstance();
        mMessaging = FirebaseMessaging.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            // open MainActivity
            openMainActivity();
            return;
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount googleSignInAccount = result.getSignInAccount();
                googleFirebaseAuth(googleSignInAccount);
                return;
            }

            Toast.makeText(this, getResources().getString(R.string.google_login_failed), Toast.LENGTH_SHORT).show();

        } else if (requestCode == TWITTER_SIGN_IN) {
            twitterAuthClient.onActivityResult(requestCode, resultCode, data);
            return;

        }

        progressDialog.hide();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        progressDialog.hide();
        Toast.makeText(this, "Connection failed!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.imageViewGoogleSignIn:
                googleSignIn();
                break;
            case R.id.imageViewTwitterSignIn:
                // twitter login method goes here
                twitterSignIn();
                break;
        }

        progressDialog.show();

    }

    private void openMainActivity() {
        String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        User user = null;

        if (firebaseUser != null) {
            user = new User(firebaseUser.getUid(), firebaseUser.getEmail(), firebaseUser.getDisplayName());

            if (loginMethod == null) {
                loginMethod = readFromSharedPrefs();
            } else {
                saveToSharedPrefs(loginMethod);
            }

            user.setLoginMethod(loginMethod);
        }

        if (user != null) {
            DatabaseReference dbRef = db.getReference();
            DatabaseReference dbRefUser = dbRef.child("users").child(user.getUid());
            dbRefUser.child("email").setValue(user.getEmail());
            dbRefUser.child("name").setValue(user.getName());
            dbRefUser.child("uid").setValue(user.getUid());
            dbRefUser.child("loginMethod").setValue(user.getLoginMethod());
            mMessaging.subscribeToTopic(user.getUid());
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void initTwitterClient() {
        TwitterAuthConfig twitterAuthConfig =
                new TwitterAuthConfig(getResources().getString(R.string.twitterConsumerKey), getResources().getString(R.string.twitterConsumerKeySecret));
        TWITTER_SIGN_IN = twitterAuthConfig.getRequestCode();

        TwitterConfig twitterConfig = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .debug(true)
                .twitterAuthConfig(twitterAuthConfig)
                .build();

        Twitter.initialize(twitterConfig);

        twitterAuthClient = new TwitterAuthClient();
    }

    private void twitterSignIn() {
        twitterAuthClient.authorize(LoginActivity.this, new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                TwitterSession session = result.data;
                twitterFirebaseAuth(session);
            }

            @Override
            public void failure(TwitterException exception) {
                progressDialog.hide();
                Toast.makeText(LoginActivity.this, "Twitter login failed!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void twitterFirebaseAuth(TwitterSession session) {
        AuthCredential authCredential = TwitterAuthProvider.getCredential(
                session.getAuthToken().token,
                session.getAuthToken().secret);

        mAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loginMethod = "twitter";
                            openMainActivity();
                        }
                    }
                });

    }

    private void initGoogleClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getResources().getString(R.string.google0AuthKey))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void googleSignIn() {
        if (googleApiClient == null) {
            initGoogleClient();
        }

        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, GOOGLE_SIGN_IN);
    }

    private void googleFirebaseAuth(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loginMethod = "google";

                            // open MainActivity
                            openMainActivity();

                        } else {
                            // show error activity or something like that
                            Toast.makeText(LoginActivity.this, "Login failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private String readFromSharedPrefs() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getString("loginMethod", null);
    }

    private void saveToSharedPrefs(String method) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putString("loginMethod", method).apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null)
            progressDialog.dismiss();
    }
}
