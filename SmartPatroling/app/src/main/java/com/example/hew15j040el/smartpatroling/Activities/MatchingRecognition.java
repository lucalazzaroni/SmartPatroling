package com.example.hew15j040el.smartpatroling.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hew15j040el.smartpatroling.AsyncTask.AlgorithmEnded;
import com.example.hew15j040el.smartpatroling.AsyncTask.BackgroundTask;
import com.example.hew15j040el.smartpatroling.Libraries.ImageProcessing;
import com.example.hew15j040el.smartpatroling.Libraries.Algorithm;
import com.example.hew15j040el.smartpatroling.R;
import com.example.hew15j040el.smartpatroling.Libraries.StorageInteraction;
import java.io.File;

/**
 * Created by HEW15J040EL on 10/05/2017.
 */

public class MatchingRecognition extends Activity implements AlgorithmEnded {

    ImageButton bttHome;
    Button bttContinueRec;
    TextView name = null;
    ImageView imgRec;
    ImageView imgTra;
    Bitmap imgBitmap = null;
    Bitmap bmpGrayscale = null;
    Canvas canGray = null;
    Bitmap rotatedBitmap = null;
    Bitmap trainingBmp = null;
    String fileName = null;
//    private static final String IMAGE_DIRECTORY_NAME = "Smart Patroling";
//    public static Algorithm algorithm;
    static int numberOfImages = -1; //numero di immagini non possibile
    static float percentage = -1; //percentuale non possibile
    static int i = 0; //per debug
    ProgressDialog progress = null;
    private AlgorithmEnded AE = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matching_recognition);

        imgRec = (ImageView) findViewById(R.id.imgRec);
        imgTra = (ImageView) findViewById(R.id.imgTra);
        name = (TextView)  findViewById(R.id.name);
        name.setText("Unknown");
        bttHome = (ImageButton)findViewById(R.id.bttHome);
        AE = this;
        progress = new ProgressDialog(this);
        //prendo la bitmap dell'immagine scattata nella TakePhotoRecognition
//        if(getIntent().hasExtra("imageByteArray")) {
//            byte[] byteArray = getIntent().getByteArrayExtra("imageByteArray");
//            imgBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        //ritaglio la bitmap del drone per vederla quadrata(siccome ritaglio la sessa immagine dove salvo la terza misura è 360 perchè non parte da 0 ma dal 140
        try
        {
//            bmpGrayscale = StorageInteraction.FakeTakeFromMemory(i++);//utilizzare l'app con foto del training anziche del drone
            imgBitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/Pictures/Drone Pictures/tempdrone.jpeg");
            imgBitmap = Bitmap.createBitmap(imgBitmap, 140, 0, 360, 360, null, true);

            new BackgroundTask(getApplicationContext(), imgBitmap, progress, AE, name, imgTra).execute();


//            bmpGrayscale = StorageInteraction.TakeFromMemory(imgBitmap, bmpGrayscale, canGray);
//            float currentPercentage = Settings.percentage;
//            int currentNumberOfImages = new File(Environment.getExternalStorageDirectory() + "/Pictures/Smart Patroling BW").listFiles().length;
//
//            if (currentNumberOfImages != numberOfImages || currentPercentage != percentage) {
//                percentage = currentPercentage;
//                numberOfImages = currentNumberOfImages;
//                algorithm = new Algorithm();
////                new BackgroundTask(progress).execute();
//                algorithm.Detection(Settings.percentage);
////                while (!isFinished){} //aspetto l'esecuzione dell'asynctask
//            }
//
//            String fileName = algorithm.Recognize(Settings.distance, bmpGrayscale);

//            Toast.makeText(getApplicationContext(), "Distance: " + algorithm.minDist, Toast.LENGTH_LONG).show();



//            if (fileName != null) {
////                trainingBmp = StorageInteraction.FromJpegToBitmap(Environment.getExternalStorageDirectory() + "/Pictures/" + IMAGE_DIRECTORY_NAME + "/" + fileName);
////                Matrix rotate = new Matrix();
////                rotate.preRotate(90);
////                //ritaglio in modo diverso a seconda del formato della foto
////                int bmpFormat = trainingBmp.getWidth() - trainingBmp.getHeight();
////                int topCut = (int) (bmpFormat * 0.4);
////                int bottomCut = (int) (bmpFormat * 0.6);
////                rotatedBitmap = Bitmap.createBitmap(trainingBmp, topCut, 0, trainingBmp.getWidth() - bottomCut - topCut, trainingBmp.getHeight(), rotate, true);
////                rotate = null;
//
////                rotatedBitmap = Bitmap.createBitmap(trainingBmp,0,0,trainingBmp.getWidth(),trainingBmp.getHeight(),rotate,true);
//
////                canGray  = new Canvas(rotatedBitmap);
////                canGray.drawBitmap(trainingBmp,new Rect(0,0,1600,1200), new Rect(), null);
//
//                //funziona per l immagine girata a sx rispetto ad una dritta
////                  canGray.drawBitmap(bmpOriginal, new Rect(200,0,1400,1200),new Rect(0,0,360,360), paint);
//                // funziona con l immagine dritta
////                canGray.drawBitmap(rotatedBitmap, new Rect(0,150,1200,1350),new Rect(0,0,360,360), null);
//                imgTra.setImageBitmap(ImageProcessing.CutTo360x360(trainingBmp, fileName));
////                imgTra.setImageBitmap(rotatedBitmap);
//                name.setText(fileName);
//            }
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////

            //metto in imgRec l'immagine scattata nella TakePhotoRecognition
            imgRec.setImageBitmap(imgBitmap);
        }
        catch (OutOfMemoryError oome)
        {
            AlertDialog alertDialog = new AlertDialog.Builder(MatchingRecognition.this).create();
            alertDialog.setTitle("Not enough memory");
            alertDialog.setMessage("You have too much photos in the training set, please delete some!");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finishAffinity(); //chiudi l'app in seguito all'outOfMemoryError
                        }
                    });
            alertDialog.show();
        }
