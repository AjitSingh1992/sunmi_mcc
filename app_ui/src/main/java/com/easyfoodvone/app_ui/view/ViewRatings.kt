package com.easyfoodvone.app_ui.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.viewdata.DataPageRatings
import com.easyfoodvone.app_ui.R
import com.easyfoodvone.app_ui.binding.UIVariationRatings

class ViewRatings(val lifecycle: LifecycleSafe, val isPhone: Boolean) {

    var ui: UIVariationRatings<*>? = null
    private var data: DataPageRatings? = null

    fun onCreateView(data: DataPageRatings, inflater: LayoutInflater, container: ViewGroup?) {
        this.data = data

        ui = if (isPhone) {
            UIVariationRatings.Phone(
                DataBindingUtil.inflate(inflater, R.layout.page_ratings_phone, container, false))
        } else {
            UIVariationRatings.Tablet(
                DataBindingUtil.inflate(inflater, R.layout.page_ratings, container, false))

        }.apply {
            setData(data, lifecycle)
        }

        // Prevent memory leak
        lifecycle.nullOnDestroy(::ui)
    }
}