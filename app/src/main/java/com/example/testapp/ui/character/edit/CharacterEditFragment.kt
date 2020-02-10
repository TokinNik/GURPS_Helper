package com.example.testapp.ui.character.edit


import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testapp.R
import com.example.testapp.databinding.FragmentCharacterEditBinding
import com.example.testapp.databinding.StatCounterBinding
import com.example.testapp.ui.SelectableData
import com.example.testapp.db.entity.Character
import com.example.testapp.db.entity.Skill.Skill
import com.example.testapp.ui.character.choiseskill.ChoiceSkillFragment
import com.example.testapp.ui.skill.SkillItem
import com.example.testapp.ui.skill.observe.single.SkillObserveSingleFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import kotlinx.android.synthetic.main.fragment_character_edit.*
import kotlinx.android.synthetic.main.stat_counter.view.*
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import toothpick.smoothie.viewmodel.installViewModelBinding

class CharacterEditFragment : Fragment() {

    private var characterSkillList: List<Skill> = emptyList()
    private var character: Character = Character()
    private var mode: String = "update"//todo need enum?
    private val viewModel: CharacterEditFragmentViewModel by inject()

    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    private lateinit var characterEditBinding: FragmentCharacterEditBinding

    private val navController: NavController?
        get() = activity?.let { Navigation.findNavController(it, R.id.nav_host_fragment) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        characterEditBinding = FragmentCharacterEditBinding.inflate(inflater, container, false)
        characterEditBinding.onClickPlus = StatCounterPlusButtonListener(100)
        characterEditBinding.onClickMinus = StatCounterMinusButtonListener(0)
        return characterEditBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scope = Toothpick.openScope("APP")
        scope.installViewModelBinding<CharacterEditFragmentViewModel>(this)
        scope.inject(this)

        viewModel.clearEvents()

        mode = arguments?.getString("mode", "update") ?: "update"
        if (mode == "update"){
            val id = arguments?.getInt("id", 0) ?: 0
            viewModel.getCharacterById(id)
            viewModel.getCharacterSkillsById(id)
        } else {
            characterEditBinding.character = character
            setDataInFields(character)
        }

        observeAddComplete()
        observeCharacterById()
        observeUpdateComplete()
        observeErrors()
        observeCharacterSkillsById()
        observeSkillByNames()
        observeGetLastCharacterIdComplete()
        observeAddCharacterSkillsComplete()

        initOnClick()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
    }

    private fun initOnClick()
    {
        button_cancel.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("id", character.id)
            val optionsBuilder = NavOptions.Builder()
            val options = optionsBuilder.setPopUpTo(R.id.startFragment, false).build()
            navController?.navigate(R.id.characterFragment, bundle, options)
        }

        button_accept.setOnClickListener {
            if (mode == "update"){
                onClickUpdate()
            } else {
                onClickAdd()
                //navController?.navigateUp()
            }
        }

        button_add_skill.setOnClickListener {
            val selectSkillDialog = ChoiceSkillFragment(characterSkillList)
            selectSkillDialog.setTargetFragment(this, 1)
            selectSkillDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.dialogFragmentStyle)
            selectSkillDialog.onClickAccept = {
                selectSkillDialog.dismiss()
                characterSkillList = it
                setItems(it)
            }
            selectSkillDialog.show(fragmentManager!!, null)
        }
    }

    private fun initRecyclerView(){
        groupAdapter.setOnItemClickListener { item, view ->
            val selectSkillDialog = SkillObserveSingleFragment((item as SkillItem).skill.data)
            selectSkillDialog.setTargetFragment(this, 1)
            selectSkillDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.dialogFragmentStyle)
            selectSkillDialog.show(fragmentManager!!, null)
        }
        recyclerView_skills.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = groupAdapter
        }
    }

    private fun setItems(skillsList: List<Skill>) {
        groupAdapter.clear()
        val section = Section()
        for (item in skillsList) {
            section.add(
                SkillItem(
                    skill = SelectableData(item),
                    colorActive = ContextCompat.getColor(context!!, R.color.accent),
                    colorInactive = ContextCompat.getColor(context!!, R.color.primary_light)
                )
            )
        }
        groupAdapter.add(section)
    }

    private fun observeCharacterById()
    {
        viewModel.getCharacterByIdComplete.observe(this, Observer {
            character = it
            characterEditBinding.character = character
            setDataInFields(it)
        })
    }

    private fun observeCharacterSkillsById() {
        viewModel.characterSkillsByIdComplete.observe(this, Observer {
            viewModel.getSkillByNames(it)
        })
    }

    private fun observeSkillByNames()
    {
        viewModel.getSkillByNamesComplete.observe(this, Observer {
            characterSkillList = it
            setItems(it)
        })
    }

    private fun observeUpdateComplete() {
        viewModel.updateCharacterComplete.observe(this, Observer {
            viewModel.addCharacterSkills(characterSkillList, character.id)
        })
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
        })
    }

    private fun observeErrors() {
        viewModel.error.observe(this, Observer {
            Toast.makeText(activity, "error", Toast.LENGTH_SHORT).show()
            println("ERROR!!! $it")
        })
    }

    private fun onClickAdd() {
        viewModel.addCharacter(character)
    }

    private fun onClickUpdate() {
        viewModel.updateCharacter(character)
    }

    private fun setDataInFields(ch: Character) {
        val bytes = Base64.decode(ch.portrait, Base64.DEFAULT)
        val image = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        character_edit_image.setImageBitmap(image)
    }
}
