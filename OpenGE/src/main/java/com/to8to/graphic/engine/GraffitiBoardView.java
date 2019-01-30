package com.to8to.graphic.engine;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.to8to.graphic.engine.gviews.PenGView;

import java.util.LinkedList;

/**
 * Created by same.li on 2018/5/7.
 * 涂鸦画板
 */

public class GraffitiBoardView extends View {

    private final LinkedList<GView> graphicalViews = new LinkedList<>();
    private Bitmap currentBitmap;

    private final static String LOG_TAG = GraffitiBoardView.class.getSimpleName();

    public GraffitiBoardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //set a pen  Graphical as a  default  Graphical
        setCurrentDrawingGraphical(PenGView.class);

        //set next action is drawing
        setMode(GraffitiBoardView.MODE_DRAW);
    }

    public static final int MODE_DRAW = 0x02;

    public static final int MODE_SELECTED = 0x04;

    public int mode;

    public void clearGraphicalViewFocus() {
        if (null != currentDrawingGraphical && currentDrawingGraphical.isFocus) {
            currentDrawingGraphical.setFocus(false);
            invalidate();
        }
    }

    public void clearAllGraphicalViewsFocus() {
        for (GView graphicalView : graphicalViews) {
            graphicalView.setFocus(false);
        }
        invalidate();
    }

    public interface OnGViewTouchCreateListener
    {

        void onCreated(GView view);

    }

    private  OnGViewTouchCreateListener onGViewCreateListener;


    public void setOnGViewCreateListener(OnGViewTouchCreateListener onGViewCreateListener) {
        this.onGViewCreateListener = onGViewCreateListener;
    }



    //清除所有涂鸦
    public void removeAllGViews() {
        for (GView graphicalView : graphicalViews) {
            graphicalView.onDestroy();
        }
        graphicalViews.clear();
        if (null != currentDrawingGraphical && currentDrawingGraphical.hasDraw) {
            currentDrawingGraphical.onDestroy();
            currentDrawingGraphical = null;
        }
        invalidate();
    }


    public void setMode(int mode) {
        this.mode = mode;
    }

    private int width, height;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

   private  Bitmap bitmap;

    private Paint paint = new Paint();

    @Override
    protected void onDraw(Canvas mcanvas) {
        //创建新的来保存图片,每一次绘制 不显式回收。
        bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        for (GView graphicalView : graphicalViews) {
            //避免刷新2次
            if (currentDrawingGraphical != graphicalView) {
                _initgraphicalView(graphicalView, canvas);
            }
        }
        _initgraphicalView(currentDrawingGraphical, canvas);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        mcanvas.drawBitmap(bitmap, 0, 0, paint);
    }

    private void _initgraphicalView(GView graphicalView, Canvas canvas) {
        if(null == graphicalView)
            return;
        graphicalView.setContext(getContext());
        graphicalView.setParent(this);
        graphicalView.setWith(width);
        graphicalView.setHeight(height);
        if (graphicalView instanceof ComponentGView) {
            ComponentGView componentGView = (ComponentGView) graphicalView;
            componentGView.onMeasure();
        }

        graphicalView.onDraw(canvas);
        graphicalView.hasDraw = true;

    }


    public Bitmap getBoardBitmap() {
        if(null != bitmap && bitmap.isRecycled())
            invalidate();
        return bitmap;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //  Log.e("onTouchEventS", "event");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return _actionDown(event);
            case MotionEvent.ACTION_MOVE:
                return _actionMove(event);
            case MotionEvent.ACTION_UP:
                return _actionUp(event);
        }
        return super.onTouchEvent(event);
    }

    private volatile GView currentDrawingGraphical;


    Class<? extends GView> gViewClass;

    public void setCurrentDrawingGraphical(Class<? extends GView> classType) {
        this.gViewClass = classType;
    }


    //绘制非屏幕touch类型涂鸦视图
    public void addComponentGView(ComponentGView view) {
        //标识是否已经添加过的view的位置

        //如果已经存在过,就不需要在添加了
        if(graphicalViews.contains(view))
        {
           removeCurrentFocusGView();
           view.setFocus(true);
           graphicalViews.remove(view);
           graphicalViews.addLast(view);
           invalidate();
           return;
        }

        if(null != currentDrawingGraphical)
            currentDrawingGraphical.isFocus = false;

        if (view.isMotionOperationView()) {
            currentDrawingGraphical = view;
            //如果是有焦点状态，那么设置当前为选中模式
            if (currentDrawingGraphical.isFocus)
                setMode(MODE_SELECTED);
        }
        this.graphicalViews.addLast(view);
        invalidate();
    }


    float sx, sy;

    private int mActivePointerId;

    private int pointerIndex;

    private Intent data;

    public void setGraffitiViewInitData(Intent data) {
        this.data = data;
    }

    private boolean _actionDown(MotionEvent event) {
        //  Log.e("onTouchEventS", "_actionDown");
        mActivePointerId = event.getPointerId(0);
        sx = event.getX();
        sy = event.getY();
        if (MODE_DRAW == mode) {
            //    Log.e("onTouchEventS", "MODE_DRAW");
            currentDrawingGraphical = GraffitiViewFactory.createGraphical(gViewClass);
            currentDrawingGraphical.setContext(getContext());
            currentDrawingGraphical.setStart(sx, sy);

            if (null == data)
                data = new Intent();

            currentDrawingGraphical.onCreate(data);
            return true;
        } else if (MODE_SELECTED == mode) {
            //   Log.e("onTouchEventS", "MODE_SELECTED");
//            //为了避免点击和别的视图重叠点击，优先选择已经选择的
//            if (null != currentDrawingGraphical && currentDrawingGraphical.isFocus &&
//                    currentDrawingGraphical.isContained(sx, sy)) {
//               // Log.e("onTouchEventS", "isContained");
//                return currentDrawingGraphical.onTouchEvent(event);
//            }

            //保存被点击到的视图
            GView gView = null;

            boolean resume = false;


           final  int size = graphicalViews.size();

            for (int i = size-1; i >=0 ; i--) {
                GView graphicalView = graphicalViews.get(i);
                    //如果点击到就分发事件
                if (graphicalView.isContained(sx, sy)) {
                    gView = graphicalView;
                    resume = graphicalView.onTouchEvent(event);
                    break;
                }
            }



            //如果有view点击到，那么把点击到的放到最顶部
            if (null != gView) {
                //把上一次其他选中的效果去掉。
                if (null != currentDrawingGraphical && currentDrawingGraphical != gView)
                    currentDrawingGraphical.setFocus(false);
                //告诉控件当前操作的视图是这个
                currentDrawingGraphical = gView;
                //将当前的视图移动到顶部
                graphicalViews.remove(gView);
                graphicalViews.addLast(gView);
                invalidate();
                return resume;
            }
        }
        return super.onTouchEvent(event);
    }

    private void _removeGView(GView view) {
        graphicalViews.remove(view);
        view.onDestroy();

    }


    //删除当前选择的涂鸦
    public void removeCurrentFocusGView() {
        if (null != currentDrawingGraphical && currentDrawingGraphical.isFocus) {
            _removeGView(currentDrawingGraphical);
            currentDrawingGraphical = null;
            invalidate();
        }
    }

    //获取当前被选中的视图
    public GView findCurrentFocusView() {
        if (null != currentDrawingGraphical && currentDrawingGraphical.isFocus)
            return currentDrawingGraphical;
        for (GView graphicalView : graphicalViews) {
            if (graphicalView.isFocus)
                return graphicalView;
        }
        return null;
    }


    private boolean _actionMove(MotionEvent event) {
//        pointerIndex = event.findPointerIndex(mActivePointerId);
//        if (pointerIndex < 0) {
//            Log.e(LOG_TAG, "Got ACTION_MOVE event but have an invalid active pointer id.");
//            return false;
//        }
        final float x = event.getX();
        final float y = event.getY();
        if (MODE_SELECTED == mode) {
            if (Math.abs(sx - x) > 5 && Math.abs(sy - y) > 5) {
                if (null != currentDrawingGraphical
                        ) {
                    if (currentDrawingGraphical.onTouchEvent(event)) {
                        //  Log.e("currentDrawingGraphical", "currentDrawingGraphical invalidate ");
                        invalidate();
                        return true;
                    }
                }
            }
            //需要跳出，免得setEnd导致出现uibug
            return false;
        }


        if (null != currentDrawingGraphical) {
            currentDrawingGraphical.setEnd(x, y);
            if (currentDrawingGraphical.enableDraw()) {
                invalidate();
                return true;
            }
        }

        return super.onTouchEvent(event);
    }


    private boolean _actionUp(MotionEvent event) {
        if (null != currentDrawingGraphical && MODE_SELECTED == mode) {
            if (sx == event.getX() && sy == event.getY()) {
                if (null != currentDrawingGraphical && currentDrawingGraphical.isFocus) {
                    GView.OnFocusClickListener onFocusClickListener = currentDrawingGraphical.onFocusClickListener;
                    if (null != onFocusClickListener)
                        onFocusClickListener.onFocusClick(currentDrawingGraphical);
                }
            }
            return currentDrawingGraphical.onTouchEvent(event);
        }

        if (MODE_DRAW == mode) {
            if (null != currentDrawingGraphical && currentDrawingGraphical.enableDraw()) {
                graphicalViews.add(currentDrawingGraphical);
                if(null != onGViewCreateListener)
                    onGViewCreateListener.onCreated(currentDrawingGraphical);
                return true;
            }
        }
        return super.onTouchEvent(event);
    }


}
