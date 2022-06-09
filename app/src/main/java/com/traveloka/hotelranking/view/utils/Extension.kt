package com.traveloka.hotelranking.view.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.traveloka.hotelranking.R

/* Toast */
fun AppCompatActivity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.showToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

/* Hide Keyboard */
fun Activity.hideKeyboard() {
    val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = currentFocus
    if (view != null) {
        inputManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}

/* Open Activity from Activity or Fragment */
fun <T> Fragment.openActivity(destination: Class<T>) {
    val intent = Intent(requireContext(), destination)
    startActivity(intent)
}

fun <T> Activity.openActivity(destination: Class<T>) {
    val intent = Intent(this, destination)
    startActivity(intent)
}

/*Add for close activity*/
fun Activity.closeActivity(){
    hideKeyboard()
    finish()
}

fun ImageView.loadImage(data : String){
    Glide.with(this)
        .load(data)
        .error(R.drawable.sample_image)
        .into(this)
}

fun ImageView.loadImageDrawable(data : Int){
    this.setImageResource(data)
}

fun Activity.concat(count : Int, value: String): String {
    return "$count $value"
}

fun TextView.concat(count : Int, value: String) {
    this.text = "$count $value"
}

fun View.visible() {
    if (this.visibility == View.GONE || this.visibility == View.INVISIBLE) this.visibility =
        View.VISIBLE
}

fun View.gone() {
    if (this.visibility == View.VISIBLE) this.visibility = View.GONE
}

fun View.invisible() {
    if (this.visibility == View.VISIBLE) this.visibility = View.INVISIBLE
}

