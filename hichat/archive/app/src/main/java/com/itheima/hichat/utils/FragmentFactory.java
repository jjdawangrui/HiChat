package com.itheima.hichat.utils;

import android.support.v4.app.Fragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Apple on 2016/12/1.
 * Fragment的工厂类
 */

public final class FragmentFactory {
    private FragmentFactory() {
    }
    private static Map<Class,Fragment> map = new HashMap<>();


    /**
     * 这里挺牛逼，先进来是空，创建fragment实例，并放入集合，以后就不用了，直接return
     * 类的字节码作为key，fragment作为value
     */
    //获取实例                                              只能是fragment的子类
    public static synchronized Fragment getInstance(Class<? extends Fragment> clazz){
        //从集合里面获取
        Fragment fragment = map.get(clazz);
        if(fragment == null){
            try{
                fragment = clazz.newInstance();
                //保存进集合
                map.put(clazz,fragment);
            }catch (Exception e){

            }
        }
        return fragment;
    }

    //清空
    public static void clear(){
        map.clear();
    }

}
