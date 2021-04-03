package com.programeira.vuttr.view

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.programeira.vuttr.R
import com.programeira.vuttr.data.model.Tool
import com.programeira.vuttr.data.model.ToolResponse
import com.programeira.vuttr.view.adapters.HomeListAdapter
import com.programeira.vuttr.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class HomeActivity : AppCompatActivity() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var loading: AlertDialog
    private lateinit var addToolDialog: AddDialog
    private lateinit var removeToolDialog: RemoveDialog
    private lateinit var listAdapter: HomeListAdapter
    private var toolsList = arrayListOf<ToolResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpViewModel()
        setUpClickListeners()
        setUpObservers()
        setUpAdapters()
        setUpContent()
    }

    private fun setUpAdapters() {
        val layoutManager =  LinearLayoutManager(this@HomeActivity, LinearLayoutManager.VERTICAL, false)
        rvToolsList.layoutManager = layoutManager
        listAdapter = HomeListAdapter(toolsList, this@HomeActivity, ::callDeleteDialog)
        rvToolsList.adapter = listAdapter
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
        viewModel.toolsObjects.observe(this@HomeActivity, Observer { tools ->
            toolsList.clear()
            toolsList.addAll(tools)
            listAdapter.notifyDataSetChanged()
        })
        viewModel.addedTool.observe(this@HomeActivity, Observer { tool ->
            val newPosition = toolsList.size
            toolsList.add(newPosition, tool)
            listAdapter.notifyItemInserted(newPosition)
        })
        viewModel.deletedTool.observe(this@HomeActivity, Observer { position ->
            toolsList.removeAt(position)
            listAdapter.notifyItemRemoved(position)
        })
    }

    private fun callAddTool(tool: Tool) {
        viewModel.addTool(this@HomeActivity, tool)
    }

    private fun callDeleteDialog(id: Int, title: String) {
        removeToolDialog = RemoveDialog(this@HomeActivity, id, title, ::callDeleteItem)
        removeToolDialog.show()
    }

    private fun callDeleteItem(id: Int) {
        viewModel.removeTool(this@HomeActivity, id)
    }
}