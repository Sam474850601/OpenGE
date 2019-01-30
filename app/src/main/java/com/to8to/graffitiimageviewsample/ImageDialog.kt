package com.to8to.graffitiimageviewsample

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView

/**
 * Created by same.li on 2018/5/14.
 */
class ImageDialog : DialogFragment() {

    private var bitmap: Bitmap? = null

    fun setImageBitMap(bitMap: Bitmap?) {
        this.bitmap = bitMap
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        val view = inflater.inflate(R.layout.dialog_image, container, false)
        view.findViewById<ImageView>(R.id.iv_image).apply {
            setImageBitmap(bitmap)
        }
        return view
    }
}