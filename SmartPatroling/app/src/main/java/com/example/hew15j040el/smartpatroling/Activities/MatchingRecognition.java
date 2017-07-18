package com.example.hew15j040el.smartpatroling.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.hew15j040el.smartpatroling.AsyncTask.AlgorithmEnded;
import com.example.hew15j040el.smartpatroling.AsyncTask.BackgroundTask;
import com.example.hew15j040el.smartpatroling.R;

/**
 * Created by HEW15J040EL on 10/05/2017.
 */

public class MatchingRecognition extends Activity implements AlgorithmEnded {

    ImageButton bttHome;
    Button bttContinueRec;
    TextView name = null;
    ImageView imgRec;
    ImageView imgTra;
    Bitmap imgBitmap = null;
    Bitmap bmpGrayscale = null;
    Canvas canGray = null;
    Bitmap rotatedBitmap = null;
    Bitmap trainingBmp = null;
    ProgressDialog progress = null;
    AlgorithmEnded AE = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matching_recognition);

        imgRec = (ImageView) findViewById(R.id.imgRec);
        imgTra = (ImageView) findViewById(R.id.imgTra);
        name = (TextView) findViewById(R.id.name);
        name.setText("Unknown");
        bttHome = (ImageButton) findViewById(R.id.bttHome);
        AE = this;
        progress = new ProgressDialog(this);


        try {
            //prendo la bitmap dell'immagine scattata nella TakePhotoRecognition
            imgBitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/Pictures/Drone Pictures/tempdrone.jpeg");
            //ritaglio la bitmap del drone per vederla quadrata(siccome ritaglio la sessa immagine dove salvo la terza misura è 360 perchè non parte da 0 ma dal 140
            imgBitmap = Bitmap.createBitmap(imgBitmap, 140, 0, 360, 360, null, true);
            //algoritmo in background
            new BackgroundTask(getApplicationContext(), imgBitmap, progress, AE, name, imgTra).execute();

            //metto in imgRec l'immagine scattata nella TakePhotoRecognition
            imgRec.setImageBitmap(imgBitmap);
        } catch (OutOfMemoryError oome) {
            AlertDialog alertDialog = new AlertDialog.Builder(MatchingRecognition.this).create();
            alertDialog.setTitle("Not enough memory");
            alertDialog.setMessage("The App needs more memory to work!");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finishAffinity(); //chiudi l'app in seguito all'outOfMemoryError
                        }
                    });
            alertDialog.show();
        }

        bttHome.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent ima = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(ima);
            }
        });

        bttContinueRec = (Button) findViewById(R.id.bttContinueRec);

        bttContinueRec.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent itpr = new Intent(getApplicationContext(), TakePhotoRecognition.class);
                startActivity(itpr);
            }
        });
    }

    @Override
    public void onAlgorithmEnd(String fileName, Bitmap imgBm) {
        name.setText(fileName);
        imgTra.setImageBitmap(imgBm);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //dealloco memoria
        if (imgBitmap != null && !imgBitmap.isRecycled())
            recyclingCanvas(canGray);
        recylingBitmap(bmpGrayscale);
        recylingBitmap(imgBitmap);
        recylingBitmap(rotatedBitmap);
        recylingBitmap(trainingBmp);
        imgTra = null;
        imgRec = null;
    }

    public void recylingBitmap(Bitmap bm) {
        if (bm != null && !bm.isRecycled()) {
            bm.recycle();
            bm = null;
        }
    }

    public void recyclingCanvas(Canvas cv) {
        if (cv != null) {
            cv.setBitmap(null);
            cv = null;
        }
    }
}