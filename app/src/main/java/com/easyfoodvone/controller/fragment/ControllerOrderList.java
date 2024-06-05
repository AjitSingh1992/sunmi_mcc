package com.easyfoodvone.controller.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableArrayList;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.easyfoodvone.DeviceList;
import com.easyfoodvone.R;
import com.easyfoodvone.api_handler.ApiClient;
import com.easyfoodvone.api_handler.ApiInterface;
import com.easyfoodvone.app_common.separation.LifecycleSafe;
import com.easyfoodvone.app_common.separation.ObservableField;
import com.easyfoodvone.app_common.viewdata.DataDialogAcceptDeliveryTime;
import com.easyfoodvone.app_common.viewdata.DataPageOrderList;
import com.easyfoodvone.app_common.viewdata.DataRowOrderDetail;
import com.easyfoodvone.app_common.viewdata.DataRowOrderOverview;
import com.easyfoodvone.app_common.ws.CommonResponse;
import com.easyfoodvone.app_common.ws.NewDetailBean;
import com.easyfoodvone.app_common.ws.OrderReportResponse;
import com.easyfoodvone.app_common.ws.OrdersListResponse;
import com.easyfoodvone.app_ui.databinding.PopupOrderAcceptDeliveryTimeBinding;
import com.easyfoodvone.app_ui.fragment.RoundedDialogFragment;
import com.easyfoodvone.app_ui.view.ViewOrderList;
import com.easyfoodvone.controller.child.ControllerOrderDetail;
import com.easyfoodvone.controller.child.ControllerRowOrderOverview;
import com.easyfoodvone.models.ChangePasswordRequest;
import com.easyfoodvone.pax.PrinterTester;
import com.easyfoodvone.utility.PrinterCommands;
import com.easyfoodvone.utility.printerutil.PrintEsayFood;
import com.easyfoodvone.models.LoginResponse;
import com.easyfoodvone.models.RestaurantClosingTimeByDataModel;
import com.easyfoodvone.new_order.models.AcceptRejectOrderRequest;
import com.easyfoodvone.new_order.models.TimeSlotRequest;
import com.easyfoodvone.models.OrdersRequest;
import com.easyfoodvone.utility.Constants;
import com.easyfoodvone.utility.Helper;
import com.easyfoodvone.utility.LoadingDialog;
import com.easyfoodvone.utility.PrefManager;
import com.easyfoodvone.utility.UserPreferences;
import com.easyfoodvone.utility.printerutil.Utils;
import com.google.gson.JsonObject;
import com.ingenico.sdk.IngenicoException;
import com.ingenico.sdk.device.printer.IPrinterEventListener;
import com.ingenico.sdk.device.printer.data.PrinterRequest;
import com.ingenico.sdk.device.printer.data.PrinterResult;
import com.ingenico.sdk.device.printer.data.PrinterStatus;
import com.ingenico.sdk.terminal.ITerminalInfo;
import com.ingenico.sdk.terminal.TerminalInfo;
import com.ingenico.sdk.terminal.TerminalInfoResult;
import com.ingenico.sdk.utils.BitmapUtils;
import com.pax.dal.IDAL;
import com.pax.dal.IPrinter;
import com.pax.dal.entity.EFontTypeAscii;
import com.pax.dal.entity.EFontTypeExtCode;
import com.pax.dal.exceptions.PrinterDevException;
import com.pax.neptunelite.api.NeptuneLiteUser;

import com.ingenico.sdk.device.printer.Printer;


import org.apache.commons.codec.binary.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import CTOS.CtPrint;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.easyfoodvone.utility.Constants.NOTIFICATION_TYPE_ACCEPTED;
import static com.easyfoodvone.utility.Helper.isInternetOn;
import static com.easyfoodvone.utility.UserContants.AUTH_TOKEN;
import static com.itextpdf.text.pdf.BaseFont.CP1250;

import android.graphics.BitmapFactory.Options;


public class ControllerOrderList extends Fragment {
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    byte FONT_TYPE;
    private UserPreferences userPreferences;
    String line20 = "--------------------------------------";//38 char
    String line20Bold = "--------------------------------------";//38 char
    String line22 = "----------------------------------";//34 char
    String line22Bold = "----------------------------------";//34 char
    String line24 = "--------------------------------";//32 char
    String line24Bold = "--------------------------------";//32 char
    String line26 = "-----------------------------";//29 char
    String line26Bold = "-----------------------------";//29 char
    static List<String> strAr1 = new ArrayList<>();

    // needed for communication to bluetooth device / network
    static OutputStream mmOutputStream;

    //castle
    public static CtPrint print;
    public static String deviceName = "";
    public NewDetailBean.OrdersDetailsBean orderDetailMain = null;

    public interface ParentInterface {
        // TODO move this into an ObservableField from the parent
        LoginResponse.Data getLoginData();

        void showToast(@Nullable String message);

        void showToastLong(@NonNull String message);

        void openAppStoreListing();

        void stopNotificationSound();

        void updateStoreOpen(boolean isOpen);

        void onDeliveryPartnerFetch(@NonNull String partner);
    }

    private static final String limitFrom = "0";
    private static final String limitTo = "100";

    private final ParentInterface parentInterface;
    private final PrefManager prefManager;
    private final ApiInterface apiService;
    private final LocalBroadcastManager localBroadcastManager;
    private final String appVersionName;
    private final boolean isPhone;

    private DataPageOrderList data;
    public static Context context;

    public ControllerOrderList(
            ParentInterface parentInterface,
            PrefManager prefManager,
            ApiInterface apiService,
            LocalBroadcastManager localBroadcastManager,
            String appVersionName,
            boolean isPhone, Context context) {
        this.parentInterface = parentInterface;
        this.prefManager = prefManager;
        this.apiService = apiService;
        this.localBroadcastManager = localBroadcastManager;
        this.appVersionName = appVersionName;
        this.isPhone = isPhone;
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        data = new DataPageOrderList(
                new ObservableField<>(false),
                new ObservableField<>(DataPageOrderList.ActiveTab.NEW),
                new ObservableField<>(0),
                new ObservableField<>(0),
                new ObservableField<>(0),
                new ObservableField<>(0),
                new ObservableArrayList<>(),
                viewEventHandler,
                new ObservableField<>(null));

        ViewOrderList view = new ViewOrderList(new LifecycleSafe(this), isPhone);
        view.onCreateView(data, inflater, container);

        IntentFilter intentFilter = new IntentFilter(NOTIFICATION_TYPE_ACCEPTED);
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);
        userPreferences = UserPreferences.get();

