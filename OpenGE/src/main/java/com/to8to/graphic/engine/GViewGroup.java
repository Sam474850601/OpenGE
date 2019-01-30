package com.to8to.graphic.engine;

import android.graphics.Canvas;

import java.util.LinkedList;

/**
 * Created by same.li on 2018/5/10.
 * 布局基类。主要用来布局ComponentGView类
 */

public abstract class GViewGroup extends ComponentGView {

    private final  LinkedList<ComponentGView> childViews = new LinkedList<>();

    public LinkedList<ComponentGView> getChildViews() {
        return childViews;
    }

    @Override
    public  void onMeasure() {
        contentWidth = MATCH_PARENT == contentWidth?with:0;
        contentHeight = MATCH_PARENT == contentHeight?height:0;
    }

    protected   abstract void onLayout(int left, int top, int right , int bottom);


    @Override
    protected final void onDraw(Canvas canvas) {
        for (ComponentGView childView : childViews) {
             childView.setCanvas(canvas);
        }
        onLayout(contentRect.left,contentRect.top, contentRect.right, contentRect.bottom);
    }

    public void addView(ComponentGView view)
    {
        childViews.add(view);
    }

    public void requestLayout()
    {
       getParent().invalidate();
    }

    public void removeAllViews()
    {
        for (ComponentGView childView : childViews) {
            childView.onDestroy();
        }
        childViews.clear();
    }

    @Override
    public void onDestroy() {
        removeAllViews();
        super.onDestroy();
    }
}
