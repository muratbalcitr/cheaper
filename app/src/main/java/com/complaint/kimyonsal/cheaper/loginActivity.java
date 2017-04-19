package com.complaint.kimyonsal.cheaper;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.complaint.kimyonsal.cheaper.Activity.MainActivity;
import com.complaint.kimyonsal.cheaper.Activity.giveBestCombine;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.TwitterAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import io.fabric.sdk.android.Fabric;


public class loginActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "cyn9UvrcvS0E843MUmsqN4UYW";
    private static final String TWITTER_SECRET = "wEVdKeWDHSEvRXmsetK3OJh9ntvkNm8qRERCsgrwR2LkQ0uwrD";

    LoginButton loginButton;

    private static final String TAG = TwitterAuthProvider.class.getSimpleName();
    private FirebaseAuth frAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference mRef;
    CallbackManager fcallbackManager;
    Button login;
    EditText etMail, etPassword;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private TwitterLoginButton loginButtonTwitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_login);


        etMail = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        //   AppEventsLogger.activateApp(this);
        frAuth = FirebaseAuth.getInstance();
        /*TwitterSession session = Twitter.getSessionManager().getActiveSession();
        TwitterAuthToken authToken = session.getAuthToken();
        String token = authToken.token;
        String secret = authToken.secret;

        TwitterAuthClient authClient = new TwitterAuthClient();
        authClient.requestEmail(session, new Callback<String>() {
            @Override
            public void success(Result<String> result) {
                // Do something with the result, which provides the email address
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
            }
        });*/
        //////////////////////
        loginButtonTwitter = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButtonTwitter.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model
                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void failure(com.twitter.sdk.android.core.TwitterException exception) {

            }
        });
        ///////////////////////
        fcallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginFacebook();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fcallbackManager.onActivityResult(requestCode, resultCode, data);
        loginButtonTwitter.onActivityResult(requestCode, resultCode, data);
    }

    private void loginFacebook() {

        fcallbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(fcallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("başarılı", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
                Intent i = new Intent(loginActivity.this, giveBestCombine.class);
                startActivity(i);
            }

            @Override
            public void onCancel() {
                Log.d("giriş iptal", "facebook:onCancel");

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "facebooka bağlanırken bir hata oluştu", Toast.LENGTH_SHORT).show();
            }


        });
    }

    private void handleFacebookAccessToken(com.facebook.AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        frAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Toast.makeText(getApplicationContext(), "başarılı", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void loginUsername(View view) {
        String mail = etMail.getText().toString();
        String parola = etPassword.getText().toString();
        frAuth.signInWithEmailAndPassword(mail, parola).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent i = new Intent(loginActivity.this, giveBestCombine.class);
                    startActivity(i);
                } else {
                    Toast.makeText(loginActivity.this, "giriş başarısız lütfen doğru mail olduğuna emin olun", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void registerRedirecting(View view) {
        Intent i = new Intent(loginActivity.this, register.class);
        startActivity(i);
    }
}
