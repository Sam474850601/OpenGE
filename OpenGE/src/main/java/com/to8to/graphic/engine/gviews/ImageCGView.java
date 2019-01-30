package com.to8to.graphic.engine.gviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.to8to.graphic.engine.ComponentGView;


/**
 * Created by same.li on 2018/5/9.
 * 背景图片，填充整个屏幕
 */

public class ImageCGView extends ComponentGView {

    private Bitmap bitmap;

    @Override
    public boolean isMotionOperationView() {
        return false;
    }


    @Override
    public  void onMeasure() {
        int with = getWith();
        int height = getHeight();
        if(MATCH_PARENT == contentWidth)
        {
            contentWidth = with;
        }
        else
        {
            contentWidth =  bitmap.getWidth();
        }
        if(MATCH_PARENT == contentHeight)
        {
            contentHeight = height;
        }
        else
        {
            contentHeight = bitmap.getHeight();
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {

        int with = getWith();
        int height = getHeight();

        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        if(bitmapWidth>with && bitmapHeight>height)
        {
            Bitmap  resultBitmap = getZoomImage(bitmap, with, height);
            canvas.drawBitmap(resultBitmap,contentRect.left , contentRect.top, paint);
        }
        else  if(bitmapWidth>=with && bitmapHeight<=height)
        {

            int realHeight = (int) (with/(float)bitmapWidth*bitmapHeight);
            Bitmap  resultBitmap  = getZoomImage(bitmap, with, realHeight);
            canvas.drawBitmap(resultBitmap,contentRect.left , contentRect.top+height/2-realHeight/2, paint);

        }

        else  if(bitmapWidth<=with && bitmapHeight<=height)
        {
            canvas.drawBitmap(bitmap,contentRect.left+with/2-bitmapWidth/2, contentRect.top+height/2-bitmapHeight/2, paint);
        }
        else if(bitmapWidth<=with && bitmapHeight>=height)
        {
            int realWidth = (int) (height/(float)bitmapHeight*bitmapWidth);
            Bitmap  resultBitmap  =  getZoomImage(bitmap, realWidth, height);
            canvas.drawBitmap(resultBitmap,contentRect.left+with/2-realWidth/2,
                    contentRect.top+height/2-height/2, paint);
        }

    }



    /**
     * 图片的缩放方法
     *
     * @param orgBitmap ：源图片资源
     * @param newWidth  ：缩放后宽度
     * @param newHeight ：缩放后高度
     * @return
     */
    public  Bitmap getZoomImage(Bitmap orgBitmap, double newWidth, double newHeight) {
        if (null == orgBitmap) {
            return null;
        }
        if (orgBitmap.isRecycled()) {
            return null;
        }
        if (newWidth <= 0 || newHeight <= 0) {
            return null;
        }

        // 获取图片的宽和高
        float width = orgBitmap.getWidth();
        float height = orgBitmap.getHeight();
        // 创建操作图片的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(orgBitmap, 0, 0, (int) width, (int) height, matrix, true);
        return bitmap;
    }




    //必须是图片资源
    public void setImageDrawableResource(Context context, int resource)
    {
        bitmap = BitmapFactory.decodeResource( context.getResources(),resource );
    }


    public void setImageBitmap(Bitmap bitmap)
    {
        this.bitmap = bitmap;
    }




    @Override
    public void onDestroy() {
        if(null != bitmap && !bitmap.isRecycled())
        {
            bitmap.recycle();
        }
    }
}
