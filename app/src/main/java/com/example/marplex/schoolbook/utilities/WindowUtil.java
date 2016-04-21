package com.example.marplex.schoolbook.utilities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;

/**
 * Created by marco on 4/21/16.
 */
public class WindowUtil {
    public static void changeStatusBarColor(Activity activity, String color){
        if (Build.VERSION.SDK_INT >= 21) {
            //Statusbar color
            activity.getWindow().setStatusBarColor(Color.parseColor(color));
        }
    }
}
