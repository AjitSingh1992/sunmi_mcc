package com.easyfoodvone.utility.printerutil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Message;
import android.os.RemoteException;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.easyfoodvone.app_common.utility.BarcodeUtils;
import com.easyfoodvone.R;
import com.easyfoodvone.app_common.ws.NewDetailBean;
import com.easyfoodvone.models.LoginResponse;
import com.easyfoodvone.app_common.ws.OrderReportResponse;
import com.easyfoodvone.new_order.models.OrderDetailsResponse;
import com.easyfoodvone.utility.Constants;
import com.easyfoodvone.utility.UserPreferences;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import woyou.aidlservice.jiuiv5.ICallback;

public class PrintEsayFood {
    static DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
    static DateFormat timeFormat = new SimpleDateFormat("HH:mma");

    static String line20 = "--------------------------------------";//38 char
    static String line20Bold = "--------------------------------------";//38 char
    static String line22 = "----------------------------------";//34 char
    static String line22Bold = "----------------------------------";//34 char
    static String line24 = "--------------------------------";//32 char
    static String line24Bold = "--------------------------------";//32 char
    static String line26 = "-----------------------------";//29 char
    static String line26Bold = "-----------------------------";//29 char

    public void printProductBarcode(List<String> barCode, List<String> productIdAndName) {
        if (AidlUtil.getInstance().isConnect()) {
            AidlUtil.getInstance().printBarCode(barCode, productIdAndName, 8, 80, 2, 1);
        }
    }

    public void openDrawer(ICallback callback) throws RemoteException {
        AidlUtil.getInstance().openDrawer(callback);
    }

    /** Returns true if the print service was available, though this doesn't confirm the content actually printed */
    public static boolean printOrderDetails(@NonNull Context context, @Nullable Bitmap logo, @NonNull LoginResponse.Data restaurantData, @NonNull NewDetailBean.OrdersDetailsBean orderDetail) {
        if ( ! AidlUtil.getInstance().isConnect()) {
            return false;
        }

        new MakeData(context, logo, orderDetail, restaurantData).execute();
        return true;
    }

    /** Returns true if the print service was available, though this doesn't confirm the content actually printed */
    public static boolean printSummaryOrdersReport(Context context, Bitmap logo, LoginResponse.Data restaurantData, OrderReportResponse.Data reportData) {
        if ( ! AidlUtil.getInstance().isConnect()) {
            return false;
        }

        Date date = new Date();
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

        return true;
    }


    private static void printOrderUnused1(OrderDetailsResponse.OrderDetails printData) {
        String data = "Product     Price  Qty Dis Amt ";
        String item = createData(printData);

        AidlUtil.getInstance().printText(data, 22, false, false, 1);
        AidlUtil.getInstance().printText(item, 22, false, false, 1);
    }

