package com.easyfoodvone.app_common.viewdata

import androidx.databinding.ObservableArrayList
import com.easyfoodvone.app_common.separation.ObservableField
import com.easyfoodvone.app_common.ws.OrdersListResponse

class DataPageOrderList(
        val isSwipeRefreshing: ObservableField<Boolean>,
        val activeTab: ObservableField<ActiveTab>,
        val tabNewCount: ObservableField<Int?>,
        val tabAcceptedCount: ObservableField<Int?>,
        val tabRejectedCount: ObservableField<Int?>,
        val tabRefundedCount: ObservableField<Int?>,
        val ordersList: ObservableArrayList<OrdersListResponse.Orders>,
        val outputEvents: OutputEvents,
        val inputEvents: ObservableField<InputEvents?>) {

    interface OutputEvents {
        fun onSwipeRefresh()
        fun onClickTabSelector(tab: ActiveTab)
        fun newRowPresenterOverview(): DataRowOrderOverview
        fun newRowPresenterDetail(): DataRowOrderDetail
    }

    interface InputEvents {
        fun scrollToOverviewOf(clickedOrder: OrdersListResponse.Orders)
        fun findDetailFor(clickedOrder: OrdersListResponse.Orders): DataRowOrderDetail?
    }

    enum class ActiveTab {
        NEW,
        ACCEPTED,
        REJECTED,
        REFUNDED
    }

    enum class AcceptedOrderRequestStatus {
        PREPARING,
        OUT_FOR_DELIVERY,
        DELIVERED,
        SERVED
    }

    fun setActiveTab(tab: ActiveTab) {
        activeTab.set(tab)
        setOrdersList(emptyList(), null, null, null,null)
    }

    fun setOrdersList(newItems: List<OrdersListResponse.Orders?>?, countNew: Int?, countAccepted: Int?, countRejected: Int?, countRefunded: Int?) {
        val filtered = newItems?.filterNotNull() ?: emptyList()
//If we want to implement 1k functionality we have to do implement code here
        tabNewCount.set(countNew)
        tabAcceptedCount.set(countAccepted)
        tabRejectedCount.set(countRejected)
        tabRefundedCount.set(countRefunded)

        ordersList.clear()
        if (filtered.isNotEmpty()) {
            ordersList.addAll(filtered)
        }
    }

    fun moveOrderItem(toMove: OrdersListResponse.Orders, moveFromTab: ActiveTab, moveToTab: ActiveTab) {
        if (activeTab.get() != moveFromTab) {
            return
        }

        val removed: Boolean = ordersList.remove(toMove)
        if (removed) {
            moveCount(moveFromTab, moveToTab)
        }
    }

    private fun moveCount(moveFromTab: ActiveTab, moveToTab: ActiveTab) {
        val fromObservable: ObservableField<Int?> = when (moveFromTab) {
            ActiveTab.NEW -> tabNewCount
            ActiveTab.ACCEPTED -> tabAcceptedCount
            ActiveTab.REJECTED -> tabRejectedCount
            ActiveTab.REFUNDED -> tabRefundedCount
        }

        val toObservable: ObservableField<Int?> = when (moveToTab) {
            ActiveTab.NEW -> tabNewCount
            ActiveTab.ACCEPTED -> tabAcceptedCount
            ActiveTab.REJECTED -> tabRejectedCount
            ActiveTab.REFUNDED -> tabRefundedCount
        }

        fromObservable.let { it.set(it.get()?.let { it-1 } ?: null) }
        toObservable.let { it.set(it.get()?.let { it+1 } ?: null) }
    }
}