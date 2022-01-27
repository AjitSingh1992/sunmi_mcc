package com.easyfoodvone.app_ui.binding

import android.widget.EditText
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.viewdata.DataPageNewOffer
import com.easyfoodvone.app_ui.databinding.PageOffersSpendXGetDiscountPhoneBinding as PhoneType
import com.easyfoodvone.app_ui.databinding.PageOffersSpendXGetDiscountBinding as TabletType

sealed class UIVariationNewOfferSpendX<T : ViewDataBinding> {
    abstract val binding: T
    abstract val bucketsRecycler: RecyclerView
    abstract val editOfferTitle: EditText
    abstract val offerDescripton: EditText
    abstract val editBetween: EditText
    abstract val editAnd: EditText
    abstract val editGiveDiscount: EditText
    abstract val editTermsCondition: EditText
    abstract fun setData(data: DataPageNewOffer.PageSpendX, lifecycle: LifecycleSafe)

    class Phone(override val binding: PhoneType) : UIVariationNewOfferSpendX<PhoneType>() {
        override val bucketsRecycler = binding.bucketsRecycler
        override val editOfferTitle = binding.editOfferTitle
        override val offerDescripton = binding.offerDescripton
        override val editBetween = binding.editBetween
        override val editAnd = binding.editAnd
        override val editGiveDiscount = binding.editGiveDiscount
        override val editTermsCondition = binding.editTermsCondition
        override fun setData(data: DataPageNewOffer.PageSpendX, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }

    class Tablet(override val binding: TabletType) : UIVariationNewOfferSpendX<TabletType>() {
        override val bucketsRecycler = binding.bucketsRecycler
        override val editOfferTitle = binding.editOfferTitle
        override val offerDescripton = binding.offerDescripton
        override val editBetween = binding.editBetween
        override val editAnd = binding.editAnd
        override val editGiveDiscount = binding.editGiveDiscount
        override val editTermsCondition = binding.editTermsCondition
        override fun setData(data: DataPageNewOffer.PageSpendX, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }
}