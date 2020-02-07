package com.example.testapp.custom_view.outline_corner

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider

class OutlineProviders(private val radius: Float, private val type: OutlineType) : ViewOutlineProvider() {
    enum class OutlineType {
        ROUND_RECT,
        ROUND_RECT_TOP,
        ROUND_RECT_BOTTOM,
        ROUND_RECT_RIGHT,
        ROUND_RECT_LEFT,
        OVAL,
        RECT
    }

    override fun getOutline(view: View?, outline: Outline?) {
        when (type) {
            OutlineType.ROUND_RECT -> outline?.setRoundRect(0, 0, view!!.width, view.height, radius)
            OutlineType.ROUND_RECT_TOP -> outline?.setRoundRect(0, 0, view!!.width, (view.height + radius).toInt(), radius)
            OutlineType.ROUND_RECT_BOTTOM -> outline?.setRoundRect(0, -radius.toInt(), view!!.width, view.height, radius)
            OutlineType.ROUND_RECT_RIGHT -> outline?.setRoundRect(-radius.toInt(), 0, view!!.width, view.height, radius)
            OutlineType.ROUND_RECT_LEFT -> outline?.setRoundRect(0, 0, (view!!.width + radius).toInt(), view.height, radius)
            OutlineType.OVAL -> outline?.setOval(0, 0, view!!.width, (view.height + radius).toInt())
            OutlineType.RECT -> outline?.setRect(0, 0, view!!.width, (view.height + radius).toInt())
        }
    }
}