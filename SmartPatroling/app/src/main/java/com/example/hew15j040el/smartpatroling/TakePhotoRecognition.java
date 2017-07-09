package com.example.hew15j040el.smartpatroling;

import android.content.Context;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

/**
 * Created by HEW15J040EL on 10/05/2017.
 */

public class TakePhotoRecognition extends Activity {

    private static final  String TAG = "TakePhotoRecognition";
    private Button bttTakeDronePhoto = null;
    private Context context = null;
    private VideoView myVideoView;
    private final String PATH = "tcp://192.168.1.1:5555/";
    private String imgpath = null;
    private Bitmap imgBitmap = null;
    private  PhotoSaver ps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!LibsChecker.checkVitamioLibs(this))
            return;

        setContentView(R.layout.take_photo_recognition);

        context = getApplicationContext();
        myVideoView = (VideoView) findViewById(R.id.vitamio_videoView);
        myVideoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);
        myVideoView.setBufferSize(4096);
        myVideoView.setVideoPath(PATH);
        myVideoView.requestFocus();
        myVideoView.setMediaController(new MediaController(this));
        myVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_STRETCH, 0);

        myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setPlaybackSpeed(1.0f);
            }
        });

        bttTakeDronePhoto = (Button)findViewById(R.id.bttTakeDronePhoto);

        bttTakeDronePhoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i(TAG, "setOnClickListener");

//trasforma la foto in memoria "/Pictures/Drone Pictures/Fakebianconero.jpeg" in una ritagliata e in bianco e nero
//                FakePhotoDrone();

                Toast.makeText(getApplicationContext(), "Capturing image...", Toast.LENGTH_SHORT).show();

                capturePhoto(null);

                Intent imr = new Intent(getApplicationContext(), MatchingRecognition.class);

                //Convert to byte array
                if(imgBitmap!=null) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    imr.putExtra("imageByteArray", stream.toByteArray());
                }


                startActivity(imr);
            }
        });

    }
    private void capturePhoto(View v){
        try {
            ps = new PhotoSaver(context, myVideoView.getMediaPlayer());
            imgBitmap = ps.record();
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Picture error!",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        recylingBitmap(imgBitmap);
        ps = null;
    }

    public void recylingBitmap (Bitmap bm)
    {
        if(bm!=null){
            bm.recycle();
            bm = null;
        }
    }




    //////////////////////////////////////////////////
    public void FakePhotoDrone(){
        //recupero immagine scattata col drone dalla memoria
        Bitmap daMemoria= BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/Pictures/Drone Pictures/Fakebianconero.jpeg");

        Bitmap bwImage = toGreyScale(daMemoria);
        try {
            File bnFile;
            String _bnPath = Environment.getExternalStorageDirectory()+"/Pictures/" + "Drone Pictures BW" + "/"+ "Fakebianconero" + ".jpeg";
            bnFile = new File(_bnPath);
            FileOutputStream fosBw = new FileOutputStream(bnFile);
            bwImage.compress(Bitmap.CompressFormat.JPEG, 100, fosBw);
            fosBw.close();
            recylingBitmap(bwImage);
        }
        catch (FileNotFoundException fnfe)
        {
            fnfe.printStackTrace();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }

    }
    public Bitmap toGreyScale(Bitmap bmpOriginal){
        Bitmap bmpGrayscale = Bitmap.createBitmap(360, 360,Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, new Rect(140,0,500,360),new Rect(0,0,360,360), paint);

        //bmpGrayscale.setPixel(0,0, bmpOriginal.getPixel(0,0)    );

        // recyclingCanvas(canGray);

        return bmpGrayscale;
    }
    /////////////////////////////////////////////////

}



