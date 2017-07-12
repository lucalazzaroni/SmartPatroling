package com.example.hew15j040el.smartpatroling.Methods;

import android.app.Activity;
import android.graphics.Bitmap;
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
//        canGray.drawBitmap(bmpOriginal, new Rect(140,0,500,360),new Rect(0,0,360,360), paint);

        //bmpGrayscale.setPixel(0,0, bmpOriginal.getPixel(0,0)    );

        // recyclingCanvas(canGray);
        return bmpGrayscale;
    }

    public static Bitmap CutTo360x360(Bitmap trainingBmp, String fileName)
    {
        trainingBmp = StorageInteraction.FromJpegToBitmap(Environment.getExternalStorageDirectory() + "/Pictures/" + IMAGE_DIRECTORY_NAME + "/" + fileName);
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
//        canvas  = new Canvas(rotatedBitmap);
        // l immagine e ancora ruotat verso sinistra perciò larghezza e altezza sono invertite
//        canvas.drawBitmap(bmpOriginal,new Rect(0,0,bmpOriginal.getHeight(),bmpOriginal.getWidth()), new Rect(), null);
        // metto dentro bmpGrayScale la rotatedBitmap in B/W

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


}
