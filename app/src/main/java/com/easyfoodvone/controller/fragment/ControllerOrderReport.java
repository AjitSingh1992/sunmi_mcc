package com.easyfoodvone.controller.fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.easyfoodvone.DeviceList;
import com.easyfoodvone.R;
import com.easyfoodvone.api_handler.ApiClient;
import com.easyfoodvone.api_handler.ApiInterface;
import com.easyfoodvone.app_common.separation.LifecycleSafe;
import com.easyfoodvone.app_common.separation.ObservableField;
import com.easyfoodvone.app_common.utility.NewConstants;
import com.easyfoodvone.app_common.viewdata.DataPageOrderReport;
import com.easyfoodvone.app_ui.view.ViewOrderReport;
import com.easyfoodvone.dialogs.PinDialog;
import com.easyfoodvone.utility.Helper;
import com.easyfoodvone.utility.PrinterCommands;
import com.easyfoodvone.utility.printerutil.AidlUtil;
import com.easyfoodvone.utility.printerutil.PrintEsayFood;
import com.easyfoodvone.models.LoginResponse;
import com.easyfoodvone.models.OrderReportRequest;
import com.easyfoodvone.app_common.ws.OrderReportResponse;
import com.easyfoodvone.utility.Constants;
import com.easyfoodvone.utility.LoadingDialog;
import com.easyfoodvone.utility.PrefManager;
import com.easyfoodvone.utility.UserPreferences;
import com.easyfoodvone.utility.printerutil.Utils;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.easyfoodvone.utility.UserContants.AUTH_TOKEN;

public class ControllerOrderReport extends Fragment {
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    byte FONT_TYPE;
    // needed for communication to bluetooth device / network
    static OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;

    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;

    public interface ParentInterface {
        void goToHome();
        void showToastLong(@NonNull String message);
    }

    private final @NonNull ParentInterface parentInterface;
    private final @NonNull PrefManager prefManager;
    private final @NonNull UserPreferences userPreferences;
    private final boolean isPhone;

    private DataPageOrderReport data;

    private @Nullable OrderReportRequest lastSuccessfulRequest = null;
    private @Nullable OrderReportResponse lastSuccessfulResponse = null;

    public ControllerOrderReport(
            @NonNull ParentInterface parentInterface,
            @NonNull PrefManager prefManager,
            @NonNull UserPreferences userPreferences,
            boolean isPhone) {
        this.parentInterface = parentInterface;
        this.prefManager = prefManager;
        this.userPreferences = userPreferences;
        this.isPhone = isPhone;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        data = new DataPageOrderReport(
                new ObservableField<>(false),
                new ObservableField<>(false),
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableField<>(null),
                viewEventHandler);

        ViewOrderReport view = new ViewOrderReport(new LifecycleSafe(this), isPhone);
        view.onCreateView(data, inflater, container);

        new PinDialog(this, userPreferences.getLoggedInResponse(getActivity()), pinCallbacks)
                .alertDialogMPIN("Enter 4 Digit Code\nTo View Order Report");

        return view.getRoot();
    }

    private final PinDialog.ParentInterface pinCallbacks = new PinDialog.ParentInterface() {
        @Override
        public void onDismiss() {
            parentInterface.goToHome();
        }

        @Override
        public void onSubmitCorrect() {
            data.isPinCheckSuccessful().set(true);

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
            cal.clear(Calendar.MINUTE);
            cal.clear(Calendar.SECOND);
            cal.clear(Calendar.MILLISECOND);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            //data.getStartDate().set(DateFormat.format("dd-MMM-yyyy", cal.getTimeInMillis()).toString());

            cal.add(Calendar.MONTH, 1);
            //data.getEndDate().set(DateFormat.format("dd-MMM-yyyy", cal.getTimeInMillis()).toString());

            getOrderReport(false, false, true);
        }

        @Override
        public void onSubmitIncorrect() {
            data.isPinCheckSuccessful().set(false);
        }
    };

