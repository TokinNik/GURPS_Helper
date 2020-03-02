package com.example.testapp.ui.advantage.observe.single

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
import com.example.testapp.db.entity.advantage.Advantage
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_advantage_single.*
import kotlinx.android.synthetic.main.fragment_skill_single.*
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import toothpick.smoothie.viewmodel.installViewModelBinding

class AdvantageObserveSingleFragment constructor(private val advantageName: String) : DialogFragment() {

    constructor(advantage: Advantage) : this(advantage.name) {
        currentAdvantage = advantage
    }

    private val viewModel: AdvantageObserveSingleFragmentViewModel by inject()

    private var currentAdvantage = Advantage()

    private val navController: NavController?
        get() = activity?.let { Navigation.findNavController(it, R.id.nav_host_fragment) }

    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.setTitle(advantageName)
        return inflater.inflate(R.layout.fragment_advantage_single, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val scope = Toothpick.openScope("APP")
        scope.installViewModelBinding<AdvantageObserveSingleFragmentViewModel>(this)
        scope.inject(this)

        viewModel.clearEvents()

        observeErrors()
        observeSkillByName()

        if (currentAdvantage.id == 0){
            viewModel.getAdvantageByName(advantageName)
        } else {
            setDataInFields()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.forceClear()
    }

    private fun initOnClick() {

    }

    private fun initRecyclerView(){

    }

    private fun observeSkillByName() {
        viewModel.getAdvantageByNameComplete.observe(this, Observer {
            currentAdvantage = it
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
        advantage_observe_single_name_loc.text = currentAdvantage.nameLoc
        advantage_observe_single_description.text = currentAdvantage.descriptionLoc
        advantage_observe_single_type.text = currentAdvantage.type
        advantage_observe_single_level.text = currentAdvantage.levels
        advantage_observe_single_point_cost.text = currentAdvantage.basePoints
        advantage_observe_single_points_per_level.text = currentAdvantage.pointsPerLevel
        advantage_observe_single_reference.text = currentAdvantage.reference
        advantage_observe_single_categories.text = currentAdvantage.categories.joinToString(""){ "$it\n" }
        if (currentAdvantage.modifiers.isNotEmpty()) advantage_observe_single_modifiers.text = currentAdvantage.modifiers
            .map{ "enabled: ${it.enabled}\n${it.name} (${it.cost} ${it.costType})\nAffects on ${it.affects} ${it.reference}" }
            .joinToString(""){ "$it\n\n" }
        if (currentAdvantage.skillBonuses.isNotEmpty()) advantage_observe_single_skills_bonuses.text = currentAdvantage.skillBonuses.map { "${it.nameCompare}  ${it.name} ${it.specializationCompare} ${it.specialization} ${it.amount}" }.joinToString("") { "$it\n" }
        if (currentAdvantage.attributeBonuses.isNotEmpty()) advantage_observe_single_attribute_bonuses.text = currentAdvantage.attributeBonuses.joinToString("") { "$it\n" }
        advantage_observe_single_prereq.text = currentAdvantage.prereqList
            .filter { it.skillPrereqList.isNotEmpty() }
            .map{ prereqList ->
                "${if(prereqList.all) "All of;\n" else "Some of:\n"}${
                prereqList.skillPrereqList
                    .map { "${it.name} ${if(it.level.isNotBlank()) "level =" else ""} ${it.level} ${if(it.specialization.isNotBlank()) "spec. =" else ""} ${it.specialization}" }
                    .joinToString(""){ "$it\n" }}"
            }
            .joinToString(""){ "$it\n" }
        println(currentAdvantage.prereqList.toString())
    }
}
