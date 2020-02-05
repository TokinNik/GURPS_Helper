package com.example.testapp.ui.skill.observe.single

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.testapp.R
import com.example.testapp.db.entity.Skill.Skill
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_skill_single.*
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import toothpick.smoothie.viewmodel.installViewModelBinding

class SkillObserveSingleFragment(
    val skillName: String
) : DialogFragment() {

    private val viewModel: SkillObserveSingleFragmentViewModel by inject()

    private var currentSkill = Skill()

    private val navController: NavController?
        get() = activity?.let { Navigation.findNavController(it, R.id.nav_host_fragment) }

    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.setTitle(skillName)
        return inflater.inflate(R.layout.fragment_skill_single, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val scope = Toothpick.openScope("APP")
        scope.installViewModelBinding<SkillObserveSingleFragmentViewModel>(this)
        scope.inject(this)

        viewModel.clearEvents()

        observeErrors()
        observeSkillByName()

        viewModel.getSkillByName(skillName)
    }

    private fun initOnClick() {

    }

    private fun initRecyclerView(){

    }

    private fun observeSkillByName() {
        viewModel.getSkillByNameComplete.observe(this, Observer {
            currentSkill = it
            setDataInFields()
        })
    }

    private fun observeErrors() {
        viewModel.error.observe(this, Observer {
            Toast.makeText(activity, "error", Toast.LENGTH_SHORT).show()
            println("ERROR!!! $it")
        })
    }

    private fun setDataInFields() {
        skill_observe_single_name.text = currentSkill.name
        skill_observe_single_name_loc.text = currentSkill.nameLoc
        skill_observe_single_description.text = currentSkill.descriptionLoc
        skill_observe_single_tl.text = currentSkill.tl
        skill_observe_single_difficulty.text = currentSkill.difficulty
        skill_observe_single_specialization.text = currentSkill.specialization
        skill_observe_single_points.text = currentSkill.points
        skill_observe_single_reference.text = currentSkill.reference
        skill_observe_single_parry.text = currentSkill.parry
        skill_observe_single_categories.text = currentSkill.categories.toString()
        skill_observe_single_default.text = currentSkill.defaults.toString()
        skill_observe_single_prereq.text = currentSkill.prereqList.toString()
    }
}
