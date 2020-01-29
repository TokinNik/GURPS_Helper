package com.example.testapp.ui.character.observe


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.testapp.R
import com.example.testapp.db.entity.Character
import kotlinx.android.synthetic.main.card_character_all.*
import kotlinx.android.synthetic.main.fragment_character.*
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import toothpick.smoothie.viewmodel.installViewModelBinding

class CharacterFragment : Fragment() {

    private lateinit var character: Character

    private val viewModel: CharacterFragmentViewModel by inject()

    private val navController: NavController?
        get() = activity?.let { Navigation.findNavController(it, R.id.nav_host_fragment) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_character, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scope = Toothpick.openScope("APP")
        scope.installViewModelBinding<CharacterFragmentViewModel>(this)
        scope.inject(this)

        viewModel.clearEvents()

        observeCharacterById()
        observeErrors()
        observeDeleteComplete()

        initOnClick()

        val id = arguments?.getInt("id", 0) ?: 0
        viewModel.getCharacterById(id)
    }
    private fun initOnClick()
    {
        button_edit.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("id", character.id)
            navController?.navigate(R.id.action_characterFragment_to_characterEditFragment, bundle)
        }
    }

    private fun observeCharacterById()
    {
        viewModel.characterById.observe(this, Observer {
            character = it
            setDataInFields(it)
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

    private fun setDataInFields(ch: Character) {
        character_card_id.text = ch.id.toString()
        character_card_name.text = ch.name
        character_card_st.text = ch.st.toString()
        character_card_dx.text = ch.dx.toString()
        character_card_iq.text = ch.iq.toString()
        character_card_ht.text = ch.ht.toString()
        character_card_hp.text = ch.hp.toString()
        character_card_move.text = ch.move.toString()
        character_card_speed.text = ch.speed.toString()
        character_card_will.text = ch.will.toString()
        character_card_per.text = ch.per.toString()
        character_card_fp.text = ch.fp.toString()
        textView_skills.text = ch.skills.toString()
    }
}
