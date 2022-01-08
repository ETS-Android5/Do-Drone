package com.example.dodrone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.SignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class MyPageActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private static final String TAG = "MyPage check";

    String uid;
    String nickname;
    Integer char_num, status;
    TextView name, mail, nicknameTv, statusTv;
    ImageView character;
    Button logoutBtn;
    Button modifyBtn;
    String watchedVid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
        logoutBtn = findViewById(R.id.logoutBtn);
        name = findViewById(R.id.name);
        mail = findViewById(R.id.mail);
        nicknameTv = findViewById(R.id.nickname);
        statusTv = findViewById(R.id.status);
        character = findViewById(R.id.character);
        modifyBtn = findViewById(R.id.modifyBtn);


        User thisUser = new User();

        thisUser.retrieveUserInfo(currUser, thisUser.listener);
        Log.d("Login", "1\n" + thisUser.nickname + "/t" + thisUser.status);

        //firebaseDatabase = FirebaseDatabase.getInstance("https://dodrone-4eebb-default-rtdb.asia-southeast1.firebasedatabase.app/");
        //databaseReference = firebaseDatabase.getReference("Users");
        //LoginActivity.User thisUser = new LoginActivity.User();
        //thisUser.retrieveUserInfo(currUser);
        //if (currUser != null) uid = currUser.getUid();
        //nickname = databaseReference.child("")


        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (signInAccount != null) {
            name.setText(signInAccount.getDisplayName());
            mail.setText(signInAccount.getEmail());

            thisUser.dbRef.child("Users").child(currUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful())
                        Log.d("User-class", "Error getting data", task.getException());
                    else {
                        nicknameTv.setText(task.getResult().child("nickname").getValue().toString());
                        statusTv.setText(task.getResult().child("status").getValue().toString());
                        int tmp = Integer.parseInt(task.getResult().child("char_num").getValue().toString());
                        character.setImageDrawable(user_char(tmp));
                    }

                }
            });
        }

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //clear all previous activities
                startActivity(intent);

                finish();
            }
        });

        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ModifyUserInfoActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public Drawable user_char(int char_num) {
        Drawable char_draw = null;
        if (char_num == 0)
            char_draw = getResources().getDrawable(R.drawable.alan_hat);
        else if (char_num == 1)
            char_draw = getResources().getDrawable(R.drawable.tom_hat);

        return char_draw;
    }

    private void watchedVid(int status){
        switch(status) {
            case 0:
                watchedVid = getResources().getString(R.string.class0);
                break;
            case 1:
                watchedVid = getResources().getString(R.string.class1);
                break;
            case 2:
                watchedVid = getResources().getString(R.string.class2);
                break;
            case 3:
                watchedVid = getResources().getString(R.string.class3);
                break;

        }
    }

}

