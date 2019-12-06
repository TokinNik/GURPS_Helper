package com.example.testapp.ui.start

import android.graphics.Color
import androidx.core.content.ContextCompat
import com.example.testapp.R
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class SelectableGroupAdapter<VH : GroupieViewHolder?>: GroupAdapter<VH>() {

    var selects = arrayListOf<Boolean>()

    override fun onBindViewHolder(
        holder: VH,
        position: Int,
        payloads: List<Any?>
    ) {
        super.onBindViewHolder(holder, position, payloads)
        if (selects[position])
            holder?.itemView?.setBackgroundColor(Color.MAGENTA)
        else
            holder?.itemView?.setBackgroundColor(Color.CYAN)

    }

}