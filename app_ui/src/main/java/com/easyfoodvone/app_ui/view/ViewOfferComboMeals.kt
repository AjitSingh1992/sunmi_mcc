package com.easyfoodvone.app_ui.view

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.viewdata.DataPageNewOffer
import com.easyfoodvone.app_ui.R
import com.easyfoodvone.app_ui.adapter.ComboProductAdapter
import com.easyfoodvone.app_ui.binding.UIVariationNewOfferCombo
import com.easyfoodvone.app_ui.databinding.PageOffersComboMealsBinding
import com.easyfoodvone.app_ui.databinding.PageOffersComboMealsPhoneBinding

class ViewOfferComboMeals(
    private val lifecycle: LifecycleSafe,
    private val context: FragmentActivity,
    private val isPhone: Boolean,
    private val data: DataPageNewOffer.PageCombo) {

    private var ui: UIVariationNewOfferCombo<*>? = null

    fun getRoot(): View {
        return ui!!.binding.root
    }

    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?) {
        ui = if (isPhone) {
            UIVariationNewOfferCombo.Phone(PageOffersComboMealsPhoneBinding.inflate(inflater, container, false))
        } else {
            UIVariationNewOfferCombo.Tablet(PageOffersComboMealsBinding.inflate(inflater, container, false))

        } .apply {
            setData(data, lifecycle)

            lifecycle.setValueUntilDestroy(data.inputEvents, inputEventHandler)

            lifecycle.addObserverOnceUntilDestroy(data.imagePickerData.pickedImage, Observer<Bitmap?> {
                // offer_picture_placeholder is assigned by default in the layout, so don't call this observer initially
                imageTop.setImageDrawable(it?.let { BitmapDrawable(binding.root.resources, it) } ?: null)
            }, false)

            val adapter = ComboProductAdapter(inflater, data.selectedMenuItems, isPhone)
            lifecycle.addObserverOnceUntilDestroy(data.selectedMenuItems, adapter, false)
            selectedMenuItemsList.setAdapter(adapter)

            spinnerMainItem.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    data.outputEvents.onSpinnerItemSelect(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            })

            lifecycle.addObserverOnceUntilDestroy(data.spinnerMainItems, Observer<Array<String>?> {
                if (it == null) {
                    spinnerMainItem.setAdapter(null)
                } else {
                    val layout = if (isPhone) R.layout.simple_spinner_dropdown_item_phone else R.layout.simple_spinner_dropdown_item
                    spinnerMainItem.setAdapter(ArrayAdapter<String>(context, layout, it))
                }
            }, true)
        }

        // Prevent memory leak
        lifecycle.nullOnDestroy(::ui)
    }

    private val inputEventHandler = object : DataPageNewOffer.PageCombo.InputEvents {
        override fun showOfferTitleError(msg: String) {
            ui!!.editOfferTitle.setError(msg)
        }

        override fun showOfferDescriptionError(msg: String) {
            ui!!.offerDescripton.setError(msg)
        }

        override fun showDiscountAmountError(msg: String) {
            ui!!.editDiscountAmount.setError(msg)
        }

        override fun showHowMuchCustomerError(msg: String) {
            ui!!.editHowMuchCustomer.setError(msg)
        }

        override fun showTermsError(msg: String) {
            ui!!.editTermsCondition.setError(msg)
        }

        override fun showAlertDialog(msg: String) {
            val factory = LayoutInflater.from(context)
            val mDialogView: View = factory.inflate(R.layout.alert_dialog, null)
            val mDialog: AlertDialog = AlertDialog.Builder(context).create()
            mDialog.setView(mDialogView)

            val msgText: TextView = mDialogView.findViewById(R.id.txt_message)
            val btnOk: Button = mDialogView.findViewById(R.id.btn_ok)

            msgText.setText(msg)

            btnOk.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    mDialog.dismiss()
                }
            })

            mDialog.show()
        }

        override fun showKeyboardFromEditDiscountAmount(show: Boolean) {
            val field = ui!!.editDiscountAmount
            val imm: InputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            if (show) {
                field.setEnabled(true)
                imm.showSoftInput(field, 0)

            } else {
                imm.hideSoftInputFromWindow(field.getWindowToken(), 0)
                Toast.makeText(context, "Please select menu item first", Toast.LENGTH_LONG).show()
            }
        }
    }
}