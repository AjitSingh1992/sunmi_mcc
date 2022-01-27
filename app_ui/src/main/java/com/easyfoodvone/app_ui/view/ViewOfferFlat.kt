package com.easyfoodvone.app_ui.view

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.viewdata.DataPageNewOffer
import com.easyfoodvone.app_ui.R
import com.easyfoodvone.app_ui.binding.UIVariationNewOfferFlatDiscount
import com.easyfoodvone.app_ui.databinding.PageOffersFlatDiscountAmountBinding
import com.easyfoodvone.app_ui.databinding.PageOffersFlatDiscountAmountPhoneBinding

class ViewOfferFlat(
    private val lifecycle: LifecycleSafe,
    private val context: FragmentActivity,
    private val isPhone: Boolean,
    private val data: DataPageNewOffer.PageFlatAmount) {

    private var ui: UIVariationNewOfferFlatDiscount<*>? = null

    fun getRoot(): View {
        return ui!!.binding.root
    }

    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?) {
        ui = if (isPhone) {
            UIVariationNewOfferFlatDiscount.Phone(PageOffersFlatDiscountAmountPhoneBinding.inflate(inflater, container, false))
        } else {
            UIVariationNewOfferFlatDiscount.Tablet(PageOffersFlatDiscountAmountBinding.inflate(inflater, container, false))

        }.apply {
            setData(data, lifecycle)

            lifecycle.setValueUntilDestroy(data.inputEvents, inputEventHandler)

            lifecycle.addObserverOnceUntilDestroy(data.imagePickerData.pickedImage, Observer<Bitmap?> {
                // offer_picture_placeholder is assigned by default in the layout, so don't call this observer initially
                imageTop.setImageDrawable(it?.let { BitmapDrawable(binding.root.resources, it) } ?: null)
            }, false)
        }

        /*radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    //case R.id.radio_selected_items:
                    //    getAllMenuProducts();
                    //    break;

                    case R.id.radio_on_entire_menu:
                        selcetdItemsList.setVisibility(View.GONE);
                        break;
                }
            }
        });*/
    }

    private val inputEventHandler = object : DataPageNewOffer.PageFlatAmount.InputEvents {
        override fun showOfferTitleError(msg: String) {
            ui!!.editOfferTitle.setError(msg)
        }

        /*override fun showOfferDescriptionError(msg: String) {
            ui!!.offerDescripton.setError(msg)
        }*/

        override fun showHowMuchCustomerError(msg: String) {
            ui!!.editHowMuchCustomer.setError(msg)
        }

        override fun showDiscountAmountError(msg: String) {
            ui!!.editDiscountAmount.setError(msg)
        }

        override fun showMaxDiscountError(msg: String) {
            ui!!.editMaxDiscount.setError(msg)
        }

        override fun showPerCustomerUsageError(msg: String) {
            ui!!.usagePerCust.setError(msg)
        }

        override fun showTotalUsageError(msg: String) {
            ui!!.totalUsage.setError(msg)
        }

        override fun showShareEasyfoodError(msg: String) {
            ui!!.editShareEasyfood.setError(msg)
        }

        override fun showShareRestaurantError(msg: String) {
            ui!!.editShareRestaurant.setError(msg)
        }

        override fun showShareFranchiseError(msg: String) {
            ui!!.editShareFranchise.setError(msg)
        }

        override fun showTermsError(msg: String) {
            ui!!.editTermsCondition.setError(msg)
        }

        override fun showAlertDialog(msg: String) {
            val factory = LayoutInflater.from(context)
            val mDialogView: View = factory.inflate(R.layout.alert_dialog, null)
            val msgText: TextView = mDialogView.findViewById(R.id.txt_message)
            val btnOk: Button = mDialogView.findViewById(R.id.btn_ok)

            val mDialog: AlertDialog = AlertDialog.Builder(context).create()
            mDialog.setView(mDialogView)

            msgText.setText(msg)

            btnOk.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    mDialog.dismiss()
                }
            })

            mDialog.show()
        }
    }
}