package com.example.hew15j040el.smartpatroling;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;

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
    private float[][] matrixOfImages = new float[RES][numberOfImages];
    private float[] imageArray = new float[RES];
    double[][] cov = new double[numberOfImages][numberOfImages];
    double[][] mostSigVectors =null;
    private double[][] coeffEigFace;
    double[] coeff=null;
    double[][] distance=null;
    private float[] avgImage=null;
    private double[][] eigenFace;
    public float minDist = 100; //inizializzo la distanza a 100: valore sicuramente oltre la distanza euclidea


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

    public void Detection(float percentage)
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
        //calcolo la matrice media
        AverageImage(matrixOfImages);
//        Bitmap eigenFaceBM = ArrayToBitmap(matrixOfImages, 2);
        //sottraggo la media
        SubtractMeanOfAllImages(matrixOfImages, avgImage);
        //calcolo matrice di covarianza
        Covariance(matrixOfImages);
        //calcolo autovettori associati agli autovalori più significativi
        FindSignificantEigenVectors(cov, percentage);
        cov = null;
        //calcolo autofacce associate alle immagini
        ComputeEigenFace(mostSigVectors, matrixOfImages);
        ////////////////////////////////////////////////////////////////////////////////////
//        Bitmap eigenFaceBM = ArrayToBitmap(eigenFace, 2);
        ////////////////////////////////////////////////////////////////////////////////////
        mostSigVectors = null;
        //calcolo coefficienti associati alle immagini
        ComputeCoeffEigenFaceOfAllImages(eigenFace, matrixOfImages);
    }

    public String Recognize(float threshold, Bitmap bmpGreyScale)
    {
        //metto immagine b/n in un array
        FromBitmapToArray(bmpGreyScale);
        recylingBitmap(bmpGreyScale);
        //sottraggo la media delle immagini nel database all'immagine
        SubtractMean(imageArray, avgImage);
        avgImage = null;
        //calcolo coefficienti associati all'immagine
        ComputeCoeffEigenFace(eigenFace, imageArray);
        eigenFace = null;
        imageArray = null;
        int index = FindMinEuclideanDistance(coeff, coeffEigFace, threshold);
        coeff = null;
        coeffEigFace = null;
        if(index != -1)
        {
            return fileNames[index];
        }
        else
            return null;
    }





    //convertire la bitmap in un array

    public void FromBitmapToArray(Bitmap bmp)
    {
        for (int r = 0; r < 360; r++)
        {
            for(int c = 0; c < 360; c++)
            {
                imageArray[c + 360*r] = bmp.getPixel(c,r) & 0x000000FF; //maschera per considerare un solo byte tanto R G e B sono uguali essendo immagine in b/n
            }
        }
    }

    //convertire da jpeg a array

    public void FromJpegToArray (String _filepath)
    {
        FromBitmapToArray (BitmapFactory.decodeFile (_filepath));
    }


    public void AverageImage(float[][] images)
    {
        avgImage=new float[RES];
        float[] sumImage=new float[RES];
        for(int row = 0; row < RES; row++)
        {
            for(int col = 0; col < numberOfImages; col++)
            {
                sumImage[row] += images[row][col];
            }
        }
        for(int k = 0; k < RES; k++) {
            avgImage[k] = sumImage[k] / numberOfImages;
        }
        sumImage = null;
    }

    public void SubtractMeanOfAllImages(float[][] images, float[] avg)
    {
        for (int c = 0; c < numberOfImages; c++)
        {
            for(int r = 0; r < RES; r++)
            {
                images[r][c]= images[r][c]-avg[r];
            }
        }
    }

    public void SubtractMean(float[] singleImg, float[] avg)
    {
        for(int j=0;j<RES;j++)
        {
            singleImg[j]= singleImg[j]-avg[j];
        }
    }

    //matrice di covarianza A'A

    public void Covariance(float[][] imagesLessMean)
    {
        for (int r = 0; r < numberOfImages; r++)
            for (int c = 0; c < numberOfImages; c++)
                for (int k = 0; k < RES; k++)
                    cov[r][c] += imagesLessMean[k][r] * imagesLessMean[k][c];
        for (int r = 0; r < cov.length; r++)
            for(int c = 0; c < cov[0].length; c++)
                cov[r][c] = cov[r][c] / numberOfImages;//normalizzo la covarianza
    }

    public void FindSignificantEigenVectors(double[][] covarM, float chosenPercentage)
    {
        Matrix cov = new Matrix(covarM);
        EigenvalueDecomposition E = cov.eig();
        cov = null;
        double[] eigValue = Diag(E.getD().getArray());
        double[][] eigVector = E.getV().getArray(); //gli autovettori sono vettori colonna di eigVector
        E = null;
        //Bubblesort mi ordina autovalori e autovettori
        BubbleSort(eigValue, eigVector);

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

    static double[] Diag(double[][] m) {

        double[] d = new double[m.length];
        for (int i = 0; i< m.length; i++)
            d[i] = m[i][i];
        return d;
    }


    public void BubbleSort(double [] eigValue, double [][] eigVector) {
        int[] index = new int[numberOfImages];
        for(int i = 0; i < numberOfImages; i++)
        {
            index[i] = i;
        }

        for(int i = 0; i < eigValue.length; i++)
        {
            double k;
            boolean flag = false;
            for(int j = 0; j < eigValue.length-1; j++)
            {
                //Se l' elemento j e minore del successivo allora
                //scambiamo i valori
                if(eigValue[j]<eigValue[j+1]) {
                    k = eigValue[j];
                    int temp = index[j];
                    eigValue[j] = eigValue[j+1];
                    index[j] = index[j+1];
                    eigValue[j+1] = k;
                    index[j+1] = temp;
                    flag=true; //Lo setto a true per indicare che é avvenuto uno scambio
                }
            }
            if(!flag) break; //Se flag=false allora vuol dire che nell' ultima iterazione
            //non ci sono stati scambi, quindi il metodo può terminare
            //poiché l' array risulta ordinato
        }
        // ordino gli autovettori in corrispondenza degli autovalori
        double[][] tempVector = new double[eigVector.length][eigVector[0].length];
        for(int col = 0; col < eigVector[0].length; col++)
        {
            for(int row = 0; row < eigVector.length; row++)
            {
                tempVector[row][col] = eigVector[row][index[col]];
            }
        }
        eigVector = tempVector;
        tempVector = null;
    }

    public void ComputeEigenFace(double[][] eigVector, float[][] imagesLessMean)
    {
//        Matrix eigVecM = new Matrix(eigVector);
//        Matrix imgLessMeanM = new Matrix(imagesLessMean);
//        double[][] eig = imgLessMeanM.times(eigVecM).getArray();
//        eigVecM = null;
//        imgLessMeanM = null;
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
//        Matrix eigenfaceM = new Matrix(eigenFace);
//        Matrix imgLessMeanM = new Matrix(imagesLessMean);
//        Matrix eigenT = eigenfaceM.transpose();
//        eigenfaceM = null;
//        double[][] eig = eigenT.times(imgLessMeanM).getArray();
//        imgLessMeanM = null;
//        eigenT = null;
//        return eig;
        coeffEigFace=new double[eigenFace[0].length][imagesLessMean[0].length];
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
        int index = -1; // se index = -1 nessuna immagine corrisponde, quindi non riconosco nessuna faccia
        double[][] EucDist = ComputeDistance(coeff, coeffEigFace);
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

    public double[][] ComputeDistance(double[] coeff, double[][] coeffEigFace)
    {
        distance = new double[coeffEigFace.length][coeffEigFace[0].length];
        for (int c = 0; c < coeffEigFace[0].length; c++)
        {
            for (int r = 0; r < coeffEigFace.length; r++)
            {
                distance[r][c] = coeff[r] - coeffEigFace[r][c];
            }
        }
        return distance;
    }

    public void recylingBitmap (Bitmap bm)
    {
        if(bm!=null){
            bm.recycle();
            bm=null;
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
//
//    public Bitmap ArrayToBitmap (double[][] myImages, int col)
//    {
//        int[] image = new int[myImages.length];
//        //immagine singola
//        for(int r = 0; r < myImages.length; r++)
//        {
//            image[r] = (int)(myImages[r][col]);
//        }
//        return Bitmap.createBitmap(image, 360, 360, Bitmap.Config.ARGB_8888);
//    }
}

