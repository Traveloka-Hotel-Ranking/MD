package com.traveloka.hotelranking.view.ui.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.traveloka.hotelranking.R
import com.traveloka.hotelranking.data.remote.response.Facilities
import com.traveloka.hotelranking.databinding.ItemRoomBinding
import com.traveloka.hotelranking.view.utils.ItemClickListener
import com.traveloka.hotelranking.view.utils.invisible
import com.traveloka.hotelranking.view.utils.loadImageDrawable

class RoomAdapter(val context: Context) :
    ListAdapter<Facilities, RoomAdapter.RoomViewHolder>(RoomDiffUtils) {

    var listRoom = mutableListOf<Facilities>()

    fun setItemListRoom(list: MutableList<Facilities>) {
        listRoom.clear()
        listRoom.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        return RoomViewHolder(
            ItemRoomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        holder.bindData(listRoom[position])
    }

    override fun getItemCount(): Int = listRoom.size

    inner class RoomViewHolder(val binding: ItemRoomBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: Facilities) {
            binding.run {
                imHotel.loadImageDrawable(R.drawable.img_welcome3)

                val king = data.king_bed
                val med = data.medium_bed
                val std = data.standard_bed
                var bed = ""

                if (king > 0) {
                    bed += itemView.resources.getString(R.string.king_bed, king.toString())
                }

                if (med > 0) {
                    bed += if (bed.isNotBlank()) ", " + itemView.resources.getString(R.string.medium_bed, med.toString())
                    else itemView.resources.getString(R.string.medium_bed, med.toString())
                }

                if (std > 0) {
                    bed += if (bed.isNotBlank()) ", " + itemView.resources.getString(R.string.standard_bed, std.toString())
                    else itemView.resources.getString(R.string.standard_bed, std.toString())
                }
                tvBed.text = bed

                val guestText = "${data.capacity} Guest(s) Per Room"
                tvGuest.text = guestText

                var food = ""
                if (data.breakfast) {
                    food += itemView.resources.getString(R.string.breakfast)
                }

                if (data.lunch) {
                    food += if (food.isNotBlank()) ", " + itemView.resources.getString(R.string.lunch)
                    else itemView.resources.getString(R.string.lunch)
                }

                if (data.dinner) {
                    food += if (food.isNotBlank()) ", " + itemView.resources.getString(R.string.dinner)
                    else itemView.resources.getString(R.string.dinner)
                }
                tvFood.text = food.ifBlank { "Not Included" }

                if (data.wifi) {
                    tvWifi.text = "Free Wifi"
                } else {
                    "Not Included"
                }

                // Referring to data from ML Model
                tvRoomPrice.isVisible = false
                selectButton.isVisible = false

            }
        }
    }

    object RoomDiffUtils : DiffUtil.ItemCallback<Facilities>() {
        override fun areItemsTheSame(
            oldItem: Facilities,
            newItem: Facilities
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Facilities,
            newItem: Facilities
        ): Boolean {
            return oldItem == newItem
        }

    }

}