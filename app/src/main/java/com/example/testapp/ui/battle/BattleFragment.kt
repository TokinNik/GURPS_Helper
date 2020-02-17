package com.example.testapp.ui.battle


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testapp.R
import com.example.testapp.db.entity.Character
import com.example.testapp.getThemeColor
import com.example.testapp.ui.SelectableData
import com.example.testapp.ui.character.CharacterHorizontalItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_battle.*
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import toothpick.smoothie.viewmodel.installViewModelBinding

class BattleFragment : Fragment() {

    private val viewModel: BattleFragmentViewModel by inject()

    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    private val navController: NavController?
        get() = activity?.let { Navigation.findNavController(it, R.id.nav_host_fragment) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_battle, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val scope = Toothpick.openScope("APP")
        scope.installViewModelBinding<BattleFragmentViewModel>(this)
        scope.inject(this)

        viewModel.clearEvents()

        observeCharacters()

        recyclerViewInit()

        viewModel.getItems()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.forceClear()
    }

    private fun recyclerViewInit() {
        recyclerView_characters_queue.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = groupAdapter
        }
    }

    private fun addItems(items: List<Character>)
    {
        groupAdapter.clear()
        val colorActive = activity!!.getThemeColor(R.attr.colorSecondary)
        val colorInactive = activity!!.getThemeColor(R.attr.colorPrimaryVariant)
        for(i in items)
        {
            groupAdapter.apply {
                add(
                    CharacterHorizontalItem(
                        character = SelectableData(i),
                        colorActive = colorActive,
                        colorInactive = colorInactive,
                        onClick = {
//                            val bundle = Bundle()
//                            bundle.putInt("id", it.data.id)
//                            navController?.navigate(R.id.action_startFragment_to_characterFragment, bundle)
                        }
                    )
                )
            }
        }
    }

    private fun observeCharacters()
    {
        viewModel.characters.observe(this, Observer {
            addItems(it)
        })
    }
}
