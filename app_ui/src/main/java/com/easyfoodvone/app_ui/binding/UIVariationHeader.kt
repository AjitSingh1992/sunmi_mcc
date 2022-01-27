package com.easyfoodvone.app_ui.binding

import android.widget.ImageView
import androidx.databinding.ViewDataBinding
import com.easyfoodvone.app_ui.databinding.IncludeHeaderPhoneBinding as PhoneType
import com.easyfoodvone.app_ui.databinding.IncludeHeaderTabletBinding as TabletType

/**
 * Common interface for swapping tablet and phone layout bindings which are largely identical in terms of view IDs
 */
sealed class UIVariationHeader<T : ViewDataBinding> {
    abstract val binding: T
    abstract val restaurantLogo: ImageView?
    // Data is bound through the layout xml, so no setData method here

    class Phone(override val binding: PhoneType) : UIVariationHeader<PhoneType>() {
        override val restaurantLogo = null
    }

    class Tablet(override val binding: TabletType) : UIVariationHeader<TabletType>() {
        override val restaurantLogo = binding.restaurantLogo
    }
}