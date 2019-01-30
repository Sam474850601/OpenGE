package com.to8to.graphic.engine.gviews;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import com.to8to.graphic.engine.Const;
import com.to8to.graphic.engine.GView;


/**
 * Created by same.li on 2018/5/8.
 * 椭圆涂鸦
 */

public class OvalGView extends GView {


    Paint foucsOutterPoint = new Paint();

    Paint foucsinnerPoint = new Paint();

    final static int LEN_FOCUS_OUTTER = 15;

    final static int LEN_FOCUS_INNER = 12;

    @Override
    public void onCreate(Intent data) {
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(data.getIntExtra(Const.param.paintSize, Const.value.Int.paintSize));
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        int color = data.getIntExtra(Const.param.color, Color.RED);
        paint.setColor(color);
        foucsOutterPoint.setAntiAlias(true);
        foucsOutterPoint.setStyle(Paint.Style.FILL);
        foucsOutterPoint.setColor(Color.parseColor("#FF929D"));

        foucsinnerPoint.setAntiAlias(true);
        foucsinnerPoint.setStyle(Paint.Style.FILL);
        foucsinnerPoint.setColor(Color.WHITE);

    }



    @Override
    protected void onDraw(Canvas canvas) {
        RectF rectF = _getOvalRect();
        canvas.drawOval(rectF, paint);

        if (isFocus) {

            _drawFocusPoint(_getLefMiddlePoint(), canvas);

            _drawFocusPoint(_getBottomLeftPoint(), canvas);
            _drawFocusPoint(_getBottomMiddlePoint(), canvas);

            _drawFocusPoint(_getRightBottomPoint(), canvas);
            _drawFocusPoint(_getRightMiddlePoint(), canvas);


            _drawFocusPoint(_getTopMiddlePoint(), canvas);
            _drawFocusPoint(_getTopRightPoint(), canvas);
            _drawFocusPoint(_getTopLeftPoint(), canvas);

        }
    }



    private void _drawFocusPoint(PointF pointF, Canvas canvas) {
        canvas.drawCircle(pointF.x, pointF.y, LEN_FOCUS_OUTTER, foucsOutterPoint);
        canvas.drawCircle(pointF.x, pointF.y, LEN_FOCUS_INNER, foucsinnerPoint);
    }


    private PointF _getTopLeftPoint() {
        RectF rectF = _getOvalRect();
        float y = rectF.top;
        float x = rectF.left;
        return new PointF(x, y);
    }

    private PointF _getLefMiddlePoint() {
        RectF rectF = _getOvalRect();
        float y = rectF.top + rectF.height() / 2;
        float x = rectF.left;
        return new PointF(x, y);
    }


    private PointF _getBottomLeftPoint() {
        RectF rectF = _getOvalRect();
        float y = rectF.bottom;
        float x = rectF.left;
        return new PointF(x, y);
    }

    private PointF _getBottomMiddlePoint() {
        RectF rectF = _getOvalRect();
        float y = rectF.bottom;
        float x = rectF.left + rectF.width() / 2;
        return new PointF(x, y);
    }

    private PointF _getRightBottomPoint() {
        RectF rectF = _getOvalRect();
        float y = rectF.bottom;
        float x = rectF.right;
        return new PointF(x, y);
    }


    private PointF _getRightMiddlePoint() {
        RectF rectF = _getOvalRect();
        float y = rectF.top + rectF.height() / 2;
        float x = rectF.right;
        return new PointF(x, y);
    }

    private PointF _getTopRightPoint() {
        RectF rectF = _getOvalRect();
        float y = rectF.top;
        float x = rectF.right;
        return new PointF(x, y);
    }

    private PointF _getTopMiddlePoint() {
        RectF rectF = _getOvalRect();
        float y = rectF.top;
        float x = rectF.left + rectF.width() / 2;
        return new PointF(x, y);
    }


    private RectF _getOvalRect() {
        float left = Math.min(startX, endX);
        float top = Math.min(startY, endY);
        float right = Math.max(startX, endX);
        float bottom = Math.max(startY, endY);
        RectF rectF = new RectF(left, top, right, bottom);
        return rectF;
    }

    public static final String TAG = OvalGView.class.getSimpleName();


    float tClickX, tClicky;


