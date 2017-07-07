package com.example.hew15j040el.smartpatroling;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
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
    TextView name = null;
    ImageView imgRec;
    ImageView imgTra;
    Bitmap imgBitmap = null;
    Bitmap bmpGrayscale = null;
    Canvas c = null;
    Bitmap rotatedBitmap=null;
    Bitmap trainingBmp = null;

    private static final String IMAGE_DIRECTORY_NAME = "Smart Patroling";
    private Algorithm algorithm;


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
            byteArray = null;
            bmpGrayscale = toGreyScale(imgBitmap);
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////
            algorithm = new Algorithm();
            algorithm.Detection(1f);
            String fileName = algorithm.Recognize(20f , bmpGrayscale);
            if(fileName != null)
            {
                trainingBmp = FromJpegToBitmap(Environment.getExternalStorageDirectory() + "/Pictures/" + IMAGE_DIRECTORY_NAME + "/" + fileName);
                Matrix rotate = new Matrix();
                rotate.preRotate(90);
                rotatedBitmap = Bitmap.createBitmap(trainingBmp,trainingBmp.getWidth()/10,0,trainingBmp.getWidth()/20*17,trainingBmp.getHeight(),rotate,true);

//                rotatedBitmap = Bitmap.createBitmap(trainingBmp,0,0,trainingBmp.getWidth(),trainingBmp.getHeight(),rotate,true);
                rotate=null;
//                c  = new Canvas(rotatedBitmap);
//                c.drawBitmap(trainingBmp,new Rect(0,0,1600,1200), new Rect(), null);

                //funziona per l immagine girata a sx rispetto ad una dritta
//        c.drawBitmap(bmpOriginal, new Rect(200,0,1400,1200),new Rect(0,0,360,360), paint);
                // funziona con l immagine dritta
//                c.drawBitmap(rotatedBitmap, new Rect(0,150,1200,1350),new Rect(0,0,360,360), null);
                imgTra.setImageBitmap(rotatedBitmap);
                name.setText(fileName);
            }
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

    @Override
    protected void onStop() {
        super.onStop();
        recyclingCanvas(c);
        recylingBitmap(bmpGrayscale);
        recylingBitmap(imgBitmap);
        algorithm = null;
        recylingBitmap(rotatedBitmap);
        recylingBitmap(trainingBmp);

    }

    public Bitmap toGreyScale(Bitmap bmpOriginal)
    {
        bmpGrayscale = Bitmap.createBitmap(360, 360,Bitmap.Config.ARGB_8888);
        c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, new Rect(140,0,500,360),new Rect(0,0,360,360), paint);

        //bmpGrayscale.setPixel(0,0, bmpOriginal.getPixel(0,0)    );

        // recyclingCanvas(c);

        return bmpGrayscale;
    }


    public void recylingBitmap (Bitmap bm)
    {
        if(bm!=null){
            bm.recycle();
            bm=null;
        }
    }

    public void recyclingCanvas(Canvas cv)
    {
        if (cv != null) {
            cv.setBitmap(null);
            cv = null;
        }
    }

    //convertire JPEG in bitmap

    public Bitmap FromJpegToBitmap(String _filepath)
    {
        return BitmapFactory.decodeFile( _filepath );
    }
}
