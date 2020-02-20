package com.example.testapp.ui.battle.actions.attack_melee

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.example.testapp.R
import com.example.testapp.ui.battle.actions.attack_range.BattleActionAttackRangeFragmentViewModel
import kotlinx.android.synthetic.main.fragment_battle_action_attack_melee.*
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import toothpick.smoothie.viewmodel.installViewModelBinding

class BattleActionAttackMeleeFragment : DialogFragment() {

    private val viewModelRange: BattleActionAttackRangeFragmentViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.setTitle(R.string.attack)
        return inflater.inflate(R.layout.fragment_battle_action_attack_melee, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val scope = Toothpick.openScope("APP")
        scope.installViewModelBinding<BattleActionAttackRangeFragmentViewModel>(this)
        scope.inject(this)

        viewModelRange.clearEvents()

        observeErrors()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModelRange.forceClear()
    }

    private fun initOnClick() {

    }

    private fun observeErrors() {
        viewModelRange.error.observe(this, Observer {
            Toast.makeText(activity, "error", Toast.LENGTH_SHORT).show()
            println("ERROR!!! $it")
        })
    }
}
