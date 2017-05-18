package com.example.hew15j040el.smartpatroling;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by HEW15J040EL on 10/05/2017.
 */

public class SavePhotoTraining extends Activity {

    Context context = null;
    Bitmap bp;
    TextView name;
    EditText writename;
    Button bttSaveHome;
    Button bttSaveContinue;
    String imgpath;
    String _writename;
    //Button bttCancel;
    ImageView image;


    private File tmpFile;
    private Uri tmpFileURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_photo_training);
        image=(ImageView)findViewById(R.id.image);
        //aprire la fotocamera
        context=getApplicationContext();



        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File()));
        startActivityForResult(camera,0);
//        try
//        {
//            tmpFile = File.createTempFile("my_app", ".jpg");
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//        tmpFileURI = Uri.fromFile(tmpFile);

//        File file =new File(Environment.getExternalStorageDirectory(),"file");
//
//        Intent camera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//
//        camera.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,Uri.fromFile(file) );
//        startActivityForResult(camera,0);//zero can be replaced with any action code




        bttSaveHome = (Button)findViewById(R.id.bttSaveHome);

        bttSaveHome.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                _writename =writename.getText().toString();

                capturePhoto(null);
                Intent ima = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(ima);
            }
        });

        bttSaveContinue = (Button)findViewById(R.id.bttSaveContinue);

        bttSaveContinue.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                _writename =writename.getText().toString();

                capturePhoto(null);
                Intent itpt = new Intent(getApplicationContext(),SavePhotoTraining.class);
                startActivity(itpt);
            }
        });

        name=(TextView)findViewById(R.id.name);
        name.setText("Name:");
        writename=(EditText)findViewById(R.id.writename);

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        bp = (Bitmap) data.getExtras().get("data");
        image.setImageBitmap(bp);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void capturePhoto(View v){
        try {
            PhotoSaver2 ps = new PhotoSaver2(context,bp,_writename );
            ps.record();
        }
        catch (Exception e) {

            Toast.makeText(getApplicationContext(), "Picture error!",Toast.LENGTH_SHORT).show();
        }

    }

}