        return view.getUi().getBinding().getRoot();
    }

    @Override
    public void onDestroyView() {
        localBroadcastManager.unregisterReceiver(broadcastReceiver);

        super.onDestroyView();
    }

    private final DataPageOrderList.OutputEvents viewEventHandler = new DataPageOrderList.OutputEvents() {
        @Override
        public void onSwipeRefresh() {
            if (isResumed()) {
                getAllOrders();
            }
        }

        @Override
        public void onClickTabSelector(@NonNull DataPageOrderList.ActiveTab tab) {
            data.setActiveTab(tab);

            getAllOrders();
        }

        @Override
        public DataRowOrderOverview newRowPresenterOverview() {
            ControllerRowOrderOverview presenterOverview = new ControllerRowOrderOverview(
                    rowOverviewParentInterface,
                    data.getActiveTab());

            return presenterOverview.getData();
        }

        @Override
        public DataRowOrderDetail newRowPresenterDetail() {
            ControllerOrderDetail presenterDetail = new ControllerOrderDetail(rowDetailParentInterface);

            return presenterDetail.getData();
        }
    };

    private final ControllerRowOrderOverview.ParentInterface rowOverviewParentInterface
            = new ControllerRowOrderOverview.ParentInterface() {
        @Override
        public void onAcceptClick(@NonNull OrdersListResponse.Orders orderDetail) {
            showChangeDeliveryTimeDialog(orderDetail);
        }

        @Override
        public void onRejectClick(@NonNull OrdersListResponse.Orders orderDetail) {
            showRejectConfirmationDialog(orderDetail);
        }

        @Override
        public void onSpinnerStatus(DataPageOrderList.AcceptedOrderRequestStatus statusEnum, OrdersListResponse.Orders orderDetail) {
            orderStatus(statusEnum, orderDetail);
        }

        @Override
        public void openOrderDetail(OrdersListResponse.Orders clickedOrder) {
            data.getInputEvents().get().scrollToOverviewOf(clickedOrder);

            @Nullable DataRowOrderDetail detailData = data.getInputEvents().get().findDetailFor(clickedOrder);
            if (detailData != null) {
                detailData.getOutputEvents().toggleVisible();
            }
        }

        @Override
        public void stopNotificationSound() {
            parentInterface.stopNotificationSound();
        }
    };

    private final ControllerOrderDetail.ParentInterface rowDetailParentInterface
            = new ControllerOrderDetail.ParentInterface() {
        @Override
        @NonNull
        public Call<NewDetailBean> callOrderDetail(@NonNull String orderNumber) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("order_number", orderNumber);
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Log.e("Order Detail Request", "" + jsonObject);
            return apiInterface.getOrderDetail(prefManager.getPreference(AUTH_TOKEN, ""), jsonObject);
        }

        @Override
        public void print(@NonNull NewDetailBean.OrdersDetailsBean orderDetail) {
            printOrderDetails(getActivity(), userPreferences.getLoggedInResponse(getActivity()), orderDetail);

        }
    };

    public void printOrderDetails(@NonNull Context context, @NonNull LoginResponse.Data restaurantData, @NonNull NewDetailBean.OrdersDetailsBean orderDetail) {
        byte[] logoByte = UserPreferences.get().getRestaurantLogoBitmap(getActivity());
        Bitmap logo = null;
        if (logoByte != null) {
            logo = BitmapFactory.decodeByteArray(logoByte, 0, logoByte.length);
        }


        LoginResponse.Data loginData = parentInterface.getLoginData();

        if (loginData != null) {
            if (Helper.getDeviceName().contains("Sunmi")) {
                boolean serviceReady = PrintEsayFood.printOrderDetails(getActivity(), logo, loginData, orderDetail);
                if (!serviceReady) {
                    parentInterface.showToastLong("The print service is not connected");
                }
            } else if (Helper.getDeviceName().contains("A920Pro") || Helper.getDeviceName().contains("Lephone")) {
                deviceName = "pax";
                ingenico(getActivity(), logo, userPreferences.getLoggedInResponse(getActivity()), orderDetail);

            } else if (Helper.getDeviceName().contains("DX8000")) {
                ingenico(getActivity(), logo, userPreferences.getLoggedInResponse(getActivity()), orderDetail);
            } else if (Helper.getDeviceName().contains("Saturn1000F2")) {
                deviceName = "castle";
                print = new CtPrint();
                ingenico(getActivity(), logo, userPreferences.getLoggedInResponse(getActivity()), orderDetail);

                //castle(getActivity(), logo, userPreferences.getLoggedInResponse(getActivity()), orderDetail);
            } else {
                //ingenico(getActivity(), logo, userPreferences.getLoggedInResponse(getActivity()), orderDetail);

                printBill(getActivity(), logo, userPreferences.getLoggedInResponse(getActivity()), orderDetail);
            }
        }
    }

    public class PrintAsyncTaskPax extends AsyncTask<Void, Void, Void> {

        private final Context context;

        public PrintAsyncTaskPax(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Perform any setup or UI updates on the main thread before the background task starts.
            Toast.makeText(context, "Printing in progress...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                IDAL idal = NeptuneLiteUser.getInstance().getDal(context);
                IPrinter printer = idal.getPrinter();

                Toast.makeText(context, "" + Helper.getDeviceName(), Toast.LENGTH_SHORT).show();

                printer.init();
                printer.printStr("test", null);
                printer.start();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            }


            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            // Update UI on the main thread (e.g., show Toast messages)
            Toast.makeText(context, "Printing completed", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Void result) {
            // This method runs on the UI thread after the background task completes.
            // You can perform any post-execution tasks here.
        }

        // ... Other methods (createMyBitmap, etc.) can be included here

    }

    private static class MakeDataPax extends AsyncTask<Void, Void, String> {
        private final Context context;

        public MakeDataPax(@NonNull Context context) {
            this.context = context;
            Log.e("1111111111111", "1111111111111111");
        }

        @Override
        protected String doInBackground(Void... voids) {
            Log.e("222222222222", "222222222222222");

            try {
                IDAL idal = NeptuneLiteUser.getInstance().getDal(context);
                IPrinter printer = idal.getPrinter();

                Toast.makeText(context, "" + Helper.getDeviceName(), Toast.LENGTH_SHORT).show();

                printer.init();
                printer.printStr("test", null);
                printer.start();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            }



            return "done";
        }

        @Override
        protected void onPostExecute(String cartData) {
            super.onPostExecute(cartData);
            Log.e("cccccccccccc", "cccccccccccc");

            Log.e("Cart DATA\n", cartData);




        }

    }

    public void showImage(Bitmap bitmap) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.bitmap_image);
        dialog.setCancelable(true);


        ImageView image = (ImageView) dialog.findViewById(R.id.goProDialogImage);
        image.setImageBitmap(bitmap);
        dialog.show();
    }

    private static Bitmap createMyBitmap(int width, @Nullable Integer logoId, Integer logoWidth,
                                         @Nullable List<String> lines,
                                         @Nullable Integer textSize) {
        if (textSize == null)
            textSize = 12;

        float density = context.getResources().getDisplayMetrics().density;

        // Compute total height
        Float height = 0F;
        float lineHeight = textSize * density * 1.5F; // 50% of interline space
        int logoHeight = 0;
        Bitmap logoBitmap = null;
        float logoBottomMargin = lineHeight / 2;
        float bottomMargin = lineHeight / 2;
        if (logoId != null) {
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inScaled = false;
            logoBitmap = BitmapFactory
                    .decodeResource(context.getResources(), logoId,
                            bitmapOptions);
            logoHeight = logoWidth * logoBitmap.getHeight() / logoBitmap.getWidth();
            height += (logoHeight + logoBottomMargin);
        }
        if (lines != null) {
            height += lines.size() * lineHeight;
        }
        height += bottomMargin;

        // Create bitmap and canvas
        Bitmap bitmap = Bitmap.createBitmap((DisplayMetrics) null, width,
                height.intValue(),
                Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.WHITE);
        Canvas canvas = new Canvas(bitmap);

        // Draw logo on canvas (optional)
        if (logoBitmap != null) {
            int w = (int) (logoWidth / density);
            int h = (int) (logoHeight / density);
            Bitmap scaledLogoBitmap = Bitmap.createScaledBitmap(logoBitmap, w, h, true);
            canvas.drawBitmap(scaledLogoBitmap, (width - logoWidth) / 2F, 0F, null);
        }

        // Draw text on canvas (optional)
        if (lines != null) {
            TextPaint paint = new TextPaint();
            paint.setColor(Color.BLACK);
            paint.setTextSize(textSize * density);
            paint.setAntiAlias(true);
            float y = logoHeight + lineHeight + 1.5F;
            for (String line : lines) {
                canvas.drawText(line, 0F, y, paint);
                y += lineHeight;
            }
        }
        return bitmap;
    }

    public void ingenico(@NonNull Context context, @Nullable Bitmap logo, @NonNull LoginResponse.Data restaurantData, @NonNull NewDetailBean.OrdersDetailsBean orderDetail) {

        printBillIngenico(getActivity(), logo, userPreferences.getLoggedInResponse(getActivity()), orderDetail);

    }

    public void castle(@NonNull Context context, @Nullable Bitmap logo, @NonNull LoginResponse.Data restaurantData, @NonNull NewDetailBean.OrdersDetailsBean orderDetail) {

        printBillCastle(getActivity(), logo, userPreferences.getLoggedInResponse(getActivity()), orderDetail);

    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getAllOrders();
            Toast.makeText(context, "New Order recieved", Toast.LENGTH_SHORT).show();
        }
    };

    private void getAllOrders() {
        final String requestStatus;
        switch (data.getActiveTab().get()) {
            case NEW:
                requestStatus = OrdersRequest.STATUS_NEW;
                break;
            case ACCEPTED:
                requestStatus = OrdersRequest.STATUS_ACCEPTED;
                break;
            case REJECTED:
                requestStatus = OrdersRequest.STATUS_REJECTED;
                break;
            case REFUNDED:
                requestStatus = OrdersRequest.STATUS_REFUNDED;
                break;
            default:
                throw new IllegalArgumentException("Missing case");
        }

        data.isSwipeRefreshing().set(true);

      /*  final LoadingDialog dialog = new LoadingDialog(getActivity(), "");
        dialog.setCancelable(false);
        dialog.show();
*/
        try {
            LoginResponse.Data loginData = parentInterface.getLoginData();

            OrdersRequest request = new OrdersRequest();
            request.setRestaurant_id(loginData.getRestaurant_id());
            request.setLimit(limitTo);
            request.setOffset(limitFrom);
            request.setStatus(requestStatus);

            Log.e("prinToken", "" + loginData.getToken());

            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(apiService.getOrders(loginData.getToken(), request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<OrdersListResponse>() {
                        @Override
                        public void onSuccess(OrdersListResponse data) {
  /*                          dialog.dismiss();
*/
                            ControllerOrderList.this.data.isSwipeRefreshing().set(false);
                            if (data.isSuccess()) {
                                if (/*com.easyfoodvone.BuildConfig.FORCE_UPDATE_TO_GOOGLE_PLAY_VERSION
                                        &&*/ !data.getData().getAndroid_version().equals(appVersionName)) {
                                    //updateDialog();
                                } else {
                                }

                                @Nullable String partner = data.getData().getDelivery_partner();
                                // prefManager.savePreference("DELIVERY_TYPE", partner);
                                parentInterface.onDeliveryPartnerFetch(partner == null ? "" : partner);

                                ControllerOrderList.this.data.setOrdersList(
                                        data.getData().getOrders(),
                                        data.getData().getTotal_new_order(),
                                        data.getData().getTotal_accepted_order(),
                                        data.getData().getTotal_rejected_order(),
                                        data.getData().getTotal_refunded_order()
                                );

                                parentInterface.updateStoreOpen(data.getData().isIs_open());

                            } else {
                                ControllerOrderList.this.data.isSwipeRefreshing().set(false);
                                parentInterface.showToast(data.getMessage());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
  /*                          dialog.dismiss();
  */                          data.isSwipeRefreshing().set(false);
                            parentInterface.showToast("Server connection failed");
                            Log.e("onError", "onError: " + e.getMessage());
                        }
                    }));

        } catch (Exception e) {
  /*          dialog.dismiss();
  */          data.isSwipeRefreshing().set(false);

            Log.e("Exception ", e.toString());
            parentInterface.showToast("Server not responding.");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        getAllOrders();
    }

    @Override
    public void onPause() {
        super.onPause();

        parentInterface.stopNotificationSound();
    }

    @Override
    public void onStop() {
        super.onStop();

        parentInterface.stopNotificationSound();
    }

    private static class ChangeDeliveryTimeFields {
        private final @NonNull SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy   HH:mm:ss", Locale.getDefault());

        public final @Nullable String originalDeliveryDateTime;
        private final @Nullable Date originalDeliveryDateTimeParsed;

        private int extraMins = 0;
        private long asyncRestaurantCloseMin = -1;

        public ChangeDeliveryTimeFields(@Nullable String originalDeliveryDateTime) {
            this.originalDeliveryDateTime = originalDeliveryDateTime;
            this.originalDeliveryDateTimeParsed = Helper.parseDeliveryDate(originalDeliveryDateTime);
        }

        public void add5() {
            if (asyncRestaurantCloseMin <= 0) {
                // Don't allow extra time?
                return;
            }

            // NewOrderActivity had untested/unused code which used a different web service field and method for displaying the minutes:
            // the dialog layout had an invisible TextView which said "You can extend delivery time maximum to 90 minutes from average delivery time"
            // 1. orderDetail.getAverage_delivery_time()
            // 2. Clamp the lower bound to this average
            // 3. Clamp the upper bound to 90
            // 4. Instead of request.setDelivey_date_time it uses request.setDelivey_time
            // 5. That is passed the integer as a string: getAverage_delivery_time() <= "int" <= 90
            if (extraMins >= 90) {
                return;
            }

            if (asyncRestaurantCloseMin > extraMins + 5) {
                extraMins = extraMins + 5;
            }
        }

        public void remove5() {
            if (asyncRestaurantCloseMin <= 0) {
                // Don't allow extra time?
                return;
            }

            if (extraMins > 0) {
                extraMins = extraMins - 5;
            }
        }

        public int getExtraTimeMins() {
            return extraMins;
        }

        public String formatDeliveryDateTimeWithExtra() {
            if (originalDeliveryDateTimeParsed == null) {
                int hour = extraMins / 60;
                int minutes = extraMins % 60;
                return originalDeliveryDateTime
                        + "\n+"
                        + new DecimalFormat("00").format(hour)
                        + ":"
                        + new DecimalFormat("00").format(minutes);

            } else {
                return dateFormatter.format(addMinutes(originalDeliveryDateTimeParsed, extraMins));
            }
        }

        // Expects input in the format "yyyy-MM-dd HH:mm:ss" and returns in the same format
        public String formatAPIOriginalDateTimeWithExtra() {
            if (originalDeliveryDateTimeParsed == null) {
                return originalDeliveryDateTime;

            } else {
                return Helper.formatDateForAPIWithExtraTime(addMinutes(originalDeliveryDateTimeParsed, extraMins));
            }
        }

        private @NonNull Date addMinutes(@NonNull Date original, int minutes) {
            return new Date(original.getTime() + TimeUnit.MINUTES.toMillis(minutes));
        }
    }

    private class ChangeDeliveryDialogOutputEvents implements DataDialogAcceptDeliveryTime.OutputEvents {
        private final @NonNull RoundedDialogFragment mDialog;
        private final @NonNull ChangeDeliveryTimeFields dialogFields;
        private final @NonNull OrdersListResponse.Orders orderDetail;
        //
        public DataDialogAcceptDeliveryTime data;

        public ChangeDeliveryDialogOutputEvents(
                @NonNull RoundedDialogFragment mDialog,
                @NonNull ChangeDeliveryTimeFields dialogFields,
                @NonNull OrdersListResponse.Orders orderDetail) {
            this.mDialog = mDialog;
            this.dialogFields = dialogFields;
            this.orderDetail = orderDetail;
        }

        @Override
        public void onClickAccept() {

            parentInterface.stopNotificationSound();
            mDialog.dismiss();

            acceptRejectOrder("accepted", orderDetail, dialogFields.formatAPIOriginalDateTimeWithExtra(), "");
        }

        @Override
        public void onClickCancel() {
            mDialog.dismiss();
        }

        @Override
        public void onClickAdd() {
            dialogFields.add5();

            data.getDeliveryDateTime().set(dialogFields.formatDeliveryDateTimeWithExtra());
            data.getMinsAboveAverage().set(dialogFields.getExtraTimeMins());
        }

        @Override
        public void onClickSubtract() {
            dialogFields.remove5();

            data.getDeliveryDateTime().set(dialogFields.formatDeliveryDateTimeWithExtra());
            data.getMinsAboveAverage().set(dialogFields.getExtraTimeMins());
        }
    }

    public void printCommon(){
        byte[] logoByte = UserPreferences.get().getRestaurantLogoBitmap(getActivity());
        Bitmap logo = null;
        if (logoByte != null) {
            logo = BitmapFactory.decodeByteArray(logoByte, 0, logoByte.length);
        }
        LoginResponse.Data loginData = parentInterface.getLoginData();

        if (loginData != null) {
            Toast.makeText(context, "" + Helper.getDeviceName(), Toast.LENGTH_SHORT).show();

            deviceName = "pax";
            if (Helper.getDeviceName().contains("Sunmi")) {
                boolean serviceReady = PrintEsayFood.printOrderDetails(getActivity(), logo, loginData, orderDetailMain);
                if (!serviceReady) {
                    parentInterface.showToastLong("The print service is not connected");
                }
            } else if (Helper.getDeviceName().contains("A920Pro") || Helper.getDeviceName().contains("Lephone")) {
                deviceName = "pax";
                ingenico(getActivity(), logo, userPreferences.getLoggedInResponse(getActivity()), orderDetailMain);

            } else if (Helper.getDeviceName().contains("DX8000")) {
                ingenico(getActivity(), logo, userPreferences.getLoggedInResponse(getActivity()), orderDetailMain);
            } else if (Helper.getDeviceName().contains("Saturn1000F2")) {
                deviceName = "castle";
                print = new CtPrint();
                ingenico(getActivity(), logo, userPreferences.getLoggedInResponse(getActivity()), orderDetailMain);

  //              castle(getActivity(), logo, userPreferences.getLoggedInResponse(getActivity()), orderDetailMain);
            } else {
                printBill(getActivity(), logo, userPreferences.getLoggedInResponse(getActivity()), orderDetailMain);
            }
        }

    }
    private void showChangeDeliveryTimeDialog(final OrdersListResponse.Orders orderDetail) {
        LayoutInflater factory = LayoutInflater.from(getActivity());
        PopupOrderAcceptDeliveryTimeBinding binding = PopupOrderAcceptDeliveryTimeBinding.inflate(factory);
        RoundedDialogFragment mDialog = new RoundedDialogFragment(binding.getRoot(), false);

        final ChangeDeliveryTimeFields dialogFields = new ChangeDeliveryTimeFields(orderDetail.getDelivery_date_time());
        final ChangeDeliveryDialogOutputEvents outputEventHandler = new ChangeDeliveryDialogOutputEvents(mDialog, dialogFields, orderDetail);

        final DataDialogAcceptDeliveryTime data = new DataDialogAcceptDeliveryTime(
                new ObservableField<>(dialogFields.formatDeliveryDateTimeWithExtra()),
                new ObservableField<>(dialogFields.getExtraTimeMins()),
                new ObservableField<>(orderDetail.getAverage_delivery_time()),
                new ObservableField<>(false),
                new ObservableField<>(false),
                outputEventHandler);
        outputEventHandler.data = data;

        binding.setData(data);

        getRestaurantClosingTimeByDate(data, orderDetail.getRestaurant_id(), dialogFields);

        mDialog.showNow(getChildFragmentManager(), null);
    }

    private void showRejectConfirmationDialog(final OrdersListResponse.Orders orderDetail) {
        final String[] reasons = new String[]{
                "Select reason",
                "Too busy to fulfill order",
                "Items not in stock",
                "Delivery driver unavailable ",
                "Closing soon",
                "collection only",
                "Other"};

        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View mDialogView = factory.inflate(R.layout.popup_order_reject_confirmation, null);

        RoundedDialogFragment mDialog = new RoundedDialogFragment(mDialogView, false);

        final EditText notes = (EditText) mDialogView.findViewById(R.id.notes);
        final Spinner spinnerMainItem = mDialogView.findViewById(R.id.spinner_main_item);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_spinner_dropdown_item, reasons);
        spinnerMainItem.setAdapter(spinnerArrayAdapter);

        final int[] selectedPos = {0};

        spinnerMainItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPos[0] = position;

                if (position == 0) {
                    notes.setVisibility(View.GONE);
                    notes.setText("");
                } else if (position == 6) {
                    notes.setText("");
                    notes.setVisibility(View.VISIBLE);
                } else {
                    notes.setVisibility(View.GONE);
                    notes.setText(reasons[position]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mDialogView.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPos[0] == 0) {
                    parentInterface.showToast("Please select rejection reason");
                } else {
                    parentInterface.stopNotificationSound();
                    acceptRejectOrder("rejected", orderDetail, orderDetail.getDelivery_date_time(), notes.getText().toString());
                    mDialog.dismiss();
                }
            }
        });
        mDialogView.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        mDialog.showNow(getChildFragmentManager(), null);
    }

    private void acceptRejectOrder(final String acceptReject, OrdersListResponse.Orders orderDetail, String deliveryTime, String notes) {
        final LoadingDialog dialog = new LoadingDialog(getActivity(), "Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        try {
            LoginResponse.Data loginData = parentInterface.getLoginData();

            AcceptRejectOrderRequest request = new AcceptRejectOrderRequest();
            request.setCustomer_id(orderDetail.getCustomer_id());
            request.setOrder_number(orderDetail.getOrder_num());
            request.setOrder_response(acceptReject);
            request.setRestaurant_id(loginData.getRestaurant_id());
            request.setDelivey_date_time(deliveryTime);
            request.setNotes(notes);

            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(apiService.acceptRejectOrders(prefManager.getPreference(AUTH_TOKEN, ""), request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<CommonResponse>() {
                        @Override
                        public void onSuccess(CommonResponse data) {
                            dialog.dismiss();

                            if (data.isSuccess()) {
                                parentInterface.stopNotificationSound();

                                if (acceptReject.equals("accepted")) {
                                    ControllerOrderList.this.data.moveOrderItem(orderDetail, DataPageOrderList.ActiveTab.NEW, DataPageOrderList.ActiveTab.ACCEPTED);
                                    printOrderDetails(orderDetail.getOrder_num());
                                    parentInterface.showToast("Order Accepted");

                                } else {
                                    ControllerOrderList.this.data.moveOrderItem(orderDetail, DataPageOrderList.ActiveTab.NEW, DataPageOrderList.ActiveTab.REJECTED);

                                    Constants.DialogClickedListener listener = new Constants.DialogClickedListener() {
                                        @Override
                                        public void onDialogClicked() {
                                        }

                                        @Override
                                        public void onDialogRejectClicked() {
                                        }
                                    };

                                    Constants.alertDialogReject(
                                            "You have rejected this order.\nCustomer has been\nnotified",
                                            getActivity(),
                                            listener);
                                }

                            } else {
                                alertDialog(data.getMessage());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            dialog.dismiss();
                            Log.e("onError", "onError: " + e.getMessage());
                        }
                    }));

        } catch (Exception e) {
            dialog.dismiss();
            Log.e("Exception ", e.toString());
            parentInterface.showToast("Server not responding.");
            e.printStackTrace();
        }
    }
    private void printOrderDetails(@NonNull String orderNumber) {
        if (TextUtils.isEmpty(orderNumber)) {
            return;
        }

        if ( ! isInternetOn(getActivity())) {
            return;
        }

        if ( ! userPreferences.isLoggedIn(getActivity())) {
            return;
        }

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        String authToken = PrefManager.getInstance(context).getPreference(AUTH_TOKEN,"");
        @NonNull LoginResponse.Data loggedInData = userPreferences.getLoggedInResponse(context);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("order_number", orderNumber);

        Call<NewDetailBean> call = apiInterface.getOrderDetail(authToken, jsonObject);
        call.enqueue(new Callback<NewDetailBean>() {
            @Override
            public void onResponse(@NonNull Call<NewDetailBean> call, @NonNull Response<NewDetailBean> response) {
                try {
                    if (response.isSuccessful()) {
                        NewDetailBean newDetailBean = response.body();
                        if (newDetailBean.isSuccess()) {
                            orderDetailMain = newDetailBean.getOrders_details();
                            printCommon();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<NewDetailBean> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void orderStatus(@NonNull DataPageOrderList.AcceptedOrderRequestStatus statusEnum, OrdersListResponse.Orders orderDetail) {
        final String status;
        switch (statusEnum) {
            case PREPARING:
                status = "preparing";
                break;
            case OUT_FOR_DELIVERY:
                status = "out_for_delivery";
                break;
            case DELIVERED:
                status = "delivered";
                break;
            case SERVED:
                status = "served";
                break;
            default:
                throw new IllegalArgumentException("Missing case");
        }

        final LoadingDialog dialog = new LoadingDialog(getActivity(), "Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        try {
            AcceptRejectOrderRequest request = new AcceptRejectOrderRequest();
            request.setOrderStatus(status);
            request.setOrder_number(orderDetail.getOrder_num());

            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(apiService.orderStatus(prefManager.getPreference(AUTH_TOKEN, ""), request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<CommonResponse>() {
                        @Override
                        public void onSuccess(CommonResponse data) {
                            dialog.dismiss();
                            if (data.isSuccess()) {
                                // This should only happen for accepted orders, layoutOrderStatus
                                // should be Visibility.GONE for any other active tab
                                getAllOrders();
                                parentInterface.showToastLong("Successful");
                            } else {
                                parentInterface.showToastLong("Unsuccessful");
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            dialog.dismiss();
                            Log.e("onError", "onError: " + e.getMessage());
                        }
                    }));

        } catch (Exception e) {
            dialog.dismiss();
            Log.e("Exception ", e.toString());
            parentInterface.showToast("Server not responding.");
            e.printStackTrace();
        }
    }

    private void getRestaurantClosingTimeByDate(final DataDialogAcceptDeliveryTime uiData, String restaurantId, ChangeDeliveryTimeFields deliveryFields) {
        uiData.getButtonsDataIsLoading().set(true);

        try {
            TimeSlotRequest request = new TimeSlotRequest(restaurantId, deliveryFields.originalDeliveryDateTime);

            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(apiService.getRestaurantClosingTimeByDate(prefManager.getPreference(AUTH_TOKEN, ""), request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<RestaurantClosingTimeByDataModel>() {
                        @Override
                        public void onSuccess(RestaurantClosingTimeByDataModel data) {
                            uiData.getButtonsDataIsLoading().set(false);

                            if (data.getSuccess()) {
                                if (data != null
                                        && data.getData().getOpeningEndTime() != null
                                        && !data.getData().getOpeningEndTime().equalsIgnoreCase("")) {
                                    try {
                                        deliveryFields.asyncRestaurantCloseMin = getDifferenceMin(
                                                Constants.getDateFromString(deliveryFields.originalDeliveryDateTime, "yyyy-MM-dd HH:mm:ss"),
                                                Constants.getDateFromString(data.getData().getOpeningEndTime(), "yyyy-MM-dd HH:mm:ss"));

                                        uiData.getButtonsDataIsAvailable().set(true);

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            uiData.getButtonsDataIsLoading().set(false);
                            Log.e("onError", "onError: " + e.getMessage());
                        }
                    }));

        } catch (Exception e) {
            uiData.getButtonsDataIsLoading().set(false);
            Log.e("Exception ", e.toString());
            parentInterface.showToast("Server not responding.");
            e.printStackTrace();
        }
    }

    private long getDifferenceMin(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
//        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
//        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
        return elapsedMinutes;
    }

    private void alertDialog(String msg) {
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View mDialogView = factory.inflate(R.layout.alert_dialog, null);
        final AlertDialog mDialog = new AlertDialog.Builder(getActivity()).create();
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

    private void updateDialog() {
        final View dialogView = LayoutInflater.from(getActivity()).inflate(
                isPhone ? R.layout.popup_update_google_play_phone : R.layout.popup_update_google_play_tablet,
                null);

        RoundedDialogFragment dialog = new RoundedDialogFragment(dialogView, false);

        TextView tvOk = (TextView) dialogView.findViewById(R.id.tv_ok);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                parentInterface.openAppStoreListing();
            }
        });

        dialog.showNow(getChildFragmentManager(), null);
    }

    protected void printBill(@NonNull Context context, @Nullable Bitmap logo, @NonNull LoginResponse.Data restaurantData, @NonNull NewDetailBean.OrdersDetailsBean orderDetail) {

        if (mmSocket == null) {
            Intent BTIntent = new Intent(getActivity(), DeviceList.class);
            this.startActivityForResult(BTIntent, DeviceList.REQUEST_CONNECT_BT);
        } else {
            OutputStream opstream = null;
            try {
                opstream = mmSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mmOutputStream = opstream;

            //print command
            try {

                mmOutputStream = mmSocket.getOutputStream();
                byte[] printformat = new byte[]{0x1B, 0x21, 0x03};
                //final byte[] Init = {27, 29, 116, 32};//for star micronics
                final byte[] InitTM = {27, 116, 16};
                //mmOutputStream.write(InitTM);
                mmOutputStream.write(InitTM);

                Date date = new Date();
                DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                DateFormat timeFormat = new SimpleDateFormat("HH:mma");


                // printCustom( "£20.00",2,1);

                new MakeData(context, logo, orderDetail, restaurantData).execute();
              /*  if (logo != null)
                    printPhoto(getActivity(),logo);




                printCustom("Fair Group BD",2,1);
                printCustom("Pepperoni Foods Ltd.",0,1);
                // printPhoto(logo);
                printCustom("H-123, R-123, Dhanmondi, Dhaka-1212",0,1);
                printCustom("Hot Line: +88000 000000",0,1);
                printCustom("Vat Reg : 0000000000,Mushak : 11",0,1);
                String dateTime[] = getDateTime();
                printText(leftRightAlign(dateTime[0], dateTime[1]));
                printText(leftRightAlign("Qty: Name" , "Price "));
                printCustom(new String(new char[32]).replace("\0", "."),0,1);
                printText(leftRightAlign("Total" , "2,0000/="));
                printNewLine();
                printCustom("Thank you for coming & we look",0,1);
                printCustom("forward to serve you again",0,1);
                printNewLine();
                printNewLine();

                mmOutputStream.flush();*/
            } catch (IOException e) {
                e.printStackTrace();
            }


        }


    }

    protected void printBillCastle(@NonNull Context context, @Nullable Bitmap logo, @NonNull LoginResponse.Data restaurantData, @NonNull NewDetailBean.OrdersDetailsBean orderDetail) {
        new MakeData(context, logo, orderDetail, restaurantData).execute();


    }

    protected void printBillIngenico(@NonNull Context context, @Nullable Bitmap logo, @NonNull LoginResponse.Data restaurantData, @NonNull NewDetailBean.OrdersDetailsBean orderDetail) {
        new MakeDataIngenico(context, logo, orderDetail, restaurantData).execute();


    }

    private static class MakeData extends AsyncTask<Void, Void, String> {
        @NonNull
        final Context context;
        @Nullable
        final Bitmap logo;
        @NonNull
        final NewDetailBean.OrdersDetailsBean orderDetail;
        @NonNull
        final LoginResponse.Data restaurantData;
        int charCount = 43;
        Bitmap bitmap1 = null;
        String addressFormatToPrint = "";
        String getAddress_1 = "", address_2 = "", city = "", post_code = "", country = "", AddressToPrint = "";

        String line24Bold = "--------------------------------";//32 char


        public MakeData(@NonNull Context context, @Nullable Bitmap logo, @NonNull NewDetailBean.OrdersDetailsBean orderDetail, @NonNull LoginResponse.Data restaurantData) {
            this.context = context;
            this.logo = logo;
            this.orderDetail = orderDetail;
            this.restaurantData = restaurantData;
            Log.e("1111111111111", "1111111111111111");
        }

        @Override
        protected String doInBackground(Void... voids) {
            Log.e("222222222222", "222222222222222");

            return makeCartData(context, charCount, orderDetail.getCart());
        }

        @Override
        protected void onPostExecute(String cartData) {
            super.onPostExecute(cartData);
            Log.e("cccccccccccc", "cccccccccccc");

            Log.e("Cart DATA\n", cartData);
            String customerName = orderDetail.getDelivery_address().getCustomer_name();
            String customerPhone;
            if (orderDetail.getOrder_phone_number() != null && !orderDetail.getOrder_phone_number().isEmpty()) {
                customerPhone = orderDetail.getOrder_phone_number();
            } else {
                customerPhone = orderDetail.getDelivery_address().getPhone_number();
            }

            String customerAddress = orderDetail.getDelivery_address().getCustomer_location();

            try {
                if (orderDetail.getDelivery_address().getAddress_1() != null && !orderDetail.getDelivery_address().getAddress_1().isEmpty())
                    getAddress_1 = orderDetail.getDelivery_address().getAddress_1() + "\n";
                else
                    getAddress_1 = "";
                if (orderDetail.getDelivery_address().getAddress_2() != null && !orderDetail.getDelivery_address().getAddress_2().isEmpty())
                    address_2 = orderDetail.getDelivery_address().getAddress_2() + "\n";
                else
                    address_2 = "";
                city = orderDetail.getDelivery_address().getCity();
                post_code = orderDetail.getDelivery_address().getPost_code();
                country = orderDetail.getDelivery_address().getCountry();

                AddressToPrint = getAddress_1 + address_2 + city + " " + post_code;
            } catch (Exception e) {
                e.printStackTrace();
            }

            String orderPhone;
            if (orderDetail.getOrder_phone_number() != null && !orderDetail.getOrder_phone_number().isEmpty()) {
                orderPhone = orderDetail.getOrder_phone_number();
            } else {
                orderPhone = orderDetail.getDelivery_address().getPhone_number();
            }
            String footer = String.format("%s", "        Delivery Details        ") + "\n"
                    + String.format("%s", "--------------------------------") + "\n"
                    + String.format("%s", orderDetail.getDelivery_address().getCustomer_name()) + "\n"
                    + String.format("%s", orderPhone) + "\n"
                    + String.format("%s", orderDetail.getDelivery_address().getCustomer_location()) + "\n"
                    + String.format("Delivery Time:%s", orderDetail.getDelivery_date_time()) + "\n";
            Log.e("FOOTER >>\n", footer);

            if (true) {
                Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.easyfood_slip_icon);


                printPhoto(context, icon);
                if (logo != null) {
                    printPhoto(context, logo);
                } else {
                    try {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);

                        URL url = new URL(UserPreferences.get().getLoggedInResponse(context).getLogo());
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream input = connection.getInputStream();
                        Bitmap myBitmap = BitmapFactory.decodeStream(input);
                        printPhoto(context, myBitmap);

                    } catch (IOException e) {
                        // Log exception
                        Log.e("IOException", e.getMessage());
                        e.printStackTrace();
                    }
                }
                printNewLine();


                if (restaurantData != null) {
                    printCustom(restaurantData.getRestaurant_name().toUpperCase(), 1, 1);
                    printCustom("", 2, 1);
                    printCustom("ORDER NUMBER", 0, 1);
                    printCustom(orderDetail.getOrder_num(), 2, 1);
                    printCustom("", 2, 1);

                    String checkPayStatus = orderDetail.getPayment_mode();
                    try {
                        Log.e("PRINT>>\n", "UNPAID" + "\t" + orderDetail.getDelivery_option().toUpperCase());
                        if (checkPayStatus.equalsIgnoreCase("Cash")) {
                            printCustom("UNPAID " + " - " + orderDetail.getDelivery_option().toUpperCase(), 1, 1);
                        } else {
                            printCustom("PAID " + " - " + orderDetail.getDelivery_option().toUpperCase(), 1, 1);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    String timeDate = orderDetail.getOrder_date_time();
                    String dddate = Constants.getDateFromDateTime(orderDetail.getOrder_date_time(), "dd MMM yyyy hh:mm:ss", "dd-MMM") + " ";
                    String OrderTime = "", hr = "", min = "";
                    for (int i = 0; i < timeDate.length(); i++) {
                        char c = timeDate.charAt(i);
                        if (c == ' ') {
                            hr = timeDate.substring(i + 1, i + 3);
                        }
                        if (c == ':') {
                            min = timeDate.substring(i + 1, i + 3);
                            break;
                        }
                    }

                    OrderTime = dddate + hr + ":" + min;
                    printCustom("", 2, 1);
                    printCustom("ORDER TIME - " + OrderTime, 1, 1);


                    String DeliveryTime1 = orderDetail.getDelivery_date_time();
                    String DeliveryTime = "", hr1 = "", min1 = "";
                    String dtime2 = Constants.getDateFromDateTime(orderDetail.getDelivery_date_time(), "dd MMM yyyy hh:mm:ss", "dd-MMM") + " ";

                    for (int j = 0; j < DeliveryTime1.length(); j++) {
                        char d = DeliveryTime1.charAt(j);
                        if (d == ' ') {
                            hr1 = DeliveryTime1.substring(j + 1, j + 3);
                        }
                        if (d == ':') {
                            min1 = DeliveryTime1.substring(j + 1, j + 3);
                            break;
                        }
                    }
                    DeliveryTime = dtime2 + hr1 + ":" + min1;
                    printCustom("", 2, 1);
                    printCustom("DELIVERY TIME", 0, 1);
                    printCustom(DeliveryTime, 1, 1);
                    printCustom(line24Bold, 1, 1);
                    printCustom(customerName.toUpperCase(), 1, 1);


                    if (orderDetail.getDelivery_option().toUpperCase().equals("DELIVERY"))
                        addressFormatToPrint = AddressNameCorrect(customerAddress);
                    printCustom(AddressToPrint, 1, 1);
                    printCustom("", 2, 1);
                    printCustom(customerPhone, 1, 1);
                    printCustom(line24Bold, 1, 1);


                    makeCartDataNew(context, charCount, orderDetail.getCart());
                    printCustom(line24Bold, 1, 1);
                    printCustom(printSpaceBetweenTwoString("DISCOUNT", context.getResources().getString(R.string.currency2) + Constants.decimalFormat(Double.parseDouble(orderDetail.getDiscount_amount())), 29), 1, 1);

                    if (orderDetail.getDelivery_option().toUpperCase().equals("DELIVERY")) {
                        printCustom(printSpaceBetweenTwoString("DELIVERY FEE", context.getResources().getString(R.string.currency2) + Constants.decimalFormat(Double.parseDouble(orderDetail.getDelivery_charge())), 29), 0, 1);
                    }
                    printCustom("", 2, 1);


                    printCustom("TOTAL    " + context.getResources().getString(R.string.currency2) + Constants.decimalFormat(Double.parseDouble(orderDetail.getOrder_total())), 2, 1);

                    if (orderDetail.getOrder_notes() != null && !orderDetail.getOrder_notes().trim().isEmpty()) {
                        printCustom("NOTES", 3, 1);
                        printCustom(orderDetail.getOrder_notes(), 3, 1);

                    }
                    printCustom(line24Bold, 1, 1);

                    printCustom("PAYMENT BY " + orderDetail.getPayment_mode().toUpperCase(), 2, 1);
                    printCustom(line24Bold, 1, 1);

                    printCustom("", 2, 1);
                    printCustom("", 2, 1);
                    printCustom("", 2, 1);
                    printCustom("", 2, 1);
                    printCustom("", 2, 1);
                    printCustom("", 2, 1);
                    printCustom("", 2, 1);
                    printCustom("", 2, 1);
                    printCustom("", 2, 1);
                    printCustom("", 2, 1);

                }
            }
            try {
                if (deviceName.equals("castle")) {

                } else {
                    mmOutputStream.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.e("ddddddddddddd", "ddddddddddddd");
        }
    }

    private class MakeDataIngenico extends AsyncTask<Void, Void, String> {
        @NonNull
        final Context context;
        @Nullable
        final Bitmap logo;
        @NonNull
        final NewDetailBean.OrdersDetailsBean orderDetail;
        @NonNull
        final LoginResponse.Data restaurantData;
        int charCount = 35;
        Bitmap bitmap1 = null;
        String addressFormatToPrint = "";
        String getAddress_1 = "", address_2 = "", city = "", post_code = "", country = "", AddressToPrint = "";

        String line24BoldIngenico = "----------------------------------------------------";//52 char
        com.ingenico.sdk.device.printer.IPrinter printerIngenico;
        IPrinterEventListener listener;


        public MakeDataIngenico(@NonNull Context context, @Nullable Bitmap logo, @NonNull NewDetailBean.OrdersDetailsBean orderDetail, @NonNull LoginResponse.Data restaurantData) {
            this.context = context;
            this.logo = logo;
            this.orderDetail = orderDetail;
            this.restaurantData = restaurantData;
            Log.e("1111111111111", "1111111111111111");
        }

        @Override
        protected String doInBackground(Void... voids) {
            Log.e("222222222222", "222222222222222");



            strAr1.clear();
            //strAr1.add(makeCartData(context, charCount, orderDetail.getCart()));

            String customerName = orderDetail.getDelivery_address().getCustomer_name();
            String customerPhone;
            if (orderDetail.getOrder_phone_number() != null && !orderDetail.getOrder_phone_number().isEmpty()) {
                customerPhone = orderDetail.getOrder_phone_number();
            } else {
                customerPhone = orderDetail.getDelivery_address().getPhone_number();
            }

            String customerAddress = orderDetail.getDelivery_address().getCustomer_location();

            try {
                if (orderDetail.getDelivery_address().getAddress_1() != null && !orderDetail.getDelivery_address().getAddress_1().isEmpty())
                    getAddress_1 = orderDetail.getDelivery_address().getAddress_1() + "\n";
                else
                    getAddress_1 = "";
                if (orderDetail.getDelivery_address().getAddress_2() != null && !orderDetail.getDelivery_address().getAddress_2().isEmpty())
                    address_2 = orderDetail.getDelivery_address().getAddress_2() + "\n";
                else
                    address_2 = "";
                city = orderDetail.getDelivery_address().getCity();
                post_code = orderDetail.getDelivery_address().getPost_code();
                country = orderDetail.getDelivery_address().getCountry();

                AddressToPrint = getAddress_1 + address_2 + city + " " + post_code;


            } catch (Exception e) {
                e.printStackTrace();
            }

            String orderPhone;
            if (orderDetail.getOrder_phone_number() != null && !orderDetail.getOrder_phone_number().isEmpty()) {
                orderPhone = orderDetail.getOrder_phone_number();
            } else {
                orderPhone = orderDetail.getDelivery_address().getPhone_number();
            }

            centerString("DELIVERY DETAILS");
            strAr1.add(line24BoldIngenico);
            centerString(orderDetail.getDelivery_address().getCustomer_name());
            centerString(orderPhone);
            centerString(orderDetail.getDelivery_address().getCustomer_location());
            centerString("Delivery Time");
            centerString(orderDetail.getDelivery_date_time());

            strAr1.add("");

            if (restaurantData != null) {
                centerString(restaurantData.getRestaurant_name().toUpperCase());
                strAr1.add("");
                centerString("ORDER NUMBER");
                centerString(orderDetail.getOrder_num());
                strAr1.add("");

                String checkPayStatus = orderDetail.getPayment_mode();
                try {
                    Log.e("PRINT>>\n", "UNPAID" + "\t" + orderDetail.getDelivery_option().toUpperCase());
                    if (checkPayStatus.equalsIgnoreCase("Cash")) {
                        centerString("UNPAID " + " - " + orderDetail.getDelivery_option().toUpperCase());
                    } else {
                        centerString("PAID " + " - " + orderDetail.getDelivery_option().toUpperCase());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                String timeDate = orderDetail.getOrder_date_time();
                String dddate = Constants.getDateFromDateTime(orderDetail.getOrder_date_time(), "dd MMM yyyy hh:mm:ss", "dd-MMM") + " ";
                String OrderTime = "", hr = "", min = "";
                for (int i = 0; i < timeDate.length(); i++) {
                    char c = timeDate.charAt(i);
                    if (c == ' ') {
                        hr = timeDate.substring(i + 1, i + 3);
                    }
                    if (c == ':') {
                        min = timeDate.substring(i + 1, i + 3);
                        break;
                    }
                }

                OrderTime = dddate + hr + ":" + min;
                strAr1.add("");
                centerString("ORDER TIME");
                centerString(OrderTime);



                String DeliveryTime1 = orderDetail.getDelivery_date_time();
                String DeliveryTime = "", hr1 = "", min1 = "";
                String dtime2 = Constants.getDateFromDateTime(orderDetail.getDelivery_date_time(), "dd MMM yyyy hh:mm:ss", "dd-MMM") + " ";

                for (int j = 0; j < DeliveryTime1.length(); j++) {
                    char d = DeliveryTime1.charAt(j);
                    if (d == ' ') {
                        hr1 = DeliveryTime1.substring(j + 1, j + 3);
                    }
                    if (d == ':') {
                        min1 = DeliveryTime1.substring(j + 1, j + 3);
                        break;
                    }
                }
                DeliveryTime = dtime2 + hr1 + ":" + min1;
                strAr1.add("");
                centerString("DELIVERY TIME");
                centerString(DeliveryTime);
                strAr1.add(line24BoldIngenico);
                centerString(customerName.toUpperCase());



                if (orderDetail.getDelivery_option().toUpperCase().equals("DELIVERY"))
                    addressFormatToPrint = AddressNameCorrect(customerAddress);
                // strAr1.add(centerString(52,AddressToPrint));
                centerString(customerPhone);
                strAr1.add(line24BoldIngenico);


                makeCartDataNewIngenico(context, charCount, orderDetail.getCart());
                strAr1.add(line24BoldIngenico);
                printSpaceBetweenTwoStringIngenico("DISCOUNT",context.getResources().getString(R.string.currency2) +
                        Constants.decimalFormat(Double.parseDouble(orderDetail.getDiscount_amount())));

                if (orderDetail.getDelivery_option().toUpperCase().equals("DELIVERY")) {
                    printSpaceBetweenTwoStringIngenico("DELIVERY FEE",context.getResources().getString(R.string.currency2) +
                            Constants.decimalFormat(Double.parseDouble(orderDetail.getDelivery_charge())));


                }
                strAr1.add("");

                printSpaceBetweenTwoStringIngenico("TOTAL",context.getResources().getString(R.string.currency2) +
                        Constants.decimalFormat(Double.parseDouble(orderDetail.getOrder_total())));
//
                if (orderDetail.getOrder_notes() != null && !orderDetail.getOrder_notes().trim().isEmpty()) {
                    centerString("FOOD NOTES");
                    strAr1.add(orderDetail.getOrder_notes());

                }
                strAr1.add(line24BoldIngenico);
                centerString("PAYMENT BY " + orderDetail.getPayment_mode().toUpperCase());


                strAr1.add(line24BoldIngenico);

                strAr1.add("");
                strAr1.add("");

            }


            Bitmap bitmap2 = createMyBitmap(350, R.drawable.easyfood_slip_icon, 380, strAr1, 10);


           if (Helper.getDeviceName().contains("DX8000")) {
                byte[] bwImage = BitmapUtils.bitmapToBWBytes(bitmap2, 127);
                try {
                    printerIngenico = Printer.getInstance(context);
                    listener = result -> {
                        if (result.getStatus() == PrinterStatus.PRINTER_STATUS_OK) {
                            if (!result.getPrintingState()) {
                                Toast.makeText(context, "Printing successful", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Printing in progress", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Failure:" + result.toString(), Toast.LENGTH_SHORT).show();
                        }
                    };
                    printerIngenico.registerPrinterEventListener(listener);

                    // Your existing printing logic goes here
                    if (bwImage != null) {
                        if (listener != null) {
                            if (printerIngenico != null) {
                                PrinterRequest request = printerIngenico.print(bwImage, bitmap2.getWidth(), bitmap2.getHeight());

                                // Handle the result of the printing operation
                                if (!request.isAccepted()) {
                                    printerIngenico.unregisterPrinterEventListener(listener);
                                    //  Toast.makeText(context, request.getStatus().getTextValue(), Toast.LENGTH_SHORT).show();

                                } else {
                                    //Toast.makeText(context, "Printing...", Toast.LENGTH_SHORT).show();

                                }

                            } else {
                                Toast.makeText(context, "Print is null", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Toast.makeText(context, "Listener is null", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (IngenicoException e) {
                    e.printStackTrace();
                }
            } else if (Helper.getDeviceName().contains("Saturn1000F2")) {
                print.initPage(bitmap2.getHeight()+100);
                //bitmap2 = print.encodeToBitmap("12ASDFSS34", print.QR_CODE, 300, 3000);
                print.drawImage(bitmap2, 0, 0);
                print.printPage();
            }else if (Helper.getDeviceName().contains("A920Pro") || Helper.getDeviceName().contains("Lephone")) {
               try {
                   IDAL paxIdal = NeptuneLiteUser.getInstance().getDal(context);
                   com.pax.dal.IPrinter prn = paxIdal.getPrinter();
                   prn.init();
                   /*prn.fontSet(EFontTypeAscii.FONT_16_16, EFontTypeExtCode.FONT_16_16);
                   prn.printStr("Hello", null);
                   prn.spaceSet((byte)0,(byte)0);

                   prn.step(0);
                   prn.invert(true);
                   prn.leftIndent(10);
                   */
                   prn.printBitmap(bitmap2);
                   int apiResult = paxIdal.getPrinter().start();

                   switch (apiResult) {
                       case 0:
                           // Submission successfully made.
                           break;
                       case 1:
                           // Busy, so far so good.
                           break;
                       case 2:
                           // Out of paper.
                           break;

                       default:
                           // Hmm.
                   }//switch
                   do {
                       // Check every quarter-second for result of print.
                       Thread.sleep(250);
                       apiResult = prn.getStatus();
                   } while (apiResult == 1);
// Paper cutter.
                   try {
                       int cutMode = prn.getCutMode();
                       if ((cutMode == 0) || (cutMode == 2)) {
                           // 0=full, or 2=partial/full => full cut.
                           prn.cutPaper(0);
                       }
                       else if (cutMode == 1) {
                           // 1=partial only => partial cut.
                           prn.cutPaper(1);
                       }
                   }
                   catch (PrinterDevException pdex) {
                   }

               } catch (Exception ex) {
                   Toast.makeText(context, "Error initializing printer: " + ex.toString(), Toast.LENGTH_SHORT).show();
               }
            }
            return "done";
        }

        @Override
        protected void onPostExecute(String cartData) {
            super.onPostExecute(cartData);
            ///Log.e("cccccccccccc", "cccccccccccc");
            //Log.e("Cart DATA\n", cartData);
        }

    }
    public static void centerString ( String inputString) {


        if (inputString.length() <= 30) {
            strAr1.add(String.format("%-" + 30  + "s", String.format("%" + (inputString.length() + (30 - inputString.length()) / 2) + "s", inputString)));

        } else {
            // Splitting the string into two lines
            int length = inputString.length();
            int middle = length / 2;
            String line1 = inputString.substring(0, middle);
            String line2 = inputString.substring(middle);

            // Center-aligning each line
            int spaces = (30 - line1.length()) / 2;
            String centeredLine1 = String.format("%" + (line1.length() + spaces) + "s", line1);
            String centeredLine2 = String.format("%" + (line2.length() + spaces) + "s", line2);

            // Printing the centered lines
            strAr1.add(String.format("%-" + 30  + "s", String.format("%" + (centeredLine1.length() + (30 - centeredLine1.length()) / 2) + "s", centeredLine1)));
            strAr1.add(String.format("%-" + 30  + "s", String.format("%" + (centeredLine2.length() + (30 - centeredLine2.length()) / 2) + "s", centeredLine2)));

        }
    }
    public static String justifyLeftRight(String one, String two){
        Formatter fmt = new Formatter();

        fmt.format("%-12s %12s", one, two);
        return fmt.toString();
    }
    public static String getPaddedString(String str, char paddingChar) {
        if (str == null) {
            throw new NullPointerException("Can not add padding in null String!");
        }

        int maxPadding = 20;//This is what you have to decide
        int length = str.length();
        int padding = (maxPadding - length) / 2;//decide left and right padding
        if (padding <= 0) {
            return str;// return actual String if padding is less than or equal to 0
        }

        String empty = "", hash = "#";//hash is used as a place holder

        // extra character in case of String with even length
        int extra = (length % 2 == 0) ? 1 : 0;

        String leftPadding = "%" + padding + "s";
        String rightPadding = "%" + (padding - extra) + "s";

        String strFormat = leftPadding + "%s" + rightPadding;
        String formattedString = String.format(strFormat, empty, hash, empty);

        //Replace space with * and hash with provided String
        String paddedString = formattedString.replace(' ', paddingChar).replace(hash, str);
        return paddedString;
    }
    private static void makeCartDataNew(Context context, int paperRowCharCount, NewDetailBean.OrdersDetailsBean.Cart data) {
        StringBuilder orderItemData = new StringBuilder();
        int charCount = paperRowCharCount;
        int serialNoCount = 0;
        int qtyCharCount = 0;
        int priceCharCount = 8;
        int charCountNew = (charCount - serialNoCount) - qtyCharCount;
        charCountNew = (charCountNew - priceCharCount) - priceCharCount;
        int slNo = 0;


        for (int i = 0; i < data.getMenu().size(); i++) {
            String productFirstLine = null;
            String productSecondLine = null;


            if (data.getMenu().get(i) != null)
                printCustom(data.getMenu().get(i).getQty() + "x " + data.getMenu().get(i).getName(), 1, 1);

            /*--------------Menu Product Modifiers----------------------------------------*/

            StringBuilder menuOrderItemData = new StringBuilder();
            if (data.getMenu().get(i).getOptions().getProductModifiers() != null) {
                //  List<ProductModifier> productModifiers = data.getMenu().get(i).getOptions().getProductModifiers();
                for (int j = 0; j < data.getMenu().get(i).getOptions().getProductModifiers().size(); j++) {
                    for (int k = 0; k < data.getMenu().get(i).getOptions().getProductModifiers().get(j).getModifierProducts().size(); k++) {

                        if (data.getMenu().get(i).getOptions().getProductModifiers().get(j).getModifierProducts().get(k) != null) {
                            String productModiProductName = data.getMenu().get(i).getOptions().getProductModifiers().get(j).getModifierProducts().get(k).getQuantity() + "x " + data.getMenu().get(i).getOptions().getProductModifiers().get(j).getModifierProducts().get(k).getProductName();
                            if (productModiProductName.length() > (charCountNew * 2)) {
                                productFirstLine = productModiProductName.substring(0, (charCountNew));
                                productSecondLine = productModiProductName.substring(charCountNew, ((charCountNew * 2) - 3)) + "... ";
                            } else {
                                if (productModiProductName.length() > charCountNew) {
                                    productFirstLine = productModiProductName.substring(0, (charCountNew));
                                    productSecondLine = productModiProductName.substring((charCountNew), productModiProductName.length());
                                } else {
                                    productFirstLine = productModiProductName;
                                    for (int b = 0; b < (charCountNew - productModiProductName.length()); b++) {
                                        productFirstLine += " ";
                                    }
                                }
                            }
                            if (productSecondLine == null) {
                                String amountSpace = createPriceSpace(priceCharCount, Double.parseDouble(data.getMenu().get(i).getOptions().getProductModifiers().get(j).getModifierProducts().get(k).getModifierProductPrice()));

                                menuOrderItemData.append(
                                        productFirstLine
                                                + String.format(amountSpace + context.getResources().getString(R.string.currency2) + "%.2f", data.getMenu().get(i).getOptions().getProductModifiers().get(j).getModifierProducts().get(k).getModifierProductPrice())
                                                + "\n"
                                );
                            } else {
                                String amountSpace = createPriceSpace(priceCharCount, data.getMenu().get(i).getPrice());

                                menuOrderItemData.append(productFirstLine

                                        + String.format(amountSpace + context.getResources().getString(R.string.currency2) + "%.2f", data.getMenu().get(i).getOptions().getProductModifiers().get(j).getModifierProducts().get(k).getModifierProductPrice())
                                        + "\n"
                                );
                            }
                        }

                    }
                }
            }

            /*  --------------Menu Meal Products----------------------------------------*/
            if (data.getMenu().get(i).getOptions().getMealProducts() != null) {
                for (int j = 0; j < data.getMenu().get(i).getOptions().getMealProducts().size(); j++) {

                    if (data.getMenu().get(i).getOptions().getMealProducts().get(j).getProductNameAPP() != null) {
                        String productModiProductName = data.getMenu().get(i).getOptions().getMealProducts().get(j).getQuantity() + "x " + data.getMenu().get(i).getOptions().getMealProducts().get(j).getProductNameAPP();
                        if (productModiProductName.length() > (charCountNew * 2)) {
                            productFirstLine = productModiProductName.substring(0, (charCountNew));
                            productSecondLine = productModiProductName.substring(charCountNew, ((charCountNew * 2) - 3)) + "... ";
                        } else {
                            if (productModiProductName.length() > charCountNew) {
                                productFirstLine = productModiProductName.substring(0, (charCountNew));
                                productSecondLine = productModiProductName.substring((charCountNew), productModiProductName.length());
                            } else {
                                productFirstLine = productModiProductName;
                                for (int b = 0; b < (charCountNew - productModiProductName.length()); b++) {
                                    productFirstLine += " ";
                                }
                            }
                        }
                        if (productSecondLine == null) {
                            String amountSpace;
                            if (data.getMenu().get(i).getOptions().getMealProducts().get(j).getAmount() != null)
                                amountSpace = createPriceSpace(priceCharCount, Double.parseDouble(data.getMenu().get(i).getOptions().getMealProducts().get(j).getAmount()));
                            else
                                amountSpace = createPriceSpace(priceCharCount, Double.parseDouble("0"));

                            if (data.getMenu().get(i).getOptions().getMealProducts().get(j).getAmount() != null) {
                                menuOrderItemData.append(
                                        productFirstLine
                                                + String.format(amountSpace + context.getResources().getString(R.string.currency2) + "%.2f", Double.parseDouble(data.getMenu().get(i).getOptions().getMealProducts().get(j).getAmount()))
                                                + "\n"
                                );
                            } else {
                                menuOrderItemData.append(
                                        productFirstLine
                                                + String.format(amountSpace + context.getResources().getString(R.string.currency2) + "%.2f", Double.parseDouble("0"))
                                                + "\n"
                                );
                            }
                        } else {

                            String amountSpace = createPriceSpace(priceCharCount, data.getMenu().get(i).getPrice());

                            if (data.getMenu().get(i).getOptions().getMealProducts().get(j).getAmount() != null) {
                                menuOrderItemData.append(productFirstLine

                                        + String.format(amountSpace + context.getResources().getString(R.string.currency2) + "%.2f", Double.parseDouble(data.getMenu().get(i).getOptions().getMealProducts().get(j).getAmount()))
                                        + "\n"
                                );
                            } else {
                                menuOrderItemData.append(productFirstLine

                                        + String.format(amountSpace + context.getResources().getString(R.string.currency2) + "%.2f", Double.parseDouble("0"))
                                        + "\n"
                                );
                            }
                        }
                    }
                    for (int k = 0; k < data.getMenu().get(i).getOptions().getMealProducts().get(j).getSizeModifiers().size(); k++) {

                        for (int l = 0; l < data.getMenu().get(i).getOptions().getMealProducts().get(j).getSizeModifiers().get(k).getSizeModifierProducts().size(); l++) {

                            if (data.getMenu().get(i).getOptions().getMealProducts().get(j).getSizeModifiers().get(k) != null) {
                                String productModiProductName = data.getMenu().get(i).getOptions().getMealProducts().get(j).getSizeModifiers().get(k).getSizeModifierProducts().get(l).getQuantity() + "x " + data.getMenu().get(i).getOptions().getMealProducts().get(j).getSizeModifiers().get(k).getSizeModifierProducts().get(l).getProductName();
                                if (productModiProductName.length() > (charCountNew * 2)) {
                                    productFirstLine = productModiProductName.substring(0, (charCountNew));
                                    productSecondLine = productModiProductName.substring(charCountNew, ((charCountNew * 2) - 3)) + "... ";
                                } else {
                                    if (productModiProductName.length() > charCountNew) {
                                        productFirstLine = productModiProductName.substring(0, (charCountNew));
                                        productSecondLine = productModiProductName.substring((charCountNew), productModiProductName.length());
                                    } else {
                                        productFirstLine = productModiProductName;
                                        for (int b = 0; b < (charCountNew - productModiProductName.length()); b++) {
                                            productFirstLine += " ";
                                        }
                                    }
                                }
                                if (productSecondLine == null) {
                                    String amountSpace = createPriceSpace(priceCharCount, Double.parseDouble(data.getMenu().get(i).getOptions().getMealProducts().get(j).getSizeModifiers().get(k).getSizeModifierProducts().get(l).getAmount()));

                                    menuOrderItemData.append(
                                            productFirstLine
                                                    + String.format(amountSpace + context.getResources().getString(R.string.currency2) + "%.2f", Double.parseDouble(data.getMenu().get(i).getOptions().getMealProducts().get(j).getSizeModifiers().get(k).getSizeModifierProducts().get(l).getAmount()))
                                                    + "\n"
                                    );
                                } else {
                                    String amountSpace = createPriceSpace(priceCharCount, data.getMenu().get(i).getPrice());

                                    menuOrderItemData.append(productFirstLine

                                            + String.format(amountSpace + context.getResources().getString(R.string.currency2) + "%.2f", Double.parseDouble(data.getMenu().get(i).getOptions().getMealProducts().get(j).getSizeModifiers().get(k).getSizeModifierProducts().get(l).getAmount()))
                                            + "\n"
                                    );
                                }
                            }

                        }
                    }
                }
            }
            /* --------------Menu Size----------------------------------------*/

            if (data.getMenu().get(i).getOptions().getSize() != null && data.getMenu().get(i).getOptions().getSize().getProductSizeName() != null) {

                if (data.getMenu().get(i).getOptions().getSize() != null) {
                    String productModiProductName = data.getMenu().get(i).getOptions().getSize().getQuantity() + "x " + data.getMenu().get(i).getOptions().getSize().getProductSizeName();
                    if (productModiProductName.length() > (charCountNew * 2)) {
                        productFirstLine = productModiProductName.substring(0, (charCountNew));
                        productSecondLine = productModiProductName.substring(charCountNew, ((charCountNew * 2) - 3)) + "... ";
                    } else {
                        if (productModiProductName.length() > charCountNew) {
                            productFirstLine = productModiProductName.substring(0, (charCountNew));
                            productSecondLine = productModiProductName.substring((charCountNew), productModiProductName.length());
                        } else {
                            productFirstLine = productModiProductName;
                            for (int b = 0; b < (charCountNew - productModiProductName.length()); b++) {
                                productFirstLine += " ";
                            }
                        }
                    }
                    if (productSecondLine == null) {
                        String amountSpace = createPriceSpace(priceCharCount, data.getMenu().get(i).getOptions().getSize().getProductSizePrice());

                        menuOrderItemData.append(
                                productFirstLine
                                        + String.format(amountSpace + context.getResources().getString(R.string.currency2) + "%.2f", data.getMenu().get(i).getOptions().getSize().getProductSizePrice())
                                        + "\n"
                        );
                    } else {
                        String amountSpace = createPriceSpace(priceCharCount, data.getMenu().get(i).getPrice());

                        menuOrderItemData.append(productFirstLine

                                + String.format(amountSpace + context.getResources().getString(R.string.currency2) + "%.2f", data.getMenu().get(i).getOptions().getSize().getProductSizePrice())
                                + "\n"
                        );
                    }
                }


                for (int j = 0; j < data.getMenu().get(i).getOptions().getSize().getSizemodifiers().size(); j++) {
                    // List<SizeModifierProduct> sizeModifierProducts = data.getMenu().get(i).getOptions().getSizeBeans().getSizemodifiers().get(j).getSizeModifierProducts();
                    for (int k = 0; k < data.getMenu().get(i).getOptions().getSize().getSizemodifiers().get(j).getSizeModifierProducts().size(); k++) {

                        if (data.getMenu().get(i).getOptions().getSize().getSizemodifiers().get(j).getSizeModifierProducts().get(k) != null) {
                            String productModiProductName = data.getMenu().get(i).getOptions().getSize().getSizemodifiers().get(j).getSizeModifierProducts().get(k).getQuantity() + "x " + data.getMenu().get(i).getOptions().getSize().getSizemodifiers().get(j).getSizeModifierProducts().get(k).getProductName();
                            if (productModiProductName.length() > (charCountNew * 2)) {
                                productFirstLine = productModiProductName.substring(0, (charCountNew));
                                productSecondLine = productModiProductName.substring(charCountNew, ((charCountNew * 2) - 3)) + "... ";
                            } else {
                                if (productModiProductName.length() > charCountNew) {
                                    productFirstLine = productModiProductName.substring(0, (charCountNew));
                                    productSecondLine = productModiProductName.substring((charCountNew), productModiProductName.length());
                                } else {
                                    productFirstLine = productModiProductName;
                                    for (int b = 0; b < (charCountNew - productModiProductName.length()); b++) {
                                        productFirstLine += " ";
                                    }
                                }
                            }
                            if (productSecondLine == null) {
                                String amountSpace = createPriceSpace(priceCharCount, data.getMenu().get(i).getOptions().getSize().getSizemodifiers().get(j).getSizeModifierProducts().get(k).getAmount());

                                menuOrderItemData.append(
                                        productFirstLine
                                                + String.format(amountSpace + context.getResources().getString(R.string.currency2) + "%.2f", data.getMenu().get(i).getOptions().getSize().getSizemodifiers().get(j).getSizeModifierProducts().get(k).getAmount())
                                                + "\n"
                                );
                            } else {
                                String amountSpace = createPriceSpace(priceCharCount, data.getMenu().get(i).getPrice());

                                menuOrderItemData.append(productFirstLine

                                        + String.format(amountSpace + context.getResources().getString(R.string.currency2) + "%.2f", data.getMenu().get(i).getOptions().getSize().getSizemodifiers().get(j).getSizeModifierProducts().get(k).getAmount())
                                        + "\n"
                                );
                            }
                        }
                    }
                }
            }

            printCustom(menuOrderItemData.toString(), 0, 1);
            printCustom(String.format(context.getResources().getString(R.string.currency2) + "%.2f", data.getMenu().get(i).getPrice()) + "     ", 0, 2);
            printNewLine();

        }
    }

    private static String makeCartDataNewIngenico(Context context, int paperRowCharCount, NewDetailBean.OrdersDetailsBean.Cart data) {
        StringBuilder orderItemData = new StringBuilder();
        int charCount = paperRowCharCount;
        int serialNoCount = 0;
        int qtyCharCount = 0;
        int priceCharCount = 8;
        int charCountNew = (charCount - serialNoCount) - qtyCharCount;
        charCountNew = (charCountNew - priceCharCount) - priceCharCount;
        int slNo = 0;
        String returnData = "";

        for (int i = 0; i < data.getMenu().size(); i++) {

            if (data.getMenu().get(i) != null)
                returnData = returnData + (data.getMenu().get(i).getQty() + "x " + data.getMenu().get(i).getName());

            if (data.getMenu().get(i).getOptions().getMealProducts() != null) {
                strAr1.add(data.getMenu().get(i).getQty() + "x " + data.getMenu().get(i).getName());

            }else{
                printSpaceBetweenTwoStringIngenico(data.getMenu().get(i).getQty() + "x " + data.getMenu().get(i).getName(),
                        context.getResources().getString(R.string.currency2) +
                                Constants.decimalFormat(data.getMenu().get(i).getPrice()));

            }
            /*--------------Menu Product Modifiers----------------------------------------*/

            StringBuilder menuOrderItemData = new StringBuilder();
            if (data.getMenu().get(i).getOptions().getProductModifiers() != null) {
                //  List<ProductModifier> productModifiers = data.getMenu().get(i).getOptions().getProductModifiers();
                for (int j = 0; j < data.getMenu().get(i).getOptions().getProductModifiers().size(); j++) {
                    for (int k = 0; k < data.getMenu().get(i).getOptions().getProductModifiers().get(j).getModifierProducts().size(); k++) {

                        if (data.getMenu().get(i).getOptions().getProductModifiers().get(j).getModifierProducts().get(k) != null) {
                            String productModiProductName = data.getMenu().get(i).getOptions().getProductModifiers().get(j).getModifierProducts().get(k).getQuantity() + "x " + data.getMenu().get(i).getOptions().getProductModifiers().get(j).getModifierProducts().get(k).getProductName();

                            printSpaceBetweenTwoStringIngenico("   "+productModiProductName,
                                    context.getResources().getString(R.string.currency2)+
                                            Constants.decimalFormat(Double.parseDouble(data.getMenu().get(i).getOptions().getProductModifiers().get(j).getModifierProducts().get(k).getModifierProductPrice())));


                        }

                    }
                }
            }

            /*  --------------Menu Meal Products----------------------------------------*/
            if (data.getMenu().get(i).getOptions().getMealProducts() != null) {
                for (int j = 0; j < data.getMenu().get(i).getOptions().getMealProducts().size(); j++) {

                    if (data.getMenu().get(i).getOptions().getMealProducts().get(j).getProductNameAPP() != null) {
                        String productModiProductName = data.getMenu().get(i).getOptions().getMealProducts().get(j).getQuantity() + "x " + data.getMenu().get(i).getOptions().getMealProducts().get(j).getProductNameAPP();
                        if (data.getMenu().get(i).getOptions().getMealProducts().get(j).getAmount() != null) {
                            printSpaceBetweenTwoStringIngenico("   "+productModiProductName,
                                    context.getResources().getString(R.string.currency2) +
                                            Constants.decimalFormat(Double.parseDouble(data.getMenu().get(i).getOptions().getMealProducts().get(j).getAmount())));
                        }
                    }
                    for (int k = 0; k < data.getMenu().get(i).getOptions().getMealProducts().get(j).getSizeModifiers().size(); k++) {

                        for (int l = 0; l < data.getMenu().get(i).getOptions().getMealProducts().get(j).getSizeModifiers().get(k).getSizeModifierProducts().size(); l++) {

                            if (data.getMenu().get(i).getOptions().getMealProducts().get(j).getSizeModifiers().get(k) != null) {
                                String productModiProductName = data.getMenu().get(i).getOptions().getMealProducts().get(j).getSizeModifiers().get(k).getSizeModifierProducts().get(l).getQuantity() + "x " + data.getMenu().get(i).getOptions().getMealProducts().get(j).getSizeModifiers().get(k).getSizeModifierProducts().get(l).getProductName();
                                printSpaceBetweenTwoStringIngenico("   "+productModiProductName,
                                        context.getResources().getString(R.string.currency2) +
                                                Constants.decimalFormat(Double.parseDouble(data.getMenu().get(i).getOptions().getMealProducts().get(j).getSizeModifiers().get(k).getSizeModifierProducts().get(l).getAmount())));


                            }

                        }
                    }
                }
            }
            /* --------------Menu Size----------------------------------------*/

            if (data.getMenu().get(i).getOptions().getSize() != null && data.getMenu().get(i).getOptions().getSize().getProductSizeName() != null) {

                if (data.getMenu().get(i).getOptions().getSize() != null) {
                    String productModiProductName = data.getMenu().get(i).getOptions().getSize().getQuantity() + "x " + data.getMenu().get(i).getOptions().getSize().getProductSizeName();

                    printSpaceBetweenTwoStringIngenico("   "+productModiProductName,
                            context.getResources().getString(R.string.currency2) +
                                    Constants.decimalFormat(data.getMenu().get(i).getOptions().getSize().getProductSizePrice()));


                }


                for (int j = 0; j < data.getMenu().get(i).getOptions().getSize().getSizemodifiers().size(); j++) {
                    // List<SizeModifierProduct> sizeModifierProducts = data.getMenu().get(i).getOptions().getSizeBeans().getSizemodifiers().get(j).getSizeModifierProducts();
                    for (int k = 0; k < data.getMenu().get(i).getOptions().getSize().getSizemodifiers().get(j).getSizeModifierProducts().size(); k++) {

                        if (data.getMenu().get(i).getOptions().getSize().getSizemodifiers().get(j).getSizeModifierProducts().get(k) != null) {
                            String productModiProductName = data.getMenu().get(i).getOptions().getSize().getSizemodifiers().get(j).getSizeModifierProducts().get(k).getQuantity() + "x " + data.getMenu().get(i).getOptions().getSize().getSizemodifiers().get(j).getSizeModifierProducts().get(k).getProductName();

                            printSpaceBetweenTwoStringIngenico("   "+productModiProductName,
                                    context.getResources().getString(R.string.currency2) +
                                            Constants.decimalFormat(data.getMenu().get(i).getOptions().getSize().getSizemodifiers().get(j).getSizeModifierProducts().get(k).getAmount()));


                        }
                    }
                }
            }

            returnData = returnData + "" + (menuOrderItemData.toString());
            returnData = returnData + "\n" + (String.format(context.getResources().getString(R.string.currency2) + "%.2f", data.getMenu().get(i).getPrice()));
            returnData = returnData + "\n";

            strAr1.add("----------------------------------------------------");


        }
        return returnData;

    }

    private static String makeCartData(Context context, int paperRowCharCount, NewDetailBean.OrdersDetailsBean.Cart data) {
        StringBuilder orderItemData = new StringBuilder();
        int charCount = paperRowCharCount;
        int serialNoCount = 0;
        int qtyCharCount = 0;
        int priceCharCount = 8;
        int charCountNew = (charCount - serialNoCount) - qtyCharCount;
        charCountNew = (charCountNew - priceCharCount) - priceCharCount;
        int slNo = 0;

        for (int i = 0; i < data.getMenu().size(); i++) {
            String productFirstLine = null;
            String productSecondLine = null;

            if (data.getMenu().get(i) != null) {
                String productName = data.getMenu().get(i).getQty() + "x " + data.getMenu().get(i).getName();
                if (productName.length() > (charCountNew * 2)) {
                    productFirstLine = productName.substring(0, (charCountNew));
                    productSecondLine = productName.substring(charCountNew, ((charCountNew * 2) - 3)) + "... ";
                } else {
                    if (productName.length() > charCountNew) {
                        productFirstLine = productName.substring(0, (charCountNew));
                        productSecondLine = productName.substring((charCountNew), productName.length());
                    } else {
                        productFirstLine = productName;
                        for (int j = 0; j < (charCountNew - productName.length()); j++) {
                            productFirstLine += " ";
                        }
                    }
                }
                if (productSecondLine == null) {
                    String amountSpace = createPriceSpace(priceCharCount, data.getMenu().get(i).getPrice());
                    orderItemData.append(
                            productFirstLine
                                    + String.format(amountSpace + context.getResources().getString(R.string.currency2) + "%.2f", data.getMenu().get(i).getPrice())
                                    + "\n"
                    );
                } else {
                    String amountSpace = createPriceSpace(priceCharCount, data.getMenu().get(i).getPrice());
                    orderItemData.append(productFirstLine

                            + String.format(amountSpace + context.getResources().getString(R.string.currency2) + "%.2f", data.getMenu().get(i).getPrice())
                            + "\n"
                    );
                    orderItemData.append("    " + productSecondLine + "\n");
                }
            }

            /*--------------Menu Product Modifiers----------------------------------------*/

            if (data.getMenu().get(i).getOptions().getProductModifiers() != null) {
                for (int j = 0; j < data.getMenu().get(i).getOptions().getProductModifiers().size(); j++) {
                    for (int k = 0; k < data.getMenu().get(i).getOptions().getProductModifiers().get(j).getModifierProducts().size(); k++) {

                        if (data.getMenu().get(i).getOptions().getProductModifiers().get(j).getModifierProducts().get(k) != null) {
                            String productModiProductName = data.getMenu().get(i).getOptions().getProductModifiers().get(j).getModifierProducts().get(k).getQuantity() + "x " + data.getMenu().get(i).getOptions().getProductModifiers().get(j).getModifierProducts().get(k).getProductName();
                            if (productModiProductName.length() > (charCountNew * 2)) {
                                productFirstLine = productModiProductName.substring(0, (charCountNew));
                                productSecondLine = productModiProductName.substring(charCountNew, ((charCountNew * 2) - 3)) + "... ";
                            } else {
                                if (productModiProductName.length() > charCountNew) {
                                    productFirstLine = productModiProductName.substring(0, (charCountNew));
                                    productSecondLine = productModiProductName.substring((charCountNew), productModiProductName.length());
                                } else {
                                    productFirstLine = productModiProductName;
                                    for (int b = 0; b < (charCountNew - productModiProductName.length()); b++) {
                                        productFirstLine += " ";
                                    }
                                }
                            }
                            if (productSecondLine == null) {
                                String amountSpace = createPriceSpace(priceCharCount, Double.parseDouble(data.getMenu().get(i).getOptions().getProductModifiers().get(j).getModifierProducts().get(k).getModifierProductPrice()));

                                orderItemData.append(
                                        productFirstLine
                                                + String.format(amountSpace + context.getResources().getString(R.string.currency2) + "%.2f", data.getMenu().get(i).getOptions().getProductModifiers().get(j).getModifierProducts().get(k).getModifierProductPrice())
                                                + "\n"
                                );
                            } else {
                                String amountSpace = createPriceSpace(priceCharCount, data.getMenu().get(i).getPrice());

                                orderItemData.append(productFirstLine

                                        + String.format(amountSpace + context.getResources().getString(R.string.currency2) + "%.2f", data.getMenu().get(i).getOptions().getProductModifiers().get(j).getModifierProducts().get(k).getModifierProductPrice())
                                        + "\n"
                                );
                                orderItemData.append("    " + productSecondLine + "\n");
                            }
                        }
                    }
                }
            }

            /*  --------------Menu Meal Products----------------------------------------*/
            if (data.getMenu().get(i).getOptions().getMealProducts() != null) {
                for (int j = 0; j < data.getMenu().get(i).getOptions().getMealProducts().size(); j++) {

                    if (data.getMenu().get(i).getOptions().getMealProducts().get(j) != null) {
                        if (data.getMenu().get(i).getOptions().getMealProducts().get(j).getProductNameAPP() != null) {
                            String productModiProductName = data.getMenu().get(i).getOptions().getMealProducts().get(j).getQuantity() + "x " + data.getMenu().get(i).getOptions().getMealProducts().get(j).getProductNameAPP();

                            if (productModiProductName.length() > (charCountNew * 2)) {
                                productFirstLine = productModiProductName.substring(0, (charCountNew));
                                productSecondLine = productModiProductName.substring(charCountNew, ((charCountNew * 2) - 3)) + "... ";
                            } else {
                                if (productModiProductName.length() > charCountNew) {
                                    productFirstLine = productModiProductName.substring(0, (charCountNew));
                                    productSecondLine = productModiProductName.substring((charCountNew), productModiProductName.length());
                                } else {
                                    productFirstLine = productModiProductName;
                                    for (int b = 0; b < (charCountNew - productModiProductName.length()); b++) {
                                        productFirstLine += " ";
                                    }
                                }
                            }
                        }
                        if (productSecondLine == null) {
                            String amountSpace;
                            if (data.getMenu().get(i).getOptions().getMealProducts().get(j).getAmount() != null)
                                amountSpace = createPriceSpace(priceCharCount, Double.parseDouble(data.getMenu().get(i).getOptions().getMealProducts().get(j).getAmount()));
                            else
                                amountSpace = createPriceSpace(priceCharCount, Double.parseDouble("0"));

                            if (data.getMenu().get(i).getOptions().getMealProducts().get(j).getAmount() != null) {
                                orderItemData.append(

                                        productFirstLine
                                                + String.format(amountSpace + context.getResources().getString(R.string.currency2) + "%.2f", Double.parseDouble(data.getMenu().get(i).getOptions().getMealProducts().get(j).getAmount()))
                                                + "\n"
                                );
                            } else {
                                orderItemData.append(

                                        productFirstLine
                                                + String.format(amountSpace + context.getResources().getString(R.string.currency2) + "%.2f", Double.parseDouble("0"))
                                                + "\n"
                                );
                            }
                        } else {

                            String amountSpace = createPriceSpace(priceCharCount, data.getMenu().get(i).getPrice());
                            if (data.getMenu().get(i).getOptions().getMealProducts().get(j).getAmount() != null) {
                                orderItemData.append(productFirstLine

                                        + String.format(amountSpace + context.getResources().getString(R.string.currency2) + "%.2f", Double.parseDouble(data.getMenu().get(i).getOptions().getMealProducts().get(j).getAmount()))
                                        + "\n"
                                );
                            } else {
                                orderItemData.append(productFirstLine

                                        + String.format(amountSpace + context.getResources().getString(R.string.currency2) + "%.2f", Double.parseDouble("0"))
                                        + "\n"
                                );
                            }
                            orderItemData.append("    " + productSecondLine + "\n");
                        }
                    }
                    for (int k = 0; k < data.getMenu().get(i).getOptions().getMealProducts().get(j).getSizeModifiers().size(); k++) {

                        for (int l = 0; l < data.getMenu().get(i).getOptions().getMealProducts().get(j).getSizeModifiers().get(k).getSizeModifierProducts().size(); l++) {

                            if (data.getMenu().get(i).getOptions().getMealProducts().get(j).getSizeModifiers().get(k) != null) {
                                String productModiProductName = data.getMenu().get(i).getOptions().getMealProducts().get(j).getSizeModifiers().get(k).getSizeModifierProducts().get(l).getQuantity() + "x " + data.getMenu().get(i).getOptions().getMealProducts().get(j).getSizeModifiers().get(k).getSizeModifierProducts().get(l).getProductName();
                                if (productModiProductName.length() > (charCountNew * 2)) {
                                    productFirstLine = productModiProductName.substring(0, (charCountNew));
                                    productSecondLine = productModiProductName.substring(charCountNew, ((charCountNew * 2) - 3)) + "... ";
                                } else {
                                    if (productModiProductName.length() > charCountNew) {
                                        productFirstLine = productModiProductName.substring(0, (charCountNew));
                                        productSecondLine = productModiProductName.substring((charCountNew), productModiProductName.length());
                                    } else {
                                        productFirstLine = productModiProductName;
                                        for (int b = 0; b < (charCountNew - productModiProductName.length()); b++) {
                                            productFirstLine += " ";
                                        }
                                    }
                                }
                                if (productSecondLine == null) {
                                    String amountSpace = createPriceSpace(priceCharCount, Double.parseDouble(data.getMenu().get(i).getOptions().getMealProducts().get(j).getSizeModifiers().get(k).getSizeModifierProducts().get(l).getAmount()));

                                    orderItemData.append(
                                            productFirstLine
                                                    + String.format(amountSpace + context.getResources().getString(R.string.currency2) + "%.2f", Double.parseDouble(data.getMenu().get(i).getOptions().getMealProducts().get(j).getSizeModifiers().get(k).getSizeModifierProducts().get(l).getAmount()))
                                                    + "\n"
                                    );
                                } else {
                                    String amountSpace = createPriceSpace(priceCharCount, data.getMenu().get(i).getPrice());

                                    orderItemData.append(productFirstLine

                                            + String.format(amountSpace + context.getResources().getString(R.string.currency2) + "%.2f", Double.parseDouble(data.getMenu().get(i).getOptions().getMealProducts().get(j).getSizeModifiers().get(k).getSizeModifierProducts().get(l).getAmount()))
                                            + "\n"
                                    );
                                    orderItemData.append("    " + productSecondLine + "\n");
                                }
                            }
                        }
                    }
                }
            }
            /* --------------Menu Size----------------------------------------*/

            if (data.getMenu().get(i).getOptions().getSize() != null && data.getMenu().get(i).getOptions().getSize().getProductSizeName() != null) {

                if (data.getMenu().get(i).getOptions().getSize() != null) {
                    String productModiProductName = data.getMenu().get(i).getOptions().getSize().getQuantity() + "x " + data.getMenu().get(i).getOptions().getSize().getProductSizeName();
                    if (productModiProductName.length() > (charCountNew * 2)) {
                        productFirstLine = productModiProductName.substring(0, (charCountNew));
                        productSecondLine = productModiProductName.substring(charCountNew, ((charCountNew * 2) - 3)) + "... ";
                    } else {
                        if (productModiProductName.length() > charCountNew) {
                            productFirstLine = productModiProductName.substring(0, (charCountNew));
                            productSecondLine = productModiProductName.substring((charCountNew), productModiProductName.length());
                        } else {
                            productFirstLine = productModiProductName;
                            for (int b = 0; b < (charCountNew - productModiProductName.length()); b++) {
                                productFirstLine += " ";
                            }
                        }
                    }
                    if (productSecondLine == null) {
                        String amountSpace = createPriceSpace(priceCharCount, data.getMenu().get(i).getOptions().getSize().getProductSizePrice());

                        orderItemData.append(
                                productFirstLine
                                        + String.format(amountSpace + context.getResources().getString(R.string.currency2) + "%.2f", data.getMenu().get(i).getOptions().getSize().getProductSizePrice())
                                        + "\n"
                        );
                    } else {
                        String amountSpace = createPriceSpace(priceCharCount, data.getMenu().get(i).getPrice());

                        orderItemData.append(productFirstLine

                                + String.format(amountSpace + context.getResources().getString(R.string.currency2) + "%.2f", data.getMenu().get(i).getOptions().getSize().getProductSizePrice())
                                + "\n"
                        );
                        orderItemData.append("    " + productSecondLine + "\n");
                    }
                }

                for (int j = 0; j < data.getMenu().get(i).getOptions().getSize().getSizemodifiers().size(); j++) {
                    // List<SizeModifierProduct> sizeModifierProducts = data.getMenu().get(i).getOptions().getSizeBeans().getSizemodifiers().get(j).getSizeModifierProducts();
                    for (int k = 0; k < data.getMenu().get(i).getOptions().getSize().getSizemodifiers().get(j).getSizeModifierProducts().size(); k++) {

                        if (data.getMenu().get(i).getOptions().getSize().getSizemodifiers().get(j).getSizeModifierProducts().get(k) != null) {
                            String productModiProductName = data.getMenu().get(i).getOptions().getSize().getSizemodifiers().get(j).getSizeModifierProducts().get(k).getQuantity() + "x " + data.getMenu().get(i).getOptions().getSize().getSizemodifiers().get(j).getSizeModifierProducts().get(k).getProductName();
                            if (productModiProductName.length() > (charCountNew * 2)) {
                                productFirstLine = productModiProductName.substring(0, (charCountNew));
                                productSecondLine = productModiProductName.substring(charCountNew, ((charCountNew * 2) - 3)) + "... ";
                            } else {
                                if (productModiProductName.length() > charCountNew) {
                                    productFirstLine = productModiProductName.substring(0, (charCountNew));
                                    productSecondLine = productModiProductName.substring((charCountNew), productModiProductName.length());
                                } else {
                                    productFirstLine = productModiProductName;
                                    for (int b = 0; b < (charCountNew - productModiProductName.length()); b++) {
                                        productFirstLine += " ";
                                    }
                                }
                            }
                            if (productSecondLine == null) {
                                String amountSpace = createPriceSpace(priceCharCount, data.getMenu().get(i).getOptions().getSize().getSizemodifiers().get(j).getSizeModifierProducts().get(k).getAmount());

                                orderItemData.append(
                                        productFirstLine
                                                + String.format(amountSpace + context.getResources().getString(R.string.currency2) + "%.2f", data.getMenu().get(i).getOptions().getSize().getSizemodifiers().get(j).getSizeModifierProducts().get(k).getAmount())
                                                + "\n"
                                );
                            } else {
                                String amountSpace = createPriceSpace(priceCharCount, data.getMenu().get(i).getPrice());

                                orderItemData.append(productFirstLine

                                        + String.format(amountSpace + context.getResources().getString(R.string.currency2) + "%.2f", data.getMenu().get(i).getOptions().getSize().getSizemodifiers().get(j).getSizeModifierProducts().get(k).getAmount())
                                        + "\n"
                                );
                                orderItemData.append("    " + productSecondLine + "\n");
                            }
                        }
                    }
                }
            }
        }

        return orderItemData.toString();
    }

    private static String AddressNameCorrect(String Address) {
        String AddressToPrint = "", FirstAdd = "", SecondAdd = "", ThirdAdd = "", FourthAdd = "";
        int charCountAddress = 19;

        ArrayList<String> addressStrings = new ArrayList<>();
        addressStrings.clear();
        int pickChar = 0;

        try {
            Address = Address + " ";
            for (int j = 0; j < Address.length(); j++) {
                char spaceS = Address.charAt(j);
                if (spaceS == ' ' || spaceS == ',') {
                    String aadd = Address.substring(pickChar, j);
                    addressStrings.add(aadd.trim());
                    pickChar = j + 1;
                }

            }

            String AddressToPrintCheck = "";
            for (int k = 0; k < addressStrings.size(); k++) {

                try {
                    if (FirstAdd.length() > charCountAddress) {

                        try {
                            if (SecondAdd.length() > charCountAddress) {
                                try {
                                    if (ThirdAdd.length() > charCountAddress) {
                                        FourthAdd = FourthAdd + " " + addressStrings.get(k);
                                    } else {
                                        ThirdAdd = ThirdAdd + " " + addressStrings.get(k);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                SecondAdd = SecondAdd + " " + addressStrings.get(k);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        FirstAdd = FirstAdd + " " + addressStrings.get(k);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //58 Wake Green Road, B13 9PG, Birmingham, England   B13 9PG
        try {
            if (FirstAdd != null && FirstAdd.length() > 0) {
                AddressToPrint = FirstAdd.trim();

                if (SecondAdd != null && SecondAdd.length() > 0) {
                    AddressToPrint = AddressToPrint + "\n   " + SecondAdd.trim();

                    if (ThirdAdd != null && ThirdAdd.length() > 0) {
                        AddressToPrint = AddressToPrint + "\n   " + ThirdAdd.trim();

                        if (FourthAdd != null && FourthAdd.length() > 0) {
                            AddressToPrint = AddressToPrint + "\n   " + FourthAdd.trim();
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return AddressToPrint + " ";
    }

    private static String createPriceSpace(int priceCharCount, Double price) {
        String priceSpace = "";
        if (Constants.decimalFormat(price).length() < (priceCharCount - 1)) {
            for (int j = 0; j < ((priceCharCount - 1) - String.valueOf(Constants.decimalFormat(price)).length()); j++) {
                priceSpace += " ";
            }
        }

        try {
            priceSpace = priceSpace.substring(0, priceSpace.length() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }


    private static String reportData(Context context, List<OrderReportResponse.OrdersList> item, int charCount) {
        StringBuilder data = new StringBuilder();
        int orderNoShowCount = 8;
        int postCodeCount = 7;
        int dateCount = 11;
        int itemQtyCount = 2;
        int amountCount = 6;
        for (int i = 0; i < item.size(); i++) {
            String orderId = item.get(i).getOrder_id();
            if (orderId.length() > 10) {
                data.append(orderId.substring((orderId.length() - orderNoShowCount), orderId.length()));
            } else {
                data.append(orderId);
            }
            if (item.get(i).getCustomer_post_code().length() == 0) {
                data.append("         ");

            } else {
                data.append("    " + item.get(i).getCustomer_post_code());
            }
            String[] date = item.get(i).getOrder_date().split(" ");
            data.append(" ");
            for (int j = 0; j < date.length; j++) {
                data.append(date[j]);
            }

            data.append("    " + item.get(i).getTotal_items());
            if (item.get(i).getOrder_total().length() < amountCount) {
                int spacePrint = amountCount - item.get(i).getOrder_total().length();
                data.append(" ");
                for (int j = 0; j < spacePrint; j++) {
                    data.append(" ");
                }
                data.append(context.getResources().getString(R.string.currency2) + item.get(i).getOrder_total() + "\n");
            } else {
                data.append(" " + context.getResources().getString(R.string.currency2) + item.get(i).getOrder_total() + "\n");
            }
        }

        return data.toString();
    }

    private static String printSpaceBetweenTwoString(String str1, String str2, int charCount) {
//String.format("%-10s %-10.2f", str1, str1)//

        String data = "";
        int stringLength = (str1.length() + str2.length());

        int spaceCount = charCount - stringLength;
        String space = "";
        for (int i = 0; i < (spaceCount - 1); i++) {
            space += " ";
        }
        data = str1 +space+  str2;
        return data;
    }
    private static String printSpaceBetweenTwoStringIngenico(String str1, String str2) {


        int totalLength = 52; // Total length for both strings
        String formattedStr1 = String.format("%-" + totalLength + "s", str1);
        strAr1.add(formattedStr1);
        String formattedStr2 = String.format("%" + totalLength + "s", str2);
        strAr1.add(formattedStr2);
        System.out.println(formattedStr1 + formattedStr2);
        return  formattedStr1 + formattedStr2;
    }

    private void print_image(Bitmap bmp) throws IOException {
        try {

            if (bmp != null) {
                byte[] command = Utils.decodeBitmap(bmp);
                printText(command);
            } else {
                Log.e("Print Photo error", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
        }
    }

    private void printText(String msg) {
        try {
            // Print normal text
            mmOutputStream.write(msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //print byte[]
    private static void printText(byte[] msg) {
        try {
            // Print normal text
            mmOutputStream.write(msg);
            printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printTextWithImage(byte[] msg, String msg2) {
        try {
            // Print normal text
            mmOutputStream.write(msg);
            mmOutputStream.write(msg2.getBytes());
            printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //print new line
    private static void printNewLine() {
        if (deviceName.equals("castle")) {
            int print_y = 26;
            int Currently_high = 5;
            try {
                print.connect(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            print.initPage(30);
            print.setAlign(print.PRT_ALIGNLEFT);
            print.drawText(0, print_y + Currently_high, "\n", print_y, 1, 0xFF000000, true, (float) 0, false,
                    false, Typeface.DEFAULT);
            print.printPage();
            try {
                print.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                mmOutputStream.write(PrinterCommands.FEED_LINE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void resetPrint() {
        try {
            mmOutputStream.write(PrinterCommands.ESC_FONT_COLOR_DEFAULT);
            mmOutputStream.write(PrinterCommands.FS_FONT_ALIGN);
            mmOutputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
            mmOutputStream.write(PrinterCommands.ESC_CANCEL_BOLD);
            mmOutputStream.write(PrinterCommands.LF);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //print custom
    private static void printCustom(String msg, int size, int align) {
        try {
            if (deviceName.equals("castle")) {
                int print_y = 26;
                int Currently_high = 5;
                try {
                    print.connect(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                print.initPage(30);
                print.setAlign(print.PRT_ALIGNLEFT);
                print.drawText(0, print_y + Currently_high, msg, print_y, 1, 0xFF000000, true, (float) 0, false,
                        false, Typeface.DEFAULT);


                print.printPage();
                try {
                    print.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                //Print config "mode"
                byte[] cc = new byte[]{0x1B, 0x21, 0x03};  // 0- normal size text
                //byte[] cc1 = new byte[]{0x1B,0x21,0x00};  // 0- normal size text
                byte[] bb = new byte[]{0x1B, 0x21, 0x08};  // 1- only bold text
                byte[] bb2 = new byte[]{0x1B, 0x21, 0x20}; // 2- bold with medium text
                byte[] bb3 = new byte[]{0x1B, 0x21, 0x10}; // 3- bold with large text
                try {
                    switch (size) {
                        case 0:
                            mmOutputStream.write(cc);
                            break;
                        case 1:
                            mmOutputStream.write(bb);
                            break;
                        case 2:
                            mmOutputStream.write(bb2);
                            break;
                        case 3:
                            mmOutputStream.write(bb3);
                            break;
                    }

                    switch (align) {
                        case 0:
                            //left align
                            mmOutputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
                            break;
                        case 1:
                            //center align
                            mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                            break;
                        case 2:
                            //right align
                            mmOutputStream.write(PrinterCommands.ESC_ALIGN_RIGHT);
                            break;
                    }
                    byte[] b = new byte[0];
                    try {
                        b = msg.getBytes("windows-1252");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    mmOutputStream.write(b);
                    mmOutputStream.write(PrinterCommands.LF);
                    //outputStream.write(cc);
                    //printNewLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            Toast.makeText(context, "" + e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    //print photo
    public static void printPhoto(Context context, Bitmap bmp) {
        if (deviceName.equals("castle")) {
            print.initPage(60);
            bmp = print.encodeToBitmap("12ASDFSS34", print.QR_CODE, 150, 150);
            print.drawImage(bmp, 0, 0);
            print.printPage();

        } else {
            try {

                if (bmp != null) {
                    byte[] command = Utils.decodeBitmap(bmp);
                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    printText(command);
                } else {
                    Log.e("Print Photo error", "the file isn't exists");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("PrintTools", "the file isn't exists");
            }
        }
    }

    public static void printPhotoAndText(Context context, Bitmap bmp, String text) {
        try {

            if (bmp != null) {
                byte[] command = Utils.decodeBitmap(bmp);
                mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                printTextWithImage(command, text);
            } else {
                Log.e("Print Photo error", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
        }
    }

    //print unicode
    public void printUnicode() {
        try {
            mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
            printText(Utils.UNICODE_TEXT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String leftRightAlign(String str1, String str2) {
        String ans = str1 + str2;
        if (ans.length() < 31) {
            int n = (31 - str1.length() + str2.length());
            ans = str1 + new String(new char[n]).replace("\0", " ") + str2;
        }
        return ans;
    }


    private static String[] getDateTime() {
        final Calendar c = Calendar.getInstance();
        String dateTime[] = new String[2];
        dateTime[0] = c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR);
        dateTime[1] = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
        return dateTime;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (mmSocket != null) {
                mmOutputStream.close();
                mmSocket.close();
                mmSocket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            mmSocket = DeviceList.getSocket();
            if (mmSocket != null) {
                byte[] logoByte = UserPreferences.get().getRestaurantLogoBitmap(getActivity());
                Bitmap logo = null;
                if (logoByte != null) {
                    logo = BitmapFactory.decodeByteArray(logoByte, 0, logoByte.length);
                }
                //  printBill(getActivity(),logo,userPreferences.getLoggedInResponse(getActivity()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
