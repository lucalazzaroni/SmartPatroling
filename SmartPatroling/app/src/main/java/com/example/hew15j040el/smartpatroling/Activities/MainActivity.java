package com.example.hew15j040el.smartpatroling.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.hew15j040el.smartpatroling.R;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;

public class MainActivity extends AppCompatActivity {

    Button bttTraining;
    Button bttRecognition;
    Button bttSwipe;
    ImageButton bttSettings;
    GifDrawable gif = null;
    TextView textDistance;
    TextView textPercentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textDistance = (TextView)  findViewById(R.id.textDistance);
        textDistance.setText("Threshold: " + (int)(Settings.distance));
        textPercentage = (TextView)  findViewById(R.id.textPercentage);
        textPercentage.setText("Precision: " + (int)(Settings.percentage * 100) + "%");
        bttTraining =(Button)findViewById(R.id.bttTraining);
        bttRecognition =(Button)findViewById(R.id.bttRecognition);
        bttSwipe = (Button)findViewById(R.id.bttswipe);
        bttSettings = (ImageButton)findViewById(R.id.bttSettings);


        try{
            gif = new GifDrawable(getResources(), R.drawable.drone);
            findViewById(R.id.activity_main).setBackground(gif);
        }catch (IOException e){
            e.printStackTrace();
        }

        bttTraining.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent itpt = new Intent(getApplicationContext(),SavePhotoTraining.class);
                startActivity(itpt);
            }
        });

        bttRecognition.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent itpr = new Intent(getApplicationContext(),TakePhotoRecognition.class);
                startActivity(itpr);
            }
        });

        bttSwipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_info = new Intent(MainActivity.this, SwipeActivity.class);
                startActivity(intent_info);
                overridePendingTransition(R.anim.up, R.anim.nochange);
            }
        });

        bttSettings.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent is = new Intent(getApplicationContext(),Settings.class);
                startActivity(is);
            }
        });



    }
    @Override
    protected void onStop() {
        super.onStop();
        gif = null;
    }
}
