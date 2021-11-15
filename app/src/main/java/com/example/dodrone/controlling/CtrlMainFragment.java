package com.example.dodrone.controlling;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.view.PreviewView;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
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


public class CtrlMainFragment extends Fragment  {

    enum Drawing{
        CLEAR,
    }

    final String TAG = "ExCameraFragment";

    private UDPSocket mUdpClient;
    private String mServerAddressBroadCast = "255.255.255.255";
    InetAddress mServerAddr;
    int mServerPort = 6868;
    final byte[] mRequestConnect      = new byte[]{'w','h','o','a','m','i'};

    ImageView mServerImageView;

    private WebSocketClient mWebSocketClient;
    private String mServerExactAddress;
    private boolean mStream = false;

    private final Size CamResolution = new Size(1000, 840);

    private OverlayView mTrackingOverlay;
    private Bitmap mBitmapDebug;
    private boolean mProcessing = false;
    private Point[] mPoints = new Point[4];
    private Point mPointCircle = new Point();
    private int mRadiusCircle = 0;
    private Drawing mDrawing = Drawing.CLEAR;
    private boolean mTargetLocked = false;
    private Bitmap mBitmapGrab = null;

    public ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    PreviewView previewView;

    TextView batteryPer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Thread(new Client()).start();
        //previewView = findViewById(R.id.previewView);
        //batteryPer = findViewById(R.id.batteryPer);

        mUdpClient = new UDPSocket(12345);
        mUdpClient.runUdpServer();

        try {
            mServerAddr = InetAddress.getByName(mServerAddressBroadCast);
        }catch (Exception e){

        }

        AssetManager assetManager = getActivity().getAssets();
        if (MyConstants.DEBUG) {
            try {
                InputStream istr = assetManager.open("image1.jpg");
                Bitmap tmpBitmap = BitmapFactory.decodeStream(istr);
                mBitmapDebug = Bitmap.createScaledBitmap(tmpBitmap, CamResolution.getWidth(), CamResolution.getHeight(), false);
            } catch (IOException e) {
                // handle exception
            }
        }

