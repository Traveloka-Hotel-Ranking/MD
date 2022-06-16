package com.traveloka.hotelranking.view.ui.home

import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.gson.Gson
import com.traveloka.hotelranking.R
import com.traveloka.hotelranking.data.remote.response.HotelItem
import com.traveloka.hotelranking.databinding.ActivityHomeBinding
import com.traveloka.hotelranking.model.Prediction
import com.traveloka.hotelranking.model.UserModel
import com.traveloka.hotelranking.model.param.HomeMLParam
import com.traveloka.hotelranking.model.param.Instance
import com.traveloka.hotelranking.view.ui.detail.DetailHotelActivity
import com.traveloka.hotelranking.view.ui.home.adapter.HomeAdapter
import com.traveloka.hotelranking.view.ui.home.paging.HotelPagingAdapter
import com.traveloka.hotelranking.view.ui.home.paging.LoadingStateAdapter
import com.traveloka.hotelranking.view.ui.main.MainActivity
import com.traveloka.hotelranking.view.ui.maps.MapsActivity
import com.traveloka.hotelranking.view.ui.profile.ProfileActivity
import com.traveloka.hotelranking.view.utils.*
import com.traveloka.hotelranking.view.utils.constants.MESSAGE_BACK_PRESS
import com.traveloka.hotelranking.view.utils.constants.RAW_DATE_PATTERN_NEW
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.math.log

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val adapter by lazy { HomeAdapter(this) }
    private val viewModel: HomeViewModel by viewModel()
    private var countNight = 0
    private var isExit = false
    private lateinit var userModel: UserModel
    private var dataEmpty = false
    private val adapterPaging by lazy { HotelPagingAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()

        initView()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        viewModel.dataRequestListName.observe(this) { data ->
            if (data.isNotEmpty()) {
                dataEmpty = false
                binding.layoutMessageIllustration.gone()
                handledAdapterWithoutPaging()
                adapter.setItemListHotel(data.toMutableList())
            } else {
                dataEmpty = true
                binding.layoutMessageIllustration.visible()
            }
        }

        viewModel.dataRequestListLocation.observe(this) { data ->
            if (data.isNotEmpty()) {
                dataEmpty = false
                binding.layoutMessageIllustration.gone()
                handledAdapterWithoutPaging()
                adapter.setItemListHotel(data.toMutableList())
            } else {
                dataEmpty = true
                binding.layoutMessageIllustration.visible()
            }
        }

        viewModel.isErrorRequestListLocation.observe(this) { message ->
            showToast(message!!)
        }

        viewModel.isErrorRequestListName.observe(this) { message ->
            showToast(message!!)
        }

        viewModel.isLoadingRequestListName.observe(this) { isLoading ->
            handleShimmer(isLoading)
        }

        viewModel.isLoadingRequestListLocation.observe(this) { isLoading ->
            handleShimmer(isLoading)
        }

        viewModel.getUser().observe(this) { data ->
            this.userModel = data

            if (data.checkLogin) {
                if (data.accessToken.isNotBlank()) {
                    binding.tvOther.alpha = 1F
                    lifecycleScope.launch {
                        viewModel.requestHotelPaging(data.accessToken, "")
                            .observe(this@HomeActivity) { data ->
                                adapterPaging.submitData(lifecycle, data)
                            }
                    }
                }

                binding.mbSearch.setOnClickListener {
                    val searchByName = binding.tvHotelName.text.toString().trim()
                    val searchByLoc = binding.tvNearYou.text.toString().trim()

                    if (searchByName != "") {
                        binding.tvOther.alpha = 1F
                        viewModel.requestDataByName(data.accessToken, searchByName)
                    } else {
                        binding.tvOther.alpha = 1F
                        viewModel.requestDataByLocation(data.accessToken, searchByLoc)
                    }
                    hideKeyboard()
                }

                val tokennya = "ya29.a0ARrdaM_u8cMEF90vAzHrHGlm8-f0qNfZYjh7E5siHsuzn19NgB_Lyu4MpWVo1wtGl3VC0yUIczpZoTkLvIu5t1mVfUcme0BAXNSmE0F6FxSGqJq-vSInMVY5CYvEZv5dM9mXrbFFpXJzWpzkv0JsLavaVVWD0WtVJD4qmrS5Jln0g_vQPF-8Ap5JPM6aWb_VAi0wYEo4R5yJ6t-ZZEWhEsv6QGu5zEbYWco-qouC84fLWKmT5EuZ84QjIKzzM06BU3krHm8"

                val listData = listOf(
                    Instance(data.id)
                )
                val dataMl = HomeMLParam(listData)
                viewModel.requestDataListML("Bearer $tokennya", dataMl)
            } else {
                openActivity(MainActivity::class.java)
                finish()
            }
        }
        viewModel.dataRequestListML.observe(this) { data ->
            setTextMLList(data.predictions, userModel)
        }

    }

    private fun initView() {
        adapter.setItemClickListener(object : ItemClickListener<HotelItem> {
            override fun onClick(data: HotelItem) {
                openActivityWithData(DetailHotelActivity::class.java, data)
            }
        })

        adapterPaging.setItemClickListener(object : ItemClickListener<HotelItem>{
            override fun onClick(data: HotelItem) {
                showToast(data.name)
            }

        })
        binding.run {
            rvHome.layoutManager = LinearLayoutManager(this@HomeActivity)
            handledAdapterPaging()
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

            mbMaps.setOnClickListener {
                hideKeyboard()
                openActivity(MapsActivity::class.java)
            }

            swipeRefresh.setOnRefreshListener {
                binding.tvOther.alpha = 1F
                handledAdapterPaging()
                adapterPaging.refresh()
            }
        }

        getMyLocation()

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
                binding.tvTittleRecommended.gone()
                binding.tvToolbar.gone()
                binding.tvOther.gone()
                if (!isShimmerStarted) startShimmer()
                binding.swipeRefresh.isRefreshing = false
                visible()
            } else {
                if (dataEmpty) {
                    binding.rvHome.gone()
                } else {
                    binding.rvHome.visible()
                }
                binding.tvToolbar.visible()
                binding.tvTittleRecommended.visible()
                binding.tvOther.visible()
                binding.swipeRefresh.isRefreshing = false
                gone()
            }
        }
    }

    override fun onBackPressed() {
        if (isExit) {
            super.onBackPressed()
            finishAffinity()
        } else {
            isExit = true
            showToast(MESSAGE_BACK_PRESS)
        }
        Handler(Looper.getMainLooper()).postDelayed({ isExit = false }, 2000)
    }

    override fun onResume() {
        super.onResume()
        getMyLocation()
    }

    private fun getMyLocation() {
        LocationUtils().getCurrentLocation(this, object : OnSuccessListener<Location?> {
            override fun onSuccess(location: Location?) {
                if (location == null) {
                    return
                } else {
                    val geocoder = Geocoder(this@HomeActivity, Locale.getDefault())
                    val addresses: List<Address> =
                        geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    if (addresses.isNotEmpty()) {
                        val bestMatch: Address? = if (addresses.isEmpty()) null else addresses[0]
                        binding.tvNearYou.setText(bestMatch?.locality)
                    } else {
                        binding.tvNearYou.setText(getString(R.string.near_you))
                    }
                }
            }
        })
    }

    private fun setTextMLList(statusList: List<Prediction>?, userModel: UserModel) {
        val array = mutableListOf<String>()
        val inflater = LayoutInflater.from(binding.chipGroup.context)
        val layoutRes = R.layout.view_chip_ml
        val parent = binding.chipGroup
        var i = 1
        statusList?.forEach { data ->
            data.output2.forEachIndexed { _, s ->
                array.addAll(listOf(s))
            }
        }
        array.forEach { hotelName ->
            val chip = (inflater.inflate(layoutRes, parent, false) as Chip)
            chip.id = i
            chip.text = hotelName
            chip.isClickable = true
            chip.setOnClickListener {
                hideKeyboard()
                viewModel.requestDataByName(userModel.accessToken, hotelName.trim())
                binding.tvOther.alpha = 0F
            }
            binding.chipGroup.addView(chip)
            i++
        }
    }

    private fun handledAdapterPaging() {
        binding.rvHome.adapter = adapterPaging
        binding.rvHome.adapter = adapterPaging.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapterPaging.retry()
            }
        )
        adapterPaging.addLoadStateListener { isLoadState ->
            handleShimmer(isLoadState.source.refresh is LoadState.Loading)
        }
    }

    private fun handledAdapterWithoutPaging() {
        binding.rvHome.adapter = adapter
    }
}