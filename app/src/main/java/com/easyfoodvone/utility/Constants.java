package com.easyfoodvone.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;

import com.easyfoodvone.R;
import com.easyfoodvone.app_common.separation.ObservableField;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Constants {

    public interface DialogClickedListener {
        void onDialogClicked();
        void onDialogRejectClicked();
    }

    public static final String REGISTRATION_COMPLETE = "Reg_comp";
    public static final String ORDER_NUMBER = "orderNumber";
    private static DecimalFormat decimalFormat = new DecimalFormat("#0.00");

    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
    public static final String NOTIFICATION_TYPE_ACCEPTED = "new";
    public static final String NOTIFICATION_CHARITY_STATUS = "charity_status_update";
    public static final String NOTIFICATION_TYPE_CANCELED = "cancel";
    public static final String CHARITY_STATUS_INTENT = "charity_status_update";
    public static final String OPEN_CLOSE_INTENT = "local";

    public static Date getDateFromString(String date, String _format) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(_format);
        return format.parse(date);
    }

    public static void alertDialog(String msg, Activity activity, final DialogClickedListener dialogClicked) {
        LayoutInflater factory = LayoutInflater.from(activity);
        final View mDialogView = factory.inflate(R.layout.alert_dialog, null);
        final AlertDialog mDialog = new AlertDialog.Builder(activity).create();
        mDialog.setView(mDialogView);
        TextView msgText = mDialogView.findViewById(R.id.txt_message);
        msgText.setText(msg);
        mDialogView.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //your business logic
                mDialog.dismiss();
                dialogClicked.onDialogClicked();
            }
        });

        mDialog.show();
    }

    public static void alertDialogReject(String msg, Activity activity, final DialogClickedListener dialogClicked) {
        LayoutInflater factory = LayoutInflater.from(activity);
        final View mDialogView = factory.inflate(R.layout.alert_dialog, null);
        final AlertDialog mDialog = new AlertDialog.Builder(activity).create();
        mDialog.setView(mDialogView);
        TextView msgText = mDialogView.findViewById(R.id.txt_message);
        msgText.setText(msg);
        mDialogView.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //your business logic
                mDialog.dismiss();
                dialogClicked.onDialogRejectClicked();
            }
        });

        mDialog.show();
    }

    public static String decimalFormat(Double aDouble) {
        return decimalFormat.format(aDouble);
    }

    public static void dateSelector1OnClick(final ObservableField<String> editText, final Activity activity) {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog.OnDateSetListener dpd = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Time chosenDate = new Time();
                chosenDate.set(dayOfMonth, monthOfYear, year);
                long dtDob = chosenDate.toMillis(true);

                CharSequence strDate = DateFormat.format("yyyy-MM-dd", dtDob);

                editText.set(strDate.toString());
            }
        };

        DatePickerDialog d = new DatePickerDialog(activity, dpd, mYear, mMonth, mDay);
        d.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        d.show();
    }

    public static void dateSelector1(final TextView editText, final Activity activity) {
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog.OnDateSetListener dpd = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {

                        CharSequence strDate = null;
                        Time chosenDate = new Time();
                        chosenDate.set(dayOfMonth, monthOfYear, year);
                        long dtDob = chosenDate.toMillis(true);

                        strDate = DateFormat.format("yyyy-MM-dd", dtDob);

                        editText.setText(strDate);
                    }
                };

                DatePickerDialog d = new DatePickerDialog(activity, dpd, mYear, mMonth, mDay);
                d.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                d.show();
            }
        });
    }

    public static void endDateSelectorClick(final ObservableField<String> editText, final Activity activity, final String startDate) {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        String myDate = startDate;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(myDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long millis = date.getTime();

        DatePickerDialog.OnDateSetListener dpd = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Time chosenDate = new Time();
                chosenDate.set(dayOfMonth, monthOfYear, year);
                long dtDob = chosenDate.toMillis(true);

                CharSequence strDate = DateFormat.format("yyyy-MM-dd", dtDob);

                editText.set(strDate.toString());
            }
        };

        DatePickerDialog d = new DatePickerDialog(activity, dpd, mYear, mMonth, mDay);
        d.getDatePicker().setMinDate(millis - 1000);
        d.show();
    }

    public static void endDateSelector(final TextView editText, final Activity activity, final String startDate) {
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                String myDate = startDate;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    date = sdf.parse(myDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long millis = date.getTime();

                DatePickerDialog.OnDateSetListener dpd = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        CharSequence strDate = null;
                        Time chosenDate = new Time();
                        chosenDate.set(dayOfMonth, monthOfYear, year);
                        long dtDob = chosenDate.toMillis(true);

                        strDate = DateFormat.format("yyyy-MM-dd", dtDob);

                        editText.setText(strDate);
                    }
                };

                DatePickerDialog d = new DatePickerDialog(activity, dpd, mYear, mMonth, mDay);
                d.getDatePicker().setMinDate(millis - 1000);
                d.show();
            }
        });
    }

    public interface DateSetListener {
        void onDateSet(String date);
    }

    public static void showDateSelectorForPastDate(Activity activity, final DateSetListener dateSetCallback) {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener dpd = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Time chosenDate = new Time();
                chosenDate.set(dayOfMonth, monthOfYear, year);
                long dtDob = chosenDate.toMillis(true);

                @NonNull String strDate = DateFormat.format("dd-MMM-yyyy", dtDob).toString();
                dateSetCallback.onDateSet(strDate);
            }
        };

        DatePickerDialog d = new DatePickerDialog(activity, dpd, mYear, mMonth, mDay);
        d.getDatePicker().setMaxDate(System.currentTimeMillis());
        d.show();
    }

    public static String getDateFromDateTime(String input, String formatFrom, String formatTo) {
        String strDate = null;
        SimpleDateFormat mFormatFrom = new SimpleDateFormat(formatFrom);
        SimpleDateFormat mFormatTo = new SimpleDateFormat(formatTo);
        if (null == input) {
            return null;
        }
        try {
            Date date = mFormatFrom.parse(input);
            strDate = mFormatTo.format(date);
        } catch (ParseException e) {
            strDate = input;
        }

        return strDate;
    }

    // TODO use other version of this method below - pass in an ObservableField from databinding rather than the EditText itself
    public static void selectTime(final TextView editText, final Context activity) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                editText.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    public static void selectTime(final ObservableField<String> textField, final Context activity) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                textField.set(String.format("%02d:%02d", selectedHour, selectedMinute));
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    public static String getYesterdayDateString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return dateFormat.format(cal.getTime());
    }

    public static String getCurrentDateString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }

    public static String getYesterdayDateString1() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return dateFormat.format(cal.getTime());
    }

    public static String getCurrentDateString1() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }
}
