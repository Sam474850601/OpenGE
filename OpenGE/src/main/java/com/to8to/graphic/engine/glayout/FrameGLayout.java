package com.to8to.graphic.engine.glayout;


import com.to8to.graphic.engine.ComponentGView;
import com.to8to.graphic.engine.GViewGroup;

import java.util.LinkedList;

/**
 * Created by same.li on 2018/5/10.
 * 帧布局
 */

public class FrameGLayout extends GViewGroup {

    @Override
    protected void onLayout(int left, int top, int right, int bottom) {
        LinkedList<ComponentGView> childViews = getChildViews();
        for (ComponentGView childView : childViews) {
            childView.setWith(getWith());
            childView.setHeight(getHeight());
            childView.onMeasure();
            childView.layout(left, top, right, bottom);
        }
    }


}