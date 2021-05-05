package com.programeira.vuttr.view

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.programeira.vuttr.R
import com.programeira.vuttr.data.model.Tool
import com.programeira.vuttr.data.model.ToolResponse
import com.programeira.vuttr.view.adapters.HomeListAdapter
import com.programeira.vuttr.view.dialogs.AddDialog
import com.programeira.vuttr.view.dialogs.RemoveDialog
import com.programeira.vuttr.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.view_error.*
import kotlinx.android.synthetic.main.view_search_field.*

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
        setUpListeners()
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
        checkboxTags.isChecked = false
        viewModel.getTools(this@HomeActivity)
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(this@HomeActivity).get(HomeViewModel::class.java)
    }

    private fun setUpListeners() {
        fab.setOnClickListener {
            addToolDialog = AddDialog(this@HomeActivity, ::callAddTool)
            addToolDialog.show()
        }
        searchField.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.getToolsBySearch(
                    this@HomeActivity, searchField.query.toString(), checkboxTags.isChecked
                )
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {return false}
        })
        btnErrorRetry.setOnClickListener {
            viewModel.retry(
                this@HomeActivity,
                searchField.query.toString(),
                checkboxTags.isChecked
            )
        }
    }

    private fun setUpObservers() {
        viewModel.showNoConnectionAlert.observe(this@HomeActivity, Observer { show ->
            if (show) {
                Snackbar.make(clHome, "No internet connection :(", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
        })
        viewModel.showErrorAlert.observe(this@HomeActivity, Observer { show ->
            if (show) {
                Snackbar.make(clHome, "Something went wrong :(", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
        })
        viewModel.showRetryError.observe(this@HomeActivity, Observer { show ->
            if (show) showError()
            else hideError()
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

    private fun showError() {
        rvToolsList.visibility = GONE
        includeError.visibility = VISIBLE
    }

    private fun hideError() {
        rvToolsList.visibility = VISIBLE
        includeError.visibility = GONE
    }
}