package com.example.dodrone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_viewChange = (Button) findViewById(R.id.btn_viewChange);
        btn_viewChange.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //Main에서 이동하는 것
                Intent intent =  new Intent(getApplicationContext(), PhotoActivity.class);
                startActivity(intent);

            }
        });
    }

}