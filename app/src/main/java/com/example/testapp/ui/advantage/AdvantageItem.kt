package com.example.testapp.ui.advantage

import android.view.View
import com.example.testapp.R
import com.example.testapp.custom_view.outline_corner.OutlineProviders
import com.example.testapp.db.entity.Skill.Skill
import com.example.testapp.db.entity.advantage.Advantage
import com.example.testapp.ui.SelectableData
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_advantage.view.*
import kotlinx.android.synthetic.main.item_skill.view.*

class AdvantageItem (
    val advantage: SelectableData<Advantage>,
    val colorActive: Int,
    val colorInactive: Int
) : Item() {

    lateinit var rootView: View

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.apply {
            root.outlineProvider = OutlineProviders(8f, OutlineProviders.OutlineType.ROUND_RECT)
            root.clipToOutline = true
            root.item_advantage_id.text = advantage.data.id.toString()
            root.item_advantage_name.text = advantage.data.name
            root.item_advantage_level.text = "l: ${advantage.data.levels}"
            root.item_advantage_points.text = "p: ${advantage.data.basePoints}"
            root.item_advantage_name_loc.text = advantage.data.nameLoc

            if (!advantage.select) {
                root.setBackgroundColor(colorInactive)
            } else {
                root.setBackgroundColor(colorActive)
            }

            rootView = root
        }
    }

    override fun getLayout(): Int = R.layout.item_advantage
}