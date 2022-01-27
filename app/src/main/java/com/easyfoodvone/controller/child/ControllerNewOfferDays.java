package com.easyfoodvone.controller.child;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

import com.easyfoodvone.app_common.separation.ObservableField;
import com.easyfoodvone.app_common.separation.ObservableUtils;
import com.easyfoodvone.app_common.viewdata.DataPageNewOffer;

public class ControllerNewOfferDays {
    public final DataPageNewOffer.DaysData data = new DataPageNewOffer.DaysData();

    private boolean ignoreAllChange = false;
    
    public ControllerNewOfferDays() {
        data.getAllBox().addOnPropertyChangedCallback(new ObservableUtils.TypedObserver<>(data.getAllBox(), justAllCheckboxObserver));
        data.getMonBox().addOnPropertyChangedCallback(new ObservableUtils.TypedObserver<>(data.getMonBox(), dayCheckboxUntoggleAllIfFalse));
        data.getTueBox().addOnPropertyChangedCallback(new ObservableUtils.TypedObserver<>(data.getTueBox(), dayCheckboxUntoggleAllIfFalse));
        data.getWedBox().addOnPropertyChangedCallback(new ObservableUtils.TypedObserver<>(data.getWedBox(), dayCheckboxUntoggleAllIfFalse));
        data.getThuBox().addOnPropertyChangedCallback(new ObservableUtils.TypedObserver<>(data.getThuBox(), dayCheckboxUntoggleAllIfFalse));
        data.getFriBox().addOnPropertyChangedCallback(new ObservableUtils.TypedObserver<>(data.getFriBox(), dayCheckboxUntoggleAllIfFalse));
        data.getSatBox().addOnPropertyChangedCallback(new ObservableUtils.TypedObserver<>(data.getSatBox(), dayCheckboxUntoggleAllIfFalse));
        data.getSunBox().addOnPropertyChangedCallback(new ObservableUtils.TypedObserver<>(data.getSunBox(), dayCheckboxUntoggleAllIfFalse));
    }

    @NonNull
    public String getWSStringSelectedDays() {
        if (data.getAllBox().get()) {
            return "All Days";

        } else {
            String days[] = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
            boolean[] checkedDays = {
                    data.getMonBox().get(),
                    data.getTueBox().get(),
                    data.getWedBox().get(),
                    data.getThuBox().get(),
                    data.getFriBox().get(),
                    data.getSatBox().get(),
                    data.getSunBox().get()
            };
            String day = "";
            for (int i = 0; i < checkedDays.length; i++) {
                if (checkedDays[i]) {
                    if (day.length() == 0) {
                        day = days[i];
                    } else {
                        day = day + "," + days[i];
                    }
                }

                Log.e("Day String 21 ", day);
                Log.e("Day concatinating 312 ", day + "," + days[i]);
            }
            return day;
        }
    }

    private final Observer<Boolean> justAllCheckboxObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean checked) {
            if (ignoreAllChange) {
                return;
            }

            data.getMonBox().set(checked);
            data.getTueBox().set(checked);
            data.getWedBox().set(checked);
            data.getThuBox().set(checked);
            data.getFriBox().set(checked);
            data.getSatBox().set(checked);
            data.getSunBox().set(checked);
        }
    };

    private final Observer<Boolean> dayCheckboxUntoggleAllIfFalse = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean checked) {
            ObservableField<Boolean> checkboxAll = data.getAllBox();

            // When unchecking the "all" box, which will call this observer once for each other box
            // with checked = false, checkboxAll.get() will be false for each call.
            // Make sure to avoid a loop here.
            if (! checked && checkboxAll.get()) {
                ignoreAllChange = true;
                checkboxAll.set(false);
                ignoreAllChange = false;
            }
        }
    };
}
