package com.programeira.vuttr.view

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import com.programeira.vuttr.R
import com.programeira.vuttr.data.model.Tool
import kotlinx.android.synthetic.main.dialog_add_tool.*

class AddDialog(
    activity: Activity,
    private val callback: (tool: Tool) -> Unit
) : Dialog(activity), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_add_tool)
        btnDialogAdd.setOnClickListener(this)
        imageBtnDialogAddClose.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.imageBtnDialogAddClose -> dismiss()
            R.id.btnDialogAdd -> callback(getToolObject())
            else -> {}
        }
        dismiss()
    }

    private fun getToolObject() : Tool {
        val name = inputTextDialogAddName.editText?.text.toString()
        val link = inputTextDialogAddLink.editText?.text.toString()
        val description = inputTextDialogAddDescription.editText?.text.toString()
        val tags = inputTextDialogAddTags.editText?.text.toString().split(" ")
        return Tool(
            name = name,
            link = link,
            description = description,
            tags = tags
        )
    }
}