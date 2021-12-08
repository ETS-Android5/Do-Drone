package com.example.dodrone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
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
import com.example.dodrone.DetectorActivity;
import com.example.dodrone.train.TrainMainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class DoDroneActivity extends AppCompatActivity{

    Button assemBtn, trainBtn, ctrlBtn;
    ImageView profile_img;
    TextView profile_nick;
    String nickname;
    int char_num;
    int stat;
    FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
    User thisUser = new User();
    OnGetDataListener dataListener;
    private OnUserDataListener userListener = new OnUserDataListener() {
        @Override
        public void getUserDataStart() {

            Log.d("user-class", "getting user data started");
        }

        @Override
        public void getUserDataDone() {
            Log.d("user-class", "user data retrieved");
            setUI();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("User-class", "do drone activity start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_drone);
        thisUser.retrieveUserInfoForDoDrone(currUser, thisUser.listener, userListener);
        Log.d("user-class", "intent val: "+stat);
        assemBtn = findViewById(R.id.assemBtn);
        trainBtn = findViewById(R.id.trainBtn);
        ctrlBtn = findViewById(R.id.ctrlBtn);

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




    }

    private void setUI() {



        profile_img = findViewById(R.id.nav_header_profile_img);
        profile_nick = findViewById(R.id.nav_header_nick);





        ctrlButton();

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

        profile_img.setImageDrawable(profileChar(thisUser.char_num));
        profile_nick.setText(thisUser.nickname);
        NavigationView navigationView = findViewById(R.id.navView);
        navigationView.setItemIconTintList(null);
    }

    private void ctrlButton(){

        //ctrlBtn.setEnabled(ctrlEnable(stat));
        //조종하기 page
        ctrlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent ctrlIntent = new Intent(getApplicationContext(), CtrlMainActivity.class);
                //startActivity(ctrlIntent);
                droneAlert(this);
            }
        });
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





    //드론 연결하기
    public void droneAlert(View.OnClickListener view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("드론을 연결하시겠습니까?");
        builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("ctrl-alert", "ok clicked");
                // 드론 연결 - 와이파이 선택 창
                // 조종하기 page
                Intent intent = new Intent(getApplicationContext(), CtrlMainActivity .class);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "홈으로 돌아갑니다.", Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(getApplicationContext(), DoDroneActivity.class);
                //startActivity(intent);
                //finish();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}