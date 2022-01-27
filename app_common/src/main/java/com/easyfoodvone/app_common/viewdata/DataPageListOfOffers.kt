package com.easyfoodvone.app_common.viewdata

import androidx.databinding.ObservableArrayList
import com.easyfoodvone.app_common.separation.ObservableField
import com.easyfoodvone.app_common.ws.OffersResponse

class DataPageListOfOffers(
        val outputEvents: OutputEvents,
        val inputEvents: ObservableField<InputEvents?>,
        val runningOffers: ObservableField<String>,
        val expiredOffers: ObservableField<String>,
        val percentageDiscount: ObservableField<String>,
        val flatDiscount: ObservableField<String>,
        val comboDiscount: ObservableField<String>,
        val offersList: ObservableArrayList<OffersResponse.Data.Offers>) {

    interface OutputEvents {
        fun onClickAddOffer()
        fun onClickRunningOffer()
        fun onClickExpiredOffer()
        fun onClickPercentageDiscount()
        fun onClickFlatDiscount()
        fun onClickComboDiscount()
        fun onClickDeleteRow(deletePosition: Int, deleteItem: OffersResponse.Data.Offers)
        fun onClickEditRow(editPosition: Int, editItem: OffersResponse.Data.Offers)
    }

    interface InputEvents {
        fun showDeleteConfirmation(
            deleteItem: OffersResponse.Data.Offers,
            onOk: Runnable)
        fun showEditConfirmation(
            editItem: OffersResponse.Data.Offers,
            onOk: Runnable)
        fun showChangeActivationDialog(
            isEditNotDelete: Boolean,
            activated: Boolean,
            item: OffersResponse.Data.Offers,
            onOk: Runnable)
        fun showAddOfferDialog(
            onSpendXDiscountX: Runnable,
            onFlatDiscount: Runnable,
            onPercentDiscount: Runnable,
            onComboItemDiscount: Runnable)
    }
}