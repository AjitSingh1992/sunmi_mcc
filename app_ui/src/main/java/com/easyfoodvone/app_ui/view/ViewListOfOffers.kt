package com.easyfoodvone.app_ui.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.viewdata.DataPageListOfOffers
import com.easyfoodvone.app_common.ws.OffersResponse
import com.easyfoodvone.app_ui.R
import com.easyfoodvone.app_ui.adapter.AdapterOfferList
import com.easyfoodvone.app_ui.binding.UIVariationOffersList
import com.easyfoodvone.app_ui.binding.UIVariationPopupSelectOfferList
import com.easyfoodvone.app_ui.databinding.PageOffersListBinding
import com.easyfoodvone.app_ui.databinding.PageOffersListPhoneBinding
import com.easyfoodvone.app_ui.databinding.PopupSelectOfferListBinding
import com.easyfoodvone.app_ui.databinding.PopupSelectOfferListPhoneBinding
import com.easyfoodvone.app_ui.fragment.RoundedDialogFragment

class ViewListOfOffers(
        val data: DataPageListOfOffers,
        val lifecycle: LifecycleSafe,
        val inflater: LayoutInflater,
        val childManager: FragmentManager,
        val isPhone: Boolean) {

    private var ui: UIVariationOffersList<*>? = null

    fun getRoot(): View {
        return ui!!.binding.root
    }

    fun onCreateView(container: ViewGroup?) {
        ui = if (isPhone) {
            UIVariationOffersList.Phone(PageOffersListPhoneBinding.inflate(inflater, container, false))
        } else {
            UIVariationOffersList.Tablet(PageOffersListBinding.inflate(inflater, container, false))

        }.apply {
            setData(data, lifecycle)

            data.offersList.let { list ->
                val adapter = AdapterOfferList(list, adapterEditClickListener, adapterDeleteClickListener, isPhone)
                lifecycle.addObserverOnceUntilDestroy(list, adapter, false)
                offersList.setAdapter(adapter)
            }
        }

        lifecycle.setValueUntilDestroy(data.inputEvents, inputEventHandler)

        // Prevent view memory leak
        lifecycle.nullOnDestroy(::ui)
    }

    private val adapterEditClickListener = object : AdapterOfferList.OnActionButtonClick {
        override fun onClick(position: Int, item: OffersResponse.Data.Offers) {
            data.outputEvents.onClickEditRow(position, item)
        }
    }

    private val adapterDeleteClickListener = object : AdapterOfferList.OnActionButtonClick {
        override fun onClick(position: Int, item: OffersResponse.Data.Offers) {
            data.outputEvents.onClickDeleteRow(position, item)
        }
    }

    private enum class DialogType {
        DELETE,
        EDIT,
        NO_EDIT_DEACTIVATE,
        NO_EDIT_ACTIVATE,
        NO_DELETE_DEACTIVATE,
        NO_DELETE_ACTIVATE
    }

    private fun newConfirmationDialog(
        type: DialogType,
        item: OffersResponse.Data.Offers,
        onOk: Runnable): RoundedDialogFragment {

        val dialogView: View = inflater.inflate(R.layout.popup_delete_confirmation, null)
        val dialog = RoundedDialogFragment(dialogView, false)

        val msgText: TextView = dialogView.findViewById(R.id.txt_message)
        val btnOk: Button = dialogView.findViewById(R.id.btn_ok)
        val btnNo: Button = dialogView.findViewById(R.id.btn_no)

        val usedMsg = "Previously used " + item.getTotal_offer_used() + " times"
        val coreMsg = when (type) {
            DialogType.DELETE -> "Are you sure you want to delete " + item.offer_title
            DialogType.EDIT -> "Are you sure you want to edit " + item.offer_title
            DialogType.NO_EDIT_DEACTIVATE -> "You can not edit this, deactivate instead?"
            DialogType.NO_DELETE_DEACTIVATE -> "You can not delete this, deactivate instead?"
            DialogType.NO_DELETE_ACTIVATE,
            DialogType.NO_EDIT_ACTIVATE -> "Are you sure you want to reactivate? (undo previous deactivation)"
        }
        msgText.setText(usedMsg + "\n\n" + coreMsg)

        val okTxt = when (type) {
            DialogType.DELETE -> "Delete"
            DialogType.EDIT -> "Edit"
            DialogType.NO_EDIT_DEACTIVATE -> "Deactivate"
            DialogType.NO_EDIT_ACTIVATE -> "Activate"
            DialogType.NO_DELETE_DEACTIVATE -> "Deactivate"
            DialogType.NO_DELETE_ACTIVATE -> "Activate"
        }
        btnOk.setText(okTxt)

        btnOk.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                dialog.dismiss()

                onOk.run()
            }
        })

        btnNo.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                dialog.dismiss()
            }
        })

        return dialog
    }

    private val inputEventHandler = object : DataPageListOfOffers.InputEvents {

        override fun showDeleteConfirmation(deleteItem: OffersResponse.Data.Offers, onOk: Runnable) {
            newConfirmationDialog(DialogType.DELETE, deleteItem, onOk)
                .showNow(childManager, null)
        }

        override fun showEditConfirmation(editItem: OffersResponse.Data.Offers, onOk: Runnable) {
            newConfirmationDialog(DialogType.EDIT, editItem, onOk)
                .showNow(childManager, null)
        }

        override fun showChangeActivationDialog(isEditNotDelete: Boolean, activated: Boolean, item: OffersResponse.Data.Offers, onOk: Runnable) {
            val type = if (activated) {
                if (isEditNotDelete) DialogType.NO_EDIT_DEACTIVATE else DialogType.NO_DELETE_DEACTIVATE
            } else {
                if (isEditNotDelete) DialogType.NO_EDIT_ACTIVATE else DialogType.NO_DELETE_ACTIVATE
            }

            newConfirmationDialog(type, item, onOk)
                .showNow(childManager, null)
        }

        override fun showAddOfferDialog(
                onSpendXDiscountX: Runnable,
                onFlatDiscount: Runnable,
                onPercentDiscount: Runnable,
                onComboItemDiscount: Runnable) {
            val ui = if (isPhone) {
                    UIVariationPopupSelectOfferList.Phone(PopupSelectOfferListPhoneBinding.inflate(inflater))
                } else {
                    UIVariationPopupSelectOfferList.Tablet(PopupSelectOfferListBinding.inflate(inflater))
                }
            val mDialog = RoundedDialogFragment(ui.binding.getRoot(), true)

            ui.offerSpendXDiscountX.setOnClickListener {
                mDialog.dismiss()
                onSpendXDiscountX.run()
            }
            ui.offeringFlatAmountDiscount.setOnClickListener {
                mDialog.dismiss()
                onFlatDiscount.run()
            }
            ui.discountOfferWithPercentage.setOnClickListener {
                mDialog.dismiss()
                onPercentDiscount.run()
            }
            ui.comboOffers.setOnClickListener {
                mDialog.dismiss()
                onComboItemDiscount.run()
            }

            mDialog.showNow(childManager, null)
        }
    }
}