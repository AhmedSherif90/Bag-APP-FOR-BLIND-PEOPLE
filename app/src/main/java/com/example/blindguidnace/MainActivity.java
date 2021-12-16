package com.example.blindguidnace;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // welcome ..
    SurfaceView cameraView;
    TextView textView;
    Button Button;
    CameraSource cameraSource;

    private TextToSpeech  tts;



int time=0;
    // 3

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        cameraView = findViewById(R.id.surface_view);
        textView = findViewById(R.id.text_view);
        Button=findViewById(R.id.button);
        Button.setOnClickListener(view ->{
                    time++;
                    if(time==2)
                    {
                        textView.setText("");
                        time=0;

                        String toSpeak = " ";


                        tts.speak(toSpeak,TextToSpeech.QUEUE_FLUSH,null);


                    }

                }


                );
        TextToSpeech.OnInitListener listener =
                new TextToSpeech.OnInitListener() {
                    @Override public void onInit(final int status){
                        if (status == TextToSpeech.SUCCESS) {
                            Log.d("TTS", "Text to speech engine started successfully.");
                            tts.setLanguage(Locale .US);
                        } else {
                            Log.d("TTS", "Error starting the text to speech engine.");
                        }
                    }
                };
        tts = new TextToSpeech(this.getApplicationContext(), listener);


        // 1
        final TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if (!textRecognizer.isOperational()) {
            Log.w("MainActivity", "Detected dependence are not found ");
        } else {
            cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setRequestedFps(2.0f)
                    .setAutoFocusEnabled(true)
                    .build();

            // 2
            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {

                    try {
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){

                        }
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    cameraSource.stop();
                }
            });


            // 4
            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {

                }

                @Override

                public void receiveDetections(Detector.Detections<TextBlock> detections) {




                    if (!tts.isSpeaking()  ) {

                        SparseArray<TextBlock> items = detections.getDetectedItems();

                        if (items.size() != 0 ){
                            textView.post(new Runnable() {
                                @Override
                                public void run() {
                                    StringBuilder    stringBuilder = new StringBuilder();
                                    for (int i = 0 ;i < items.size();i++){
                                        TextBlock item = items.valueAt(i);
                                        stringBuilder.append(item.getValue());
                                        stringBuilder.append("\n");

                                    }
                                    textView.setText(stringBuilder.toString());
                                    Log.d("Text",stringBuilder.toString());
                                    tts.setSpeechRate(0.7f);
                                    tts.speak(stringBuilder.toString(), TextToSpeech.QUEUE_ADD, null, "DEFAULT");

                                }
                            });
                        }


                    }

                }



                });
        }
    }
}
