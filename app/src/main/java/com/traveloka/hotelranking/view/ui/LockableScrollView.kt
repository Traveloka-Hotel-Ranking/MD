package com.traveloka.hotelranking.view.ui

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.MotionEvent
import androidx.core.widget.NestedScrollView

class LockableScrollView : NestedScrollView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var scrollable = true

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return when (ev.action) {
            MotionEvent.ACTION_DOWN -> scrollable && super.onTouchEvent(ev)
            MotionEvent.ACTION_MOVE -> scrollable && super.onTouchEvent(ev)
            MotionEvent.ACTION_UP -> scrollable && super.onTouchEvent(ev)
            else -> super.onTouchEvent(ev)
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return scrollable && super.onInterceptTouchEvent(ev)
    }

    fun setScrollingEnabled(enabled: Boolean) {
        scrollable = enabled
    }

    fun isScrollable(): Boolean {
        return scrollable
    }
}