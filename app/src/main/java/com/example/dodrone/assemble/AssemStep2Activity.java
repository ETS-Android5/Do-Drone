package com.example.dodrone.assemble;

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

public class AssemStep2Activity extends AppCompatActivity {
    private static int STATUS_NUM2 = 2;
    Button nextStep2;
    FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
    User thisUser = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("stack", "assem_2");

        setContentView(R.layout.activity_assem_step2);
        ArrayList<String> userInfo = new ArrayList<>();
        thisUser.retrieveUserInfo(currUser, thisUser.listener);


        nextStep2 = findViewById(R.id.nextStep_2);
        nextStep2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AssemStep3Activity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //intent.putExtra("EXIT", true);
                startActivity(intent);
                //finishAffinity();
                finish();
                thisUser.updateStatus(STATUS_NUM2);


            }
        });
    }
}