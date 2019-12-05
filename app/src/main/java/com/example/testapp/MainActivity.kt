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
import com.example.testapp.ui.CharacterItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import toothpick.Toothpick
import toothpick.ktp.delegate.inject

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel
    private var currentCharacter = 1
    private var maxCharacter = 1
    lateinit var textView: TextView
    private val groupAdapter = GroupAdapter<GroupieViewHolder>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Toothpick.inject(this, Toothpick.openScope("APP"))

        button_rx.setOnClickListener { onClickRx() }
        button_next.setOnClickListener { onClickNext() }
        button_prev.setOnClickListener { onClickPrev() }
        button_add.setOnClickListener { onClickAdd() }
        button_update.setOnClickListener { onClickUpdate() }
        button_delete.setOnClickListener { onClickDelete() }
        button_clear.setOnClickListener { clearFields() }
        textView = findViewById(R.id.textView)

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)

        observeCharacters()
        observeCharacterById()

        recyclerViewInit()
    }

    private fun onClickAdd() {
        viewModel.addCharacter(getCurrentCharacter())
        updateItems()
    }

    private fun onClickUpdate() {
        val character = getCurrentCharacter()
        character.id = currentCharacter
        viewModel.updateCharacter(character)
    }

    private fun onClickDelete() {
        val character = getCurrentCharacter()
        character.id = currentCharacter
        viewModel.deleteCharacter(character)
        updateItems()
        clearFields()
    }

    private fun recyclerViewInit() {
        recyclerView_characters.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = groupAdapter
        }
        updateItems()
    }

    private fun updateItems()
    {
        viewModel.getItems()
    }

    private fun addItems(items: List<Character>)
    {
        groupAdapter.clear()
        for(i in items)
        {
            groupAdapter.apply {
                add(CharacterItem(
                    character = i,
                    onClick = {
                        setDataInFields(it)
                        currentCharacter = it.id
                    }))

            }
        }
    }

    private fun onClickRx() {
        val observable = Observable.create(RxTest())

        val x = observable.subscribeOn(Schedulers.io())//??? x
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { println(it) }
            .subscribe({
                textView.text = "Next = ${it}"
                println(it)
            },
            {
                textView.text = "Error!!!"
            },
            {
                textView.text = "Complete!!!"
            },
            {
                //dispose
            })
    }

    private fun onClickNext(){

        currentCharacter = if (currentCharacter == maxCharacter) maxCharacter else currentCharacter + 1
        viewModel.getCharacterById(currentCharacter)
        button_next.visibility = View.INVISIBLE
    }

    private fun onClickPrev(){
        currentCharacter = if (currentCharacter == 1) 1 else currentCharacter - 1
        viewModel.getCharacterById(currentCharacter)
        button_next.visibility = View.INVISIBLE
    }

    private fun observeCharacterById()
    {
        viewModel.characterById.observe(this, Observer {
            setDataInFields(it)
            button_next.visibility = View.VISIBLE
        })
    }

    private fun observeCharacters()
    {
        viewModel.characters.observe(this, Observer {
            addItems(it)
            maxCharacter = it.size
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

    private fun getCurrentCharacter(): Character {
        return Character(
            name = textView_name.text.toString(),
            st = textView_st.text.toString().toInt(),
            dx = textView_dx.text.toString().toInt(),
            iq = textView_iq.text.toString().toInt(),
            ht = textView_ht.text.toString().toInt()
        )
    }

    private fun clearFields() {
        textView_id.text = "0"
        textView_name.setText("name")
        textView_st.setText("10")
        textView_dx.setText("10")
        textView_iq.setText("10")
        textView_ht.setText("10")
    }
}
