package com.easyfoodvone.app_ui.binding

import androidx.appcompat.widget.AppCompatSeekBar
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.viewdata.DataPageCharity
import com.easyfoodvone.app_ui.databinding.PageCharityBinding as TabletType
import com.easyfoodvone.app_ui.databinding.PageCharityPhoneBinding as PhoneType

sealed class UIVariationCharity<T : ViewDataBinding> {
    abstract val binding: T
    abstract val previousDonationsList: RecyclerView
    abstract val charitySeekBar: AppCompatSeekBar
    abstract fun setData(data: DataPageCharity, lifecycle: LifecycleSafe)

    class Phone(override val binding: PhoneType) : UIVariationCharity<PhoneType>() {
        override val previousDonationsList = binding.rvPreviousDonation
        override val charitySeekBar = binding.charitySeekbar
        override fun setData(data: DataPageCharity, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }

    class Tablet(override val binding: TabletType) : UIVariationCharity<TabletType>() {
        override val previousDonationsList = binding.rvPreviousDonation
        override val charitySeekBar = binding.charitySeekbar
        override fun setData(data: DataPageCharity, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }
}