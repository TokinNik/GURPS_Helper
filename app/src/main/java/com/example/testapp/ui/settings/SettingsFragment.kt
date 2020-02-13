package com.example.testapp.ui.settings


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
import com.example.testapp.db.entity.Skill.Skill
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_skill_all.*
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import toothpick.smoothie.viewmodel.installViewModelBinding

class SettingsFragment : Fragment() {

    private val viewModel: SettingsFragmentViewModel by inject()

    private val navController: NavController?
        get() = activity?.let { Navigation.findNavController(it, R.id.nav_host_fragment) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val scope = Toothpick.openScope("APP")
        scope.installViewModelBinding<SettingsFragmentViewModel>(this)
        scope.inject(this)

        viewModel.clearEvents()

        observeErrors()

        initOnClick()
    }

    private fun initOnClick() {
        when(viewModel.getColorScheme()) {
            ColorScheme.CLASSIC -> radio_classic.isChecked = true
            ColorScheme.BRIGHT -> radio_bright.isChecked = true
        }
        settings_fragment_color_scheme_radio_group.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.radio_classic -> viewModel.setColorSchemeValue(ColorScheme.CLASSIC)
                R.id.radio_bright -> viewModel.setColorSchemeValue(ColorScheme.BRIGHT)
            }
        }
    }

    private fun observeErrors() {
        viewModel.error.observe(this, Observer {
            Toast.makeText(activity, "error", Toast.LENGTH_SHORT).show()
            println("ERROR!!! $it")
        })
    }
}
