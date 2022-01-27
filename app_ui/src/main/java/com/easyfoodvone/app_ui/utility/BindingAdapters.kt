package com.easyfoodvone.app_ui.utility

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter

@BindingAdapter("binding_layout_constraintHorizontal_bias")
fun setBindingLayoutConstraintHorizontalBias(childView: View, bias: Float) {
    val params = childView.getLayoutParams() as ConstraintLayout.LayoutParams
    params.horizontalBias = bias
    childView.setLayoutParams(params)
}