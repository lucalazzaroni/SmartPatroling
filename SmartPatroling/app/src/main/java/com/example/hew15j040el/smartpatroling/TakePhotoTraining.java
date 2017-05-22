package com.example.hew15j040el.smartpatroling;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by HEW15J040EL on 10/05/2017.
 */

public class TakePhotoTraining extends Activity {

    Button bttTakePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_photo_training);
        bttTakePhoto = (Button)findViewById(R.id.bttTakePhoto);

        bttTakePhoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent ispt = new Intent(getApplicationContext(),SavePhotoTraining.class);
                startActivity(ispt);
            }
        });

    }
}
