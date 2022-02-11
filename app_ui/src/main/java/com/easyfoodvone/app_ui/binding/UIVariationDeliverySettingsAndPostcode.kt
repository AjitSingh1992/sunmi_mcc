package com.easyfoodvone.app_ui.binding

import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.viewdata.DataPageDeliverySettingsAndPostcodes
import com.easyfoodvone.app_ui.databinding.PageDeliverySettingsAndPostcodesBinding
import com.easyfoodvone.app_ui.databinding.PageDeliverySettingsAndPostcodesPhoneBinding

sealed class UIVariationDeliverySettingsAndPostcode<T : ViewDataBinding> {
    abstract val binding: T
    abstract val recyclerTimeList: RecyclerView
    abstract val etDeliveryTravelTime: EditText
    abstract val etQuiet: EditText
    abstract val etNormal: EditText
    abstract val etBusy: EditText

    abstract val rbQuiet: RadioButton
    abstract val rbNormal: RadioButton
    abstract val rbBusy: RadioButton
    abstract fun setData(data: DataPageDeliverySettingsAndPostcodes, lifecycle: LifecycleSafe)

    class Phone(override val binding: PageDeliverySettingsAndPostcodesPhoneBinding): UIVariationDeliverySettingsAndPostcode<PageDeliverySettingsAndPostcodesPhoneBinding>() {
        override val recyclerTimeList = binding.recyclerTimeList
        override val etDeliveryTravelTime = binding.etDeliveryTravelTime

        override val etQuiet = binding.etQuiet
        override val etNormal = binding.etNormal
        override val etBusy = binding.etBusy

        override val rbQuiet = binding.rbQuiet
        override val rbNormal = binding.rbNormal
        override val rbBusy = binding.rbBusy

        override fun setData(data: DataPageDeliverySettingsAndPostcodes, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }

    class Tablet(override val binding: PageDeliverySettingsAndPostcodesBinding): UIVariationDeliverySettingsAndPostcode<PageDeliverySettingsAndPostcodesBinding>() {
        override val recyclerTimeList = binding.recyclerTimeList
        override val etDeliveryTravelTime = binding.etDeliveryTravelTime

        override val etQuiet = binding.etQuiet
        override val etNormal = binding.etNormal
        override val etBusy = binding.etBusy

        override val rbQuiet = binding.rbQuiet
        override val rbNormal = binding.rbNormal
        override val rbBusy = binding.rbBusy

        override fun setData(data: DataPageDeliverySettingsAndPostcodes, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }
}