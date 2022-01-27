package com.easyfoodvone.app_ui.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.easyfoodvone.app_common.separation.nullOnDestroy
import com.easyfoodvone.app_common.viewdata.DataPageRestaurantProfile
import com.easyfoodvone.app_ui.R
import com.easyfoodvone.app_ui.binding.UIVariationRestaurantProfile

class ViewRestarauntProfile(val fragment: Fragment, val isPhone: Boolean) {

    var ui: UIVariationRestaurantProfile<*>? = null
    private var data: DataPageRestaurantProfile? = null

    fun getRoot(): View {
        return ui!!.binding.root
    }

    fun onCreateView(data: DataPageRestaurantProfile, inflater: LayoutInflater, container: ViewGroup?) {
        this.data = data

        ui = if (isPhone) {
            UIVariationRestaurantProfile.Phone(DataBindingUtil.inflate(inflater, R.layout.page_profile_phone, container, false))
        } else {
            UIVariationRestaurantProfile.Tablet(DataBindingUtil.inflate(inflater, R.layout.page_profile, container, false))

        }.apply {
            setData(data, fragment)
        }

        // Prevent memory leak
        ::ui.nullOnDestroy(fragment)
    }
}