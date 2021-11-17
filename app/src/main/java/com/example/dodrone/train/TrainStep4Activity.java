package com.example.dodrone.train;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dodrone.LoginActivity;
import com.example.dodrone.R;
import com.example.dodrone.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class TrainStep4Activity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener{
    private static int STATUS_NUM9 = 9;
    Button nextStep9;
    FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
    User thisUser = new User();
    RecyclerView recyclerView;
    ArrayList<Column> columnArrayList;
    MyAdapter myAdapter;
    String[] label;
    int arrayPosition = -1;
    Button  closeBtn, camBtn, captureBtn, doneBtn,refresh;
    int[] guidePicList;
    CameraFragment cameraFragment;
    Bundle mSaverInstanceState;
    Drawable[] drawableList;
    Bitmap[] bitmapList;
    String[] filepathList;
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_train_step4);
        //thisUser.retrieveUserInfo(currUser);
        thisUser.retrieveUserInfo(currUser, thisUser.listener);

        nextStep9 = findViewById(R.id.nextStep_9);
        nextStep9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TrainStep5Activity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //intent.putExtra("EXIT", true);
                startActivity(intent);
                finish();
                //finishAffinity();


                thisUser.updateStatus(STATUS_NUM9);

            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        columnArrayList = new ArrayList<Column>();
        myAdapter = new MyAdapter(this, columnArrayList);
        recyclerView.setAdapter(myAdapter);

        mSaverInstanceState=savedInstanceState;
        cameraFragment = new CameraFragment();

        captureBtn = findViewById(R.id.captureBtn);
        doneBtn = findViewById(R.id.doneBtn);
        refresh = findViewById(R.id.refresh);
        refresh.setVisibility(View.VISIBLE);
        nextStep9.setVisibility(View.VISIBLE);


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0; i<label.length; i++) {

                    //Column column = new Column(label[i], guidePicList[i], drawableList[i], camBtn);
                    columnArrayList.set(i, new Column(label[i], guidePicList[i], bitmapList[i], camBtn));

                }
                myAdapter.notifyDataSetChanged();
            }
        });

        label = new String[] {
                "1. Up 위", "2. Down 아래",
                "3. Left 왼쪽", "4. Right 오른쪽",
                "5. Forward 전진", "6. Backward 후진",
                "7. Stop 정지"
        };

        guidePicList = new int[] {
                R.drawable.label1, R.drawable.label2, R.drawable.label3, R.drawable.label4,
                R.drawable.label5, R.drawable.label6, R.drawable.label7
        };

        drawableList = new Drawable[7];
        Arrays.fill(drawableList, null);

        bitmapList = new Bitmap[7];
        Arrays.fill(bitmapList, null);

        filepathList = new String[7];
        //Arrays.setAll(filepathList, android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+"/CameraXPhotos");
        getData();

    }

    private void getData() {
        for (int i=0; i<label.length; i++) {

            //Column column = new Column(label[i], guidePicList[i], drawableList[i], camBtn);
            //Column column = new Column(label[i], guidePicList[i], drawableList[i], camBtn);
            Column column = new Column(label[i], guidePicList[i], bitmapList[i], camBtn);
            columnArrayList.add(column);

        }
        myAdapter.notifyDataSetChanged();
    }


    @Override
    public void onBackStackChanged() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(getSupportFragmentManager().getBackStackEntryCount()>0);

    }

    public void redrawRecycle(int index) {
        recyclerView.setAdapter(null);
        recyclerView.setLayoutManager(null);
        recyclerView.getRecycledViewPool().clear();
        Log.d("whatswrong", "redraw method");

        drawableList[index] = Drawable.createFromPath(String.valueOf(new File(filepathList[index])));
        bitmapList[index] = ((BitmapDrawable)drawableList[index]).getBitmap();
        bitmapList[index] = flip(bitmapList[index]);
        //getData();
        recyclerView.swapAdapter(myAdapter, false);
        recyclerView.setLayoutManager(layoutManager);
        myAdapter.notifyDataSetChanged();
    }

    public static Bitmap flip(Bitmap src)
    {
        // create new matrix for transformation
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);

        // return transformed image
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }
}