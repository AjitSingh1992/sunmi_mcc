package com.easyfoodvone.app_common.viewdata

import com.easyfoodvone.app_common.separation.ObservableField

class DataPageChangePassword(
        val outputEvents: OutputEvents,
        val inputEvents: ObservableField<InputEvents?>,
        val inputCurrentPassword: ObservableField<String>,
        val inputNewPassword: ObservableField<String>,
        val inputConfirmNewPassword: ObservableField<String>) {

    interface OutputEvents {
        fun onClickReset()
        fun onClickCancel()
    }

    interface InputEvents {
        fun setErrorCurrentPassword(msg: String)
        fun setErrorNewPassword(msg: String)
        fun setErrorConfirmPassword(msg: String)
    }
}