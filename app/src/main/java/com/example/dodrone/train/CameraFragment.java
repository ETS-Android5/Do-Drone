package com.example.dodrone.train;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import com.example.dodrone.R;
import com.google.common.util.concurrent.ListenableFuture;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class CameraFragment extends Fragment {

    Button captureBtn, doneBtn, refresh, nextStep9;
    PreviewView previewView;
    ImageCapture imageCapture;
    public ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    Drawable previewPicTaken=null;
    Drawable replayBtn, camImg;
    int index;



    private void setIndex(int index) {
        this.index = index;
        Log.d("whatswrong", "this is index in frag: "+index+"\n");
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_camera_train, container, false);
        index = Integer.parseInt(getArguments().get("index").toString());
        Log.d("whatswrong", "ha "+index);



        //closeBtn = rootView.findViewById(R.id.closeBtn);
        refresh = rootView.findViewById(R.id.refresh);
        nextStep9 = rootView.findViewById(R.id.nextStep_9);
        //refresh.setVisibility(View.INVISIBLE);
        //nextStep9.setVisibility(INVISIBLE);
        captureBtn = rootView.findViewById(R.id.captureBtn);
        doneBtn = rootView.findViewById(R.id.doneBtn);
        doneBtn.setVisibility(View.INVISIBLE);
        previewView = rootView.findViewById(R.id.previewView);
        replayBtn = getResources().getDrawable(R.drawable.replay);
        camImg = getResources().getDrawable(R.drawable.ic_baseline_photo_camera_24);
        //((MainActivity)getActivity()).myAdapter.setCallBackListener(indexListener);

        cameraProviderFuture = ProcessCameraProvider.getInstance(getActivity());
        initCameraX(cameraProviderFuture);

        captureBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(captureBtn.getBackground() == camImg) {
                    capturePhoto();
                    Log.d("whatswrong", "This is captureBtn. index "+index+"\n");
                }
                else {
                    initCameraX(cameraProviderFuture);
                    Log.d("whatswrong", "This is captureBtn. index " + index + "\n");
                }

            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPreviewPic(previewPicTaken, index);
                Log.d("whatswrong", previewPicTaken.toString()+"\n");
                Log.d("whatswrong", ((TrainStep4Activity)getActivity()).drawableList[index].toString());
                ((TrainStep4Activity)getActivity()).myAdapter.notifyItemChanged(index);
                getActivity().onBackPressed();
                ((TrainStep4Activity)getActivity()).redrawRecycle(index);
            }
        });


        return rootView;
    }



    private void initCameraX(ListenableFuture<ProcessCameraProvider> cameraProviderFuture) {

        cameraProviderFuture.addListener(() -> {
            try {
                // Camera provider is now guaranteed to be available
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                startCameraX(cameraProvider);


            } catch (InterruptedException | ExecutionException e) {
                // Currently no exceptions thrown. cameraProviderFuture.get()
                // shouldn't block since the listener is being called, so no need to
                // handle InterruptedException.
            }
        }, ContextCompat.getMainExecutor(getActivity()));
    }

    private void startCameraX(ProcessCameraProvider cameraProvider) {
        captureBtn.setBackground(camImg);
        cameraProvider.unbindAll();

        // Choose the camera by requiring a lens facing, selector use case
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build();

        Preview preview = new Preview.Builder().build();

        // Connect the preview use case to the previewView
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        // Set up the capture use case to allow users to take photos
        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();

        cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, preview, imageCapture);

    }



    private void capturePhoto() {
        File photoDir = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+"/CameraXPhotos");
        if (!photoDir.exists())
            photoDir.mkdir();

        Date date = new Date();
        String timestamp = String.valueOf(date.getTime());
        String photoFilePath = photoDir.getAbsolutePath()+"/"+timestamp+".jpg";

        File photoFile = new File(photoFilePath);
        imageCapture.takePicture(
                new ImageCapture.OutputFileOptions.Builder(photoFile).build(),
                ContextCompat.getMainExecutor(getView().getContext()),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull @NotNull ImageCapture.OutputFileResults outputFileResults) {
                        Toast.makeText(getContext(), "photo saved successfully", Toast.LENGTH_SHORT).show();
                        Log.d("whatswrong", photoDir.getAbsolutePath()+"에 저장됨");

                        previewPicTaken = Drawable.createFromPath(String.valueOf(photoFile));
                        try {
                            cameraProviderFuture.get().unbindAll();
                            previewView.setBackground(previewPicTaken);
                            ((TrainStep4Activity)getActivity()).filepathList[index]=photoFilePath;

                            doneBtn.setVisibility(View.VISIBLE);

                            captureBtn.setBackground(replayBtn);


                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }


                    @Override
                    public void onError(@NonNull @NotNull ImageCaptureException exception) {
                        Toast.makeText(getContext(), "Photo save failed", Toast.LENGTH_SHORT).show();
                        Log.d("whatswrong", exception.toString()+"\n"+exception.getMessage());
                    }
                }
        );

    }

    private void setPreviewPic(Drawable d, int index) {

        ((TrainStep4Activity)getActivity()).drawableList[index] = d;
    }

}