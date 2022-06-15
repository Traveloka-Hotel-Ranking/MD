package com.traveloka.hotelranking.view.ui.detail

import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.traveloka.hotelranking.databinding.ActivityFullScreenImageBinding
import com.traveloka.hotelranking.view.utils.loadImage


class FullScreenImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFullScreenImageBinding

    var mScaleGestureDetector: ScaleGestureDetector? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullScreenImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        val img = intent.getStringExtra(EXTRA_IMAGE)
        binding.imgHotel.loadImage(img.toString())

        binding.mbClose.setOnClickListener {
            finish()
        }

        mScaleGestureDetector = ScaleGestureDetector(this, ScaleListener(binding.imgHotel))
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mScaleGestureDetector?.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    class ScaleListener internal constructor(private var mImageView: ImageView) :
        SimpleOnScaleGestureListener() {
        private var mScaleFactor = 1.0f

        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            mScaleFactor *= scaleGestureDetector.scaleFactor
            mScaleFactor = 1f.coerceAtLeast(mScaleFactor.coerceAtMost(10.0f))
            mImageView.scaleX = mScaleFactor
            mImageView.scaleY = mScaleFactor
            return true
        }

    }

    private fun setupActionBar() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    companion object {
        const val EXTRA_IMAGE = "extra_image"
    }
}