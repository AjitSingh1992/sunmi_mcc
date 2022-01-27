package com.easyfoodvone.app_ui.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.easyfoodvone.app_ui.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class RoundedDialogFragment(dialogView: View, val cancellable: Boolean) : DialogFragment() {
    private var dialogViewOrNull: View? = dialogView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isCancelable = cancellable
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireActivity(), R.style.Material_AlertDialog_Rounded)
                .setView(dialogViewOrNull ?: throw IllegalStateException("Unexpected reuse"))
                .create()
    }

    override fun onDestroy() {
        dialogViewOrNull = null

        super.onDestroy()
    }
}