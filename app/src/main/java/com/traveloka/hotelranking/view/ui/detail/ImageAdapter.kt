package com.traveloka.hotelranking.view.ui.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.traveloka.hotelranking.databinding.ItemImgHotelBinding
import com.traveloka.hotelranking.model.dummy.ImageModel
import com.traveloka.hotelranking.view.utils.ItemClickListener
import com.traveloka.hotelranking.view.utils.loadImageDrawable

class ImageAdapter(val context: Context) :
    ListAdapter<ImageModel, ImageAdapter.ImageViewHolder>(ImageDiffUtils) {

    var listRoom = mutableListOf<ImageModel>()
    private lateinit var listener: ItemClickListener<ImageModel>

    fun setItemClickListener(itemClickListener: ItemClickListener<ImageModel>) {
        this.listener = itemClickListener
    }

    fun setItemListImage(list: MutableList<ImageModel>) {
        listRoom.clear()
        listRoom.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            ItemImgHotelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bindData(listRoom[position])
    }

    override fun getItemCount(): Int = listRoom.size

    inner class ImageViewHolder(val binding: ItemImgHotelBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: ImageModel) {
            binding.run {
                image.loadImageDrawable(data.image)
            }
            itemView.setOnClickListener {
                listener.onClick(data)
            }
        }
    }

    object ImageDiffUtils : DiffUtil.ItemCallback<ImageModel>() {
        override fun areItemsTheSame(
            oldItem: ImageModel,
            newItem: ImageModel
        ): Boolean {
            return oldItem.image == newItem.image
        }

        override fun areContentsTheSame(
            oldItem: ImageModel,
            newItem: ImageModel
        ): Boolean {
            return oldItem == newItem
        }

    }

}