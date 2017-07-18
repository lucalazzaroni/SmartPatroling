package com.example.hew15j040el.smartpatroling.Libraries;

/**
 * Created by HEW15J040EL on 12/05/2017.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import io.vov.vitamio.MediaPlayer;

/**

 *

 * Classe che permette di prendere un frame dal video e salvarlo in memoria

 */
public class PhotoSaver {
    String filename = "tempdrone.jpeg";
    Bitmap image = null;
    MediaPlayer mMediaPlayer;
    Context context;
    String imgname = null;
    private static final String DRONE_DIRECTORY_NAME = "Drone Pictures";
    private static final String DRONE_BW_DIRECTORY_NAME = "Drone Pictures BW";
    private File mediaStorageDir;
    private File mediaStorageDirBW;

    public PhotoSaver(Context c, MediaPlayer m){
        this.context = c ;
        this.mMediaPlayer = m ;
    }

    public void record(){
        if(Environment.getExternalStorageState() != null){
            try
            {
                image = mMediaPlayer.getCurrentFrame(); //scatta la foto
                File picture = getOutputMediaFile(); //crea le due directory per foto a colori e BW e un file vuoto dove salvare l'immagine a colori
                FileOutputStream fos = new FileOutputStream(picture);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fos); //salva la foto a colori
                fos.close();
            }
            catch(FileNotFoundException e){
                Toast.makeText(context, "Picture file creation failed" , Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "Unable to create picture file" , Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(context, "Internal memory not available" , Toast.LENGTH_SHORT).show();
        }

        //dealloco memoria
        recylingBitmap(image);
        mMediaPlayer = null;
        context = null;
        mediaStorageDir = null;
        mediaStorageDirBW = null;
    }

    //inizialzza il file utilizzato per salvare il frame
    private File getOutputMediaFile() {

        mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                DRONE_DIRECTORY_NAME);

        // Crea la cartella se non esiste
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(DRONE_DIRECTORY_NAME, "Oops! Failed to create "
                        + DRONE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

//        creazione cartella per foto BW
        mediaStorageDirBW = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                DRONE_BW_DIRECTORY_NAME);

        if (!mediaStorageDirBW.exists()) {
            if (!mediaStorageDirBW.mkdirs()) {
                Log.d(DRONE_BW_DIRECTORY_NAME, "Oops! Failed to create "
                        + DRONE_BW_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        File mediaFile;
        imgname = Environment.getExternalStorageDirectory() + "/Pictures/" + DRONE_DIRECTORY_NAME + "/" + filename;
        mediaFile = new File(imgname);
        return mediaFile;
    }

    public void recylingBitmap (Bitmap bm)
    {
        if(bm!=null && !bm.isRecycled()){
            bm.recycle();
            bm = null;
        }
    }
}
