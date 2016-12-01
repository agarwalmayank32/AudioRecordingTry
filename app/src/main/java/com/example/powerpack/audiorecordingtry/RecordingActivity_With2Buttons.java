package com.example.powerpack.audiorecordingtry;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class RecordingActivity_With2Buttons extends AppCompatActivity {

    ImageButton imageButton1,imageButton2;

    private MediaRecorder myAudioRecorder;
    private String outputFile = null;

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2;
    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    private static final int MY_PERMISSIONS = 0;

    int flag,recordingNumber;
    MediaPlayer m;
    Chronometer chronometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording__with2_buttons);

        imageButton1 = (ImageButton)findViewById(R.id.imageButton1);
        imageButton2 = (ImageButton)findViewById(R.id.imageButton2);
        chronometer = (Chronometer)findViewById(R.id.chrono);

        CheckForPermission();

    }

    void CheckForPermission()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS);
                }
                else {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                }
            }
            else if(checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
            }
            else {
                Record();
            }
        }
        else
        {
            Record();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS: {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED ) {
                    Toast.makeText(getBaseContext(), "You must allow all the permissions.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    Record();
                }
            }
            break;
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getBaseContext(), "You must allow writing in external storage permission.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    Record();
                }
            }
            break;
            case MY_PERMISSIONS_REQUEST_RECORD_AUDIO: {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getBaseContext(), "You must allow record audio permission.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    Record();
                }
            }
        }
    }

    void Record()
    {
        recordingNumber=0;
        m = new MediaPlayer();
        final File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +"/GOODSPEAKS/");
        try{
            dir.mkdir();
        }catch(Exception e){
            e.printStackTrace();
        }
        flag=0;
        imageButton1.setEnabled(false);
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(flag==0)
                    {
                        chronometer.setBase(SystemClock.elapsedRealtime());
                        imageButton2.setBackgroundResource(R.drawable.stop);
                        myAudioRecorder=new MediaRecorder();
                        myAudioRecorder.setAudioSource(1);
                        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                        myAudioRecorder.setAudioEncoder(1);
                        outputFile = dir+"/MayankRecord"+recordingNumber+"Latest.mp4";
                        flag++;
                        recordingNumber++;
                        myAudioRecorder.setOutputFile(outputFile);
                        myAudioRecorder.prepare();
                        myAudioRecorder.start();
                        chronometer.start();
                        Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
                    }
                    else if(flag==1)
                    {
                        if(m.isPlaying())
                        {
                            imageButton2.setBackgroundResource(R.drawable.play);
                            m.stop();
                        }
                        else
                        {
                            imageButton2.setBackgroundResource(R.drawable.replay);
                            myAudioRecorder.stop();
                            myAudioRecorder.release();
                            myAudioRecorder  = null;
                            Toast.makeText(getApplicationContext(), "Audio recorded successfully",Toast.LENGTH_LONG).show();
                            imageButton1.setEnabled(true);
                            chronometer.stop();
                        }
                        flag++;
                    }
                    else
                    {
                        chronometer.setBase(SystemClock.elapsedRealtime());
                        imageButton2.setBackgroundResource(R.drawable.stop);
                        myAudioRecorder=new MediaRecorder();
                        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
                        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.DEFAULT);
                        outputFile = dir+"/Recording"+recordingNumber+".mp4";
                        myAudioRecorder.setOutputFile(outputFile);
                        recordingNumber++;
                        myAudioRecorder.prepare();
                        myAudioRecorder.start();
                        chronometer.start();
                        Toast.makeText(getApplicationContext(), "Recording started Again", Toast.LENGTH_LONG).show();
                        imageButton2.setBackgroundResource(R.drawable.stop);
                        flag=1;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) throws IllegalArgumentException,SecurityException,IllegalStateException {
                m = new MediaPlayer();
                try {
                    m.setDataSource(outputFile);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    m.prepare();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                m.start();
                imageButton2.setBackgroundResource(R.drawable.stop);
                chronometer.setBase(SystemClock.elapsedRealtime());
                imageButton2.setEnabled(true);
                flag=1;
                Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_LONG).show();
            }
        });
    }

}
