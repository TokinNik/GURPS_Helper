package com.example.testapp.ui.skill.observe.observeall

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testapp.R
import com.example.testapp.ui.SelectableData
import com.example.testapp.db.entity.Skill.Skill
import com.example.testapp.ui.skill.SkillItem
import com.example.testapp.ui.skill.SkillsHeaderItem
import com.example.testapp.ui.skill.observe.single.SkillObserveSingleFragment
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

        viewModel.clearEvents()

        observeErrors()
        observeSkillById()
        observeSkills()
        observeDeleteComplete()

        initOnClick()
        initRecyclerView()

        viewModel.getAllSkills()
    }

    private fun initOnClick()
    {

    }

    private fun initRecyclerView(){
        groupAdapter.setOnItemClickListener { item, view ->
            val selectSkillDialog = SkillObserveSingleFragment((item as SkillItem).skill.data.name)
            selectSkillDialog.setTargetFragment(this, 1)
            selectSkillDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.dialogFragmentStyle)
            selectSkillDialog.show(fragmentManager!!, null)
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
            section.setHeader(SkillsHeaderItem(
                onClickAdd = {
                    val bundle = Bundle()
                    bundle.putString("mode", "add")
                    navController?.navigate(
                        R.id.action_skillObserveAllFragment_to_editSkillFragment,
                        bundle
                    )
                },
                onClickDelete = {
                    if (currentSelect >= 0) {
                        viewModel.deleteSkill(currentSkill)
                        currentSkill =
                            Skill()
                        currentSelect = -1
                    }
                }
            ))
            for (item in it) {
                section.add(
                    SkillItem(
                        skill = SelectableData(item),
                        colorActive = ContextCompat.getColor(context!!, R.color.accent),
                        colorInactive = ContextCompat.getColor(context!!, R.color.primary_light)
                    )
                )
            }
            groupAdapter.add(section)
        })
    }

    private fun observeSkillById()
    {
        /*viewModel.skillById.observe(this, Observer {

        })*/
    }

    private fun observeDeleteComplete() {
        viewModel.deleteComplete.observe(this, Observer {
            Toast.makeText(activity, "deleted", Toast.LENGTH_SHORT).show()
        })
    }

    private fun observeErrors() {
        viewModel.error.observe(this, Observer {
            Toast.makeText(activity, "error", Toast.LENGTH_SHORT).show()
            println("ERROR!!! $it")
        })
    }
}
