package com.example.testapp.ui.character

import android.view.View
import com.example.testapp.R
import com.example.testapp.ui.SelectableData
import com.example.testapp.db.entity.Character
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_character.view.*


class CharacterHorizontalItem(
    val character: SelectableData<Character>,
    val colorActive: Int,
    val colorInactive: Int,
    private val onClick: (character: SelectableData<Character>) -> Unit
) : Item() {

    lateinit var rootView: View

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.apply {
            root.textView_item_id.text = character.data.id.toString()
            root.textView_item_select.text = character.select.toString()
            root.textView_item_name.text = character.data.name
//            root.button_observe.setOnClickListener {
//                onClick.invoke(character)
//            }
            if (!character.select) {
                root.setBackgroundColor(colorInactive)
            } else {
                root.setBackgroundColor(colorActive)
            }

            rootView = root
        }
    }

    override fun getLayout(): Int = R.layout.item_character_horizontal

}