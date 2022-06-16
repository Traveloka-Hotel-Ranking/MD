package com.traveloka.hotelranking.view.ui.home

import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.traveloka.hotelranking.R
import com.traveloka.hotelranking.data.remote.response.HotelItem
import com.traveloka.hotelranking.databinding.ActivityHomeBinding
import com.traveloka.hotelranking.model.Prediction
import com.traveloka.hotelranking.model.UserModel
import com.traveloka.hotelranking.model.param.HomeMLParam
import com.traveloka.hotelranking.model.param.Instance
import com.traveloka.hotelranking.view.ui.detail.DetailHotelActivity
import com.traveloka.hotelranking.view.ui.home.adapter.HomeAdapter
import com.traveloka.hotelranking.view.ui.main.MainActivity
import com.traveloka.hotelranking.view.ui.maps.MapsActivity
import com.traveloka.hotelranking.view.ui.profile.ProfileActivity
import com.traveloka.hotelranking.view.utils.*
import com.traveloka.hotelranking.view.utils.constants.MESSAGE_BACK_PRESS
import com.traveloka.hotelranking.view.utils.constants.RAW_DATE_PATTERN_NEW
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val adapter by lazy { HomeAdapter(this) }
    private val viewModel: HomeViewModel by viewModel()
    private var countNight = 0
    private var isExit = false
    private lateinit var userModel: UserModel
    private var dataEmpty = false

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
            if (data.isNotEmpty()) {
                dataEmpty = false
                binding.layoutMessageIllustration.gone()
                adapter.setItemListHotel(data.toMutableList())
            } else {
                dataEmpty = true
                binding.layoutMessageIllustration.visible()
            }
        }

        viewModel.isErrorRequestList.observe(this) { message ->
            showToast(message!!)
        }

        viewModel.isLoadingRequestList.observe(this) { isLoading ->
            handleShimmer(isLoading)
        }
        viewModel.getUser().observe(this) { data ->
            this.userModel = data

            if (data.checkLogin) {
                if (data.accessToken.isNotBlank()) {
                    viewModel.requestDataList(data.accessToken)
                }

                binding.mbSearch.setOnClickListener {
                    val searchByName = binding.tvHotelName.text.toString().trim()
                    val searchByLoc = binding.tvNearYou.text.toString().trim()

                    if (searchByName != "") {
                        viewModel.requestDataByName(data.accessToken, searchByName)
                    } else {
                        viewModel.requestDataByLocation(data.accessToken, searchByLoc)
                    }
                    hideKeyboard()
                }

                val tokennya =
                    "ya29.A0ARrdaM_rqMCvI0yiaE3_IxPqtuqAQ4i_b05i6rEc8yzq_Vr7CrYd3RZ4lr-m4mozIzo-Bg-my-0gpMopMqsgq85FxlpIGqWJ3JRdLAQsicYSxCM6CJ0QpdkYuC4nLnLL_daitk7_hlkKjY5otw8lsFwev-bMolIJnpPZU56o4LFMZQyzb2-9UtL7uAeJpzOdqnMAd1eFzb_eccirmq2Olub9ga1iZogEWIyKYW87ppKR6CgAwiTBSTwXFZqPqru98McngewYUNnWUtBVEFTQVRBU0ZRRl91NjFWOV9fZkllRmk5NzNBUTFWSEZTM0hsQQ0270"
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
        viewModel.isErrorRequestListML.observe(this) {

        }
        viewModel.isLoadingRequestListML.observe(this) {

        }
    }

    private fun initView() {
        adapter.setItemClickListener(object : ItemClickListener<HotelItem> {
            override fun onClick(data: HotelItem) {
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

            mbMaps.setOnClickListener {
                hideKeyboard()
                openActivity(MapsActivity::class.java)
            }

            swipeRefresh.setOnRefreshListener {
                viewModel.requestDataList(userModel.accessToken)
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
            data.output2.forEachIndexed { index, s ->
                array.addAll(listOf(s))
            }
        }
        array.forEach { hotelName ->
            val chip = (inflater.inflate(layoutRes, parent, false) as Chip)
            chip.id = i
            chip.text = hotelName
            chip.isCheckable = true
            chip.setOnCheckedChangeListener { _, b ->
                if (b) {
                    viewModel.requestDataByName(userModel.accessToken, hotelName.trim())
                }
            }
            binding.chipGroup.addView(chip)
            i++
        }
    }
}