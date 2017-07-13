package com.example.hew15j040el.smartpatroling.AsyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hew15j040el.smartpatroling.Activities.MatchingRecognition;
import com.example.hew15j040el.smartpatroling.Activities.Settings;
import com.example.hew15j040el.smartpatroling.Libraries.Algorithm;
import com.example.hew15j040el.smartpatroling.Libraries.ImageProcessing;
import com.example.hew15j040el.smartpatroling.Libraries.StorageInteraction;

import java.io.File;

/**
 * Created by HEW15J040EL on 13/07/2017.
 */

public class BackgroundTask extends AsyncTask<Void, Integer, String> {

    private ProgressDialog progress = null;
    private Context context = null;
    private Bitmap imgBitmap = null;
//    Bitmap bmpGrayscale = null;
    private static Algorithm algorithm;
    static int numberOfImages = -1; //numero di immagini non possibile
    static float percentage = -1; //percentuale non possibile
//    public static String fileName;
    private AlgorithmEnded myAE = null;
    TextView name = null;
    ImageView imgTra = null;

    public BackgroundTask(Context context, Bitmap imgBitmap, ProgressDialog progress, AlgorithmEnded myAE, TextView name, ImageView imgTra) {
        this.progress = progress;
//        this.algorithm = algorithm;
        this.context = context;
        this.imgBitmap = imgBitmap;
        this.myAE = myAE;
        this.name = name;
        this.imgTra = imgTra;
//        this.bmpGrayscale = bmpGrayscale;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress.setMax(100);
        progress.setIndeterminate(true);
        progress.setTitle("Please wait");
        progress.setMessage("Computing result...");
//        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//abilita la barra di caricamento
        progress.setCancelable(false);
//            progress.setProgress(0);
        progress.show();//mostra la finestra di dialogo di caricamento(rotellina che gira)
    }

    @Override
    protected String doInBackground(Void... arg0) {
        Bitmap bmpGrayscale = null;
//        bmpGrayscale = StorageInteraction.FakeTakeFromMemory(i++);//utilizzare l'app con foto del training anziche del drone
        bmpGrayscale = StorageInteraction.TakeFromMemory(imgBitmap, bmpGrayscale, new Canvas());
        float currentPercentage = Settings.percentage;
        int currentNumberOfImages = new File(Environment.getExternalStorageDirectory() + "/Pictures/Smart Patroling BW").listFiles().length;

        if (currentNumberOfImages != numberOfImages || currentPercentage != percentage) {
            percentage = currentPercentage;
            numberOfImages = currentNumberOfImages;
            Looper.prepare();
            algorithm = new Algorithm();
//                new BackgroundTask(progress).execute();
            algorithm.Detection(Settings.percentage);
//                while (!isFinished){} //aspetto l'esecuzione dell'asynctask
        }

        return algorithm.Recognize(Settings.distance, bmpGrayscale);
    }


    //aggiorna la barra di caricamento
//        @Override
//        protected void onProgressUpdate(Integer... values)
//        {
//            super.onProgressUpdate(values);
//            progress.setProgress(values[0].intValue());
//        }

    @Override
    protected void onPostExecute(String fileName) {
        super.onPostExecute(fileName);
        progress.dismiss();
        if (fileName != null) {

            imgTra.setImageBitmap(ImageProcessing.CutTo360x360(fileName));
            name.setText(fileName);
            myAE.onAlgorithmEnd(fileName, ImageProcessing.CutTo360x360(fileName));
        }
        if(algorithm.minDist != 100)
            Toast.makeText(context, "Distance: " + algorithm.minDist, Toast.LENGTH_LONG).show();
        else
            Toast.makeText(context, "Distance over threshold!", Toast.LENGTH_LONG).show();
    }

    public void recylingBitmap (Bitmap bm)
    {
        if(bm != null && !bm.isRecycled()){
            bm.recycle();
            bm = null;
        }
    }
}
