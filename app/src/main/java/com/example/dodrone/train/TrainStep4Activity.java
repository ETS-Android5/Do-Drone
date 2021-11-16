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

public class TrainStep4Activity extends AppCompatActivity {
    private static int STATUS_NUM9 = 9;
    Button nextStep9;
    FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
    User thisUser = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("stack", "assem_4");
        setContentView(R.layout.activity_train_step4);
        //thisUser.retrieveUserInfo(currUser);
        thisUser.retrieveUserInfo(currUser, thisUser.listener);

        nextStep9 = findViewById(R.id.nextStep_9);
        nextStep9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TrainStep5Activity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //intent.putExtra("EXIT", true);
                startActivity(intent);
                finish();
                //finishAffinity();


                thisUser.updateStatus(STATUS_NUM9);

            }
        });
    }
}