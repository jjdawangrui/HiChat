package com.itheima.hichat.utils;

import android.widget.TextView;

/**
 * Created by Apple on 2016/12/6.
 */

public final class TextViewUtils {
    private TextViewUtils() {
    }

    public static String getContent(TextView textView){
        return textView.getText().toString().trim();
    }
}
