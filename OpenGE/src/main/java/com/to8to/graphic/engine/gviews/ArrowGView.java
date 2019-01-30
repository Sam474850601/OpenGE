package com.to8to.graphic.engine.gviews;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.to8to.graphic.engine.Const;
import com.to8to.graphic.engine.GView;
import com.to8to.graphic.engine.ViewUtil;

/**
 * Created by same.li on 2018/5/7.
 * 箭头涂鸦
 */

public class ArrowGView extends GView {
    public final static String TAG = ArrowGView.class.getSimpleName();

    public final static int SIZE_DEFAULT = 0;


    public final static int SIZE_MIDDEL = 5;


    public final static int SIZE_LARGE= 10;

    public final static int SIZE_ARROW = 50;

    Paint mArrowPaint = new Paint();

    Paint mArrowTopRedPointPaint = new Paint();

    Paint mArrowTopWhitePointPaint = new Paint();

    Paint mLinePaint = new Paint();





    public static final int STATUS_DRAW = 0;

    public static final int STATUS_MOVING = 1;





    @Override
    public void onCreate(Intent data) {
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(15);
        paint.setColor(data.getIntExtra(Const.param.color, Color.RED));

        mArrowPaint.setStyle(Paint.Style.FILL);
        mArrowPaint.setStrokeWidth(paint.getStrokeWidth());
        mArrowPaint.setColor(paint.getColor());

        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(15);
        mLinePaint.setColor(paint.getColor());

        mArrowTopRedPointPaint.setAntiAlias(true);
        mArrowTopRedPointPaint.setStyle(Paint.Style.FILL);
        mArrowTopRedPointPaint.setColor(Color.parseColor("#FF929D"));

        mArrowTopWhitePointPaint.setAntiAlias(true);
        mArrowTopWhitePointPaint.setStyle(Paint.Style.FILL);
        mArrowTopWhitePointPaint.setColor(Color.WHITE);
    }


    @Override
    public void onDraw(Canvas canvas) {
        //如果不满足绘制的条件，那么不绘制

        //清除以前的记忆路径
        path.reset();

        Path linePath = new Path();
        linePath.moveTo(startX, startY);
        linePath.lineTo(endX, endY);

        path.moveTo(startX, startY);



        PathMeasure pathMeasure = new PathMeasure(linePath, false);
        float[] objectiveCd = new float[2];
        pathMeasure.getPosTan(pathMeasure.getLength()-15, objectiveCd, null);


        PointF[] arrowPoinfs = getPoints(30, 45, objectiveCd[0], startX, objectiveCd[1], startY);

        Path triangle = new Path();
        triangle.moveTo(objectiveCd[0], objectiveCd[1]);
        triangle.lineTo(arrowPoinfs[0].x, arrowPoinfs[0].y);
        triangle.lineTo(arrowPoinfs[1].x, arrowPoinfs[1].y);
        triangle.close();


        path.lineTo(arrowPoinfs[0].x, arrowPoinfs[0].y);
        path.lineTo(endX, endY);
        path.lineTo(arrowPoinfs[1].x, arrowPoinfs[1].y);
        path.lineTo(startX, startY);
        path.close();



        pathMeasure.getPosTan(pathMeasure.getLength()-30, objectiveCd, null);

        linePath.reset();
        linePath.moveTo(startX, startY);
        linePath.lineTo( objectiveCd[0], objectiveCd[1]);




        canvas.drawPath(linePath, mLinePaint);


        canvas.drawPath(triangle,paint);
        canvas.drawPath(triangle,mArrowPaint);




        if (isFocus) {
            //画圈圈
            canvas.drawCircle(endX, endY, LEN_REDCIRCLE, mArrowTopRedPointPaint);
            canvas.drawCircle(endX,  endY, LEN_WHITECIRCLE, mArrowTopWhitePointPaint);

            canvas.drawCircle(startX, startY, LEN_REDCIRCLE , mArrowTopRedPointPaint);
            canvas.drawCircle(startX, startY, LEN_WHITECIRCLE, mArrowTopWhitePointPaint);
        }
    }



    final static int LEN_REDCIRCLE = 15;

    final static int LEN_WHITECIRCLE = 12;


    private PointF[] getPoints(float L, float H, float ex, float sx,float ey, float sy)
    {
        int x3 = 0;
        int y3 = 0;
        int x4 = 0;
        int y4 = 0;

        double awrad = Math.atan(L / H); // 箭头角度
        double arraow_len = Math.sqrt(L * L + H * H); // 箭头的长度
        double[] arrXY_1 = rotateVec(ex - sx, ey - sy, awrad, true, arraow_len);
        double[] arrXY_2 = rotateVec(ex - sx, ey - sy, -awrad, true, arraow_len);
        double x_3 = ex - arrXY_1[0]; // (x3,y3)是第一端点
        double y_3 = ey - arrXY_1[1];
        double x_4 = ex - arrXY_2[0]; // (x4,y4)是第二端点
        double y_4 = ey - arrXY_2[1];
        Double X3 = new Double(x_3);
        x3 = X3.intValue();
        Double Y3 = new Double(y_3);
        y3 = Y3.intValue();
        Double X4 = new Double(x_4);
        x4 = X4.intValue();
        Double Y4 = new Double(y_4);
        y4 = Y4.intValue();
        PointF lp = new PointF();
        lp.x = x3;
        lp.y = y3;

        PointF rp = new PointF();
        rp.x = x4;
        rp.y = y4;
        return new PointF[]{lp, rp};
    }


