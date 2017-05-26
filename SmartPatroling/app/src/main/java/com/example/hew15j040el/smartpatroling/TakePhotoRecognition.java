package com.example.hew15j040el.smartpatroling;

import android.content.Context;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

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
                capturePhoto(null);
                Intent imr = new Intent(getApplicationContext(), MatchingRecognition.class);

                //Convert to byte array
                if(imgBitmap!=null) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    imr.putExtra("imageByteArray", stream.toByteArray());
                }

                recylingBitmap(imgBitmap);
                startActivity(imr);
            }
        });

    }
    private void capturePhoto(View v){
        try {
            PhotoSaver ps = new PhotoSaver(context, myVideoView.getMediaPlayer());
            imgpath = ps.record();
            imgBitmap = ps.image;
        }
        catch (Exception e) {

            Toast.makeText(getApplicationContext(), "Picture error!",Toast.LENGTH_SHORT).show();
        }

    }

    public void recylingBitmap (Bitmap bm)
    {
        if(bm!=null){
            bm.recycle();
            bm=null;
        }
    }
}



