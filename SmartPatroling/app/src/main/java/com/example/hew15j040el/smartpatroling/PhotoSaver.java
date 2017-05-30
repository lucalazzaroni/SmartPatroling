package com.example.hew15j040el.smartpatroling;

/**
 * Created by HEW15J040EL on 12/05/2017.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import io.vov.vitamio.MediaPlayer;

/**

 *

 * Class allowing to get a frame from the MediaPlayer which streams the video and save it into the local storage of the device.

 */
public class PhotoSaver {
    String filename;
    String finalname;
    Bitmap image=null;
    Bitmap bwImage =null;
    Bitmap bmpGrayscale=null;
    Canvas c=null;
    Calendar rightNow;
    MediaPlayer mMediaPlayer;
    Context context;
    String imgname = null;
    private static final String DRONE_DIRECTORY_NAME = "Drone Pictures";
    private static final String DRONE_BW_DIRECTORY_NAME = "Drone Pictures BW";
    private File mediaStorageDir;
    private File mediaStorageDirBW;


//    public String getImgname()
//    {
//        return this.imgname;
//    }


    public PhotoSaver(Context c, MediaPlayer m){
        this.context = c ;
        this.mMediaPlayer = m ;
        rightNow = Calendar.getInstance();
        filename = rightNow.get(Calendar.DAY_OF_MONTH)+"_"+(rightNow.get(Calendar.MONTH)+1)+"_"+rightNow.get(Calendar.YEAR);

    }

    /**

     *

     * Get the current frame of the MediaPlayer and saves it in the local storage of the phone at
     the PNG format.

     */
    public String record(){
        if(Environment.getExternalStorageState() != null){
            try{

                image = mMediaPlayer.getCurrentFrame(); //scatta la foto
                File picture = getOutputMediaFile(); //crea le due directory per foto a colori e BW e un file vuoto dove salvare l'immagine a colori
                FileOutputStream fos = new FileOutputStream(picture);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fos); //salva la foto a colori
                fos.close();

                bwImage = toGreyScale(image);
                try {
                    File bnFile;
                    String _bnPath = Environment.getExternalStorageDirectory()+"/Pictures/" + DRONE_BW_DIRECTORY_NAME + "/"+ finalname + ".jpeg";
                    bnFile = new File(_bnPath);
                    FileOutputStream fosBw = new FileOutputStream(bnFile);
                    bwImage.compress(Bitmap.CompressFormat.JPEG, 100, fosBw);
                    fos.close();
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

                Toast.makeText(context, "Picture saved in :" + imgname , Toast.LENGTH_SHORT).show();

            } catch(FileNotFoundException e){
                Toast.makeText(context, "Picture file creation failed" , Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "Unable to create picture file" , Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(context, "Internal memory not available" , Toast.LENGTH_SHORT).show();
        }
//        recylingBitmap(image);
        return imgname;
    }


    /**

     *

     * Initializes the File which will be used to save the frame. Called in the record() method.
     * @return the File to be used.

     */
    private  File getOutputMediaFile() {

        mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                DRONE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(DRONE_DIRECTORY_NAME, "Oops! Failed to create "
                        + DRONE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        //creazione cartella per foto BW
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

            rightNow = Calendar.getInstance();
            finalname = "DronePicture_" + rightNow.get(Calendar.HOUR) + ":" + rightNow.get(Calendar.MINUTE) + ":" + rightNow.get(Calendar.SECOND) + "_" + filename;
            //Create a media file name
            File mediaFile;
            imgname = Environment.getExternalStorageDirectory() + "/Pictures/" + DRONE_DIRECTORY_NAME + "/" + finalname + ".jpeg";

            mediaFile = new File(imgname);
            return mediaFile;
    }

    public Bitmap toGreyScale(Bitmap bmpOriginal){
        bmpGrayscale = Bitmap.createBitmap(360, 360, bmpOriginal.getConfig());
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

}
