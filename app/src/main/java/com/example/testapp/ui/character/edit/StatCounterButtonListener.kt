package com.example.testapp.ui.character.edit

import android.content.Context
import android.view.View
import android.widget.TextView
import com.google.android.material.button.MaterialButton

class StatCounterPlusButtonListener(private val textView: TextView): View.OnClickListener {
    override fun onClick(v: View?) {
        var value = textView.text.toString().toInt()
        if (value < 100) value++
        textView.text = value.toString()
    }
}

class StatCounterMinusButtonListener(private val textView: TextView): View.OnClickListener {
    override fun onClick(v: View?) {
        var value = textView.text.toString().toInt()
        if (value > 1) value-- else value = 1
        textView.setText(value.toString())
    }
}

class PlusButton(private val textView: TextView, context: Context) : MaterialButton(context) {

    init {
        setOnClickListener(StatCounterPlusButtonListener(textView))//???
    }

}