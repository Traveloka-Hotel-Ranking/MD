package com.traveloka.hotelranking.view.ui.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.traveloka.hotelranking.R
import com.traveloka.hotelranking.databinding.ActivityHomeBinding
import com.traveloka.hotelranking.model.dummy.HomeModel
import com.traveloka.hotelranking.view.ui.detail.DetailHotelActivity
import com.traveloka.hotelranking.view.ui.home.adapter.HomeAdapter
import com.traveloka.hotelranking.view.ui.profile.ProfileActivity
import com.traveloka.hotelranking.view.utils.*
import com.traveloka.hotelranking.view.utils.constants.RAW_DATE_PATTERN_NEW
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val adapter by lazy { HomeAdapter(this) }
    private val viewModel: HomeViewModel by viewModel()
    var countNight = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()

        initView()
        subscribeToLiveData()

    }

    private fun subscribeToLiveData() {
        viewModel.dataRequestList.observe(this) { data ->
            adapter.setItemListHotel(data.toMutableList())
        }

        viewModel.isErrorRequestList.observe(this) { message ->
            showToast(message!!)
        }

        viewModel.isLoadingRequestList.observe(this) { isLoading ->
            handleShimmer(isLoading)
        }
    }

    private fun initView() {
        adapter.setItemClickListener(object : ItemClickListener<HomeModel> {
            override fun onClick(data: HomeModel) {
                openActivityWithData(DetailHotelActivity::class.java, data)
            }
        })
        binding.run {
            rvHome.adapter = adapter
            tvDate.isFocusable = false
            etCountNight.isFocusable = false
            tvDate.setOnClickListener {
                showCalender(object : ItemClickListener<String> {
                    override fun onClick(data: String) {
                        tvDate.setText(data)
                    }
                })
            }

            imMinus.setOnClickListener {

                if (countNight > 1) {
                    countNight -= 1
                    etCountNight.setText(concat(countNight, getString(R.string.text_night)))
                }
            }

            imPlus.setOnClickListener {
                countNight += 1

                etCountNight.setText(concat(countNight, getString(R.string.text_night)))
            }
        }

        viewModel.requestDataList()
    }

    private fun setupActionBar() {
        supportActionBar?.elevation = 0F
        this.title = getString(R.string.accommodations)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_profile) {
            openActivity(ProfileActivity::class.java)
        }
        return super.onOptionsItemSelected(item)

    }

    private fun showCalender(listener: ItemClickListener<String>) {
        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now())

        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(constraintsBuilder.build())
                .build()

        datePicker.addOnPositiveButtonClickListener {
            val date = Calendar.getInstance(TimeZone.getTimeZone("id"))
            date.timeInMillis = it
            val newDate = DateUtils.formatDateToString(date.time, RAW_DATE_PATTERN_NEW)
            listener.onClick(newDate.toString())
        }
        datePicker.addOnCancelListener { dialog ->
            dialog.dismiss()
        }
        datePicker.show(supportFragmentManager, "")
    }

    private fun handleShimmer(isShown: Boolean) {
        binding.shimmerListHotel.apply {
            if (isShown) {
                binding.rvHome.gone()
                if (!isShimmerStarted) startShimmer()
                visible()
            } else {
                binding.rvHome.visible()
                gone()
            }
        }
    }
}