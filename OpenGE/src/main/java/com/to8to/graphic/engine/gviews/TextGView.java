package com.to8to.graphic.engine.gviews;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;

import com.to8to.graphic.engine.ComponentGView;
import com.to8to.graphic.engine.SizeAdapter;


/**
 * Created by same.li on 2018/5/9.
 */

public class TextGView extends ComponentGView {
    public static final String TAG = TextGView.class.getSimpleName();

    String text = null;

    final Paint foucsOutterPoint = new Paint();

    final Paint foucsinnerPoint = new Paint();

    float textSize = 22;

    int textColor = Color.WHITE;

    int backgroundColor;

    public TextGView() {
        setFocus(true);
        backgroundColor = Color.parseColor("#FC3142");
        foucsOutterPoint.setAntiAlias(true);
        foucsOutterPoint.setStyle(Paint.Style.FILL);
        foucsOutterPoint.setColor(Color.parseColor("#FF929D"));

        foucsinnerPoint.setAntiAlias(true);
        foucsinnerPoint.setStyle(Paint.Style.FILL);
        foucsinnerPoint.setColor(Color.WHITE);
    }

    SizeAdapter sizeAdapter;

    final RectF textBackgroundRect = new RectF();

    boolean isFirstDraw = true;

    float textPaddingH = 0;
    float textPaddingV = 0;
    float textGap = 0;

    @Override
    public void onMeasure() {
        sizeAdapter = new SizeAdapter(getContext());
        textSize = sizeAdapter.getRealPx(24);
        textPaddingH = sizeAdapter.getRealPx(8);
        textPaddingV = sizeAdapter.getRealPx(6f);
        textGap = sizeAdapter.getRealPx(8f);

        paint.setAntiAlias(true);
        paint.setTextSize(textSize);
        paint.setColor(textColor);

        backgroundPaint.setColor(backgroundColor);
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setAlpha(180);


        if (isFirstDraw) {
            final int defaultWidth = (int) sizeAdapter.getRealPx(180f);
            float currentWidth = textPaddingH * 2 + textSize * text.length();
            contentWidth = (int) Math.min(defaultWidth, currentWidth);
        }
        contentHeight = calculateContentHeight();
        if (isFirstDraw) {
            startX = getWith() / 2 - contentWidth / 2;
            startY = getHeight() / 2 - contentHeight / 2;
            isFirstDraw = false;
        }

        textBackgroundRect.left = startX;
        textBackgroundRect.top = startY;
        textBackgroundRect.right = startX + contentWidth;
        textBackgroundRect.bottom = startY + contentHeight;

    }


    public void setBackgoundColor(int color) {
        this.backgroundColor = color;
    }

    private int calculateContentHeight() {
        int strLen = text.length();
        if (0 == strLen)
            return (int) _getSingleWardHeight();
        //每行显示的个数
        int enableShowTextSingleLineNumber = (int) ((contentWidth - textPaddingH * 2) / textSize);
        if (0 == enableShowTextSingleLineNumber)
            return (int) _getSingleWardHeight();
        //行数
        int lines = strLen / enableShowTextSingleLineNumber;
        //如果有余数，那么代表多一行
        if (strLen % enableShowTextSingleLineNumber > 0) {
            lines++;
        }
        //加多一行4Px调试显示高度
        return (int) (textPaddingV * 2 + lines * textSize + (lines - 1) * (textGap)) + (int) (sizeAdapter.getRealPx(4f));
    }


    //获取单行文字的时候高度
    private float _getSingleWardHeight() {
        return textPaddingV * 2 + (int) (sizeAdapter.getRealPx(4f)) + textSize;
    }


    Paint backgroundPaint = new Paint();


    float tClickX, tClicky;


    int touchstatus = TOUCHSTATUS_BODY;
    final static int TOUCHSTATUS_BODY = 0;
    final static int TOUCHSTATUS_POINT_LEFTTOP = 1;
    final static int TOUCHSTATUS_POINT_RIGHTTOP = 2;
    final static int TOUCHSTATUS_POINT_LEFTBOTTOM = 3;
    final static int TOUCHSTATUS_POINT_RIGHTBOTTOM = 4;

