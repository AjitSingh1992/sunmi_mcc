package com.easyfoodvone.utility.printerutil;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.easyfoodvone.R;

import java.util.ArrayList;
import java.util.List;

import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;


public class AidlUtil {
    private static final String SERVICE＿PACKAGE = "woyou.aidlservice.jiuiv5";
    private static final String SERVICE＿ACTION = "woyou.aidlservice.jiuiv5.IWoyouService";

    private IWoyouService woyouService;
    private static AidlUtil mAidlUtil = new AidlUtil();
    private Context context;

    private AidlUtil() {
    }

    public static AidlUtil getInstance() {
        return mAidlUtil;
    }

    public void openDrawer(ICallback callback) throws RemoteException {
        woyouService.openDrawer(callback);
    }

    public void setFont(String fontName) {
        if (woyouService != null) {

            try {
                woyouService.setFontName(fontName, null);
            } catch (RemoteException e) {
                e.printStackTrace();
                Log.e("AidlUtil", e.toString());
            }
        }
    }

    /**
     * 连接服务
     *
     * @param context context
     */
    public void connectPrinterService(Context context) {
        this.context = context.getApplicationContext();
        Intent intent = new Intent();
        intent.setPackage(SERVICE＿PACKAGE);
        intent.setAction(SERVICE＿ACTION);
        context.getApplicationContext().startService(intent);
        context.getApplicationContext().bindService(intent, connService, Context.BIND_AUTO_CREATE);
    }

    /**
     * 断开服务
     *
     * @param context context
     */
    public void disconnectPrinterService(Context context) {
        if (woyouService != null) {
            context.getApplicationContext().unbindService(connService);
            woyouService = null;
        }
    }

    public boolean isConnect() {
        return woyouService != null;
    }

