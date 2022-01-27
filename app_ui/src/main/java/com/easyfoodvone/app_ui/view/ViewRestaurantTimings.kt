package com.easyfoodvone.app_ui.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.viewdata.DataDialogRestaurantTime
import com.easyfoodvone.app_common.viewdata.DataPageRestaurantTimings
import com.easyfoodvone.app_common.ws.AllDaysRestaurantTiming
import com.easyfoodvone.app_ui.R
import com.easyfoodvone.app_ui.adapter.AdapterRestaurantTimings
import com.easyfoodvone.app_ui.binding.UIVariationPopupSetRestaurentTime
import com.easyfoodvone.app_ui.binding.UIVariationRestaurantTimings
import com.easyfoodvone.app_ui.databinding.PopupSetRestaurentTimeBinding
import com.easyfoodvone.app_ui.databinding.PopupSetRestaurentTimePhoneBinding
import com.easyfoodvone.app_ui.fragment.RoundedDialogFragment

class ViewRestaurantTimings(
    private val data: DataPageRestaurantTimings,
    private val inflater: LayoutInflater,
    private val lifecycle: LifecycleSafe,
    private val childManager: FragmentManager,
    private val isPhone: Boolean) {

    private var ui: UIVariationRestaurantTimings<*>? = null
    private var addTimingDialog: RoundedDialogFragment? = null
    private var editTimingDialog: RoundedDialogFragment? = null

    fun getRoot(): View {
        return ui!!.binding.root
    }

    fun onCreateView(container: ViewGroup?) {
        ui = if (isPhone) {
            UIVariationRestaurantTimings.Phone(DataBindingUtil.inflate(inflater, R.layout.page_restaurant_timings_phone, container, false))
        } else {
            UIVariationRestaurantTimings.Tablet(DataBindingUtil.inflate(inflater, R.layout.page_restaurant_timings, container, false))

        }.apply {
            setData(data, lifecycle)

            data.restaurantTimings.let { list ->
                val adapter = AdapterRestaurantTimings(adapterClickListener, list, isPhone)
                lifecycle.addObserverOnceUntilDestroy(list, adapter, callNow=false)
                listTimings.setAdapter(adapter)
            }
        }

        lifecycle.apply {
            addObserverOnceUntilDestroy(
                data.showingPopupAdd,
                Observer { data: DataDialogRestaurantTime? -> if (data == null) hidePopupAddTiming() else showPopupAddTiming(data) },
                callNow=true)

            addObserverOnceUntilDestroy(
                data.showingPopupEdit,
                Observer { data: DataDialogRestaurantTime? -> if (data == null) hidePopupEditTiming() else showPopupEditTiming(data) },
                callNow=true)

            actionOnceOnDestroy {
                hidePopupAddTiming()
                hidePopupEditTiming()
            }

            setValueUntilDestroy(data.inputEvents, inputEventHandler)

            // Prevent memory leak
            nullOnDestroy(::ui)
        }
    }

    private val adapterClickListener = object : AdapterRestaurantTimings.OnAdapterItemClickListener {
        override fun onAddClick(position: Int, timings: AllDaysRestaurantTiming.Data) {
            data.outputEvents.onClickAdd(timings)
        }

        override fun onEditClick(position: Int, timings: AllDaysRestaurantTiming.Data.TimingData?) {
            data.outputEvents.onClickEdit(timings ?: return)
        }

        override fun onDeleteClick(position: Int, timings: AllDaysRestaurantTiming.Data.TimingData?) {
            data.outputEvents.onClickDelete(position, timings ?: return)
        }
    }

    private val inputEventHandler = object : DataPageRestaurantTimings.InputEvents {
        override fun showPopupDeleteConfirm(onYes: Runnable) {
            val layoutView: View = inflater.inflate(R.layout.popup_confirmation, null, false)
            val dialog = RoundedDialogFragment(layoutView, false)

            val btn_yes: TextView = layoutView.findViewById(R.id.btn_yes)
            val btn_no: TextView = layoutView.findViewById(R.id.btn_no)
            val txt_message: TextView = layoutView.findViewById(R.id.txt_message)

            txt_message.setText("Do you want to delete this time slot?")

            btn_yes.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    dialog.dismiss()

                    onYes.run()
                }
            })
            btn_no.setOnClickListener { dialog.dismiss() }

            dialog.showNow(childManager, null)
        }
    }

    private fun hidePopupAddTiming() {
        addTimingDialog?.dismissAllowingStateLoss()
        addTimingDialog = null
    }

    private fun showPopupAddTiming(data: DataDialogRestaurantTime) {
        hidePopupAddTiming()

        if (isPhone) {
            UIVariationPopupSetRestaurentTime.Phone(PopupSetRestaurentTimePhoneBinding.inflate(inflater))
        } else {
            UIVariationPopupSetRestaurentTime.Tablet(PopupSetRestaurentTimeBinding.inflate(inflater))

        }.apply {
            addTimingDialog = RoundedDialogFragment(binding.root, false)
            setData(data, LifecycleSafe(addTimingDialog!!))
        }

        addTimingDialog!!.showNow(childManager, null)
    }

    private fun hidePopupEditTiming() {
        editTimingDialog?.dismissAllowingStateLoss()
        editTimingDialog = null
    }

    private fun showPopupEditTiming(data: DataDialogRestaurantTime) {
        hidePopupEditTiming()

        if (isPhone) {
            UIVariationPopupSetRestaurentTime.Phone(PopupSetRestaurentTimePhoneBinding.inflate(inflater))
        } else {
            UIVariationPopupSetRestaurentTime.Tablet(PopupSetRestaurentTimeBinding.inflate(inflater))

        }.apply {
            editTimingDialog = RoundedDialogFragment(binding.root, false)
            setData(data, LifecycleSafe(editTimingDialog!!))
        }

        editTimingDialog!!.showNow(childManager, null)
    }
}