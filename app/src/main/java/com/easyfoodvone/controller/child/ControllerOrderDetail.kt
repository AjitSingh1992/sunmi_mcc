package com.easyfoodvone.controller.child

import android.text.TextUtils
import androidx.databinding.ObservableArrayList
import com.easyfoodvone.app_common.separation.ObservableField
import com.easyfoodvone.app_common.utility.NewConstants
import com.easyfoodvone.app_common.viewdata.DataRowOrderDetail
import com.easyfoodvone.app_common.ws.NewDetailBean
import com.easyfoodvone.app_common.ws.OrdersListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ControllerOrderDetail(
        private val parentInterface: ParentInterface) {

    private var orderOverview: OrdersListResponse.Orders? = null
    private var orderDetails: NewDetailBean.OrdersDetailsBean? = null
    private var wsRequest: CancellableRequest? = null

    interface ParentInterface {
        fun callOrderDetail(orderNumber: String): Call<NewDetailBean>
        fun print(orderDetail: NewDetailBean.OrdersDetailsBean)
    }

    val data = DataRowOrderDetail(
            ObservableField(false),
            ObservableField(false),
            ObservableArrayList(),
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
            this.ViewEventHandler())

    private inner class ViewEventHandler : DataRowOrderDetail.OutputEvents {
        override fun bind(clickedOrder: OrdersListResponse.Orders) {
            wsRequest?.disposeNow()
            setLoaded(null)

            orderOverview = clickedOrder
        }

        override fun toggleVisible() {
            if (data.isExpanded.get()) {
                // Collapse
                wsRequest?.disposeNow()
                setLoaded(null)

            } else {
                // Expand (or continue loading)
                orderOverview?.getOrder_num()?.let { loadToExpandOrderDetails(it) }
            }
        }

        override fun onClickPrint() {
            orderDetails?.let { parentInterface.print(it) }
        }
    }

    private fun setLoaded(detail: NewDetailBean.OrdersDetailsBean?) {
        orderDetails = detail

        data.isExpanded.set(detail != null)

        data.itemRows.clear()
        data.itemRows.addAll(detail?.getCart()?.let { createMenuItems(it) } ?: emptyList())

        val isDeliveryOptionTable: Boolean = detail?.getDelivery_option()?.trim().equals("table", ignoreCase = true)
        val orderPhoneNumber: String? = detail?.getOrder_phone_number()
        val deliveryAddress: NewDetailBean.OrdersDetailsBean.DeliveryAddress? = detail?.getDelivery_address()

        data.gbpSubTotal.set(detail?.getSub_total()?.let { NewConstants.POUND + String.format("%.2f", it) } ?: "")
        data.gbpDeliveryCharges.set(detail?.getDelivery_charge()?.let { NewConstants.POUND + it } ?: "")
        data.gbpDiscount.set(detail?.getDiscount_amount()?.let { NewConstants.POUND + it } ?: "")
        data.gbpTotal.set(detail?.getOrder_total()?.let { NewConstants.POUND + it } ?: "")
        data.notes.set(detail?.let { it.getOrder_notes()?.trim().let { if (TextUtils.isEmpty(it)) "-" else it } ?: "-" } ?: "")
        data.deliveryTableNumber.set(detail?.getUnitId()?.let { if (isDeliveryOptionTable) it else ""} ?: "")
        data.deliveryRequestedDateTime.set(detail?.getDelivery_date_time()?.let { formatDate(it) } ?: "")
        data.deliveryCustomerName.set(deliveryAddress?.getCustomer_name() ?: "")
        data.deliveryCustomerPhone.set(if (orderPhoneNumber.isNullOrEmpty()) (deliveryAddress?.getPhone_number() ?: "") else orderPhoneNumber)
        data.deliveryCustomerAddress.set(deliveryAddress?.getCustomer_location() ?: "")
        data.barcodeCodabarFormat.set(detail?.getOrder_num() ?: "")

        //TODO: Open dialer on click the phone icon...
    }

    private inner class CancellableRequest {
        var terminated: Boolean = false

        fun disposeNow() {
            if ( ! terminated) {
                terminated = true
                wsRequest = null

                endUI()
            }
        }

        fun beginUI() {
            data.loading.set(true)
        }

        fun endUI() {
            data.loading.set(false)
        }
    }

    private fun loadToExpandOrderDetails(orderNumber: String) {
        if (wsRequest != null) {
            return
        }

        val call: Call<NewDetailBean> = parentInterface.callOrderDetail(orderNumber)

        val requestAtStart = CancellableRequest()
        wsRequest = requestAtStart
        wsRequest?.beginUI()

        call.enqueue(object : Callback<NewDetailBean> {
            override fun onResponse(call: Call<NewDetailBean>, response: Response<NewDetailBean>) {
                if (requestAtStart.terminated) {
                    return
                }

                try {
                    if (response.isSuccessful()) {
                        val serverSuccess = response.body()?.isSuccess() ?: false
                        if (serverSuccess) {
                            val orderDetails: NewDetailBean.OrdersDetailsBean? = response.body()?.getOrders_details()
                            if (orderDetails != null) {
                                setLoaded(orderDetails)

                                requestAtStart.disposeNow()

                            } else {
                                onFailure(call, Exception("Malformed response"))
                            }

                        } else {
                            onFailure(call, Exception("Unsuccessful server response"))
                        }

                    } else {
                        onFailure(call, Exception("Unsuccessful request"))
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    onFailure(call, e)
                }
            }

            override fun onFailure(call: Call<NewDetailBean>, t: Throwable) {
                requestAtStart.disposeNow()
            }
        })
    }

    private fun formatDate(inputDate: String): String? {
        // With dealInfo.getDealStartDate() You will get date object relative to server/client timezone wherever it is parsed?

        val inputFormat = SimpleDateFormat("dd MMM yyyy HH:mm:ss")
        val date: Date
        try {
            date = inputFormat.parse(inputDate) ?: return inputDate
        } catch (e: ParseException) {
            e.printStackTrace()
            return null
        }

        val outputFormat = SimpleDateFormat("dd MMM yyyy HH:mm")
        return outputFormat.format(date)
    }

    private fun createMenuItems(cart: NewDetailBean.OrdersDetailsBean.Cart): LinkedList<DataRowOrderDetail.ItemRow> {
        val out: LinkedList<DataRowOrderDetail.ItemRow> = LinkedList()

        for (menuRow: NewDetailBean.OrdersDetailsBean.Cart.MenuBean? in cart.getMenu() ?: emptyList()) {
            if (menuRow == null) {
                continue
            }

            out.add(createItemLevel0(menuRow))
            out.addAll(menuRow.getOptions()?.getProductModifiers()?.let { createItemsLevel1Modifier(it) } ?: emptyList())
            out.addAll(menuRow.getOptions()?.getMealProducts()?.let { createItemsLevel1Meal(it) } ?: emptyList())
            out.addAll(menuRow.getOptions()?.getSize()?.let { if (it.getProductSizeName() != null) createItemsLevel12Size(it) else emptyList() } ?: emptyList())
        }

        return out
    }

    private fun createItemLevel0(menuRow: NewDetailBean.OrdersDetailsBean.Cart.MenuBean): DataRowOrderDetail.ItemRow {
        return DataRowOrderDetail.ItemRow(
                0,
                "" + menuRow.getQty(),
                menuRow.getName().trim(),
                NewConstants.POUND + String.format("%.2f", menuRow.getPrice()),
                NewConstants.POUND + String.format("%.2f", menuRow.getSubtotal()))
    }

    private fun createItemsLevel1Modifier(outerModifierList: List<NewDetailBean.OrdersDetailsBean.Cart.MenuBean.OptionsBean.ProductModifiersBean>): List<DataRowOrderDetail.ItemRow> {
        val out: LinkedList<DataRowOrderDetail.ItemRow> = LinkedList()

        for (outerModifier: NewDetailBean.OrdersDetailsBean.Cart.MenuBean.OptionsBean.ProductModifiersBean? in outerModifierList) {
            if (outerModifier == null) {
                continue
            }

            val innerModifierList: List<NewDetailBean.OrdersDetailsBean.Cart.MenuBean.OptionsBean.ProductModifiersBean.ModifierProductsBean?>?
                    = outerModifier.getModifierProducts()
            if (innerModifierList == null) {
                continue
            }

            for (innerModifier: NewDetailBean.OrdersDetailsBean.Cart.MenuBean.OptionsBean.ProductModifiersBean.ModifierProductsBean? in innerModifierList) {
                if (innerModifier == null) {
                    continue
                }

                out.add(DataRowOrderDetail.ItemRow(
                        1,
                        innerModifier.getQuantity() ?: "0",
                        innerModifier.getProductName()?.trim() ?: "",
                        "",
                        innerModifier.getModifierProductPrice()?.let { NewConstants.POUND + it } ?: "£0.00"))
            }
        }

        return out
    }

    private fun createItemsLevel1Meal(mealProductsList: List<NewDetailBean.OrdersDetailsBean.Cart.MenuBean.OptionsBean.MealProduct>): List<DataRowOrderDetail.ItemRow> {
        val out: LinkedList<DataRowOrderDetail.ItemRow> = LinkedList()

        for (outerProduct: NewDetailBean.OrdersDetailsBean.Cart.MenuBean.OptionsBean.MealProduct? in mealProductsList) {
            if (outerProduct == null) {
                continue
            }

            val productNameApp = outerProduct.getProductNameAPP()?.trim() ?: ""
            if (productNameApp.isNotEmpty()) {
                out.add(DataRowOrderDetail.ItemRow(
                    1,
                    outerProduct.getQuantity()?.toString() ?: "0",
                    productNameApp,
                    "",
                    outerProduct.getAmount().let { NewConstants.POUND + String.format("%.2f", it?.toDoubleOrNull() ?: 0.0) }))
            }

            val sizeModifierList: List<NewDetailBean.OrdersDetailsBean.Cart.MenuBean.OptionsBean.MealProduct.SizeModifier?>?
                    = outerProduct.getSizeModifiers()
            if (sizeModifierList == null) {
                continue
            }

            for (sizeModifier: NewDetailBean.OrdersDetailsBean.Cart.MenuBean.OptionsBean.MealProduct.SizeModifier? in sizeModifierList) {
                if (sizeModifier == null) {
                    continue
                }

                val sizeItemList: List<NewDetailBean.OrdersDetailsBean.Cart.MenuBean.OptionsBean.MealProduct.SizeModifier.SizeModifierProducts?>?
                        = sizeModifier.getSizeModifierProducts()
                if (sizeItemList == null) {
                    continue
                }

                for (sizeItem: NewDetailBean.OrdersDetailsBean.Cart.MenuBean.OptionsBean.MealProduct.SizeModifier.SizeModifierProducts? in sizeItemList) {
                    if (sizeItem == null) {
                        continue
                    }

                    out.add(DataRowOrderDetail.ItemRow(
                            1,
                            sizeItem.getQuantity()?.toString() ?: "0",
                            sizeItem.getProductName()?.trim() ?: "",
                            "",
                            sizeItem.getAmount().let { NewConstants.POUND + String.format("%.2f", it?.toDoubleOrNull() ?: 0.0) }))
                }
            }
        }

        return out
    }

    private fun createItemsLevel12Size(sizeChoice: NewDetailBean.OrdersDetailsBean.Cart.MenuBean.OptionsBean.Size): List<DataRowOrderDetail.ItemRow> {
        val out: LinkedList<DataRowOrderDetail.ItemRow> = LinkedList()

        out.add(DataRowOrderDetail.ItemRow(
                1,
                sizeChoice.getQuantity()?.toString() ?: "0",
                sizeChoice.getProductSizeName()?.trim() ?: "",
                "",
                sizeChoice.getProductSizePrice().let { NewConstants.POUND + String.format("%.2f", it ?: 0) }))

        val sizeModifierList: List<NewDetailBean.OrdersDetailsBean.Cart.MenuBean.OptionsBean.Size.Sizemodifier?>?
                = sizeChoice.getSizemodifiers()
        if (sizeModifierList == null) {
            return out
        }

        for (sizeModifier: NewDetailBean.OrdersDetailsBean.Cart.MenuBean.OptionsBean.Size.Sizemodifier? in sizeModifierList) {
            if (sizeModifier == null) {
                continue
            }

            val productsList: List<NewDetailBean.OrdersDetailsBean.Cart.MenuBean.OptionsBean.Size.Sizemodifier.SizeModifierProduct?>?
                    = sizeModifier.getSizeModifierProducts()
            if (productsList == null) {
                continue
            }

            for (product: NewDetailBean.OrdersDetailsBean.Cart.MenuBean.OptionsBean.Size.Sizemodifier.SizeModifierProduct? in productsList) {
                if (product == null) {
                    continue
                }

                out.add(DataRowOrderDetail.ItemRow(
                        2,
                        product.getQuantity() ?: "0",
                        product.getProductName()?.trim() ?: "",
                        "",
                        product.getAmount().let { NewConstants.POUND + String.format("%.2f", it ?: 0) }))
            }
        }

        return out
    }
}