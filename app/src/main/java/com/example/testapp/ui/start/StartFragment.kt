package com.example.testapp.ui.start

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testapp.R
import com.example.testapp.RxTest
import com.example.testapp.ui.SelectableData
import com.example.testapp.db.entity.Character
import com.example.testapp.getThemeColor
import com.example.testapp.ui.character.CharacterItem
import com.example.testapp.util.DataManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tbruyelle.rxpermissions2.RxPermissions
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
    private val dataManager: DataManager by inject()

    private var isFileAdd: Boolean = false
    private var isIntentAdd: Boolean = false
    private val requestCodeAddFromFile = 200
    private val disposeBag = CompositeDisposable()
    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    private var newCharacter = Character()
    private var newCharacterFilePath: String = ""
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
        viewModel.initStandartLibrary(
            resources.assets.open("Mentor_Skills.gcs"),
            resources.assets.open("AdvTest.gcs")
        )//todo move in appConstants

        button_rx.setOnClickListener { onClickRx() }
        button_add.setOnClickListener { onClickAdd() }
        button_delete.setOnClickListener { onClickDelete() }

        observeStandartSkillsLibraryLoadComplete()
        observeStandartAdvantageLibraryLoadComplete()
        observeGetAllCharacters()
        observeErrors()
        observeDeleteComplete()
        observeAddComplete()
        observeGetLastCharacterIdComplete()
        observeParseCharacterComplete()

        recyclerViewInit()
    }

    override fun onResume() {
        super.onResume()
        showProgressBar()
        viewModel.getAllCharacters()
        if (viewModel.getFileIntentAdd().isNotBlank())
        {
            isIntentAdd = true
            addFromFile(viewModel.getFileIntentAdd())
            viewModel.clearFileIntentAdd()
        }
    }

    override fun onStop() {
        super.onStop()
        disposeBag.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.forceClear()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestCodeAddFromFile && resultCode == RESULT_OK) {
            addFromFile(data?.data?.path ?: "")
        }
    }

    private fun addFromFile(path: String) {
        newCharacter = Character()
        viewModel.addCharacter(newCharacter)
        isFileAdd = true
        newCharacterFilePath = path
    }

    private fun onClickAdd() {
        val builder = MaterialAlertDialogBuilder(activity)
        builder
            .setTitle(R.string.add_new_character)
            .setMessage(R.string.add_new_character_message)
            .setPositiveButton(R.string.add_from_file) { dialogInterface: DialogInterface, i: Int ->

                RxPermissions(activity!!)
                    .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .subscribe {
                        if (it) {
                            val intent = Intent(Intent.ACTION_GET_CONTENT)
                            intent.type = "file/*"
                            startActivityForResult(intent, requestCodeAddFromFile)
                        } else {
                            Toast.makeText(activity, "permission denied", Toast.LENGTH_SHORT).show()
                        }
                        dialogInterface.dismiss()
                    }
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
        dataManager.appSettingsVault.clearSettings()//todo delete
        progressBar.visibility = View.VISIBLE
        val rxt = RxTest()
        rxt
//            .rxTimerRoll()
            .rxCreateRollWithTime(1)
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
                view.setBackgroundColor(activity!!.getThemeColor(R.attr.colorPrimaryVariant))
            }
            else {
                item.character.select = true
                view.setBackgroundColor(activity!!.getThemeColor(R.attr.colorSecondary))

                if (currentSelect >= 0 && currentSelect != select){
                    val prevItem = groupAdapter.getGroupAtAdapterPosition(currentSelect) as CharacterItem
                    prevItem.character.select = false
                    prevItem.rootView.setBackgroundColor(activity!!.getThemeColor(R.attr.colorPrimaryVariant))
                }
            }
            currentCharacter = item.character.data
            groupAdapter.notifyItemChanged(currentSelect)
            currentSelect = select
            groupAdapter.notifyItemChanged(currentSelect)
        }
        start_fragment_characters.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = groupAdapter
        }
    }

    private fun addItems(items: List<Character>)
    {
        val colorActive = activity!!.getThemeColor(R.attr.colorSecondary)
        val colorInactive = activity!!.getThemeColor(R.attr.colorPrimaryVariant)
        groupAdapter.clear()
        for(i in items)
        {
            groupAdapter.apply {
                add(
                    CharacterItem(
                        character = SelectableData(i),
                        colorActive = colorActive,
                        colorInactive = colorInactive,
                        onClick = {
                            openCharacter(it.data.id)
                        }
                    )
                )
            }
        }
    }

    private fun openCharacter(id: Int) {
        val bundle = Bundle()
        bundle.putInt("id", id)
        navController?.navigate(R.id.action_startFragment_to_characterFragment, bundle)
    }

    private fun observeGetAllCharacters() {
        viewModel.getAllCharactersComplete.observe(this, Observer {
            addItems(it)
            hideProgressBar()
        })
    }

    private fun observeStandartSkillsLibraryLoadComplete() {
        viewModel.standartSkillsLibraryLoadComplete.observe(this, Observer {
            if (it) {
                Toast.makeText(activity, "Skills lib loaded", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun observeStandartAdvantageLibraryLoadComplete() {
        viewModel.standartAdvantageLibraryLoadComplete.observe(this, Observer {
            if (it) {
                Toast.makeText(activity, "Advantages lib loaded", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun observeAddComplete() {
        viewModel.addComplete.observe(this, Observer {
            if (isFileAdd) {
                viewModel.getLastCharacterId()
                isFileAdd = false
            } else if(isIntentAdd) {
                isIntentAdd = false
                openCharacter(newCharacter.id)
            } else {
                Toast.makeText(activity, "added", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun observeGetLastCharacterIdComplete() {
        viewModel.getLastCharacterIdComplete.observe(this, Observer {
            viewModel.parseCharacter(it, newCharacterFilePath)
        })
    }

    private fun observeParseCharacterComplete() {
        viewModel.parseCharacterComplete.observe(this, Observer {
            newCharacter = it
            viewModel.updateCharacter(it)
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
        start_fragment_progress_bar.visibility = View.GONE
    }

    private fun showProgressBar() {
        start_fragment_progress_bar.visibility = View.VISIBLE
    }
}
