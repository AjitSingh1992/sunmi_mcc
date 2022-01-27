package com.easyfoodvone.app_ui.binding

import android.widget.ImageView
import androidx.databinding.ViewDataBinding
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.viewdata.DataRowRestaurantMenu
import com.easyfoodvone.app_ui.databinding.RowRestaurantMenuCategoryBinding as TabletType
import com.easyfoodvone.app_ui.databinding.RowRestaurantMenuCategoryPhoneBinding as PhoneType

sealed class UIVariationRestaurantMenuCategoryRow<T : ViewDataBinding> {
    abstract val binding: T
    //abstract val imageView: ImageView
    abstract val dragHandle: ImageView
    abstract fun setData(data: DataRowRestaurantMenu, lifecycle: LifecycleSafe)

    class Phone(override val binding: PhoneType) : UIVariationRestaurantMenuCategoryRow<PhoneType>() {
        override val dragHandle = binding.dragHandle
        override fun setData(data: DataRowRestaurantMenu, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }

    class Tablet(override val binding: TabletType) : UIVariationRestaurantMenuCategoryRow<TabletType>() {
        override val dragHandle = binding.dragHandle
        override fun setData(data: DataRowRestaurantMenu, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }
}