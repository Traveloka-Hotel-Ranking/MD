package com.traveloka.hotelranking.view.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.traveloka.hotelranking.R
import com.traveloka.hotelranking.data.remote.response.HotelItem
import com.traveloka.hotelranking.databinding.ItemHotelBinding
import com.traveloka.hotelranking.model.dummy.HomeModel
import com.traveloka.hotelranking.view.utils.*

class HomeAdapter(val context: Context) :
    ListAdapter<HomeModel, HomeAdapter.HomeViewHolder>(HomeDiffUtils) {

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

    object HomeDiffUtils : DiffUtil.ItemCallback<HomeModel>() {
        override fun areItemsTheSame(
            oldItem: HomeModel,
            newItem: HomeModel
        ): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(
            oldItem: HomeModel,
            newItem: HomeModel
        ): Boolean {
            return oldItem == newItem
        }

    }

}