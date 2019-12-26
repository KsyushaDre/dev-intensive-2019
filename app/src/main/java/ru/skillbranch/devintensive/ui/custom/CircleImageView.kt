package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.annotation.Dimension.DP
import androidx.annotation.Px
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.toRectF
import ru.skillbranch.devintensive.App
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.extensions.dpToPx
import ru.skillbranch.devintensive.utils.Utils

class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_BORDER_WIDTH = 2
        private const val DEFAULT_BORDER_COLOR = Color.WHITE
        private const val DEFAULT_SIZE = 40
        private var initials: String? = null
        fun setInitials(init: String?) {
            initials = if (init != null) init else null
        }

        private var bgColor: Int? = null

        fun setBgColor(initColor: Int) {
            bgColor = initColor
        }
    }

    @Px
    var borderWidth: Float = context.dpToPx(DEFAULT_BORDER_WIDTH)
    @ColorInt
    private var borderColor: Int = Color.WHITE


    private val avatarPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val initialsPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val viewRect = Rect()
    private val borderRect = Rect()

    init {
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)

            borderWidth = ta.getDimension(
                R.styleable.CircleImageView_cv_borderWidth,
                context.dpToPx(DEFAULT_BORDER_WIDTH)
            )

            borderColor =
                ta.getColor(R.styleable.CircleImageView_cv_borderColor, DEFAULT_BORDER_COLOR)
            ta.recycle()
        }

        scaleType = ScaleType.CENTER_CROP
        setup()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val initSize = resolveDefaultSize(widthMeasureSpec)
        setMeasuredDimension(initSize, initSize)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        if (w == 0) return
        with(viewRect) {
            left = 0
            top = 0
            right = w
            bottom = h
        }

        prepareShader(w, h)
    }

    override fun onDraw(canvas: Canvas) {
        if (drawable != null && initials == null) {
            drawAvatar(canvas)
        } else {
            drawInitials(canvas)
        }

        val half = (borderWidth / 2).toInt()
        borderRect.set(viewRect)
        borderRect.inset(half, half)
        canvas.drawOval(borderRect.toRectF(), borderPaint)
    }

    @Dimension(unit = DP)
    fun getBorderWidth(): Int = Utils.convertPxToDp(context, borderWidth.toInt())

    @Dimension(unit = DP)
    fun setBorderWidth(dp: Int) {
        borderWidth = Utils.convertDpToPx(context, dp)
        this.invalidate()
    }

    @ColorInt
    fun getBorderColor(): Int = borderColor

    fun setBorderColor(hex: String) {
        borderColor = Color.parseColor(hex)
        this.invalidate()
    }

    fun setBorderColor(@ColorRes colorId: Int) {
        borderColor = ContextCompat.getColor(App.applicationContext(), colorId)
        this.invalidate()
    }

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)
        if (initials == null) prepareShader(width, height)
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        if (initials == null) prepareShader(width, height)
    }

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        if (initials == null) prepareShader(width, height)
    }

    private fun setup() {
        with(borderPaint) {
            style = Paint.Style.STROKE
            strokeWidth = borderWidth
            color = borderColor
        }
    }

    private fun prepareShader(w: Int, h: Int) {
        if (w == 0 || drawable == null) return
        val srcBm = drawable.toBitmap(w, h, Bitmap.Config.ARGB_8888)
        avatarPaint.shader = BitmapShader(srcBm, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
    }

    private fun resolveDefaultSize(spec: Int): Int {
        return when (MeasureSpec.getMode(spec)) {
            MeasureSpec.UNSPECIFIED -> context.dpToPx(DEFAULT_SIZE).toInt()
            MeasureSpec.AT_MOST -> MeasureSpec.getSize(spec)
            MeasureSpec.EXACTLY -> MeasureSpec.getSize(spec)
            else -> MeasureSpec.getSize(spec)
        }
    }

    private fun drawAvatar(canvas: Canvas) {
        canvas.drawOval(viewRect.toRectF(), avatarPaint)
    }

    private fun drawInitials(canvas: Canvas) {
        if (bgColor != null) {
            initialsPaint.color = bgColor as Int
        }
        canvas.drawOval(viewRect.toRectF(), initialsPaint)
        with(initialsPaint) {
            color = Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = height * 0.33f
        }
        val offsetY = (initialsPaint.descent() + initialsPaint.ascent()) / 2
        val initialsText: String = initials ?: "??"

        canvas.drawText(
            initialsText,
            viewRect.exactCenterX(),
            viewRect.exactCenterY() - offsetY,
            initialsPaint
        )
    }
}