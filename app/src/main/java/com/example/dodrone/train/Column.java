package com.example.dodrone.train;

import android.graphics.Bitmap;
import android.widget.Button;

public class Column {

    String label;
    int guidePics;
    Bitmap bitmap;
    //Drawable d;
    //PreviewView previewView;
    String photofile;
    Button openCam;

    /*public Column(String label, int guidePics, Drawable d, Button openCam) {
        this.label = label;
        this.guidePics = guidePics;
        this.d = d;
        this.openCam = openCam;
    }*/
    /*public Column(String label, int guidePics, Drawable d, Button openCam) {
        this.label = label;
        this.guidePics = guidePics;
        this.d = d;
        this.openCam = openCam;
    }*/
    public Column(String label, int guidePics, Bitmap bitmap, Button openCam) {
        this.label = label;
        this.guidePics = guidePics;
        this.bitmap = bitmap;
        this.openCam = openCam;
    }
}
