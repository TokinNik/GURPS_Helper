package com.example.testapp.ui.character

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.View
import com.example.testapp.R
import com.example.testapp.custom_view.outline_corner.OutlineProviders
import com.example.testapp.ui.SelectableData
import com.example.testapp.db.entity.Character
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.card_character_all.*
import kotlinx.android.synthetic.main.item_character.view.*


class CharacterItem(
    val character: SelectableData<Character>,
    val colorActive: Int,
    val colorInactive: Int,
    private val onClick: (character: SelectableData<Character>) -> Unit
) : Item() {

    lateinit var rootView: View

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.apply {
            root.outlineProvider = OutlineProviders(8f, OutlineProviders.OutlineType.ROUND_RECT)
            root.clipToOutline = true
            root.item_character_id.text = character.data.id.toString()
            val bytes = Base64.decode(character.data.portrait, Base64.DEFAULT)
            val image = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            root.item_character_image.setImageBitmap(image)
            root.item_character_name.text = character.data.name
            root.item_character_button_observe.setOnClickListener {
                onClick.invoke(character)
            }
            if (!character.select) {
                root.setBackgroundColor(colorInactive)
            } else {
                root.setBackgroundColor(colorActive)
            }

            rootView = root
        }
    }

    override fun getLayout(): Int = R.layout.item_character

}