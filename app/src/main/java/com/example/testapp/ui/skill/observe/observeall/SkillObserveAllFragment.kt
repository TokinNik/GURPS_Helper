package com.example.testapp.ui.skill.observe.observeall

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testapp.R
import com.example.testapp.ui.SelectableData
import com.example.testapp.db.entity.Skill.Skill
import com.example.testapp.getThemeColor
import com.example.testapp.ui.skill.SkillItem
import com.example.testapp.ui.skill.SkillsHeaderItem
import com.example.testapp.ui.skill.observe.single.SkillObserveSingleFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
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

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_skill_observe_all, menu)
        val item = menu.findItem(R.id.menu_item_skill_search)
            item.actionView.findViewById<MaterialButton>(R.id.button_search).setOnClickListener {
                val query = item.actionView.findViewById<TextInputEditText>(R.id.search_query).text
                viewModel.searchSkills(query.toString())
            }
        super.onCreateOptionsMenu(menu, inflater)
    }


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
        observeSearchSkillComplete()
        observeSearchSkillsComplete()

        initOnClick()
        initRecyclerView()

        showProgressBar()
        viewModel.getAllSkills()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.forceClear()
    }

    private fun initOnClick()
    {

    }

    private fun showSkillObserveSingleDialog(skill: Skill) {
        val selectSkillDialog = SkillObserveSingleFragment(skill)
        selectSkillDialog.setTargetFragment(this, 1)
        selectSkillDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.dialogFragmentStyle)
        selectSkillDialog.show(fragmentManager!!, null)
    }

    private fun initRecyclerView(){
        groupAdapter.setOnItemClickListener { item, view ->
            if (item is SkillItem) {
                showSkillObserveSingleDialog(item.skill.data)
            }
        }
        recyclerView_skills.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = groupAdapter
        }

    }

    private fun setItems(skillList: List<Skill>) {
        groupAdapter.clear()
        val section = Section()
        section.setHeader(SkillsHeaderItem(
            onClickAdd = {
                val bundle = Bundle()
                bundle.putString("mode", "add")
                navController?.navigate(R.id.action_skillObserveAllFragment_to_editSkillFragment, bundle)
            },
            onClickDelete = {
                if (currentSelect >= 0) {
                    viewModel.deleteSkill(currentSkill)
                    currentSkill = Skill()
                    currentSelect = -1
                }
            }
        ))
        val colorActive = activity!!.getThemeColor(R.attr.colorSecondary)
        val colorInactive = activity!!.getThemeColor(R.attr.colorPrimaryVariant)
        for (item in skillList) {
            section.add(
                SkillItem(
                    skill = SelectableData(item),
                    colorActive = colorActive,
                    colorInactive = colorInactive
                )
            )
        }
        groupAdapter.add(section)
    }

    private fun observeSkills()
    {
        viewModel.skills.observe(this, Observer {
            setItems(it)
            hideProgressBar()
        })
    }

    private fun observeSkillById()
    {
        /*viewModel.skillById.observe(this, Observer {

        })*/
    }

    private fun observeSearchSkillComplete() {
        viewModel.searchSkillComplete.observe(this, Observer {
            showSkillObserveSingleDialog(it)
        })
    }

    private fun observeSearchSkillsComplete() {
        viewModel.searchSkillsComplete.observe(this, Observer {
            setItems(it)
            Toast.makeText(activity, "search complete", Toast.LENGTH_SHORT).show()
        })
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

    private fun hideProgressBar() {
        skill_all_fragment_progress_bar.visibility = View.GONE
    }

    private fun showProgressBar() {
        skill_all_fragment_progress_bar.visibility = View.VISIBLE
    }
}
