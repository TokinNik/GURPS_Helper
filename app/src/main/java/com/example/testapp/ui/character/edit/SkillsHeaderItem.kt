package com.example.testapp.ui.character.edit

import android.view.View
import com.example.testapp.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
class SkillsHeaderItem(): Item() {
    lateinit var rootView: View

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.apply {
            rootView = root
        }
    }

    override fun getLayout(): Int = R.layout.item_header_skills
}