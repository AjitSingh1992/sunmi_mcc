package com.easyfoodvone.app_ui.binding

import androidx.databinding.ViewDataBinding
import androidx.drawerlayout.widget.DrawerLayout
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.viewdata.DataPageHome
import com.easyfoodvone.app_ui.databinding.PageHomePhoneBinding as PhoneType
import com.easyfoodvone.app_ui.databinding.PageHomeTabletBinding as TabletType

/**
 * Common interface for swapping tablet and phone layout bindings which are largely identical in terms of view IDs
 */
sealed class UIVariationHome<T : ViewDataBinding> {
    abstract val binding: T
    abstract val drawerLayout: DrawerLayout
    abstract val header: UIVariationHeader<*>
    abstract fun setData(data: DataPageHome, lifecycle: LifecycleSafe)

    class Phone(override val binding: PhoneType) : UIVariationHome<PhoneType>() {
        override val drawerLayout = binding.drawerLayout
        override val header = UIVariationHeader.Phone(binding.header)
        override fun setData(data: DataPageHome, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }

    class Tablet(override val binding: TabletType) : UIVariationHome<TabletType>() {
        override val drawerLayout = binding.drawerLayout
        override val header = UIVariationHeader.Tablet(binding.header)
        override fun setData(data: DataPageHome, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }
}