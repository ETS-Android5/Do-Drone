package com.example.dodrone.controlling;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.view.PreviewView;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.example.dodrone.DoDroneActivity;
import com.example.dodrone.R;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.tensorflow.lite.Interpreter;

import android.util.Size;
import android.graphics.Point;
import android.content.res.AssetManager;
import android.graphics.Matrix;
import android.view.LayoutInflater;
import java.net.SocketAddress;
import android.util.Pair;
import android.graphics.Color;
import android.view.Gravity;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

//tflite
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions;
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader;
import com.google.firebase.ml.modeldownloader.*;
import com.google.firebase.ml.modeldownloader.CustomModel;
import com.google.firebase.ml.modeldownloader.DownloadType;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions;

import java.io.File;



public class CtrlMainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener{
    private static final String TAG = "MainActivity";
    private String mSelectedCam = "";
    private String mSelectedCamPre = "";
    Bundle mSavedInstanceState;
    boolean mConnected = false;
    private CtrlMainFragment mFragmentCam = null;
    private Menu mMenu = null;
    CtrlMainFragment cameraFragment;

    private static final String OBJECT_DETECTOR_NATIVE_LIB = "task_vision_jni";
    private static final int OPTIONAL_FD_LENGTH = -1;
    private static final int OPTIONAL_FD_OFFSET = -1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("tag", "Creating Success");
        setContentView(R.layout.activity_ctrl_main);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        mSavedInstanceState = savedInstanceState;
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        cameraFragment = new CtrlMainFragment();
        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().add(R.id.fragment, cameraFragment, "camera").commit();
        else
            onBackStackChanged();

        CustomModelDownloadConditions conditions = new CustomModelDownloadConditions.Builder()
                .requireWifi()
                .build();
        FirebaseModelDownloader.getInstance()
                .getModel("Hand-Detector", DownloadType.LOCAL_MODEL, conditions)
                .addOnSuccessListener(new OnSuccessListener<CustomModel>() {
                    @Override
                    public void onSuccess(CustomModel model) {
                        // Download complete. Depending on your app, you could enable
                        // the ML feature, or switch from the local model to the remote
                        // model, etc.
                        File modelFile = model.getFile();
                        if(modelFile != null){
                            //interpreter = new Interpreter(modelFile);
                        }
                    }
                });



        // Initialization
        //ObjectDetectorOptions options = ObjectDetectorOptions.builder()
        //        .setScoreThreshold(0)  // Evaluate your model in the Google Cloud Console
                // to determine an appropriate value.
        //        .build();
        //ObjectDetector objectDetector = ObjectDetector.createFromFileAndOptions(context, modelFile, options);
    }

    /*
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        cameraFragment.onWindowFocusChanged();
    }

     */

    @Override
    public void onBackStackChanged() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(getSupportFragmentManager().getBackStackEntryCount()>0);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.layout.menu_main, menu);
//        mMenu = menu;
//        return true;
//    }


}
