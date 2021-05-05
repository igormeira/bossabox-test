package com.programeira.vuttr.view.dialogs

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.programeira.vuttr.R

class LoadingAlert {

    companion object {
        fun loadingDialog(context: Context): AlertDialog {
            val builder = MaterialAlertDialogBuilder(context)
            builder
                .setView(R.layout.alert_loading)
                .setCancelable(false)
            val dialog = builder.show()
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            return dialog
        }
    }
}