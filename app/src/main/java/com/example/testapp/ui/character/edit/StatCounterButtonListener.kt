package com.example.testapp.ui.character.edit

import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.testapp.databinding.StatCounterFloatBinding
import com.example.testapp.databinding.StatCounterIntBinding

class StatCounterIntPlusButtonListener(private val maxValue: Int) {
    constructor(maxValue: Int, onClick: () -> Unit) : this(maxValue) {
        onClickAction = onClick
    }

    private var onClickAction: () -> Unit = {}

    fun onClick(textView: View) {
        val bind: StatCounterIntBinding? = DataBindingUtil.findBinding(textView)
        if (bind != null) {
            var stat = bind.stat
            stat = if (stat < maxValue) stat + 1 else stat
            bind.let {
                it.stat = stat
                it.invalidateAll()
                onClickAction.invoke()
            }
        }
    }
}

class StatCounterIntMinusButtonListener(private val minValue: Int) {
    constructor(minValue: Int, onClick: () -> Unit) : this(minValue) {
        onClickAction = onClick
    }

    private var onClickAction: () -> Unit = {}
    fun onClick( textView: View) {
        val bind: StatCounterIntBinding? = DataBindingUtil.findBinding(textView)
        if (bind != null) {
            var stat = bind.stat
            stat = if (stat > minValue) stat - 1 else minValue
            bind.let {
                it.stat = stat
                it.invalidateAll()
                onClickAction.invoke()
            }
        }
    }
}
class StatCounterFloatPlusButtonListener(private val maxValue: Float, private val step: Float) {
    constructor(maxValue: Float, step: Float, onClick: () -> Unit) : this(maxValue, step) {
        onClickAction = onClick
    }

    private var onClickAction: () -> Unit = {}
    fun onClick(textView: View) {
        val bind: StatCounterFloatBinding? = DataBindingUtil.findBinding(textView)
        if (bind != null) {
            var stat = bind.stat
            stat = if (stat < maxValue) stat + step else stat
            bind.let {
                it.stat = stat
                it.invalidateAll()
                onClickAction.invoke()
            }
        }
    }
}

class StatCounterFloatMinusButtonListener(private val minValue: Float, private val step: Float) {
    constructor(minValue: Float, step: Float, onClick: () -> Unit) : this(minValue, step) {
        onClickAction = onClick
    }

    private var onClickAction: () -> Unit = {}

    fun onClick( textView: View) {
        val bind: StatCounterFloatBinding? = DataBindingUtil.findBinding(textView)
        if (bind != null) {
            var stat = bind.stat
            stat = if (stat > minValue) stat - step else minValue
            bind.let {
                it.stat = stat
                it.invalidateAll()
                onClickAction.invoke()
            }
        }
    }
}