    private static void printOrderUnused2(Bitmap logo, OrderDetailsResponse.OrderDetails orderDetail, Context activity) {
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        DateFormat timeFormat = new SimpleDateFormat("HH:mma");

        UserPreferences userPreferences = UserPreferences.get();
        LoginResponse.Data ss = userPreferences.getLoggedInResponse(activity);

        String restaurantName = ss.getRestaurant_name();
        String address = ss.getAddress();
        String contact = ss.getPhone_number();


        Date date = new Date();
        Date time = new Date();
        String _date = dateFormat.format(date);
        String _time = timeFormat.format(time);

        String header = String.format("%s", restaurantName) + "\n"
                + String.format("%s", contact) + "\n"
                + String.format("%s", address) + "\n"
                + String.format("Date: %s", _date) + String.format("  Time: %s", _time) + "\n"
                + String.format("%s", "            Delivery            ") + "\n"
                + String.format("%s", "--------------------------------");

        String footer = String.format("%s", "        Delivery Details        ") + "\n"
                + String.format("%s", "--------------------------------") + "\n"
                + String.format("%s", orderDetail.getDelivery_address().getCustomer_name()) + "\n"
                + String.format("%s", orderDetail.getDelivery_address().getPhone_number()) + "\n"
                + String.format("%s", orderDetail.getDelivery_address().getCustomer_location()) + "\n"
                + String.format("Delivery Time:%s", orderDetail.getDelivery_date_time()) + "\n";

        int totalChar = 34;
        String orderId = String.format("Order ID: %s", orderDetail.getOrder_num());
        String orderDate = String.format("Order Date: %s", orderDetail.getOrder_date_time()) + "\n"
                + String.format("%s", "----------------------------------");
        String data;
        if (orderDetail.getDelivery_option().toUpperCase().equals("DELIVERY")) {
            data = String.format("%s", "----------------------------------") + "\n"
                    + String.format("%s", alignString(totalChar, "Payment Method", orderDetail.getPayment_mode())) + "\n"
                    + String.format("%s", alignString(totalChar, "Sub total", "£" + orderDetail.getSub_total())) + "\n"
                    + String.format("%s", alignString(totalChar, "Discount", "£" + orderDetail.getDiscount_amount())) + "\n"
                    + String.format("%s", alignString(totalChar, "Delivery Charge", "£" + orderDetail.getDelivery_charge())) + "\n"
                    + String.format("%s", alignString(totalChar, "Total amount", (orderDetail.getOrder_total() != null) ? "£" + orderDetail.getOrder_total() : "£")) + "\n"
                    + String.format("%s", "----------------------------------") + "\n";
        } else {
            data = String.format("%s", "----------------------------------") + "\n"
                    + String.format("%s", alignString(totalChar, "Payment Method", orderDetail.getPayment_mode())) + "\n"
                    + String.format("%s", alignString(totalChar, "Sub total", "£" + orderDetail.getSub_total())) + "\n"
                    + String.format("%s", alignString(totalChar, "Discount", "£" + orderDetail.getDiscount_amount())) + "\n"
                    + String.format("%s", alignString(totalChar, "Total amount", (orderDetail.getOrder_total() != null) ? "£" + orderDetail.getOrder_total() : "£")) + "\n"
                    + String.format("%s", "----------------------------------") + "\n";
        }

        String item = createItem(totalChar, orderDetail.getCart());

        Log.e("PRINT>>\n", header + "\n" + orderId + "\n" + item + "\n" + orderDate + "\n" + data);
        if (AidlUtil.getInstance().isConnect()) {
            if (logo != null)
                AidlUtil.getInstance().printBitmap(logo);

            AidlUtil.getInstance().printText(header, 24, false, false, 1);
            AidlUtil.getInstance().printText(orderId, 22, false, false, 1);
            AidlUtil.getInstance().printText(orderDate, 22, false, false, 1);
            AidlUtil.getInstance().printText(item, 22, true, false, 0);
            AidlUtil.getInstance().printText(data, 22, false, false, 1);
            AidlUtil.getInstance().printText(alignString(totalChar, "Note", ""), 22, false, false, 1);
            AidlUtil.getInstance().printText(orderDetail.getOrder_notes() + "", 20, false, false, 1);

            AidlUtil.getInstance().printText(footer, 22, true, false, 1);
            if (orderDetail.getOrder_notes() != null && orderDetail.getOrder_notes().trim().length() > 0) {
                AidlUtil.getInstance().printText("NOTES:", 24, true, false, 1);
                AidlUtil.getInstance().printText(orderDetail.getOrder_notes(), 24, false, false, 1);
                AidlUtil.getInstance().printText(line24Bold, 24, true, false, 1);
            }

            @Nullable Bitmap b = BarcodeUtils.encodeAsBitmap(orderDetail.getOrder_num(), BarcodeUtils.Format.CODABAR, 400, 80);
            if (b != null) {
                try {
                    AidlUtil.getInstance().printBitmap(b);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            AidlUtil.getInstance().printText("\n\n           Thank You            ", 22, true, false, 5);

        } else {
            Message msg = new Message();
            msg.obj = "Printer not attached";
            Toast.makeText(activity, "Printer not attached", Toast.LENGTH_SHORT).show();
        }
    }

    private static String createData(OrderDetailsResponse.OrderDetails printData) {
        StringBuilder data = new StringBuilder();
        int count = 0;
        List<OrderDetailsResponse.OrderDetails.Cart.Items> items;
        for (OrderDetailsResponse.OrderDetails.Cart item : printData.getCart()) {
            items = item.getItems();
            for (int i = 0; i < item.getItems().size(); i++) {
                String pName = items.get(i).getProduct_name();
                String pPrice = items.get(i).getProduct_price();
                String pQty = items.get(i).getProduct_qty();
                String pDis = "0.00";
                String pAmount = items.get(i).getTotal_amount();

                StringBuilder nName = new StringBuilder();

                data.append(pName != null && pName.length() > 7 ? pName.subSequence(0, 9) : nName + " ");
                data.append(" " + pPrice);
                data.append("  " + pQty + " ");
                data.append(" " + pDis + " ");
                data.append(" " + pAmount + "\n");
            }

        }
        return data.toString();
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

    private static class MakeData extends AsyncTask<Void, Void, String> {
        @NonNull final Context context;
        @Nullable final Bitmap logo;
        @NonNull final NewDetailBean.OrdersDetailsBean orderDetail;
        @NonNull final LoginResponse.Data restaurantData;
        int charCount = 43;
        Bitmap bitmap1 = null;
        String addressFormatToPrint = "";
        String getAddress_1 = "", address_2 = "", city = "", post_code = "", country = "", AddressToPrint = "";

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

                AidlUtil.getInstance().printBitmap(icon, 1, 1, 1);

                if (logo != null) {
                    AidlUtil.getInstance().printBitmap(logo, 1, 1, 1);
                    AidlUtil.getInstance().printText("", 20, true, false, 1, 1);
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
                        AidlUtil.getInstance().printBitmap(myBitmap, 1, 1, 1);
                    } catch (IOException e) {
                        // Log exception
                        Log.e("IOException", e.getMessage());
                        e.printStackTrace();
                    }
                }

                if (restaurantData != null) {
                    AidlUtil.getInstance().printText(restaurantData.getRestaurant_name().toUpperCase(), 26, true, false, 1, 1);
                    AidlUtil.getInstance().printText("", 20, true, false, 1, 1);
                    AidlUtil.getInstance().printText("ORDER NUMBER", 22, false, false, 1, 1);
                    AidlUtil.getInstance().printText(orderDetail.getOrder_num(), 40, true, false, 1, 1);
                    AidlUtil.getInstance().printText("", 20, true, false, 1, 1);
                    String checkPayStatus = orderDetail.getPayment_mode();
                    try {
                        Log.e("PRINT>>\n", "UNPAID" + "\t" + orderDetail.getDelivery_option().toUpperCase());
                        if (checkPayStatus.equalsIgnoreCase("Cash")) {
                            AidlUtil.getInstance().printText("UNPAID" + " - " + orderDetail.getDelivery_option().toUpperCase(), 40, true, false, 1, 1);
                        } else {
                            AidlUtil.getInstance().printText("PAID" + " - " + orderDetail.getDelivery_option().toUpperCase(), 40, true, false, 1, 1);
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

                    AidlUtil.getInstance().printText("", 20, true, false, 1, 1);
                    AidlUtil.getInstance().printText("ORDER TIME - " + OrderTime, 24, true, false, 1, 1);

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

                    AidlUtil.getInstance().printText("", 20, true, false, 1, 1);
                    AidlUtil.getInstance().printText("DELIVERY TIME", 22, false, false, 1, 1);
                    AidlUtil.getInstance().printText(DeliveryTime, 34, true, false, 1, 1);
                    AidlUtil.getInstance().printText(line24Bold, 24, true, false, 1);
                    AidlUtil.getInstance().printText(customerName.toUpperCase(), 32, true, false, 1, 1);

                    if (orderDetail.getDelivery_option().toUpperCase().equals("DELIVERY"))
                        addressFormatToPrint = AddressNameCorrect(customerAddress);
                    AidlUtil.getInstance().printText(AddressToPrint, 30, true, false, 1, 1);
                    AidlUtil.getInstance().printText("", 20, true, false, 1, 1);
                    AidlUtil.getInstance().printText(customerPhone, 40, true, false, 1, 1);

                    AidlUtil.getInstance().printText(line24Bold, 24, true, false, 1);

                    makeCartDataNew(context, charCount, orderDetail.getCart());
                    AidlUtil.getInstance().printText(line24Bold, 24, true, false, 1);

                    AidlUtil.getInstance().printText(printSpaceBetweenTwoString("DISCOUNT", context.getResources().getString(R.string.currency) + Constants.decimalFormat(Double.parseDouble(orderDetail.getDiscount_amount())), 29), 26, true, false, 1);
                    if (orderDetail.getDelivery_option().toUpperCase().equals("DELIVERY")) {
                        AidlUtil.getInstance().printText(printSpaceBetweenTwoString("DELIVERY FEE", context.getResources().getString(R.string.currency) + Constants.decimalFormat(Double.parseDouble(orderDetail.getDelivery_charge())), 29), 26, false, false, 2);
                    }

                    AidlUtil.getInstance().printText("TOTAL  " + context.getResources().getString(R.string.currency) + Constants.decimalFormat(Double.parseDouble(orderDetail.getOrder_total())), 40, true, false, 1, 1);

                    if (orderDetail.getOrder_notes() != null && !orderDetail.getOrder_notes().trim().isEmpty()) {
                        AidlUtil.getInstance().printText(line24Bold, 24, true, false, 1);
                        AidlUtil.getInstance().printText("NOTES:", 32, true, false, 1, 1);
                        AidlUtil.getInstance().printText(orderDetail.getOrder_notes(), 26, true, false, 1, 1);
                    }

                    AidlUtil.getInstance().printText(line24Bold, 24, true, false, 1);
                    AidlUtil.getInstance().printText("PAYMENT BY " + orderDetail.getPayment_mode().toUpperCase(), 32, true, false, 1, 1);

                    AidlUtil.getInstance().printText(line24Bold, 24, true, false, 5);
                }
            }
            Log.e("ddddddddddddd", "ddddddddddddd");
        }
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
                                    + String.format(amountSpace + context.getResources().getString(R.string.currency) + "%.2f", data.getMenu().get(i).getPrice())
                                    + "\n"
                    );
                } else {
                    String amountSpace = createPriceSpace(priceCharCount, data.getMenu().get(i).getPrice());
                    orderItemData.append(productFirstLine

                            + String.format(amountSpace + context.getResources().getString(R.string.currency) + "%.2f", data.getMenu().get(i).getPrice())
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
                                                + String.format(amountSpace + context.getResources().getString(R.string.currency) + "%.2f", data.getMenu().get(i).getOptions().getProductModifiers().get(j).getModifierProducts().get(k).getModifierProductPrice())
                                                + "\n"
                                );
                            } else {
                                String amountSpace = createPriceSpace(priceCharCount, data.getMenu().get(i).getPrice());

                                orderItemData.append(productFirstLine

                                        + String.format(amountSpace + context.getResources().getString(R.string.currency) + "%.2f", data.getMenu().get(i).getOptions().getProductModifiers().get(j).getModifierProducts().get(k).getModifierProductPrice())
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
                                                + String.format(amountSpace + context.getResources().getString(R.string.currency) + "%.2f", Double.parseDouble(data.getMenu().get(i).getOptions().getMealProducts().get(j).getAmount()))
                                                + "\n"
                                );
                            } else {
                                orderItemData.append(

                                        productFirstLine
                                                + String.format(amountSpace + context.getResources().getString(R.string.currency) + "%.2f", Double.parseDouble("0"))
                                                + "\n"
                                );
                            }
                        } else {

                            String amountSpace = createPriceSpace(priceCharCount, data.getMenu().get(i).getPrice());
                            if (data.getMenu().get(i).getOptions().getMealProducts().get(j).getAmount() != null) {
                                orderItemData.append(productFirstLine

                                        + String.format(amountSpace + context.getResources().getString(R.string.currency) + "%.2f", Double.parseDouble(data.getMenu().get(i).getOptions().getMealProducts().get(j).getAmount()))
                                        + "\n"
                                );
                            } else {
                                orderItemData.append(productFirstLine

                                        + String.format(amountSpace + context.getResources().getString(R.string.currency) + "%.2f", Double.parseDouble("0"))
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
                                                    + String.format(amountSpace + context.getResources().getString(R.string.currency) + "%.2f", Double.parseDouble(data.getMenu().get(i).getOptions().getMealProducts().get(j).getSizeModifiers().get(k).getSizeModifierProducts().get(l).getAmount()))
                                                    + "\n"
                                    );
                                } else {
                                    String amountSpace = createPriceSpace(priceCharCount, data.getMenu().get(i).getPrice());

                                    orderItemData.append(productFirstLine

                                            + String.format(amountSpace + context.getResources().getString(R.string.currency) + "%.2f", Double.parseDouble(data.getMenu().get(i).getOptions().getMealProducts().get(j).getSizeModifiers().get(k).getSizeModifierProducts().get(l).getAmount()))
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
                                        + String.format(amountSpace + context.getResources().getString(R.string.currency) + "%.2f", data.getMenu().get(i).getOptions().getSize().getProductSizePrice())
                                        + "\n"
                        );
                    } else {
                        String amountSpace = createPriceSpace(priceCharCount, data.getMenu().get(i).getPrice());

                        orderItemData.append(productFirstLine

                                + String.format(amountSpace + context.getResources().getString(R.string.currency) + "%.2f", data.getMenu().get(i).getOptions().getSize().getProductSizePrice())
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
                                                + String.format(amountSpace + context.getResources().getString(R.string.currency) + "%.2f", data.getMenu().get(i).getOptions().getSize().getSizemodifiers().get(j).getSizeModifierProducts().get(k).getAmount())
                                                + "\n"
                                );
                            } else {
                                String amountSpace = createPriceSpace(priceCharCount, data.getMenu().get(i).getPrice());

                                orderItemData.append(productFirstLine

                                        + String.format(amountSpace + context.getResources().getString(R.string.currency) + "%.2f", data.getMenu().get(i).getOptions().getSize().getSizemodifiers().get(j).getSizeModifierProducts().get(k).getAmount())
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
                AidlUtil.getInstance().printText(data.getMenu().get(i).getQty() + "x " + data.getMenu().get(i).getName(), 24, true, false, 0, 1);

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
                                                + String.format(amountSpace + context.getResources().getString(R.string.currency) + "%.2f", data.getMenu().get(i).getOptions().getProductModifiers().get(j).getModifierProducts().get(k).getModifierProductPrice())
                                                + "\n"
                                );
                            } else {
                                String amountSpace = createPriceSpace(priceCharCount, data.getMenu().get(i).getPrice());

                                menuOrderItemData.append(productFirstLine

                                        + String.format(amountSpace + context.getResources().getString(R.string.currency) + "%.2f", data.getMenu().get(i).getOptions().getProductModifiers().get(j).getModifierProducts().get(k).getModifierProductPrice())
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
                                                + String.format(amountSpace + context.getResources().getString(R.string.currency) + "%.2f", Double.parseDouble(data.getMenu().get(i).getOptions().getMealProducts().get(j).getAmount()))
                                                + "\n"
                                );
                            } else {
                                menuOrderItemData.append(
                                        productFirstLine
                                                + String.format(amountSpace + context.getResources().getString(R.string.currency) + "%.2f", Double.parseDouble("0"))
                                                + "\n"
                                );
                            }
                        } else {

                            String amountSpace = createPriceSpace(priceCharCount, data.getMenu().get(i).getPrice());

                            if (data.getMenu().get(i).getOptions().getMealProducts().get(j).getAmount() != null) {
                                menuOrderItemData.append(productFirstLine

                                        + String.format(amountSpace + context.getResources().getString(R.string.currency) + "%.2f", Double.parseDouble(data.getMenu().get(i).getOptions().getMealProducts().get(j).getAmount()))
                                        + "\n"
                                );
                            } else {
                                menuOrderItemData.append(productFirstLine

                                        + String.format(amountSpace + context.getResources().getString(R.string.currency) + "%.2f", Double.parseDouble("0"))
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
                                                    + String.format(amountSpace + context.getResources().getString(R.string.currency) + "%.2f", Double.parseDouble(data.getMenu().get(i).getOptions().getMealProducts().get(j).getSizeModifiers().get(k).getSizeModifierProducts().get(l).getAmount()))
                                                    + "\n"
                                    );
                                } else {
                                    String amountSpace = createPriceSpace(priceCharCount, data.getMenu().get(i).getPrice());

                                    menuOrderItemData.append(productFirstLine

                                            + String.format(amountSpace + context.getResources().getString(R.string.currency) + "%.2f", Double.parseDouble(data.getMenu().get(i).getOptions().getMealProducts().get(j).getSizeModifiers().get(k).getSizeModifierProducts().get(l).getAmount()))
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
                                        + String.format(amountSpace + context.getResources().getString(R.string.currency) + "%.2f", data.getMenu().get(i).getOptions().getSize().getProductSizePrice())
                                        + "\n"
                        );
                    } else {
                        String amountSpace = createPriceSpace(priceCharCount, data.getMenu().get(i).getPrice());

                        menuOrderItemData.append(productFirstLine

                                + String.format(amountSpace + context.getResources().getString(R.string.currency) + "%.2f", data.getMenu().get(i).getOptions().getSize().getProductSizePrice())
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
                                                + String.format(amountSpace + context.getResources().getString(R.string.currency) + "%.2f", data.getMenu().get(i).getOptions().getSize().getSizemodifiers().get(j).getSizeModifierProducts().get(k).getAmount())
                                                + "\n"
                                );
                            } else {
                                String amountSpace = createPriceSpace(priceCharCount, data.getMenu().get(i).getPrice());

                                menuOrderItemData.append(productFirstLine

                                        + String.format(amountSpace + context.getResources().getString(R.string.currency) + "%.2f", data.getMenu().get(i).getOptions().getSize().getSizemodifiers().get(j).getSizeModifierProducts().get(k).getAmount())
                                        + "\n"
                                );
                            }
                        }
                    }
                }
            }

            AidlUtil.getInstance().printText(menuOrderItemData.toString(), 22, false, false, 0, 0);

            AidlUtil.getInstance().printText(String.format(context.getResources().getString(R.string.currency) + "%.2f", data.getMenu().get(i).getPrice()), 24, true, false, 2, 1);
        }
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

        return priceSpace;
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

    private static String createItem(int totalChar, List<OrderDetailsResponse.OrderDetails.Cart> cartItems) {
        StringBuilder data = new StringBuilder();
        int _totalChar = totalChar - 17;

        for (OrderDetailsResponse.OrderDetails.Cart _item : cartItems) {
            for (OrderDetailsResponse.OrderDetails.Cart.Items item : _item.getItems()) {
                String _name = item.getProduct_name();
                String _qty = item.getProduct_qty();
                String _price = String.format("%.2f", Float.parseFloat(item.getProduct_price()));
                String _amount = item.getTotal_amount();
                Log.e("IS > 1111", "" + (_name.toString().length() > _totalChar));
                if (_name.toString().length() > _totalChar) {
                    _name = _name.substring(0, (_totalChar - 4));
                    _name += "...";
                } else {
                    int loopCount = (_totalChar - _name.toString().length());
                    Log.e("22222222", _totalChar + " > " + _name.toString().length() + " > " + loopCount);
                    for (int i = 1; i < loopCount; i++) {
                        _name += " ";
                    }
                }

                if (_price.toString().length() < 5) {
                    int spaceCount = (5 - _price.toString().length());
                    for (int i = 0; i < spaceCount; i++) {
                        _price += " ";
                    }
                }

                totalChar = (totalChar - _amount.toString().length());
                data.append(_qty + "x " + _name + " " + "£" + _price + "  " + "£" + _amount + "\n");
            }
        }
        return data.toString();
    }

    private static String alignString(int totalChar, String title, String contant) {
        StringBuilder data = new StringBuilder();
        int remainingChar = (totalChar - title.toString().length());
        if (contant != null)
            remainingChar = (remainingChar - contant.length());
        data.append(title);
        for (int i = 0; i < remainingChar; i++) {
            data.append(" ");
        }
        if (contant != null)
            data.append(contant);
        return data.toString();
    }
}
