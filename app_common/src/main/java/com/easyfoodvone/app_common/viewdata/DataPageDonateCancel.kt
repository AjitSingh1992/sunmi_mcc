package com.easyfoodvone.app_common.viewdata

class DataPageDonateCancel(val outputEvents: OutputEvents) {

    interface OutputEvents {
        fun onClickOk()
        fun onClickCancel()
    }
}