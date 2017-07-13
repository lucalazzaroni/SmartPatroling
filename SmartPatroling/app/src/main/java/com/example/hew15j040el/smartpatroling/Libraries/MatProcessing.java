package com.example.hew15j040el.smartpatroling.Libraries;

import android.app.Activity;

/**
 * Created by HEW15J040EL on 12/07/2017.
 */

public class MatProcessing extends Activity {

    private static final int RES = 129600;

    //matrice di covarianza A'A

    public static double[][] Covariance(float[][] imagesLessMean, int numberOfImages, double[][] cov)
    {
        for (int r = 0; r < numberOfImages; r++)
            for (int c = 0; c < numberOfImages; c++)
                for (int k = 0; k < RES; k++)
                    cov[r][c] += imagesLessMean[k][r] * imagesLessMean[k][c];
        for (int r = 0; r < cov.length; r++)
            for(int c = 0; c < cov[0].length; c++)
                cov[r][c] = cov[r][c] / numberOfImages;//normalizzo la covarianza
        return cov;
    }

    public static double[] Diag(double[][] m) {

        double[] d = new double[m.length];
        for (int i = 0; i< m.length; i++)
            d[i] = m[i][i];
        return d;
    }

    public static double[][] BubbleSort(double [] eigValue, double [][] eigVector, int numberOfImages) {
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
        return  eigVector;
    }

    public static double[][] ComputeDistance(double[] coeff, double[][] coeffEigFace, double[][] distance)
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
}
