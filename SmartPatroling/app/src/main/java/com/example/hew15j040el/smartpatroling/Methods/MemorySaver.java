package com.example.hew15j040el.smartpatroling.Methods;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by HEW15J040EL on 12/07/2017.
 */

public class MemorySaver extends Activity {
    public static void recylingBitmap (Bitmap bm)
    {
        if(bm != null && !bm.isRecycled()){
            bm.recycle();
            bm = null;
        }
    }

    public static void recyclingCanvas(Canvas cv)
    {
        if (cv != null) {
            cv.setBitmap(null);
            cv = null;
        }
    }
}
