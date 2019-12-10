package com.example.testapp.ui.skill.observeall

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testapp.R
import com.example.testapp.SelectableData
import com.example.testapp.db.entity.Skill
import com.example.testapp.ui.character.edit.SkillItem
import com.example.testapp.ui.character.edit.SkillsHeaderItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import kotlinx.android.synthetic.main.fragment_skill_all.*
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import toothpick.smoothie.viewmodel.installViewModelBinding

class SkillObserveAllFragment : Fragment() {

    private val viewModel: SkillObserveAllFragmentViewModel by inject()

    private var currentSkill = Skill()
    private var currentSelect = -1

    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    private val navController: NavController?
        get() = activity?.let { Navigation.findNavController(it, R.id.nav_host_fragment) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_skill_all, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val scope = Toothpick.openScope("APP")
        scope.installViewModelBinding<SkillObserveAllFragmentViewModel>(this)
        scope.inject(this)

        observeErrors()
        observeSkillById()
        observeSkills()

        initOnClick()
        initRecyclerView()

        viewModel.getAllSkills()
    }

    private fun initOnClick()
    {

    }

    private fun initRecyclerView(){
        groupAdapter.setOnItemClickListener { item, view ->
            val select = groupAdapter.getAdapterPosition(item)
            if(item is SkillItem) {
                if (item.skill.select) {
                    item.skill.select = false
                    view.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
                }
                else {
                    item.skill.select = true
                    view.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorAccent))

                    if (currentSelect >= 0 && currentSelect != select){
                        val prevItem = groupAdapter.getGroupAtAdapterPosition(0).getItem(currentSelect) as SkillItem
                        prevItem.skill.select = false
                        prevItem.rootView.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
                    }
                }
                currentSkill = item.skill.data
                groupAdapter.notifyItemChanged(currentSelect)
                currentSelect = select
                groupAdapter.notifyItemChanged(currentSelect)
            }
        }
        recyclerView_skills.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = groupAdapter
        }

    }

    private fun observeSkills()
    {
        viewModel.skills.observe(this, Observer {
            groupAdapter.clear()
            val section = Section()
            section.setHeader(SkillsHeaderItem())
            for (item in it) {
                section.add(
                    SkillItem(
                        skill = SelectableData(item),
                        colorActive = ContextCompat.getColor(context!!, R.color.colorAccent),
                        colorInactive = ContextCompat.getColor(context!!, R.color.colorPrimary)
                    )
                )
            }
            groupAdapter.add(section)
        })
    }

    private fun observeSkillById()
    {
        viewModel.skillById.observe(this, Observer {

        })
    }

    private fun observeErrors() {
        viewModel.error.observe(this, Observer {
            Toast.makeText(activity, "error", Toast.LENGTH_SHORT).show()
            println("ERROR!!! $it")
        })
    }
}
