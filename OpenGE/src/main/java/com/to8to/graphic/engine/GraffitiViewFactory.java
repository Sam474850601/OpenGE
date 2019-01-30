package com.to8to.graphic.engine;

/**
 * Created by same.li on 2018/5/7.
 */

public class GraffitiViewFactory {

    public static GView createGraphical(Class<? extends GView> classType)
    {
        try {
            return classType.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
