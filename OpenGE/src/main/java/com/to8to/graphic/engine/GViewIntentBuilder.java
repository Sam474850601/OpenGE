package com.to8to.graphic.engine;

import android.content.Intent;

/**
 * Created by same.li on 2018/5/9.
 */

public class GViewIntentBuilder {

    private static final Intent intent = new Intent();


    public GViewIntentBuilder setColor(int color) {
        intent.putExtra(Const.param.color, color);
        return this;
    }

    public GViewIntentBuilder setPaintSzie(int size) {
        intent.putExtra(Const.param.paintSize, size);
        return this;
    }


    public GViewIntentBuilder setBackgoundColor(int color) {
        intent.putExtra(Const.param.backgroundColor, color);
        return this;
    }


    public GViewIntentBuilder setText(String text) {
        intent.putExtra(Const.param.text, text);
        return this;
    }


    public GViewIntentBuilder setTextColor(String text) {
        intent.putExtra(Const.param.textColor, text);
        return this;
    }

    public GViewIntentBuilder setTextSize(int textSize) {
        intent.putExtra(Const.param.textSize, textSize);
        return this;
    }

    public Intent build() {
        return intent;
    }


}
