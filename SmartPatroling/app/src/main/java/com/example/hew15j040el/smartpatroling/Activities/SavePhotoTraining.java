package com.example.hew15j040el.smartpatroling.Activities;


import java.io.File;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.hew15j040el.smartpatroling.R;
import com.example.hew15j040el.smartpatroling.Libraries.StorageInteraction;

/**
 * Created by HEW15J040EL on 10/05/2017.
 */

public class SavePhotoTraining extends Activity {

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 0;
    public static final int MEDIA_TYPE_IMAGE = 1;
    Uri fileUri; // url file per salvare imgPreview/video
    Context context = null;
    Bitmap bitmap = null;
    Bitmap bmpGrayscale=null;
    Bitmap rotatedBitmap=null;
    Canvas canGray =null;
    TextView name;
    EditText writename = null;
    Button bttSaveHome;
    Button bttSaveContinue;
    String imgPath;
    ImageView imgPreview;
    File mediaFile;
    StrictMode.VmPolicy.Builder builder = null; //per funzionamento su android 7.x

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_photo_training);

        builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        context = this;
        imgPreview = (ImageView) findViewById(R.id.imgPreview);

        try
        {
            //aprire la fotocamera
            launchCamera();
        }
        catch (OutOfMemoryError oome)
        {
            AlertDialog alertDialog = new AlertDialog.Builder(SavePhotoTraining.this).create();
            alertDialog.setTitle("Not enough memory");
            alertDialog.setMessage("The App needs more memory to work!");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finishAffinity(); //chiudi l'app in seguito all'outOfMemoryError
                        }
                    });
            alertDialog.show();
        }

        bttSaveHome = (Button) findViewById(R.id.bttSaveHome);

        bttSaveHome.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(StorageInteraction.SaveColorAndBW(writename, getApplicationContext(),
                        rotatedBitmap, bitmap, bmpGrayscale, canGray, mediaFile))
                {
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
                if(StorageInteraction.SaveColorAndBW(writename, getApplicationContext(),
                        rotatedBitmap, bitmap, bmpGrayscale, canGray, mediaFile)) {
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
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

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
            }
            catch (NullPointerException e) {
                Toast.makeText(getApplicationContext(), "Picture error", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        else
            {
            Toast.makeText(getApplicationContext(), "Camera closed", Toast.LENGTH_SHORT).show();
            Intent ima = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(ima);
        }
    }

    private void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mediaFile = StorageInteraction.getOutputMediaFile(MEDIA_TYPE_IMAGE,getApplicationContext(),mediaFile,imgPath);
        fileUri = Uri.fromFile(mediaFile);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //dealloco memoria
        recyclingCanvas(canGray);
        recylingBitmap(bitmap);
        recylingBitmap(bmpGrayscale);
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
