package com.easyfoodvone.app_ui.view

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.viewdata.DataPageRestaurantMenuDetails
import com.easyfoodvone.app_common.ws.MenuCategoryItemsResponse
import com.easyfoodvone.app_ui.R
import com.easyfoodvone.app_ui.adapter.AdapterItemsList
import com.easyfoodvone.app_ui.adapter.AdapterMealItemsList
import com.easyfoodvone.app_ui.adapter.ItemTouchHelperAdapter
import com.easyfoodvone.app_ui.adapter.SimpleItemTouchHelperCallback
import com.easyfoodvone.app_ui.binding.UIVariationRestaurantMenuDetails
import com.easyfoodvone.app_ui.databinding.PageRestaurantMenuDetailsBinding
import com.easyfoodvone.app_ui.databinding.PageRestaurantMenuDetailsPhoneBinding

class ViewRestaurantMenuDetail(val lifecycle: LifecycleSafe, val isPhone: Boolean, val data: DataPageRestaurantMenuDetails,val isMeal:Boolean) {

    private var ui: UIVariationRestaurantMenuDetails<*>? = null
    private var itemTouchHelper: ItemTouchHelper? = null

    fun getRoot(): View {
        return ui!!.binding.root
    }

    @IdRes
    fun getChildFragmentLayoutId(): Int {
        return ui!!.childFragmentLayout.getId()
    }

    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?) {
        ui = if (isPhone) {
            UIVariationRestaurantMenuDetails.Phone(
                PageRestaurantMenuDetailsPhoneBinding.inflate(inflater, container, false))
        } else {
            UIVariationRestaurantMenuDetails.Tablet(
                PageRestaurantMenuDetailsBinding.inflate(inflater, container, false))

        }.apply {
            setData(data, Formatter(), lifecycle)
            if(!isMeal) {
                val adapter = AdapterItemsList(
                    lifecycle,
                    adapterParentInterface,
                    inflater,
                    data.menuItems,
                    data.categoryRow.toggleIsOn,
                    isPhone
                )
                recycler.setAdapter(adapter)
                lifecycle.addObserverOnceUntilDestroy(data.menuItems, adapter, callNow=false)

            }else{
                val adapter = AdapterMealItemsList(
                    lifecycle,
                    adapterMealParentInterface,
                    inflater,
                    data.mealItems,
                    data.categoryRow.toggleIsOn,
                    isPhone
                )
                recycler.setAdapter(adapter)
                lifecycle.addObserverOnceUntilDestroy(data.mealItems, adapter, callNow=false)

            }

            itemTouchHelper = ItemTouchHelper(
                object : SimpleItemTouchHelperCallback(touchHelperItemCallback) {
                    override fun isLongPressDragEnabled(): Boolean {
                        // We use a "drag handle" on each item view, disable the automatic full-item drag target
                        return false
                    }

                }).apply {
                    attachToRecyclerView(recycler)
                }
        }

        lifecycle.nullOnDestroy(::ui)
        lifecycle.nullOnDestroy(::itemTouchHelper)
    }

    private val touchHelperItemCallback = object : ItemTouchHelperAdapter {
        override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
            data.outputEvents.onItemMove(fromPosition, toPosition)

            return true
        }

        override fun onItemMoveDone(): Boolean {
            data.outputEvents.onItemMoveDone()
            return true;
        }

        override fun onItemDismiss(position: Int) { }
    }

    private val adapterParentInterface = object : AdapterItemsList.ParentInterface {
        override fun onSetProductActive(item: MenuCategoryItemsResponse.Items?, isActive: Boolean) {
            data.outputEvents.onSetProductActive(item ?: return, isActive)
        }

        override fun onEditClicked(items: MenuCategoryItemsResponse.Items?) {
            data.outputEvents.onEditClicked(items ?: return)
        }

        override fun showToast(message: String) {
            Toast.makeText(getRoot().context, message, Toast.LENGTH_SHORT).show();
        }

        override fun startReorderDrag(item: RecyclerView.ViewHolder) {
            itemTouchHelper?.startDrag(item)
        }
    }

    private val adapterMealParentInterface = object : AdapterMealItemsList.ParentInterface {
        override fun onSetProductActive(item: MenuCategoryItemsResponse.Meals?, isActive: Boolean) {
            data.outputEvents.onSetMealProductActive(item ?: return, isActive)
        }

        override fun onEditClicked(items: MenuCategoryItemsResponse.Meals?) {
            data.outputEvents.onMealEditClicked(items ?: return)
        }

        override fun showToast(message: String) {
            Toast.makeText(getRoot().context, message, Toast.LENGTH_SHORT).show();
        }

        override fun startReorderDrag(item: RecyclerView.ViewHolder) {
            itemTouchHelper?.startDrag(item)
        }
    }

    class Formatter {
        fun orangeAndBlackSpan(context: Context, textOrange: String, textBlack: String): Spannable {
            val txtOrange = context.resources.getColor(R.color.txt_orange)
            val txtBlack = context.resources.getColor(R.color.txt_black)

            val spannable = SpannableString(textOrange + textBlack)
            spannable.setSpan(ForegroundColorSpan(txtOrange), 0, textOrange.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannable.setSpan(ForegroundColorSpan(txtBlack), textOrange.length, spannable.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            return spannable
        }

        fun noColorSpan(textOrange: String, textBlack: String): Spannable {
            return SpannableString(textOrange + textBlack)
        }
    }
}