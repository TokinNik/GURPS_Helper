package com.example.testapp.ui.skill

import android.view.View
import com.example.testapp.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_header_skills.view.*

class SkillsHeaderItem(
    val onClickAdd: () -> Unit,
    val onClickDelete: () -> Unit
): Item() {
    lateinit var rootView: View

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.apply {
            root.button_add_skill.setOnClickListener{onClickAdd.invoke()}
            root.button_delete_skill.setOnClickListener{onClickDelete.invoke()}
            rootView = root
        }
    }

    override fun getLayout(): Int = R.layout.item_header_skills
}