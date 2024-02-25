package com.easyfoodvone;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;


import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class PrinterTestActivity extends Activity  {
    private static final String TAG = "PrinterTestActivity";
    private static final int REQUEST_ACCOUNT = 0;
    private static final Random RANDOM = new Random(SystemClock.currentThreadTimeMillis());

    private Account account;

    private TextView resultGetPrintersText;
    private TextView statusGetPrintersText;
    private Button buttonGetPrinters;

    private TextView resultGetPrintersCategoryText;
    private TextView statusGetPrintersCategoryText;
    private Button buttonGetPrintersCategory;
    private RadioGroup radioGroupGetPrintersCategory;

    private TextView resultIssetText;
    private TextView statusIssetText;
    private Button buttonIsset;

    private TextView resultGetPrinterIdText;
    private TextView statusGetPrinterIdText;
    private Button buttonGetPrinterId;
    private EditText editGetPrinterId;

    private TextView resultSetPrinterText;
    private TextView statusSetPrinterText;
    private Button buttonSetPrinter;

    private TextView statusRemovePrinterText;
    private EditText editRemovePrinter;
    private Button buttonRemovePrinter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer_test);

        resultGetPrintersText = (TextView) findViewById(R.id.result_get_printers);
        statusGetPrintersText = (TextView) findViewById(R.id.status_get_printers);
        buttonGetPrinters = (Button) findViewById(R.id.button_get_printers);

        resultGetPrintersCategoryText = (TextView) findViewById(R.id.result_get_printers_category);
        statusGetPrintersCategoryText = (TextView) findViewById(R.id.status_get_printers_category);
        buttonGetPrintersCategory = (Button) findViewById(R.id.button_get_printers_category);
        radioGroupGetPrintersCategory = (RadioGroup) findViewById(R.id.radiogroup_get_printers_category);
        radioGroupGetPrintersCategory.check(R.id.button_receipt);

        resultIssetText = (TextView) findViewById(R.id.result_isset);
        statusIssetText = (TextView) findViewById(R.id.status_isset);
        buttonIsset = (Button) findViewById(R.id.button_isset);

        resultGetPrinterIdText = (TextView) findViewById(R.id.result_get_printer_id);
        statusGetPrinterIdText = (TextView) findViewById(R.id.status_get_printer_id);
        buttonGetPrinterId = (Button) findViewById(R.id.button_get_printer_id);
        editGetPrinterId = (EditText) findViewById(R.id.edit_get_printer_id);

        resultSetPrinterText = (TextView) findViewById(R.id.result_set_printer);
        statusSetPrinterText = (TextView) findViewById(R.id.status_set_printer);
        buttonSetPrinter = (Button) findViewById(R.id.button_set_printer);

        statusRemovePrinterText = (TextView) findViewById(R.id.status_remove_printer);
        buttonRemovePrinter = (Button) findViewById(R.id.button_remove_printer);
        editRemovePrinter = (EditText) findViewById(R.id.edit_remove_printer);

    }





    @Override
    protected void onResume() {
        super.onResume();


    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ACCOUNT) {
            if (resultCode == RESULT_OK) {
                String name = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                String type = data.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE);

                account = new Account(name, type);
            } else {
                if (account == null) {
                    finish();
                }
            }
        }
    }
}