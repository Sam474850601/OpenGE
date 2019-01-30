package com.to8to.graphic.engine;

import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;

/**
 * Created by same.li on 2018/5/8.
 */

public final  class ViewUtil {

    public static boolean isContained(Path path, int x, int  y) {
        final   RectF bounds = new RectF();
        path.computeBounds(bounds, true);
        Region region = new Region();
        region.setPath(path, new Region((int) bounds.left, (int) bounds.top, (int) bounds.right, (int) bounds.bottom));
        return region.contains(x, y);
    }

}