    // 计算
    public double[] rotateVec(float px, float py, double ang, boolean isChLen, double newLen)
    {
        double mathstr[] = new double[2];
        // 矢量旋转函数，参数含义分别是x分量、y分量、旋转角、是否改变长度、新长度
        double vx = px * Math.cos(ang) - py * Math.sin(ang);
        double vy = px * Math.sin(ang) + py * Math.cos(ang);
        if (isChLen) {
            double d = Math.sqrt(vx * vx + vy * vy);
            vx = vx / d * newLen;
            vy = vy / d * newLen;
            mathstr[0] = vx;
            mathstr[1] = vy;
        }
        return mathstr;
    }


    //允许箭头最小长度
    static  final float LEN_ARROW_MIN = 30;

    @Override
    public boolean enableDraw()
    {
      return Math.abs(startX- endX)>LEN_ARROW_MIN || Math.abs(startY- endY)>LEN_ARROW_MIN;
    }





    @Override
    public boolean isContained(float x, float y) {

        //是否点到视图上
        boolean containedViewPath = ViewUtil.isContained(path, (int) x, (int) y);
       // Log.e(TAG, "contained:"+containedViewPath);

        //是否点在起点上
        RectF startPointRectF =  getStartPointRectF();

        //是否点在终点上
        RectF endPointRectF = getEndPointRectF();

        boolean containedStartPoint = startPointRectF.contains(x, y);
        //Log.e(TAG, "containedStartPoint:"+containedStartPoint);

        boolean containedEndPoint = endPointRectF.contains(x, y);

       // Log.e(TAG, "containedEndPoint:"+containedEndPoint);

        return containedViewPath || containedStartPoint||containedEndPoint;
    }




    //标识当前被点击的位置是开始点击位置类型
    static final int TOUCHSTATUS_STARTPOINT = 1;
    //标识当前被点击的位置是末尾点击位置类型
    static final int TOUCHSTATUS_ENDPOINT = 2;

    static final int TOUCHSTATUS_BODY = 0;

    //标记当前点点击的类型是在哪里
    int touchstatus = TOUCHSTATUS_BODY;


    float tClickX = 0;
    float tClicky = 0;




    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                //Log.e(TAG, "ACTION_DOWN:");
                setFocus(true);
                tClickX  = event.getX();
                tClicky = event.getY();

                _updateTouchStatus(tClickX, tClicky);
                return true;
            }
            case MotionEvent.ACTION_MOVE: {

                final float x = event.getX();
                final float y = event.getY();

                switch (touchstatus)
                {
                    case TOUCHSTATUS_BODY:
                    {
                        //Log.e(TAG, "TOUCHSTATUS_BODY");
                        float moveX = ( x - tClickX);
                        float moveY = ( y - tClicky);

                        //平移
                        startX += moveX;
                        startY += moveY;
                        endX += moveX;
                        endY += moveY;
                        tClickX = x;
                        tClicky = y;
                        return true;
                    }
                    case TOUCHSTATUS_STARTPOINT:
                    {
                       // Log.e(TAG, "TOUCHSTATUS_STARTPOINT");

                        if(Math.abs(x -endX) < LEN_ARROW_MIN)
                            return false;
                        if(Math.abs(y -endY) < LEN_ARROW_MIN)
                            return false;

                        startX = x;
                        startY = y;
                        return true;
                    }
                    case TOUCHSTATUS_ENDPOINT:
                    {
                       // Log.e(TAG, "TOUCHSTATUS_ENDPOINT");

                        if(Math.abs(x -startX) < LEN_ARROW_MIN)
                            return false;
                        if(Math.abs(y -startY) < LEN_ARROW_MIN)
                            return false;
                        endX = x;
                        endY = y;
                        return true;
                    }
                }

            }
            break;
        }
        return  super.onTouchEvent(event);
    }


    //更新当前touch状态
    private void _updateTouchStatus(float x, float y)
    {

        RectF endPointRectF = getEndPointRectF();
        //如果点击的是顶部
        if(endPointRectF.contains(x,y)){
            touchstatus = TOUCHSTATUS_ENDPOINT;
        }
        RectF startPointRectF = getStartPointRectF();
        //如果是点击开始点
        if(startPointRectF.contains(x, y)) {
            touchstatus = TOUCHSTATUS_STARTPOINT;
        }

        //如果是点击开始点
        if(ViewUtil.isContained(path, (int)x,(int) y)) {
            touchstatus = TOUCHSTATUS_BODY;
        }
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
}
