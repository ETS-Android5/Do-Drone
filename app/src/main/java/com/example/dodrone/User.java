package com.example.dodrone;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class User {
    protected String uid;
    protected String nickname;
    protected Integer char_num, status;
    public DatabaseReference dbRef = FirebaseDatabase.getInstance("https://dodrone-4eebb-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference();
    public OnGetDataListener listener;
    private int tmp;
    private int flag = 0;
    public OnUserDataListener userDataListener;

    public User() {

    }

    public User(OnUserDataListener listener) {
        this.userDataListener = listener;
    }

    public User(String nickname, int char_num, int status) {

    }


    public void updateStatus(int val) {
        String str_val = Integer.toString(val);
        dbRef.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (Integer.parseInt(snapshot.child("status").getValue().toString()) < val)
                    dbRef.child("Users").child(uid).child("status").setValue(str_val);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
            /*if (status<val) {
                dbRef.child("Users").child(uid).child("status").setValue(this.status).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("user-class", "status update successful");
                    }
                });
            }*/
    }

    public void retrieveUserInfo(FirebaseUser user, final OnGetDataListener listener) {
        int fllag = 0;
        if (user != null) {
            uid = user.getUid();

            readData(dbRef.child("Users").child(uid), new OnGetDataListener() {

                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    Log.d("user-class", "listener onSuccess");
                    nickname = dataSnapshot.child("nickname").getValue().toString();
                    char_num = Integer.parseInt(dataSnapshot.child("char_num").getValue().toString());
                    status = Integer.parseInt(dataSnapshot.child("status").getValue().toString());
                    Log.d("user-class successful", nickname + " " + char_num + " " + status);
                }

                @Override
                public void onStart() {
                    Log.d("user-class", "listener onStart");
                }

                @Override
                public void onFailure() {
                    Log.d("user-class", "listener onFailure");
                }

            });
        }


    }


    public void retrieveUserInfoForDoDrone(FirebaseUser user, final OnGetDataListener listener, final OnUserDataListener userListener) {
        int fllag = 0;
        if (user != null) {
            uid = user.getUid();
            this.userDataListener = userListener;
            readData(dbRef.child("Users").child(uid), new OnGetDataListener() {

                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    userDataListener.getUserDataStart();
                    Log.d("user-class", "listener onSuccess");
                    nickname = dataSnapshot.child("nickname").getValue().toString();
                    char_num = Integer.parseInt(dataSnapshot.child("char_num").getValue().toString());
                    status = Integer.parseInt(dataSnapshot.child("status").getValue().toString());
                    Log.d("user-class successful", nickname + " " + char_num + " " + status);

                }

                @Override
                public void onStart() {
                    Log.d("user-class", "listener onStart");
                }

                @Override
                public void onFailure() {userDataListener.getUserDataDone();
                }

            });
        }



    }


    public void readData(DatabaseReference dbRef, final OnGetDataListener listener) {
        listener.onStart();
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Log.d("user-class", "readData onDataChange start");
                listener.onSuccess(snapshot);
                Log.d("user-class","readData onDataChange end");
                listener.onFailure();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                listener.onFailure();
            }
        });

    }

        /*public boolean ctrlEnable(FirebaseUser user) {
            //CountDownLatch done = new CountDownLatch(1);
            Log.d("User-class", "starting ctrlEnable");

            if (user != null) {
                Log.d("user-clss", "this user status in ctrlEnable: "+ status);
                if (status == 10) return true;
                else return false;
            }
            else
                return false;


        }*/

}
