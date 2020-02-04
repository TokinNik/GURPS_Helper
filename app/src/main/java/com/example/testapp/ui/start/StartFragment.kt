package com.example.testapp.ui.start

import android.app.Activity.RESULT_OK
import android.content.DialogInterface
import android.content.Intent
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
import com.example.testapp.RxTest
import com.example.testapp.ui.SelectableData
import com.example.testapp.db.entity.Character
import com.example.testapp.ui.character.CharacterItem
import com.example.testapp.util.GCSParser
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_start.*
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import toothpick.smoothie.viewmodel.installViewModelBinding

class StartFragment : Fragment() {

    private val viewModel: StartFragmentViewModel by inject()

    private var isfileAdd: Boolean = false
    private val parser: GCSParser by inject()
    private val requestCodeAddFromFile = 200
    private val disposeBag = CompositeDisposable()
    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    private var newCharacter = Character()
    private var currentCharacter = Character()
    private var currentSelect = -1
    private var counter = 1

    private val navController: NavController?
        get() = activity?.let { Navigation.findNavController(it, R.id.nav_host_fragment) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_start, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val scope = Toothpick.openScope("APP")
        scope.installViewModelBinding<StartFragmentViewModel>(this)
        scope.inject(this)

        viewModel.clearEvents()

        button_rx.setOnClickListener { onClickRx() }
        button_add.setOnClickListener { onClickAdd() }
        button_delete.setOnClickListener { onClickDelete() }

        observeGetAllCharacters()
        observeErrors()
        observeDeleteComplete()
        observeAddComplete()
        observeGetLastCharacterIdComplete()

        recyclerViewInit()

        viewModel.getAllCharacters()
    }

    override fun onStop() {
        super.onStop()
        disposeBag.clear()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestCodeAddFromFile && resultCode == RESULT_OK) {
            newCharacter = Character()
            viewModel.addCharacter(newCharacter)
            isfileAdd = true
            parser.filePath = data?.data?.path ?: ""
        }
    }

    private fun onClickAdd() {
        val builder = MaterialAlertDialogBuilder(activity)
        builder
            .setTitle(R.string.add_new_character)
            .setMessage(R.string.add_new_character_message)
            .setPositiveButton(R.string.add_from_file) { dialogInterface: DialogInterface, i: Int ->
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "file/*"
                startActivityForResult(intent, requestCodeAddFromFile)
                dialogInterface.dismiss()
            }
            .setNegativeButton(R.string.create_new) { dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss()
                val bundle = Bundle()
                bundle.putString("mode", "add")
                navController?.navigate(R.id.action_startFragment_to_characterEditFragment, bundle)
            }
            .create().show()
    }

    private fun onClickDelete() {
        viewModel.deleteCharacter(currentCharacter)
        currentCharacter = Character()
        currentSelect = -1
    }

    private fun onClickRx() {
        if (disposeBag.size() > 0)
            return
        progressBar.visibility = View.VISIBLE
        val rxt = RxTest()
        rxt.rxTimerRoll()//rxCreateRollWithTime(5)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            //.filter { it > 10 }
            .subscribe(
                {
                    textView.text = "Next = ${it} (${counter++})"
                    println(it)
                },
                {
                    textView.text = "Error!!!"
                },
                {
                    //textView.text = "Complete!!!"
                    progressBar.visibility = View.INVISIBLE
                },
                {
                    //dispose
                    //it.dispose()
                }
            ).let(disposeBag::add)
    }

    private fun recyclerViewInit() {
        groupAdapter.setOnItemClickListener { item, view ->
            val select = groupAdapter.getAdapterPosition(item)
            if ((item as CharacterItem).character.select) {
                item.character.select = false
                view.setBackgroundColor(ContextCompat.getColor(context!!, R.color.primary))
            }
            else {
                item.character.select = true
                view.setBackgroundColor(ContextCompat.getColor(context!!, R.color.accent))

                if (currentSelect >= 0 && currentSelect != select){
                    val prevItem = groupAdapter.getGroupAtAdapterPosition(currentSelect) as CharacterItem
                    prevItem.character.select = false
                    prevItem.rootView.setBackgroundColor(ContextCompat.getColor(context!!, R.color.primary))
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
                        colorActive = ContextCompat.getColor(context!!, R.color.accent),
                        colorInactive = ContextCompat.getColor(context!!, R.color.primary_light),//todo move to val?
                        onClick = {
                            val bundle = Bundle()
                            bundle.putInt("id", it.data.id)
                            navController?.navigate(R.id.action_startFragment_to_characterFragment, bundle)
                        }
                    )
                )
            }
        }
    }

    private fun observeGetAllCharacters() {
        viewModel.getAllCharactersComplete.observe(this, Observer {
            addItems(it)
        })
    }

    private fun observeAddComplete() {
        viewModel.addComplete.observe(this, Observer {
            if (isfileAdd) {
                viewModel.getLastCharacterId()
                isfileAdd = false
            } else {
                Toast.makeText(activity, "added", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun observeGetLastCharacterIdComplete() {
        viewModel.getLastCharacterIdComplete.observe(this, Observer {
            parser.parse(it)
            newCharacter = parser.character
            viewModel.updateCharacter(newCharacter)
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
