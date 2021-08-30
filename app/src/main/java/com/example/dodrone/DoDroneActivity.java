package com.example.dodrone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;

import com.example.dodrone.assemble.AssemblyMainActivity;
import com.example.dodrone.controlling.CtrlMainActivity;
import com.example.dodrone.train.TrainMainActivity;

public class DoDroneActivity extends AppCompatActivity {

    Button assemBtn, trainBtn, ctrlBtn, myPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_drone);

        assemBtn = findViewById(R.id.assemBtn);
        trainBtn = findViewById(R.id.trainBtn);
        ctrlBtn = findViewById(R.id.ctrlBtn);
        myPage = findViewById(R.id.myPage);

        assemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent assemIntent = new Intent(getApplicationContext(), AssemblyMainActivity.class);
                startActivity(assemIntent);
            }
        });

        trainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent trainIntent = new Intent(getApplicationContext(), TrainMainActivity.class);
                startActivity(trainIntent);
            }
        });

        ctrlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ctrlIntent = new Intent(getApplicationContext(), CtrlMainActivity.class);
                startActivity(ctrlIntent);
            }
        });

        myPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myPageIntent = new Intent(getApplicationContext(), MyPageActivity.class);
                startActivity(myPageIntent);
            }
        });
    }





}