        //int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        //int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        //float batteryPct = level * 100 / (float)scale;

    }
    class Client implements Runnable {
        public static final String SERVERIP = "192.168.35.215";
        //IP주소는 매번 바뀔수있으니까 수정필요 (SERVERIP)
        public static final int SERVERPORT = 1234;
        //public static final String SERVERIP = "192.168.4.1";
        //public static final int SERVERPORT = 1234;
        @Override
        public void run() {
            // TODO Auto-generated method stub
            System.out.println("dddddddd");
            try {
                // Retrieve the ServerName
                InetAddress serverAddr = InetAddress.getByName(SERVERIP);

                Log.d("UDP", "C: Connecting...");
                /* Create new UDP-Socket */
                DatagramSocket socket = new DatagramSocket();

                /* Prepare some data to be sent. */
                byte[] buf = ("A").getBytes();

                /* Create UDP-packet with
                 * data & destination(url+port) */
                DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddr,SERVERPORT);
                Log.d("UDP", "C: Sending: '" + new String(buf) + "'");

                /* Send out the packet */
                socket.send(packet);
                Log.d("UDP", "C: Sent.");
                Log.d("UDP", "C: Done.");

                socket.receive(packet);
                Log.d("UDP", "C: Received: '" + new String(packet.getData()) + "'");

            } catch (Exception e) {
                Log.e("UDP", "C: Error", e);
            }
        }
    }
    /*
    public void onWindowFocusChanged(){
        int testW = mTrackingOverlay.getWidth();
        int testH = mTrackingOverlay.getHeight();
//        mTrackingOverlay.setLayoutParams(new FrameLayout.LayoutParams(testW, CamResolution.getWidth()/CamResolution.getHeight()*testW));
//        mServerImageView.setLayoutParams(new FrameLayout.LayoutParams(testW, CamResolution.getWidth()/CamResolution.getHeight()*testW));
    }

     */

    private Executor getExecutor() {
        return ContextCompat.getMainExecutor(getActivity());
    }

    private void startCameraX(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build();

        Preview preview = new Preview.Builder().build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, preview);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_camera, parent, false);
        previewView = (PreviewView) rootView.findViewById(R.id.previewView);
        batteryPer = (TextView) rootView.findViewById(R.id.batteryPer);
        mServerImageView = (ImageView)rootView.findViewById(R.id.droneView);
        Button streamBtn = (Button) rootView.findViewById(R.id.streamBtn);
        cameraProviderFuture = ProcessCameraProvider.getInstance(getActivity());
        cameraProviderFuture.addListener(() -> {
            try{
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                startCameraX(cameraProvider);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, getExecutor());

        streamBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (!mStream) {
                    try {
                        mServerAddr = InetAddress.getByName(mServerAddressBroadCast);
                    }catch (Exception e){

                    }
                    mUdpClient.sendBytes(mServerAddr, mServerPort, mRequestConnect);
                    Pair<SocketAddress, String> res = mUdpClient.getResponse();
                    int cnt = 3;
                    while (res.first == null && cnt > 0) {
                        res = mUdpClient.getResponse();
                        Log.d("udpclient", res.toString());
                        cnt--;
                    }
                    if (res.first != null) {
                        Log.d(TAG, res.first.toString() + ":" + res.second);
                        mServerExactAddress = res.first.toString().split(":")[0].replace("/","");
                        mStream = true;
                        connectWebSocket();
                        try {
                            mServerAddr = InetAddress.getByName(mServerExactAddress);
                        }catch (Exception e){

                        }
                    }else{
                        Toast toast =
                                Toast.makeText(
                                        getActivity(), "Cannot connect to ESP32 Camera", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                } else {
                    mStream = false;
                    mWebSocketClient.close();
                    // ((Button) getActivity().findViewById(R.id.streamBtn)).setBackgroundResource(R.drawable.my_button_bg);
                    ((Button) getActivity().findViewById(R.id.streamBtn)).setTextColor(Color.rgb(255,255,255));
                }
            }
        });


        //mTrackingOverlay = (OverlayView) rootView.findViewById(R.id.tracking_overlay);
        //assert (mTrackingOverlay != null);



        return rootView;
    }


    private void connectWebSocket() {
        URI uri;
        try {
            uri = new URI("ws://"+mServerExactAddress+":86/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.d("Websocket", "Open");
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.d("Websocket", "Closed " + s);
            }

            @Override
            public void onMessage(String message){
                Log.d("Websocket", "Receive");
            }

            @Override
            public void onMessage(ByteBuffer message){
//                Log.d("Websocket", "Receive");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        byte[] imageBytes= new byte[message.remaining()];
                        message.get(imageBytes);
                        final Bitmap bmp=BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.length);
                        if (bmp == null)
                        {
                            return;
                        }
                        int viewWidth = mServerImageView.getWidth();
                        Matrix matrix = new Matrix();
                        matrix.postRotate(90);
                        final Bitmap bmp_traspose = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true );
                        float imagRatio = (float)bmp_traspose.getHeight()/(float)bmp_traspose.getWidth();
                        int dispViewH = (int)(viewWidth*imagRatio);
                        mServerImageView.setImageBitmap(Bitmap.createScaledBitmap(bmp_traspose, viewWidth, dispViewH, false));

                        mBitmapGrab = bmp;
                        //   mProcessing = detectorSSD.IsProcessing;
                        //   if (!mProcessing) {
                        //       Log.d(null,"dodododsksd야얀ㅁ얀ㅁ얀먄ㅇ");
                        //   }
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                Log.d("Websocket", "Error " + e.getMessage());
            }
        };
        mWebSocketClient.connect();
    }


    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        // detectorSSD.requestStop();
        // detectorSSD.waitForExit();
        mWebSocketClient.close();
        super.onDestroy();
    }


}