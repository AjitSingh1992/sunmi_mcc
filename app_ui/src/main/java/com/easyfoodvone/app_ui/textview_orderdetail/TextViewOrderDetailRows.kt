package com.easyfoodvone.app_ui.textview_orderdetail

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.text.*
import android.text.style.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import com.easyfoodvone.app_common.viewdata.DataRowOrderDetail
import com.easyfoodvone.app_ui.R
import java.lang.IllegalStateException
import kotlin.math.roundToInt


class TextViewOrderDetailRows : View {
    private lateinit var textSpanStyleQuantity: TextViewOrderDetailUtil.LoadedStyle
    private lateinit var textPaintBlackRegular: TextPaint
    private var quantityIndentPx: Int = -999
    private var levelIndexPx: Int = -999
    
    private val wrappedItemLineSpacingMultiplier = 0.8f

    private val lineSpacingLevel0 = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        28f,
        resources.displayMetrics).roundToInt()

    private val lineSpacingSubLevels = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        12f,
        resources.displayMetrics).roundToInt()

    private data class Content(val span: SpannableStringBuilder, val items: Array<IndexedContent>)
    private data class IndexedContent(val itemRow: DataRowOrderDetail.ItemRow, val indexInSpan: Int)

    private var content: Content? = null
    private var staticLayout: StaticLayout? = null
    private var calculatedPricePositions: Array<PositionedContent>? = null

    private var priceViewEach: TextViewOrderDetailPrice? = null
    private var priceViewTotal: TextViewOrderDetailPrice? = null

    init {
        if (isInEditMode) {
            setData(arrayOf(
                DataRowOrderDetail.ItemRow(0, "20", "Shaslick Special Medium", "", "£20.99"),
                DataRowOrderDetail.ItemRow( 1,"99", "Large Pizza", "£3.99", "£1500.00"),
                DataRowOrderDetail.ItemRow(2, "1", "Extra Long Hot Dog", "", "£2.99"),
                DataRowOrderDetail.ItemRow(0, "3", "Another Item", "", "£4.10")))
        }
    }

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

    fun setLinkedTotalViews(priceViewEach: TextViewOrderDetailPrice, priceViewTotal: TextViewOrderDetailPrice) {
        this.priceViewEach = priceViewEach
        this.priceViewTotal = priceViewTotal

        if (content != null) {
            throw IllegalStateException("Set these first, they don't need to change")
        }
    }

    fun setData(items: Array<DataRowOrderDetail.ItemRow>) {
        content = setupTextSpans(items)
        staticLayout = null
        calculatedPricePositions = null

        forceLayout()
        requestLayout()
    }

    private fun initWithSomeDefault() {
        init(TextViewOrderDetailUtil.LoadedStyle(
                resources.getDimension(R.dimen.phone_text_large_bold),
                resources.getColor(R.color.txt_red), // Make it obvious this is not normal
                ResourcesCompat.getFont(context, R.font.raleway_extra_bold)!!),
            TextViewOrderDetailUtil.LoadedStyle(
                resources.getDimension(R.dimen.phone_text_large_regular),
                resources.getColor(R.color.txt_red), // Make it obvious this is not normal
                ResourcesCompat.getFont(context, R.font.raleway_semi_bold)!!))
    }

    @SuppressLint("Recycle")
    private fun initWithAttributes(attrs: AttributeSet) {
        context.obtainStyledAttributes(attrs, R.styleable.TextViewOrderDetailRows).let { a: TypedArray ->
            try {
                val textStyleQuantity = TextViewOrderDetailUtil.loadStyleResource(a, R.styleable.TextViewOrderDetailRows_quantityStyle, context)
                val textStyleMain = TextViewOrderDetailUtil.loadStyleResource(a, R.styleable.TextViewOrderDetailRows_mainStyle, context)
                init(textStyleQuantity, textStyleMain)

            } finally {
                a.recycle()
            }
        }
    }

    private fun init(textStyleQuantity: TextViewOrderDetailUtil.LoadedStyle, textStyleMain: TextViewOrderDetailUtil.LoadedStyle) {
        textSpanStyleQuantity = textStyleQuantity
        textPaintBlackRegular = textStyleMain.let {
            TextPaint().apply {
                isAntiAlias = true
                color = it.textColor
                textSize = it.textSize
                typeface = it.typeface
            }
        }

        // Scale with textSize so it aligns the same on phone and tablet (and will scale with sp)
        quantityIndentPx = (2.49351f * textStyleQuantity.textSize).roundToInt()
        levelIndexPx = (2.44156 * textStyleQuantity.textSize).roundToInt()
    }

    private fun filterNewlines(str: String): String {
        return str.replace("\n", "")
    }

    private fun setupTextSpans(items: Array<DataRowOrderDetail.ItemRow>): Content {
        val span = SpannableStringBuilder()
        val itemsIndexed: Array<IndexedContent?> = arrayOfNulls(items.size)

        var isFirstItem = true
        for ((index, item) in items.withIndex()) {
            // Setup spacing after the first item
            if (isFirstItem) {
                isFirstItem = false

            } else {
                span.apply {
                    append("\n \n")
                    // Adjust the height of the " " paragraph
                    val spaceOnlyStart: Int = length - 2
                    val spaceOnlyEnd: Int = length - 1
                    val rowSpacing: Int = if (item.indentLevel == 0) lineSpacingLevel0 else lineSpacingSubLevels
                    setSpan(LineHeightCompatSpan(rowSpacing), spaceOnlyStart, spaceOnlyEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }

            // Add the content
            var startLine = -1
            span.apply {
                startLine = length
                itemsIndexed[index] = IndexedContent(item, length)
            }
            .apply {
                append("" + filterNewlines(item.quantity) + " x\t")
                setSpan(TextSpan(textSpanStyleQuantity), startLine, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                setSpan(TabStopSpan.Standard(quantityIndentPx), startLine, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            .apply {
                append(filterNewlines(item.itemName))
            }
            .apply {
                val extraForLevels = item.indentLevel * levelIndexPx
                setSpan(LeadingMarginSpan.Standard(0 + extraForLevels, quantityIndentPx + extraForLevels), startLine, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }

        @Suppress("UNCHECKED_CAST")
        return Content(span, itemsIndexed as Array<IndexedContent>)
    }

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width: Int = getDefaultSize(0, widthMeasureSpec)

        staticLayout = StaticLayout(
            content!!.span, textPaintBlackRegular, width, Layout.Alignment.ALIGN_NORMAL,
            wrappedItemLineSpacingMultiplier, 0f, true)

        // Add 3 to the height because some devices clip the bottom (below baseline) of text off...
        // I'm not sure why, this device did happen to have staticLayout.bottomPadding > 0, whereas
        // it is 0 on my unaffected devices. We shouldn't need to add bottom and top padding as
        // staticLayout constructor has includePad=true. Perhaps we can improve this later if needed.
        setMeasuredDimension(width, staticLayout!!.height + 3)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        calculatedPricePositions = calculatePricePositions(staticLayout!!, content!!.items)

        priceViewEach!!.setData(calculatedPricePositions!!)
        priceViewTotal!!.setData(calculatedPricePositions!!)
    }

    data class PositionedContent(val item: DataRowOrderDetail.ItemRow, val yPosBaseline: Int)

    private fun calculatePricePositions(staticLayout: StaticLayout, indexedContent: Array<IndexedContent>): Array<PositionedContent> {
        val calculated: Array<PositionedContent?> = arrayOfNulls<PositionedContent>(indexedContent.size)

        val nextItemIterator = indexedContent.withIndex().iterator()
        var nextItem: IndexedValue<IndexedContent>? = if (nextItemIterator.hasNext()) nextItemIterator.next() else null

        for (line in 0..staticLayout.lineCount - 1) {
            // Each "line" contains the original text split up with all text wrapping based on the supplied width.
            // A newline comes through at the end of each "line" which had it (a.k.a. not wrapping onto another line)
            val startPosInText = staticLayout.getLineStart(line)

            // We make the perfectly fine assumption that staticLayout.text is the same as content!!.span.toString(), which we indexed previously
            if (nextItem?.value?.indexInSpan ?: break == startPosInText) {
                val yPosBaseline = staticLayout.getLineBaseline(line)
                calculated[nextItem.index] = PositionedContent(nextItem.value.itemRow, yPosBaseline)

                //val endPosInText = staticLayout.getLineEnd(line)
                //Log.d("OrderDetail", "top=" + top + " str=\"" + staticLayout.text.substring(startPosInText, endPosInText) + "\" item=" + nextItem.value.itemRow.itemName)

                nextItem = if (nextItemIterator.hasNext()) nextItemIterator.next() else break
            }
        }

        @Suppress("UNCHECKED_CAST")
        return calculated as Array<PositionedContent>
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        staticLayout?.draw(canvas)
    }

    /**
     * Copied from LineHeightSpan.Standard as that is only available on new APIs
     */
    private class LineHeightCompatSpan(val mHeight: Int) : LineHeightSpan {
        override fun chooseHeight(text: CharSequence, start: Int, end: Int, spanstartv: Int,
                                  lineHeight: Int, fm: Paint.FontMetricsInt) {
            val originHeight: Int = fm.descent - fm.ascent
            // If original height is not positive, do nothing.
            if (originHeight <= 0) {
                return
            }
            val ratio: Float = mHeight * 1.0f / originHeight
            fm.descent = Math.round(fm.descent * ratio)
            fm.ascent = fm.descent - mHeight
        }
    }

    private class TextSpan(val style: TextViewOrderDetailUtil.LoadedStyle) : MetricAffectingSpan() {
        override fun updateDrawState(paint: TextPaint) {
            setupPaint(paint)
        }

        override fun updateMeasureState(paint: TextPaint) {
            setupPaint(paint)
        }

        private fun setupPaint(paint: TextPaint) {
            paint.typeface = style.typeface
            paint.textSize = style.textSize
            paint.color = style.textColor
            paint.isAntiAlias = true
        }
    }
}