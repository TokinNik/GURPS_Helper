package com.example.testapp.ui.skill

import android.view.View
import com.example.testapp.R
import com.example.testapp.custom_view.outline_corner.OutlineProviders
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_header_sadq.view.*

class SADQHeaderItem(
    val title: String,
    val onClickAdd: () -> Unit,
    val onClickDelete: () -> Unit
): Item() {
    lateinit var rootView: View

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.apply {
            root.outlineProvider = OutlineProviders(32f, OutlineProviders.OutlineType.ROUND_RECT_BOTTOM)
            root.clipToOutline = true
            root.header_sadq_title.text = title
            root.header_sadq_button_add.setOnClickListener{onClickAdd.invoke()}
            root.header_sadq_button_delete.setOnClickListener{onClickDelete.invoke()}
            rootView = root
        }
    }

    override fun getLayout(): Int = R.layout.item_header_sadq
}