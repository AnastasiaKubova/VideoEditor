package com.example.videoeditor.ui.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.videoeditor.R
import kotlin.math.abs


class ClipVideoSeekbar(context: Context, private val attrs: AttributeSet?): View(context, attrs), View.OnTouchListener {

    private var mBitmaps: ArrayList<Bitmap>? = null
    private var colorSeekbars: Int
    private var rectLeft: RectF? = null
    private var rectRight: RectF? = null
    private var seekbarWidth: Float = 30f
    private var minSeekbarWayWidth: Float = 150f

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ClipVideoSeekbar, 0, 0)
        try {
            colorSeekbars = a.getColor(R.styleable.ClipVideoSeekbar_seekbarColor, Color.WHITE)
        } finally {
            a.recycle()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val paint = Paint()
        if (mBitmaps.isNullOrEmpty() || canvas == null) {
            return
        }
        val frameWidth = this.width / mBitmaps!!.size
        var nextBitmapPositionX = 0F
        for (bitmap in mBitmaps!!) {
            val resizeBitmap = Bitmap.createScaledBitmap(bitmap,  frameWidth, this.height, false)
            canvas.drawBitmap(resizeBitmap, nextBitmapPositionX, 0F, paint)
            nextBitmapPositionX += frameWidth
        }
        drawSeekbars(canvas, width.toFloat(), height.toFloat())
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when(event?.action) {
            MotionEvent.ACTION_MOVE -> {
                if (rectLeft == null || rectRight == null) {
                    return true
                }
                if (event.y >= height) {
                    return true
                }
                if (event.x > 0 && event.x < rectLeft!!.right + seekbarWidth && (rectRight!!.left - event.x) >= minSeekbarWayWidth) {
                    rectLeft?.right = event.x
                    invalidate()
                } else if (event.x > this.width - seekbarWidth - rectRight!!.left && event.x < this.width && abs(rectLeft!!.right - event.x) >= minSeekbarWayWidth) {
                    rectRight?.left = event.x
                    invalidate()
                }
            }
        }
        return true
    }

    fun setFrames(bitmaps: ArrayList<Bitmap>) {
        mBitmaps = bitmaps
        invalidate()
    }

    @SuppressLint("ResourceAsColor")
    private fun drawSeekbars(canvas: Canvas?, w: Float, h: Float) {
        if (canvas == null) {
            return
        }

        // Init rects.
        if (rectRight == null) {
            rectRight = RectF()
            rectRight?.top = h
            rectRight?.bottom = 0f
            rectRight?.left = w - seekbarWidth
            rectRight?.right = w
        }
        if (rectLeft == null) {
            rectLeft = RectF()
            rectLeft?.top = h
            rectLeft?.bottom = 0f
            rectLeft?.right = seekbarWidth
            rectLeft?.left = 0f
        }

        /* Prepare paint for draw template. */
        val paint = Paint()
        paint.color = R.color.colorPrimaryTransparent
        canvas.drawRect(rectRight!!, paint)
        canvas.drawRect(rectLeft!!, paint)
    }
}