package com.itheima.hichat.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by Apple on 2016/12/7.
 */

public final class ImageViewUtils {

    //显示图片
    public static void showImage(Context context, String url, int resId, ImageView iv){
        Glide
                .with(context)
                .load(url)
                .placeholder(resId)
                .crossFade()
                .into(iv);
    }
}
