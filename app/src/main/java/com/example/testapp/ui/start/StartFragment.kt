package com.example.testapp.ui.start

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testapp.R
import com.example.testapp.RxTest
import com.example.testapp.SelectableData
import com.example.testapp.db.entity.Character
import com.example.testapp.db.entity.Skill
import com.example.testapp.ui.character.CharacterItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_start.*
import kotlinx.android.synthetic.main.item_character.*
import kotlinx.coroutines.selects.select
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import toothpick.smoothie.viewmodel.installViewModelBinding

class StartFragment : Fragment() {

    private val viewModel: StartFragmentViewModel by inject()

    private var currentCharacter = Character()
    private var currentSelect = -1

    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    private val navController: NavController
        get() = Navigation.findNavController(activity!!, R.id.nav_host_fragment)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        println("--------------------------------------START")
        return inflater.inflate(R.layout.fragment_start, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val scope = Toothpick.openScope("APP")
        scope.installViewModelBinding<StartFragmentViewModel>(this)
        scope.inject(this)

        button_rx.setOnClickListener { onClickRx() }
        button_add.setOnClickListener { onClickAdd() }
        button_delete.setOnClickListener { onClickDelete() }

        observeCharacters()
        observeErrors()
        observeDeleteComplete()

        recyclerViewInit()


/*        viewModel.addSkill(Skill(
            name = "skill_1",
            mainAttribute = "st",
            level = 1
        ))
        viewModel.addSkill(Skill(
            name = "skill_2",
            mainAttribute = "dx",
            level = 1
        ))
        viewModel.addSkill(Skill(
            name = "skill_3",
            mainAttribute = "iq",
            level = 1
        ))
        viewModel.addSkill(Skill(
            name = "skill_4",
            mainAttribute = "ht",
            level = 1
        ))*/
    }

    private fun onClickAdd() {
        val bundle = Bundle()
        bundle.putString("mode", "add")
        navController.navigate(R.id.action_startFragment_to_characterEditFragment, bundle)
    }

    private fun onClickDelete() {
        viewModel.deleteCharacter(currentCharacter)
        updateItems()
    }

    private fun recyclerViewInit() {
        groupAdapter.setOnItemClickListener { item, view ->
            val select = groupAdapter.getAdapterPosition(item)
            if ((item as CharacterItem).character.select) {
                item.character.select = false
                view.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
            }
            else {
                item.character.select = true
                view.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorAccent))

                if (currentSelect >= 0 && currentSelect != select){
                    val prevItem = groupAdapter.getGroupAtAdapterPosition(currentSelect) as CharacterItem
                    prevItem.character.select = false
                    prevItem.rootView.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
                }
            }
            currentCharacter = item.character.data
            groupAdapter.notifyItemChanged(currentSelect)
            currentSelect = select
            groupAdapter.notifyItemChanged(currentSelect)
        }
        recyclerView_characters.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = groupAdapter
        }
        updateItems()
    }

    private fun updateItems()
    {
        viewModel.getItems()
    }

    private fun addItems(items: List<Character>)
    {
        groupAdapter.clear()

        for(i in items)
        {
            groupAdapter.apply {
                add(
                    CharacterItem(
                        character = SelectableData(i),
                        colorActive = ContextCompat.getColor(context!!, R.color.colorAccent),
                        colorInactive = ContextCompat.getColor(context!!, R.color.colorPrimary),//todo move to val?
                        onClick = {
                            val bundle = Bundle()
                            bundle.putInt("id", it.data.id)
                            navController.navigate(R.id.action_startFragment_to_characterFragment, bundle)
                        })
                )
            }
        }
    }

    private fun onClickRx() {
        val observable = Observable.create(RxTest())

        val x = observable.subscribeOn(Schedulers.io())//??? x
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { println(it) }
            .subscribe({
                textView.text = "Next = ${it}"
                println(it)
            },
                {
                    textView.text = "Error!!!"
                },
                {
                    textView.text = "Complete!!!"
                    progressBar.visibility = View.INVISIBLE
                },
                {
                    //dispose
                })
    }

    private fun observeCharacters()
    {
        viewModel.characters.observe(this, Observer {
            addItems(it)
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
}
