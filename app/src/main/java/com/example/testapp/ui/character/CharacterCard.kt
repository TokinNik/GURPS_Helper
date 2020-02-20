package com.example.testapp.ui.character

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.View
import android.widget.ImageView
import androidx.viewpager2.widget.ViewPager2
import com.example.testapp.R
import com.example.testapp.custom_view.outline_corner.OutlineProviders
import com.example.testapp.db.entity.Skill.Skill
import com.example.testapp.ui.SelectableData
import com.example.testapp.ui.character.observe.ViewPagerCharacterAdapter
import com.example.testapp.ui.settings.ColorScheme
import com.example.testapp.ui.skill.SkillItem
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.button.MaterialButton
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class CharacterCard(
    val cardRoot: View,
    val groupAdapter: GroupAdapter<GroupieViewHolder>,
    val colorActive: Int,
    val colorInactive: Int,
    val onSkillClick: (Skill) -> Unit
) {

    private val characterCardPager: ViewPager2 = cardRoot.findViewById(R.id.character_card_pager)
    private var isInfoCollapsed = false

    init {
        characterCardPager.adapter = ViewPagerCharacterAdapter()
        characterCardPager.offscreenPageLimit = 4

        val radius = 32f
        characterCardPager.outlineProvider = OutlineProviders(radius, OutlineProviders.OutlineType.ROUND_RECT_TOP)
        characterCardPager.clipToOutline = true

        groupAdapter.setOnItemClickListener { item, _ ->
            onSkillClick.invoke((item as SkillItem).skill.data)
        }

        (characterCardPager.adapter as ViewPagerCharacterAdapter).groupAdapter = groupAdapter

        cardRoot.findViewById<MaterialButton>(R.id.character_card_collapse_info).setOnClickListener {
            if (isInfoCollapsed) {
                isInfoCollapsed = false
                cardRoot.findViewById<FlexboxLayout>(R.id.character_card_other_info).visibility = View.VISIBLE
            } else {
                isInfoCollapsed = true
                cardRoot.findViewById<FlexboxLayout>(R.id.character_card_other_info).visibility = View.GONE
            }
        }
    }

    fun setColorScheme(scheme: ColorScheme) {
        (characterCardPager.adapter as ViewPagerCharacterAdapter).schemeType = scheme
    }

    fun setItems(items: List<Skill>) {
        groupAdapter.clear()
        for(i in items)
        {
            groupAdapter.apply {
                add(
                    SkillItem(
                        skill = SelectableData(i),
                        colorActive = colorActive,
                        colorInactive = colorInactive
                    )
                )
            }
        }
    }

    fun setImage(portrait: String) {
        val bytes = Base64.decode(portrait, Base64.DEFAULT)
        val image = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        cardRoot.findViewById<ImageView>(R.id.character_card_image).setImageBitmap(image)
    }
}