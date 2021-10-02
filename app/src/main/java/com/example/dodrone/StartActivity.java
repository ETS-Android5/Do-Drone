package com.example.dodrone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class StartActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;

    Button registerBtn;
    Button loginBtn;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);

        registerBtn = findViewById(R.id.registerBtn);
        loginBtn = findViewById(R.id.loginBtn);

        mAuth = FirebaseAuth.getInstance();
        //createRequest();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reg = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(reg);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //resultLauncher.launch(new Intent(mGoogleSignInClient.getSignInIntent()));
                FirebaseUser user = mAuth.getCurrentUser();
                Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(login);
            }
        });



    }

    /*private void createRequest() {

        //configure google sign in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //Build a googleSignInClient with the options specified by gso
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {

            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent intent = result.getData();

                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(intent);
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    assert account != null;
                    firebaseAuthWithGoogle(account.getIdToken());
                } catch (ApiException e) {
                    //Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

        }
    });


    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update MyPageActivity data?
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(getApplicationContext(), DoDroneActivity.class);
                            startActivity(intent);
                            Toast.makeText(StartActivity.this, "Welcome "+user.getDisplayName()+".\n(ID: "+user.getEmail()+")\n", Toast.LENGTH_LONG).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //updateUI(null);
                            Toast.makeText(StartActivity.this, "Sorry auth failed", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    private void login() {
    }*/
}
