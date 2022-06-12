package com.traveloka.hotelranking.view.ui.maps

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.transform.CircleCropTransformation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnSuccessListener
import com.traveloka.hotelranking.R
import com.traveloka.hotelranking.databinding.ActivityMapsBinding
import com.traveloka.hotelranking.model.UserModel
import com.traveloka.hotelranking.view.ui.main.MainActivity
import com.traveloka.hotelranking.view.utils.LocationUtils
import com.traveloka.hotelranking.view.utils.openActivity
import com.traveloka.hotelranking.view.utils.showToast
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private val binding by lazy { ActivityMapsBinding.inflate(layoutInflater) }
    lateinit var maps: GoogleMap
    var latLngArrayList = ArrayList<LatLng>()
    var locationNameArraylist = ArrayList<String>()
    private val viewModel : MapsViewModel by viewModel()
    private lateinit var userModel: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupActionBar()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.maps) as SupportMapFragment
        mapFragment.getMapAsync(this)

        subscribeToLiveData()
        initView()

    }

    private fun initView() {
        // TODO "Not yet implemented"
    }

    private fun subscribeToLiveData() {
       viewModel.dataRequestList.observe(this){ data ->
           data.forEachIndexed { index, hotel ->
               val latLng = LatLng(hotel.lat.toDouble(), hotel.lon.toDouble())
               latLngArrayList.add(latLng)
               locationNameArraylist.add(hotel.name)
               lifecycleScope.launchWhenCreated {
                   maps.addMarker(
                       MarkerOptions()
                           .position(latLng)
                           .title(locationNameArraylist[index])
                           .snippet("Lat : ${latLng.latitude} Long : ${latLng.longitude}")
                           .icon(
                               BitmapDescriptorFactory.fromBitmap(
                                   resizesPhotos(
                                       getBitmapFromURL(
                                           hotel.image
                                       )
                                   )
                               )
                           )
                   )
               }
           }
       }
        viewModel.isErrorRequestList.observe(this){ message ->
            showToast(message)
        }

        viewModel.observeGetUser().observe(this){ data ->
            this.userModel = data
            if (data.checkLogin){
                viewModel.requestHotelList(data.accessToken)
            }else{
                openActivity(MainActivity::class.java)
            }
        }


    }

    override fun onMapReady(googleMaps: GoogleMap) {
        maps = googleMaps
        maps.uiSettings.isZoomControlsEnabled = true
        maps.uiSettings.isIndoorLevelPickerEnabled = true
        maps.uiSettings.isCompassEnabled = true
        maps.uiSettings.isMapToolbarEnabled = true



        getMyLocation()

    }

    private fun setupActionBar() {
        supportActionBar?.elevation = 0F
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.title = getString(R.string.maps)
    }


    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            maps.isMyLocationEnabled = true
            LocationUtils().getCurrentLocation(this, object : OnSuccessListener<Location?>{
                override fun onSuccess(location: Location?) {
                    if (location == null){
                        return
                    }else{
                        val geocoder = Geocoder(this@MapsActivity, Locale.getDefault())
                        val addresses: List<Address> = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        if (addresses.isNotEmpty()){
                            val currentAddress : Address? = if (addresses.isEmpty()) null else addresses[0]
//                        binding.tvNearYou.setText(bestMatch?.locality)
                            val currentLocation = LatLng(location.latitude, location.longitude)
                            maps.addMarker(
                                MarkerOptions()
                                    .position(currentLocation)
                                    .title(currentAddress?.locality)
                                    .icon(BitmapDescriptorFactory.fromBitmap(
                                        resizesPhotos(
                                            getBitmap(R.drawable.ic_person_pin)!!
                                        )
                                    )))
                            maps.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))
                        }
                    }
                }
            })
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
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

    private suspend fun getBitmapFromURL(value: String): Bitmap {
        val loading = ImageLoader(this)
        val request = ImageRequest.Builder(this)
            .data(value)
            .transformations(CircleCropTransformation())
            .build()

        val result = (loading.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }

    fun resizesPhotos(bitmap: Bitmap): Bitmap {
        val w = bitmap.width
        val h = bitmap.height
        val aspRat = w / h
        val W = 80
        val H = W * aspRat
        val b = Bitmap.createScaledBitmap(bitmap, W, H, false)
        return b
    }

    override fun onResume() {
        super.onResume()
        viewModel.requestHotelList(userModel.accessToken)
    }

    fun getBitmap(@DrawableRes resId: Int): Bitmap? {
        val drawable = ResourcesCompat.getDrawable(resources, resId, null)
        val canvas = Canvas()
        val bitmap = drawable?.let {
            Bitmap.createBitmap(
                it.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
        }
        canvas.setBitmap(bitmap)
        drawable?.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable?.draw(canvas)
        return bitmap
    }

}