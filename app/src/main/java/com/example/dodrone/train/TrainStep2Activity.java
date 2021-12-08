package com.example.dodrone.train;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dodrone.LoginActivity;
import com.example.dodrone.R;
import com.example.dodrone.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class TrainStep2Activity extends AppCompatActivity {
    private static int STATUS_NUM7 = 7;
    Button nextStep7;
    FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
    User thisUser = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("stack", "train_2");

        setContentView(R.layout.activity_train_step2);
        thisUser.retrieveUserInfo(currUser, thisUser.listener);

        nextStep7 = findViewById(R.id.nextStep_7);
        nextStep7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TrainStep3Activity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //intent.putExtra("EXIT", true);
                startActivity(intent);
                //finishAffinity();
                finish();
                thisUser.updateStatus(STATUS_NUM7);


            }
        });
    }
}