    int touchstatus = TOUCHSTATUS_BODY;
    final static int TOUCHSTATUS_BODY = 0;
    final static int TOUCHSTATUS_POINT_TOPLEFT = 1;
    final static int TOUCHSTATUS_POINT_TOPMIDDLE = 2;
    final static int TOUCHSTATUS_POINT_BOTTOMLEFT = 3;
    final static int TOUCHSTATUS_POINT_BOTTOMMIDDLE = 4;
    final static int TOUCHSTATUS_POINT_RIGHTBOTTOM = 5;
    final static int TOUCHSTATUS_POINT_RIGHTMIDDLE = 6;
    final static int TOUCHSTATUS_POINT_TOPRIGHT = 7;
    final static int TOUCHSTATUS_POINT_LEFTMIDDLE = 8;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        float x = event.getX();
        float y = event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                // Log.e(TAG, "ACTION_DOWN");
                setFocus(true);
                tClickX = x;
                tClicky = y;
                _updateStatus(x, y);
                return true;
            }
            case MotionEvent.ACTION_MOVE: {
                float moveX = (x - tClickX);
                float moveY = (y - tClicky);

                switch (touchstatus) {
                    case TOUCHSTATUS_BODY: {
                        Log.e("test", "OvalGView");
                        //平移
                        startX += moveX;
                        startY += moveY;
                        endX += moveX;
                        endY += moveY;
                        tClickX = x;
                        tClicky = y;
                        return true;
                    }
                    case TOUCHSTATUS_POINT_LEFTMIDDLE: {
                        //不允许翻过去
                        if(startX < endX)
                        {
                            if(startX + moveX > endX-LEN_FOCUS_OUTTER*2)
                                return false;
                            startX += moveX;
                            tClickX = x;
                        }
                        else
                        {
                            if(endX + moveX > startX-LEN_FOCUS_OUTTER*2)
                                return false;
                            endX += moveX;
                            tClickX = x;
                        }

                        return true;
                    }

                    case TOUCHSTATUS_POINT_RIGHTMIDDLE: {

                        if(startX < endX)
                        {
                            //不允许翻过去
                            if(endX + moveX < startX+LEN_FOCUS_OUTTER*2)
                                return false;
                            endX += moveX;
                            tClickX = x;

                        }
                        else
                        {
                            //不允许翻过去
                            if(startX + moveX < endX+LEN_FOCUS_OUTTER*2)
                                return false;
                            startX += moveX;
                            tClickX = x;
                        }

                        return true;
                    }

                    case TOUCHSTATUS_POINT_TOPMIDDLE: {
                        if(startY>endY)
                        {
                            if(endY + moveY > startY-LEN_FOCUS_OUTTER*2)
                                return false;
                            endY += moveY;
                            tClicky = y;
                        }
                        else
                        {
                            if(startY + moveY > endY-LEN_FOCUS_OUTTER*2)
                                return false;
                            startY += moveY;
                            tClicky = y;
                        }

                        return true;
                    }
                    case TOUCHSTATUS_POINT_BOTTOMMIDDLE: {

                        if(startY>endY)
                        {
                            if(startY + moveY < endY+LEN_FOCUS_OUTTER*2)
                                return false;

                            startY += moveY;
                            tClicky = y;
                        }
                       else
                        {
                            if(endY + moveY < startY+LEN_FOCUS_OUTTER*2)
                                return false;
                            endY += moveY;
                            tClicky = y;
                        }

                        return true;
                    }

                    case TOUCHSTATUS_POINT_TOPLEFT: {



                       if(startX < endX && startY > endY)
                       {
                           if(startX + moveX > endX-LEN_FOCUS_OUTTER*2)
                               return false;
                           if(endY + moveY > startY-LEN_FOCUS_OUTTER*2)
                               return false;
                           startX += moveX;
                           endY += moveY;
                           tClickX = x;
                           tClicky = y;
                       }
                       else if(startX < endX && startY < endY)
                       {
                           if(startX + moveX > endX-LEN_FOCUS_OUTTER*2)
                               return false;
                           if(startY + moveY > endY-LEN_FOCUS_OUTTER*2)
                               return false;
                           startX += moveX;
                           startY += moveY;
                           tClickX = x;
                           tClicky = y;
                       }

                       else if(startX > endX && startY > endY)
                        {
                            if(endX + moveX > startX-LEN_FOCUS_OUTTER*2)
                                return false;
                            if(endY + moveY > startY-LEN_FOCUS_OUTTER*2)
                                return false;
                            endX += moveX;
                            endY += moveY;
                            tClickX = x;
                            tClicky = y;
                        }
                       else if(startX > endX && startY < endY)
                       {
                           if(endX + moveX > startX-LEN_FOCUS_OUTTER*2)
                               return false;
                           if(startY + moveY > endY-LEN_FOCUS_OUTTER*2)
                               return false;
                           endX += moveX;
                           startY += moveY;
                           tClickX = x;
                           tClicky = y;
                       }

                        return true;
                    }
                    case TOUCHSTATUS_POINT_TOPRIGHT: {

                        if(startX < endX && startY > endY)
                        {
                            if(endY + moveY > startY-LEN_FOCUS_OUTTER*2)
                                return false;
                            if(endX + moveX < startX+LEN_FOCUS_OUTTER*2)
                                return false;

                            endX += moveX;
                            endY += moveY;
                            tClickX = x;
                            tClicky = y;
                        }
                        else if(startX < endX && startY < endY)
                        {
                            if(startY + moveY > endY-LEN_FOCUS_OUTTER*2)
                                return false;
                            if(endX + moveX < startX+LEN_FOCUS_OUTTER*2)
                                return false;
                            endX += moveX;
                            startY += moveY;
                            tClickX = x;
                            tClicky = y;
                        }

                        else if(startX > endX && startY > endY)
                        {
                            if(endY + moveY > startY-LEN_FOCUS_OUTTER*2)
                                return false;
                            if(startX + moveX < endX+LEN_FOCUS_OUTTER*2)
                                return false;
                            startX += moveX;
                            endY += moveY;
                            tClickX = x;
                            tClicky = y;
                        }
                        else if(startX > endX && startY < endY)
                        {
                            if(startY + moveY > endY-LEN_FOCUS_OUTTER*2)
                                return false;
                            if(startX + moveX < endX+LEN_FOCUS_OUTTER*2)
                                return false;
                            startX += moveX;
                            startY += moveY;
                            tClickX = x;
                            tClicky = y;
                        }


                        return true;
                    }

                    case TOUCHSTATUS_POINT_RIGHTBOTTOM: {

                        if(startX<endX && startY>endY)
                        {
                            if(endX + moveX < startX+LEN_FOCUS_OUTTER*2)
                                return false;
                            if(startY + moveY < endY+LEN_FOCUS_OUTTER*2)
                                return false;
                            endX += moveX;
                            startY += moveY;
                            tClickX = x;
                            tClicky = y;
                        }
                        else if(startX<endX && startY<endY)
                        {
                            if(endX + moveX < startX+LEN_FOCUS_OUTTER*2)
                                return false;
                            if(endY + moveY < startY+LEN_FOCUS_OUTTER*2)
                                return false;
                            endX += moveX;
                            endY += moveY;
                            tClickX = x;
                            tClicky = y;

                        }
                        else if(startX>endX && startY>endY)
                        {
                            if(startX + moveX < endX+LEN_FOCUS_OUTTER*2)
                                return false;
                            if(startY + moveY < endY+LEN_FOCUS_OUTTER*2)
                                return false;
                            startX += moveX;
                            startY += moveY;
                            tClickX = x;
                            tClicky = y;

                        }
                        else if(startX>endX && startY<endY)
                        {
                            if(startX + moveX < endX+LEN_FOCUS_OUTTER*2)
                                return false;
                            if(endY + moveY < startY+LEN_FOCUS_OUTTER*2)
                                return false;
                            startX += moveX;
                            endY += moveY;
                            tClickX = x;
                            tClicky = y;

                        }


                        return true;
                    }
                    case TOUCHSTATUS_POINT_BOTTOMLEFT: {
                        if(startX<endX && startY>endY)
                        {
                            if(startY + moveY < endY+LEN_FOCUS_OUTTER*2)
                                return false;
                            if(startX + moveX > endX-LEN_FOCUS_OUTTER*2)
                                return false;
                            startX += moveX;
                            startY += moveY;
                            tClickX = x;
                            tClicky = y;
                        }
                        else if(startX<endX && startY<endY)
                        {
                            if(endY + moveY < startY+LEN_FOCUS_OUTTER*2)
                                return false;
                            if(startX + moveX > endX-LEN_FOCUS_OUTTER*2)
                                return false;
                            startX += moveX;
                            endY += moveY;
                            tClickX = x;
                            tClicky = y;
                        }
                        else if(startX>endX && startY>endY)
                        {
                            if(startY + moveY < endY+LEN_FOCUS_OUTTER*2)
                                return false;
                            if(endX + moveX > startX-LEN_FOCUS_OUTTER*2)
                                return false;
                            endX += moveX;
                            startY += moveY;
                            tClickX = x;
                            tClicky = y;
                        }
                        else if(startX>endX && startY<endY)
                        {
                            if(endY + moveY < startY+LEN_FOCUS_OUTTER*2)
                                return false;
                            if(endX + moveX > startX-LEN_FOCUS_OUTTER*2)
                                return false;
                            endX += moveX;
                            endY += moveY;
                            tClickX = x;
                            tClicky = y;
                        }
                        return true;
                    }
                }
            }
            case MotionEvent.ACTION_UP:
                break;
        }

        return super.onTouchEvent(event);
    }



    //设置点击的容错戳为25
    final static int LEN_TOLERANCE = 35;

    //更新当前点下时候点中的部分状态
    private void _updateStatus(float x, float y) {
        if (_isContainPoint(_getLefMiddlePoint(), x, y)) {
            touchstatus = TOUCHSTATUS_POINT_LEFTMIDDLE;
        } else if (_isContainPoint(_getBottomLeftPoint(), x, y)) {
            touchstatus = TOUCHSTATUS_POINT_BOTTOMLEFT;
        } else if (_isContainPoint(_getBottomMiddlePoint(), x, y)) {
            touchstatus = TOUCHSTATUS_POINT_BOTTOMMIDDLE;
        } else if (_isContainPoint(_getRightBottomPoint(), x, y)) {
            touchstatus = TOUCHSTATUS_POINT_RIGHTBOTTOM;
        } else if (_isContainPoint(_getRightMiddlePoint(), x, y)) {
            touchstatus = TOUCHSTATUS_POINT_RIGHTMIDDLE;
        } else if (_isContainPoint(_getTopMiddlePoint(), x, y)) {
            touchstatus = TOUCHSTATUS_POINT_TOPMIDDLE;
        } else if (_isContainPoint(_getTopRightPoint(), x, y)) {
            touchstatus = TOUCHSTATUS_POINT_TOPRIGHT;
        } else if (_isContainPoint(_getTopLeftPoint(), x, y)) {
            touchstatus = TOUCHSTATUS_POINT_TOPLEFT;
        } else {
            touchstatus = TOUCHSTATUS_BODY;
        }
    }


    private boolean _isContainPoint(PointF point, float x, float y) {
        return new RectF(point.x - LEN_TOLERANCE, point.y - LEN_TOLERANCE,
                point.x + LEN_TOLERANCE, point.y + LEN_TOLERANCE).contains(x, y);
    }

    @Override
    public boolean isContained(float x, float y) {

        if (_isContainPoint(_getLefMiddlePoint(), x, y)) {
            return true;
        } else if (_isContainPoint(_getBottomLeftPoint(), x, y)) {
            return true;
        } else if (_isContainPoint(_getBottomMiddlePoint(), x, y)) {
            return true;
        } else if (_isContainPoint(_getRightBottomPoint(), x, y)) {
            return true;
        } else if (_isContainPoint(_getRightMiddlePoint(), x, y)) {
            return true;
        } else if (_isContainPoint(_getTopMiddlePoint(), x, y)) {
            return true;
        } else if (_isContainPoint(_getTopRightPoint(), x, y)) {
            return true;
        } else if (_isContainPoint(_getTopLeftPoint(), x, y)) {
            return true;
        }
        return _getOvalRect().contains(x, y);
    }


    @Override
    public boolean enableDraw() {
        return Math.abs(startX-endX)>10 || Math.abs(startY-endY)>10 ;
    }
}
