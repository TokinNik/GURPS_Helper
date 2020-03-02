package com.example.testapp.ui.advantage.observe.all

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testapp.R
import com.example.testapp.ui.SelectableData
import com.example.testapp.db.entity.advantage.Advantage
import com.example.testapp.getThemeColor
import com.example.testapp.ui.advantage.AdvantageItem
import com.example.testapp.ui.advantage.observe.single.AdvantageObserveSingleFragment
import com.example.testapp.ui.skill.SADQHeaderItem
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import kotlinx.android.synthetic.main.fragment_advantage_all.*
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import toothpick.smoothie.viewmodel.installViewModelBinding

class AdvantageObserveAllFragment : Fragment() {

    private val viewModel: AdvantageObserveAllFragmentViewModel by inject()

    private var currentAdvantage = Advantage()
    private var currentSelect = -1

    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    private val navController: NavController?
        get() = activity?.let { Navigation.findNavController(it, R.id.nav_host_fragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        val item = menu.findItem(R.id.menu_item_search)
            item.actionView.findViewById<MaterialButton>(R.id.button_search).setOnClickListener {
                val query = item.actionView.findViewById<TextInputEditText>(R.id.search_query).text
                viewModel.searchAdvantages(query.toString())
            }
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_advantage_all, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val scope = Toothpick.openScope("APP")
        scope.installViewModelBinding<AdvantageObserveAllFragmentViewModel>(this)
        scope.inject(this)

        viewModel.clearEvents()

        observeErrors()
        observeAdvantageById()
        observeAdvantages()
        observeDeleteComplete()
        observeSearchAdvantageComplete()
        observeSearchAdvantagesComplete()

        initOnClick()
        initRecyclerView()

        showProgressBar()
        viewModel.getAllAdvantages()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.forceClear()
    }

    private fun initOnClick()
    {

    }

    private fun showAdvantageObserveSingleDialog(advantage: Advantage) {
        val selectSkillDialog = AdvantageObserveSingleFragment(advantage)
        selectSkillDialog.setTargetFragment(this, 1)
        selectSkillDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.dialogFragmentStyle)
        selectSkillDialog.show(fragmentManager!!, null)
    }

    private fun initRecyclerView(){
        groupAdapter.setOnItemClickListener { item, view ->
            if (item is AdvantageItem) {
                showAdvantageObserveSingleDialog(item.advantage.data)
            }
        }
        recyclerView_advantage.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = groupAdapter
        }

    }

    private fun setItems(skillList: List<Advantage>) {
        groupAdapter.clear()
        val section = Section()
        section.setHeader(SADQHeaderItem(
            title = resources.getString(R.string.advantages),
            onClickAdd = {
//                val bundle = Bundle()
//                bundle.putString("mode", "add")
//                navController?.navigate(R.id.action_skillObserveAllFragment_to_editSkillFragment, bundle) todo edit fragment
            },
            onClickDelete = {
                if (currentSelect >= 0) {
                    viewModel.deleteAdvantage(currentAdvantage)
                    currentAdvantage = Advantage()
                    currentSelect = -1
                }
            }
        ))
        val colorActive = activity!!.getThemeColor(R.attr.colorSecondary)
        val colorInactive = activity!!.getThemeColor(R.attr.colorPrimaryVariant)
        for (item in skillList) {
            section.add(
                AdvantageItem(
                    advantage = SelectableData(item),
                    colorActive = colorActive,
                    colorInactive = colorInactive
                )
            )
        }
        groupAdapter.add(section)
    }

    private fun observeAdvantages() {
        viewModel.getAllAdvantage.observe(this, Observer {
            setItems(it)
            hideProgressBar()
        })
    }

    private fun observeAdvantageById() {
        /*viewModel.skillById.observe(this, Observer {

        })*/
    }

    private fun observeSearchAdvantageComplete() {
        viewModel.searchAdvantageComplete.observe(this, Observer {
            showAdvantageObserveSingleDialog(it)
        })
    }

    private fun observeSearchAdvantagesComplete() {
        viewModel.searchAdvantagesComplete.observe(this, Observer {
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
        advantage_all_fragment_progress_bar.visibility = View.GONE
    }

    private fun showProgressBar() {
        advantage_all_fragment_progress_bar.visibility = View.VISIBLE
    }
}
