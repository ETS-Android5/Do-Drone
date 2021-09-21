package com.example.dodrone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.dodrone.assemble.AssemblyMainActivity;
import com.example.dodrone.controlling.CtrlMainActivity;
import com.example.dodrone.train.TrainMainActivity;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

public class DoDroneActivity extends AppCompatActivity{

    Button assemBtn, trainBtn, ctrlBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_drone);

        assemBtn = findViewById(R.id.assemBtn);
        trainBtn = findViewById(R.id.trainBtn);
        ctrlBtn = findViewById(R.id.ctrlBtn);

        ctrlBtn.setEnabled(false);

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



        //navigation 설정
        final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });

        NavigationView navigationView = findViewById(R.id.navView);
        navigationView.setItemIconTintList(null);
    }

    public boolean menuOnclick(MenuItem item) {
        int id= item.getItemId();
        if (id==R.id.nav_home) {
            Intent intent = new Intent(this, DoDroneActivity.class);
            startActivity(intent);
        }
        if (id==R.id.nav_myPage) {
            Intent intent = new Intent(this, MyPageActivity.class);
            startActivity(intent);
        }
        if (id==R.id.nav_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}