package com.easyfoodvone.app_common.viewdata

import android.content.Intent
import android.graphics.Bitmap
import androidx.databinding.ObservableArrayList
import androidx.fragment.app.FragmentActivity
import com.easyfoodvone.app_common.separation.ObservableField
import com.easyfoodvone.app_common.ws.BucketDataModel
import com.easyfoodvone.app_common.ws.TempModel

class DataPageNewOffer {

    data class DaysData(
        val allBox: ObservableField<Boolean>,
        val monBox: ObservableField<Boolean>,
        val tueBox: ObservableField<Boolean>,
        val wedBox: ObservableField<Boolean>,
        val thuBox: ObservableField<Boolean>,
        val friBox: ObservableField<Boolean>,
        val satBox: ObservableField<Boolean>,
        val sunBox: ObservableField<Boolean>) {

        constructor() : this(
            ObservableField<Boolean>(false),
            ObservableField<Boolean>(false),
            ObservableField<Boolean>(false),
            ObservableField<Boolean>(false),
            ObservableField<Boolean>(false),
            ObservableField<Boolean>(false),
            ObservableField<Boolean>(false),
            ObservableField<Boolean>(false))
    }

    data class ImagePickerData(
        val outputEvents: OutputEvents,
        val inputEvents: InputEvents,
        val pickedImage: ObservableField<Bitmap?>) {

        interface OutputEvents {
            fun onClickSelectImage(activity: FragmentActivity)
            fun onActivityResult(activity: FragmentActivity, requestCode: Int, resultCode: Int, data: Intent)
        }

        interface InputEvents {
            fun startActivityForResult(intent: Intent, requestCode: Int)
        }
    }

    data class PageSpendX(
        val outputEvents: OutputEvents,
        val inputEvents: ObservableField<InputEvents?>,
        val daysData: DaysData,
        val bucketsRecyclerVisible: ObservableField<Boolean>,
        val bucketsList: ObservableArrayList<BucketDataModel>,
        val txtOfferTitle: ObservableField<String>,
        val txtOfferDescription: ObservableField<String>,
        val txtActiveFrom: ObservableField<String>,
        val txtActiveTo: ObservableField<String>,
        val txtStartDate: ObservableField<String>,
        val txtEndDate: ObservableField<String>,
        val txtEditBetween: ObservableField<String>,
        val txtEditAnd: ObservableField<String>,
        val txtEditGiveDiscount: ObservableField<String>,
        val applicableOnDelivery: ObservableField<Boolean>,
        val applicableOnCollection: ObservableField<Boolean>,
        val txtTerms: ObservableField<String>,
        val errorHighlightStartDate: ObservableField<Boolean>,
        val errorHighlightEndDate: ObservableField<Boolean>,
        val errorHighlightActiveFrom: ObservableField<Boolean>,
        val errorHighlightActiveTo: ObservableField<Boolean>) {

        interface OutputEvents {
            fun onClickActiveFrom()
            fun onClickActiveTo()
            fun onClickStartDate()
            fun onClickEndDate()
            fun onClickAddMoreBucket()
            fun onClickDeleteBucket(position: Int)
            fun onClickBtnSave()
            fun onClickBtnCancel()
        }

        interface InputEvents {
            fun showOfferTitleError(msg: String)
            fun showOfferDescriptionError(msg: String)
            fun showEditBetweenError(msg: String)
            fun showEditAndError(msg: String)
            fun showEditGiveDiscountError(msg: String)
            fun showTermsError(msg: String)
            fun showAlertDialog(msg: String)
        }
    }

    data class ToEditFlatOrPercent(
        val id: String,
        val checkedDayMon: Boolean,
        val checkedDayTue: Boolean,
        val checkedDayWed: Boolean,
        val checkedDayThu: Boolean,
        val checkedDayFri: Boolean,
        val checkedDaySat: Boolean,
        val checkedDaySun: Boolean,
        // val imageUrl: String?,
        val offerTitle: String,
        val offerDescription: String,
        val startDate: String,
        val startTime: String,
        val endDate: String,
        val endTime: String,
        val howMuchCustomer: String,
        val editDiscountAmount: String,
        val maxDiscount: String,
        val perCustomerUsage: String,
        val totalUsage: String,
        val applicableOnDelivery: Boolean,
        val applicableOnCollection: Boolean,
        val applicableOnDineIn: Boolean,
        val applicableOnShowInSwipe: Boolean,
        val shareEasyfood: String,
        val shareRestaurant: String,
        val shareFranchise: String,
        val terms: String)

    data class PageFlatAmount(
        val outputEvents: OutputEvents,
        val inputEvents: ObservableField<InputEvents?>,
        val daysData: DaysData,
        val imagePickerData: ImagePickerData,
        val txtOfferTitle: ObservableField<String>,
        val txtOfferDescription: ObservableField<String>,
        val txtActiveFrom: ObservableField<String>,
        val txtActiveTo: ObservableField<String>,
        val txtStartDate: ObservableField<String>,
        val txtEndDate: ObservableField<String>,
        val txtHowMuchCustomer: ObservableField<String>,
        val txtEditDiscountAmount: ObservableField<String>,
        val txtMaxDiscount: ObservableField<String>,
        val txtPerCustomerUsage: ObservableField<String>,
        val txtTotalUsage: ObservableField<String>,
        val applicableOnDelivery: ObservableField<Boolean>,
        val applicableOnCollection: ObservableField<Boolean>,
        val applicableOnDineIn: ObservableField<Boolean>,
        val applicableOnShowInSwipe: ObservableField<Boolean>,
        val txtShareEasyfood: ObservableField<String>,
        val txtShareRestaurant: ObservableField<String>,
        val txtShareFranchise: ObservableField<String>,
        val txtTerms: ObservableField<String>,
        val errorHighlightStartDate: ObservableField<Boolean>,
        val errorHighlightEndDate: ObservableField<Boolean>,
        val errorHighlightActiveFrom: ObservableField<Boolean>,
        val errorHighlightActiveTo: ObservableField<Boolean>) {

        interface OutputEvents {
            fun onClickImageTop()
            fun onClickActiveFrom()
            fun onClickActiveTo()
            fun onClickStartDate()
            fun onClickEndDate()
            fun onClickBtnSave()
            fun onClickBtnCancel()
        }

        interface InputEvents {
            fun showOfferTitleError(msg: String)
            fun showHowMuchCustomerError(msg: String)
            fun showDiscountAmountError(msg: String)
            fun showMaxDiscountError(msg: String)
            fun showPerCustomerUsageError(msg: String)
            fun showTotalUsageError(msg: String)
            fun showShareEasyfoodError(msg: String)
            fun showShareRestaurantError(msg: String)
            fun showShareFranchiseError(msg: String)
            fun showTermsError(msg: String)
            fun showAlertDialog(msg: String)
        }
    }

