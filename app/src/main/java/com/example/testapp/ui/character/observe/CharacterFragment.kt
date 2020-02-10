package com.example.testapp.ui.character.observe


import android.annotation.SuppressLint
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
import androidx.navigation.Navigation
import com.example.testapp.R
import com.example.testapp.custom_view.outline_corner.OutlineProviders
import com.example.testapp.databinding.CardCharacterAllBinding
import com.example.testapp.databinding.FragmentCharacterBinding
import com.example.testapp.db.entity.Character
import com.example.testapp.db.entity.CharacterSkills
import com.example.testapp.db.entity.Skill.Skill
import com.example.testapp.ui.SelectableData
import com.example.testapp.ui.settings.ColorScheme
import com.example.testapp.ui.skill.SkillItem
import com.example.testapp.ui.skill.observe.single.SkillObserveSingleFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.card_character_all.*
import kotlinx.android.synthetic.main.fragment_character.*
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import toothpick.smoothie.viewmodel.installViewModelBinding

class CharacterFragment : Fragment() {

    private lateinit var character: Character
    private lateinit var characterSkills: List<CharacterSkills>
    private lateinit var characterBinding: FragmentCharacterBinding
    private val groupAdapter = GroupAdapter<GroupieViewHolder>()
    private var isInfoCollapsed = false

    private val viewModel: CharacterFragmentViewModel by inject()

    private val navController: NavController?
        get() = activity?.let { Navigation.findNavController(it, R.id.nav_host_fragment) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        characterBinding = FragmentCharacterBinding.inflate(inflater, container, false)
        return characterBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scope = Toothpick.openScope("APP")
        scope.installViewModelBinding<CharacterFragmentViewModel>(this)
        scope.inject(this)

        viewModel.clearEvents()
        viewModel.getColorScheme()

        character_card_pager.adapter = ViewPagerAdapter()
        character_card_pager.offscreenPageLimit = 4

        val radius = 32f
        character_card_pager.outlineProvider = OutlineProviders(radius, OutlineProviders.OutlineType.ROUND_RECT_TOP)
        character_card_pager.clipToOutline = true

        recyclerViewInit()

        observeColorScheme()
        observeCharacterById()
        observeErrors()
        observeDeleteComplete()
        observeCharacterSkillsById()
        observeSkillByName()
        observeSkillByNames()

        initOnClick()

        val id = arguments?.getInt("id", 0) ?: 0
        showProgressBar()
        viewModel.getCharacterById(id)
        viewModel.getCharacterSkillsById(id)
    }

    private fun initOnClick() {
        button_edit.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("id", character.id)
            navController?.navigate(R.id.action_characterFragment_to_characterEditFragment, bundle)
        }

        haracter_card_collapse_info.setOnClickListener {
            if (isInfoCollapsed) {
                isInfoCollapsed = false
                character_card_other_info.visibility = View.VISIBLE
            } else {
                isInfoCollapsed = true
                character_card_other_info.visibility = View.GONE
            }
        }
    }

    private fun recyclerViewInit() {
        groupAdapter.setOnItemClickListener { item, view ->
            val selectSkillDialog = SkillObserveSingleFragment((item as SkillItem).skill.data)
            selectSkillDialog.setTargetFragment(this, 1)
            selectSkillDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.dialogFragmentStyle)
            selectSkillDialog.show(fragmentManager!!, null)
        }

        (character_card_pager.adapter as ViewPagerAdapter).groupAdapter = groupAdapter
    }

    private fun setItems(items: List<Skill>)
    {
        groupAdapter.clear()

        for(i in items)
        {
            groupAdapter.apply {
                add(
                    SkillItem(
                        skill = SelectableData(i),
                        colorActive = ContextCompat.getColor(context!!, R.color.accent),
                        colorInactive = ContextCompat.getColor(context!!, R.color.primary_light)
                    )
                )
            }
        }
    }

    private fun observeColorScheme() {
        viewModel.colorScheme.observe(this, Observer {
            (character_card_pager.adapter as ViewPagerAdapter).schemeType = it
        })
    }

    private fun observeCharacterById() {
        viewModel.characterById.observe(this, Observer {
            character = it
            characterBinding.character = character
            setDataInFields(it)
        })
    }

    private fun observeSkillByName() {
        viewModel.getSkillByNameComplete.observe(this, Observer {
            println(it)
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
            setItems(it)
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

    @SuppressLint("SetTextI18n")
    private fun setDataInFields(ch: Character) {
        val bytes = Base64.decode(ch.portrait, Base64.DEFAULT)
        val image = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        character_card_image.setImageBitmap(image)
    }

    private fun hideProgressBar() {
        character_fagment_progress_bar.visibility = View.GONE
    }

    private fun showProgressBar() {
        character_fagment_progress_bar.visibility = View.VISIBLE
    }
}
