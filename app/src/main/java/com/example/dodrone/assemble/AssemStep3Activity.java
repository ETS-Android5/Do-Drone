package com.example.dodrone.assemble;

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

public class AssemStep3Activity extends AppCompatActivity {
    private static int STATUS_NUM3 = 3;
    Button nextStep3;
    FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
    LoginActivity.User thisUser = new LoginActivity.User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("stack", "assem_3");
        setContentView(R.layout.activity_assem_step3);
        //thisUser.retrieveUserInfo(currUser);

        ArrayList<String> userInfo = new ArrayList<>();
        thisUser.retrieveUserInfo(currUser, thisUser.listener);
        nextStep3 = findViewById(R.id.nextStep_3);
        nextStep3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AssemStep4Activity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //intent.putExtra("EXIT", true);
                startActivity(intent);
                //finish();
                finishAffinity();
                thisUser.updateStatus(STATUS_NUM3);

            }
        });
    }
}