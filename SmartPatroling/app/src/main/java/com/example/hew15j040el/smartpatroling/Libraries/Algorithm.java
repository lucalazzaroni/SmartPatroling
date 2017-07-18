package com.example.hew15j040el.smartpatroling.Libraries;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Environment;
import java.io.File;
import Jama.EigenvalueDecomposition;
import Jama.Matrix;

/**
 * Created by HEW15J040EL on 24/05/2017.
 */

public class Algorithm extends Activity {

    private static final String IMAGE_BW_DIRECTORY_NAME = "Smart Patroling BW";
    public int numberOfImages = new File(Environment.getExternalStorageDirectory()+"/Pictures/" + IMAGE_BW_DIRECTORY_NAME).listFiles().length;
    private static final int RES = 129600;
    private String[] fileNames = new File(Environment.getExternalStorageDirectory()+ "/Pictures/" + IMAGE_BW_DIRECTORY_NAME).list();
    private float[][] matrixOfImages = new float[RES][numberOfImages];
    private float[] imageArray = new float[RES];
    double[][] cov = new double[numberOfImages][numberOfImages];
    double[][] mostSigVectors = null;
    private double[][] coeffEigFace;
    double[] coeff = null;
    double[][] distance = null;
    private float[] avgImage = new float[RES];
    private double[][] eigenFace;
    public float minDist;

    public void Train(float percentage)
    {
        for(int i = 0; i < numberOfImages; i++)
        {
            //trasformo tutte le immagini in array
            ImageProcessing.FromJpegToArray (Environment.getExternalStorageDirectory()+ "/Pictures/" + IMAGE_BW_DIRECTORY_NAME + "/" + fileNames[i], imageArray);

            for(int j = 0; j < RES; j++)
            {
                matrixOfImages[j][i] = imageArray [j];
            }
        }
        //calcolo la matrice media
        avgImage = ImageProcessing.AverageImage(matrixOfImages, new float[RES], numberOfImages);
        //sottraggo la media
        matrixOfImages = ImageProcessing.SubtractMeanOfAllImages(matrixOfImages, avgImage, numberOfImages);
        //calcolo matrice di covarianza
        cov = MatProcessing.Covariance(matrixOfImages, numberOfImages, cov);
        //calcolo autovettori associati agli autovalori più significativi
        FindSignificantEigenVectors(cov, percentage);
        cov = null;
        //calcolo autofacce associate alle immagini
        ComputeEigenFace(mostSigVectors, matrixOfImages);
        mostSigVectors = null;
        //calcolo coefficienti associati alle immagini
        ComputeCoeffEigenFaceOfAllImages(eigenFace, matrixOfImages);
    }

    public String Recognize(float threshold, Bitmap bmpGreyScale)
    {
        //metto immagine b/n in un array
        ImageProcessing.FromBitmapToArray(bmpGreyScale, imageArray);
        recylingBitmap(bmpGreyScale);
        //sottraggo la media delle immagini nel database all'immagine
        imageArray = ImageProcessing.SubtractMean(imageArray, avgImage);
        //calcolo coefficienti associati all'immagine
        ComputeCoeffEigenFace(eigenFace, imageArray);
        int index = FindMinEuclideanDistance(coeff, coeffEigFace, threshold);
        coeff = null;
        if(index != -1)
        {
            return fileNames[index];
        }
        else
            return null;
    }

    public void FindSignificantEigenVectors(double[][] covarM, float chosenPercentage)
    {
        Matrix cov = new Matrix(covarM);
        EigenvalueDecomposition E = cov.eig();
        cov = null;
        double[] eigValue = MatProcessing.Diag(E.getD().getArray());
        double[][] eigVector = E.getV().getArray(); //gli autovettori sono vettori colonna di eigVector
        E = null;
        //Bubblesort mi ordina autovalori e autovettori
        eigVector = MatProcessing.BubbleSort(eigValue, eigVector, numberOfImages);

        float eigSum = 0;
        for(int i = 0; i < eigValue.length; i++)
        {
            eigSum += eigValue[i];
        }
        float percentage = 0;
        float partialEigSum = 0;
        int eigCount = 0;
        while(percentage < chosenPercentage)
        {
            partialEigSum += eigValue[eigCount++];
            percentage = partialEigSum / eigSum;
        }
        eigValue = null;
        mostSigVectors = new double[eigVector.length][eigCount+1];
        for(int i = 0; i < eigVector.length; i++)
        {
            for(int j = 0; j <= eigCount; j++)
            {
                mostSigVectors[i][j] = eigVector[i][j];
            }
        }
        eigVector = null;
    }

    public void ComputeEigenFace(double[][] eigVector, float[][] imagesLessMean)
    {
        eigenFace = new double[imagesLessMean.length][eigVector[0].length];
        for(int r=0;r<imagesLessMean.length ;r++){
            for (int c=0;c<eigVector[0].length;c++)
                for(int i=0;i<eigVector.length;i++)
                {
                    eigenFace[r][c]+=imagesLessMean[r][i]*eigVector[i][c];
                }
        }

    }

    public void ComputeCoeffEigenFaceOfAllImages(double[][] eigenFace, float[][] imagesLessMean)
    {
        //i vettori dei coefficienti di ogni singola immagine sono vettori colonna
        coeffEigFace = new double[eigenFace[0].length][imagesLessMean[0].length];
        for (int i = 0; i < eigenFace[0].length ; i++)
            for (int j = 0; j <imagesLessMean[0].length; j++)
                for (int k = 0; k < RES; k++)
                    coeffEigFace[i][j] += eigenFace[k][i] * imagesLessMean[k][j];
    }

    public void ComputeCoeffEigenFace(double[][] eigenFace, float[] imageLessMean)
    {
        coeff = new double[eigenFace[0].length];
        for(int c = 0; c < eigenFace[0].length; c++)
        {
            for(int r = 0; r < eigenFace.length; r++)
            {
                //moltiplico le colonne di eigenface per la colonna di imgLessMean (traspongo eigenFace tramite indici)
                coeff[c] += eigenFace[r][c] * imageLessMean[r];
            }
        }

    }

    public int FindMinEuclideanDistance(double[] coeff, double[][] coeffEigFace, float threshold)
    {
        double tempDist = 0;
        minDist = 100; //inizializzo la distanza a 100: valore sicuramente oltre la distanza euclidea
        int index = -1; // se index = -1 nessuna immagine corrisponde, quindi non riconosco nessuna faccia
        double[][] EucDist = MatProcessing.ComputeDistance(coeff, coeffEigFace, distance);
        //calcolo la distanza euclidea
        for(int c = 0; c < coeffEigFace[0].length; c++)
        {
            for( int r = 0; r < coeffEigFace.length; r++)
            {
                //elevo al quadrato
                EucDist[r][c] *= EucDist[r][c];
                tempDist += EucDist[r][c];
            }
            tempDist = Math.sqrt(tempDist) / 10000000;
            if(tempDist < minDist && tempDist < threshold)
            {
                minDist = (float)tempDist;
                index = c;
            }
        }
        return index;
    }

    public void recylingBitmap (Bitmap bm)
    {
        if(bm!=null){
            bm.recycle();
            bm=null;
        }
    }
}

