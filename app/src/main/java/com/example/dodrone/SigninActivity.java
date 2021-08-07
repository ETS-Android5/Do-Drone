package com.example.dodrone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dodrone.databinding.ActivityMainBinding;
import com.example.dodrone.databinding.ActivitySigninBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class SigninActivity extends AppCompatActivity {

    //view binding
    private ActivitySigninBinding binding;

    private static final int RC_SIGN_IN = 100;
    private GoogleSignInClient googleSignInClient;

    private FirebaseAuth firebaseAuth;

    private static final String TAG = "GOOGLE_SIGN_IN_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySigninBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //configure the google signin
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        googleSignInClient= GoogleSignIn.getClient(this, googleSignInOptions);

        //init firebase auth
        firebaseAuth = firebaseAuth.getInstance();
        checkUser();

        //google sign-in btn - click to begin Google Sign in
        //binding.googleSignInBtn.setOnClickListener()
        binding.googleSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //begin google sign in
                Log.d(TAG, "onClick: being Google Sign in");
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent, RC_SIGN_IN);
                //startActivityForResult is deprecated. find some other way

            }
        });
    }

    private void checkUser() {
        //if user is already signed in then go to profile activity
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            Log.d(TAG, "checkUser: Already logged in");
            startActivity(new Intent(this, ProfileActivity.class));
            finish();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Result returned from launching from launching the Internet from GoogleSignInApi.getSignInIntent(..);
        if (requestCode == RC_SIGN_IN) {
            Log.d(TAG, "onActivityResult: Google SignIn intent result");
            Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //google sign in success, now auth with firebase
                GoogleSignInAccount account = accountTask.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            }
            catch (Exception e) {
                //failed google sign in
                Log.d(TAG, "onActivityResult"+e.getMessage());

            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle: begin firebase auth with google account");
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {

            @Override
            public void onSuccess(AuthResult authResult) {
                //log in success
                Log.d(TAG, "onSuccess: logged in");

                //get logged in user
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                //get uer info
                String uid = firebaseUser.getUid();
                String email = firebaseUser.getEmail();
                Log.d(TAG, "onSuccess: Email"+email);
                Log.d(TAG, "onSuccess: UID"+uid);

                //check if user is new or existing
                if(authResult.getAdditionalUserInfo().isNewUser()) {
                    //user is new - Account Created
                    Log.d(TAG, "onSuccess: Account created...\n"+email);
                    Toast.makeText(SigninActivity.this, "account Created...\n"+email, Toast.LENGTH_SHORT).show();
                }
                else {
                    //existing user - Logged In
                    Log.d(TAG, "onSuccess: Existing user...\n"+email);
                    Toast.makeText(SigninActivity.this, "Existing user...\n"+email, Toast.LENGTH_SHORT).show();

                }

                //start profile activity
                startActivity(new Intent(SigninActivity.this, ProfileActivity.class));
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}
