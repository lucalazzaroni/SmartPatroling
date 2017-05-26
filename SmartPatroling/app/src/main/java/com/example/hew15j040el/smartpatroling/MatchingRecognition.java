package com.example.hew15j040el.smartpatroling;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by HEW15J040EL on 10/05/2017.
 */

public class MatchingRecognition extends Activity {

    ImageButton bttHome;
    Button bttContinueRec;
    TextView name=null;
    ImageView imgRec;
    ImageView imgTra;
    Bitmap imgBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matching_recognition);

        imgRec = (ImageView) findViewById(R.id.imgRec);
        imgTra = (ImageView) findViewById(R.id.imgTra);
        name = (TextView)  findViewById(R.id.name);
        name.setText("Unknown");
        bttHome = (ImageButton)findViewById(R.id.bttHome);

        //prendo la bitmap dell'immagine scattata nella TakePhotoRecognition
        if(getIntent().hasExtra("imageByteArray")) {
            byte[] byteArray = getIntent().getByteArrayExtra("imageByteArray");
            imgBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

            //metto in imgRec l'immagine scattata nella TakePhotoRecognition
            imgRec.setImageBitmap(imgBitmap);
        }
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
