package com.traveloka.hotelranking.view.ui.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.traveloka.hotelranking.R
import com.traveloka.hotelranking.databinding.ItemRoomBinding
import com.traveloka.hotelranking.model.dummy.RoomModel
import com.traveloka.hotelranking.view.utils.ItemClickListener
import com.traveloka.hotelranking.view.utils.invisible
import com.traveloka.hotelranking.view.utils.loadImageDrawable

class RoomAdapter(val context: Context) :
    ListAdapter<RoomModel, RoomAdapter.RoomViewHolder>(RoomDiffUtils) {

    var listRoom = mutableListOf<RoomModel>()
    private lateinit var listener: ItemClickListener<RoomModel>

    fun setItemClickListener(itemClickListener: ItemClickListener<RoomModel>) {
        this.listener = itemClickListener
    }

    fun setItemListRoom(list: MutableList<RoomModel>) {
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
        fun bindData(data: RoomModel) {
            binding.run {
                imHotel.loadImageDrawable(data.image)
                tvKindOfRooms.text = data.bedType

                val guestText = "${data.guest} Guest(s) Per Room"
                tvGuest.text = guestText

                val bedText = "${data.bedNumber} ${data.bedType}"
                tvBed.text = bedText

                when (data.breakfast) {
                    true -> tvFood.text = "Included"
                    else -> tvFood.text = "Not Included"
                }

                when (data.wifi) {
                    true -> tvWifi.text = "Free Wifi"
                    else -> tvWifi.text = "Not Included"
                }

                when (data.discount) {
                    true -> {
                        tvRoomDiscount.text = itemView.resources.getString(
                            R.string.room_price_before_discount,
                            data.price
                        )
                        tvRoomPrice.text = data.discountPrice
                    }
                    else -> {
                        tvRoomDiscount.invisible()
                        tvRoomPrice.text = data.price
                    }
                }
            }
            itemView.setOnClickListener {
                listener.onClick(data)
            }
        }
    }

    object RoomDiffUtils : DiffUtil.ItemCallback<RoomModel>() {
        override fun areItemsTheSame(
            oldItem: RoomModel,
            newItem: RoomModel
        ): Boolean {
            return oldItem.type == newItem.type
        }

        override fun areContentsTheSame(
            oldItem: RoomModel,
            newItem: RoomModel
        ): Boolean {
            return oldItem == newItem
        }

    }

}