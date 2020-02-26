package com.example.testapp.ui.character.edit


import android.os.Bundle
import android.text.BoringLayout
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.example.testapp.R
import com.example.testapp.custom_view.outline_corner.OutlineProviders
import com.example.testapp.db.entity.Character
import com.example.testapp.db.entity.Skill.Skill
import com.google.android.material.button.MaterialButton
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import kotlinx.android.synthetic.main.fragment_character_edit.*
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import toothpick.smoothie.viewmodel.installViewModelBinding

class CharacterEditFragment : Fragment() {

    private val viewModel: CharacterEditFragmentViewModel by inject()

    private var characterSkillList: List<Skill> = emptyList()
    private var character: Character = Character()
    private var mode: String = "update"//todo need enum?

    private val onSave: LiveData<Boolean>
        get() = onSaveEvent
    private var onSaveEvent: MutableLiveData<Boolean> = MutableLiveData()

    private val navController: NavController?
        get() = activity?.let { Navigation.findNavController(it, R.id.nav_host_fragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_character_edit, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_character_edit, menu)
        menu.findItem(R.id.menu_item_character_edit_accept)
            .actionView.findViewById<MaterialButton>(R.id.button_accept)
            .setOnClickListener {
                if (mode == "update"){
                    onClickUpdate()
                } else {
                    onClickAdd()
                    //navController?.navigateUp()
                }
            }
        menu.findItem(R.id.menu_item_character_edit_cancel)//todo delete
            .actionView.findViewById<MaterialButton>(R.id.button_cancel)
            .setOnClickListener {
                val bundle = Bundle()
                bundle.putInt("id", character.id)
                val optionsBuilder = NavOptions.Builder()
                val options = optionsBuilder.setPopUpTo(R.id.startFragment, false).build()
                navController?.navigate(R.id.action_characterEditFragment_to_characterFragment, bundle, options)
            }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scope = Toothpick.openScope("APP")
        scope.installViewModelBinding<CharacterEditFragmentViewModel>(this)
        scope.inject(this)

        viewModel.clearEvents()

        mode = arguments?.getString("mode", "update") ?: "update"
        if (mode == "update") {
            val id = arguments?.getInt("id", 0) ?: 0
            viewModel.setEditCharacterId(id)
        }

        character_edit_pager.adapter = ViewPagerCharacterEditAdapter(activity!!,
            Observable.create { emitter: ObservableEmitter<Boolean> ->
                onSave.observe(this, Observer {
                    emitter.onNext(it)
                    if (it)
                    {
                        emitter.onComplete()
                    }
                })
            })
        character_edit_pager.offscreenPageLimit = 4

        observeAddComplete()
        observeErrors()
        observeGetLastCharacterIdComplete()
        observeAddCharacterSkillsComplete()
        observeEditCharacter()
        observeEditCharacterSkills()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.forceClear()
    }

    private fun observeAddComplete() {
        viewModel.addCharacterComplete.observe(this, Observer {
            viewModel.getLastCharacterId()
        })
    }

    private fun observeGetLastCharacterIdComplete() {
        viewModel.getLastCharacterIdComplete.observe(this, Observer {
            viewModel.addCharacterSkills(characterSkillList, it)
        })
    }

    private fun observeAddCharacterSkillsComplete() {
        viewModel.addCharacterSkillsComplete.observe(this, Observer {
            if (mode == "add") {
                Toast.makeText(activity, "added", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "updated", Toast.LENGTH_SHORT).show()
            }
            onSaveEvent.value = false
            viewModel.clearEditCharacter()
        })
    }

    private fun observeErrors() {
        viewModel.error.observe(this, Observer {
            Toast.makeText(activity, "error", Toast.LENGTH_SHORT).show()
            println("ERROR!!! $it")
        })
    }

    private fun onClickAdd() {
        onSaveEvent.value = true
    }

    private fun onClickUpdate() {
        onSaveEvent.value = true
    }

    private fun observeEditCharacter() {
        viewModel.getEditCharacter().observe(this, Observer {
            character = it
            if (onSave.value == true)
                if(mode == "add") {
                    viewModel.addCharacter(character)
                } else {
                    viewModel.updateCharacter(character)
                }
        })
    }

    private fun observeEditCharacterSkills() {
        viewModel.getEditCharacterSkills().observe(this, Observer {
            characterSkillList = it
            if (onSave.value == true && mode == "update") {
                    viewModel.addCharacterSkills(characterSkillList, character.id)
            }
        })
    }
}
