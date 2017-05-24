package com.example.hew15j040el.smartpatroling;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by HEW15J040EL on 10/05/2017.
 */

public class MatchingRecognition extends Activity {

    ImageButton bttHome;
    Button bttContinueRec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matching_recognition);
        bttHome = (ImageButton)findViewById(R.id.bttHome);

        bttHome.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent ima = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(ima);
            }
        });

        bttContinueRec = (Button)findViewById(R.id.bttContinueRec);

        bttContinueRec.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent itpr = new Intent(getApplicationContext(),TakePhotoRecognition.class);
                startActivity(itpr);
            }
        });

    }
}
