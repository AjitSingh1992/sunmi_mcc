package com.easyfoodvone.app_ui.textview_orderdetail

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat
import com.easyfoodvone.app_ui.R

class TextViewOrderDetailUtil {
    data class LoadedStyle(val textSize: Float, val textColor: Int, val typeface: Typeface?)

    companion object {
        /**
         * @param a The TypedArray to read from. You must recycle it yourself.
         * @param b The id which will be used with a.getResourceId(), which points to the style you want to load.
         *          i.e. R.styleable.TextViewOrderDetailPrice_priceStyle
         * @param context Used to fetch the theme via context.theme and also to load the font from
         */
        fun loadStyleResource(a: TypedArray, styleFieldWithinA: Int, context: Context): LoadedStyle {
            val textStyleId: Int = a.getResourceId(styleFieldWithinA, -1)

            if (textStyleId < 0) {
                throw IllegalArgumentException("Style xml parameter was not provided on the view")
            }

            context.theme.obtainStyledAttributes(textStyleId, R.styleable.TextAppearanceOrderDetail).let { b: TypedArray ->
                try {
                    return LoadedStyle(
                        b.getDimension(R.styleable.TextAppearanceOrderDetail_android_textSize, 1f),
                        b.getColor(R.styleable.TextAppearanceOrderDetail_android_textColor, 0),
                        b.getResourceId(R.styleable.TextAppearanceOrderDetail_android_fontFamily, -1).let { typefaceId: Int ->
                                if (typefaceId < 0) null else ResourcesCompat.getFont(context, typefaceId) })

                } finally {
                    b.recycle()
                }
            }
        }
    }
}