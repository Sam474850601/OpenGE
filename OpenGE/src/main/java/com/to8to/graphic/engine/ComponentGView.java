package com.to8to.graphic.engine;


import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by same.li on 2018/5/9.
 * 组件抽象类。通过编码添加到画板
 */

public abstract class ComponentGView extends GView {


    public static final int   MATCH_PARENT = -1;
    public static final int   WRAP_CONTENT = -2;



    @Override
    public   void onCreate(Intent data) {

    }


    //在绘制之前,测量和计算大小
    public    abstract void onMeasure();

    protected Rect contentRect = new Rect();


    public void layout(int left, int top, int right, int bottom) {
        contentRect.left = left;
        contentRect.top = top;
        contentRect.right = right;
        contentRect.bottom = bottom;
        onDraw(canvas);
        canvas = null;
    }


    @Override
    protected void onDraw(Canvas canvas) {

    }

    public void setPosition(float x, float y)
    {
        this.contentRect.left = (int) x;
        this.contentRect.top= (int) y;
    }



    public    int contentWidth = MATCH_PARENT;
    public int contentHeight = WRAP_CONTENT;

    //设置组件的大小
    public void setContentSize(int width, int height)
    {
        this.contentWidth = width;
        this.contentHeight = height;
    }

    protected Canvas canvas;


    public void setCanvas(Canvas canvas)
    {
        this.canvas = canvas;
    }

    public int getContentWidth() {
        return contentWidth;
    }

    public int getContentHeight() {
        return contentHeight;
    }
}