//        }
        bttHome.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent ima = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(ima);
            }
        });

        bttContinueRec = (Button)findViewById(R.id.bttContinueRec);

        bttContinueRec.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent itpr = new Intent(getApplicationContext(),TakePhotoRecognition.class);
                startActivity(itpr);
            }
        });
    }

    @Override
    public void onAlgorithmEnd(String fileName, Bitmap imgBm) {
        name.setText(fileName);
        imgTra.setImageBitmap(imgBm);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(imgBitmap != null && !imgBitmap.isRecycled())
            recyclingCanvas(canGray);
//
        recylingBitmap(bmpGrayscale);
        recylingBitmap(imgBitmap);

//        algorithm = null;
        recylingBitmap(rotatedBitmap);
        recylingBitmap(trainingBmp);
        imgTra = null;
        imgRec = null;
    }

    public void recylingBitmap (Bitmap bm)
    {
        if(bm != null && !bm.isRecycled()){
            bm.recycle();
            bm = null;
        }
    }

    public void recyclingCanvas(Canvas cv)
    {
        if (cv != null) {
            cv.setBitmap(null);
            cv = null;
        }
    }
//    public void toGreyScale(Bitmap bmpOriginal)
//    {
//        bmpGrayscale = Bitmap.createBitmap(360, 360,Bitmap.Config.ARGB_8888);
//        canGray = new Canvas(bmpGrayscale);
//
//        Paint paint = new Paint();
//        ColorMatrix cm = new ColorMatrix();
//        cm.setSaturation(0);
//        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
//        paint.setColorFilter(f);
//        while (true)
//        {
//            if (bmpOriginal != null && !bmpOriginal.isRecycled())
//                break;
//        }
//            canGray.drawBitmap(bmpOriginal, new Rect(0, 0, bmpOriginal.getWidth(), bmpOriginal.getHeight()), new Rect(0, 0, 360, 360), paint);
////        canGray.drawBitmap(bmpOriginal, new Rect(140,0,500,360),new Rect(0,0,360,360), paint);
//
//        //bmpGrayscale.setPixel(0,0, bmpOriginal.getPixel(0,0)    );
//
//        // recyclingCanvas(canGray);
//    }

//        public void TakeFromMemory()
//        {
//            //recupero immagine scattata col drone dalla memoria
//            imgBitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/Pictures/Drone Pictures/tempdrone.jpeg");
//            //taglio
//            imgBitmap = Bitmap.createBitmap(imgBitmap, 140, 0, 360, 360, null, true);
//            //converto in bianco e nero
//            ImageProcessing.toGreyScale(imgBitmap, bmpGrayscale, canGray);
//
////            salvo la foto in B/N in memoria per questioni di debug
//            try
//            {
//                File bnFile;
//                String _bnPath = Environment.getExternalStorageDirectory() + "/Pictures/Drone Pictures BW/tempdrone.jpeg";
//                bnFile = new File(_bnPath);
//                FileOutputStream fosBw = new FileOutputStream(bnFile);
//                bmpGrayscale.compress(Bitmap.CompressFormat.JPEG, 100, fosBw);
//                fosBw.close();
////                recylingBitmap(bwImage);
//            }
//            catch (FileNotFoundException fnfe)
//            {
//                fnfe.printStackTrace();
//            }
//            catch (IOException ioe)
//            {
//                ioe.printStackTrace();
//            }
//        }



    //convertire JPEG in bitmap

//    public Bitmap FromJpegToBitmap(String _filepath)
//    {
//        return BitmapFactory.decodeFile( _filepath );
//    }

//    //Funzione utile per il debug senza drone
//    public void FakeTakeFromMemory()
//    {
//        int[] i = new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9};
//        bmpGrayscale = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/Pictures/Smart Patroling BW/lazza" + i[this.i++] + ".jpeg");
//    }
}
