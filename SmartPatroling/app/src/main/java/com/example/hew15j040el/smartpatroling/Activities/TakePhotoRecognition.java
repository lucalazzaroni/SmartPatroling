package com.example.hew15j040el.smartpatroling.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.example.hew15j040el.smartpatroling.Libraries.PhotoSaver;
import com.example.hew15j040el.smartpatroling.R;
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
    private Bitmap imgBitmap = null;
    private PhotoSaver ps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(getApplicationContext(), "Please wait for the drone camera to open...", Toast.LENGTH_LONG).show();
        final android.media.MediaPlayer tonoMP = android.media.MediaPlayer.create(this, R.raw.scatto);

        if (!LibsChecker.checkVitamioLibs(this))
            return;
        try
        {
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

            bttTakeDronePhoto = (Button) findViewById(R.id.bttTakeDronePhoto);

            bttTakeDronePhoto.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    tonoMP.start();
                    Log.i(TAG, "setOnClickListener");
                    capturePhoto(null);
                    Intent imr = new Intent(getApplicationContext(), MatchingRecognition.class);
                    startActivity(imr);
                }
            });
        }
        catch (OutOfMemoryError oome)
        {
            AlertDialog alertDialog = new AlertDialog.Builder(TakePhotoRecognition.this).create();
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
    }
    private void capturePhoto(View v){
        try {
            ps = new PhotoSaver(context, myVideoView.getMediaPlayer());
            ps.record();
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Picture error!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        recylingBitmap(imgBitmap);
        ps = null;
        context = null;
        myVideoView = null;
    }

    public void recylingBitmap (Bitmap bm)
    {
        if(bm!=null){
            bm.recycle();
            bm = null;
        }
    }
}



