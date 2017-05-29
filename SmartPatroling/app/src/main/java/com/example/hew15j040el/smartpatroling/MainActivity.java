package com.example.hew15j040el.smartpatroling;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;

public class MainActivity extends AppCompatActivity {

    Button bttTraining;
    Button bttRecognition;
    Button bttSwipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bttTraining =(Button)findViewById(R.id.bttTraining);
        bttRecognition =(Button)findViewById(R.id.bttRecognition);
        bttSwipe = (Button)findViewById(R.id.bttswipe);

        GifDrawable gif = null;
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

    }

}
