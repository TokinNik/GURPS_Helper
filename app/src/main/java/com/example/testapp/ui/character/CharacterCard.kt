package com.example.testapp.ui.character

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.example.testapp.R
import com.example.testapp.custom_view.outline_corner.OutlineProviders
import com.example.testapp.db.entity.Skill.Skill
import com.example.testapp.ui.SelectableData
import com.example.testapp.ui.character.observe.ViewPagerCharacterAdapter
import com.example.testapp.ui.settings.ColorScheme
import com.example.testapp.ui.skill.SkillItem
import com.example.testapp.util.Base64RequestHandler
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.button.MaterialButton
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.card_character_all.view.*

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
        Picasso
            .get()
            .load("${Base64RequestHandler.BASE_64_SCHEME}${portrait}")
            .placeholder(R.drawable.gm_logo_original)
            .error(R.drawable.gm_logo_original)
            .into(cardRoot.character_card_image)
    }
}