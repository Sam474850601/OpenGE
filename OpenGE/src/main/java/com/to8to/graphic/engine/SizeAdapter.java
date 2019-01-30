package com.to8to.graphic.engine;


import android.content.Context;
import android.view.WindowManager;

/**
 * Created by same.li on 2018/5/10.
 */

public class SizeAdapter {



    private float designWith = 720;

    private float sale = 1;

    public SizeAdapter(Context context)
    {
        WindowManager systemService = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        sale =  systemService.getDefaultDisplay().getWidth()/designWith ;
    }


    public float getRealPx(float size)
    {
        return size*sale;
    }


}
