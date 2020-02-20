package com.example.testapp.ui.character.observe


import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.testapp.R
import com.example.testapp.custom_view.outline_corner.OutlineProviders
import com.example.testapp.databinding.FragmentCharacterBinding
import com.example.testapp.db.entity.Character
import com.example.testapp.db.entity.Skill.Skill
import com.example.testapp.getThemeColor
import com.example.testapp.ui.SelectableData
import com.example.testapp.ui.character.CharacterCard
import com.example.testapp.ui.character.edit.pages.BindingCharacter
import com.example.testapp.ui.skill.SkillItem
import com.example.testapp.ui.skill.observe.single.SkillObserveSingleFragment
import com.example.testapp.util.GCSXmlBuilder
import com.example.testapp.util.GurpsCalculations
import com.google.android.material.button.MaterialButton
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.card_character_all.*
import kotlinx.android.synthetic.main.fragment_character.*
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import toothpick.smoothie.viewmodel.installViewModelBinding

class CharacterFragment : Fragment() {

    private lateinit var character: Character
    private lateinit var characterBinding: FragmentCharacterBinding
    private lateinit var characterCard: CharacterCard
    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    private val viewModel: CharacterFragmentViewModel by inject()

    private val gurpsCalculations: GurpsCalculations by inject()

    private val navController: NavController?
        get() = activity?.let {
            Navigation.findNavController(it, R.id.nav_host_fragment)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        characterBinding = FragmentCharacterBinding.inflate(inflater, container, false)
        return characterBinding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_character, menu)
        menu.findItem(R.id.menu_item_character_edit)
            .actionView.findViewById<MaterialButton>(R.id.button_edit)
            .setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("id", character.id)
            navController?.navigate(R.id.action_characterFragment_to_characterEditFragment, bundle)
        }
        menu.findItem(R.id.menu_item_character_export)
            .actionView.findViewById<MaterialButton>(R.id.button_export)
            .setOnClickListener {
                val xmlb = GCSXmlBuilder()
                var skills = mutableListOf<Skill>()
                for (i in 0 until groupAdapter.itemCount){
                    skills.add((groupAdapter.getItem(i) as SkillItem).skill.data)
                }
                xmlb.saveInFile(character.name, xmlb.xmlTest(character, skills.toList()).toString())
                Toast.makeText(activity, "exported in file ${character.name}.gcs", Toast.LENGTH_SHORT).show()
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scope = Toothpick.openScope("APP")
        scope.installViewModelBinding<CharacterFragmentViewModel>(this)
        scope.inject(this)

        viewModel.clearEvents()
        viewModel.getColorScheme()

        characterCard = CharacterCard(
            cardRoot = character_card_root,
            groupAdapter = groupAdapter,
            colorActive = activity!!.getThemeColor(R.attr.colorSecondary),
            colorInactive = activity!!.getThemeColor(R.attr.colorPrimaryVariant),
            onSkillClick = {
                setSkillInfoDialog(it)
            }
        )

        observeColorScheme()
        observeCharacterById()
        observeErrors()
        observeDeleteComplete()
        observeCharacterSkillsById()
        observeSkillByNames()

        val id = arguments?.getInt("id", 0) ?: 0
        showProgressBar()
        viewModel.getCharacterById(id)
        viewModel.getCharacterSkillsById(id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.forceClear()
    }

    private fun setSkillInfoDialog(skill: Skill) {
        val selectSkillDialog = SkillObserveSingleFragment(skill)
        selectSkillDialog.setTargetFragment(this, 1)
        selectSkillDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.dialogFragmentStyle)
        selectSkillDialog.show(fragmentManager!!, null)
    }

    private fun observeColorScheme() {
        viewModel.colorScheme.observe(this, Observer {
            characterCard.setColorScheme(it)
        })
    }

    private fun observeCharacterById() {
        viewModel.characterById.observe(this, Observer {
            character = it
            characterBinding.character = gurpsCalculations.getReMathCharacter(character)
            characterCard.setImage(it.portrait)
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
            characterCard.setItems(it)
            hideProgressBar()
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
            println("ERROR!!! ${it.printStackTrace()}")
        })
    }

    private fun hideProgressBar() {
        character_fagment_progress_bar.visibility = View.GONE
    }

    private fun showProgressBar() {
        character_fagment_progress_bar.visibility = View.VISIBLE
    }
}
