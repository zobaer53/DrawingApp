package com.pijung.kidsdrawingapp

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import java.util.*
import androidx.core.graphics.createBitmap

class DrawingView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private var mDrawPath: CustomPath? = null
    private var mCanvasBitmap: Bitmap? = null
    private var mDrawPaint: Paint? = null
    private var mCanvasPaint: Paint? = null
    private var mBrushSize: Float = 0.toFloat()
    private var color = Color.BLACK
    private var canvas: Canvas? = null
    private val mPaths = ArrayList<CustomPath>()
    private val mUndoPaths = ArrayList<CustomPath>()

    init {
        setUpDrawing()
    }

    private fun setUpDrawing() {
        mDrawPaint = Paint()
        mDrawPath = CustomPath(color, mBrushSize)
        mDrawPaint?.color = color
        mDrawPaint?.style = Paint.Style.STROKE
        mDrawPaint?.strokeJoin = Paint.Join.ROUND
        mDrawPaint?.strokeCap = Paint.Cap.ROUND
        mDrawPaint?.strokeWidth = mBrushSize
        mCanvasPaint = Paint(Paint.DITHER_FLAG)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w > 0 && h > 0) {
            mCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            canvas = Canvas(mCanvasBitmap!!)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        // Draw the background bitmap first
        mCanvasBitmap?.let { bitmap ->
            canvas.drawBitmap(bitmap, 0f, 0f, mCanvasPaint)
        }

        // Draw all the paths
        for (path in mPaths) {
            mDrawPaint?.strokeWidth = path.brushThickness
            mDrawPaint?.color = path.color
            canvas.drawPath(path, mDrawPaint!!)
        }

        // Draw the current path being drawn
        if (!mDrawPath!!.isEmpty) {
            mDrawPaint?.strokeWidth = mDrawPath!!.brushThickness
            mDrawPaint?.color = mDrawPath!!.color
            canvas.drawPath(mDrawPath!!, mDrawPaint!!)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x
        val touchY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mDrawPath?.color = color
                mDrawPath?.brushThickness = mBrushSize
                mDrawPath?.reset()
                mDrawPath?.moveTo(touchX, touchY)
            }
            MotionEvent.ACTION_MOVE -> {
                mDrawPath?.lineTo(touchX, touchY)
            }
            MotionEvent.ACTION_UP -> {
                mPaths.add(mDrawPath!!)
                mDrawPath = CustomPath(color, mBrushSize)
            }
            else -> return false
        }
        invalidate()
        return true
    }

    fun setSizeForBrush(newSize: Float) {
        mBrushSize = newSize
        mDrawPaint?.strokeWidth = newSize
    }

    fun setColor(newColor: String) {
        color = Color.parseColor(newColor)
        mDrawPaint?.color = color
    }

    fun onClickUndo() {
        if (mPaths.size > 0) {
            mUndoPaths.add(mPaths.removeAt(mPaths.size - 1))
            invalidate()
        }
    }

    fun onSavedFile() {
        mPaths.clear()
        mUndoPaths.clear()
        invalidate()
    }

    fun loadBitmap(bitmap: Bitmap) {
        // Clear existing paths
        mPaths.clear()
        mUndoPaths.clear()
        
        // Calculate scaling to fit the bitmap to the view
        val scaleX = width.toFloat() / bitmap.width
        val scaleY = height.toFloat() / bitmap.height
        val scale = minOf(scaleX, scaleY)
        
        // Calculate the position to center the bitmap
        val scaledWidth = bitmap.width * scale
        val scaledHeight = bitmap.height * scale
        val left = (width - scaledWidth) / 2
        val top = (height - scaledHeight) / 2
        
        // Create a new bitmap for the canvas with the same dimensions as the view
        mCanvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        canvas = Canvas(mCanvasBitmap!!)
        
        // Draw white background first
        canvas?.drawColor(Color.WHITE)
        
        // Draw the bitmap centered on the canvas
        canvas?.drawBitmap(bitmap, left, top, null)
        
        // Invalidate the view to redraw
        invalidate()
    }

    internal inner class CustomPath(var color: Int, var brushThickness: Float) : Path()
} 