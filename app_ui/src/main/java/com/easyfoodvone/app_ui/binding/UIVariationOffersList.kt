package com.easyfoodvone.app_ui.binding

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.viewdata.DataPageListOfOffers
import com.easyfoodvone.app_ui.databinding.PageOffersListBinding as TabletType
import com.easyfoodvone.app_ui.databinding.PageOffersListPhoneBinding as PhoneType

sealed class UIVariationOffersList<T : ViewDataBinding> {
    abstract val binding: T
    abstract val offersList: RecyclerView
    abstract fun setData(data: DataPageListOfOffers, lifecycle: LifecycleSafe)

    class Phone(override val binding: PhoneType) : UIVariationOffersList<PhoneType>() {
        override val offersList = binding.offersList
        override fun setData(data: DataPageListOfOffers, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }

    class Tablet(override val binding: TabletType) : UIVariationOffersList<TabletType>() {
        override val offersList = binding.offersList
        override fun setData(data: DataPageListOfOffers, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }
}