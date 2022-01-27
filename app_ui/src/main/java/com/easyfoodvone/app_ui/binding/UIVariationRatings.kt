package com.easyfoodvone.app_ui.binding

import android.widget.Spinner
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.viewdata.DataPageRatings
import com.easyfoodvone.app_ui.databinding.PageRatingsBinding as TabletType
import com.easyfoodvone.app_ui.databinding.PageRatingsPhoneBinding as PhoneType

sealed class UIVariationRatings<T : ViewDataBinding> {
    abstract val binding: T
    abstract val ratingList: RecyclerView
    abstract val spinnerStar: Spinner
    abstract fun setData(data: DataPageRatings, lifecycle: LifecycleSafe)

    class Phone(override val binding: PhoneType) : UIVariationRatings<PhoneType>() {
        override val ratingList = binding.ratingList
        override val spinnerStar = binding.spinnerStar
        override fun setData(data: DataPageRatings, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }

    class Tablet(override val binding: TabletType) : UIVariationRatings<TabletType>() {
        override val ratingList = binding.ratingList
        override val spinnerStar = binding.spinnerStar
        override fun setData(data: DataPageRatings, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }
}