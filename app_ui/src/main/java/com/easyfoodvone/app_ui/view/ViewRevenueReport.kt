package com.easyfoodvone.app_ui.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.viewdata.DataPageRevenueReport
import com.easyfoodvone.app_ui.R
import com.easyfoodvone.app_ui.binding.UIVariationRevenueReport

class ViewRevenueReport(val lifecycle: LifecycleSafe, val isPhone: Boolean) {

    private var ui: UIVariationRevenueReport<*>? = null

    fun getRoot(): View {
        return ui!!.binding.root
    }

    fun onCreateView(data: DataPageRevenueReport, inflater: LayoutInflater, container: ViewGroup?) {
        ui = if (isPhone) {
            UIVariationRevenueReport.Phone(DataBindingUtil.inflate(inflater, R.layout.page_revenue_report_phone, container, false))
        } else {
            UIVariationRevenueReport.Tablet(DataBindingUtil.inflate(inflater, R.layout.page_revenue_report, container, false))

        }.apply {
            setData(data, lifecycle)
        }

        // Prevent memory leak
        lifecycle.nullOnDestroy(::ui)
    }
}