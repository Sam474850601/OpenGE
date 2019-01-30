package com.to8to.graphic.engine.gviews;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.to8to.graphic.engine.Const;
import com.to8to.graphic.engine.GView;
import com.to8to.graphic.engine.ViewUtil;


/**
 * Created by same.li on 2018/5/9.
 * 画笔
 */

public class PenGView extends GView {


    Paint mArrowTopRedPointPaint = new Paint();

    Paint mArrowTopWhitePointPaint = new Paint();
    final static int LEN_REDCIRCLE = 15;

    final static int LEN_WHITECIRCLE = 12;


    @Override
    public void onCreate(Intent data) {
        int size = data.getIntExtra(Const.param.paintSize, Const.value.Int.paintSize);
        int color = data.getIntExtra(Const.param.color, Color.RED);
        paint.setColor(color);
        paint.setStrokeWidth(size);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);


        mArrowTopRedPointPaint.setAntiAlias(true);
        mArrowTopRedPointPaint.setStyle(Paint.Style.FILL);
        mArrowTopRedPointPaint.setColor(Color.parseColor("#FF929D"));

        mArrowTopWhitePointPaint.setAntiAlias(true);
        mArrowTopWhitePointPaint.setStyle(Paint.Style.FILL);
        mArrowTopWhitePointPaint.setColor(Color.WHITE);


    }


    @Override
    protected void onDraw(Canvas canvas) {
        if(path.isEmpty())
            path.moveTo(startX, startY);

        path.lineTo(endX, endY);
        canvas.drawPath(path,paint);


        if (isFocus) {
            //画圈圈
            canvas.drawCircle(endX, endY, LEN_REDCIRCLE, mArrowTopRedPointPaint);
            canvas.drawCircle(endX,  endY, LEN_WHITECIRCLE, mArrowTopWhitePointPaint);

            canvas.drawCircle(startX, startY, LEN_REDCIRCLE , mArrowTopRedPointPaint);
            canvas.drawCircle(startX, startY, LEN_WHITECIRCLE, mArrowTopWhitePointPaint);
        }
    }

    @Override
    public boolean enableDraw() {
        return Math.abs(startX-endX)>0 || Math.abs(startY-endY)>0;
    }

    @Override
    protected boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                setFocus(true);
             return true;

        }

        return super.onTouchEvent(event);
    }

    @Override
    protected boolean isContained(float x, float y) {
        if( ViewUtil.isContained(path, (int) x, (int) y))
            return true;
        //Log.e("PenGView", "isContained:"+contained);
        if(getStartPointRectF().contains(x, y))
            return true;

        if(getEndPointRectF().contains(x, y))
            return true;

        //可能是条直线分25个等分撒点。点击中就认为，点击中了。
        PathMeasure pathMeasure = new PathMeasure(path, false);
        final float length = pathMeasure.getLength();
        final int avrag = (int) (length/25);
        for(int i = avrag; i < length; i += avrag)
        {
            final float[] point = new float[2];
            pathMeasure.getPosTan(i, point, null);
            if(getPointRectF(point[0],point[1]).contains(x, y))
                return true;
        }
        return false;
    }

    //设置点击的容错戳
    final static int LEN_TOLERANCE = 35;


    public RectF getStartPointRectF() {
        return    new RectF(startX-LEN_TOLERANCE, startY-LEN_TOLERANCE,
                startX+LEN_TOLERANCE, startY+LEN_TOLERANCE);
    }

    public RectF getEndPointRectF() {
        return new RectF(endX - LEN_TOLERANCE, endY - LEN_TOLERANCE,
                endX +LEN_TOLERANCE, endY + LEN_TOLERANCE);
    }

    public RectF getPointRectF(float x, float y)
    {

        return   new RectF(x-LEN_TOLERANCE, y-LEN_TOLERANCE,
                x+LEN_TOLERANCE, y+LEN_TOLERANCE);
    }

}
