package com.easyfoodvone.app_ui.binding

import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.viewdata.DataPageNewOffer
import com.easyfoodvone.app_ui.databinding.PageOffersComboMealsPhoneBinding as PhoneType
import com.easyfoodvone.app_ui.databinding.PageOffersComboMealsBinding as TabletType

sealed class UIVariationNewOfferCombo<T : ViewDataBinding> {
    abstract val binding: T
    abstract val imageTop: ImageView
    abstract val editOfferTitle: EditText
    abstract val offerDescripton: EditText
    abstract val editHowMuchCustomer: EditText
    abstract val editDiscountAmount: EditText
    abstract val editTermsCondition: EditText
    abstract val spinnerMainItem: Spinner
    abstract val selectedMenuItemsList: RecyclerView
    abstract fun setData(data: DataPageNewOffer.PageCombo, lifecycle: LifecycleSafe)

    class Phone(override val binding: PhoneType) : UIVariationNewOfferCombo<PhoneType>() {
        override val imageTop = binding.imageTop
        override val editOfferTitle = binding.editOfferTitle
        override val offerDescripton = binding.offerDescripton
        override val editHowMuchCustomer = binding.editHowMuchCustomer
        override val editDiscountAmount = binding.editDiscountAmount
        override val editTermsCondition = binding.editTermsCondition
        override val spinnerMainItem = binding.spinnerMainItem
        override val selectedMenuItemsList = binding.selectedMenuItemsList
        override fun setData(data: DataPageNewOffer.PageCombo, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }

    class Tablet(override val binding: TabletType) : UIVariationNewOfferCombo<TabletType>() {
        override val imageTop = binding.imageTop
        override val editOfferTitle = binding.editOfferTitle
        override val offerDescripton = binding.offerDescripton
        override val editHowMuchCustomer = binding.editHowMuchCustomer
        override val editDiscountAmount = binding.editDiscountAmount
        override val editTermsCondition = binding.editTermsCondition
        override val spinnerMainItem = binding.spinnerMainItem
        override val selectedMenuItemsList = binding.selectedMenuItemsList
        override fun setData(data: DataPageNewOffer.PageCombo, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }
}