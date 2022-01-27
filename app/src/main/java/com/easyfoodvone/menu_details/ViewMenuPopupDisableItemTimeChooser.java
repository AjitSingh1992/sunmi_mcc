package com.easyfoodvone.menu_details;

import android.app.Activity;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;

import com.easyfoodvone.app_common.viewdata.DataDialogRestaurantMenuDisableItemTimeChooser;
import com.easyfoodvone.app_ui.databinding.PopupMenuDisableItemTimeChooserBinding;
import com.easyfoodvone.app_ui.fragment.RoundedDialogFragment;
import com.easyfoodvone.controller.fragment.ControllerMenuDetails;

public class ViewMenuPopupDisableItemTimeChooser {
    public interface ParentInterface {
        void onClickDisableForToday();
        void onClickDisableForEver();
    }

    private final @NonNull Activity activity;
    private final @NonNull ControllerMenuDetails fragment;

    public ViewMenuPopupDisableItemTimeChooser(@NonNull ControllerMenuDetails fragment) {
        this.activity = fragment.requireActivity();
        this.fragment = fragment;
    }

    public void alertDialog(@NonNull ParentInterface parentInterface) {
        final PopupMenuDisableItemTimeChooserBinding dialogBinding = PopupMenuDisableItemTimeChooserBinding.inflate(LayoutInflater.from(activity));
        final RoundedDialogFragment dialog = new RoundedDialogFragment(dialogBinding.getRoot(), false);

        final DataDialogRestaurantMenuDisableItemTimeChooser data = new DataDialogRestaurantMenuDisableItemTimeChooser(
                new DialogOutputEvents(parentInterface, dialog));
        dialogBinding.setData(data);

        dialog.showNow(fragment.getChildFragmentManager(), null);
    }

    private class DialogOutputEvents implements DataDialogRestaurantMenuDisableItemTimeChooser.OutputEvents {
        private final @NonNull ParentInterface parentInterface;
        private final @NonNull RoundedDialogFragment dialog;

        public DialogOutputEvents(@NonNull ParentInterface parentInterface, @NonNull RoundedDialogFragment dialog) {
            this.parentInterface = parentInterface;
            this.dialog = dialog;
        }

        @Override
        public void onClickRemainingDay() {
            dialog.dismiss();
            parentInterface.onClickDisableForToday();
        }

        @Override
        public void onClickPermanent() {
            dialog.dismiss();
            parentInterface.onClickDisableForEver();
        }

        @Override
        public void onClickCancel() {
            dialog.dismiss();
        }
    }
}
