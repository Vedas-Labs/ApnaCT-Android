package com.vedas.apna.Helper

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList


@SuppressLint("ClickableViewAccessibility")
abstract class SwiperHelper(context: Context, recyclerView: RecyclerView, buttonWidth: Int) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
    private var buttonWidth = 0
    private var recyclerView: RecyclerView? = null
    private lateinit var buttonList: List<MyButton>
    private var gestureDetector: GestureDetector? = null
    private var swipePosition = -1
    private var swipeThreshold = 0.5f
    private var buttonBuffer: MutableMap<Int, MutableList<MyButton>> = mutableMapOf()

    //private var removeQueue: Queue<Int>? = null
    private var removeQueue :LinkedList<Int> = LinkedList() /*= object : LinkedList<Int>() {
        override fun add(element: Int): Boolean {
            if (contains(element)) return false
            return super.add(element)
        }
    }*/
    private val gestureListener: SimpleOnGestureListener = object : SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            for (button in buttonList) {
                if (button.onClick(e.x, e.y)) {
                    break
                }
            }
            return true
        }
    }

    private val onTouchListener = OnTouchListener { _, motionEvent ->
        if (swipePosition < 0) {
            return@OnTouchListener false
        }
        val point = Point(motionEvent.rawX.toInt(), motionEvent.rawY.toInt())
        val swipeViewHolder = recyclerView.findViewHolderForAdapterPosition(swipePosition)
        if (swipeViewHolder != null) {
            val swipedItem = swipeViewHolder.itemView
            val rect = Rect()
            swipedItem.getGlobalVisibleRect(rect)
            if (motionEvent.action == MotionEvent.ACTION_DOWN || motionEvent.action == MotionEvent.ACTION_UP || motionEvent.action == MotionEvent.ACTION_MOVE) {
                if (rect.top < point.y && rect.bottom > point.y) {
                    gestureDetector!!.onTouchEvent(motionEvent)
                } else {
                    removeQueue.add(swipePosition)
                    swipePosition = -1
                    recoverSwipedItem()
                }
            }
            return@OnTouchListener false
        }
        false
    }

    init {
        this.recyclerView = recyclerView
        this.buttonList = ArrayList()
        gestureDetector = GestureDetector(context, gestureListener)
        this.recyclerView!!.setOnTouchListener(onTouchListener)
        buttonBuffer = HashMap()
        this.buttonWidth = buttonWidth

        attachSwipe()
        removeQueue = object : LinkedList<Int>() {
            override fun add(element: Int): Boolean {
                if (contains(element)) return false
                return super.add(element)
            }
        }
    }

    private fun attachSwipe() {
        val itemTouchHelper = ItemTouchHelper(this)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    @Synchronized
    private fun recoverSwipedItem() {
        while (!removeQueue.isEmpty()) {
            val pos :Int = removeQueue.poll()
            if (pos > -1) recyclerView!!.adapter!!.notifyItemChanged(pos)
        }
    }

    interface MyButtonClickListener {
        fun onClick(pos: Int)
    }

    class MyButton(context: Context, imageResId: Int, color: Int, listener: MyButtonClickListener) {
        private var imageResId = 0
        private var color: Int = 0
        private var pos: Int = 0
        private var clickRegion: RectF? = null
        private var listener: MyButtonClickListener? = null
        private var context: Context? = null
        private var resources: Resources? = null

        init {
            this.context = context
            this.imageResId = imageResId
            this.color = color
            this.listener = listener
            this.resources = context.resources
        }

        fun onClick(x: Float, y: Float): Boolean {
            if (clickRegion != null && clickRegion!!.contains(x, y)) {
                listener!!.onClick(pos)
                return true
            }
            return false
        }

        fun onDraw(c: Canvas, rectF: RectF, pos: Int) {
            val p = Paint()
            p.color = color
            c.drawRect(rectF, p)
            p.color = Color.WHITE
            val r = Rect()
            val cHeight = rectF.height()
            val cWidth = rectF.width()
            p.textAlign = Paint.Align.LEFT
            val d = ContextCompat.getDrawable(context!!, imageResId)
            val bitmap: Bitmap = drawableToBitmap(d!!)
            val bw = (bitmap.width / 2).toFloat()
            val bh = (bitmap.height / 2).toFloat()
            c.drawBitmap(bitmap, (rectF.left + rectF.right) / 2 - bw, (rectF.top + rectF.bottom) / 2 - bh, p)
            clickRegion = rectF
            this.pos = pos
        }

        private fun drawableToBitmap(d: Drawable): Bitmap {
            if (d is BitmapDrawable) {
                return Bitmap.createScaledBitmap(d.bitmap, 80, 80, true)
            }
            val bitmap = Bitmap.createBitmap(d.intrinsicWidth, d.intrinsicHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            d.setBounds(0, 0, canvas.width, canvas.height)
            d.draw(canvas)
            return bitmap
        }
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val pos = viewHolder.adapterPosition
        if (swipePosition != pos) removeQueue.add(swipePosition)
        swipePosition = pos
        buttonList = if (buttonBuffer.containsKey(swipePosition)) buttonBuffer[swipePosition]!! else ArrayList()
        buttonBuffer.clear()
        swipeThreshold = 0.5f * buttonList.size * buttonWidth
        recoverSwipedItem()
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return swipeThreshold
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return 0.1f * defaultValue
    }

    override fun getSwipeVelocityThreshold(defaultValue: Float): Float {
        return 5.0f * defaultValue
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        val pos = viewHolder.adapterPosition
        var translationX = dX
        val itemView = viewHolder.itemView
        if (pos < 0) {
            swipePosition = pos
            return
        }
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (dX < 0) {
                var buffer: MutableList<MyButton> = ArrayList()
                if (!buttonBuffer.containsKey(pos)) {
                    instantiateMyButton(viewHolder, buffer)
                    buttonBuffer[pos] = buffer
                } else {
                    buffer = buttonBuffer[pos]!!
                }
                translationX = dX * buffer.size * buttonWidth / itemView.width
                drawButton(c, itemView, buffer, pos, translationX)
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, translationX, dY, actionState, isCurrentlyActive)
    }

    private fun drawButton(c: Canvas, itemView: View, buffer: MutableList<MyButton>, pos: Int, translationX: Float) {
        var right = itemView.right.toFloat()
        val dButtonWidth = -1 * translationX / buffer.size
        for (button in buffer) {
            val left = right - dButtonWidth
            button.onDraw(c, RectF(left, itemView.top.toFloat(), right, itemView.bottom.toFloat()), pos)
            right = left
        }
    }

    abstract fun instantiateMyButton(viewHolder: RecyclerView.ViewHolder?, buffer: MutableList<MyButton>)
}