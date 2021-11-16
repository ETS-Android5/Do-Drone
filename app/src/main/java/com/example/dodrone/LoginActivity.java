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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.concurrent.CountDownLatch;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class LoginActivity extends AppCompatActivity{

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    public OnGetDataListener loginListener;
    public User thisUser = new User();
    public boolean userCheck;


    private static final String TAG = "Do-Drone-Login";
    Button googleSignInBtn;

    protected void onStart() {
        super.onStart();

        FirebaseUser currUser = mAuth.getCurrentUser();
        //thisUser.retrieveUserInfo(currUser);

        if (currUser != null) {
            thisUser.retrieveUserInfo(currUser, thisUser.listener);
            Intent intent = new Intent(getApplicationContext(), DoDroneActivity.class);
            //intent.putExtra(EXTRA_MESSAGE, thisUser.status );
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("stat", thisUser.status);
            intent.putExtra("nickname", thisUser.nickname);
            intent.putExtra("char_num", thisUser.char_num);
            startActivity(intent);
            Toast.makeText(LoginActivity.this, "Welcome back "+currUser.getDisplayName()+".\n(Email: "+currUser.getEmail()+")", Toast.LENGTH_LONG).show();
            Log.d(TAG, "updateUI "+String.valueOf(currUser)+"\n"+currUser.getUid());


            finish();
        }

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d("login-check", "starting oncreate");
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase =   FirebaseDatabase.getInstance("https://dodrone-4eebb-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference();
        //int tmp = Integer.parseInt(databaseReference.child("Users").child(mAuth.getUid()).child("status").get().getResult().getValue().toString());


        createRequest();
        googleSignInBtn = findViewById(R.id.googleSignInBtn);
        googleSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGoogleSignInClient.signOut();
                resultLauncher.launch(new Intent(mGoogleSignInClient.getSignInIntent()));
                //Log.d(TAG, "SignInBtn clicked");
            }
        });
    }









    /* Login authorization functions*/

    private void createRequest() {

        //configure google sign in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //Build a googleSignInClient with the options specified by gso
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Log.d("login-check", "create request done");

    }

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {

            Log.d("login-check", "this is resultCode"+result.getResultCode()+"\nthis is Activity.RESULT_OK");
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent intent = result.getData();

                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(intent);
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    assert account != null;
                    firebaseAuthWithGoogle(account.getIdToken(), loginListener);
                    Log.d("lgoin-check", "ActivityResultLauncher task onActivityResult mehthod");
                } catch (ApiException e) {
                    //Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

        }
    });


    private void firebaseAuthWithGoogle(String idToken, final OnGetDataListener listener) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update MyPageActivity data?
                            FirebaseUser user = mAuth.getCurrentUser();
                            assert user!=null;
                            userCheck = false;

                            checkUserExist(databaseReference, new OnGetDataListener() {
                                @Override
                                public void onSuccess(DataSnapshot dataSnapshot) {
                                    Log.d("login-check", "check user listener onSuccess");
                                    if(dataSnapshot.child("Users").hasChild(user.getUid()))
                                    {
                                        Log.d("login-check", "user exists");
                                        Log.d("login-check", "single value listener" + Calendar.getInstance().getTime().toString());
                                        userCheck = true; //if user already exists in DB, userCheck is true
                                    }

                                    else {
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("char_num", 0);
                                        hashMap.put("nickname", "알랭 두 드롱");
                                        hashMap.put("status", 0);
                                        databaseReference.child("Users").child(user.getUid())
                                                .setValue(hashMap)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Log.d("login-check", "new member added " + Calendar.getInstance().getTime().toString());

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull @NotNull Exception e) {
                                                        Log.d("login-check", "Failed attempt to add new user to realtime db.");
                                                    }
                                                });
                                        Log.d("login-check", "new user added to the table");
                                    }

                                    Intent intent = new Intent(getApplicationContext(), DoDroneActivity.class);
                                    startActivity(intent);
                                    thisUser.retrieveUserInfo(user, thisUser.listener);
                                    Toast.makeText(LoginActivity.this, "Welcome "+user.getDisplayName()+".\n(Email: "+user.getEmail()+")", Toast.LENGTH_SHORT).show();
                                    Log.d("login-check", "retrieve user data from DB check\nnickname: "+thisUser.nickname);

                                    //if user does not exist in DB, userCheck remains to be false
                                }

                                @Override
                                public void onStart() {

                                }

                                @Override
                                public void onFailure() {

                                }
                            }, userCheck);

                            Log.d("login-check", "UserCheck value 2: "+userCheck);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("login-check", "signInWithCredential:failure", task.getException());
                            //updateUI(null);
                            Toast.makeText(LoginActivity.this, "Sorry auth failed", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    public void checkUserExist(DatabaseReference databaseReference, final OnGetDataListener listener, boolean userCheck) {
        listener.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                listener.onSuccess(snapshot);

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                listener.onFailure();
            }
        });

    }

}
