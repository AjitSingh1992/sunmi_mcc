package com.easyfoodvone.app_ui.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.viewdata.DataPageCharity
import com.easyfoodvone.app_ui.R
import com.easyfoodvone.app_ui.binding.UIVariationCharity

class ViewCharity(val lifecycle: LifecycleSafe, val isPhone: Boolean) {

    var ui: UIVariationCharity<*>? = null
    private var data: DataPageCharity? = null

    fun onCreateView(data: DataPageCharity, inflater: LayoutInflater, container: ViewGroup?) {
        this.data = data

        ui = if (isPhone) {
            UIVariationCharity.Phone(DataBindingUtil.inflate(inflater, R.layout.page_charity_phone, container, false))
        } else {
            UIVariationCharity.Tablet(DataBindingUtil.inflate(inflater, R.layout.page_charity, container, false))

        }.apply {
            setData(data, lifecycle)
        }

        // Prevent memory leak
        lifecycle.nullOnDestroy(::ui)
    }
}