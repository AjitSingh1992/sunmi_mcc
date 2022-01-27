package com.easyfoodvone.app_ui.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.OnRebindCallback
import androidx.databinding.ViewDataBinding
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.viewdata.DataPageHome
import com.easyfoodvone.app_ui.R
import com.easyfoodvone.app_ui.binding.UIVariationHome
import com.squareup.picasso.Picasso

class ViewHome(private val lifecycle: LifecycleSafe) {

    var ui: UIVariationHome<*>? = null

    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, data: DataPageHome, isPhone: Boolean) {
        ui = if (isPhone) {
                UIVariationHome.Phone(DataBindingUtil.inflate(inflater, R.layout.page_home_phone, container, false))
            } else {
                UIVariationHome.Tablet(DataBindingUtil.inflate(inflater, R.layout.page_home_tablet, container, false))

            }.apply {
                setData(data, lifecycle)
                binding.addOnRebindCallback(onRebindCallback)
            }

        // Prevent memory leak
        lifecycle.nullOnDestroy(::ui)

        lifecycle.setValueUntilDestroy(data.inputEvents, inputEventHandler)

        loadRestaurantIcon(data.logoUrl)
    }

    private fun loadRestaurantIcon(logo: String?) {
        val imageView: ImageView = ui!!.header.restaurantLogo ?: return

        // Prevent crash in Picasso
        val safeLogo = if (logo.isNullOrEmpty()) "fail" else logo

        Picasso.get()
            .load(safeLogo)
            .into(imageView)
    }

    private val onRebindCallback = object : OnRebindCallback<ViewDataBinding>() {
        override fun onPreBind(untypedBinding: ViewDataBinding?): Boolean {
            return true
        }
    }

    private val inputEventHandler: DataPageHome.InputEvents = object : DataPageHome.InputEvents {
        override fun toggleSideDrawer() {
            if (ui!!.drawerLayout.isDrawerOpen(GravityCompat.END)) {
                ui!!.drawerLayout.closeDrawer(GravityCompat.END)
            } else {
                ui!!.drawerLayout.openDrawer(GravityCompat.END)
            }
        }
    }
}