package com.easyfoodvone.app_ui.binding

import android.widget.TextView
import androidx.databinding.ViewDataBinding
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.viewdata.DataRowOrderOverview
import com.easyfoodvone.app_ui.databinding.RowOrderOverviewPhoneBinding as PhoneType
import com.easyfoodvone.app_ui.databinding.RowOrderOverviewTabletBinding as TabletType

/**
 * Common interface for swapping tablet and phone layout bindings which are largely identical in terms of view IDs
 */
sealed class UIVariationOrderOverviewRow<T : ViewDataBinding> {
    abstract val binding: T
    abstract val buttonEditAcceptedStatus: TextView
    abstract fun setData(data: DataRowOrderOverview, lifecycle: LifecycleSafe)

    class Phone(override val binding: PhoneType) : UIVariationOrderOverviewRow<PhoneType>() {
        override val buttonEditAcceptedStatus = binding.buttonEditAcceptedStatus
        override fun setData(data: DataRowOrderOverview, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }

    class Tablet(override val binding: TabletType) : UIVariationOrderOverviewRow<TabletType>() {
        override val buttonEditAcceptedStatus = binding.textOrderStatus
        override fun setData(data: DataRowOrderOverview, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }
}