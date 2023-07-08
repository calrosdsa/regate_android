package app.regate.map.view

import android.view.View
import android.widget.TextView


import androidx.recyclerview.widget.RecyclerView
import app.regate.map.R

class TextItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val textView: TextView = itemView.findViewById(R.id.list_item)
    private val separator: View = itemView.findViewById(R.id.separator)

    fun bind(text: String) {
        textView.text = text
    }

    fun hideSeparator(hide: Boolean) {
        if (hide) {
            separator.visibility = View.GONE
        } else {
            separator.visibility = View.VISIBLE
        }
    }
}
