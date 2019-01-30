package com.to8to.graphic.engine;


import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;

/**
 * Created by same.li on 2018/5/7.
 * 涂鸦视图基类。可以继承它来实现任意canvas视图。
 */

public abstract class GView {

    private GraffitiBoardView parent;

    public abstract void onCreate(Intent data);



    protected abstract void onDraw(Canvas canvas);

    protected  boolean isContained(float x, float y) {
        return false;
    }

    public interface  OnFocusListener
    {
        void onFocus(boolean isFocus);
    }





    OnFocusClickListener onFocusClickListener;

    public void setOnFocusClickListener(OnFocusClickListener onFocusClickListener) {
        this.onFocusClickListener = onFocusClickListener;
    }

    public interface  OnFocusClickListener
    {
        void onFocusClick(GView gView);
    }


    OnFocusListener onFocusListener;

    public void setOnFocusLinstener(OnFocusListener onClickLinstener)
    {
        this.onFocusListener = onClickLinstener;
    }


    protected boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    public  void onDestroy(){
        setContext(null);
        setParent(null);
        isRecycled = true;
    }

    public final Paint paint = new Paint();
    protected final Path path = new Path();

    private Context context;
    protected void setContext(Context context)
    {
        this.context = context;
    }

    public Context getContext()
    {
        return context;
    }




    //是否已经绘制过
   public   boolean hasDraw = false;

      public boolean isFocus = false;


    //是否是动作操作视图
    public  boolean isMotionOperationView() {
        return true;
    }

    int  with, height;

    public int getWith() {
        return with;
    }

    public  void setWith(int with) {
        this.with = with;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Path getPath() {
        return path;
    }


    protected int color = Color.RED;

    public void setColor(int color) {
        this.color = color;
    }


   protected float startX, startY, endX, endY;

    public void setStart(float x, float y) {
        this.startX = x;
        this.startY = y;

    }

    public void setEnd(float x, float y) {
        this.endX = x;
        this.endY = y;
    }


    private volatile  boolean isRecycled;

    //是否被回收了
    public boolean isRecycled() {
        return isRecycled;
    }


    //如果不能完成视图需要绘制的规则，那么就不会添加到缓存。
    public boolean enableDraw() {
        return true;
    }

    public void setFocus(boolean isFocus) {
        this.isFocus = isFocus;
        if(null != onFocusListener)
        {
            onFocusListener.onFocus(isFocus);
        }
    }

    public GraffitiBoardView getParent() {
        return parent;
    }

    public void setParent(GraffitiBoardView parent) {
        this.parent = parent;
    }




}
