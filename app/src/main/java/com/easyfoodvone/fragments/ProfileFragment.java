package com.easyfoodvone.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.easyfoodvone.R;
import com.easyfoodvone.adapters.AdapterProfileImage;
import com.easyfoodvone.app_common.viewdata.DataPageRestaurantProfile;
import com.easyfoodvone.app_ui.view.ViewRestarauntProfile;
import com.easyfoodvone.models.LoginResponse;
import com.easyfoodvone.utility.UserPreferences;

public class ProfileFragment extends Fragment {

    private final boolean isPhone;

    private DataPageRestaurantProfile viewData;
    private ViewRestarauntProfile view;

    LoginResponse.Data data;

    public ProfileFragment(boolean isPhone) {
        this.isPhone = isPhone;
    }

    private final DataPageRestaurantProfile.OutputEvents viewEventHandler =
            () -> alertDialog("Profile details are saved successfully.");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewData = new DataPageRestaurantProfile(viewEventHandler);

        view = new ViewRestarauntProfile(this, isPhone);
        view.onCreateView(viewData, inflater, container);

        data = UserPreferences.get().getLoggedInResponse(getActivity());

        setData();

        setRecyclerView();

        return view.getRoot();
    }

    private void setData() {
        viewData.getPostcode().set(data.getPost_code());
        viewData.getRestname().set(data.getRestaurant_name());
        viewData.getServe_style().set(data.getServe_style());
        viewData.getAbout().set(data.getAbout());
        viewData.getWeb().set(data.getWebsite_url());
        viewData.getPhone().set(data.getPhone_number());
        viewData.getLandline().set(data.getLandline_number());
        viewData.getAddress().set(data.getAddress());
        viewData.getEmail().set(data.getEmail());
    }

    private void setRecyclerView() {
        AdapterProfileImage imageAdapter = new AdapterProfileImage(data.getRestaurant_images());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL,
                false);
        view.getUi().getImageListView().setLayoutManager(linearLayoutManager);
        view.getUi().getImageListView().setItemAnimator(new DefaultItemAnimator());
        view.getUi().getImageListView().setAdapter(imageAdapter);

        if (imageAdapter.getItemCount() == 0) {
            view.getUi().getImageListView().setVisibility(View.GONE);
        }
    }

    public void alertDialog(String msg) {
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View mDialogView = factory.inflate(R.layout.alert_dialog, null);
        final android.app.AlertDialog mDialog = new android.app.AlertDialog.Builder(getActivity()).create();
        mDialog.setView(mDialogView);
        TextView msgText = mDialogView.findViewById(R.id.txt_message);
        msgText.setText(msg);
        mDialogView.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        mDialog.show();
    }
}
