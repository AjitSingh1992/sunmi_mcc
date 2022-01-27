package com.easyfoodvone.app_ui.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.viewdata.DataPageDonate
import com.easyfoodvone.app_ui.R
import com.easyfoodvone.app_ui.binding.UIVariationDonate

class ViewDonate(val lifecycle: LifecycleSafe, val isPhone: Boolean) {

    var ui: UIVariationDonate<*>? = null
    private var data: DataPageDonate? = null

    fun onCreateView(data: DataPageDonate, inflater: LayoutInflater, container: ViewGroup?) {
        this.data = data

        ui = if (isPhone) {
            UIVariationDonate.Phone(
                DataBindingUtil.inflate(inflater, R.layout.page_donate_phone, container, false))
        } else {
            UIVariationDonate.Tablet(
                DataBindingUtil.inflate(inflater, R.layout.page_donate, container, false))

        }.apply {
            setData(data, lifecycle)
        }

        // Prevent memory leak
        lifecycle.nullOnDestroy(::ui)
    }
}