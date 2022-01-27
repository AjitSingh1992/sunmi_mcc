package com.easyfoodvone.app_common.viewdata

import androidx.databinding.ObservableArrayList
import com.easyfoodvone.app_common.separation.ObservableField
import com.easyfoodvone.app_common.ws.DeliveryPostCodeBean

class DataPageDeliverySettingsAndPostcodes(
        val outputEvents: OutputEvents,
        val inputEvents: ObservableField<InputEvents?>,
        val inputMiles: ObservableField<String>,
        val inputDeliveryTime: ObservableField<String>,
        val inputDeliveryCharge: ObservableField<String>,
        val inputMinOrder: ObservableField<String>,
        val inputFreeDelivery: ObservableField<String>,
        val inputAvgPrepTime: ObservableField<String>,
        val allPostcodesChecked: ObservableField<Boolean>,
        val postcodesList: ObservableArrayList<DeliveryPostCodeBean.DataBean?>) {

    interface OutputEvents {
        fun onClickUpdate()
        fun onEditClick(position: Int, data: DeliveryPostCodeBean.DataBean?)
        fun onDeleteClick(position: Int, data: DeliveryPostCodeBean.DataBean?)
    }

    interface InputEvents {
        fun setErrorEtDeliveryCharge(msg: String)
        fun setErrorEtMinimumOrder(msg: String)
        fun setErrorEtFreeDelivery(msg: String)
        fun alertDialog(msg: String?, endAction: DialogOkAction)
        fun alertDialog(msg: String?, initial: DialogCharges?, okAction: DialogChargesOkAction)
        fun alertDialogDelete(postcode: String?, okAction: DialogOkAction)
    }

    interface DialogOkAction {
        fun onOk()
    }

    interface DialogChargesOkAction {
        fun onOk(charges: DialogCharges)
    }

    data class DialogCharges(val minOrder: String, val deliveryCharge: String, val freeDelivery: String)
}