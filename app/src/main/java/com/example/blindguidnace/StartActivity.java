package com.example.blindguidnace;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;

import java.util.ArrayList;
import java.util.Locale;

public class StartActivity extends AppCompatActivity {

    protected static final int RESULT_SPEECH = 1;
    private static final int MY_DATA_CHECK_CODE = 1234;
    private ImageButton btnSpeak;
    TextToSpeech  tts;

    final int RequestCameraPermission = 1001;
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ActivityCompat.requestPermissions(StartActivity.this,new String[]{Manifest.permission.CAMERA},
                RequestCameraPermission);

        btnSpeak = findViewById(R.id.btnSpeak);

        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);



        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");


                try {
                    startActivityForResult(intent, RESULT_SPEECH);


                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Your device doesn't support Speech to Text", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onInit(int i)
    {
        tts.setSpeechRate(0.7f);
        tts.speak("Hello , welcome to our little demo   .",
                TextToSpeech.QUEUE_FLUSH,  // Drop all pending entries in the playback queue.
                null);
        tts.setSpeechRate(0.5f);
        tts.speak(" Say one for Object detection and two for text detection .",
                TextToSpeech.QUEUE_ADD,  // Drop all pending entries in the playback queue.
                null);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case RESULT_SPEECH:
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    if(text.get(0).equals("two")||text.get(0).equals("2"))
                    {
Intent intent = new Intent(this,  MainActivity.class);
                        startActivity(intent);


                    }
                    else if(text.get(0).equals("one")||text.get(0).equals("1")){
                        Intent intent = new Intent(this,  Objectdet.class);
                        startActivity(intent);
                    }
                    else
                    {
                        tts.setSpeechRate(0.5f);
                        tts.speak(" invalid input try again .",
                                TextToSpeech.QUEUE_ADD,  // Drop all pending entries in the playback queue.
                                null);


                    }
                }
                break;


            case MY_DATA_CHECK_CODE:

                if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS)
                {
                    // success, create the TTS instance
                    tts = new TextToSpeech(this, this::onInit);
                }
                else
                {
                    // missing data, install it
                    Intent installIntent = new Intent();
                    installIntent.setAction(
                            TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                    startActivity(installIntent);
                    finish();
                }
                break;
        }
    }
}