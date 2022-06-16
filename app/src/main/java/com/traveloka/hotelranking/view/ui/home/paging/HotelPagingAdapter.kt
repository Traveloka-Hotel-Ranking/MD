package com.traveloka.hotelranking.view.ui.home.paging

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.traveloka.hotelranking.data.remote.response.HotelItem
import com.traveloka.hotelranking.databinding.ItemHotelBinding
import com.traveloka.hotelranking.view.utils.ItemClickListener
import com.traveloka.hotelranking.view.utils.concatRupiah
import com.traveloka.hotelranking.view.utils.loadImage

class HotelPagingAdapter : PagingDataAdapter<HotelItem, HotelPagingAdapter.HotelViewHolder>(HotelDiffCallBack()) {

    private lateinit var listener: ItemClickListener<HotelItem>


    fun setItemClickListener(itemClickListener: ItemClickListener<HotelItem>) {
        this.listener = itemClickListener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelViewHolder {
       return HotelViewHolder(ItemHotelBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: HotelViewHolder, position: Int) {
        getItem(position)?.let { holder.bindData(it) }
    }

    inner class HotelViewHolder(val binding : ItemHotelBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(data : HotelItem){
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

    class HotelDiffCallBack : DiffUtil.ItemCallback<HotelItem>(){
        override fun areItemsTheSame(oldItem: HotelItem, newItem: HotelItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: HotelItem, newItem: HotelItem): Boolean {
           return oldItem.id == newItem.id
        }

    }


}