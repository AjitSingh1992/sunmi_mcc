package com.easyfoodvone.app_ui.view

import android.app.AlertDialog
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.viewdata.DataPageDeliverySettingsAndPostcodes
import com.easyfoodvone.app_common.ws.DeliveryPostCodeBean.DataBean
import com.easyfoodvone.app_ui.R
import com.easyfoodvone.app_ui.binding.UIVariationDeliverySettingsAndPostcode
import com.easyfoodvone.app_ui.binding.UIVariationPopupSetDeliveryCharges
import com.easyfoodvone.app_ui.databinding.PopupSetDeliveryChargesBinding
import com.easyfoodvone.app_ui.databinding.PopupSetDeliveryChargesPhoneBinding
import com.easyfoodvone.app_ui.fragment.RoundedDialogFragment
import com.easyfoodvone.app_ui.utility.DecimalDigitsInputFilter

class ViewDeliverySettingsAndPostcodes(
    val lifecycle: LifecycleSafe,
    val inflater: LayoutInflater,
    val childManager: FragmentManager,
    val isPhone: Boolean) {

    private var ui: UIVariationDeliverySettingsAndPostcode<*>? = null

    fun getRoot(): View {
        return ui!!.binding.root
    }

    fun onCreateView(data: DataPageDeliverySettingsAndPostcodes, container: ViewGroup?) {
        ui = if (isPhone) {
            UIVariationDeliverySettingsAndPostcode.Phone(
                DataBindingUtil.inflate(inflater, R.layout.page_delivery_settings_and_postcodes_phone, container, false))
        } else {
            UIVariationDeliverySettingsAndPostcode.Tablet(
                DataBindingUtil.inflate(inflater, R.layout.page_delivery_settings_and_postcodes, container, false))

        }.apply {
            setData(data, lifecycle)

            etDeliveryCharge.setFilters(arrayOf<InputFilter>(DecimalDigitsInputFilter(2)))
            etMinimumOrder.setFilters(arrayOf<InputFilter>(DecimalDigitsInputFilter(2)))
            etFreeDelivery.setFilters(arrayOf<InputFilter>(DecimalDigitsInputFilter(2)))

            distance.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (s.length != 0 && (s.toString().toInt() > 6 || s.toString().toInt() == 0)) {
                        distance.setText("")
                        distance.setError("Please Enter Distance between 1-6")
                    }
                }

                override fun afterTextChanged(s: Editable) {}
            })
            deliveryTime.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (s.length != 0 && s.toString().toInt() > 90) {
                        deliveryTime.setText("")
                        deliveryTime.setError("Please Enter Time between 0-90")
                    }
                }

                override fun afterTextChanged(s: Editable) {}
            })
            etAvgPrepTime.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (s.length != 0 && s.toString().toInt() > 90) {
                        etAvgPrepTime.setText("")
                        etAvgPrepTime.setError("Please Enter Time between 0-90")
                    }
                }

                override fun afterTextChanged(s: Editable) {}
            })

            val adapter = AdapterDeliveryCharges(inflater, data.outputEvents, data.postcodesList, isPhone)
            lifecycle.addObserverOnceUntilDestroy(data.postcodesList, adapter, callNow=false)

            recyclerTimeList.setAdapter(adapter)
        }

        lifecycle.setValueUntilDestroy(data.inputEvents, inputEventHandler)

        // Prevent memory leak
        lifecycle.nullOnDestroy(::ui)
    }

    private val inputEventHandler = object : DataPageDeliverySettingsAndPostcodes.InputEvents {
        override fun setErrorEtDeliveryCharge(msg: String) {
            ui!!.etDeliveryCharge.setError(msg)
        }

        override fun setErrorEtMinimumOrder(msg: String) {
            ui!!.etMinimumOrder.setError(msg)
        }

        override fun setErrorEtFreeDelivery(msg: String) {
            ui!!.etFreeDelivery.setError(msg)
        }

        override fun alertDialog(
                msg: String?,
                endAction: DataPageDeliverySettingsAndPostcodes.DialogOkAction) {

            val mDialogView: View = inflater.inflate(R.layout.alert_dialog, null)
            val msgText: TextView = mDialogView.findViewById(R.id.txt_message)
            val btnOk: Button = mDialogView.findViewById(R.id.btn_ok)

            val mDialog: AlertDialog = AlertDialog.Builder(getRoot().context).create()
            mDialog.setView(mDialogView)

            msgText.setText(msg)

            btnOk.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    mDialog.dismiss()

                    endAction.onOk()
                }
            })

            mDialog.show()
        }

        override fun alertDialog(
                msg: String?,
                initial: DataPageDeliverySettingsAndPostcodes.DialogCharges?,
                okAction: DataPageDeliverySettingsAndPostcodes.DialogChargesOkAction) {

            val ui = if (isPhone) {
                UIVariationPopupSetDeliveryCharges.Phone(
                        PopupSetDeliveryChargesPhoneBinding.inflate(inflater))
            } else {
                UIVariationPopupSetDeliveryCharges.Tablet(
                        PopupSetDeliveryChargesBinding.inflate(inflater))
            }

            val mDialog = AlertDialog.Builder(getRoot().context).create()
            mDialog.setView(ui.binding.root)

            ui.txtMessage.setText(msg)

            ui.edtMinOrder.setFilters(arrayOf<InputFilter>(DecimalDigitsInputFilter(2)))
            ui.edtDeliveryCharge.setFilters(arrayOf<InputFilter>(DecimalDigitsInputFilter(2)))
            ui.edtFreeDelivery.setFilters(arrayOf<InputFilter>(DecimalDigitsInputFilter(2)))

            if (initial != null) {
                ui.edtMinOrder.setText(initial.minOrder)
                ui.edtDeliveryCharge.setText(initial.deliveryCharge)
                ui.edtFreeDelivery.setText(initial.freeDelivery)
            }

            ui.btnOk.setOnClickListener { v ->
                if (TextUtils.isEmpty(ui.edtMinOrder.getText().toString())) {
                    ui.edtMinOrder.setError("Enter minimum order")
                } else if (TextUtils.isEmpty(ui.edtDeliveryCharge.getText().toString())) {
                    ui.edtDeliveryCharge.setError("Enter delivery charge")
                } else if (TextUtils.isEmpty(ui.edtFreeDelivery.getText().toString())) {
                    ui.edtFreeDelivery.setError(" Enter free delivery")
                } else {
                    mDialog.dismiss()

                    okAction.onOk(
                        DataPageDeliverySettingsAndPostcodes.DialogCharges(
                            ui.edtMinOrder.getText().toString(),
                            ui.edtDeliveryCharge.getText().toString(),
                            ui.edtFreeDelivery.getText().toString()))
                }
            }

            ui.btnCancel.setOnClickListener { v -> mDialog.dismiss() }

            mDialog.show()
        }

        override fun alertDialogDelete(postcode: String?, okAction: DataPageDeliverySettingsAndPostcodes.DialogOkAction) {
            val mDialogView: View = inflater.inflate(R.layout.popup_delete_confirmation, null)
            val mDialog = RoundedDialogFragment(mDialogView, false)

            val msgText: TextView = mDialogView.findViewById(R.id.txt_message)
            val btnOk: Button = mDialogView.findViewById(R.id.btn_ok)
            val btnNo: Button = mDialogView.findViewById(R.id.btn_no)

            msgText.setText("Are you sure you want to delete the postcode " + postcode + " from your list?")

            btnOk.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    mDialog.dismiss()

                    okAction.onOk()
                }
            })
            btnNo.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    mDialog.dismiss()
                }
            })

            mDialog.showNow(childManager, null)
        }
    }

    class AdapterDeliveryCharges(
        val inflater: LayoutInflater,
        val outputEvents: DataPageDeliverySettingsAndPostcodes.OutputEvents,
        val list: List<DataBean?>,
        val isPhone: Boolean)
        : RecyclerView.Adapter<AdapterDeliveryCharges.MyViewHolder>() {

        class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val btnEdit: ImageView = view.findViewById(R.id.btn_edit)
            val btnDel: ImageView = view.findViewById(R.id.btn_del)
            val txtPostCode: TextView = view.findViewById(R.id.post_code)
            val txtMinOrderVal: TextView = view.findViewById(R.id.min_order_val)
            val txtDeleveryCharge: TextView = view.findViewById(R.id.delivery_charge)
            val txtFreeDelivery: TextView = view.findViewById(R.id.free_delivery)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val layout = if (isPhone) R.layout.row_delivery_settings_postcode_list_phone else R.layout.row_delivery_settings_postcode_list
            val itemView = inflater.inflate(layout, parent, false)

            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val item: DataBean? = list[position]

            holder.btnEdit.setOnClickListener {
                outputEvents.onEditClick(holder.adapterPosition, item)
            }
            holder.btnDel.setOnClickListener {
                outputEvents.onDeleteClick(holder.adapterPosition, item)
            }
            holder.txtPostCode.text = item?.postcode
            holder.txtMinOrderVal.text = "£" + item?.delivery_min_value
            holder.txtDeleveryCharge.text = "£" + item?.ship_cost
            holder.txtFreeDelivery.text = "£" + item?.free_delivery_over
            holder.btnDel.visibility = if (item?.is_primary == 1) View.GONE else View.VISIBLE
        }

        override fun getItemCount(): Int {
            return list.size
        }
    }
}