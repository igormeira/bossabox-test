package com.programeira.vuttr.view

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import com.programeira.vuttr.R
import com.programeira.vuttr.data.model.Tool
import kotlinx.android.synthetic.main.dialog_add_tool.*
import kotlinx.android.synthetic.main.dialog_add_tool.imageBtnDialogAddClose
import kotlinx.android.synthetic.main.dialog_remove_tool.*

class RemoveDialog(
    activity: Activity,
    private val id: Int,
    private val title: String,
    private val callback: (id: Int) -> Unit
) : Dialog(activity), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_remove_tool)
        tvDialogRemoveDescription.text = "Are you sure you want to remove $title?"
        btnDialogRemovePositive.setOnClickListener(this)
        btnDialogRemoveCancel.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnDialogRemoveCancel -> dismiss()
            R.id.btnDialogRemovePositive -> callback(id)
            else -> {}
        }
        dismiss()
    }
}