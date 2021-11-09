package com.example.dodrone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.dodrone.assemble.AssemblyMainActivity;
import com.example.dodrone.controlling.CtrlMainActivity;
import com.example.dodrone.train.TrainMainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class DoDroneActivity extends AppCompatActivity{

    Button assemBtn, trainBtn, ctrlBtn, refreshBtn;
    ImageView profile_img;
    TextView profile_nick;
    String nickname;
    int char_num;
    int stat;
    FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("User-class", "do drone activity start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_drone);

        stat = getIntent().getIntExtra("stat", 0);
        nickname = getIntent().getStringExtra("nickname");
        char_num = getIntent().getIntExtra("char_num", 0);
        Log.d("user-class", "intent val: "+stat);

        /*LoginActivity.User thisUser = new LoginActivity.User();
        thisUser.retrieveUserInfo(currUser,thisUser.listener);
        nickname = thisUser.nickname;
        stat = thisUser.status;
        char_num = thisUser.char_num;*/

        refreshBtn = findViewById(R.id.refreshBtn);
        assemBtn = findViewById(R.id.assemBtn);
        trainBtn = findViewById(R.id.trainBtn);
        ctrlBtn = findViewById(R.id.ctrlBtn);
        profile_img = findViewById(R.id.nav_header_profile_img);
        profile_nick = findViewById(R.id.nav_header_nick);

        //FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
        //LoginActivity.User thisUser = new LoginActivity.User();
        //thisUser.retrieveUserInfo(currUser, thisUser.listener);

        ctrlBtn.setEnabled(ctrlEnable(stat));
        Log.d("user-class", "stat: "+stat+" \nctrlEnable result "+ctrlEnable(stat));

        refreshBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                refreshPage();
            }
        });

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
                Intent trainIntent = new Intent(getApplicationContext(), CtrlMainActivity.class);
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

        /*findViewById(R.id.closeBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.END);
            }
        });*/

        //profile_img.setImageDrawable(profileChar(char_num));
        //profile_nick.setText(nickname);
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

    public boolean ctrlEnable(int stat) {
        if (stat == 10) return true;
        else return false;
    }

    public Drawable profileChar (int char_num) {
        Drawable char_draw = null;
        if (char_num == 0)
            char_draw = getResources().getDrawable(R.drawable.alan_hi);
        else if (char_num == 1)
            char_draw = getResources().getDrawable(R.drawable.tom_hi);

        return char_draw;
    }



    private void refreshPage() {
        LoginActivity.User refreshUser = new LoginActivity.User();
        refreshUser.retrieveUserInfo(currUser, refreshUser.listener);
        nickname = refreshUser.nickname;
        stat = refreshUser.status;
        char_num = refreshUser.char_num;
        Log.d("user-class", nickname+" "+stat+" "+char_num);

        Intent refresh = new Intent(getApplicationContext(), DoDroneActivity.class);
        refresh.putExtra("stat", stat);
        refresh.putExtra("nickname", nickname);
        refresh.putExtra("char_num", char_num);
        //refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(refresh);
        finish();
    }
}