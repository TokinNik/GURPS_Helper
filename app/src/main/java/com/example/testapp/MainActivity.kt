package com.example.testapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testapp.db.entity.Character
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.Observable
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.testapp.ui.character.CharacterItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_start.*
import toothpick.Toothpick

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel

    private val navController: NavController
        get() = Navigation
            .findNavController(this, R.id.nav_host_fragment)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Toothpick.inject(this, Toothpick.openScope("APP"))

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
    }
}