    @Override
    protected boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                final float x = tClickX = event.getX();
                final float y = tClicky = event.getY();
                Log.e(TAG, "ACTION_DOWN");
                setFocus(true);
                tClickX = x;
                tClicky = y;
                _updateStatus(x, y);
                return true;
            }
            case MotionEvent.ACTION_MOVE: {

                final float x = event.getX();
                final float y = event.getY();
                float moveX = (x - tClickX);
                float moveY = (y - tClicky);

                Log.e(TAG, "ACTION_MOVE");
                switch (touchstatus) {
                    case TOUCHSTATUS_BODY: {
                        Log.e(TAG, "TOUCHSTATUS_BODY");

                        //平移
                        startX += moveX;
                        startY += moveY;
                        endX += moveX;
                        endY += moveY;
                        tClickX = x;
                        tClicky = y;
                        return true;
                    }
                    case TOUCHSTATUS_POINT_LEFTBOTTOM:

                    case TOUCHSTATUS_POINT_LEFTTOP: {
                        Log.e(TAG, "TOUCHSTATUS_POINT_LEFTTOP");
                        int temp = contentWidth;
                        temp -= moveX;
                        //如果单行小于一行字，那么不处理和刷新
                        if (temp < textPaddingH * 2 + textSize)
                            return false;

                        //如果单行小于一列字，那么
                        final int width = getWith();
                        //不允许超过屏幕那么大
                        if (contentWidth > width)
                            contentWidth = width;
                        contentWidth = temp;
                        startX = x;
                        tClickX = x;
                    }
                    break;
                    case TOUCHSTATUS_POINT_RIGHTBOTTOM:
                    case TOUCHSTATUS_POINT_RIGHTTOP: {

                        int temp = contentWidth;
                        temp += moveX;
                        //如果单行小于一行字，那么不处理和不刷新
                        int singleWardWidth = (int) (textPaddingH * 2 + textSize);
                        if (temp < singleWardWidth)
                            return false;
                        contentWidth = temp;
                        //如果单行小于一列字，那么
                        final int width = getWith();
                        //不允许超过屏幕那么大
                        if (contentWidth > width)
                            contentWidth = width;
                        endX = x;
                        tClickX = x;
                    }
                }

            }
        }
        return true;
    }


    //设置点击的容错戳为20
    final static int LEN_TOLERANCE = 25;

    //更新当前点下时候点中的部分状态
    private void _updateStatus(float x, float y) {
        if (_isContainPoint(_getLeftBottomPoint(), x, y)) {
            touchstatus = TOUCHSTATUS_POINT_LEFTBOTTOM;
        } else if (_isContainPoint(_getLeftTopPoint(), x, y)) {
            touchstatus = TOUCHSTATUS_POINT_LEFTTOP;
        } else if (_isContainPoint(_getRightBottomPoint(), x, y)) {
            touchstatus = TOUCHSTATUS_POINT_RIGHTBOTTOM;
        } else if (_isContainPoint(_getRightTopPoint(), x, y)) {
            touchstatus = TOUCHSTATUS_POINT_RIGHTTOP;
        } else {
            touchstatus = TOUCHSTATUS_BODY;
        }
    }

    @Override
    protected boolean isContained(float x, float y) {
        //判断是否点击到四个点
        if (_isContainPoint(_getLeftBottomPoint(), x, y)) {
            return true;
        } else if (_isContainPoint(_getLeftTopPoint(), x, y)) {
            return true;
        } else if (_isContainPoint(_getRightBottomPoint(), x, y)) {
            return true;
        } else if (_isContainPoint(_getRightBottomPoint(), x, y)) {
            return true;
        } else if (_isContainPoint(_getRightTopPoint(), x, y)) {
            return true;
        }

        //判断有没有点击到本身
        return textBackgroundRect.contains(x, y);
    }

    private boolean _isContainPoint(PointF point, float x, float y) {
        return new RectF(point.x - LEN_TOLERANCE, point.y - LEN_TOLERANCE,
                point.x + LEN_TOLERANCE, point.y + LEN_TOLERANCE).contains(x, y);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.e(TAG, "startx=" + startX);
        if (TextUtils.isEmpty(text)) {
            text = " ";
        }

        canvas.drawRoundRect(textBackgroundRect, sizeAdapter.getRealPx(5),
                sizeAdapter.getRealPx(5), backgroundPaint);

        int enableShowTextSingleLineNumber = (int) ((contentWidth - textPaddingH * 2) / textSize);
        float x = startX + textPaddingH;
        int strLen = text.length();
        if (0 == strLen || 0 == enableShowTextSingleLineNumber)
            enableShowTextSingleLineNumber = 1;
        float fistLineY = startY + textSize + textPaddingV;

        if (strLen > enableShowTextSingleLineNumber) {
            int lines = strLen / enableShowTextSingleLineNumber;
            for (int i = 0; i < lines; i++) {
                String value = text.substring(i * enableShowTextSingleLineNumber, (i + 1) * enableShowTextSingleLineNumber);
                if (0 != i) {

                    float v = fistLineY + i * (textSize + textGap);
                    if (v - textSize <= textBackgroundRect.bottom) {
                        canvas.drawText(value, x, v,
                                paint);
                    }


                } else {
                    if (fistLineY - textSize <= textBackgroundRect.bottom) {
                        canvas.drawText(value, x, fistLineY, paint);
                    }

                }

            }
            int remain = strLen % enableShowTextSingleLineNumber;
            ///如果不整除
            if (remain > 0) {
                String value = text.substring(lines * enableShowTextSingleLineNumber,
                        lines * enableShowTextSingleLineNumber + remain);
                float currentY = fistLineY + lines * (textSize + textGap);
                if (currentY - textSize <= textBackgroundRect.bottom) {
                    canvas.drawText(value, x, currentY,
                            paint);
                }

            }
        } else {

            if (fistLineY - textSize <= textBackgroundRect.bottom) {
                canvas.drawText(text, startX + textPaddingH,
                        fistLineY,
                        paint);
            }

        }


        if (isFocus) {
            _drawFocusPoint(_getLeftBottomPoint(), canvas);
            _drawFocusPoint(_getLeftTopPoint(), canvas);
            _drawFocusPoint(_getRightBottomPoint(), canvas);
            _drawFocusPoint(_getRightTopPoint(), canvas);
        }

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        if (hasDraw) {
            getParent().invalidate();
        }
    }

    final static int LEN_FOCUS_OUTTER = 8;

    final static int LEN_FOCUS_INNER = 6;

    private void _drawFocusPoint(PointF pointF, Canvas canvas) {
        canvas.drawCircle(pointF.x, pointF.y, LEN_FOCUS_OUTTER, foucsOutterPoint);
        canvas.drawCircle(pointF.x, pointF.y, LEN_FOCUS_INNER, foucsinnerPoint);
    }


    private PointF _getLeftTopPoint() {
        return new PointF(textBackgroundRect.left, textBackgroundRect.top);
    }


    private PointF _getRightTopPoint() {
        return new PointF(textBackgroundRect.right, textBackgroundRect.top);
    }


    private PointF _getLeftBottomPoint() {
        return new PointF(textBackgroundRect.left, textBackgroundRect.bottom);
    }

    private PointF _getRightBottomPoint() {
        return new PointF(textBackgroundRect.right, textBackgroundRect.bottom);
    }


    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }


}
