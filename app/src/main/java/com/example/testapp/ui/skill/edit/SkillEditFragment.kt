package com.example.testapp.ui.skill.edit


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
import kotlinx.android.synthetic.main.fragment_skill_edit.*
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import toothpick.smoothie.viewmodel.installViewModelBinding

class SkillEditFragment : Fragment() {

    private var mode: String = "update"//need enum?

    private var skill = Skill()

    private val viewModel: SkillEditFragmentViewModel by inject()


    private val navController: NavController?
        get() = activity?.let { Navigation.findNavController(it, R.id.nav_host_fragment) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_skill_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scope = Toothpick.openScope("APP")
        scope.installViewModelBinding<SkillEditFragmentViewModel>(this)
        scope.inject(this)

        viewModel.clearEvents()

        mode = arguments?.getString("mode", "update") ?: "update"
        if (mode == "update"){
            val id = arguments?.getInt("id", 0) ?: 0
            viewModel.getSkillById(id)
            observeSkillById()
            observeUpdateComplete()
        } else {
            observeAddComplete()
        }

        observeErrors()
        initOnClick()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.forceClear()
    }

    private fun initOnClick()
    {
        button_cancel.setOnClickListener {
            navController?.navigateUp()
        }

        button_accept.setOnClickListener {
            if (mode == "update") onClickUpdate() else onClickAdd()
            //navController?.navigateUp()
        }
    }

    private fun observeSkillById()
    {
        viewModel.skillById.observe(this, Observer {
            skill = it
            setDataInFields(it)
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
        viewModel.addSkill(getSkillFromFields())
    }

    private fun onClickUpdate() {
        viewModel.updateSkill(getSkillFromFields())
    }

    private fun setDataInFields(skill: Skill) {
        item_skill_id.text = skill.id.toString()
        editText_skill_name.setText(skill.name)
    }

    private fun getSkillFromFields(): Skill {
        return skill.apply {
            name = editText_skill_name.text.toString()
        }
    }
}
