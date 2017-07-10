package com.example.hew15j040el.smartpatroling;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Thread.sleep;

/**
 * Created by HEW15J040EL on 10/05/2017.
 */

public class SavePhotoTraining extends Activity {

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 0;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final String IMAGE_DIRECTORY_NAME = "Smart Patroling";
    private static final String IMAGE_BW_DIRECTORY_NAME = "Smart Patroling BW";
    private Uri fileUri; // file url to store imgPreview/video



    Context context = null;
    Bitmap bitmap = null;
    Bitmap bmpGrayscale=null;
    Bitmap rotatedBitmap=null;
    Canvas canGray =null;
//    Canvas canvas=null;
    TextView name;
    EditText writename = null;
    Button bttSaveHome;
    Button bttSaveContinue;
    String imgPath;
    String _writename;
    ImageView imgPreview;
    File mediaStorageDir;
    File mediaFile;
    File mediaStorageDirBW;
    Bitmap bnBitmap;
    StrictMode.VmPolicy.Builder builder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_photo_training);

        ///////////////////////////////////////////////////////////////////////////////////
        builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        ///////////////////////////////////////////////////////////////////////////////////

        context = this;

        imgPreview = (ImageView) findViewById(R.id.imgPreview);
        //aprire la fotocamera
//        context = getApplicationContext();

        launchCamera();

//        Intent camera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(camera,0);


        bttSaveHome = (Button) findViewById(R.id.bttSaveHome);


        bttSaveHome.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(saveNormalAndBW()) {

//                    bitmap.recycle();
                    Intent ima = new Intent(getApplicationContext(), MainActivity.class);

                    startActivity(ima);

                }
                else
                    return;
            }
        });

        bttSaveContinue = (Button) findViewById(R.id.bttSaveContinue);

        bttSaveContinue.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(saveNormalAndBW()) {

//                    bitmap.recycle();
                    Intent itpt = new Intent(getApplicationContext(), SavePhotoTraining.class);
                    startActivity(itpt);
                }
                else
                    return;
            }
        });

        name = (TextView) findViewById(R.id.name);
        name.setText("Name:");
        writename = (EditText) findViewById(R.id.writename);
//        File memory = Environment.getExternalStorageDirectory();
//        File from = new File(memory, imgPath);
//        partialImgPath = partialImgPath + _writename;
//        File to = new File(memory,"to.txt");
//        from.renameTo(to);


//        bttCancel = (Button)findViewById(R.id.bttCancel);
//
//        bttCancel.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Intent itpt = new Intent(getApplicationContext(),TakePhotoTraining.class);
//                startActivity(itpt);
//            }
//        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // SaveValues file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            try {
                imgPreview.setRotation(90);
                imgPreview.setVisibility(View.VISIBLE);
                BitmapFactory.Options options = new BitmapFactory.Options();
                bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                        options);

                imgPreview.setImageBitmap(bitmap);
//                bitmap.recycle();
            } catch (NullPointerException e) {

                Toast.makeText(getApplicationContext(), "Picture error", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
//            Toast.makeText(getApplicationContext(),
//                    "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
//                    .show();
            Toast.makeText(getApplicationContext(), "Camera closed", Toast.LENGTH_SHORT).show();

            Intent ima = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(ima);
        }
    }

    //    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // TODO Auto-generated method stub
//        super.onActivityResult(requestCode, resultCode, data);
//
//        bp = (Bitmap) data.getExtras().get("data");
//        imgPreview.setImageBitmap(bp);
//    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//    }

