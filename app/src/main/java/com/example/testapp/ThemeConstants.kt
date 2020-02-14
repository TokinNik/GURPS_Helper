package com.example.testapp

import android.util.TypedValue
import androidx.fragment.app.FragmentActivity

fun FragmentActivity.genThemeColor(attrId: Int): Int = TypedValue().run {
        this@genThemeColor.theme.resolveAttribute(attrId, this, true)
        data
    }
