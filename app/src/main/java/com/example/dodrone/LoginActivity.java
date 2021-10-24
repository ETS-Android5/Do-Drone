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
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.concurrent.CountDownLatch;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class LoginActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    public User thisUser = new User();

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


    public static class User {
        protected String uid;
        protected String nickname;
        protected Integer char_num, status;
        public DatabaseReference dbRef = FirebaseDatabase.getInstance("https://dodrone-4eebb-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference();
        public OnGetDataListener listener;
        private int tmp;
        private int flag=0;
        public User () {

        }

        public User (String nickname, int char_num, int status) {

        }

        public interface OnGetDataListener {
            void onSuccess(DataSnapshot dataSnapshot);
            void onStart();
            void onFailure();

        }

        public void updateStatus(int val) {
            String str_val = Integer.toString(val);
            dbRef.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (Integer.parseInt(snapshot.child("status").getValue().toString()) < val)
                    dbRef.child("Users").child(uid).child("status").setValue(str_val);
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
            /*if (status<val) {
                dbRef.child("Users").child(uid).child("status").setValue(this.status).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("user-class", "status update successful");
                    }
                });
            }*/
        }

        public void retrieveUserInfo(FirebaseUser user, final OnGetDataListener listener) {
            //listener.onStart();
            int fllag = 0;
            if (user != null) {
                uid = user.getUid();

                /*dbRef.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        thisUser.nickname = (String) snapshot.child("nickname").getValue();
                        thisUser.char_num = Integer.parseInt(String.valueOf(snapshot.child("char_num").getValue()));
                        thisUser.status = Integer.parseInt(String.valueOf(snapshot.child("status").getValue()));
                        Log.d("Login", thisUser.nickname + "\t" + thisUser.char_num + "\t" + thisUser.status);
                        flag=1;

                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });*/
                readData(dbRef.child("Users").child(uid), new OnGetDataListener() {

                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        Log.d("user-class", "listener onSuccess");
                        nickname = dataSnapshot.child("nickname").getValue().toString();
                        char_num = Integer.parseInt(dataSnapshot.child("char_num").getValue().toString());
                        status = Integer.parseInt(dataSnapshot.child("status").getValue().toString());
                        Log.d("user-class successful", nickname+" "+char_num+" "+status);
                    }

                    @Override
                    public void onStart() {
                        Log.d("user-class", "listener onStart");
                    }

                    @Override
                    public void onFailure() {
                        Log.d("user-class", "listener onFailure");
                    }

                });
            }



        }
        public void readData(DatabaseReference dbRef, final OnGetDataListener listener) {
            listener.onStart();
            dbRef.addValueEventListener(new ValueEventListener() {
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

        /*public boolean ctrlEnable(FirebaseUser user) {
            //CountDownLatch done = new CountDownLatch(1);
            Log.d("User-class", "starting ctrlEnable");

            if (user != null) {
                Log.d("user-clss", "this user status in ctrlEnable: "+ status);
                if (status == 10) return true;
                else return false;
            }
            else
                return false;


        }*/

    }




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance("https://dodrone-4eebb-default-rtdb.asia-southeast1.firebasedatabase.app/");
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
        Log.d("login", "creat request done");

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
                            assert user!=null;
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("char_num", 0);
                            hashMap.put("nickname", "알랭 두 드롱");
                            hashMap.put("status", 0);
                            databaseReference.child("Users").child(user.getUid())
                                    .setValue(hashMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d(TAG, "New member added.");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull @NotNull Exception e) {
                                            Log.d(TAG, "Failed attempt to add new user to realtime db.");
                                        }
                                    });
                            Log.d(TAG, "new user added to the table");
                            Intent intent = new Intent(getApplicationContext(), DoDroneActivity.class);
                            startActivity(intent);
                            thisUser.retrieveUserInfo(user, thisUser.listener);
                            Toast.makeText(LoginActivity.this, "Welcome "+user.getDisplayName()+".\n(Email: "+user.getEmail()+")", Toast.LENGTH_SHORT).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //updateUI(null);
                            Toast.makeText(LoginActivity.this, "Sorry auth failed", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

}
