package com.traveloka.hotelranking.view.ui.detail

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.transform.CircleCropTransformation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.traveloka.hotelranking.R
import com.traveloka.hotelranking.data.remote.response.HotelItem
import com.traveloka.hotelranking.databinding.ActivityDetailHotelBinding
import com.traveloka.hotelranking.view.utils.constants.HOTEL_DATA
import com.traveloka.hotelranking.view.utils.loadImage
import kotlinx.coroutines.launch
import java.util.*


class DetailHotelActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityDetailHotelBinding
    private val roomAdapter by lazy { RoomAdapter(this) }

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var data: HotelItem

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHotelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent.getParcelableExtra<HotelItem>(HOTEL_DATA)
        if (intent != null) {
            data = intent
            setupActionBar(data.name)
            setRoomAdapter(data)
            initView(data)
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

    private fun setRoomAdapter(data: HotelItem) {
        roomAdapter.setItemListRoom(data)

        binding.rvRoom.run {
            adapter = roomAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@DetailHotelActivity, RecyclerView.HORIZONTAL, false)
        }
    }

    private fun initView(data: HotelItem) {
        val geocoder = Geocoder(this@DetailHotelActivity, Locale.getDefault())
        val lat = data.lat.toDouble()
        val lon = data.lon.toDouble()
        val hotelName = data.name
        val mapAddress = geocoder.getFromLocation(lat, lon, 1)
        val addressLine = mapAddress[0].getAddressLine(0)
        binding.run {

            imgHotel.loadImage(data.image)
            tvTitleHotel.text = hotelName
            rbRatingHotel.numStars = 5
            rbRatingHotel.rating = data.rating.toFloat()
            tvLocation.text = data.location
            tvRatingHotel.text = data.rating.toString()

            imgHotel.setOnClickListener {
                val imageIntent = Intent(this@DetailHotelActivity, FullScreenImageActivity::class.java)
                imageIntent.putExtra(FullScreenImageActivity.EXTRA_IMAGE, data.image)
                startActivity(imageIntent)
            }

            address.text = addressLine
            address.paint.isUnderlineText = true
            address.setOnClickListener {
                val gmmIntentUri: Uri = Uri.parse("geo:0,0?q=$hotelName+$addressLine?z=zoom=12.0f")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                if (mapIntent.resolveActivity(packageManager) != null) {
                    startActivity(mapIntent)
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        mMap.setOnCameraMoveStartedListener { i ->
            if (i == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
                binding.scrollView.setScrollingEnabled(false)
            }
        }
        mMap.setOnCameraIdleListener {
            binding.scrollView.setScrollingEnabled(true)
        }

        getMyLocation()
        setMapMarker(data)
    }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun setMapMarker(data: HotelItem) {
        val latLng = LatLng(data.lat.toDouble(), data.lon.toDouble())
        lifecycleScope.launch {
            mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(data.name)
                    .snippet("Lat : ${latLng.latitude} Long : ${latLng.longitude}")
                    .icon(
                        BitmapDescriptorFactory.fromBitmap(
                            resizesPhotos(
                                getBitmapFromURL(
                                    data.image
                                )
                            )
                        )
                    )
            )
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home ->{
                finish()
                super.onOptionsItemSelected(item)
            }
            else -> true
        }
    }

    private fun resizesPhotos(bitmap: Bitmap): Bitmap {
        val w = bitmap.width
        val h = bitmap.height
        val aspRat = w / h
        val W = 80
        val H = W * aspRat
        val b = Bitmap.createScaledBitmap(bitmap, W, H, false)
        return b
    }

    private suspend fun getBitmapFromURL(value: String): Bitmap {
        val loading = ImageLoader(this)
        val request = ImageRequest.Builder(this)
            .data(value)
            .transformations(CircleCropTransformation())
            .build()

        val result = (loading.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }
}