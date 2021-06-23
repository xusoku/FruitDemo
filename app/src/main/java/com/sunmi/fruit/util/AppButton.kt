package com.sunmi.fruit.util

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.Button
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.sunmi.fruit.R

class AppButton : androidx.appcompat.widget.AppCompatButton {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        val typeface = ResourcesCompat.getFont(context, R.font.heavy)
        setTypeface(typeface)
    }
}