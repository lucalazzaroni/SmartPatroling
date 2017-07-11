package com.example.hew15j040el.smartpatroling;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by HEW15J040EL on 07/07/2017.
 */

public class Settings extends Activity {
    public static float distance = 19;//default value
    public static float percentage = 1;//default value
    Spinner spinner;
    Spinner spinner1;
    ArrayAdapter<CharSequence> adapter;
    ArrayAdapter<CharSequence> adapter1;
    ImageButton bttDone;
//    static int positionSelected = 5;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        bttDone = (ImageButton) findViewById(R.id.bttDone);
        spinner = (Spinner) findViewById(R.id.spinner);
//        spinner.setSelection(positionSelected);
        adapter = ArrayAdapter.createFromResource(this, R.array.distance, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
//                positionSelected = position;
//                Toast.makeText(getBaseContext(), spinner.getItemAtPosition(position) + " selected", Toast.LENGTH_SHORT).show();
                String item = adapterView.getItemAtPosition(position).toString();
                try
                {
                    distance = Float.parseFloat(item);
                    Toast.makeText(getBaseContext(), spinner.getItemAtPosition(position) + " selected", Toast.LENGTH_SHORT).show();
                }

                catch(NumberFormatException nfe)
                {
                }


//                SaveValues(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner1 = (Spinner) findViewById(R.id.spinner1);
        adapter1 = ArrayAdapter.createFromResource(this, R.array.percentage, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String item = adapterView.getItemAtPosition(position).toString();
                item = item.replace("%", "");

                try
                {
                    percentage = Float.parseFloat(item) / 100;
                    Toast.makeText(getBaseContext(), spinner1.getItemAtPosition(position) + " selected", Toast.LENGTH_SHORT).show();
                }

                catch(NumberFormatException nfe)
                {
                }


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
    }


//    public void SaveValues(View view) {
//        distance = (double)spinner.getSelectedItem();
//        percentage = (double)(spinner1.getSelectedItem()) / 100;
//    }
}