package com.example.myfamily

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_member.view.*
import java.lang.reflect.Member

class MemberAdapter(private val listmember: List<MemberModel>) :
    RecyclerView.Adapter<MemberAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_member, parent, false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.userName.text = listmember[position].name
        holder.userAddress.text = listmember[position].address
        holder.batteryPercent.text = listmember[position].batteryPercent
        holder.userDistance.text = listmember[position].distance
    }

    override fun getItemCount(): Int {
        return listmember.size
    }

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName = itemView.user_name
        val userAddress = itemView.user_address
        val batteryPercent = itemView.batter_percent
        val userDistance = itemView.distance_value

    }
}