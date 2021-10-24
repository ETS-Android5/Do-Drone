package com.example.dodrone.assemble;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import com.example.dodrone.R;

public class AssemblyMainActivity extends AppCompatActivity {
    Button step1, step2, step3, step4, step5;
    ImageView rImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assembly_main);

        step1 = findViewById(R.id.assem_step_1);
        step2 = findViewById(R.id.assem_step_2);
        step3 = findViewById(R.id.assem_step_3);
        step4 = findViewById(R.id.assem_step_4);
        step5 = findViewById(R.id.assem_step_5);

        /*Button.OnClickListener onClickListener = new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent stepIntent;
                switch (v.getId()) {
                    case R.id.assem_step_1:
                        stepIntent = new Intent(getApplicationContext(), AssemStep1Activity.class);
                        startActivity(stepIntent);
                        break;
                    case R.id.assem_step_2:
                        stepIntent = new Intent(getApplicationContext(), AssemStep2Activity.class);
                        startActivity(stepIntent);
                        break;
                    case R.id.assem_step_3:
                        stepIntent = new Intent(getApplicationContext(), AssemStep3Activity.class);
                        startActivity(stepIntent);
                        break;
                    case R.id.assem_step_4:
                        stepIntent = new Intent(getApplicationContext(), AssemStep4Activity.class);
                        startActivity(stepIntent);
                        break;
                    case R.id.assem_step_5:
                        stepIntent = new Intent(getApplicationContext(), AssemStep5Activity.class);
                        startActivity(stepIntent);
                        break;

                }
            }
        };

        step1.setOnClickListener(onClickListener);
        step2.setOnClickListener(onClickListener);
        step3.setOnClickListener(onClickListener);
        step4.setOnClickListener(onClickListener);
        step5.setOnClickListener(onClickListener);*/



        step1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stepIntent = new Intent(getApplicationContext(), AssemStep1Activity.class);
                startActivity(stepIntent);
            }
        });

        step2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stepIntent = new Intent(getApplicationContext(), AssemStep2Activity.class);
                startActivity(stepIntent);
            }
        });

        step3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stepIntent = new Intent(getApplicationContext(), AssemStep3Activity.class);
                startActivity(stepIntent);
            }
        });

        step4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stepIntent = new Intent(getApplicationContext(), AssemStep4Activity.class);
                startActivity(stepIntent);
            }
        });

        step5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stepIntent = new Intent(getApplicationContext(), AssemStep5Activity.class);
                startActivity(stepIntent);
            }
        });


    }
}

