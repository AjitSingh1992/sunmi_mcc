package com.easyfoodvone.controller.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.easyfoodvone.R;
import com.easyfoodvone.adapters.AdapterRatingReview;
import com.easyfoodvone.api_handler.ApiClient;
import com.easyfoodvone.api_handler.ApiInterface;
import com.easyfoodvone.app_common.separation.LifecycleSafe;
import com.easyfoodvone.app_common.viewdata.DataPageRatings;
import com.easyfoodvone.app_common.ws.CommonResponse;
import com.easyfoodvone.app_ui.view.ViewRatings;
import com.easyfoodvone.models.CommonRequest;
import com.easyfoodvone.models.RatingRequest;
import com.easyfoodvone.models.RatingResponse;
import com.easyfoodvone.utility.LoadingDialog;
import com.easyfoodvone.utility.PrefManager;
import com.easyfoodvone.utility.UserPreferences;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.easyfoodvone.utility.UserContants.AUTH_TOKEN;

public class ControllerRatingReview extends Fragment implements AdapterRatingReview.RatingCommentClickListner {

    private final Activity mActivity;
    private final PrefManager prefManager;
    private final boolean isPhone;

    private DataPageRatings viewData;
    private AdapterRatingReview mAdapter;
    private final List<RatingResponse.Data.UserRatingsList> userReviews = new ArrayList<>();
    private String searchNumber ="";
    private final DataPageRatings.OutputEvents viewEventHandler = new DataPageRatings.OutputEvents() {
        @Override
        public void onClickSearch() {
            getRatingReview(searchNumber);
        }
    };

    public ControllerRatingReview(Activity mActivity, PrefManager prefManager, boolean isPhone) {
        this.mActivity = mActivity;
        this.prefManager = prefManager;
        this.isPhone = isPhone;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewData = new DataPageRatings(viewEventHandler);

        ViewRatings view = new ViewRatings(new LifecycleSafe(this), isPhone);
        view.onCreateView(viewData, inflater, container);

        mAdapter = new AdapterRatingReview(userReviews, ControllerRatingReview.this);
        view.getUi().getRatingList().setAdapter(mAdapter);

        view.getUi().getSpinnerStar().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        getRatingReview("");
                        searchNumber ="";

                        break;
                    case 1:
                        getRatingReview("0");
                        searchNumber="0";

                        break;
                    case 2:
                        getRatingReview("1");
                        searchNumber="1";

                        break;
                    case 3:
                        getRatingReview("2");
                        searchNumber="2";

                        break;
                    case 4:
                        getRatingReview("3");
                        searchNumber="3";

                        break;
                    case 5:
                        getRatingReview("4");
                        searchNumber="4";

                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getRatingReview("");

        return view.getUi().getBinding().getRoot();
    }

