package com.example.dodrone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class ModifyUserInfoActivity extends AppCompatActivity {
    TextView userName, userEmail;
    EditText userNick;
    RadioGroup charSel;
    RadioButton alan, tom;
    Button saveBtn;
    int rbClicked, new_char_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_user_info);

        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
        User thisUser = new User();
        thisUser.retrieveUserInfo(currUser, thisUser.listener);

        rbClicked =0;
        new_char_num =0;

        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        userNick = findViewById(R.id.userNick);
        charSel = findViewById(R.id.charSel);
        saveBtn = findViewById(R.id.saveBtn);
        alan = findViewById(R.id.radioAlan);
        tom = findViewById(R.id.radioTom);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (signInAccount != null) {
            userName.setText(signInAccount.getDisplayName());
            userEmail.setText(signInAccount.getEmail());

            userNick.setText(thisUser.nickname);

        }




        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rbClicked==1 && userNick.getText().toString().isEmpty() == false)
                {
                    Log.d("mod-act", "save btn clicked");
                    thisUser.dbRef.child("Users").child(thisUser.uid).child("char_num").setValue(new_char_num);
                    thisUser.dbRef.child("Users").child(thisUser.uid).child("nickname").setValue(userNick.getText().toString());

                    Intent intent =  new Intent(getApplicationContext(), MyPageActivity.class);
                    startActivity(intent);
                    finish();
                    //recreate();

                }
                if (rbClicked != 1 || userNick.getText().toString().isEmpty() == true)
                {
                    Toast.makeText(ModifyUserInfoActivity.this, "정보를 모두 입력해주세요.", Toast.LENGTH_LONG).show();
                    Log.d("mod-act", "rbClicked val: "+rbClicked+" \n"+"userNick Val: "+userNick.getText().toString()+" "+userNick.getText().toString().isEmpty());
                    if (rbClicked !=1) Log.d("mod-act", "radio button 값 못 읽어옴");
                    else if (userNick.getText().toString().isEmpty() == true) Log.d("mod-act", "nickname edit text 문제");
                }
            }
        });

    }

    public void rbClick(View v) {
        if (alan.isChecked()) {
            rbClicked = 1;
            new_char_num =0;
        }
        else if (tom.isChecked()) {
            rbClicked = 1;
            new_char_num = 1;
        }
    }


}