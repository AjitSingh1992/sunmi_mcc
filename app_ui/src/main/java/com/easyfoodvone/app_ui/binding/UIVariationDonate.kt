package com.easyfoodvone.app_ui.binding

import android.widget.EditText
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.viewdata.DataPageDonate
import com.easyfoodvone.app_ui.databinding.PageDonateBinding as TabletType
import com.easyfoodvone.app_ui.databinding.PageDonatePhoneBinding as PhoneType

sealed class UIVariationDonate<T : ViewDataBinding> {
    abstract val binding: T
    abstract val rvTime: RecyclerView
    abstract val etNoOfMeals: EditText
    abstract fun setData(data: DataPageDonate, lifecycle: LifecycleSafe)

    class Phone(override val binding: PhoneType): UIVariationDonate<PhoneType>() {
        override val rvTime = binding.rvTime
        override val etNoOfMeals = binding.etNoOfMeals
        override fun setData(data: DataPageDonate, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }

    class Tablet(override val binding: TabletType): UIVariationDonate<TabletType>() {
        override val rvTime = binding.rvTime
        override val etNoOfMeals = binding.etNoOfMeals
        override fun setData(data: DataPageDonate, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }
}