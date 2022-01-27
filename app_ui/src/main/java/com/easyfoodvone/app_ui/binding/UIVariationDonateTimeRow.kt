package com.easyfoodvone.app_ui.binding

import android.widget.TextView
import androidx.databinding.ViewDataBinding
import com.easyfoodvone.app_ui.databinding.RowDonateTimePhoneBinding as PhoneType
import com.easyfoodvone.app_ui.databinding.RowDonateTimeBinding as TabletType

sealed class UIVariationDonateTimeRow<T : ViewDataBinding> {
    abstract val binding: T
    abstract val tvTime: TextView

    class Phone(override val binding: PhoneType): UIVariationDonateTimeRow<PhoneType>() {
        override val tvTime = binding.tvTime
    }

    class Tablet(override val binding: TabletType): UIVariationDonateTimeRow<TabletType>() {
        override val tvTime = binding.tvTime
    }
}