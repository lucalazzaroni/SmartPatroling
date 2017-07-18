package com.example.hew15j040el.smartpatroling.Libraries;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by HEW15J040EL on 12/07/2017.
 */

public class StorageInteraction extends Activity {

    private static final int MEDIA_TYPE_IMAGE = 1;
    private static final String IMAGE_DIRECTORY_NAME = "Smart Patroling";
    private static final String IMAGE_BW_DIRECTORY_NAME = "Smart Patroling BW";
    private static File mediaStorageDir;
    private static File mediaStorageDirBW;

    public static Bitmap TakeFromMemory(Bitmap imgBitmap, Bitmap bmpGrayscale, Canvas canGray)
    {

        //converto in bianco e nero
        bmpGrayscale = ImageProcessing.toGreyScale(imgBitmap, bmpGrayscale, canGray);

//      salvo la foto in B/N in memoria per questioni di debug
        try
        {
            File bnFile;
            String _bnPath = Environment.getExternalStorageDirectory() + "/Pictures/Drone Pictures BW/tempdrone.jpeg";
            bnFile = new File(_bnPath);
            FileOutputStream fosBw = new FileOutputStream(bnFile);
            bmpGrayscale.compress(Bitmap.CompressFormat.JPEG, 100, fosBw);
            fosBw.close();
        }
        catch (FileNotFoundException fnfe)
        {
            fnfe.printStackTrace();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
        return bmpGrayscale;
    }

    public static Bitmap FromJpegToBitmap(String _filepath)
    {
        return BitmapFactory.decodeFile( _filepath );
    }

    public static File getOutputMediaFile(int type, Context context, File mediaFile, String imgPath) {
        if (Environment.getExternalStorageState() != null) {
            mediaStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    IMAGE_DIRECTORY_NAME);

            // Create la cartella di storage se non esiste
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed to create "
                            + IMAGE_DIRECTORY_NAME + " directory");
                    return null;
                }
            }

            //creazione cartella per foto BW
            mediaStorageDirBW = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    IMAGE_BW_DIRECTORY_NAME);

            if (!mediaStorageDirBW.exists()) {
                if (!mediaStorageDirBW.mkdirs()) {
                    Log.d(IMAGE_BW_DIRECTORY_NAME, "Oops! Failed to create "
                            + IMAGE_BW_DIRECTORY_NAME + " directory");
                    return null;
                }
            }

            if (type == MEDIA_TYPE_IMAGE) {
                imgPath = mediaStorageDir.getPath() + File.separator + "IMG_temp.jpeg";
                mediaFile = new File(imgPath);
            } else
                return null;
            return mediaFile;
        }
        else
            Toast.makeText(context, "Internal memory not available", Toast.LENGTH_SHORT).show();
        return  null;
    }

    public static boolean SaveColorAndBW(EditText writename, Context context, Bitmap rotatedBitmap,
                                         Bitmap bitmap, Bitmap bmpGrayscale, Canvas canGray, File mediaFile)
    {
        String _writename = writename.getText().toString() + ".jpeg";

        if(TextUtils.isEmpty(writename.getText().toString())) //se non viene dato un nome all'immagine avvisa e non salvare
        {
            writename.setError("Image must have a name!");
            return false;
        }

        File to = new File(mediaStorageDir.getPath(),_writename);
        if(!mediaFile.renameTo(to)){
            Toast.makeText(context, "Image not renamed!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(context, "Picture saved", Toast.LENGTH_SHORT).show();
        }

        bmpGrayscale = ImageProcessing.toGreyScale(rotatedBitmap, bitmap, bmpGrayscale, canGray);
        try {
            File bnFile;
            String _bnPath = Environment.getExternalStorageDirectory()+"/Pictures/" + IMAGE_BW_DIRECTORY_NAME + "/"+ writename.getText().toString() + ".jpeg";
            bnFile = new File(_bnPath);
            FileOutputStream fos = new FileOutputStream(bnFile);
            bmpGrayscale.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        }
        catch (FileNotFoundException fnfe)
        {
            fnfe.printStackTrace();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
        return true;
    }
}
