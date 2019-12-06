package com.example.testapp.ui.character.observe


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.testapp.R
import com.example.testapp.db.entity.Character
import kotlinx.android.synthetic.main.fragment_character.*
import toothpick.Toothpick

class CharacterFragment : Fragment() {

    private lateinit var character: Character

    private lateinit var viewModel: CharacterFragmentViewModel

    private val navController: NavController?
        get() = activity?.let { Navigation.findNavController(it, R.id.nav_host_fragment) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_character, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Toothpick.inject(this, Toothpick.openScope("APP"))

        viewModel = ViewModelProviders.of(this).get(CharacterFragmentViewModel::class.java)

        button_edit.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("id", character.id)
            navController?.navigate(R.id.action_characterFragment_to_characterEditFragment, bundle)
        }

        observeCharacterById()

        val id = arguments?.getInt("id", 0) ?: 0
        viewModel.getCharacterById(id)
    }

    private fun observeCharacterById()
    {
        viewModel.characterById.observe(this, Observer {
            character = it
            setDataInFields(it)
        })
    }

    private fun setDataInFields(ch: Character) {
        textView_id.text = ch.id.toString()
        textView_name.setText(ch.name)
        textView_st.setText(ch.st.toString())
        textView_dx.setText(ch.dx.toString())
        textView_iq.setText(ch.iq.toString())
        textView_ht.setText(ch.ht.toString())
    }
}
