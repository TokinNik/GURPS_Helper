package com.example.testapp.ui.character.edit

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.example.testapp.R
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.stat_counter.view.*

class StatCounterPlusButtonListener(
    private val textView: TextView,
    private val maxValue: Int
): View.OnClickListener {
    override fun onClick(v: View?) {
        var value = textView.text.toString().toInt()
        if (value < maxValue) value++
        textView.text = value.toString()
    }
}

class StatCounterMinusButtonListener(
    private val textView: TextView,
    private val minValue: Int
): View.OnClickListener {
    override fun onClick(v: View?) {
        var value = textView.text.toString().toInt()
        if (value > minValue) value-- else value = minValue
        textView.text = value.toString()
    }
}

class PlusButton(private val textView: TextView, context: Context) : MaterialButton(context) {

    init {
        setOnClickListener(StatCounterPlusButtonListener(textView, 100))//???
    }

}

class StatCounter(context: Context): LinearLayout(context) {

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        //val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        //inflater.inflate(R.layout.stat_counter, this)
        edit_stat_button_plus.setOnClickListener(StatCounterPlusButtonListener(edit_stat, 100))
        edit_stat_button_minus.setOnClickListener(StatCounterPlusButtonListener(edit_stat, 1))
    }

}