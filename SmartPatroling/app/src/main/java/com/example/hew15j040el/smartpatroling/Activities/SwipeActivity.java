package com.example.hew15j040el.smartpatroling.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.hew15j040el.smartpatroling.R;

/**
 * Created by HEW15J040EL on 29/05/2017.
 */

public class SwipeActivity extends Activity {
    TextView tv;
    Button bttint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);
        tv = (TextView) findViewById(R.id.textView);
        bttint = (Button)findViewById(R.id.button);

    bttint.setOnClickListener(new View.OnClickListener() {

    @Override
    public void onClick(View v) {
        finish();
        Intent intent_home = new Intent (getApplicationContext(), MainActivity.class);
        startActivity(intent_home);
        overridePendingTransition(R.anim.nochange, R.anim.down);
        }
    });
    }
}