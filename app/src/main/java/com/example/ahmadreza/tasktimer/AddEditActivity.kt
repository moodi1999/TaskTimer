package com.example.ahmadreza.tasktimer

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log

import kotlinx.android.synthetic.main.activity_add_edit.*

class AddEditActivity : AppCompatActivity() , AddEditActivityFragment.OnSavedClicked{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)
        Log.d("AddEditActivity", "onCreate: Start")
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val fragment = AddEditActivityFragment()

        val args = intent.extras
        fragment.arguments = args

        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.fragment, fragment)
        transaction.commit()
    }

    override fun onSaveClicked() {
        finish()
    }
}
