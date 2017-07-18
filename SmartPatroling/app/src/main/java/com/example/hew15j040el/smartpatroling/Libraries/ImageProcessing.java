package com.example.hew15j040el.smartpatroling.Libraries;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Environment;

/**
 * Created by HEW15J040EL on 12/07/2017.
 */

public class ImageProcessing extends Activity {

    private static final String IMAGE_DIRECTORY_NAME = "Smart Patroling";
    private static final int RES = 129600;

    public static Bitmap toGreyScale(Bitmap bmpOriginal, Bitmap bmpGrayscale, Canvas canGray)
    {
        bmpGrayscale = Bitmap.createBitmap(360, 360,Bitmap.Config.ARGB_8888);
        canGray = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        while (true)
        {
            if (bmpOriginal != null && !bmpOriginal.isRecycled())
                break;
        }
        canGray.drawBitmap(bmpOriginal, new Rect(0, 0, bmpOriginal.getWidth(), bmpOriginal.getHeight()), new Rect(0, 0, 360, 360), paint);
        return bmpGrayscale;
    }

    public static Bitmap CutTo360x360(String fileName)
    {
        Bitmap trainingBmp = StorageInteraction.FromJpegToBitmap(Environment.getExternalStorageDirectory() + "/Pictures/" + IMAGE_DIRECTORY_NAME + "/" + fileName);
        Matrix rotate = new Matrix();
        rotate.preRotate(90);
        //ritaglio in modo diverso a seconda del formato della foto
        int bmpFormat = trainingBmp.getWidth() - trainingBmp.getHeight();
        int topCut = (int) (bmpFormat * 0.4);
        int bottomCut = (int) (bmpFormat * 0.6);
        return Bitmap.createBitmap(trainingBmp, topCut, 0, trainingBmp.getWidth() - bottomCut - topCut, trainingBmp.getHeight(), rotate, true);

    }

    public static Bitmap toGreyScale(Bitmap rotatedBitmap, Bitmap bmpOriginal, Bitmap bmpGrayscale, Canvas canGray)
    {
        bmpGrayscale = Bitmap.createBitmap(360, 360, Bitmap.Config.ARGB_8888);
        canGray = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        cm = null;
        paint.setColorFilter(f);
        f = null;
        Matrix rotate = new Matrix();
        rotate.preRotate(90);
        //metto dentro rotateBitmap bmpOriginal girata dritta
        rotatedBitmap=Bitmap.createBitmap(bmpOriginal,0,0,bmpOriginal.getWidth(),bmpOriginal.getHeight(),rotate,true);
        rotate = null;
        //ritaglio in modo diverso a seconda del formato della foto
        int bmpFormat = bmpOriginal.getWidth() - bmpOriginal.getHeight();
        int topCut = (int)(bmpFormat * 0.4);
        int bottomCut = (int)(bmpFormat * 0.6);
        while (true)
        {
            if (rotatedBitmap != null && !rotatedBitmap.isRecycled())
                break;
        }
        canGray.drawBitmap(rotatedBitmap, new Rect(0,topCut,bmpOriginal.getHeight(), bmpOriginal.getWidth() - bottomCut), new Rect(0,0,360,360), paint);
        paint = null;

        return bmpGrayscale;
    }

    //convertire la bitmap in un array
    public static float[] FromBitmapToArray(Bitmap bmp, float[] imageArray)
    {
        for (int r = 0; r < 360; r++) {
            for (int c = 0; c < 360; c++) {
                imageArray[c + 360 * r] = bmp.getPixel(c, r) & 0x000000FF; //maschera per considerare un solo byte tanto R G e B sono uguali essendo immagine in b/n
            }
        }
        return imageArray;
    }

    public static float[] FromJpegToArray (String _filepath, float[] imageArray)
    {
        return FromBitmapToArray (BitmapFactory.decodeFile (_filepath), imageArray);
    }

    public static float[] AverageImage(float[][] images, float[] avgImage, int numberOfImages)
    {
        float[] sumImage = new float[RES];
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
        return avgImage;
    }

    public static float[][] SubtractMeanOfAllImages(float[][] images, float[] avg, int numberOfImages)
    {
        for (int c = 0; c < numberOfImages; c++)
        {
            for(int r = 0; r < RES; r++)
            {
                images[r][c]= images[r][c]-avg[r];
            }
        }
        return images;
    }

    public static float[] SubtractMean(float[] singleImg, float[] avg)
    {
        for(int j=0;j<RES;j++)
        {
            singleImg[j]= singleImg[j]-avg[j];
        }
        return singleImg;
    }
}
