package com.example.dodrone.controlling;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.dodrone.R;
//import org.tensorflow.lite.Converter;
import org.tensorflow.lite.support.model.Model;
import org.tensorflow.lite.gpu.CompatibilityList;
import org.tensorflow.lite.gpu.GpuDelegate;


public class CtrlMainActivity extends AppCompatActivity {

    private static final String OBJECT_DETECTOR_NATIVE_LIB = "task_vision_jni";
    private static final int OPTIONAL_FD_LENGTH = -1;
    private static final int OPTIONAL_FD_OFFSET = -1;

    // Initialize interpreter with GPU delegate
    Model.Options options;
    CompatibilityList compatList = new CompatibilityList();

    if(compatList.isDelegateSupportedOnThisDevice()){
        // if the device has a supported GPU, add the GPU delegate
        options = new Model.Options.Builder().setDevice(Model.Device.GPU).build();
    } else {
        // if the GPU is not supported, run on 4 threads
        options = new Model.Options.Builder().setNumThreads(4).build();
    }

    MyModel myModel = new MyModel.newInstance(context, options);

    // Run inference per sample code


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ctrl_main);
    }

}

