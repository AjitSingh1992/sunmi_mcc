package com.easyfoodvone.app_ui.binding

import android.widget.Button
import android.widget.EditText
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.easyfoodvone.app_common.separation.unbindOnDestroy
import com.easyfoodvone.app_common.viewdata.DataPageRestaurantProfile
import com.easyfoodvone.app_ui.databinding.PageProfileBinding
import com.easyfoodvone.app_ui.databinding.PageProfilePhoneBinding

sealed class UIVariationRestaurantProfile<T : ViewDataBinding> {
    abstract val binding: T
    abstract val postcode: EditText
    abstract val restname: EditText
    abstract val serve_style: EditText
    abstract val about: EditText
    abstract val web: EditText
    abstract val phone: EditText
    abstract val landline: EditText
    abstract val address: EditText
    abstract val email: EditText
    abstract val imageListView: RecyclerView
    abstract val btnSave: Button

    abstract fun setData(data: DataPageRestaurantProfile, lifecycle: Fragment)

    class Phone(override val binding: PageProfilePhoneBinding) : UIVariationRestaurantProfile<PageProfilePhoneBinding>() {
        override val postcode: EditText = binding.postcode
        override val restname: EditText = binding.restname
        override val serve_style: EditText = binding.serveStyle
        override val about: EditText = binding.about
        override val web: EditText = binding.web
        override val phone: EditText = binding.phone
        override val landline: EditText = binding.landline
        override val address: EditText = binding.address
        override val email: EditText = binding.email
        override val imageListView: RecyclerView = binding.imageListView
        override val btnSave: Button = binding.btnSave
        override fun setData(data: DataPageRestaurantProfile, lifecycle: Fragment) {
            binding.data = data
            ::binding.unbindOnDestroy(lifecycle)
        }
    }

    class Tablet(override val binding: PageProfileBinding) : UIVariationRestaurantProfile<PageProfileBinding>() {
        override val postcode: EditText = binding.postcode
        override val restname: EditText = binding.restname
        override val serve_style: EditText = binding.serveStyle
        override val about: EditText = binding.about
        override val web: EditText = binding.web
        override val phone: EditText = binding.phone
        override val landline: EditText = binding.landline
        override val address: EditText = binding.address
        override val email: EditText = binding.email
        override val imageListView: RecyclerView = binding.imageListView
        override val btnSave: Button = binding.btnSave
            override fun setData(data: DataPageRestaurantProfile, lifecycle: Fragment) {
                binding.data = data
                ::binding.unbindOnDestroy(lifecycle)
            }
    }
}