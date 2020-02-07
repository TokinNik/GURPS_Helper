package com.example.testapp.custom_view.outline_corner

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.example.testapp.R

class CustomOutlineCornerLinearLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): LinearLayout(context, attrs, defStyleAttr) {

    init {
        val cornerRadius = context.resources.getDimension(R.dimen.corner_radius)
        val backgroundDrawable = RoundRectDrawable(android.R.color.white, cornerRadius)
        background = backgroundDrawable
        clipToOutline = true
        backgroundDrawable.setRadius(cornerRadius)
        invalidate()
    }
}