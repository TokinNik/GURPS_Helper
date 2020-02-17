package com.example.testapp

import android.util.TypedValue
import androidx.fragment.app.FragmentActivity

fun FragmentActivity.getThemeColor(attrId: Int): Int = TypedValue().run {
        this@getThemeColor.theme.resolveAttribute(attrId, this, true)
        data
    }
