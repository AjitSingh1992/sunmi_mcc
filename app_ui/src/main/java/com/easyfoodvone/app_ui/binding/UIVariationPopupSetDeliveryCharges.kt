package com.easyfoodvone.app_ui.binding

import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.easyfoodvone.app_common.viewdata.DataDialogRestaurantTime
import com.easyfoodvone.app_ui.databinding.PopupSetDeliveryChargesBinding
import com.easyfoodvone.app_ui.databinding.PopupSetDeliveryChargesPhoneBinding

sealed class UIVariationPopupSetDeliveryCharges<T : ViewDataBinding> {
    abstract val binding: T
    abstract val txtMessage: TextView
    abstract val edtMinOrder: EditText
    abstract val edtDeliveryCharge: EditText
    abstract val edtFreeDelivery: EditText
    abstract val btnCancel: Button
    abstract val btnOk: Button

    class Phone(override val binding: PopupSetDeliveryChargesPhoneBinding) : UIVariationPopupSetDeliveryCharges<PopupSetDeliveryChargesPhoneBinding>() {
        override val txtMessage: TextView = binding.txtMessage
        override val edtMinOrder: EditText = binding.edtMinOrder
        override val edtDeliveryCharge: EditText = binding.edtDeliveryCharge
        override val edtFreeDelivery: EditText = binding.edtFreeDelivery
        override val btnCancel: Button = binding.btnCancel
        override val btnOk: Button = binding.btnOk
    }

    class Tablet(override val binding: PopupSetDeliveryChargesBinding) : UIVariationPopupSetDeliveryCharges<PopupSetDeliveryChargesBinding>() {
        override val txtMessage: TextView = binding.txtMessage
        override val edtMinOrder: EditText = binding.edtMinOrder
        override val edtDeliveryCharge: EditText = binding.edtDeliveryCharge
        override val edtFreeDelivery: EditText = binding.edtFreeDelivery
        override val btnCancel: Button = binding.btnCancel
        override val btnOk: Button = binding.btnOk
    }


}