package com.easyfoodvone.app_ui.binding

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.easyfoodvone.app_ui.databinding.PopupSelectOfferListBinding as TabletType
import com.easyfoodvone.app_ui.databinding.PopupSelectOfferListPhoneBinding as PhoneType

sealed class UIVariationPopupSelectOfferList<T : ViewDataBinding> {
    abstract val binding: T
    abstract val offerSpendXDiscountX: ViewGroup
    abstract val offeringFlatAmountDiscount: ViewGroup
    abstract val discountOfferWithPercentage: ViewGroup
    abstract val comboOffers: ViewGroup

    class Phone(override val binding: PhoneType) : UIVariationPopupSelectOfferList<PhoneType>() {
        override val offerSpendXDiscountX = binding.offerSpendXDiscountX
        override val offeringFlatAmountDiscount = binding.offeringFlatAmountDiscount
        override val discountOfferWithPercentage = binding.discountOfferWithPercentage
        override val comboOffers = binding.comboOffers
    }

    class Tablet(override val binding: TabletType) : UIVariationPopupSelectOfferList<TabletType>() {
        override val offerSpendXDiscountX = binding.offerSpendXDiscountX
        override val offeringFlatAmountDiscount = binding.offeringFlatAmountDiscount
        override val discountOfferWithPercentage = binding.discountOfferWithPercentage
        override val comboOffers = binding.comboOffers
    }
}