package com.example.hew15j040el.smartpatroling.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.hew15j040el.smartpatroling.R;

/**
 * Created by HEW15J040EL on 07/07/2017.
 */

public class Settings extends Activity {
    public static float distance = 17;//default value
    public static float percentage = 0.95f;//default value
    Spinner spinnerDis;
    Spinner spinnerPerc;
    ArrayAdapter<CharSequence> adapterDis;
    ArrayAdapter<CharSequence> adapterPerc;
    ImageButton bttDone;
    ImageButton bttHelp;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        bttDone = (ImageButton) findViewById(R.id.bttDone);
        bttHelp = (ImageButton) findViewById(R.id.bttHelp);
        spinnerDis = (Spinner) findViewById(R.id.spinner);
        adapterDis = ArrayAdapter.createFromResource(this, R.array.distance, android.R.layout.simple_spinner_item);
        adapterDis.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDis.setAdapter(adapterDis);
        spinnerDis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                String item = adapterView.getItemAtPosition(position).toString();
                try
                {
                    distance = Float.parseFloat(item);
                    Toast.makeText(getBaseContext(), spinnerDis.getItemAtPosition(position) + " selected", Toast.LENGTH_SHORT).show();
                }

                catch(NumberFormatException nfe){}
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerPerc = (Spinner) findViewById(R.id.spinner1);
        adapterPerc = ArrayAdapter.createFromResource(this, R.array.percentage, android.R.layout.simple_spinner_item);
        adapterPerc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPerc.setAdapter(adapterPerc);
        spinnerPerc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String item = adapterView.getItemAtPosition(position).toString();
                item = item.replace("%", "");

                try
                {
                    percentage = Float.parseFloat(item) / 100;
                    Toast.makeText(getBaseContext(), spinnerPerc.getItemAtPosition(position) + " selected", Toast.LENGTH_SHORT).show();
                }
                catch(NumberFormatException nfe){}
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        bttDone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent ima = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(ima);
            }
        });

        bttHelp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(Settings.this).create();
                alertDialog.setTitle("Hint:");
                alertDialog.setMessage(getString(R.string.settingsHint));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });
    }
}