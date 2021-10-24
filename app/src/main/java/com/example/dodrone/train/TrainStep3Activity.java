package com.example.dodrone.train;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dodrone.LoginActivity;
import com.example.dodrone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class TrainStep3Activity extends AppCompatActivity {
    private static int STATUS_NUM8 = 8;
    Button nextStep8;
    FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
    LoginActivity.User thisUser = new LoginActivity.User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("stack", "train_3");
        setContentView(R.layout.activity_train_step3);
        //thisUser.retrieveUserInfo(currUser);

        thisUser.retrieveUserInfo(currUser, thisUser.listener);
        nextStep8 = findViewById(R.id.nextStep_8);
        nextStep8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TrainStep4Activity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //intent.putExtra("EXIT", true);
                startActivity(intent);
                //finish();
                finishAffinity();
                thisUser.updateStatus(STATUS_NUM8);

            }
        });
    }
}