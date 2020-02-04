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
import com.example.testapp.ui.SelectableData
import com.example.testapp.db.entity.Character
import com.example.testapp.db.entity.Skill
import com.example.testapp.ui.character.choiseskill.ChoiceSkillFragment
import com.example.testapp.ui.skill.SkillItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import kotlinx.android.synthetic.main.fragment_character_edit.*
import kotlinx.android.synthetic.main.stat_counter.view.*
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import toothpick.smoothie.viewmodel.installViewModelBinding

class CharacterEditFragment : Fragment() {

    private var character: Character = Character()

    private var mode: String = "update"//need enum?

    private var currentSkill = Skill()

    private var currentSelect = -1

    private val viewModel: CharacterEditFragmentViewModel by inject()

    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    private val navController: NavController?
        get() = activity?.let { Navigation.findNavController(it, R.id.nav_host_fragment) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_character_edit, container, false)
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
        } else {
            setDataInFields(character)
        }


        observeAddComplete()
        observeCharacterById()
        observeUpdateComplete()
        observeErrors()
        observeSkillByIds()
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
            val selectSkillDialog = ChoiceSkillFragment(emptyList())
            selectSkillDialog.setTargetFragment(this, 1)
            selectSkillDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.dialogFragmentStyle)
            selectSkillDialog.onClickAccept = {
               // character.skills = it
               // viewModel.getSkillByIds(it) todo
                selectSkillDialog.dismiss()
            }
            selectSkillDialog.show(fragmentManager!!, null)
        }
        character_edit_st_counter.edit_stat_button_plus.setOnClickListener(StatCounterPlusButtonListener(character_edit_st_counter.edit_stat, 100))//???????? wtf todo something with it
        character_edit_st_counter.edit_stat_button_minus.setOnClickListener(StatCounterMinusButtonListener(character_edit_st_counter.edit_stat, 1))
        character_edit_dx_counter.edit_stat_button_plus.setOnClickListener(StatCounterPlusButtonListener(character_edit_dx_counter.edit_stat, 100))
        character_edit_dx_counter.edit_stat_button_minus.setOnClickListener(StatCounterMinusButtonListener(character_edit_dx_counter.edit_stat, 1))
        character_edit_iq_counter.edit_stat_button_plus.setOnClickListener(StatCounterPlusButtonListener(character_edit_iq_counter.edit_stat, 100))
        character_edit_iq_counter.edit_stat_button_minus.setOnClickListener(StatCounterMinusButtonListener(character_edit_iq_counter.edit_stat, 1))
        character_edit_ht_counter.edit_stat_button_plus.setOnClickListener(StatCounterPlusButtonListener(character_edit_ht_counter.edit_stat, 100))
        character_edit_ht_counter.edit_stat_button_minus.setOnClickListener(StatCounterMinusButtonListener(character_edit_ht_counter.edit_stat, 1))
        character_edit_hp_counter.edit_stat_button_plus.setOnClickListener(StatCounterPlusButtonListener(character_edit_hp_counter.edit_stat, 100))
        character_edit_hp_counter.edit_stat_button_minus.setOnClickListener(StatCounterMinusButtonListener(character_edit_hp_counter.edit_stat, 0))
        character_edit_move_counter.edit_stat_button_plus.setOnClickListener(StatCounterPlusButtonListener(character_edit_move_counter.edit_stat, 100))
        character_edit_move_counter.edit_stat_button_minus.setOnClickListener(StatCounterMinusButtonListener(character_edit_move_counter.edit_stat, 0))
        character_edit_speed_counter.edit_stat_button_plus.setOnClickListener(StatCounterPlusButtonListener(character_edit_speed_counter.edit_stat, 100))
        character_edit_speed_counter.edit_stat_button_minus.setOnClickListener(StatCounterMinusButtonListener(character_edit_speed_counter.edit_stat, 0))
        character_edit_will_counter.edit_stat_button_plus.setOnClickListener(StatCounterPlusButtonListener(character_edit_will_counter.edit_stat, 100))
        character_edit_will_counter.edit_stat_button_minus.setOnClickListener(StatCounterMinusButtonListener(character_edit_will_counter.edit_stat, 0))
        character_edit_per_counter.edit_stat_button_plus.setOnClickListener(StatCounterPlusButtonListener(character_edit_per_counter.edit_stat, 100))
        character_edit_per_counter.edit_stat_button_minus.setOnClickListener(StatCounterMinusButtonListener(character_edit_per_counter.edit_stat, 0))
        character_edit_fp_counter.edit_stat_button_plus.setOnClickListener(StatCounterPlusButtonListener(character_edit_fp_counter.edit_stat, 100))
        character_edit_fp_counter.edit_stat_button_minus.setOnClickListener(StatCounterMinusButtonListener(character_edit_fp_counter.edit_stat, 0))

    }

    private fun initRecyclerView(){
        groupAdapter.setOnItemClickListener { item, view ->
            val select = groupAdapter.getAdapterPosition(item)
            if ((item as SkillItem).skill.select) {
                item.skill.select = false
                view.setBackgroundColor(ContextCompat.getColor(context!!, R.color.primary))
            }
            else {
                item.skill.select = true
                view.setBackgroundColor(ContextCompat.getColor(context!!, R.color.accent))

                if (currentSelect >= 0 && currentSelect != select){
                    val prevItem = groupAdapter.getGroupAtAdapterPosition(0).getItem(currentSelect) as SkillItem
                    prevItem.skill.select = false
                    prevItem.rootView.setBackgroundColor(ContextCompat.getColor(context!!, R.color.primary))
                }
            }
            currentSkill = item.skill.data
            groupAdapter.notifyItemChanged(currentSelect)
            currentSelect = select
            groupAdapter.notifyItemChanged(currentSelect)
        }
        recyclerView_skills.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = groupAdapter
        }
    }

    private fun observeCharacterById()
    {
        viewModel.characterById.observe(this, Observer {
            character = it
            setDataInFields(it)
        })
    }

    private fun observeSkillByIds()
    {
        viewModel.skillByIds.observe(this, Observer {
            groupAdapter.clear()
            val section = Section()
            for (item in it) {
                section.add(
                    SkillItem(
                        skill = SelectableData(item),
                        colorActive = ContextCompat.getColor(context!!, R.color.accent),
                        colorInactive = ContextCompat.getColor(context!!, R.color.primary)
                    )
                )
            }
            groupAdapter.add(section)
        })
    }

    private fun observeUpdateComplete() {
        viewModel.updateComplete.observe(this, Observer {
            Toast.makeText(activity, "updated", Toast.LENGTH_SHORT).show()
        })
    }
    private fun observeAddComplete() {
        viewModel.addComplete.observe(this, Observer {
            Toast.makeText(activity, "added", Toast.LENGTH_SHORT).show()
        })
    }

    private fun observeErrors() {
        viewModel.error.observe(this, Observer {
            Toast.makeText(activity, "error", Toast.LENGTH_SHORT).show()
            println("ERROR!!! $it")
        })
    }

    private fun onClickAdd() {
        viewModel.addCharacter(getCharacterFromFields())
    }

    private fun onClickUpdate() {
        viewModel.updateCharacter(getCharacterFromFields())
    }

    private fun setDataInFields(ch: Character) {
        character_edit_id.text = ch.id.toString()//todo and this too
        character_edit_name.setText(ch.name)
        character_edit_player_name.setText(ch.playerName)
        character_edit_world.setText(ch.world)
        character_edit_tl.setText(ch.tl)
        character_edit_age.setText(ch.age)
        character_edit_eye.setText(ch.eyes)
        character_edit_hairs.setText(ch.hairs)
        character_edit_skin.setText(ch.skin)
        character_edit_height.setText(ch.height)
        character_edit_weight.setText(ch.weight)
        character_edit_gender.setText(ch.gender)
        character_edit_race.setText(ch.race)
        character_edit_sm.setText(ch.sm)
        character_edit_st_counter.edit_stat.setText(ch.st.toString())
        character_edit_dx_counter.edit_stat.setText(ch.dx.toString())
        character_edit_iq_counter.edit_stat.setText(ch.iq.toString())
        character_edit_ht_counter.edit_stat.setText(ch.ht.toString())
        character_edit_hp_counter.edit_stat.setText(ch.hp.toString())
        character_edit_move_counter.edit_stat.setText(ch.move.toString())
        character_edit_speed_counter.edit_stat.setText(ch.speed.toString())
        character_edit_will_counter.edit_stat.setText(ch.will.toString())
        character_edit_per_counter.edit_stat.setText(ch.per.toString())
        character_edit_fp_counter.edit_stat.setText(ch.fp.toString())
        val bytes = Base64.decode(ch.portrait, Base64.DEFAULT)
        val image = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        character_edit_image.setImageBitmap(image)

        //viewModel.getSkillByIds(ch.skills)
//        viewModel.getAllSkills()
    }

    private fun getCharacterFromFields(): Character{
        return character.apply {
            name = character_edit_name.text.toString()//todo and this too
            world = character_edit_world.text.toString()
            tl = character_edit_tl.text.toString()
            age = character_edit_age.text.toString()
            eyes = character_edit_eye.text.toString()
            hairs = character_edit_hairs.text.toString()
            skin = character_edit_skin.text.toString()
            height = character_edit_height.text.toString()
            weight = character_edit_weight.text.toString()
            gender = character_edit_gender.text.toString()
            race = character_edit_race.text.toString()
            sm = character_edit_sm.text.toString()
            st = character_edit_st_counter.edit_stat.text.toString().toInt()
            dx = character_edit_dx_counter.edit_stat.text.toString().toInt()
            iq = character_edit_iq_counter.edit_stat.text.toString().toInt()
            ht = character_edit_ht_counter.edit_stat.text.toString().toInt()
            hp = character_edit_hp_counter.edit_stat.text.toString().toInt()
            move = character_edit_move_counter.edit_stat.text.toString().toInt()
            speed = character_edit_speed_counter.edit_stat.text.toString().toInt()
            will = character_edit_will_counter.edit_stat.text.toString().toInt()
            per = character_edit_per_counter.edit_stat.text.toString().toInt()
            fp = character_edit_fp_counter.edit_stat.text.toString().toInt()
        }
    }
}
