package com.traveloka.hotelranking.view.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.traveloka.hotelranking.R
import com.traveloka.hotelranking.model.OnBoarding

class OnBoardingAdapter(private val context: Context) : PagerAdapter() {

    private var listOnBoarding = mutableListOf<OnBoarding>()

    fun setBoardingList(listOnBoarding : MutableList<OnBoarding>){
        this.listOnBoarding = listOnBoarding
        notifyDataSetChanged()
    }

    override fun getCount() = listOnBoarding.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater : LayoutInflater = container.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var view = View(context)
        if (inflater !=null){
            val data : OnBoarding = listOnBoarding[position]
            view = inflater.inflate(R.layout.item_on_boarding, null)
            val mBoardingIcon = view.findViewById<ImageView>(R.id.mBoardingIcon)
            val mBoardingTitle = view.findViewById<TextView>(R.id.mBoardingTitle)
            val mBoardingDescription = view.findViewById<TextView>(R.id.mBoardingDescription)
            mBoardingIcon.setImageResource(data.image)
            mBoardingTitle.text = data.title
            mBoardingDescription.text = data.des
        }
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }
}