package com.example.testapp.ui.battle.actions.attack_melee

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.example.testapp.R
import com.example.testapp.databinding.FragmentBattleActionAttackMeleeBinding
import com.example.testapp.ui.character.edit.StatCounterIntMinusButtonListener
import com.example.testapp.ui.character.edit.StatCounterIntPlusButtonListener
import com.example.testapp.util.RollUtil
import kotlinx.android.synthetic.main.fragment_battle_action_attack_melee.*
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import toothpick.smoothie.viewmodel.installViewModelBinding

class BattleActionAttackMeleeFragment : DialogFragment() {

    private val viewModel: BattleActionAttackMeleeFragmentViewModel by inject()
    private val rollUtil: RollUtil by inject()

    private lateinit var binding: FragmentBattleActionAttackMeleeBinding

    private var actionAttackMeleeData = ActionAttackMeleeData{ binding.invalidateAll() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.setTitle(R.string.attack)
        binding = FragmentBattleActionAttackMeleeBinding.inflate(inflater, container, false)
        binding.meleeAttackData = actionAttackMeleeData
        binding.onClickPlus = StatCounterIntPlusButtonListener(100)
        binding.onClickMinus = StatCounterIntMinusButtonListener(-100)
        binding.onClickPlusZero = StatCounterIntPlusButtonListener(0)
        binding.onClickMinusZero = StatCounterIntMinusButtonListener(0)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val scope = Toothpick.openScope("APP")
        scope.installViewModelBinding<BattleActionAttackMeleeFragmentViewModel>(this)
        scope.inject(this)

        viewModel.clearEvents()

        observeErrors()

        initOnClick()

        hideKeyboard()
    }

    override fun onResume() {
        super.onResume()
        hideKeyboard()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.forceClear()
    }

    private fun initOnClick() {
        battle_action_attack_roll.setOnClickListener {
            val rollValue = rollUtil.roll3D6()
            Toast.makeText(activity, "Roll on(${actionAttackMeleeData.result}) Result = $rollValue (${rollValue <= actionAttackMeleeData.result || rollValue == 3})", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeErrors() {
        viewModel.error.observe(this, Observer {
            Toast.makeText(activity, "error", Toast.LENGTH_SHORT).show()
            println("ERROR!!! $it")
        })
    }

    private fun hideKeyboard()
    {
        (activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(battle_action_attack_mele_st.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}
