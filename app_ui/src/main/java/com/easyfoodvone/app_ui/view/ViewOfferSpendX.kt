package com.easyfoodvone.app_ui.view

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.viewdata.DataPageNewOffer
import com.easyfoodvone.app_ui.R
import com.easyfoodvone.app_ui.adapter.BucketsAdapter
import com.easyfoodvone.app_ui.binding.UIVariationNewOfferSpendX
import com.easyfoodvone.app_ui.databinding.PageOffersSpendXGetDiscountBinding
import com.easyfoodvone.app_ui.databinding.PageOffersSpendXGetDiscountPhoneBinding

class ViewOfferSpendX(
    private val lifecycle: LifecycleSafe,
    private val context: FragmentActivity,
    private val isPhone: Boolean,
    private val data: DataPageNewOffer.PageSpendX) {

    private var ui: UIVariationNewOfferSpendX<*>? = null

    fun getRoot(): View {
        return ui!!.binding.root
    }

    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?) {
        ui = if (isPhone) {
            UIVariationNewOfferSpendX.Phone(PageOffersSpendXGetDiscountPhoneBinding.inflate(inflater, container, false))
        } else {
            UIVariationNewOfferSpendX.Tablet(PageOffersSpendXGetDiscountBinding.inflate(inflater, container, false))

        }.apply {
            setData(data, lifecycle)

            lifecycle.setValueUntilDestroy(data.inputEvents, inputEventHandler)

            val adapter = BucketsAdapter(data.bucketsList, inflater, onAdapterItemClickListener, isPhone)
            bucketsRecycler.setAdapter(adapter)
            lifecycle.addObserverOnceUntilDestroy(data.bucketsList, adapter, false)
        }
    }

    private val onAdapterItemClickListener = object : BucketsAdapter.OnAdapterItemClickListener {
        override fun onDeleteClick(position: Int, holder: BucketsAdapter.ViewInitializer) {
            data.outputEvents.onClickDeleteBucket(position)
        }
    }

    private val inputEventHandler = object : DataPageNewOffer.PageSpendX.InputEvents {
        override fun showOfferTitleError(msg: String) {
            ui!!.editOfferTitle.setError(msg)
        }

        override fun showOfferDescriptionError(msg: String) {
            ui!!.offerDescripton.setError(msg)
        }

        override fun showEditBetweenError(msg: String) {
            ui!!.editBetween.setError(msg)
        }

        override fun showEditAndError(msg: String) {
            ui!!.editAnd.setError(msg)
        }

        override fun showEditGiveDiscountError(msg: String) {
            ui!!.editGiveDiscount.setError(msg)
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