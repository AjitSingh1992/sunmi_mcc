package com.easyfoodvone.app_ui.view

import android.view.*
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.viewdata.DataPageOrderList
import com.easyfoodvone.app_common.viewdata.DataRowOrderOverview
import com.easyfoodvone.app_ui.R
import com.easyfoodvone.app_ui.binding.UIVariationOrderOverviewRow

class ViewOrderOverview(val lifecycle: LifecycleSafe, val isPhone: Boolean, val data: DataRowOrderOverview) {

    var ui: UIVariationOrderOverviewRow<*>? = null

    fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?) {
        ui = if (isPhone) {
                UIVariationOrderOverviewRow.Phone(DataBindingUtil.inflate(inflater, R.layout.row_order_overview_phone, parent, false))
            } else {
                UIVariationOrderOverviewRow.Tablet(DataBindingUtil.inflate(inflater, R.layout.row_order_overview_tablet, parent, false))

            }.apply {
                setData(data, lifecycle)
            }

        // Prevent memory leak
        lifecycle.nullOnDestroy(::ui)

        lifecycle.setValueUntilDestroy(data.inputEvents, this.InputEventHandler())
    }

    private inner class InputEventHandler : DataRowOrderOverview.InputEvents {
        override fun showEditAcceptedStatusChooser(options: List<Pair<String, DataPageOrderList.AcceptedOrderRequestStatus>>) {
            val anchorView = ui!!.buttonEditAcceptedStatus

            // Create a floating Popup box
            val popupMenu = PopupMenu(ui!!.binding.root.context!!, anchorView, Gravity.BOTTOM)

            // Add the multiple choice rows
            for (i in options.indices) {
                val (displayString: String, actualStatus: DataPageOrderList.AcceptedOrderRequestStatus) = options.get(i)
                val itemId = i

                popupMenu.menu.add(Menu.NONE, itemId, Menu.NONE, displayString)
            }

            // Handle the row click
            val popupOnItemClickListener = object : PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem): Boolean {
                    data.outputEvents.stopNotificationSound()

                    val (displayString: String, actualStatus: DataPageOrderList.AcceptedOrderRequestStatus) = options.get(item.itemId)
                    data.outputEvents.onClickEditAcceptedStatusEnd(actualStatus)

                    return true
                }
            }

            popupMenu.setOnMenuItemClickListener(popupOnItemClickListener)
            popupMenu.show()
        }
    }
}