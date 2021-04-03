package com.programeira.vuttr.view

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.programeira.vuttr.R
import com.programeira.vuttr.data.model.Tool
import com.programeira.vuttr.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.activity_main.*

class HomeActivity : AppCompatActivity() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var loading: AlertDialog
    private lateinit var addToolDialog: AddDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpViewModel()
        setUpClickListeners()
        setUpObservers()
        setUpContent()
    }

    private fun setUpContent() {
        viewModel.getTools(this@HomeActivity)
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(this@HomeActivity).get(HomeViewModel::class.java)
    }

    private fun setUpClickListeners() {
        fab.setOnClickListener {
            addToolDialog = AddDialog(this@HomeActivity, ::callAddTool)
            addToolDialog.show()
        }
    }

    private fun setUpObservers() {
        viewModel.showNoConnectionAlert.observe(this@HomeActivity, Observer { show ->
            if (show) {
                Snackbar.make(clHome, "No internet connection :(", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
        })
        viewModel.showLoading.observe(this@HomeActivity, Observer { show ->
            if (show) loading = LoadingAlert.loadingDialog(this@HomeActivity)
            else loading.dismiss()
        })
    }

    private fun callAddTool(tool: Tool) {
        viewModel.addTool(this@HomeActivity, tool)
    }
}