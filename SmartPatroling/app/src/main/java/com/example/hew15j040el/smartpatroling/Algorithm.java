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
import  Jama.*;
import  Jama.EigenvalueDecomposition;
import Jama.Matrix;


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
            FromJpegToArray (Environment.getExternalStorageDirectory()+ "/Pictures/" + IMAGE_BW_DIRECTORY_NAME + "/" + fileNames[i]);

            for(int j = 0; j < RES; j++)
            {
                matrixOfImages[j][i] = imageArray [j];
            }
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
        for (int r=0;r<360;r++)
        {
            for(int c=0;c<360;c++)
            {
                imageArray[c] = (byte)(bmp.getPixel(c,r) & 0x000000FF); //maschera per considerare un solo byte tanto R G e B sono uguali essendo immagine in b/n
            }
        }
        return imageArray;
    }

    //convertire da jpeg a array

    public void FromJpegToArray (String _filepath)
    {
        FromBitmapToArray (BitmapFactory.decodeFile (_filepath));
    }


    public byte[] AverageImage(byte[][] images )
    {
        byte[] avgImage=new byte[RES];
        int[] sumImage=new int[RES];
        for(int i = 0; i < numberOfImages; i++)
        {
            for(int j = 0; j < RES; j++)
            {
                sumImage[j] += images[j][i];
            }
        }
        for(int k = 0; k < RES; k++) {
            avgImage[k] = (byte)(sumImage[k] / numberOfImages);
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

    //matrice di covarianza A'A

    public int[][] Covariance(byte[][] imagesLessMean)
    {
        int[][] cov = new int[numberOfImages][numberOfImages];

        //traspongo eigen
//        byte[][] eigenT = transposeMatrix(imagesLessMean);

        for (int i = 0; i < numberOfImages; i++)
            for (int j = 0; j < numberOfImages; j++)
                for (int k = 0; k < RES; k++)
                    cov[i][j] += imagesLessMean[k][i] * imagesLessMean[k][j];
        for (int i = 0; i < cov.length; i++)
            for(int j = 0; j < cov.length; j++)
                cov[i][j] = cov[i][j] / numberOfImages;

        return cov;
    }

    public double[][] FindSignificantEigenVectors(double[][] covarM)
    {
        Matrix cov = new Matrix(covarM);
        EigenvalueDecomposition E = cov.eig();
        double[] eigValue = Diag(E.getD().getArray());
        double[][] eigVector = E.getV().getArray();
        BubbleSort(eigValue);
        double eigSum = 0;
        for(int i = 0; i < eigValue.length; i++)
        {
            eigSum += eigValue[i];
        }
        double percentage = 0;
        double partialEigSum = 0;
        int eigCount = 0;
        while(percentage < 0.85)
        {
            partialEigSum += eigValue[eigCount++];
            percentage = partialEigSum / eigSum;
        }
        return  null;
    }

    static double[] Diag(double[][] m) {

        double[] d = new double[m.length];
        for (int i = 0; i< m.length; i++)
            d[i] = m[i][i];
        return d;
    }

    public void BubbleSort(double [] array) {

        for(int i = 0; i < array.length; i++)
        {
            boolean flag = false;
            for(int j = 0; j < array.length-1; j++)
            {
                //Se l' elemento j e minore del successivo allora
                //scambiamo i valori
                if(array[j]<array[j+1]) {
                    double k = array[j];
                    array[j] = array[j+1];
                    array[j+1] = k;
                    flag=true; //Lo setto a true per indicare che é avvenuto uno scambio
                }
            }
            if(!flag) break; //Se flag=false allora vuol dire che nell' ultima iterazione
            //non ci sono stati scambi, quindi il metodo può terminare
            //poiché l' array risulta ordinato
        }
    }
//
//    public static byte[][] transposeMatrix(byte [][] m)
//    {
//        byte[][] temp = new byte[m[0].length][m.length];
//        for (int i = 0; i < m.length; i++)
//            for (int j = 0; j < m[0].length; j++)
//                temp[j][i] = m[i][j];
//        return temp;
//    }
}

