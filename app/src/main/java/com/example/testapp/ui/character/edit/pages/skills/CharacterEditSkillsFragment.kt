package com.example.testapp.ui.character.edit.pages.skills


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testapp.R
import com.example.testapp.ui.SelectableData
import com.example.testapp.db.entity.Character
import com.example.testapp.db.entity.Skill.Skill
import com.example.testapp.ui.character.choiseskill.ChoiceSkillFragment
import com.example.testapp.ui.skill.SkillItem
import com.example.testapp.ui.skill.observe.single.SkillObserveSingleFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.page_character_edit_skills.*
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import toothpick.smoothie.viewmodel.installViewModelBinding

class CharacterEditSkillsFragment(private val onSave: Observable<Boolean>) : Fragment() {

    private val viewModel: CharacterEditSkillsFragmentViewModel by inject()

    private val groupAdapter = GroupAdapter<GroupieViewHolder>()
    private val compositeDisposable = CompositeDisposable()

    private var characterSkillList: List<Skill> = emptyList()

    private lateinit var character: Character


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.page_character_edit_skills, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scope = Toothpick.openScope("APP")
        scope.installViewModelBinding<CharacterEditSkillsFragmentViewModel>(this)
        scope.inject(this)

        viewModel.clearEvents()
        character = viewModel.getEditCharacter()
        if (character.id != 0) {
            viewModel.getCharacterSkillsById(character.id)
        } else {
            //todo place text with ("skills list id empty")
        }

        onSave.subscribe{
            if (it) viewModel.setEditSkills(characterSkillList)
        }.let(compositeDisposable::add)

        observeErrors()
        observeCharacterSkillsById()
        observeSkillByNames()

        initOnClick()
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
    }

    private fun initOnClick() {
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

    private fun initRecyclerView() {
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

    private fun observeCharacterSkillsById() {
        viewModel.characterSkillsByIdComplete.observe(this, Observer {
            viewModel.getSkillByNames(it)
        })
    }

    private fun observeSkillByNames() {
        viewModel.getSkillByNamesComplete.observe(this, Observer {
            characterSkillList = it
            setItems(it)
        })
    }

    private fun observeErrors() {
        viewModel.error.observe(this, Observer {
            Toast.makeText(activity, "error", Toast.LENGTH_SHORT).show()
            println("ERROR!!! $it")
        })
    }
}