    private ServiceConnection connService = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            woyouService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            woyouService = IWoyouService.Stub.asInterface(service);
        }
    };

    public ICallback generateCB(final PrinterCallback printerCallback) {
        return new ICallback.Stub() {
            @Override
            public void onRunResult(boolean isSuccess) throws RemoteException {

            }

            @Override
            public void onReturnString(String result) throws RemoteException {

            }

            @Override
            public void onRaiseException(int code, String msg) throws RemoteException {

            }

            @Override
            public void onPrintResult(int code, String msg) throws RemoteException {

            }


        };
    }

    /**
     * 设置打印浓度
     */
    private int[] darkness = new int[]{0x0600, 0x0500, 0x0400, 0x0300, 0x0200, 0x0100, 0,
            0xffff, 0xfeff, 0xfdff, 0xfcff, 0xfbff, 0xfaff};

    public void setDarkness(int index) {
        if (woyouService == null) {
            // Toast.makeText(context, R.string.toast_2, Toast.LENGTH_LONG).show();
            return;
        }

        int k = darkness[index];
        try {
            woyouService.sendRAWData(ESCUtil.setPrinterDarkness(k), null);
            woyouService.printerSelfChecking(null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 取得打印机系统信息，放在list中
     *
     * @return list
     */
    public List<String> getPrinterInfo() {
        if (woyouService == null) {
            //Toast.makeText(context, R.string.toast_2, Toast.LENGTH_LONG).show();
            return null;
        }

        List<String> info = new ArrayList<>();
        try {
            info.add(woyouService.getPrinterSerialNo());
            info.add(woyouService.getPrinterModal());
            info.add(woyouService.getPrinterVersion());
            info.add(woyouService.getPrintedLength() + "");
            info.add("");
            //info.add(woyouService.getServiceVersion());
            PackageManager packageManager = context.getPackageManager();
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(SERVICE＿PACKAGE, 0);
                if (packageInfo != null) {
                    info.add(packageInfo.versionName);
                    info.add(packageInfo.versionCode + "");
                } else {
                    info.add("");
                    info.add("");
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return info;
    }

    /**
     * 初始化打印机
     */
    public void initPrinter() {
        if (woyouService == null) {
            // Toast.makeText(context, R.string.toast_2, Toast.LENGTH_LONG).show();
            return;
        }

        try {
            woyouService.printerInit(null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印二维码
     */
    public void printQr(String data, int modulesize, int errorlevel) {
        if (woyouService == null) {
            //Toast.makeText(context, R.string.toast_2, Toast.LENGTH_LONG).show();
            return;
        }


        try {
            woyouService.setAlignment(1, null);
            woyouService.printQRCode(data, modulesize, errorlevel, null);
            woyouService.lineWrap(3, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印条形码
     */
    public void printBarCode(String data, int symbology, int height, int width, int textposition) {
        if (woyouService == null) {
            //Toast.makeText(context, R.string.toast_2, Toast.LENGTH_LONG).show();
            return;
        }


        try {
            woyouService.printBarCode(data, symbology, height, width, 0, null);
            woyouService.lineWrap(3, null);
            woyouService.cutPaper(null);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void printBarCode(String data, int symbology, int height, int width, int textposition, Boolean cutPaper) {
        if (woyouService == null) {
            //Toast.makeText(context, R.string.toast_2, Toast.LENGTH_LONG).show();
            return;
        }


        try {
            woyouService.printBarCode(data, symbology, height, width, 0, null);
            woyouService.lineWrap(3, null);
            if (cutPaper)
                woyouService.cutPaper(null);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void cutPaper() {
        if (woyouService == null) {
            // Toast.makeText(context, R.string.toast_2, Toast.LENGTH_LONG).show();
            return;
        }
        try {
            woyouService.cutPaper(null);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void printBarCode(List<String> barCode, List<String> productIdAndName, int symbology, int height, int width, int textposition) {
        if (woyouService == null) {
            //Toast.makeText(context, R.string.toast_2, Toast.LENGTH_LONG).show();
            return;
        }


        try {
            for (int i = 0; i < barCode.size(); i++) {
                AidlUtil.getInstance().printBarcodeText(productIdAndName.get(i), 20, false, false);
                woyouService.printBarCode(barCode.get(i), symbology, height, width, 0, null);
                woyouService.lineWrap(3, null);
            }

            woyouService.cutPaper(null);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void printZRead(List<String> orderId, List<String> orderAmount, int symbology, int height, int width, int textposition) {
        if (woyouService == null) {
            //Toast.makeText(context, R.string.toast_2, Toast.LENGTH_LONG).show();
            return;
        }


        try {
            for (int i = 0; i < orderId.size(); i++) {
                AidlUtil.getInstance().printBarcodeText(orderId.get(i), 0, false, false);
                AidlUtil.getInstance().printBarcodeText(orderAmount.get(i), 20, false, false);

                woyouService.lineWrap(2, null);
            }

            woyouService.cutPaper(null);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印文字
     */
    public void printText(String content, float size, boolean isBold, boolean isUnderLine, int lineWrap) {
        if (woyouService == null) {
            // Toast.makeText(context, R.string.toast_2, Toast.LENGTH_LONG).show();
            return;
        }
        try {
            woyouService.setFontName(" gh ", null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        try {
            if (isBold) {
                woyouService.sendRAWData(ESCUtil.boldOn(), null);
            } else {
                woyouService.sendRAWData(ESCUtil.boldOff(), null);
            }

            if (isUnderLine) {
                woyouService.sendRAWData(ESCUtil.underlineWithOneDotWidthOn(), null);
            } else {
                woyouService.sendRAWData(ESCUtil.underlineOff(), null);
            }
            woyouService.setAlignment(0, null);
            woyouService.printTextWithFont(content, null, size, null);
            woyouService.lineWrap(lineWrap, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    public void printText(String content, float size, boolean isBold, boolean isUnderLine, int alignment, int lineWrap) {
        if (woyouService == null) {
            // Toast.makeText(context, R.string.toast_2, Toast.LENGTH_LONG).show();
            return;
        }
        try {
            woyouService.setFontName(" gh ", null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        try {
            if (isBold) {
                woyouService.sendRAWData(ESCUtil.boldOn(), null);
            } else {
                woyouService.sendRAWData(ESCUtil.boldOff(), null);
            }

            if (isUnderLine) {
                woyouService.sendRAWData(ESCUtil.underlineWithOneDotWidthOn(), null);
            } else {
                woyouService.sendRAWData(ESCUtil.underlineOff(), null);
            }
            woyouService.setAlignment(alignment, null);
            woyouService.printTextWithFont(content, "gh", size, null);
            woyouService.lineWrap(lineWrap, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    public void printBarcodeText(String content, float size, boolean isBold, boolean isUnderLine) {
        if (woyouService == null) {
            // Toast.makeText(context, R.string.toast_2, Toast.LENGTH_LONG).show();
            return;
        }

        try {
            if (isBold) {
                woyouService.sendRAWData(ESCUtil.boldOn(), null);
            } else {
                woyouService.sendRAWData(ESCUtil.boldOff(), null);
            }

            if (isUnderLine) {
                woyouService.sendRAWData(ESCUtil.underlineWithOneDotWidthOn(), null);
            } else {
                woyouService.sendRAWData(ESCUtil.underlineOff(), null);
            }

            woyouService.printTextWithFont(content, null, size, null);
            woyouService.lineWrap(1, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    public void printMerchantLogo(String content, float size, boolean isBold, boolean isUnderLine) {
        if (woyouService == null) {
            //Toast.makeText(context, "The service has been disconnected", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            if (isBold) {
                woyouService.sendRAWData(ESCUtil.boldOn(), null);
            } else {
                woyouService.sendRAWData(ESCUtil.boldOff(), null);
            }

            if (isUnderLine) {
                woyouService.sendRAWData(ESCUtil.underlineWithOneDotWidthOn(), null);
            } else {
                woyouService.sendRAWData(ESCUtil.underlineOff(), null);
            }

            woyouService.printTextWithFont(content, null, size, null);
            woyouService.lineWrap(1, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    public void printBitmap(Bitmap bitmap, int orientation, int alignment,int lineWrap) {
        if (woyouService == null) {

            // Toast.makeText(context, "Service has been disconnected！", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            if (orientation == 0) {
                woyouService.printBitmap(bitmap, null);
            } else {
                woyouService.printBitmap(bitmap, null);
            }
            woyouService.setAlignment(alignment,null);
            woyouService.lineWrap(lineWrap, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /*
     *打印图片
     */
    public void printBitmap(Bitmap bitmap) {
        if (woyouService == null) {
            //Toast.makeText(context, R.string.toast_2, Toast.LENGTH_LONG).show();
            return;
        }

        try {
            woyouService.setAlignment(1, null);
            woyouService.printBitmap(bitmap, null);
            woyouService.lineWrap(0, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印图片和文字按照指定排列顺序
     */
    public void printBitmap(Bitmap bitmap, int orientation) {
        if (woyouService == null) {
            // Toast.makeText(context, "服务已断开！", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            if (orientation == 0) {
                woyouService.printBitmap(bitmap, null);
                woyouService.printText("Horizontal arrangement\n\n", null);
                woyouService.printBitmap(bitmap, null);
                woyouService.printText("Horizontal arrangement\n", null);
            } else {
                woyouService.printBitmap(bitmap, null);
                woyouService.printText("\nVertical arrangement\n", null);
                woyouService.printBitmap(bitmap, null);
                woyouService.printText("\nVertical arrangement\n", null);
            }
            woyouService.lineWrap(3, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    /**
     * 打印表格
     */
    /*public void printTable(LinkedList<TableItem> list) {
        if (woyouService == null) {
            Toast.makeText(context, R.string.toast_2, Toast.LENGTH_LONG).show();
            return;
        }

        try {
            for (TableItem tableItem : list) {
                Log.i("kaltin", "printTable: " + tableItem.getText()[0] + tableItem.getText()[1] + tableItem.getText()[2]);
                woyouService.printColumnsText(tableItem.getText(), tableItem.getWidth(), tableItem.getAlign(), null);
            }
            woyouService.lineWrap(3, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }*/

    /*
     * 空打三行！
     */
    public void print3Line() {
        if (woyouService == null) {
            // Toast.makeText(context, R.string.toast_2, Toast.LENGTH_LONG).show();
            return;
        }

        try {
            woyouService.lineWrap(3, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public void sendRawData(byte[] data) {
        if (woyouService == null) {
            //  Toast.makeText(context, R.string.toast_2, Toast.LENGTH_LONG).show();
            return;
        }

        try {
            woyouService.sendRAWData(data, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    //获取当前的打印模式
    public int getPrintMode() {
        if (woyouService == null) {
            // Toast.makeText(context, "Service has been disconnected\n！", Toast.LENGTH_LONG).show();
            return -1;
        }

        int res;
        try {
            res = woyouService.getPrinterMode();
        } catch (RemoteException e) {
            e.printStackTrace();
            res = -1;
        }
        return res;
    }

    /*public void printTableColumn(TableItem tableItem) {
        if (woyouService == null) {
            Toast.makeText(context, R.string.toast_2, Toast.LENGTH_LONG).show();
            return;
        }

        try {

            Log.i("kaltin", "printTable: " + tableItem.getText()[0] + tableItem.getText()[1] + tableItem.getText()[2]);
            woyouService.printColumnsText(tableItem.getText(), tableItem.getWidth(), tableItem.getAlign(), null);

            woyouService.lineWrap(3, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }*/


}
