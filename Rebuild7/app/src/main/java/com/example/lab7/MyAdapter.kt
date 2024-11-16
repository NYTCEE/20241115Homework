package com.example.rebuild7

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class MyAdapter(
    context: Context,
    private val data: List<Item>,
    private val layout: Int
) : ArrayAdapter<Item>(context, layout, data) {

    private val inflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val holder: ViewHolder
        val view: View

        if (convertView == null) {
            view = inflater.inflate(layout, parent, false)
            holder = ViewHolder(
                imgPhoto = view.findViewById(R.id.imgPhoto),
                tvMsg = view.findViewById(R.id.tvMsg)
            )
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        val item = getItem(position) ?: return view

        // 使用字串插值直接設定訊息
        holder.imgPhoto.setImageResource(item.photo)
        holder.tvMsg.text = if (layout == R.layout.adapter_vertical) {
            item.name
        } else {
            "${item.name}: ${item.price}元"
        }

        return view
    }

    // ViewHolder 類別，保存視圖中的元件
    private data class ViewHolder(
        val imgPhoto: ImageView,
        val tvMsg: TextView
    )
}