    private void getRatingReview(String filter) {
        final LoadingDialog dialog = new LoadingDialog(getActivity(), "Loading details...");
        dialog.setCancelable(false);
        dialog.show();
        try {
            RatingRequest request = new RatingRequest();
            request.setRestaurant_id(UserPreferences.get().getLoggedInResponse(getActivity()).getRestaurant_id());
            request.setFilter(filter);

            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(apiService.getRatings(prefManager.getPreference(AUTH_TOKEN,""),request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<RatingResponse>() {
                        @Override
                        public void onSuccess(RatingResponse data) {
                            dialog.dismiss();

                            viewData.getAverageRating().set(data.getData().getTotal_ratings());
                            viewData.getAverageNoRating().set(data.getData().getTotal_no_ratings());

                            userReviews.clear();
                            userReviews.addAll(data.getData().getUser_review_ratings());
                            mAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onError(Throwable e) {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), "Loading failed!", Toast.LENGTH_LONG).show();
                            Log.e("onError", "onError: " + e.getMessage());
                        }
                    }));

        } catch (Exception e) {
            dialog.dismiss();
            Log.e("Exception ", e.toString());
            Toast.makeText(getActivity(), "Server not responding.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void sendThankYou(String customer_id) {
        final LoadingDialog dialog = new LoadingDialog(getActivity(), "Sending Thank-you...");
        dialog.setCancelable(false);
        dialog.show();
        try {
            CommonRequest request = new CommonRequest();
            request.setUser_id(customer_id);
            request.setRestaurant_id(UserPreferences.get().getLoggedInResponse(getActivity()).getRestaurant_id());

            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(apiService.sendThankyou(prefManager.getPreference(AUTH_TOKEN,""),request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<CommonResponse>() {
                        @Override
                        public void onSuccess(CommonResponse data) {
                            dialog.dismiss();
                            alertDialog("Your thanks has been sent");
                            Log.e("Ratings data ", data.toString());
                        }

                        @Override
                        public void onError(Throwable e) {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), "Sending failed!", Toast.LENGTH_LONG).show();
                            Log.e("onError", "onError: " + e.getMessage());
                        }
                    }));

        } catch (Exception e) {
            dialog.dismiss();
            Log.e("Exception ", e.toString());
            Toast.makeText(getActivity(), "Server not responding.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void alertDialogReply(String msg, final int position, final RatingResponse.Data.UserRatingsList data) {
        LayoutInflater factory = LayoutInflater.from(mActivity);
        final View mDialogView = factory.inflate(R.layout.popup_send_reply, null);
        final android.app.AlertDialog mDialog = new android.app.AlertDialog.Builder(mActivity).create();
        mDialog.setView(mDialogView);
        TextView msgText = mDialogView.findViewById(R.id.txt_message);
        final EditText editText5 = mDialogView.findViewById(R.id.editText5);
        msgText.setText(msg);
        mDialogView.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //your business logic
                mDialog.dismiss();
                if (TextUtils.isEmpty(editText5.getText().toString())) {
                    editText5.setError("Enter message!");
                    return;
                } else
                    reply(editText5.getText().toString(), data.getCustomer_id());

            }
        });
        mDialogView.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //your business logic
                mDialog.dismiss();
            }
        });

        mDialog.show();
    }

    private void reply(String s, String customer_id) {
        final LoadingDialog dialog = new LoadingDialog(getActivity(), "Replying...");
        dialog.setCancelable(false);
        dialog.show();
        try {
            CommonRequest request = new CommonRequest();
            request.setUser_id(customer_id);
            request.setRestaurant_id(UserPreferences.get().getLoggedInResponse(getActivity()).getRestaurant_id());
            request.setMessage(s);

            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(apiService.reply(prefManager.getPreference(AUTH_TOKEN,""),request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<CommonResponse>() {
                        @Override
                        public void onSuccess(CommonResponse data) {
                            dialog.dismiss();
                            alertDialog("Your message has been sent \n" +
                                    "Successfully");
                            Log.e("Ratings data ", data.toString());
                        }

                        @Override
                        public void onError(Throwable e) {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), "Replying failed!", Toast.LENGTH_LONG).show();
                            Log.e("onError", "onError: " + e.getMessage());
                        }
                    }));

        } catch (Exception e) {
            dialog.dismiss();
            Log.e("Exception ", e.toString());
            Toast.makeText(getActivity(), "Server not responding.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onReplyClicked(int position, AdapterRatingReview.MyViewHolder holder, RatingResponse.Data.UserRatingsList data) {
        alertDialogReply("Type your message you want \n" +
                "to send to " + data.getCustomer_name(), position, data);
    }

    @Override
    public void onThankyouClicked(int position, AdapterRatingReview.MyViewHolder holder, RatingResponse.Data.UserRatingsList data) {
        sendThankYou(data.getCustomer_id());
    }

    public void alertDialog(String msg) {
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View mDialogView = factory.inflate(R.layout.alert_dialog, null);
        final AlertDialog mDialog = new AlertDialog.Builder(getActivity()).create();
        mDialog.setView(mDialogView);
        TextView msgText = mDialogView.findViewById(R.id.txt_message);
        msgText.setText(msg);
        mDialogView.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //your business logic
                mDialog.dismiss();
            }
        });

        mDialog.show();
    }

}
