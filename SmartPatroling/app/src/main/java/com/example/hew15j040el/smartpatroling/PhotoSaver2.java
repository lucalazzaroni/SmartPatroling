package com.example.hew15j040el.smartpatroling;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import io.vov.vitamio.MediaPlayer;

/**
 * Created by utente on 17/05/2017.
 */

public class PhotoSaver2 {
    Bitmap image;
    Context context;
    String imgname = null;

//    public String getImgname()
//    {
//        return this.imgname;
//    }


    public PhotoSaver2(Context c,Bitmap b,String filename){
        this.context = c ;
        this.image = b ;
        this.imgname = filename+".jpeg";

    }

    /**

     *

     * Get the current frame of the MediaPlayer and saves it in the local storage of the phone at
     the PNG format.

     */
    public void record(){
        if(Environment.getExternalStorageState() != null){
            try{

                File mediaFile;
                imgname = Environment.getExternalStorageDirectory()+ "/Pictures/" + imgname;
                mediaFile = new File(imgname);

//                image = mMediaPlayer.getCurrentFrame();
//                File picture = getOutputMediaFile();
                FileOutputStream fos = new FileOutputStream(mediaFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.close();

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
    }


    /**

     *

     * Initializes the File which will be used to save the frame. Called in the record() method.
     * @return the File to be used.

     */

}
