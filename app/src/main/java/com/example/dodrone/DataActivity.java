package com.example.dodrone;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

//input output
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataActivity extends AppCompatActivity {
    final private static String TAG = "name_of_tag";

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int TAKE_PICTURE = 1;
    Button btn_photo;
    ImageView iv_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        iv_photo = findViewById(R.id.iv_photo);
        btn_photo = findViewById(R.id.btn_photo);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "권한 설정 완료");
            }
            else {
                Log.d(TAG, "권한 설정 요청");
                ActivityCompat.requestPermissions(DataActivity.this, new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }

        btn_photo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btn_photo:
                        Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(camIntent, TAKE_PICTURE);
                        break;
                }
            }
        });


    }

    //권한 요청하기
   @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                    @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult");
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1]
                == PackageManager.PERMISSION_GRANTED ) {
            Log.d(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
        }
    }

    // 짝은 사진 image view에 띄우기
    @Override protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case TAKE_PICTURE:
                if (resultCode == RESULT_OK && intent.hasExtra("data")) {
                    Bitmap bitmap = (Bitmap) intent.getExtras().get("data");
                    if (bitmap != null) { iv_photo.setImageBitmap(bitmap);
                    }
                }
                break;
        }
    }
}