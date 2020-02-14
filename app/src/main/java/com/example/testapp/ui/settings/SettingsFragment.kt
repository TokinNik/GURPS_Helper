package com.example.testapp.ui.settings


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.lifecycle.Observer
import com.example.testapp.R
import kotlinx.android.synthetic.main.fragment_settings.*
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import toothpick.smoothie.viewmodel.installViewModelBinding

class SettingsFragment : Fragment() {

    private val viewModel: SettingsFragmentViewModel by inject()

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
        settings_fragment_night_theme.isChecked = viewModel.isNightTheme()
        settings_fragment_color_scheme_radio_group.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.radio_classic -> viewModel.setColorSchemeValue(ColorScheme.CLASSIC)
                R.id.radio_bright -> viewModel.setColorSchemeValue(ColorScheme.BRIGHT)
            }
        }
        settings_fragment_night_theme.setOnClickListener {
            if (viewModel.isNightTheme())
            {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
                viewModel.setNightTheme(false)
            } else {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
                viewModel.setNightTheme(true)
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
