package com.example.testapp.ui.character.choiseskill

import android.view.View
import com.example.testapp.R
import com.example.testapp.custom_view.outline_corner.OutlineProviders
import com.example.testapp.ui.SelectableData
import com.example.testapp.db.entity.Skill.Skill

import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_select_skill.view.*
class ChoiceSkillItem(
    val skill: SelectableData<Skill>,
    val backgroundColor: Int
) : Item() {

    lateinit var rootView: View

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.apply {
            root.outlineProvider = OutlineProviders(16f, OutlineProviders.OutlineType.ROUND_RECT)
            root.clipToOutline = true
//            root.item_skill_id.text = skill.data.id.toString()
            root.item_skill_name.text = skill.data.name
            root.item_skill_name_loc.text = skill.data.nameLoc
            root.checkBox_select_skill.isChecked = skill.select
            root.checkBox_select_skill.setOnClickListener{
                skill.select = skill.select.not()
            }
            root.setBackgroundColor(backgroundColor)
            rootView = root
        }
    }

    override fun getLayout(): Int = R.layout.item_select_skill
}