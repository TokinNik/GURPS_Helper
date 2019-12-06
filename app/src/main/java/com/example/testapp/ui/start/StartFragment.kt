package com.example.testapp.ui.start

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testapp.R
import com.example.testapp.RxTest
import com.example.testapp.db.entity.Character
import com.example.testapp.ui.character.CharacterItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_start.*
import kotlinx.android.synthetic.main.item_character.*
import toothpick.Toothpick

class StartFragment : Fragment() {

    private lateinit var viewModel: StartFragmentViewModel

    private var currentCharacter = 1
    private var maxCharacter = 1

    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    private val navController: NavController
        get() = Navigation.findNavController(activity!!, R.id.nav_host_fragment)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_start, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Toothpick.inject(this, Toothpick.openScope("APP"))

        viewModel = ViewModelProviders.of(this).get(StartFragmentViewModel::class.java)

        button_rx.setOnClickListener { onClickRx() }
        button_add.setOnClickListener { onClickAdd() }
        button_delete.setOnClickListener { onClickDelete() }

        observeCharacters()

        recyclerViewInit()
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
            view.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorAccent))
            currentCharacter = (item as CharacterItem).character.id
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
                        character = i,
                        onClick = {
                            val bundle = Bundle()
                            bundle.putInt("id", it.id)
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
            maxCharacter = it.size
        })
    }
}
