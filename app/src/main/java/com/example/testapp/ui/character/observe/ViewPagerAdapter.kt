package com.example.testapp.ui.character.observe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp.R
import com.example.testapp.ui.settings.ColorScheme
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.item_page_sadq.view.*

class ViewPagerAdapter : RecyclerView.Adapter<PagerVH>() {

    var groupAdapter = GroupAdapter<GroupieViewHolder>()
    var schemeType: ColorScheme = ColorScheme.CLASSIC
    lateinit var layoutManager: LinearLayoutManager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerVH {
        layoutManager = LinearLayoutManager(parent.context)
        return PagerVH(LayoutInflater.from(parent.context).inflate(R.layout.item_page_sadq, parent, false))
    }

    override fun getItemCount(): Int = CharacterPages.values().size

    override fun onBindViewHolder(holder: PagerVH, position: Int) = holder.itemView.run {
        character_card_page_text.text = CharacterPages.values()[position].name
        test_linear_layout.setBackgroundResource(CharacterPages.values()[position].color(schemeType))
        if ( character_card_skills_list.layoutManager == null ) character_card_skills_list.layoutManager = layoutManager
        character_card_skills_list.adapter = groupAdapter
    }
}

class PagerVH(itemView: View) : RecyclerView.ViewHolder(itemView)

enum class CharacterPages {

    SKILLS{
        override fun color(schemeType: ColorScheme): Int = when(schemeType) {
            ColorScheme.CLASSIC -> R.color.skills_container
            ColorScheme.BRIGHT -> android.R.color.holo_purple
            else -> android.R.color.holo_purple
        }
    },
    ADVANTAGE{
        override fun color(schemeType: ColorScheme): Int = when(schemeType) {
            ColorScheme.CLASSIC -> R.color.advantage_container
            ColorScheme.BRIGHT -> android.R.color.holo_blue_dark
            else -> android.R.color.holo_blue_dark
        }
    },
    DISADVANTAGE{
        override fun color(schemeType: ColorScheme): Int = when(schemeType) {
            ColorScheme.CLASSIC -> R.color.disadvantage_container
            ColorScheme.BRIGHT -> android.R.color.holo_red_dark
            else -> android.R.color.holo_red_dark
        }
    },
    QUIRKS{
        override fun color(schemeType: ColorScheme): Int = when(schemeType) {
            ColorScheme.CLASSIC -> R.color.quirks_container
            ColorScheme.BRIGHT -> android.R.color.holo_red_light
            else -> android.R.color.holo_red_light
        }
    };

    abstract fun color(schemeType: ColorScheme): Int
}