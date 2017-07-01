package com.example.hew15j040el.smartpatroling;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.util.List;
import java.util.Scanner;

import io.vov.vitamio.utils.Base64;


/**
 * Created by HEW15J040EL on 24/05/2017.
 */

public class Algorithm extends Activity {

    private static final String IMAGE_BW_DIRECTORY_NAME = "Smart Patroling BW";
    private int numberOfImages = new File(Environment.getExternalStorageDirectory()+"/Pictures/" + IMAGE_BW_DIRECTORY_NAME).listFiles().length;
    private static final int RES = 129600;
    private String[] fileNames = new File(Environment.getExternalStorageDirectory()+ "/Pictures/" + IMAGE_BW_DIRECTORY_NAME).list();
    private byte[][] matrixOfImages = new byte[RES][numberOfImages];
    private byte[] imageArray = new byte [RES];
    // array of supported extensions (use a List if you prefer)
//    static final String[] EXTENSIONS = new String[]
//            {
//                    "JPEG"
//            };
    // filter to identify images based on their extensions
//    static final FilenameFilter IMAGE_FILTER = new FilenameFilter(){
//        @Override
//        public boolean accept(final File dir, final String name) {
//            for (final String ext : EXTENSIONS) {
//                if (name.endsWith("." + ext)) {
//                    return (true);
//                }
//            }
//            return (false);
//        }
//    };

    public void Detection()
    {
        for(int i = 0; i < numberOfImages; i++)
        {
            //trasformo tutte le immagini in array
            imageArray = FromJpegToArray (Environment.getExternalStorageDirectory()+ "/Pictures/" + IMAGE_BW_DIRECTORY_NAME + "/" + fileNames[i]);

            for(int j = 0; j < RES; j++)
            {
                matrixOfImages[j][i] = imageArray [j];
            }
            imageArray = null;
        }
    }



    //convertire JPEG in bitmap

    public Bitmap FromJpegToBitmap(String _writename)
    {
        File root = Environment.getExternalStorageDirectory();
        return BitmapFactory.decodeFile(root+"/Smart Patroling/"+ _writename);
    }

    //convertire la bitmap in un array

    public byte[] FromBitmapToArray(Bitmap bmp)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    //convertire da jpeg a array

    public byte[] FromJpegToArray (String _filepath)
    {
        File root = Environment.getExternalStorageDirectory();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        BitmapFactory.decodeFile (_filepath).compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public int[] AverageImage(byte[][] images )
    {
        int[] avgImage=new int[RES];
        int[] sumImage=new int[RES];
        for(int i=0;i<numberOfImages;i++)
        {
            for(int j=0;j<RES;j++)
            {
                sumImage[j] += images[j][i];
            }
        }
        for(int k=0;k<sumImage.length;k++) {
            avgImage[k] = sumImage[k] / numberOfImages;
        }
        return avgImage;
    }

    public int[][] EigenImage(byte[][] images ,int[] avg) {
        int[][] eigen = new int[RES][numberOfImages];

        for (int i = 0; i < numberOfImages; i++) {
            for(int j=0;j<RES;j++) {
                eigen[j][i]= images[j][i]-avg[j];
            }
        }
        return eigen;
    }

    //matrice di covarianza

    public int[][] Covarianza(int[][] eigen){
        int[][] covarianza=new int[RES][RES];
        //traspongo eigen
        int[][] eigenT=transposeMatrix(eigen);
        for (int i = 0; i < RES; i++)
            for (int j = 0; j < numberOfImages; j++)
                for (int k = 0; k < numberOfImages; k++)
                    covarianza[i][j] += eigen[i][k] * eigenT[k][j];
        for(int i=0;i<covarianza.length;i++)
            for(int j=0;j<covarianza.length;j++)
                covarianza[i][j]=covarianza[i][j] / numberOfImages;

        return covarianza;
    }

    public static int[][] transposeMatrix(int [][] m){
        int[][] temp = new int[m[0].length][m.length];
        for (int i = 0; i < m.length; i++)
            for (int j = 0; j < m[0].length; j++)
                temp[j][i] = m[i][j];
        return temp;
    }
}
