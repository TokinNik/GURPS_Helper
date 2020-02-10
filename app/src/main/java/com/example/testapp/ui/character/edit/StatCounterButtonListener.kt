package com.example.testapp.ui.character.edit

import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.testapp.databinding.StatCounterBinding

class StatCounterPlusButtonListener(private val maxValue: Int) {
    fun onClick(textView: View) {
        val bind: StatCounterBinding? = DataBindingUtil.findBinding(textView)
        if (bind != null) {
            var stat = bind.stat
            stat = if (stat < maxValue) stat + 1 else stat
            bind.let {
                it.stat = stat
                it.invalidateAll()
            }
        }
    }
}

class StatCounterMinusButtonListener(private val minValue: Int) {
    fun onClick( textView: View) {
        val bind: StatCounterBinding? = DataBindingUtil.findBinding(textView)
        if (bind != null) {
            var stat = bind.stat
            stat = if (stat > minValue) stat - 1 else minValue
            bind.let {
                it.stat = stat
                it.invalidateAll()
            }
        }
    }
}

