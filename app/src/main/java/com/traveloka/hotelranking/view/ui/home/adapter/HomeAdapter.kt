package com.traveloka.hotelranking.view.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.traveloka.hotelranking.data.remote.response.HotelItem
import com.traveloka.hotelranking.databinding.ItemHotelBinding
import com.traveloka.hotelranking.view.utils.ItemClickListener
import com.traveloka.hotelranking.view.utils.concatRupiah
import com.traveloka.hotelranking.view.utils.loadImage

class HomeAdapter(val context: Context) :
    ListAdapter<HotelItem, HomeAdapter.HomeViewHolder>(HomeDiffUtils) {

    var listHotel = mutableListOf<HotelItem>()
    private lateinit var listener: ItemClickListener<HotelItem>

    fun setItemClickListener(itemClickListener: ItemClickListener<HotelItem>) {
        this.listener = itemClickListener
    }

    fun setItemListHotel(list: MutableList<HotelItem>) {
        listHotel.clear()
        listHotel.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            ItemHotelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bindData(listHotel[position])
    }

    override fun getItemCount(): Int = listHotel.size

    inner class HomeViewHolder(val binding: ItemHotelBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: HotelItem) {
            binding.run {
                imHotel.loadImage(data.image)
                tvTitle.text = data.name
                rbRatingHotel.numStars = 5
                rbRatingHotel.rating = data.rating.toFloat()
                tvLocationDistance.text = data.location
                tvRatingHotel.text = data.review.toString()
                tvRoomPrice.concatRupiah(data.price)
            }
            itemView.setOnClickListener {
                listener.onClick(data)
            }
        }
    }

    object HomeDiffUtils : DiffUtil.ItemCallback<HotelItem>() {
        override fun areItemsTheSame(
            oldItem: HotelItem,
            newItem: HotelItem
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: HotelItem,
            newItem: HotelItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

    }

}