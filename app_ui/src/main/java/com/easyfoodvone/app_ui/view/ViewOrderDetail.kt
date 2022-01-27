package com.easyfoodvone.app_ui.view

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableList
import androidx.databinding.OnRebindCallback
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.utility.BarcodeUtils
import com.easyfoodvone.app_common.viewdata.DataRowOrderDetail
import com.easyfoodvone.app_ui.R
import com.easyfoodvone.app_ui.binding.UIVariationOrderDetailRow

class ViewOrderDetail(val lifecycle: LifecycleSafe, val isPhone: Boolean, val data: DataRowOrderDetail) {

    var ui: UIVariationOrderDetailRow<*>? = null

    fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?) {
        ui = if (isPhone) {
                UIVariationOrderDetailRow.Phone(DataBindingUtil.inflate(inflater, R.layout.row_order_detail_phone, parent, false))
            } else {
                UIVariationOrderDetailRow.Tablet(DataBindingUtil.inflate(inflater, R.layout.row_order_detail_tablet, parent, false))

            }.apply {
                setData(data, lifecycle)

                textViewOrderDetailRows.setLinkedTotalViews(
                    textViewOrderDetailPriceEach,
                    textViewOrderDetailPriceTotal)

                // In case the text view is requested to be measured before bind is called (which can happen sometimes in rare cases)
                textViewOrderDetailRows.setData(emptyArray())

                binding.addOnRebindCallback(onRebindCallback)
            }

        // Prevent memory leak
        lifecycle.nullOnDestroy(::ui)

        // Every time onCreateView is called here, a new presenter is created also, the list will be empty so no need for callNow
        lifecycle.addObserverOnceUntilDestroy(data.itemRows, itemDetailRowObserver, callNow=false)

        lifecycle.addObserverOnceUntilDestroy(data.barcodeCodabarFormat, barcodeObserver, callNow=true)
    }

    private val onRebindCallback = object : OnRebindCallback<ViewDataBinding>() {
        override fun onPreBind(untypedBinding: ViewDataBinding?): Boolean {
            val expand = ChangeBounds()
            expand.addTarget(ui!!.expandingLayout.getId())
            expand.addTarget(ui!!.progressBar.getId())

            val set = TransitionSet()
            set.ordering = TransitionSet.ORDERING_TOGETHER
            set.addTransition(expand)

            TransitionManager.beginDelayedTransition(ui!!.binding.root as ViewGroup, set)

            return true
        }
    }

    private val itemDetailRowObserver = object : ObservableList.OnListChangedCallback<ObservableList<DataRowOrderDetail.ItemRow>>() {
        override fun onChanged(sender: ObservableList<DataRowOrderDetail.ItemRow>) {
            ui!!.textViewOrderDetailRows.setData(sender.toTypedArray())
        }

        override fun onItemRangeChanged(sender: ObservableList<DataRowOrderDetail.ItemRow>, positionStart: Int, itemCount: Int) {
            ui!!.textViewOrderDetailRows.setData(sender.toTypedArray())
        }

        override fun onItemRangeInserted(sender: ObservableList<DataRowOrderDetail.ItemRow>, positionStart: Int, itemCount: Int) {
            ui!!.textViewOrderDetailRows.setData(sender.toTypedArray())
        }

        override fun onItemRangeMoved(sender: ObservableList<DataRowOrderDetail.ItemRow>, fromPosition: Int, toPosition: Int, itemCount: Int) {
            ui!!.textViewOrderDetailRows.setData(sender.toTypedArray())
        }

        override fun onItemRangeRemoved(sender: ObservableList<DataRowOrderDetail.ItemRow>, positionStart: Int, itemCount: Int) {
            ui!!.textViewOrderDetailRows.setData(sender.toTypedArray())
        }
    }

    private val barcodeObserver = object : Observer<String> {
        override fun onChanged(barcodeText: String) {
            if (barcodeText.isEmpty()) {
                return
            }

            val barcode: Bitmap? = BarcodeUtils.encodeAsBitmap(barcodeText, BarcodeUtils.Format.CODABAR, 100, 60)
            if (barcode == null) {
                ui!!.imageBarcode.setImageDrawable(null)
            } else {
                ui!!.imageBarcode.setImageBitmap(barcode)
            }
        }
    }
}