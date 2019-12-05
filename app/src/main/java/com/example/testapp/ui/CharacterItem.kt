package com.example.testapp.ui

import com.example.testapp.R
import com.example.testapp.db.entity.Character
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_character.view.*


class CharacterItem(
    private val character: Character,
    private val onClick: (character: Character) -> Unit
) : Item() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.apply {
            root.textView_item_id.text = character.id.toString()
            root.textView_item_name.text = character.name
            root.setOnClickListener {
                onClick.invoke(character)
            }
        }
    }

    override fun getLayout(): Int = R.layout.item_character

}