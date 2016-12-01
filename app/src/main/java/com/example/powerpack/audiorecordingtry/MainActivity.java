package com.example.powerpack.audiorecordingtry;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
        
    Button play,stop,record;
    private MediaRecorder myAudioRecorder;
    private String outputFile = null;

    TextView textView;

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2;
    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    private static final int MY_PERMISSIONS = 0;

    int x;
    MediaPlayer m;

        
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView =(TextView)findViewById(R.id.textV);

        CheckForPermission(); //TO check for Permissions in Android M
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
        m = new MediaPlayer();
        final File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +"/GOODSPEAKS/");
        try{
            if(dir.mkdir()) {
               // textView.setText("Directory created");
            } else {
                //textView.setText("Directory is not created");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        x=0;
        play=(Button)findViewById(R.id.button3);
        stop=(Button)findViewById(R.id.button2);
        record=(Button)findViewById(R.id.button);
        stop.setEnabled(false); // to disable button , it will enable after record button pressed
        play.setEnabled(false);
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    stop.setEnabled(false);
                    play.setEnabled(false);
                    //Long tsLong = System.currentTimeMillis()/1000;
                    //final String ts = tsLong.toString();
                    myAudioRecorder=new MediaRecorder();
                    myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
                    myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.DEFAULT);

                    outputFile = dir+"/Mayank"+x+".mp4";
                    x++;
                    myAudioRecorder.setOutputFile(outputFile);
                    myAudioRecorder.prepare();
                    myAudioRecorder.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                record.setEnabled(false);
                stop.setEnabled(true);
                textView.setText("Recording Started");
                Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(m.isPlaying())
                {
                    m.stop();
                }
                else
                {
                    myAudioRecorder.stop();
                    myAudioRecorder.release();
                    myAudioRecorder  = null;
                    stop.setEnabled(false);
                    play.setEnabled(true);
                    textView.setText("Recording Saved");
                    Toast.makeText(getApplicationContext(), "Audio recorded successfully",Toast.LENGTH_LONG).show();
                }
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) throws IllegalArgumentException,SecurityException,IllegalStateException {
                MediaPlayer m = new MediaPlayer();
                try {
                    m.setDataSource(outputFile);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    m.prepare();
                    record.setEnabled(true);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                m.start();
                stop.setEnabled(true);
                textView.setText("Recording Playing");
                Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_LONG).show();
            }
        });
    }
}
