package com.easyfoodvone.app_ui.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.viewdata.DataPageContactSupport
import com.easyfoodvone.app_ui.R
import com.easyfoodvone.app_ui.binding.UIVariationContactSupport

class ViewContactSupport(val lifecycle: LifecycleSafe, val isPhone: Boolean) {

    private var ui: UIVariationContactSupport<*>? = null

    fun getRoot(): View {
        return ui!!.binding.root
    }

    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, data: DataPageContactSupport) {
        ui = if (isPhone) {
            UIVariationContactSupport.Phone(
                    DataBindingUtil.inflate(inflater, R.layout.page_contact_support_phone, container, false))
        } else {
            UIVariationContactSupport.Tablet(
                DataBindingUtil.inflate(inflater, R.layout.page_contact_support, container, false))

        }.apply {
            setData(data, lifecycle)
        }

        // Prevent memory leak
        lifecycle.nullOnDestroy(::ui)
    }
}