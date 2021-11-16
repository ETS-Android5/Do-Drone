package com.example.dodrone.assemble;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.dodrone.LoginActivity;
import com.example.dodrone.R;
import com.example.dodrone.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class AssemStep1Activity extends AppCompatActivity {
    private static int STATUS_NUM1 = 1;
    Button nextStep1;
    FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
    User thisUser = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("stack", "assem_1");
        setContentView(R.layout.activity_assem_step1);

        thisUser.retrieveUserInfo(currUser, thisUser.listener);

        nextStep1 = findViewById(R.id.nextStep_1);
        nextStep1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AssemStep2Activity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //intent.putExtra("EXIT", true);
                startActivity(intent);
                finish();
                //finishAffinity();


                thisUser.updateStatus(STATUS_NUM1);


            }
        });
    }
}