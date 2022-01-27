package com.easyfoodvone.app_ui.textview_orderdetail

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import com.easyfoodvone.app_ui.R


class TextViewOrderDetailPrice : View {

    /**
     * ENUM ORDER HERE MUST MATCH THE ORDER IN attrs.xml
     */
    private enum class PriceDisplay {
        EACH,
        TOTAL
    }

    private lateinit var textPaintBlackRegular: TextPaint
    private lateinit var priceDisplay: PriceDisplay
    private var calculatedPricePositions: Array<TextViewOrderDetailRows.PositionedContent>? = null

    constructor(context: Context) : super(context) {
        initWithSomeDefault()
    }

    constructor(context: Context,
                attrs: AttributeSet?) : super(context, attrs) {
        attrs?.let { initWithAttributes(it) } ?: initWithSomeDefault()
    }

    constructor(context: Context,
                attrs: AttributeSet?,
                defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        attrs?.let { initWithAttributes(it) } ?: initWithSomeDefault()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context,
                attrs: AttributeSet?,
                defStyleAttr: Int,
                defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        attrs?.let { initWithAttributes(it) } ?: initWithSomeDefault()
    }

    fun setData(calculatedPricePositions: Array<TextViewOrderDetailRows.PositionedContent>) {
        this.calculatedPricePositions = calculatedPricePositions;

        invalidate()
    }

    private fun initWithSomeDefault() {
        init(
            resources.getDimension(R.dimen.phone_text_large_regular),
            resources.getColor(R.color.txt_red), // Make it obvious this is not normal
            ResourcesCompat.getFont(context, R.font.raleway_semi_bold)!!, PriceDisplay.TOTAL)
    }

    private fun initWithAttributes(attrs: AttributeSet) {
        context.obtainStyledAttributes(attrs, R.styleable.TextViewOrderDetailPrice).let { a: TypedArray ->
            try {
                val textStyle = TextViewOrderDetailUtil.loadStyleResource(a, R.styleable.TextViewOrderDetailPrice_priceStyle, context)
                val priceDisplay = PriceDisplay.values()[
                        a.getInt(R.styleable.TextViewOrderDetailPrice_priceDisplay, -1)]

                init(textStyle.textSize, textStyle.textColor, textStyle.typeface, priceDisplay)

            } finally {
                a.recycle()
            }
        }
    }

    private fun init(textSize: Float, textColor: Int, typeface: Typeface?, priceDisplay: PriceDisplay) {
        this.textPaintBlackRegular = TextPaint().apply {
            isAntiAlias = true
            color = textColor
            this.textSize = textSize
            this.typeface = typeface
            textAlign = Paint.Align.RIGHT
        }
        this.priceDisplay = priceDisplay
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        calculatedPricePositions?.forEach {
            val price: String = when (priceDisplay) {
                PriceDisplay.EACH -> it.item.eachPrice
                PriceDisplay.TOTAL -> it.item.totalPrice
            }
            canvas.drawText(price, width.toFloat(), it.yPosBaseline.toFloat(), textPaintBlackRegular)
        }
    }
}