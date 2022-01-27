package com.easyfoodvone.dialogs;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.easyfoodvone.R;
import com.easyfoodvone.app_ui.fragment.RoundedDialogFragment;
import com.easyfoodvone.models.LoginResponse;

public class PinDialog {

    public interface ParentInterface {
        void onDismiss();
        void onSubmitCorrect();
        void onSubmitIncorrect();
    }

    private final Fragment parent;
    private final LoginResponse.Data loginResponse;
    private final ParentInterface parentInterface;

    public PinDialog(Fragment parent, LoginResponse.Data loginResponse, ParentInterface parentInterface) {
        this.parent = parent;
        this.loginResponse = loginResponse;
        this.parentInterface = parentInterface;
    }

    public void alertDialogMPIN(String message) {
        final char[] pin = loginResponse.getPincode().toCharArray();

        final View mDialogView = LayoutInflater.from(parent.getActivity()).inflate(R.layout.popup_enter_mpin, null);
        final RoundedDialogFragment mDialog = new RoundedDialogFragment(mDialogView, false);

        final EditText firstMpin = mDialogView.findViewById(R.id.mpin_first);
        final EditText secondMpin = mDialogView.findViewById(R.id.mpin_two);
        final EditText thirdMpin = mDialogView.findViewById(R.id.mpin_three);
        final EditText fourthMpin = mDialogView.findViewById(R.id.mpin_four);
        final TextView error = mDialogView.findViewById(R.id.txt_error_message);
        final TextView msg = mDialogView.findViewById(R.id.txt_message);

        msg.setText(message);

        firstMpin.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    secondMpin.requestFocus();
                }
            }
        });
        secondMpin.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    thirdMpin.requestFocus();
                } else {
                    firstMpin.requestFocus();
                }
            }
        });
        thirdMpin.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    fourthMpin.requestFocus();
                } else {
                    secondMpin.requestFocus();
                }
            }
        });
        fourthMpin.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {

                } else {
                    thirdMpin.requestFocus();
                }
            }
        });

        mDialogView.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstMpin.getText().toString().trim().equalsIgnoreCase(String.valueOf(pin[0]))
                        && secondMpin.getText().toString().trim().equalsIgnoreCase(String.valueOf(pin[1]))
                        && thirdMpin.getText().toString().trim().equalsIgnoreCase(String.valueOf(pin[2]))
                        && fourthMpin.getText().toString().trim().equalsIgnoreCase(String.valueOf(pin[3]))) {
                    mDialog.dismiss();

                    error.setVisibility(View.GONE);

                    parentInterface.onSubmitCorrect();

                } else {
                    error.setVisibility(View.VISIBLE);

                    parentInterface.onSubmitIncorrect();
                }
            }
        });
        mDialogView.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();

                parentInterface.onDismiss();
            }
        });

        mDialog.showNow(parent.getChildFragmentManager(), null);
    }
}