    data class PagePercent(
        val outputEvents: OutputEvents,
        val inputEvents: ObservableField<InputEvents?>,
        val daysData: DaysData,
        val imagePickerData: ImagePickerData,
        val txtOfferTitle: ObservableField<String>,
        val txtOfferDescription: ObservableField<String>,
        val txtActiveFrom: ObservableField<String>,
        val txtActiveTo: ObservableField<String>,
        val txtStartDate: ObservableField<String>,
        val txtEndDate: ObservableField<String>,
        val txtHowMuchCustomer: ObservableField<String>,
        val txtEditDiscountAmount: ObservableField<String>,
        val txtMaxDiscount: ObservableField<String>,
        val txtPerCustomerUsage: ObservableField<String>,
        val txtTotalUsage: ObservableField<String>,
        val applicableOnDelivery: ObservableField<Boolean>,
        val applicableOnCollection: ObservableField<Boolean>,
        val applicableOnDineIn: ObservableField<Boolean>,
        val applicableOnShowInSwipe: ObservableField<Boolean>,
        val txtShareEasyfood: ObservableField<String>,
        val txtShareRestaurant: ObservableField<String>,
        val txtShareFranchise: ObservableField<String>,
        val txtTerms: ObservableField<String>,
        val errorHighlightStartDate: ObservableField<Boolean>,
        val errorHighlightEndDate: ObservableField<Boolean>,
        val errorHighlightActiveFrom: ObservableField<Boolean>,
        val errorHighlightActiveTo: ObservableField<Boolean>) {

        interface OutputEvents {
            fun onClickImageTop()
            fun onClickActiveFrom()
            fun onClickActiveTo()
            fun onClickStartDate()
            fun onClickEndDate()
            fun onClickBtnSave()
            fun onClickBtnCancel()
        }

        interface InputEvents {
            fun showOfferTitleError(msg: String)
            fun showDiscountAmountError(msg: String)
            fun showMaxDiscountError(msg: String)
            fun showPerCustomerUsageError(msg: String)
            fun showTotalUsageError(msg: String)
            fun showHowMuchCustomerError(msg: String)
            fun showShareEasyfoodError(msg: String)
            fun showShareRestaurantError(msg: String)
            fun showShareFranchiseError(msg: String)
            fun showTermsError(msg: String)
            fun showAlertDialog(msg: String)
        }
    }

    data class PageCombo(
        val outputEvents: OutputEvents,
        val inputEvents: ObservableField<InputEvents?>,
        val daysData: DaysData,
        val imagePickerData: ImagePickerData,
        val txtOfferTitle: ObservableField<String>,
        val txtOfferDescription: ObservableField<String>,
        val txtActiveFrom: ObservableField<String>,
        val txtActiveTo: ObservableField<String>,
        val txtStartDate: ObservableField<String>,
        val txtEndDate: ObservableField<String>,
        val txtHowMuchCustomer: ObservableField<String>,
        val txtEditDiscountAmount: ObservableField<String>,
        val txtTotalComboAmt: ObservableField<String>,
        val applicableOnDelivery: ObservableField<Boolean>,
        val applicableOnCollection: ObservableField<Boolean>,
        val txtTerms: ObservableField<String>,
        val spinnerMainItems: ObservableField<Array<String>?>,
        val selectedMenuItems: ObservableArrayList<TempModel>,
        val totalAmtLayoutVisible: ObservableField<Boolean>,
        val recyclerHeaderLayoutVisible: ObservableField<Boolean>,
        val recyclerSelectedMenuItemsVisible: ObservableField<Boolean>,
        val errorHighlightStartDate: ObservableField<Boolean>,
        val errorHighlightEndDate: ObservableField<Boolean>,
        val errorHighlightActiveFrom: ObservableField<Boolean>,
        val errorHighlightActiveTo: ObservableField<Boolean>) {

        interface OutputEvents {
            fun onClickImageTop()
            fun onClickActiveFrom()
            fun onClickActiveTo()
            fun onClickStartDate()
            fun onClickEndDate()
            fun onSpinnerItemSelect(position: Int)
            fun onClickSelectComboItems()
            fun onClickEditDiscountAmount()
            fun onClickSave()
            fun onClickCancel()
        }

        interface InputEvents {
            fun showOfferTitleError(msg: String)
            fun showOfferDescriptionError(msg: String)
            fun showDiscountAmountError(msg: String)
            fun showHowMuchCustomerError(msg: String)
            fun showTermsError(msg: String)
            fun showAlertDialog(msg: String)
            fun showKeyboardFromEditDiscountAmount(show: Boolean)
        }
    }
}