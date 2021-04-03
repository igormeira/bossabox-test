package com.programeira.vuttr.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.programeira.vuttr.R
import com.programeira.vuttr.data.model.ToolResponse

class HomeListViewHolder(
    inflater: LayoutInflater,
    parent: ViewGroup,
    private val context: Context,
    private val callback: (Int, String) -> Unit
) : RecyclerView.ViewHolder(inflater.inflate(R.layout.view_tools_item, parent, false)) {

    private val title: AppCompatTextView = itemView.findViewById(R.id.tvTitleItemCard)
    private val description: AppCompatTextView = itemView.findViewById(R.id.tvDescriptionItemCard)
    private val tags: AppCompatTextView = itemView.findViewById(R.id.tvTagsItemCard)
    private val deleteBtn: AppCompatImageButton = itemView.findViewById(R.id.imageBtnDeleteItemCard)

    fun bind(tool: ToolResponse) {
        title.text = tool.title
        description.text = tool.description
        tags.text = treatTags(tool.tags)
        deleteBtn.setOnClickListener { callback(tool.id, tool.title) }
    }

    private fun treatTags(tags: List<String>) : String {
        var result = ""
        for (tag in tags) {
            result += "#${tag} "
        }
        return result
    }
}