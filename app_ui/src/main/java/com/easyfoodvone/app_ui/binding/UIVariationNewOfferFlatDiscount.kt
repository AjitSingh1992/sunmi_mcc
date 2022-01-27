package com.easyfoodvone.app_ui.binding

import android.widget.EditText
import android.widget.ImageView
import androidx.databinding.ViewDataBinding
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.viewdata.DataPageNewOffer
import com.easyfoodvone.app_ui.databinding.PageOffersFlatDiscountAmountPhoneBinding as PhoneType
import com.easyfoodvone.app_ui.databinding.PageOffersFlatDiscountAmountBinding as TabletType

sealed class UIVariationNewOfferFlatDiscount<T : ViewDataBinding> {
    abstract val binding: T
    abstract val imageTop: ImageView
    abstract val editOfferTitle: EditText
    //abstract val offerDescripton: EditText
    abstract val editHowMuchCustomer: EditText
    abstract val editDiscountAmount: EditText
    abstract val editMaxDiscount: EditText
    abstract val usagePerCust: EditText
    abstract val totalUsage: EditText
    abstract val editShareEasyfood: EditText
    abstract val editShareRestaurant: EditText
    abstract val editShareFranchise: EditText
    abstract val editTermsCondition: EditText
    abstract fun setData(data: DataPageNewOffer.PageFlatAmount, lifecycle: LifecycleSafe)

    class Phone(override val binding: PhoneType) : UIVariationNewOfferFlatDiscount<PhoneType>() {
        override val imageTop = binding.imageTop
        override val editOfferTitle = binding.editOfferTitle
        //override val offerDescripton = binding.offerDescripton
        override val editHowMuchCustomer = binding.editHowMuchCustomer
        override val editDiscountAmount = binding.editDiscountAmount
        override val editMaxDiscount = binding.editMaxDiscount
        override val usagePerCust = binding.usagePerCust
        override val totalUsage = binding.totalUsage
        override val editShareEasyfood = binding.editShareEasyfood
        override val editShareRestaurant = binding.editShareRestaurant
        override val editShareFranchise = binding.editShareFranchise
        override val editTermsCondition = binding.editTermsCondition
        override fun setData(data: DataPageNewOffer.PageFlatAmount, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }

    class Tablet(override val binding: TabletType) : UIVariationNewOfferFlatDiscount<TabletType>() {
        override val imageTop = binding.imageTop
        override val editOfferTitle = binding.editOfferTitle
        //override val offerDescripton = binding.offerDescripton
        override val editHowMuchCustomer = binding.editHowMuchCustomer
        override val editDiscountAmount = binding.editDiscountAmount
        override val editMaxDiscount = binding.editMaxDiscount
        override val usagePerCust = binding.usagePerCust
        override val totalUsage = binding.totalUsage
        override val editShareEasyfood = binding.editShareEasyfood
        override val editShareRestaurant = binding.editShareRestaurant
        override val editShareFranchise = binding.editShareFranchise
        override val editTermsCondition = binding.editTermsCondition
        override fun setData(data: DataPageNewOffer.PageFlatAmount, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }
}