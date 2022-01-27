package com.easyfoodvone.controller.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.easyfoodvone.app_common.separation.LifecycleSafe;
import com.easyfoodvone.app_ui.view.ViewDonateSuccess;
import com.easyfoodvone.app_common.viewdata.DataPageDonateSuccess;

public class ControllerCharitySuccess extends Fragment {

    public interface ParentInterface {
        void goToCharityPage();
        void setBackAction(int backNo);
    }

    private final ParentInterface parentInterface;
    private final boolean isPhone;
    private final boolean isCancel;

    private DataPageDonateSuccess viewData;
    private ViewDonateSuccess view;

    public ControllerCharitySuccess(ParentInterface parentInterface, boolean isPhone, boolean isCancel) {
        this.parentInterface = parentInterface;
        this.isPhone = isPhone;
        this.isCancel = isCancel;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private final DataPageDonateSuccess.OutputEvents viewEventHandler =
            new DataPageDonateSuccess.OutputEvents() {
                @Override
                public void onClickOk() {
                    parentInterface.goToCharityPage();
                }
            };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewData = new DataPageDonateSuccess(viewEventHandler);

        view = new ViewDonateSuccess(new LifecycleSafe(this), isPhone);
        view.onCreateView(viewData, inflater, container);
        return view.getUi().getBinding().getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentInterface.setBackAction(2);

        viewData.isCancellationPage().set(isCancel);
    }
}
