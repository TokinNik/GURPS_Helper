package com.example.testapp.ui.character.edit


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.example.testapp.R
import com.example.testapp.db.entity.Character
import kotlinx.android.synthetic.main.fragment_character_edit.*
import toothpick.Toothpick

class CharacterEditFragment : Fragment() {

    private var character: Character = Character()

    private var mode: String = "update"//need enum?

    private lateinit var viewModel: CharacterEditFragmentViewModel

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
        Toothpick.inject(this, Toothpick.openScope("APP"))

        viewModel = ViewModelProviders.of(this).get(CharacterEditFragmentViewModel::class.java)

        mode = arguments?.getString("mode", "update") ?: "update"
        if (mode == "update"){
            val id = arguments?.getInt("id", 0) ?: 0
            viewModel.getCharacterById(id)
            observeCharacterById()
        }

        button_cancel.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("id", character.id)
            val optionsBuilder = NavOptions.Builder()
            val options = optionsBuilder.setPopUpTo(R.id.startFragment, false).build()
            navController?.navigate(R.id.characterFragment, bundle, options)
        }

        button_accept.setOnClickListener {
            if (mode == "update") {
                onClickUpdate()
                val bundle = Bundle()
                bundle.putInt("id", character.id)
                navController?.navigate(R.id.characterFragment, bundle)
            } else {
                onClickAdd()
                navController?.navigateUp()
            }
        }
    }

    private fun observeCharacterById()
    {
        viewModel.characterById.observe(this, Observer {
            character = it
            setDataInFields(it)
        })
    }

    private fun onClickAdd() {
        viewModel.addCharacter(getCharacterFromFields())
    }

    private fun onClickUpdate() {
        viewModel.updateCharacter(getCharacterFromFields())
    }

    private fun setDataInFields(ch: Character) {
        textView_id.text = ch.id.toString()
        textView_name.setText(ch.name)
        textView_st.setText(ch.st.toString())
        textView_dx.setText(ch.dx.toString())
        textView_iq.setText(ch.iq.toString())
        textView_ht.setText(ch.ht.toString())
    }

    private fun getCharacterFromFields(): Character{
        return character.apply {
            name = textView_name.text.toString()
            st = textView_st.text.toString().toInt()
            dx = textView_dx.text.toString().toInt()
            iq = textView_iq.text.toString().toInt()
            ht = textView_ht.text.toString().toInt()
        }
    }
}
