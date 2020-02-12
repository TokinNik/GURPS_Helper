package com.example.testapp.ui.character.edit.pages.stats


import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.testapp.databinding.PageCharacterEditStatsBinding
import com.example.testapp.db.entity.Character
import com.example.testapp.ui.character.edit.StatCounterMinusButtonListener
import com.example.testapp.ui.character.edit.StatCounterPlusButtonListener
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.page_character_edit_stats.*
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import toothpick.smoothie.viewmodel.installViewModelBinding

class CharacterEditStatsFragment(val onSave: Observable<Boolean>) : Fragment() {

    private val viewModel: CharacterEditStatsFragmentViewModel by inject()
    private var isOtherCollapsed = true
    private val compositeDisposable = CompositeDisposable()
    private lateinit var character: Character
    private lateinit var characterEditBinding: PageCharacterEditStatsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        characterEditBinding = PageCharacterEditStatsBinding.inflate(inflater, container, false)
        characterEditBinding.onClickPlus =
            StatCounterPlusButtonListener(
                100
            )
        characterEditBinding.onClickMinus =
            StatCounterMinusButtonListener(
                0
            )
        return characterEditBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scope = Toothpick.openScope("APP")
        scope.installViewModelBinding<CharacterEditStatsFragmentViewModel>(this)
        scope.inject(this)

        viewModel.clearEvents()
        character = viewModel.getEditCharacter()
        if (character.id != 0){
            viewModel.getCharacterById(character.id)
        } else {
            characterEditBinding.character = character
        }

        onSave.subscribe{
            if (it) viewModel.setEditCharacter(character)
        }.let(compositeDisposable::add)

        observeCharacterById()
        observeErrors()

        initOnClick()
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    private fun initOnClick() {
        character_edit_collapse_other.setOnClickListener {
            if (isOtherCollapsed) {
                character_edit_other_3.visibility = View.VISIBLE//todo place it in single container
                character_edit_other_4.visibility = View.VISIBLE
                character_edit_other_5.visibility = View.VISIBLE
                character_edit_other_6.visibility = View.VISIBLE
            } else {
                character_edit_other_3.visibility = View.GONE
                character_edit_other_4.visibility = View.GONE
                character_edit_other_5.visibility = View.GONE
                character_edit_other_6.visibility = View.GONE
            }
            isOtherCollapsed = !isOtherCollapsed
        }
    }

    private fun observeCharacterById()
    {
        viewModel.getCharacterByIdComplete.observe(this, Observer {
            character = it
            characterEditBinding.character = character
            setDataInFields(it)
        })
    }

    private fun observeErrors() {
        viewModel.error.observe(this, Observer {
            Toast.makeText(activity, "error", Toast.LENGTH_SHORT).show()
            println("ERROR!!! $it")
        })
    }

    private fun setDataInFields(ch: Character) {
        val bytes = Base64.decode(ch.portrait, Base64.DEFAULT)
        val image = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        character_edit_image.setImageBitmap(image)
    }
}
