package com.easyfoodvone.controller.child

import com.easyfoodvone.app_common.separation.ObservableField
import com.easyfoodvone.app_common.utility.NewConstants
import com.easyfoodvone.app_common.ws.OrdersListResponse
import com.easyfoodvone.app_common.viewdata.DataPageOrderList
import com.easyfoodvone.app_common.viewdata.DataRowOrderOverview
import com.easyfoodvone.utility.Helper
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * NOTE THAT DUE TO THE STICKY HEADER LIBRARY IN USE, HEADER VIEWS MUST NOT USE ViewHolder.getAdapterPosition()
 */
class ControllerRowOrderOverview(
    private val parentInterface: ParentInterface,
    private val activeTab: ObservableField<DataPageOrderList.ActiveTab>) {
    private var bound: OrdersListResponse.Orders? = null

    interface ParentInterface {
        fun onAcceptClick(orderDetail: OrdersListResponse.Orders)
        fun onRejectClick(orderDetail: OrdersListResponse.Orders)
        fun onSpinnerStatus(statusEnum: DataPageOrderList.AcceptedOrderRequestStatus, orderDetail: OrdersListResponse.Orders)
        fun openOrderDetail(clickedOrder: OrdersListResponse.Orders)
        fun stopNotificationSound()
    }

    val data: DataRowOrderOverview = DataRowOrderOverview(
            ObservableField(""),
            ObservableField(""),
            ObservableField(""),
            ObservableField(""),
            ObservableField(""),
            ObservableField(""),
            ObservableField(""),
            ObservableField(""),
            ObservableField(""),
            ObservableField(""),
            ObservableField(""),
            ObservableField(""),
            ObservableField(""),
            ObservableField(""),
            ObservableField(""),
            ObservableField(""),
            ObservableField(""),
            ObservableField(DataRowOrderOverview.OrderStatus.NEW),
            this.ViewEventHandler(),
            ObservableField(null))

    companion object {
        // The input is formatted like "19th April 2021 | 01:01:01"
        // but to parse it we need to remove the "th", leaving the "19" and the " "
        val DATE_REGEX_REPLACE_DAY_MONTH_SUFFIX = "(?si)(?<=(^[0-9]{2}))([a-zA-Z]+)(?=(\\s))".toRegex()

        val DATE_FORMATTER_INPUT_NO_SUFFIX = SimpleDateFormat("dd MMMM yyyy | HH:mm:ss")
        val DATE_FORMATTER_OUTPUT_TABLET_JUST_DATE = SimpleDateFormat("d MMM yyyy")
        val DATE_FORMATTER_OUTPUT_TABLET_JUST_TIME = SimpleDateFormat("HH:mm")
        val DATE_FORMATTER_OUTPUT_PHONE_JUST_DATE = SimpleDateFormat("d MMM yy")
        val DATE_FORMATTER_OUTPUT_PHONE_JUST_TIME = SimpleDateFormat("h:mm a")
    }

    private inner class ViewEventHandler : DataRowOrderOverview.OutputEvents {
        override fun bind(clickedOrder: OrdersListResponse.Orders) {
            bound = clickedOrder.also { assignFields(it) }
        }

        override fun onClickNewOrderAccept() {
            bound?.also {
                parentInterface.onAcceptClick(it)
            }
        }

        override fun onClickNewOrderReject() {
            bound?.also {
                parentInterface.onRejectClick(it)
            }
        }

        override fun onClickViewDetail() {
            bound?.also {
                parentInterface.openOrderDetail(it)
            }
        }

        override fun onClickEditAcceptedStatusBegin() {
            bound?.also {
                showEditAcceptedStatusChooser(it)
            }
        }

        override fun onClickEditAcceptedStatusEnd(clickedStatus: DataPageOrderList.AcceptedOrderRequestStatus) {
            bound?.also {
                parentInterface.onSpinnerStatus(clickedStatus, it)
            }
        }

        override fun stopNotificationSound() {
            parentInterface.stopNotificationSound()
        }
    }

    private fun assignFields(item: OrdersListResponse.Orders) {
        data.currentAcceptedStatus.set(formatCurrentAcceptedStatus(item))
        formatOrderDateTime(item).let {
            data.orderDateTablet.set(it.tabletDate)
            data.orderDatePhone.set(it.phoneDate)
            data.orderTimeTablet.set(it.tabletTime)
            data.orderTimePhone.set(it.phoneTime)
        }
        formatDeliveryDateTime(item).let {
            data.deliveryDateTablet.set(it.tabletDate)
            data.deliveryDatePhone.set(it.phoneDate)
            data.deliveryTimeTablet.set(it.tabletTime)
            data.deliveryTimePhone.set(it.phoneTime)
        }
        data.deliveryTypeTablet.set(formatDeliveryType(item, true))
        data.deliveryTypePhone.set(formatDeliveryType(item, false))
        data.orderID.set(formatOrderId(item))
        data.customerName.set(item.getCustomer_name())
        data.previousOrder.set(formatPrevOrder(item))
        data.orderAmount.set(NewConstants.POUND + item.getOrder_total())
        data.paymentType.set(formatPaymentType(item))
        data.customerAddress.set(formatAddress(item))
        data.status.set(convertStatus(activeTab.get()))
    }

    private fun convertStatus(activeTab: DataPageOrderList.ActiveTab): DataRowOrderOverview.OrderStatus {
        return when (activeTab) {
            DataPageOrderList.ActiveTab.NEW -> DataRowOrderOverview.OrderStatus.NEW
            DataPageOrderList.ActiveTab.ACCEPTED -> DataRowOrderOverview.OrderStatus.ACCEPTED
            DataPageOrderList.ActiveTab.REJECTED -> DataRowOrderOverview.OrderStatus.REJECTED
            DataPageOrderList.ActiveTab.REFUNDED -> DataRowOrderOverview.OrderStatus.REFUNDED
        }
    }

    private fun showEditAcceptedStatusChooser(item: OrdersListResponse.Orders) {
        val itemDeliveryOption: String? = item.getDelivery_option()
        val toDisplay: ArrayList<Pair<String, DataPageOrderList.AcceptedOrderRequestStatus>> = ArrayList()

        if (itemDeliveryOption.equals("Collection", ignoreCase = true)) {
            toDisplay.add(Pair("Preparing", DataPageOrderList.AcceptedOrderRequestStatus.PREPARING))
            toDisplay.add(Pair("Ready to collect", DataPageOrderList.AcceptedOrderRequestStatus.OUT_FOR_DELIVERY))
            toDisplay.add(Pair("Collected", DataPageOrderList.AcceptedOrderRequestStatus.DELIVERED))

        } else if (itemDeliveryOption.equals("Table", ignoreCase = true)) {
            toDisplay.add(Pair("Preparing", DataPageOrderList.AcceptedOrderRequestStatus.PREPARING))
            toDisplay.add(Pair("Served", DataPageOrderList.AcceptedOrderRequestStatus.SERVED))

        } else {
            toDisplay.add(Pair("Preparing", DataPageOrderList.AcceptedOrderRequestStatus.PREPARING))
            toDisplay.add(Pair("On the way", DataPageOrderList.AcceptedOrderRequestStatus.OUT_FOR_DELIVERY))
            toDisplay.add(Pair("Delivered", DataPageOrderList.AcceptedOrderRequestStatus.DELIVERED))
        }

        data.inputEvents.get()?.showEditAcceptedStatusChooser(toDisplay)
    }

    private fun formatCurrentAcceptedStatus(item: OrdersListResponse.Orders): String {
        if (activeTab.get() != DataPageOrderList.ActiveTab.ACCEPTED) {
            return ""
        }

        val itemDeliveryOption: String? = item.getDelivery_option()
        val itemOrderStatus: String? = item.getOrder_status()

        if (itemDeliveryOption.equals("Collection", true)) {

            return if (itemOrderStatus.equals("Accepted", true)) {
                "Accepted"
            } else if (itemOrderStatus.equals("Preparing", true)) {
                "Preparing"
            } else if (itemOrderStatus.equals("On the way", true)) {
                "Ready to collect"
            } else if (itemOrderStatus.equals("Delivered", true)) {
                "Collected"
            } else {
                "Accepted"
            }

        } else if (itemDeliveryOption.equals("Table", true)) {

            return if (itemOrderStatus.equals("Accepted", true)) {
                "Accepted"
            } else if (itemOrderStatus.equals("Preparing", true)) {
                "Preparing"
            } else if (itemOrderStatus.equals("Served", true)) {
                "Served"
            } else {
                "Accepted"
            }

        } else {

            return if (itemOrderStatus.equals("Accepted", true)) {
                "Accepted"
            } else if (itemOrderStatus.equals("Preparing", true)) {
                "Preparing"
            } else if (itemOrderStatus.equals("On the way", true)) {
                "On the way"
            } else if (itemOrderStatus.equals("Delivered", true)) {
                "Delivered"
            } else {
                "Accepted"
            }
        }
    }

    private data class FormattedDateTime(
        val tabletDate: String,
        val tabletTime: String,
        val phoneDate: String,
        val phoneTime: String)

    private fun formatOrderDateTime(item: OrdersListResponse.Orders): FormattedDateTime {
        val dateTime: String? = item.getOrder_date_time()

        if (dateTime == null) {
            return FormattedDateTime("", "", "", "")

        } else {
            val parseableDateTime = dateTime.trim().replace(DATE_REGEX_REPLACE_DAY_MONTH_SUFFIX, "")

            val parsed: Date?
            try {
                parsed = DATE_FORMATTER_INPUT_NO_SUFFIX.parse(parseableDateTime)
            } catch (e: ParseException) {
                return FormattedDateTime(dateTime, "", dateTime, "")
            }

            if (parsed == null) {
                return FormattedDateTime(dateTime, "", dateTime, "")
            }

            return FormattedDateTime(
                DATE_FORMATTER_OUTPUT_TABLET_JUST_DATE.format(parsed),
                DATE_FORMATTER_OUTPUT_TABLET_JUST_TIME.format(parsed),
                DATE_FORMATTER_OUTPUT_PHONE_JUST_DATE.format(parsed),
                DATE_FORMATTER_OUTPUT_PHONE_JUST_TIME.format(parsed))
        }
    }

    private fun formatDeliveryDateTime(item: OrdersListResponse.Orders): FormattedDateTime {
        val dateTime: String? = item.getDelivery_date_time()

        if (dateTime == null || dateTime.isEmpty()) {
            return FormattedDateTime("", "", "", "")

        } else {
            val parsed: Date? = Helper.parseDeliveryDate(dateTime)

            if (parsed == null) {
                return FormattedDateTime(dateTime, "", dateTime, "")
            } else {
                return FormattedDateTime(
                    DATE_FORMATTER_OUTPUT_TABLET_JUST_DATE.format(parsed),
                    DATE_FORMATTER_OUTPUT_TABLET_JUST_TIME.format(parsed),
                    DATE_FORMATTER_OUTPUT_PHONE_JUST_DATE.format(parsed),
                    DATE_FORMATTER_OUTPUT_PHONE_JUST_TIME.format(parsed))
            }
        }
    }

    private fun formatDeliveryType(item: OrdersListResponse.Orders, isTablet: Boolean): String {
        val option: String? = item.getDelivery_option()?.trim()

        if (option.isNullOrEmpty() || option.equals("0")) {
            return ""
        } else if (option.equals("table", ignoreCase = true)) {
            return if (isTablet) "Table" else "TABLE"
        } else if (option.equals("collection", ignoreCase = true)) {
            return if (isTablet) "Collection" else "COLLECT"
        } else if (option.equals("delivery", ignoreCase = true)) {
            return if (isTablet) "Delivery" else "DELIVER"
        } else {
            // Unknown type, limit the max length to 7 so it will fit in the boxes
            val endOrFirst7 = Math.min(option.length, 7)
            if (isTablet) {
                return option.substring(0, 1).uppercase() + option.substring(1, endOrFirst7).lowercase()
            } else {
                return option.substring(0, endOrFirst7).uppercase()
            }
        }
    }

    private fun formatPaymentType(item: OrdersListResponse.Orders): String {
        val start: String = item.getPayment_mode()
        val middle: String = if (item.getIs_preorder().isNullOrEmpty()) "" else " Pre"

        return start + middle
    }

    private fun formatOrderId(item: OrdersListResponse.Orders): String {
        val num: String? = item.getOrder_num()
        if (num == null || num.length <= 8) {
            return ""
        } else {
            return num.substring(num.length - 8)
                    .replace("-", "")
        }
    }

    private fun formatPrevOrder(item: OrdersListResponse.Orders): String {
        val prev: String? = item.getPrev_order()
        if (prev == null || prev.equals("0") || prev.equals("")) {
            return "New"
        } else {
            return prev
        }
    }

    private fun formatAddress(item: OrdersListResponse.Orders): String {
        val location: String? = item.getCustomer_location()
        if (location != null && location.trim().isNotEmpty()) {
            return location.trim()
        } else {
            return "N/A"
        }
    }
}