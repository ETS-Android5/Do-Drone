package com.example.dodrone.assemble;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dodrone.DoDroneActivity;
import com.example.dodrone.LoginActivity;
import com.example.dodrone.R;
import com.example.dodrone.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class AssemStep5Activity extends AppCompatActivity {
    private static int STATUS_NUM5 = 5;
    Button nextStep5;
    FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("stack", "assem_3");
        setContentView(R.layout.activity_assem_step5);


        User thisUser = new User();
        thisUser.retrieveUserInfo(currUser, thisUser.listener);


        nextStep5 = findViewById(R.id.nextStep_5);
        nextStep5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DoDroneActivity.class);

                thisUser.updateStatus(STATUS_NUM5);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(intent);



            }
        });
    }
}