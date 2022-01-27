package com.easyfoodvone.app_ui.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.viewdata.DataPageOrderReport
import com.easyfoodvone.app_common.ws.OrderReportResponse
import com.easyfoodvone.app_ui.R
import com.easyfoodvone.app_ui.adapter.AdapterReportOrderList
import com.easyfoodvone.app_ui.binding.UIVariationOrderReport

class ViewOrderReport(val lifecycle: LifecycleSafe, val isPhone: Boolean) {

    private var ui: UIVariationOrderReport<*>? = null

    fun getRoot(): View {
        return ui!!.binding.root
    }

    fun onCreateView(data: DataPageOrderReport, inflater: LayoutInflater, container: ViewGroup?) {
        ui = if (isPhone) {
            UIVariationOrderReport.Phone(DataBindingUtil.inflate(inflater, R.layout.page_order_report_phone, container, false))
        } else {
            UIVariationOrderReport.Tablet(DataBindingUtil.inflate(inflater, R.layout.page_order_report, container, false))

        }.apply {
            setData(data, lifecycle)
        }

        lifecycle.addObserverOnceUntilDestroy(data.ordersList, observerOrderList, false)

        // Prevent memory leak
        lifecycle.nullOnDestroy(::ui)
    }

    private val observerOrderList = object : Observer<List<OrderReportResponse.OrdersList>?> {
        override fun onChanged(items: List<OrderReportResponse.OrdersList>?) {
            if (items == null) {
                // Not yet loaded
            } else {
                // TODO reuse adapter properly
                ui!!.totalOrdersList.setLayoutManager(LinearLayoutManager(getRoot().context, LinearLayoutManager.VERTICAL, false))
                ui!!.totalOrdersList.setAdapter(AdapterReportOrderList(items, isPhone))
            }
        }
    }
}