    private final DataPageOrderReport.OutputEvents viewEventHandler = new DataPageOrderReport.OutputEvents() {
        @Override
        public void onClickAll() {
            getOrderReport(false, false, true);
        }

        @Override
        public void onClickYesterday() {
            data.getEndDate().set("");
            data.getStartDate().set("");

            getOrderReport(false, true, false);
        }

        @Override
        public void onClickToday() {
            data.getEndDate().set("");
            data.getStartDate().set("");

            getOrderReport(true, false, false);
        }

        @Override
        public void onClickStartDate() {
            Constants.showDateSelectorForPastDate(getActivity(), date -> data.getStartDate().set(date));

        }

        @Override
        public void onClickEndDate() {
          // Constants.showDateSelectorForPastDate(getActivity(), date -> data.getEndDate().set(date));
            Constants.showDateSelectorEndDate(getActivity(), date -> data.getEndDate().set(date));

        }

        @Override
        public void onClickSearchBetweenDates() {
            String startDate = data.getStartDate().get();
            String endDate = data.getEndDate().get();

            if (TextUtils.isEmpty(startDate) || startDate.equalsIgnoreCase("Start Date")) {
                Toast.makeText(getActivity(), "Enter correct start date", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(endDate) || endDate.equalsIgnoreCase("End Date")) {
                Toast.makeText(getActivity(), "Enter correct end date", Toast.LENGTH_SHORT).show();
            } else {
                getOrderReport(false, false, true);
            }
        }

        @Override
        public void onClickEmailReport() {
            sentReport();
        }

        @Override
        public void onClickPrintReport() {
            try {
                printReport();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private @NonNull Single<OrderReportResponse> getAPIFor(@NonNull OrderReportRequest request) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        String authToken = prefManager.getPreference(AUTH_TOKEN,"");
        Gson gson = new Gson();
        gson.toJson(request);
        if ( ! TextUtils.isEmpty(request.getDate())) {
            return apiService.getOrderReportByDate(authToken, request);

        } else if ( ! TextUtils.isEmpty(request.getFrom_date()) && ! TextUtils.isEmpty(request.getEnd_date())) {
            return apiService.getOrderReportBetweenDate(authToken, request);

        } else {
            // TODO this web service doesn't actually work...

            return apiService.getOrderReport(authToken, request);
        }


    }

    private void sentReport() {
        if (lastSuccessfulRequest == null) {
            return;
        }

        final LoadingDialog dialog = new LoadingDialog(getActivity(), "");
        dialog.setCancelable(false);
        dialog.show();

        try {
            LoginResponse.Data loginData = userPreferences.getLoggedInResponse(getActivity());

            final OrderReportRequest request = new OrderReportRequest();
            request.setRestaurant_id(lastSuccessfulRequest.getRestaurant_id());
            request.setUser_id(lastSuccessfulRequest.getUser_id());
            request.setEnd_date(lastSuccessfulRequest.getEnd_date());
            request.setFrom_date(lastSuccessfulRequest.getFrom_date());
            request.setDate(lastSuccessfulRequest.getDate());

            // TODO check this should be the email which the report is sent to
            request.setEmail(loginData.getEmail());

            final Single<OrderReportResponse> api = getAPIFor(request);

            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(api
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<OrderReportResponse>() {
                        @Override
                        public void onSuccess(OrderReportResponse data) {
                            dialog.dismiss();

                            Toast.makeText(getActivity(), "Report sent to " + request.getEmail(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Throwable e) {
                            dialog.dismiss();

                            Toast.makeText(getActivity(), "Server connection failed", Toast.LENGTH_SHORT).show();

                            e.printStackTrace();
                        }
                    }));

        } catch(Exception e) {
            dialog.dismiss();

            Toast.makeText(getActivity(), "Unable to send email", Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        }
    }

    private void getOrderReport(boolean todayRevenue, boolean yesterdayRevenue, boolean betweenRevenue) {
        final LoadingDialog dialog = new LoadingDialog(getActivity(), "Loading orders report...");
        dialog.setCancelable(false);
        dialog.show();
        try {
            LoginResponse.Data loginData = userPreferences.getLoggedInResponse(getActivity());

            final OrderReportRequest request = new OrderReportRequest();
            request.setRestaurant_id(loginData.getRestaurant_id());
            request.setUser_id(loginData.getUser_id());
            if (yesterdayRevenue) {
                request.setDate(Constants.getYesterdayDateString1());
            } else if (todayRevenue) {
                request.setDate(Constants.getCurrentDateString1());
            } else if (betweenRevenue) {
                request.setFrom_date(data.getStartDate().get());
                request.setEnd_date(data.getEndDate().get());
            }

            final Single<OrderReportResponse> api = getAPIFor(request);

            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(api
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<OrderReportResponse>() {
                        @Override
                        public void onSuccess(OrderReportResponse data) {
                            dialog.dismiss();

                            if (data.isSuccess()) {
                                lastSuccessfulRequest = request;
                                lastSuccessfulResponse = data;

                                String totalOrders = data.getData().getTotal_orders();
                                String totalRevenue = data.getData().getTotal_revenue();
                                String totalItems = data.getData().getTotal_items();
                                String totalDiscount = data.getData().getTotal_discount();
                                String acceptedPercent = data.getData().getTotal_orders_accepted_per();
                                String acceptedAmount = data.getData().getTotal_orders_accepted_amount();
                                String acceptedCount = data.getData().getTotal_orders_accepted();
                                String declinedAmount = data.getData().getTotal_orders_declined_amount();
                                String declinedCount = data.getData().getTotal_orders_declined();
                                String declinedPercent = data.getData().getTotal_orders_declined_per();
                                String total_taxes = data.getData().getTotal_taxes();
                                String restaurant_wallet = data.getData().getRestaurant_wallet();
                                ControllerOrderReport.this.data.isDataLoaded().set(true);

                                ControllerOrderReport.this.data.getTotalOrders().set(TextUtils.isEmpty(totalOrders) ? "0" : totalOrders);
                                ControllerOrderReport.this.data.getTotalRevenue().set(NewConstants.POUND + (TextUtils.isEmpty(totalRevenue) ? "0" : totalRevenue));
                                ControllerOrderReport.this.data.getTotalItems().set(TextUtils.isEmpty(totalItems) ? "0" : totalItems);
                                ControllerOrderReport.this.data.getTotalDiscount().set(NewConstants.POUND + (TextUtils.isEmpty(totalDiscount) ? "0" : totalDiscount));
                                // wallet_balance.setText(data.getData().getWallet_balance() == null || data.getData().getWallet_balance().equalsIgnoreCase("") ? NewConstants.POUND + "0" : NewConstants.POUND + data.getData().getWallet_balance());
                                // credit_count.setText(data.getData().getTotal_orders_by_credit_card() == null || data.getData().getTotal_orders_by_credit_card().equalsIgnoreCase("") ? "Credit(0)" : "Credit(" + data.getData().getTotal_orders_by_credit_card() + ")");
                                // credit_amt.setText(data.getData().getTotal_orders_by_credit_card_amount() == null || data.getData().getTotal_orders_by_credit_card_amount().equalsIgnoreCase("") ? NewConstants.POUND + "0" : NewConstants.POUND + data.getData().getTotal_orders_by_credit_card_amount());
                                ControllerOrderReport.this.data.getAcceptedPercent().set(TextUtils.isEmpty(acceptedPercent) ? "0%" : (acceptedPercent + "%"));
                                ControllerOrderReport.this.data.getAcceptedAmount().set(NewConstants.POUND + (TextUtils.isEmpty(acceptedAmount) ? "0" : acceptedAmount));
                                ControllerOrderReport.this.data.getAcceptedCount().set(TextUtils.isEmpty(acceptedCount) ? "0" : acceptedCount);
                                ControllerOrderReport.this.data.getDeclinedAmount().set(NewConstants.POUND + (TextUtils.isEmpty(declinedAmount) ? "0" : declinedAmount));
                                ControllerOrderReport.this.data.getDeclinedCount().set(TextUtils.isEmpty(declinedCount) ? "0" : declinedCount);
                                ControllerOrderReport.this.data.getDeclinedPercent().set(TextUtils.isEmpty(declinedPercent) ? "0%" : declinedPercent + "%");
                                ControllerOrderReport.this.data.getTotaltaxes().set(NewConstants.POUND + (TextUtils.isEmpty(total_taxes) ? "0" : total_taxes + ""));
                                ControllerOrderReport.this.data.getRestaurantwallet().set(NewConstants.POUND + (TextUtils.isEmpty(restaurant_wallet) ? "0" : restaurant_wallet + ""));

                                // cash_per.setText(data.getData().getTotal_orders_by_cash_per() == null || data.getData().getTotal_orders_by_cash_per().equalsIgnoreCase("") ? "0%" : data.getData().getTotal_orders_by_cash_per() + "%");
                                // cash_amt.setText(data.getData().getTotal_orders_by_cash_amount() == null || data.getData().getTotal_orders_by_cash_amount().equalsIgnoreCase("") ? NewConstants.POUND + "0" : NewConstants.POUND + data.getData().getTotal_orders_by_cash_amount());
                                // cash_count.setText(data.getData().getTotal_orders_by_cash() == null || data.getData().getTotal_orders_by_cash().equalsIgnoreCase("") ? "CASH (0)" : "CASH (" + data.getData().getTotal_orders_by_cash() + ")");

                                ControllerOrderReport.this.data.getOrdersList().set(data.getData().getOrder_list());

                            } else {
                                errorClearData();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            errorClearData();
                            dialog.dismiss();

                            Toast.makeText(getActivity(), "Loading failed ", Toast.LENGTH_LONG).show();
                            Log.e("onError", "onError: " + e.getMessage());
                        }
                    }));

        } catch (Exception e) {
            errorClearData();
            dialog.dismiss();

            Log.e("Exception ", e.toString());
            Toast.makeText(getActivity(), "Server not responding.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void errorClearData() {
        lastSuccessfulRequest = null;
        lastSuccessfulResponse = null;

        ControllerOrderReport.this.data.isDataLoaded().set(false);

        data.getTotalOrders().set("");
        data.getTotalRevenue().set("");
        data.getTotalItems().set("");
        data.getTotalDiscount().set("");
        data.getAcceptedPercent().set("");
        data.getAcceptedCount().set("");
        data.getAcceptedAmount().set("");
        data.getDeclinedAmount().set("");
        data.getDeclinedCount().set("");
        data.getDeclinedPercent().set("");
    }

    private void printReport() throws IOException {
        if (lastSuccessfulResponse == null) {
            return;
        }

        byte[] logoByte = userPreferences.getRestaurantLogoBitmap(getActivity());
        Bitmap logo = null;
        if (logoByte != null) {
            logo = BitmapFactory.decodeByteArray(logoByte, 0, logoByte.length);
        }

            boolean serviceReady = PrintEsayFood.printSummaryOrdersReport(getActivity(), logo, userPreferences.getLoggedInResponse(getActivity()), lastSuccessfulResponse.getData());
            if ( ! serviceReady) {
                parentInterface.showToastLong("The print service is not connected");
            }

    }

    protected void printBill() {
        if(mmSocket == null){
            Intent BTIntent = new Intent(getActivity(), DeviceList.class);
            this.startActivityForResult(BTIntent, DeviceList.REQUEST_CONNECT_BT);
        }
        else{
            OutputStream opstream = null;
            try {
                opstream = mmSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mmOutputStream = opstream;

            //print command
            try {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mmOutputStream = mmSocket.getOutputStream();
                byte[] printformat = new byte[]{0x1B,0x21,0x03};
                mmOutputStream.write(printformat);


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

                mmOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void printSummaryOrdersReportBluetooth(Context context, Bitmap logo, LoginResponse.Data restaurantData, OrderReportResponse.Data reportData) {
        if (mmDevice != null) {
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
            DateFormat timeFormat = new SimpleDateFormat("HH:mma");

            String line20 = "--------------------------------------";//38 char
            String line20Bold = "--------------------------------------";//38 char
            String line22 = "----------------------------------";//34 char
            String line22Bold = "----------------------------------";//34 char
            String line24 = "--------------------------------";//32 char
            String line24Bold = "--------------------------------";//32 char
            String line26 = "-----------------------------";//29 char
            String line26Bold = "-----------------------------";//29 char

            String _date = dateFormat.format(date);
            String _time = timeFormat.format(date);

            if (logo != null)
                AidlUtil.getInstance().printBitmap(logo, 1, 1, 1);

            AidlUtil.getInstance().printText(restaurantData.getRestaurant_name(), 26, true, false, 1, 2);
            AidlUtil.getInstance().printText(line24Bold, 24, true, false, 1);
            AidlUtil.getInstance().printText("DATE: " + _date + "  TIME: " + _time, 22, true, false, 1, 1);
            AidlUtil.getInstance().printText(line24Bold, 24, true, false, 1);

            AidlUtil.getInstance().printText(printSpaceBetweenTwoString("TOTAL ORDER", reportData.getTotal_orders(), 32), 24, true, false, 1);
            AidlUtil.getInstance().printText(printSpaceBetweenTwoString("TOTAL ITEMS", reportData.getTotal_items(), 32), 24, true, false, 1);
            AidlUtil.getInstance().printText(printSpaceBetweenTwoString("TOTAL REVENUE", context.getResources().getString(R.string.currency) + Constants.decimalFormat(Double.parseDouble(reportData.getTotal_revenue())), 32), 24, true, false, 1);
            AidlUtil.getInstance().printText(printSpaceBetweenTwoString("TOTAL DISCOUNT", context.getResources().getString(R.string.currency) + Constants.decimalFormat(Double.parseDouble(reportData.getTotal_discount())), 32), 24, true, false, 1);
            AidlUtil.getInstance().printText(printSpaceBetweenTwoString("WALLET BALANCE", context.getResources().getString(R.string.currency) + Constants.decimalFormat(Double.parseDouble(reportData.getWallet_balance())), 32), 24, true, false, 1);
            AidlUtil.getInstance().printText(printSpaceBetweenTwoString("CREDIT(" + reportData.getTotal_orders_by_credit_card() + ")", context.getResources().getString(R.string.currency) + Constants.decimalFormat(Double.parseDouble(reportData.getTotal_orders_by_credit_card_amount())), 32), 24, true, false, 1);
            AidlUtil.getInstance().printText(printSpaceBetweenTwoString("ACCEPTED(" + reportData.getTotal_orders_accepted() + ")", context.getResources().getString(R.string.currency) + Constants.decimalFormat(Double.parseDouble(reportData.getTotal_orders_accepted_amount())), 32), 24, true, false, 1);
            AidlUtil.getInstance().printText(printSpaceBetweenTwoString("DECLINED(" + reportData.getTotal_orders_declined() + ")", context.getResources().getString(R.string.currency) + Constants.decimalFormat(Double.parseDouble(reportData.getTotal_orders_declined_amount())), 32), 24, true, false, 1);
            AidlUtil.getInstance().printText(printSpaceBetweenTwoString("CASH(" + reportData.getTotal_orders_by_cash() + ")" + context.getResources().getString(R.string.currency) + Constants.decimalFormat(Double.parseDouble(reportData.getTotal_orders_by_cash_amount())), Constants.decimalFormat(Double.parseDouble(reportData.getTotal_orders_by_cash_per())) + "%", 32), 24, true, false, 1);

            AidlUtil.getInstance().printText(line24Bold, 24, true, false, 1);

            String dataReport = reportData(context, reportData.getOrder_list(), 32);
            AidlUtil.getInstance().printText(dataReport, 20, true, false, 4);



        }
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
                data.append("  " + item.get(i).getCustomer_post_code());
            }
            String[] date = item.get(i).getOrder_date().split(" ");
            data.append(" ");
            for (int j = 0; j < date.length; j++) {
                data.append(date[j]);
            }

            data.append("  " + item.get(i).getTotal_items());
            if (item.get(i).getOrder_total().length() < amountCount) {
                int spacePrint = amountCount - item.get(i).getOrder_total().length();
                data.append(" ");
                for (int j = 0; j < spacePrint; j++) {
                    data.append(" ");
                }
                data.append(context.getResources().getString(R.string.currency) + item.get(i).getOrder_total() + "\n");
            } else {
                data.append(" " + context.getResources().getString(R.string.currency) + item.get(i).getOrder_total() + "\n");
            }
        }

        return data.toString();
    }

    private static String printSpaceBetweenTwoString(String str1, String str2, int charCount) {
        String data = "";
        int stringLength = (str1.length() + str2.length());

        int spaceCount = charCount - stringLength;
        String space = "";
        for (int i = 0; i < (spaceCount - 1); i++) {
            space += " ";
        }
        data = str1 + space + str2;
        return data;
    }
    private void print_image(Bitmap bmp) throws IOException {
        try {

            if(bmp!=null){
                byte[] command = Utils.decodeBitmap(bmp);
                printText(command);
            }else{
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
    private void printText(byte[] msg) {
        try {
            // Print normal text
            mmOutputStream.write(msg);
            printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //print new line
    private void printNewLine() {
        try {
            mmOutputStream.write(PrinterCommands.FEED_LINE);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void resetPrint() {
        try{
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
    private void printCustom(String msg, int size, int align) {
        //Print config "mode"
        byte[] cc = new byte[]{0x1B,0x21,0x03};  // 0- normal size text
        //byte[] cc1 = new byte[]{0x1B,0x21,0x00};  // 0- normal size text
        byte[] bb = new byte[]{0x1B,0x21,0x08};  // 1- only bold text
        byte[] bb2 = new byte[]{0x1B,0x21,0x20}; // 2- bold with medium text
        byte[] bb3 = new byte[]{0x1B,0x21,0x10}; // 3- bold with large text
        try {
            switch (size){
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

            switch (align){
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
            mmOutputStream.write(msg.getBytes());
            mmOutputStream.write(PrinterCommands.LF);
            //outputStream.write(cc);
            //printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //print photo
    public void printPhoto(Bitmap bmp) {
        try {

            if(bmp!=null){
                byte[] command = Utils.decodeBitmap(bmp);
                mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                printText(command);
            }else{
                Log.e("Print Photo error", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
        }
    }

    //print unicode
    public void printUnicode(){
        try {
            mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
            printText(Utils.UNICODE_TEXT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String leftRightAlign(String str1, String str2) {
        String ans = str1 +str2;
        if(ans.length() <31){
            int n = (31 - str1.length() + str2.length());
            ans = str1 + new String(new char[n]).replace("\0", " ") + str2;
        }
        return ans;
    }


    private String[] getDateTime() {
        final Calendar c = Calendar.getInstance();
        String dateTime [] = new String[2];
        dateTime[0] = c.get(Calendar.DAY_OF_MONTH) +"/"+ c.get(Calendar.MONTH) +"/"+ c.get(Calendar.YEAR);
        dateTime[1] = c.get(Calendar.HOUR_OF_DAY) +":"+ c.get(Calendar.MINUTE);
        return dateTime;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if(mmSocket!= null){
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
            if(mmSocket != null){
                printBill();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
