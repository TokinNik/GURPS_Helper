package com.example.testapp.ui.character.choiseskill


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testapp.R
import com.example.testapp.db.entity.Skill.Skill
import com.example.testapp.ui.SelectableData
import com.example.testapp.ui.skill.SkillItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_choice_skill.*
import kotlinx.android.synthetic.main.fragment_choice_skill.button_accept
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import toothpick.smoothie.viewmodel.installViewModelBinding


class ChoiceSkillFragment(oldSkills: List<Skill>) : DialogFragment() {

    var onClickAccept: (skills: List<Skill>) -> Unit = {}

    private val viewModel: ChoiceSkillFragmentViewModel by inject()

    private var selectedSkills = ArrayList<Skill>(oldSkills)

    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.setTitle("Choice Skills")
        return inflater.inflate(R.layout.fragment_choice_skill, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val scope = Toothpick.openScope("APP")
        scope.installViewModelBinding<ChoiceSkillFragmentViewModel>(this)
        scope.inject(this)

        viewModel.clearEvents()

        observeSkills()
        observeSkillById()

        initOnClick()
        initRecyclerView()

        viewModel.getAllSkills()
    }

    private fun initOnClick()
    {
        button_accept.setOnClickListener {
            for (i in 0 until groupAdapter.itemCount){
                val item = (groupAdapter.getItem(i) as ChoiceSkillItem)
                if (item.skill.select) {
                    if (!selectedSkills.contains(item.skill.data)) selectedSkills.add(item.skill.data)
                } else {
                    selectedSkills.remove(item.skill.data)
                }
            }
            onClickAccept.invoke(selectedSkills)
        }
    }

    private fun initRecyclerView(){
        groupAdapter.setOnItemClickListener { item, _ ->
            (item as ChoiceSkillItem).skill.select = !item.skill.select
        }
        recyclerView_choice_skill.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = groupAdapter
        }

    }

    private fun observeSkills()
    {
        viewModel.skills.observe(this, Observer {
            groupAdapter.clear()
            for (item in it) {
                groupAdapter.add(
                    ChoiceSkillItem(
                        skill = SelectableData(item, selectedSkills.contains(item))
                    )
                )
            }
        })
    }

    private fun observeSkillById()
    {
        /*viewModel.skillById.observe(this, Observer {

        })*/
    }
}
