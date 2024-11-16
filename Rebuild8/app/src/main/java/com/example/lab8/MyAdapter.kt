package com.example.Rebuild8

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(
    private val data: ArrayList<Contact>
) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val tvName: TextView = v.findViewById(R.id.tvName)
        private val tvPhone: TextView = v.findViewById(R.id.tvPhone)
        private val imgDelete: ImageView = v.findViewById(R.id.imgDelete)

        fun bind(
            item: Contact,
            position: Int,
            onDeleteClick: (Contact, Int) -> Unit
        ) {
            tvName.text = item.name
            tvPhone.text = item.phone

            imgDelete.setOnClickListener {
                onDeleteClick(item, position)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_row, viewGroup, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]

        holder.bind(item, position) { contact, pos ->
            try {
                if (pos in 0 until data.size) {
                    data.removeAt(pos)
                    notifyItemRemoved(pos)
                    notifyItemRangeChanged(pos, data.size)
                }
            } catch (e: Exception) {
                // 如果需要，這裡可以加入錯誤處理邏輯
                notifyDataSetChanged()
            }
        }
    }

    fun addItem(contact: Contact) {
        data.add(contact)
        notifyItemInserted(data.size - 1)
    }

    fun removeItem(position: Int) {
        if (position in 0 until data.size) {
            data.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, data.size)
        }
    }
}