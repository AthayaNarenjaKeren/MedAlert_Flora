package com.example.medalert_flora.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.medalert_flora.R
import com.example.medalert_flora.model.Healthcare
import com.example.medalert_flora.openRoute

// Custom adapter untuk ListView (BaseAdapter + ViewHolder)
class HealthcareAdapter(
    private val context: Context,
    private var items: List<Healthcare>
) : BaseAdapter() {

    private class ViewHolder(view: View) {
        val name: TextView = view.findViewById(R.id.tvName)
        val distance: TextView = view.findViewById(R.id.tvDistance)
        val hours: TextView = view.findViewById(R.id.tvHours)
        val route: TextView = view.findViewById(R.id.btnRoute)
    }

    override fun getCount() = items.size
    override fun getItem(position: Int) = items[position]
    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_healthcare, parent, false)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }
        val item = items[position]
        holder.name.text = item.name
        holder.distance.text = item.distance
        holder.hours.text = " · ${item.hours}"
        holder.route.setOnClickListener { context.openRoute(item.name) }
        return view
    }

    fun updateData(newItems: List<Healthcare>) {
        items = newItems
        notifyDataSetChanged()
    }
}
