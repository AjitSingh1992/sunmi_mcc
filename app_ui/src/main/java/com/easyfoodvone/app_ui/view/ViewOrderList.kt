package com.easyfoodvone.app_ui.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableList
import androidx.databinding.OnRebindCallback
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.brandongogetap.stickyheaders.StickyLayoutManager
import com.brandongogetap.stickyheaders.exposed.StickyHeader
import com.brandongogetap.stickyheaders.exposed.StickyHeaderHandler
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.viewdata.DataPageOrderList
import com.easyfoodvone.app_common.viewdata.DataRowOrderDetail
import com.easyfoodvone.app_common.viewdata.DataRowOrderOverview
import com.easyfoodvone.app_common.ws.OrdersListResponse
import com.easyfoodvone.app_ui.R
import com.easyfoodvone.app_ui.binding.UIVariationOrderList

class ViewOrderList(val lifecycle: LifecycleSafe, val isPhone: Boolean) {

    var ui: UIVariationOrderList<*>? = null

    private var data: DataPageOrderList? = null
    private var adapter: OverviewAdapter? = null

    fun onCreateView(data: DataPageOrderList, inflater: LayoutInflater, container: ViewGroup?) {
        this.data = data

        ui = if (isPhone) {
                UIVariationOrderList.Phone(DataBindingUtil.inflate(inflater, R.layout.page_order_list_phone, container, false))
            } else {
                UIVariationOrderList.Tablet(DataBindingUtil.inflate(inflater, R.layout.page_order_list_tablet, container, false))

            }.apply {
                adapter = OverviewAdapter(data, lifecycle, inflater, isPhone)

                setData(data, lifecycle)

                binding.addOnRebindCallback(onRebindCallback)

                swipeRefresh.setOnRefreshListener({ data.outputEvents.onSwipeRefresh() })

                // https://github.com/bgogetap/StickyHeaders
                // layoutManager.setStickyHeaderListener
                recyclerOrdersList.layoutManager = StickyLayoutManager(binding.root.context, StickyHeaderHandlerImpl(adapter!!))

                recyclerOrdersList.setAdapter(adapter)
            }

        // Prevent memory leak
        lifecycle.nullOnDestroy(::ui)

        lifecycle.addObserverOnceUntilDestroy(data.isSwipeRefreshing, invalidateSwipeOnChange, true)
        lifecycle.setValueUntilDestroy(data.inputEvents, inputEventHandler)
    }

    private val invalidateSwipeOnChange = object : Observer<Boolean> {
        override fun onChanged(isSwipeRefreshing: Boolean) {
            ui!!.swipeRefresh.setRefreshing(isSwipeRefreshing)
        }
    }

    private val onRebindCallback = object : OnRebindCallback<ViewDataBinding>() {
        override fun onPreBind(untypedBinding: ViewDataBinding?): Boolean {
            return true
        }
    }

    private val inputEventHandler = object : DataPageOrderList.InputEvents {
        override fun scrollToOverviewOf(clickedOrder: OrdersListResponse.Orders) {
            val index: Int = data?.ordersList?.indexOf(clickedOrder) ?: return
            val overviewIndex = index * 2
            val detailIndex = overviewIndex + 1

            val linearSmoothScroller = object : LinearSmoothScroller(ui!!.recyclerOrdersList.getContext()) {
                override fun getVerticalSnapPreference(): Int {
                    return SNAP_TO_START;
                }
            }
            linearSmoothScroller.targetPosition = overviewIndex

            val layoutManager = (ui!!.recyclerOrdersList.layoutManager as StickyLayoutManager)
            layoutManager.startSmoothScroll(linearSmoothScroller)
        }

        override fun findDetailFor(clickedOrder: OrdersListResponse.Orders): DataRowOrderDetail? {
            val index: Int = data?.ordersList?.indexOf(clickedOrder) ?: return null
            val detailIndex = (index * 2) + 1
            val viewHolder: RecyclerView.ViewHolder? = ui!!.recyclerOrdersList.findViewHolderForAdapterPosition(detailIndex)
            return (viewHolder as? OrderItemHolder.Detail)?.view?.data
        }
    }