//    private void capturePhoto(View v) {
//        try {
//            PhotoSaver2 ps = new PhotoSaver2(context, bp, _writename);
//            ps.record();
//        } catch (Exception e) {
//
//            Toast.makeText(getApplicationContext(), "Picture error!", Toast.LENGTH_SHORT).show();
//        }
//
//    }

    private void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_IMAGE));

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

    }

    private File getOutputMediaFile(int type) {
        if (Environment.getExternalStorageState() != null) {
            mediaStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    IMAGE_DIRECTORY_NAME);

            // Create the storage directory if it does not exist
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
            Toast.makeText(getApplicationContext(), "Internal memory not available", Toast.LENGTH_SHORT).show();
        return  null;
    }

    public Bitmap toGreyScale(Bitmap bmpOriginal)
    {
//        int width, height;
//        height = bmpOriginal.getHeight();
//        width = bmpOriginal.getWidth();

        bmpGrayscale = Bitmap.createBitmap(360, 360, Bitmap.Config.ARGB_8888);
        canGray = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        cm=null;
        paint.setColorFilter(f);
        f=null;

        Matrix rotate = new Matrix();
        rotate.preRotate(90);
        //metto dentro rotateBitmap bmpOriginal girata dritta
        rotatedBitmap=Bitmap.createBitmap(bmpOriginal,0,0,bmpOriginal.getWidth(),bmpOriginal.getHeight(),rotate,true);
        rotate=null;
//        canvas  = new Canvas(rotatedBitmap);
        // l immagine e ancora ruotat verso sinistra perciò larghezza e altezza sono invertite
//        canvas.drawBitmap(bmpOriginal,new Rect(0,0,bmpOriginal.getHeight(),bmpOriginal.getWidth()), new Rect(), null);
        // metto dentro bmpGrayScale la rotatedBitmap in B/W

        //ritaglio in modo diverso a seconda del formato della foto
        int bmpFormat = bmpOriginal.getWidth() - bmpOriginal.getHeight();
        int topCut = (int)(bmpFormat * 0.4);
        int bottomCut = (int)(bmpFormat * 0.6);
        canGray.drawBitmap(rotatedBitmap, new Rect(0,topCut,bmpOriginal.getHeight(), bmpOriginal.getWidth() - bottomCut), new Rect(0,0,360,360), paint);
        paint=null;

        return bmpGrayscale;
    }

    public boolean saveNormalAndBW() {
        _writename = writename.getText().toString() + ".jpeg";

        if(TextUtils.isEmpty(writename.getText().toString())) //se non viene dato un nome all'immagine avvisa e non salvare
        {
            writename.setError("Image must have a name!");
            return false;
        }

//                File from = new File(mediaStorageDir.getPath(), "IMG_temp.jpg");
//        while(_writename == ".jpeg")
//        {
//            Toast.makeText(getApplicationContext(), "Image must have a name!", Toast.LENGTH_SHORT).show();
//            _writename = writename.getText().toString() + ".jpeg";
//        }
        File to = new File(mediaStorageDir.getPath(),_writename);
        if(!mediaFile.renameTo(to)){
            Toast.makeText(getApplicationContext(), "Image not renamed!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(context, "Picture saved", Toast.LENGTH_SHORT).show();
        }

        fileUri = Uri.fromFile(to); //percorso del file rinominato
//                BitmapFactory.Options optionsIm = new BitmapFactory.Options();
//                bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
//                        optionsIm);
        bnBitmap = toGreyScale(bitmap);
        try {
            File bnFile;
            String _bnPath = Environment.getExternalStorageDirectory()+"/Pictures/" + IMAGE_BW_DIRECTORY_NAME + "/"+ writename.getText().toString() + ".jpeg";
            bnFile = new File(_bnPath);
            FileOutputStream fos = new FileOutputStream(bnFile);
            bnBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
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

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            Toast.makeText(this, "Picture saved", Toast.LENGTH_SHORT).show();
//        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
//            Toast.makeText(this, "Picture saved", Toast.LENGTH_SHORT).show();
//        }
//
//    }

    @Override
    protected void onStop() {
        super.onStop();
        recyclingCanvas(canGray);
        recylingBitmap(bitmap);
        recylingBitmap(bnBitmap);
        recylingBitmap(bmpGrayscale);
//        recyclingCanvas(canvas);
        recylingBitmap(rotatedBitmap);
        builder = null;
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
}
