package com.easyfoodvone.app_ui.binding

import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import com.easyfoodvone.app_ui.databinding.RowPreviousDonationBinding as TabletType
import com.easyfoodvone.app_ui.databinding.RowPreviousDonationPhoneBinding as PhoneType

sealed class UIVariationCharityDonationRow<T : ViewDataBinding> {
    abstract val binding: T
    abstract val tvMeals: TextView
    abstract val tvDate: TextView
    abstract val rlFoodCollected: RelativeLayout
    abstract val tvStatus: Button
    abstract val cvNo: Button
    abstract val cvYes: Button

    class Phone(override val binding: PhoneType) : UIVariationCharityDonationRow<PhoneType>() {
        override val tvMeals = binding.tvMeals
        override val tvDate = binding.tvDate
        override val rlFoodCollected = binding.rlFoodCollected
        override val tvStatus = binding.tvStatus
        override val cvNo = binding.cvNo
        override val cvYes = binding.cvYes
    }

    class Tablet(override val binding: TabletType) : UIVariationCharityDonationRow<TabletType>() {
        override val tvMeals = binding.tvMeals
        override val tvDate = binding.tvDate
        override val rlFoodCollected = binding.rlFoodCollected
        override val tvStatus = binding.tvStatus
        override val cvNo = binding.cvNo
        override val cvYes = binding.cvYes
    }
}