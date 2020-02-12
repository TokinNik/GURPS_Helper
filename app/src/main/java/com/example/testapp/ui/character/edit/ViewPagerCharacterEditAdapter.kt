package com.example.testapp.ui.character.edit

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.testapp.ui.character.edit.pages.skills.CharacterEditSkillsFragment
import com.example.testapp.ui.character.edit.pages.stats.CharacterEditStatsFragment
import com.example.testapp.ui.settings.ColorScheme
import io.reactivex.Observable

class ViewPagerCharacterEditAdapter(fa: FragmentActivity, val onSave: Observable<Boolean>) : FragmentStateAdapter(fa) {

    var schemeType: ColorScheme = ColorScheme.CLASSIC
    lateinit var onClickAdd: () -> Unit
    lateinit var layoutManager: LinearLayoutManager

    override fun getItemCount(): Int = 2//CharacterPages.values().size

    override fun createFragment(position: Int): Fragment = when(position) {
        0 -> CharacterEditStatsFragment(onSave)
        1 -> CharacterEditSkillsFragment(onSave)
        else -> CharacterEditStatsFragment(onSave)
    }
}