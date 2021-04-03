package com.programeira.vuttr.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.programeira.vuttr.data.model.ToolResponse

class HomeListAdapter(
    private val tools: ArrayList<ToolResponse>,
    private val context: Context,
    private val callback: (Int, String) -> Unit
    ) : RecyclerView.Adapter<HomeListViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeListViewHolder {
        val inflatedView = LayoutInflater.from(parent.context)
        return HomeListViewHolder(inflatedView, parent, context, callback)
    }

    override fun onBindViewHolder(holder: HomeListViewHolder, position: Int) {
        val tool = tools[position]
        holder.bind(tool)
    }

    override fun getItemCount() = tools.size
}