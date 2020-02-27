package com.example.testapp.ui.character.edit.pages.stats


import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.testapp.R
import com.example.testapp.databinding.PageCharacterEditStatsBinding
import com.example.testapp.db.entity.Character
import com.example.testapp.ui.character.edit.StatCounterFloatMinusButtonListener
import com.example.testapp.ui.character.edit.StatCounterFloatPlusButtonListener
import com.example.testapp.ui.character.edit.StatCounterIntMinusButtonListener
import com.example.testapp.ui.character.edit.StatCounterIntPlusButtonListener
import com.example.testapp.ui.character.edit.pages.BindingCharacter
import com.example.testapp.util.Base64RequestHandler
import com.example.testapp.util.GurpsCalculations
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.item_character.view.*
import kotlinx.android.synthetic.main.page_character_edit_stats.*
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import toothpick.smoothie.viewmodel.installViewModelBinding

class CharacterEditStatsFragment(val onSave: Observable<Boolean>) : Fragment() {

    private val viewModel: CharacterEditStatsFragmentViewModel by inject()
    private val gurpsCalculations: GurpsCalculations by inject()
    private var isOtherCollapsed = true
    private val compositeDisposable = CompositeDisposable()
    private lateinit var bindCharacter: BindingCharacter
    private lateinit var character: Character
    private lateinit var characterEditBinding: PageCharacterEditStatsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        characterEditBinding = PageCharacterEditStatsBinding.inflate(inflater, container, false)
        characterEditBinding.onClickPlus = StatCounterIntPlusButtonListener(100)
        characterEditBinding.onClickMinus = StatCounterIntMinusButtonListener(0)
        characterEditBinding.onClickPlusFloat = StatCounterFloatPlusButtonListener(100f, 0.25f)
        characterEditBinding.onClickMinusFloat = StatCounterFloatMinusButtonListener(0f, 0.25f)
        return characterEditBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scope = Toothpick.openScope("APP")
        scope.installViewModelBinding<CharacterEditStatsFragmentViewModel>(this)
        scope.inject(this)

        viewModel.clearEvents()
        character = viewModel.getEditCharacter()
        bindCharacter = BindingCharacter(character) {characterEditBinding.invalidateAll()}
        if (character.id != 0){
            viewModel.getCharacterById(character.id)
        } else {
            characterEditBinding.character = character
            characterEditBinding.bindCharacter = bindCharacter
        }

        onSave.subscribe{
            copyCharacterBindParam()
            if (it) viewModel.setEditCharacter(character)
        }.let(compositeDisposable::add)

        observeCharacterById()
        observeErrors()

        initOnClick()
    }

    private fun copyCharacterBindParam() {
        character.apply {
            st = bindCharacter.st
            dx = bindCharacter.dx
            iq = bindCharacter.iq
            ht = bindCharacter.ht
            hp = bindCharacter.realHp
            per = bindCharacter.realPer
            will = bindCharacter.realWill
            fp = bindCharacter.realFp
            move = bindCharacter.realMove
            speed = bindCharacter.realSpeed
            totalPoints = bindCharacter.totalPoints
            earnPoints = bindCharacter.earnPoints
        }
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.forceClear()
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
            bindCharacter = BindingCharacter(character) {characterEditBinding.invalidateAll()}
            characterEditBinding.character = character
            characterEditBinding.bindCharacter = bindCharacter
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
        Picasso
            .get()
            .load("${Base64RequestHandler.BASE_64_SCHEME}${ch.portrait}")
            .placeholder(R.drawable.gm_logo_original)
            .error(R.drawable.gm_logo_original)
            .into(activity!!.findViewById<ImageView>(R.id.character_edit_image))
    }
}
