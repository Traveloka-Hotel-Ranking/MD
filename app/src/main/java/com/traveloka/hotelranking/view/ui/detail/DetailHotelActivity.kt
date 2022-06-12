package com.traveloka.hotelranking.view.ui.detail

import android.Manifest
import android.content.ContentValues
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.traveloka.hotelranking.R
import com.traveloka.hotelranking.data.remote.response.HotelItem
import com.traveloka.hotelranking.databinding.ActivityDetailHotelBinding
import com.traveloka.hotelranking.model.dummy.HomeModel
import com.traveloka.hotelranking.model.dummy.ImageModel
import com.traveloka.hotelranking.model.dummy.RoomModel
import com.traveloka.hotelranking.view.utils.ItemClickListener
import com.traveloka.hotelranking.view.utils.constants.HOTEL_DATA
import timber.log.Timber
import java.util.concurrent.TimeUnit

class DetailHotelActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityDetailHotelBinding
    private val imageAdapter by lazy { ImageAdapter(this) }
    private val roomAdapter by lazy { RoomAdapter(this) }

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private var boundsBuilder = LatLngBounds.builder()

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    getMyLastLocation()
                }
                else -> {
                    // No location access granted.
                }
            }
        }

    private val resolutionLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            when (result.resultCode) {
                RESULT_OK ->
                    Timber.tag(ContentValues.TAG)
                        .i("onActivityResult: All location settings are satisfied.")
                RESULT_CANCELED ->
                    Toast.makeText(
                        this@DetailHotelActivity,
                        "Anda harus mengaktifkan GPS untuk menggunakan aplikasi ini!",
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHotelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent.getParcelableExtra<HotelItem>(HOTEL_DATA)
        if (intent != null) {
            setupActionBar(intent.name)
//            setImageAdapter(intent.image)
//            setRoomAdapter(intent.room)
            initView(intent)
        }

        val mapView = supportFragmentManager
            .findFragmentById(R.id.map_view) as SupportMapFragment
        mapView.getMapAsync(this@DetailHotelActivity)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun setupActionBar(title: String) {
        supportActionBar?.elevation = 0F
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.title = title

    }

    private fun setImageAdapter(data: List<ImageModel>) {
        imageAdapter.setItemListImage(data.toMutableList())

        imageAdapter.setItemClickListener(object: ItemClickListener<ImageModel> {
            override fun onClick(data: ImageModel) {
                TODO("Not yet implemented")
            }
        })

        binding.rvImgHotel.run {
            adapter = imageAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@DetailHotelActivity, RecyclerView.HORIZONTAL, false)
        }
    }

    private fun setRoomAdapter(data: List<RoomModel>) {
        roomAdapter.setItemListRoom(data.toMutableList())

        roomAdapter.setItemClickListener(object: ItemClickListener<RoomModel> {
            override fun onClick(data: RoomModel) {
                TODO("Not yet implemented")
            }
        })

        binding.rvRoom.run {
            adapter = roomAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@DetailHotelActivity)
        }
    }

    private fun initView(data: HotelItem) {
        binding.run {
            tvTitleHotel.text = data.name
            rbRatingHotel.numStars = 5
            rbRatingHotel.rating = data.rating.toFloat()
            tvLocation.text = data.location
            tvRatingHotel.text = data.rating.toString()
        }
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    showStartMarker(location)
                } else {
                    Toast.makeText(
                        this@DetailHotelActivity,
                        "Location is not found. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun showStartMarker(location: Location) {
        val startLocation = LatLng(location.latitude, location.longitude)
        mMap.addMarker(
            MarkerOptions()
                .position(startLocation)
                .title("You're Here")
        )
        boundsBuilder.include(startLocation)
        val bounds: LatLngBounds = boundsBuilder.build()
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 128))
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        createLocationRequest()

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        mMap.setOnMapLongClickListener { latLng ->
            mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("New Marker")
                    .snippet("Lat: ${latLng.latitude} Long: ${latLng.longitude}")
            )
        }

        mMap.setOnCameraMoveStartedListener { i ->
            if (i == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
                binding.scrollView.setScrollingEnabled(false)
            }
        }
        mMap.setOnCameraIdleListener {
            binding.scrollView.setScrollingEnabled(true)
        }
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(1)
            maxWaitTime = TimeUnit.SECONDS.toMillis(1)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(this)
        client.checkLocationSettings(builder.build())
            .addOnSuccessListener {
                getMyLastLocation()
            }
            .addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    try {
                        resolutionLauncher.launch(
                            IntentSenderRequest.Builder(exception.resolution).build()
                        )
                    } catch (sendEx: IntentSender.SendIntentException) {
                        Toast.makeText(this@DetailHotelActivity, sendEx.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home ->{
                finish()
//                val intent = Intent(this, HomeActivity::class.java)
//                startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this@ProfileActivity).toBundle())
                super.onOptionsItemSelected(item)
            }
            else -> true
        }
    }
}