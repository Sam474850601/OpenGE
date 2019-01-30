package com.to8to.graffitiimageviewsample

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.jrummyapps.android.colorpicker.ColorPickerDialog
import com.jrummyapps.android.colorpicker.ColorPickerDialogListener
import kotlinx.android.synthetic.main.activity_main.*
import android.view.inputmethod.InputMethodManager
import com.to8to.graphic.engine.glayout.FrameGLayout
import com.to8to.graphic.engine.gviews.ImageCGView
import com.to8to.graphic.engine.gviews.TextGView


class MainActivity : AppCompatActivity() {


    var textView: com.to8to.graphic.engine.gviews.TextGView? = null


    var imm: InputMethodManager? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //在画板画一个背景图
        imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        _addImageBackgound();
        addTextGView("你好，傻吊")

    }

    private fun _addImageBackgound() {
        FrameGLayout().let { layout ->
            ImageCGView().run {
                setContentSize(com.to8to.graphic.engine.ComponentGView.MATCH_PARENT,
                        com.to8to.graphic.engine.gviews.ImageCGView.MATCH_PARENT)
                setImageDrawableResource(gbroad.context, R.mipmap.test)
                layout.addView(this)
                gbroad
            }.addComponentGView(layout)
        }
    }

    var mColor: Int = Color.RED


    fun onDrawModeClick(view: View) {
        gbroad.apply {
            clearGraphicalViewFocus()
            setMode(com.to8to.graphic.engine.GraffitiBoardView.MODE_DRAW)
            setGraffitiViewInitData(com.to8to.graphic.engine.GViewIntentBuilder().apply {
                setColor(mColor)
                when (view.id) {
                    R.id.btn_arrow ->
                        gbroad.setCurrentDrawingGraphical(com.to8to.graphic.engine.gviews.ArrowGView::class.java)
                    R.id.btn_oval ->
                        gbroad.setCurrentDrawingGraphical(com.to8to.graphic.engine.gviews.OvalGView::class.java)
                    R.id.btn_pen -> {
                        gbroad.setCurrentDrawingGraphical(com.to8to.graphic.engine.gviews.PenGView::class.java)
                    }

                }
            }.build())
        }


    }

    fun onChoiceModeClick(view: View) {
        val id = view.id
        Log.e("id", "id=$id");
        gbroad.setMode(com.to8to.graphic.engine.GraffitiBoardView.MODE_SELECTED)
    }


    val DIALGE_ID = 0


    fun onColorSelectedClick(view: View) {

        val id = view.id
        Log.e("id", "id=$id");

        val pickerDialogListener = object : ColorPickerDialogListener {
            override fun onColorSelected(dialogId: Int, @ColorInt color: Int) {
                if (DIALGE_ID == dialogId) {
                    mColor = color
                    gbroad.run {
                        clearGraphicalViewFocus()
                        setGraffitiViewInitData(com.to8to.graphic.engine.GViewIntentBuilder().setColor(color).build())
                        setMode(com.to8to.graphic.engine.GraffitiBoardView.MODE_DRAW)
                    }

                }

            }

            override fun onDialogDismissed(dialogId: Int) {

            }
        }

        ColorPickerDialog.newBuilder()
                .apply {
                    setColor(Color.BLACK)
                    setDialogTitle(R.string.select_color)
                    //设置dialog标题
                    setDialogType(ColorPickerDialog.TYPE_CUSTOM)
                    //设置为自定义模式
                    setShowAlphaSlider(true)
                    //设置有透明度模式，默认没有透明度
                    setDialogId(DIALGE_ID)
                    //设置Id,回调时传回用于判断
                    setAllowPresets(false)
                    //不显示预知模式
                }.create().
                apply {
                    setColorPickerDialogListener(pickerDialogListener)
                }.show(fragmentManager, "color-picker-dialog")

    }

    fun onDeleteClick(view: View) {
        val id = view.id
        Log.e("id", "id=$id")
        gbroad.removeCurrentFocusGView()
    }


    fun onClearClick(view: View) {
        val id = view.id
        Log.e("id", "id=$id")
        gbroad.removeAllGViews()
        _addImageBackgound()
    }

    fun onInputTextClick(view: View) {
        val id = view.id
        Log.e("id", "id=$id")

        Log.e("MainActivity", "onInputTextClick")
        val text = et_input.text.toString()

        //如果回收掉了，那么重新搞一个进去
        if (textView!!.isRecycled) {
            addTextGView(et_input.text.toString())
        } else {
            textView?.text = text
        }

        imm?.hideSoftInputFromWindow(et_input.windowToken, 0)
    }


    fun onCutClick(view: View) {
        //提前把焦点移除了。

        gbroad.let {
            gb->
            gb.clearGraphicalViewFocus()
            with(ImageDialog())
            {
                setImageBitMap(gb.boardBitmap)
                show(supportFragmentManager, "test")
            }
        }

    }

    fun onAddTextGViewClick(view: View) {
        addTextGView(et_input.text.toString())
    }


    fun addTextGView(input: String) {
        gbroad.addComponentGView(com.to8to.graphic.engine.gviews.TextGView().apply {
            textView = this
            text = input
            setOnFocusClickListener { view ->
                textView = view as TextGView
                et_input.requestFocus()
                imm?.showSoftInput(et_input, 0)
            }
        })
    }

}