    private class OverviewAdapter(
            private val data: DataPageOrderList,
            private val lifecycle: LifecycleSafe,
            private val inflater: LayoutInflater,
            private val isPhone: Boolean)
        : RecyclerView.Adapter<OrderItemHolder>() {

        // It would be simpler to use ObservableUtils.newAdapterListenerForObservableList if we didn't
        // dynamically adjust the size of this adapter to be twice that of the data.
        // Each item becomes two rows: 1. overview 2. detail
        private val onListChangedCallback
                = object : ObservableList.OnListChangedCallback<ObservableList<OrdersListResponse.Orders>>() {
            override fun onChanged(sender: ObservableList<OrdersListResponse.Orders>) {
                this@OverviewAdapter.notifyDataSetChanged()
            }

            override fun onItemRangeChanged(sender: ObservableList<OrdersListResponse.Orders>, positionStart: Int, itemCount: Int) {
                this@OverviewAdapter.notifyItemRangeChanged(positionStart, itemCount * 2)
            }

            override fun onItemRangeInserted(sender: ObservableList<OrdersListResponse.Orders>, positionStart: Int, itemCount: Int) {
                this@OverviewAdapter.notifyItemRangeInserted(positionStart, itemCount * 2)
            }

            override fun onItemRangeMoved(sender: ObservableList<OrdersListResponse.Orders>, fromPosition: Int, toPosition: Int, itemCount: Int) {
                this@OverviewAdapter.notifyDataSetChanged()
            }

            override fun onItemRangeRemoved(sender: ObservableList<OrdersListResponse.Orders>, positionStart: Int, itemCount: Int) {
                this@OverviewAdapter.notifyItemRangeRemoved(positionStart, itemCount * 2)
            }
        }

        init {
            // todo if activetab changes, explicitly refresh all list items
            lifecycle.addObserverOnceUntilDestroy(this.data.ordersList, onListChangedCallback, callNow=false)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderItemHolder {
            val holder: OrderItemHolder
            if (viewType == 0) {
                val rowData: DataRowOrderOverview = data.outputEvents.newRowPresenterOverview()
                val view = ViewOrderOverview(lifecycle, isPhone, rowData).apply {
                    onCreateView(inflater, parent)
                }
                holder = OrderItemHolder.Overview(view)

            } else if (viewType == 1) {
                val rowData: DataRowOrderDetail = data.outputEvents.newRowPresenterDetail()
                val view = ViewOrderDetail(lifecycle, isPhone, rowData).apply {
                    onCreateView(inflater, parent)
                }
                holder = OrderItemHolder.Detail(view)

            } else {
                throw IllegalArgumentException("Unexpected viewType")
            }

            return holder
        }

        override fun onBindViewHolder(holder: OrderItemHolder, positionForNow: Int) {
            val clicked: OrdersListResponse.Orders = data.ordersList.get(positionForNow / 2)

            when (holder) {
                is OrderItemHolder.Overview -> holder.view.data.outputEvents.bind(clicked)
                is OrderItemHolder.Detail -> holder.view.data.outputEvents.bind(clicked)
            }

            holder.untypedBinding.executePendingBindings()
        }

        override fun getItemViewType(position: Int): Int {
            return position % 2
        }

        override fun getItemCount(): Int {
            return data.ordersList.size * 2
        }
    }

    private sealed class OrderItemHolder(val untypedBinding: ViewDataBinding) : RecyclerView.ViewHolder(untypedBinding.root) {
        data class Overview(val view: ViewOrderOverview) : OrderItemHolder(view.ui!!.binding)
        data class Detail(val view: ViewOrderDetail) : OrderItemHolder(view.ui!!.binding)
    }

    /**
     * Create a fake list because we don't have a single data list variable matching our adapter
     * size which is appropriate for StickyHeaderHandler
     */
    private class StickyHeaderHandlerImpl(val adapter: OverviewAdapter): StickyHeaderHandler {
        override fun getAdapterData(): List<*> {
            return object : List<Any> {
                override val size: Int
                    get() = adapter.itemCount

                val itemForHeader = object : StickyHeader { }
                val itemForContent = object : Any() { }

                override fun get(index: Int): Any {
                    if (adapter.getItemViewType(index) == 0) {
                        return itemForHeader
                    } else {
                        return itemForContent
                    }
                }

                private fun notUsed(): IllegalStateException {
                    return IllegalStateException("Not needed by StickyLayoutManager")
                }

                override fun isEmpty(): Boolean { throw notUsed() }
                override fun contains(element: Any): Boolean { throw notUsed() }
                override fun containsAll(elements: Collection<Any>): Boolean { throw notUsed() }
                override fun subList(fromIndex: Int, toIndex: Int): List<Any> { throw notUsed() }
                override fun indexOf(element: Any): Int { throw notUsed() }
                override fun lastIndexOf(element: Any): Int { throw notUsed() }
                override fun iterator(): Iterator<Any> { throw notUsed() }
                override fun listIterator(): ListIterator<Any> { throw notUsed() }
                override fun listIterator(index: Int): ListIterator<Any> { throw notUsed() }
            }
        }
    }
}