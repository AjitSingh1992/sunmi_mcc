package com.easyfoodvone.app_ui.binding

import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.widget.ContentLoadingProgressBar
import androidx.databinding.ViewDataBinding
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.viewdata.DataRowOrderDetail
import com.easyfoodvone.app_ui.textview_orderdetail.TextViewOrderDetailPrice
import com.easyfoodvone.app_ui.textview_orderdetail.TextViewOrderDetailRows
import com.easyfoodvone.app_ui.databinding.RowOrderDetailPhoneBinding as PhoneType
import com.easyfoodvone.app_ui.databinding.RowOrderDetailTabletBinding as TabletType

/**
 * Common interface for swapping tablet and phone layout bindings which are largely identical in terms of view IDs
 */
sealed class UIVariationOrderDetailRow<T : ViewDataBinding> {
    abstract val binding: T
    abstract val expandingLayout: FrameLayout
    abstract val progressBar: ContentLoadingProgressBar
    abstract val textViewOrderDetailRows: TextViewOrderDetailRows
    abstract val textViewOrderDetailPriceEach: TextViewOrderDetailPrice
    abstract val textViewOrderDetailPriceTotal: TextViewOrderDetailPrice
    abstract val imageBarcode: ImageView
    abstract fun setData(data: DataRowOrderDetail, lifecycle: LifecycleSafe)

    class Phone(override val binding: PhoneType) : UIVariationOrderDetailRow<PhoneType>() {
        override val expandingLayout = binding.expandingLayout
        override val progressBar = binding.progressBar
        override val textViewOrderDetailRows = binding.textViewOrderDetailRows
        override val textViewOrderDetailPriceEach = binding.textViewOrderDetailPriceEach
        override val textViewOrderDetailPriceTotal = binding.textViewOrderDetailPriceTotal
        override val imageBarcode = binding.imageBarcode
        override fun setData(data: DataRowOrderDetail, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }

    class Tablet(override val binding: TabletType) : UIVariationOrderDetailRow<TabletType>() {
        override val expandingLayout = binding.expandingLayout
        override val progressBar = binding.progressBar
        override val textViewOrderDetailRows = binding.textViewOrderDetailRows
        override val textViewOrderDetailPriceEach = binding.textViewOrderDetailPriceEach
        override val textViewOrderDetailPriceTotal = binding.textViewOrderDetailPriceTotal
        override val imageBarcode = binding.imageBarcode
        override fun setData(data: DataRowOrderDetail, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }
}