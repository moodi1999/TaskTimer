package com.example.ahmadreza.tasktimer

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() , CursorRecyclerViewAdaptor.OnTaskClicklistener , AddEditActivityFragment.OnSavedClicked{

    private val TAG = "MainActivity"

    /**
     * whether or not the activity is in 2 pane mode
     * i.e running in lacndscape or a tablet
     */
    private var mTwoPane = false

    companion object {
        private val ADD_EDIT_FRAGMENT = "AddEditFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MainActivity", "onCreate: Start")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        if (findViewById<FrameLayout>(R.id.task_detail_container) != null) {
            // the datail conteiner view will be peresent only in large screen layouts (res/values-sw600dp).
            // If this peresent, then activity shoulb be in two pane mode
            mTwoPane = true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.menumain_setting -> true
            R.id.menumain_addTask -> {
                taskEditRequest(null)
            }
            R.id.menumain_showabout -> true
            R.id.menumain_showDuration -> true
            R.id.menumain_generate -> true
            else -> super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun taskEditRequest(task: Task?) {
        Log.d("MainActivity", "taskEditRequest: Start")
        if (mTwoPane) {
            Log.d("MainActivity", "taskEditRequest: in twopane mode")
            val fragment = AddEditActivityFragment()

            val arguments = Bundle()
            arguments.putSerializable(Task::class.java.simpleName, task)
            fragment.arguments = arguments

            supportFragmentManager.beginTransaction()
                    .replace(R.id.task_detail_container, fragment)
                    .commit()
        } else {
            Log.d("MainActivity", "taskEditRequest: not twopane mode (phone)")
            val detailIntent = Intent(this, AddEditActivity::class.java)
            if (task != null) {
                detailIntent.putExtra(Task::class.java.simpleName, task)
                startActivity(detailIntent)
            }else{
                // adding a new task
                startActivity(detailIntent)
            }

        }
    }

    override fun onEditClick(task: Task) {
        taskEditRequest(task)
    }

    override fun onDeleteClick(task: Task) {
        contentResolver.delete(TaskContract.buildTaskUri(task.m_Id), null, null)
    }

    override fun onSaveClicked() {
        Log.e(TAG, "onSaveClicked :::: Start")
        val fragment: Fragment? = supportFragmentManager.findFragmentById(R.id.task_detail_container)
        if (fragment != null) {
            supportFragmentManager.beginTransaction()
                    .remove(fragment)
                    .commit()
        }
    }
}
